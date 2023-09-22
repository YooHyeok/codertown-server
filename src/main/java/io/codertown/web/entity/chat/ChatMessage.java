package io.codertown.web.entity.chat;

import io.codertown.web.entity.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_MESSAGE_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
    private ChatRoom roomId;

    @OneToOne
    @JoinColumn(name = "AUTHOR_ID", insertable = false, updatable = false)
    private User authorId;

    @Column(name = "message")
    private String message;

    @CreatedDate
    @Column
    private LocalDateTime chatSendDate;
}