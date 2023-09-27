package io.codertown.web.service;

import io.codertown.web.dto.ChatMessageDto;
import io.codertown.web.dto.UserDto;
import io.codertown.web.entity.chat.ChatMessage;
import io.codertown.web.entity.chat.ChatRoom;
import io.codertown.web.entity.chat.ChatRoomUser;
import io.codertown.web.entity.user.User;
import io.codertown.web.payload.ChatMessageSaveResult;
import io.codertown.web.payload.request.ChatMessageRequest;
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
        ChatRoom findChatRoom = chatRoomSRepository.findById(request.getRoomId()).orElseThrow();
        User sender = (User) userRepository.findByEmail(request.getSenderId());

        /* 메시지 생성 */
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(findChatRoom)
                .message(request.getMessage())
                .sender(sender)
                .chatSendDate(LocalDateTime.now())
                .isReaded(false)
                .build();

        /* 채팅방회원에서 상대방의 신규 메시지 카운트 증가 */
        ChatRoomUser friendChatRoomUser = findChatRoom.getChatRoomUserList()
                .stream().filter(chatRoomUser -> !chatRoomUser.getChatRoomUser().getEmail().equals(request.getSenderId()))
                .findAny().orElseThrow();
        friendChatRoomUser.incrementNewMsgCount();

        /* 상대방의 신규 메시지 토탈 카운트 증가 */
        User friendUser = friendChatRoomUser.getChatRoomUser();
        friendUser.incrementNewMsgTotalCount();

        /* 메시지 저장 */
        ChatRoom chatRoom = findChatRoom.updateChatRoom(chatMessage);

        UserDto findSenderDto = chatRoom.getChatRoomUserList()
                .stream()
                .filter(chatRoomUser -> chatRoomUser.getChatRoomUser().getEmail().equals(request.getSenderId()))
                .map(chatRoomUser -> UserDto.userEntityToDto(chatRoomUser.getChatRoomUser())).findFirst().orElseThrow();
        return ChatMessageSaveResult.builder()
                .sender(findSenderDto)
                .message(chatMessage.getMessage())
                .chatSendDate(chatMessage.getChatSendDate())
                .build();
    }

    public List<ChatMessageDto> findChatMessageList(String roomId) {

        ChatRoom chatRoom = chatRoomSRepository.findById(roomId).orElseThrow();
        List<ChatMessageDto> chatMessageDtoList = chatMessageRepository.findByChatRoom(chatRoom).stream().map(chatMessage ->
                ChatMessageDto.builder()
                        .sender(UserDto.userEntityToDto(chatMessage.getSender()))
                        .message(chatMessage.getMessage())
                        .chatSendDate(chatMessage.getChatSendDate())
                        .build())
                .collect(Collectors.toList());

        return chatMessageDtoList;
    }

}
