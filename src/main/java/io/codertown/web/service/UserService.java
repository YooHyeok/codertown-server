package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.support.base.CommonLoggerComponent;
import io.codertown.support.jwt.JwtTokenProvider;
import io.codertown.web.dto.*;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.SignInResult;
import io.codertown.web.payload.SignStatus;
import io.codertown.web.payload.request.SignInRequest;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.payload.request.UserUpdateRequest;
import io.codertown.web.payload.response.JoinedProjectResponse;
import io.codertown.web.payload.response.SignInResponse;
import io.codertown.web.repository.ProjectRepository;
import io.codertown.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * *****************************************************<p>
 * 패키지:io.codertown.user<p>
 * 파일 : UserService.java<p>
 * 프로그램 설명 : 회원 서비스 클래스<p>
 * 연관테이블 : user<p>
 * 담당 : 유재혁<p>
 * *****************************************************<p>
 */
@Service
@Transactional(readOnly = true)
public class UserService extends CommonLoggerComponent implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ProjectRepository projectRepository, @Lazy JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * Eamil 중복 확인
     * @param email Client Text/Plain 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return Boolean - true : 중복
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 회원 가입
     * @param request Client 요청 DTO 객체
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     *    password : 비밀번호 <br/>
     *    nickname : 닉네임 <br/>
     * profileIcon : 프로필 아이콘 <br/>
     *      gender : 성별
     * </pre>
     * @return Boolean 저장 성공/실패 여부
     * @throws RuntimeException 저장중 닉네임 불일치 저장실패 예외
     */
    @Transactional(readOnly = false)
    public SignStatus signUp(SignUpRequest request) throws IOException {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        setNickname(request); //닉네임 중복 체크 및 난수 부여 메소드
        User user = User.userDtoToEntity(request);
        SignStatus responseStatus = new SignStatus();
        try {
            String savedNickname = userRepository.save(user).getNickname();
            if(savedNickname != user.getNickname() || savedNickname.isEmpty()) {
                throw new RuntimeException("nickname mismatch save failed");
            }
            responseStatus.setSuccessResult(responseStatus); //성공코드 객체반환
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus.setFailResult(responseStatus); //실패코드 객체반환
        }
        return responseStatus;
    }

    /**
     * 로그인
     * @param request Client 요청 DTO 객체
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     *    password : 비밀번호
     * </pre>
     * @return SignUpResponse - [로그인정보/성공여부]
     * @throws RuntimeException 저장중 닉네임 불일치 저장실패 예외
     */
    public SignInResponse signIn(SignInRequest request) {
        SignStatus statusResponse = SignStatus.builder().build();
        SignInResult signInInfo = SignInResult.builder().build();
        try {
            User user = (User) loadUserByUsername(request.getEmail());
            boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (user == null || !matches) { //사용자 정보가 null이거나 패스워드가 일치하지 않으면 Exception 후 catch이동
                throw new RuntimeException("User information entered is incorrect");
            }
            signInInfo = SignInResult
                    .builder()
                    .createToken(jwtTokenProvider.createToken(user.getNickname(), user.getRoles()))
                    .refreshToken(jwtTokenProvider.refreshToken(user.getNickname(), user.getRoles()))
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
            LOGGER.info("UserService signInResult: {}", signInInfo);
            statusResponse.setSuccessResult(statusResponse);

        } catch (Exception e) {
            e.printStackTrace();
            statusResponse.setFailResult(statusResponse);
        }
        return SignInResponse.builder()
                .signInResult(signInInfo)
                .signStatus(statusResponse)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    /**
     * 회원가입시 닉네임 중복여부 확인 및 난수부여 메소드 <br/>
     * 1) @기준으로 email 문자열을 split한 후 앞자리 ID를 추출 <br/>
     * 2) 추출한 ID가 닉네임으로 존재하는지 여부 확인 <br/>
     * 3) 1.중복이면 LOOP | 2.중복이아니면 그대로 저장 <br/>
     * 3-1) 중복이면 LOOP안에서 난수를 부여한다.<br/>
     * 3-2) 난수를 부여한 닉네임이 중복인지 확인 <br/>
     * 3-3) 닉네임이 중복이 아니면 변수에 저장후 loop탈출 <br/>
     * 3-3) 닉네임이 중복이면 loop의 처음인 3-1로 재귀
     * @param request
     * @return
     */
    private SignUpRequest setNickname(SignUpRequest request) {
        String splitNickname = request.getEmail().split("@")[0]; // 1. email split
        Boolean existResult = existsByNickname(splitNickname); //2. 중복체크
        String completedNickname = null; //완료된 닉네임

        while (existResult) { // 3. 중복이면 난수 루프 시작
            String randomNickname = splitNickname + (int)(Math.random()*1000); //난수 부여
            if(!userRepository.existsByNickname(randomNickname)){ //난수부여한 닉네임이 중복이아니면
                completedNickname = randomNickname;
                break;
            }
            continue; // 난수를 부여한 닉네임이 중복이면 loop continue
        }
        if(!existResult) completedNickname = splitNickname; // 3.중복이 아니면 그대로 저장
        request.setNickname(completedNickname);
        return request;
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return Boolean
     */
    public Boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 마이페이지 - 회원정보 반환 메소드 <br/>
     * @param loginEmail Client 파라미터
     * <pre>
     *       email : 이메일 (로그인계정) <br/>
     * </pre>
     * @return UserDto - [회원정보]
     */
    public UserDto userInfo(String loginEmail) {
        User user = (User)loadUserByUsername(loginEmail);
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileUrl())
                .gender(user.getGender())
                .role(user.getRolesToString())
                .build();
        return userDto;
    }

    /**
     * 마이페이지 - 회원 수정 메소드
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public UserDto userUpdate(UserUpdateRequest request) {
        try {
            if(!request.getPassword().equals("")) {
                /* 패스워드 암호화 */
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                request.setPassword(encodedPassword);
            }
            /* 변경감지 구현 */
            User findUser = (User)userRepository.findByEmail(request.getLoginEmail());
            findUser.updateUser(request);

            UserDto userDto = UserDto.builder().build().userEntityToDto(findUser);
            return userDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 참여중인 프로젝트 목록 출력 메소드
     * @param page
     * @param size
     * @param loginEmail
     * @return
     */
    public JoinedProjectResponse findJoinedProject(Integer page, Integer size, String loginEmail) {
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build().createPageRequest(page, size, "id", "DESC");
        try {


            User loginUser = (User)loadUserByUsername(loginEmail);
            Page<JoinedProjectSimpleConvertDto> pages = projectRepository.findJoinedProject(loginUser, pageInfo.getPageRequest());
            pageInfo.setPageInfo(pages, pageInfo);
            List<JoinedProjectResponseDto> projectList = pages.getContent().stream().map(joinedProjectDto -> {
                        /* 프로젝트 파트 리스트 DTO 변환 */
                        List<ProjectPartSaveDto> projectPartList = joinedProjectDto.getProject().getProjectParts().stream()
                                .map(projectPart -> {
                                    /* 프로젝트별 참여중인 회원 리스트 DTO변환  */
                                    List<UserProjectDto> userProjectDtoList = projectPart.getUserProjects().stream().map(userProject ->
                                            UserProjectDto.builder().build().convertEntityToDto(userProject)
                                    ).collect(Collectors.toList());
                                    return ProjectPartSaveDto.builder().build().entityToDto(projectPart, userProjectDtoList);
                                })
                                .collect(Collectors.toList());
                        /* Projet dto 변환 */
                        ProjectDto projectDto = ProjectDto.builder().build().entityToDto(joinedProjectDto.getProject(), projectPartList);
                        /* PartDto 변환 */
                        PartDto partDto = PartDto.builder()
                                .partNo(joinedProjectDto.getProjectPart().getPart().getId())
                                .partNmae(joinedProjectDto.getProjectPart().getPart().getPartName()).build();
                        return JoinedProjectResponseDto.builder().projectDto(projectDto).partDto(partDto).build();
                    }
            ).collect(Collectors.toList());

            return JoinedProjectResponse.builder()
                    .projectList(projectList)
                    .pageInfo(pageInfo)
                    .articleCount(pages.getTotalElements())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] profileImage(String loginEmail) throws Exception {
        User user = (User)loadUserByUsername(loginEmail);
        return user.getProfileUrl();
    }
}
