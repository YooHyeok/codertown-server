package io.codertown.web.repository;

import io.codertown.web.entity.Like;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndRecruit(User user, Recruit recruit);
}