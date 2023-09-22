package io.codertown.web.entity.chat;

import io.codertown.web.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "chatRoomUser")
@Entity
public class ChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ROOM_USERS_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CHAT_ROOM_NO")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User chatRoomUser;

    @Column(name = "IS_ROOM_MAKER")
    private Boolean isRoomMaker;

    public void addMembers(ChatRoom newChatRoom, User roomMaker, User guest) {
        ChatRoomUser roomMakerUser = createChatRoomMakerUser(newChatRoom, roomMaker);
        ChatRoomUser guestUser = createChatRoomGuestUser(newChatRoom, guest);
        newChatRoom.getChatRoomUserList().add(roomMakerUser);
        newChatRoom.getChatRoomUserList().add(guestUser);
    }

    public static ChatRoomUser createChatRoomMakerUser(ChatRoom newChatRoom, User user) {
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(newChatRoom)
                .chatRoomUser(user)
                .isRoomMaker(true)
                .build();
        user.getChatRoomUserList().add(chatRoomUser);
        return chatRoomUser;
    }

    public static ChatRoomUser createChatRoomGuestUser(ChatRoom newChatRoom, User user) {
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(newChatRoom)
                .chatRoomUser(user)
                .isRoomMaker(false)
                .build();
        user.getChatRoomUserList().add(chatRoomUser);
        return chatRoomUser;
    }
}
