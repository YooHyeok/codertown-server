package io.codertown.support.interceptor;

import io.codertown.web.service.ChatRoomService;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate; //순환참조 방지

    public StompHandler(ChatRoomService chatRoomService, @Lazy SimpMessagingTemplate simpMessagingTemplate) {
        this.chatRoomService = chatRoomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("accessor = " + accessor);
        if (accessor.containsNativeHeader("connectedRoomId")) {
            // 첫 번째로 해당 요소의 존재 여부를 확인
            String connectedRoomId = accessor.getFirstNativeHeader("connectedRoomId");
            String connectedUserEmail = accessor.getFirstNativeHeader("connectedUserEmail");
            Boolean connectedValue = false;

            switch (accessor.getCommand()) {
                case SUBSCRIBE: //채팅방 입장
                    connectedValue = true;
                    chatRoomService.changeConnected(connectedRoomId, connectedUserEmail, connectedValue);
                    break;
                case UNSUBSCRIBE:
                    connectedValue = false;
                    chatRoomService.changeConnected(connectedRoomId, connectedUserEmail, connectedValue);
            }
            String url = "/connected/room." + connectedRoomId + "/user." + connectedUserEmail;
            simpMessagingTemplate.convertAndSend(url, connectedValue);
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
