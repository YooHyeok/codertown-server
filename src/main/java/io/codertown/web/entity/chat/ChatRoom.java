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
import java.util.ArrayList;
import java.util.List;
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

    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @JoinColumn(name = "LAST_CHAT_MESSAGE_NO")
    private ChatMessage chatMessage;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    @OneToOne
    @JoinColumn(name = "PROJECT_PART_NO")
    private ProjectPart projectPart;

    @CreatedDate
    @Column
    private LocalDateTime createdDate;

    public static ChatRoom createChatRoom(User roomMaker, User guest, Project project, ProjectPart projectPart) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomUserList(new ArrayList<>())
                .project(project)
                .projectPart(projectPart)
                .build();
        ChatRoomUser.builder().build().addMembers(newChatRoom, roomMaker, guest);
        return newChatRoom;
    }


}