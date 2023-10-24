package io.codertown.web.repository;

import io.codertown.web.entity.coggle.Notification;
import io.codertown.web.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByNotifyUser(User findUser, PageRequest pageRequest);
}
