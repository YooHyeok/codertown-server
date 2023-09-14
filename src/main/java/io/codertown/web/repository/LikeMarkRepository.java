package io.codertown.web.repository;

import io.codertown.web.entity.recruit.LikeMark;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeMarkRepository extends JpaRepository<LikeMark, Long> {

    Optional<LikeMark> findByUserAndRecruit(User user, Recruit recruit);
}