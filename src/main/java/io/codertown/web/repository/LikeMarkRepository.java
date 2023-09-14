package io.codertown.web.repository;

import io.codertown.web.entity.recruit.BookMark;
import io.codertown.web.entity.recruit.Recruit;
import io.codertown.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeMarkRepository extends JpaRepository<BookMark, Long> {

    Optional<BookMark> findByUserAndRecruit(User user, Recruit recruit);
}