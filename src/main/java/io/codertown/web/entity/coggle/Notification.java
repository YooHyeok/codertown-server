package io.codertown.web.entity.coggle;

import io.codertown.web.entity.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_NO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User notifyUser; //알림 받을 사용자
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COGGLE_NO")
    private Coggle coggle;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_NO")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private ReplyConditionEnum replyCondition; //댓글 유형
    private Boolean isCliked;

    @CreatedDate
    @Column
    private LocalDateTime firstRegDate;

    public static Notification createNotification(User notifyUser, Coggle coggle, Comment comment, ReplyConditionEnum replyCondition) {
        Notification createdNotification = Notification.builder()
                .notifyUser(notifyUser)
                .coggle(coggle)
                .comment(comment)
                .replyCondition(replyCondition)
                .isCliked(false)
                .build();
        notifyUser.relatedNotificationSet(createdNotification); /* User엔티티에서 양방향 연관관계 추가 */
        notifyUser.incrementNewNotifyCount();/* 새소식 증가 */
        return  createdNotification;
    }

    public void notifyChangeClicked() {
        this.isCliked = true;
    }

}
