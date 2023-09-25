package io.codertown.web.controller;

import io.codertown.web.dto.ChatMessageDto;
import io.codertown.web.payload.ChatMessageSaveResult;
import io.codertown.web.payload.request.ChatMessageRequest;
import io.codertown.web.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final static String CHAT_EXCHANGE_NAME = "/sub/room.";

    private final SimpMessagingTemplate simpMessagingTemplate; //원하는 시점에 클라이언트 측에 메시지 전송을 할 수 있다.
    private final ChatMessageService chatMessageService;



    /* 연결 성공시 해당 접속 완료 알림 전송 */
    @MessageMapping("/chat/connect")
    public void sendConnectMsg(@Payload Map<String,Object> data){
        simpMessagingTemplate.convertAndSend("/connected-success",data); // 채팅방1번으로 연결 완료 data전송
    }

    /**
     * 프로젝트 참여 요청 WebSocket API
     * @param request
     */
    @MessageMapping("/chat.message")
    public void send(ChatMessageRequest request) {
        ChatMessageSaveResult result = chatMessageService.createChatMessage(request);
        simpMessagingTemplate.convertAndSend(CHAT_EXCHANGE_NAME + request.getRoomId(),result);
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
