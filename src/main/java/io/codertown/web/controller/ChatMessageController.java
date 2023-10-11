package io.codertown.web.controller;

import io.codertown.web.dto.ChatMessageDto;
import io.codertown.web.payload.ChatMessageSaveResult;
import io.codertown.web.payload.request.ChatMessageRequest;
import io.codertown.web.payload.request.ProjectJoinRequest;
import io.codertown.web.service.ChatMessageService;
import io.codertown.web.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final static String CHAT_EXCHANGE_URL = "/sub/room.";
    private final static String IS_CONFIRM_ROOM_URL = "/confirm/room.";
    private final static String IS_CONFIRM_USER_URL = "/user.";


    private final SimpMessagingTemplate simpMessagingTemplate; //원하는 시점에 클라이언트 측에 메시지 전송을 할 수 있다.
    private final ChatMessageService chatMessageService;
    private final RecruitService recruitService;


    /* 요청 수락 Web Socket API */
    @MessageMapping("/chat.confirm")
    public void sendIsConfirm(ProjectJoinRequest request){
        String url = IS_CONFIRM_ROOM_URL + request.getChatRoomNo() + IS_CONFIRM_USER_URL + request.getRequesterEmail();
        recruitService.projectJoinConfirm(request);
        simpMessagingTemplate.convertAndSend(url, true); // 채팅방1번으로 연결 완료 data전송
    }

    /**
     * 프로젝트 참여 요청 WebSocket API
     * @param request
     */
    @MessageMapping("/chat.message")
    public void send(ChatMessageRequest request) {
        ChatMessageSaveResult result = chatMessageService.createChatMessage(request);
        simpMessagingTemplate.convertAndSend(CHAT_EXCHANGE_URL + request.getRoomId(),result);
    }

    @PostMapping("/chat-message-list")
    public ResponseEntity<List<ChatMessageDto>> messageList(String roomNo) {
        try {
            List<ChatMessageDto> chatMessageList = chatMessageService.findChatMessageList(roomNo);

            return ResponseEntity.ok(chatMessageList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
