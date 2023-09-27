package io.codertown.web.repository;

import io.codertown.web.entity.chat.ChatMessage;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    @Modifying
    @Query("UPDATE ChatMessage c SET c.isReaded = true WHERE c.chatRoom =:chatRoom AND c.sender =:friend")
    void bulkUpdateIsReadedByChatRoomAndSender(@Param("chatRoom") ChatRoom chatRoom, @Param("friend") User friend);
}
