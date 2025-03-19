package com.example.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {



    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                // securityconfig에서의 cors예외는 http요청에 대한 예외. 따라서 websocket 프로토콜에 대한 요청에 대해서는 별도의 cors설정 필요.
                .setAllowedOrigins()
                // ws:// 가 아닌 http:// 엔드포인트를 사용할수 있게 해주는 sockJs라이브러를 통한 요청을 허용하는 설정.
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        /publish/1형태로 메시지 발행해야 함을 설정
//        /publish로 시작하는 url패턴으로 메시지가 발행되면 @Controller 객체의 @MessaMapping메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");

//        /topic/1형태로 메시지를 수신(subscribe)해야 함을 설정
        registry.enableSimpleBroker("/topic");

    }
}
