package io.codertown.web.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByNickname(String nickname);

    UserDetails findByEmail(String email);
}
