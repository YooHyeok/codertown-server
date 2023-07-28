package io.codertown.web.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 닉네임 중복확인
     * @param nickname
     * @return
     */
    Boolean existsByNickname(String nickname);

    /**
     * 로그인 계정으로 사용자정보 조회
     * @param email
     * @return
     */
    UserDetails findByEmail(String email);
}
