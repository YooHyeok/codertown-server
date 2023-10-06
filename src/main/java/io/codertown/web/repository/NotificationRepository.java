package io.codertown.web.repository;

import io.codertown.web.entity.coggle.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
