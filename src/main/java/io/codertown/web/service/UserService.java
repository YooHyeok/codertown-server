package io.codertown.web.service;

import io.codertown.support.PageInfo;
import io.codertown.support.base.CommonLoggerComponent;
import io.codertown.support.jwt.JwtTokenProvider;
import io.codertown.web.dto.*;
import io.codertown.web.entity.coggle.Notification;
import io.codertown.web.entity.project.PersonalStatusEnum;
import io.codertown.web.entity.project.TotalStatusEnum;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.SignInResult;
import io.codertown.web.payload.SignStatus;
import io.codertown.web.payload.SuccessBooleanResult;
import io.codertown.web.payload.request.SignInRequest;
import io.codertown.web.payload.request.SignUpRequest;
import io.codertown.web.payload.request.UserDisabledRequest;
import io.codertown.web.payload.request.UserUpdateRequest;
import io.codertown.web.payload.response.JoinedProjectDetailResponse;
import io.codertown.web.payload.response.JoinedProjectResponse;
import io.codertown.web.payload.response.NotificationResponse;
import io.codertown.web.payload.response.SignInResponse;
import io.codertown.web.repository.NotificationRepository;
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
import java.util.Optional;
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
    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ProjectRepository projectRepository, NotificationRepository notificationRepository, @Lazy JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.notificationRepository = notificationRepository;
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
     * 회원 비활성화 (자진탈퇴, 관리자에 의한 정지)
     * @param request Client 요청 DTO 객체
     * <pre>
     *       loginEmail : 이메일 (로그인계정) <br/>
     *    changeStatus : 변경할 상태 <br/>
     * </pre>
     * @return Boolean 저장 성공/실패 여부
     * @throws RuntimeException 저장중 닉네임 불일치 저장실패 예외
     */
    @Transactional(readOnly = false)
    public SuccessBooleanResult changeStatusAccount(UserDisabledRequest request) throws IOException {
        try {

            /* 변경감지 구현 */
            User findUser = (User)userRepository.findByEmail(request.getLoginEmail());
            findUser.changeStatusAccount(request.getChangeStatus());
            findUser.getRecruitUsers().forEach(recruit -> recruit.deleteRecruit(true));
            findUser.getCoggleUsers().forEach(coggle -> coggle.deleteCoggle(true));
            findUser.getCommentUsers().forEach(comment -> comment.deleteComment(true));
            return SuccessBooleanResult.builder().build().setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    .createToken(jwtTokenProvider.createToken(user.getEmail(), user.getRoles()))
                    .refreshToken(jwtTokenProvider.refreshToken(user.getEmail(), user.getRoles()))
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
    public SuccessBooleanResult userUpdate(UserUpdateRequest request) {
        try {
            if(!request.getPassword().equals("")) {
                /* 패스워드 암호화 */
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                request.setPassword(encodedPassword);
            }
            /* 변경감지 구현 */
            User findUser = (User)userRepository.findByEmail(request.getLoginEmail());

            /* 패스워드 일치 여부 확인 */
            boolean matches = passwordEncoder.matches(request.getOriginalPassword(), findUser.getPassword());
            if (!matches){
                return SuccessBooleanResult.builder().build().setResult(matches);
            }

            findUser.updateUser(request);

            return SuccessBooleanResult.builder().build().setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 참여중인 프로젝트 목록 출력 메소드 <br/>
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
                        PersonalStatusEnum personalStatus = joinedProjectDto.getUserProject().getPersonalStatus();
                        ProjectDto projectDto = ProjectDto.builder().build().entityToDto(joinedProjectDto.getProject(), null);
                        /* PartDto 변환 */
                        PartDto partDto = PartDto.builder()
                                .partNo(joinedProjectDto.getProjectPart().getPart().getId())
                                .partNmae(joinedProjectDto.getProjectPart().getPart().getPartName()).build();
                        return JoinedProjectResponseDto.builder().projectDto(projectDto).partDto(partDto).personalStatus(personalStatus)
                                .build();
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

    /**
     * 참여중인 프로젝트 갯수 조회 메소드 <br/>
     * @param loginEmail
     * @return
     */
    public Long findJoinedProjectCount(String loginEmail) {
        try {
            User loginUser = (User)loadUserByUsername(loginEmail);
            Long count = projectRepository.findJoinedProject(loginUser)
                    .stream()
                    .filter(joinedProjectDto ->
//                            joinedProjectDto.getProject().getCokkiri().getStatus().equals(false) &&
                            (joinedProjectDto.getProject().getProjectStatus().equals(TotalStatusEnum.RECURUIT)
                                    || joinedProjectDto.getProject().getProjectStatus().equals(TotalStatusEnum.RUN))
                    ).count();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 프로젝트 인원현황 상세보기 <br/>
     * 프로젝트 파트 리스트 및 프로젝트 파트별 참여자 현황 조회 <br/>
     * @param projectNo
     * @return
     */
    public JoinedProjectDetailResponse joinedProjectDetail(Long projectNo) {
        /* 프로젝트 파트 리스트 DTO 변환 */
        List<ProjectPartDetailDto> projectPartList = projectRepository.findById(projectNo).orElseThrow().getProjectParts().stream()
                .map(projectPart -> {
                    /* 프로젝트별 참여중인 회원 리스트 DTO변환  */
                    List<UserProjectDto> userProjectDtoList = projectPart.getUserProjects().stream()
                            .map(userProject ->
                            UserProjectDto.builder().build().convertEntityToDto(userProject)
                    ).collect(Collectors.toList());
                    return ProjectPartDetailDto.builder().build().entityToDto(projectPart, userProjectDtoList);
                })
                .collect(Collectors.toList());
        return JoinedProjectDetailResponse.builder().projectPartList(projectPartList).build();

    }

    /**
     * 로그인 후 단건 프로필 이미지 조회
     * @param loginEmail
     * @return 프로필 이미지 바이트배열 (바이너리)
     * @throws Exception
     */
    public byte[] profileImage(String loginEmail) throws Exception {
        User user = (User)loadUserByUsername(loginEmail);
        return user.getProfileUrl();
    }

    /**
     * Notification-새소식 댓글 신규 카운트 조회
     * @param loginEmail
     * @return
     */
    public Long myNewNotifyCount(String loginEmail) {
        User findUser = (User)userRepository.findByEmail(loginEmail);
        return findUser.getNewNotifyCount();
    }

    /**
     * Notification-새소식 댓글 목록 조회
     * @param loginId
     * @param page
     * @param size
     * @return
     */
    public NotificationResponse myNotificationList(String loginId, Integer page, Integer size) throws Exception {
        User findUser = (User)userRepository.findByEmail(loginId);
        page = page == null ? 1 : page;
        PageInfo pageInfo = PageInfo.builder().build().createPageRequest(page, size, "firstRegDate", "DESC");
        Page<Notification> notifyPagenation = notificationRepository.findByNotifyUser(findUser, pageInfo.getPageRequest());
        List<NotificationDto> notificationDtoList = notifyPagenation.getContent().stream().map(notification ->
                NotificationDto.builder()
                        .notificationNo(notification.getId())
                        .replyCondition(notification.getReplyCondition().name())
                        .commentNo(notification.getComment().getId())
                        .writerNickname(notification.getComment().getWriter().getNickname())
                        .profileUrl(notification.getComment().getWriter().getProfileUrl())
                        .mentionNickname(Optional.ofNullable(notification.getComment().getMention()).isEmpty() ? null :  notification.getComment().getMention().getNickname())
                        .commentContent(notification.getComment().getContent())
                        .firstRegDate(notification.getComment().getFirstRegDate())
                        .coggleNo(notification.getCoggle().getId())
                        .coggleTitle(notification.getCoggle().getTitle())
                        .isClicked(notification.getIsCliked())
                        .build()

        ).collect(Collectors.toList());
        return NotificationResponse.builder()
                .notificationDtoList(notificationDtoList)
                .build();
    }

    /**
     * 새소식 카운트 초기화 <br/>
     * 변경감지
     * @param loginEmail
     */
    @Transactional(readOnly = false)
    public void initNewNotifyCount(String loginEmail) {
        User findUser = (User)userRepository.findByEmail(loginEmail);
        findUser.initNewNotifyCount();
    }

    /**
     * 새소식 클릭 여부 <br/>
     * 변경감지
     * @param notificationNo
     */
    @Transactional(readOnly = false)
    public void notifyChangeClicked(Long notificationNo) {
        Notification notification = notificationRepository.findById(notificationNo).orElseThrow();
        notification.notifyChangeClicked();
    }

}
