package io.codertown.web.repository;

import io.codertown.web.entity.coggle.Coggle;
import io.codertown.web.entity.coggle.LikeMark;
import io.codertown.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeMarkRepository extends JpaRepository<LikeMark, Long> {

    Optional<LikeMark> findByUserAndCoggle(User user, Coggle coggle);
}