package io.codertown.web.user;

import io.codertown.support.base.CommonLoggerComponent;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService extends CommonLoggerComponent implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * 회원 가입
     * @param requestDto Client 요청 DTO 객체
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
    public Boolean signUp(CreateUserRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(encodedPassword);
        User user = User.userDtoToEntity(requestDto);
        try {
            String savedNickname = userRepository.save(user).getNickname();
            if(savedNickname != user.getNickname()) throw new RuntimeException("nickname mismatch save failed");
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
