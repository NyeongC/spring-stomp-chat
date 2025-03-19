package com.example.chatserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")  //해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    //    DestinationVariable : @MessageMapping 어노테이션으로 정의된 Websocket Controller 내에서만 사용
    public String sendMessage(@DestinationVariable Long roomId, String message){
        System.out.println(message);
        return message;
    }

}
