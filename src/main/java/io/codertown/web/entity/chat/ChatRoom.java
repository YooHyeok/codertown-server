package io.codertown.web.entity.chat;

import io.codertown.web.entity.ProjectPart;
import io.codertown.web.entity.project.Project;
import io.codertown.web.entity.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@DynamicUpdate
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class ChatRoom {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "CHAT_ROOM_NO")
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "LAST_CHAT_MESSAGE_NO")
    private ChatMessage chatMessage;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "CHAT_ROOM_USERS",
            joinColumns = @JoinColumn(name = "CHAT_ROOM_NO"),
            inverseJoinColumns = @JoinColumn(name = "USER_NO"))
    private Set<User> chatRoomUsers = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    @OneToOne
    @JoinColumn(name = "PROJECT_PART_NO")
    private ProjectPart projectPart;

    @CreatedDate
    @Column
    private LocalDateTime createdDate;

}