package io.codertown.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DirectMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate; //원하는 시점에 클라이언트 측에 메시지 전송을 할 수 있다.

    /**
     * 프로젝트 참여 요청 WebSocket API
     * @param requsetId
     */
    @MessageMapping("/project-request")
    @SendTo("/") //원하는 시점에 메시지를 전송하는 SimpMessagingTemplate와는 다르게 클라이언트가 메시지를 보낸 요청에 대한 응답으로 메시지를 보내는데 사용한다.
    public void projectRequest(@Payload Long requsetId) {
        System.out.println("requsetId = " + requsetId);
        // 신청자 회원 정보 조회
        // 채팅방 생성 및 신청자, 수신자 등록
        // 신청자 입장 메시지 반환 "닉네임" 님이 프로젝트 참여를 신청하셧습니다.
//        simpMessagingTemplate.convertAndSend("client - URL", message);
//        receivedMessage(message);
    }

}
