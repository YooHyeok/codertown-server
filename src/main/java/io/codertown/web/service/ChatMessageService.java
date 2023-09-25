package io.codertown.web.service;

import io.codertown.web.payload.ChatMessageSaveResult;
import io.codertown.web.dto.ChatMessageDto;
import io.codertown.web.payload.request.ChatMessageRequest;
import io.codertown.web.dto.UserDto;
import io.codertown.web.entity.chat.ChatMessage;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.user.User;
import io.codertown.web.repository.ChatMessageRepository;
import io.codertown.web.repository.ChatRoomRepository;
import io.codertown.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRoomRepository chatRoomSRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public ChatMessageSaveResult createChatMessage(ChatMessageRequest request) {
        System.out.println("request = " + request);
        ChatRoom findChatRoom = chatRoomSRepository.findById(request.getRoomId()).orElseThrow();
        User sender = (User) userRepository.findByEmail(request.getSenderId());

        ChatMessage chatMessage = ChatMessage.builder().chatRoom(findChatRoom).message(request.getMessage()).sender(sender).chatSendDate(LocalDateTime.now()).build();

        ChatRoom chatRoom = chatRoomSRepository.findById(chatMessage.getChatRoom().getId()).orElseThrow();
        chatRoom.updateChatRoom(chatMessage);
        UserDto friend = chatRoom.getChatRoomUserList().stream()
                .filter(m -> m.getChatRoomUser().getEmail() != request.getSenderId())
                .map(chatRoomUser -> UserDto.userEntityToDto(chatRoomUser.getChatRoomUser()))
                .collect(Collectors.toList())
                .get(0);
        return ChatMessageSaveResult.builder()
                .sender(friend)
                .message(chatMessage.getMessage())
                .chatSendDate(chatMessage.getChatSendDate())
                .build();
    }

    public ChatMessage findById(long chatMessageId) {
        return chatMessageRepository.findById(chatMessageId).orElseThrow();
    }

    public List<ChatMessageDto> findChatMessageList(String roomId) {

        ChatRoom chatRoom = chatRoomSRepository.findById(roomId).orElseThrow();
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoom(chatRoom);
        List<ChatMessageDto> chatMessageDtoList = chatMessageList.stream().map(chatMessage ->
                ChatMessageDto.builder()
                        .sender(UserDto.userEntityToDto(chatMessage.getSender()))
                        .message(chatMessage.getMessage())
                        .chatSendDate(chatMessage.getChatSendDate())
                        .build())
                .collect(Collectors.toList());

        return chatMessageDtoList;
    }

}
