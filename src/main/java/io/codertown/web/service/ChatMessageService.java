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
        /* 메시지 저장 */
        ChatRoom chatRoom = findChatRoom.updateChatRoom(chatMessage);

        /* 저장된 채팅중 상대방이 보낸 메시지 필터링 후 신규 메시지 갯수 조회 */
        Long newMsgcount = chatRoom.getChatMessage().stream().filter(chatMessage1 -> chatMessage1.getIsReaded() == false).count();

        /* 전송자의 아이디와 일치하지 않는 채팅참여자 정보 조회 */
        ChatRoomUser findChatRoomUser = chatRoom.getChatRoomUserList()
                .stream().filter(chatRoomUser -> !chatRoomUser.getChatRoomUser().getEmail().equals(request.getSenderId())).findAny().orElseThrow();
        /* 채팅별 참여회원중 수신자의 신규채팅 카운트 초기화. */
        findChatRoomUser.incrementNewMsgCount(newMsgcount); // 채팅수 초기화

        /* 전송자의 아이디와 일치하지 않는 회원 정보 조회 */
        User friend = findChatRoomUser.getChatRoomUser();

        /* 해당 회원이 참여중인 모든 채팅방의 메시지중 읽지않은 채팅의 갯수 총합 */
        Long newMsgTotalcount = friend.getChatRoomUserList().stream().map(chatRoomUser -> {
            Long sum = 0L;
            sum += chatRoomUser.getChatRoom().getChatMessage().stream().filter(chatMessage1 -> chatMessage1.getIsReaded() == false).count();
            return sum;
        }).findAny().orElseThrow();

        /* 수신자의 신규채팅 토탈 카운트 증가 */
        friend.incrementNewMsgTotalCount(newMsgTotalcount);


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

    public ChatMessage findById(long chatMessageId) {
        return chatMessageRepository.findById(chatMessageId).orElseThrow();
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
