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


    public Boolean signUp(CreateUserRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        LOGGER.info("인코딩된 패스워드 : {}",encodedPassword);
        requestDto.setPassword(encodedPassword);
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
