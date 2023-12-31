package io.codertown.support.configuration;

import io.codertown.support.interceptor.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker //웹 소켓 서버 사용 설정
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    /**
     *  WebSocket 설정 메소드 <br/>
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/apic") //엔드포인트 등록 - apic테스트용
                .setAllowedOrigins("*"); // 웹 소켓 연결을 허용할 원본(Origin) 설정 (CROS 정책 설정)
//                .withSockJS();

        registry.addEndpoint("/ws") //엔드포인트 등록 - 해당 경로를 통해 Websocket연결
//                .setAllowedOrigins("*")
                .setAllowedOrigins("http://localhost:80")
//                .setAllowedOriginPatterns("http://localhost:*")
                .withSockJS(); //WebSocket을 지원하지 않는 브라우저의 대체수단
    }

    /**
     * 브로커 설정 메소드 <br/>
     * enableSimpleBroker : 메시지 브로커 활성화 및 수신 경로 지정 <br/>
     * setApplicationDestinationPrefixes : 메시지를 발행(publish)할 때의 경로(prefix) 설정 <br/>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub", "/confirm", "/connected"); //송신
        registry.setApplicationDestinationPrefixes("/pub"); //수신
    }

    // 클라이언트 인바운드 채널을 구성하는 메서드
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // stompHandler를 인터셉터로 등록하여 STOMP 메시지 핸들링을 수행
        registration.interceptors(stompHandler);
    }
}
