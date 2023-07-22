package io.codertown.web.user;

import io.codertown.support.base.CommonLoggerComponent;
import io.codertown.support.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
public class UserService extends CommonLoggerComponent implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, @Lazy JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public SignUpResult signUp(SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        existsNickname(request); //닉네임 중복 체크 및 난수 부여 메소드
        User user = User.userDtoToEntity(request);
        SignUpResult signUpResult = new SignUpResult();
        try {
            String savedNickname = userRepository.save(user).getNickname();
            if(savedNickname != user.getNickname() || savedNickname.isEmpty()) {
                throw new RuntimeException("nickname mismatch save failed");
            }
            setSuccessResult(signUpResult); //성공코드 객체반환
        } catch (Exception e){
            e.printStackTrace();
            setFailResult(signUpResult); //실패코드 객체반환
        }
        return signUpResult;
    }

    private void setSuccessResult(SignUpResult result) {
       result.setSuccess(true);
       result.setCode(CommonResponse.SUCCESS.getCode());
       result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResult result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    public SignInResult signIn(SignInRequest request) {
        SignInResult signInResult;
        try {
            User user = (User) loadUserByUsername(request.getEmail());
            boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (user == null || !matches) { //사용자 정보가 null이거나 패스워드가 일치하지 않으면 Exception 후 catch이동
                throw new RuntimeException("User information entered is incorrect");
            }
            signInResult = SignInResult
                    .builder()
                    .token(jwtTokenProvider.createToken(user.getNickname(), user.getRoles()))
                    .build();
            setSuccessResult(signInResult);
            return signInResult;
        } catch (Exception e) {
            e.printStackTrace();
            signInResult = SignInResult.builder().build();
            setFailResult(signInResult);
        }
        return signInResult;
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
    private SignUpRequest existsNickname(SignUpRequest request) {
        String splitNickname = request.getEmail().split("@")[0]; // 1. email split
        Boolean existResult = userRepository.existsByNickname(splitNickname); //2. 중복체크
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
}
