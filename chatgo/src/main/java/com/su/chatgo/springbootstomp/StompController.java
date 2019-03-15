package com.su.chatgo.springbootstomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 16:00
 */
@RestController
public class StompController {

    //注入SimpMessagingTemplate 用于点对点消息发送
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendPublicMessage")
    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPublicMessage
    @SendTo("/topic/public") //也可以使用 messagingTemplate.convertAndSend(); 推送
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/sendPrivateMessage")
    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPrivateMessage
    public void sendPrivateMessage(@Payload ChatMessage msg, Principal principal) {
        msg.setSender(principal.getName());
        //将消息推送到指定路径上
        messagingTemplate.convertAndSendToUser(msg.getReceiver(), "topic/chat", msg);
    }

     /*
    注释方式推不过去这里没调通,有大神的话慢慢研究吧
    @SendToUser(value = "/topic/chat",broadcast=false)
    public ChatMessage sendPrivateMessage(@Payload  ChatMessage msg,Principal principal) {
        msg.setSender(principal.getName());
        return msg;
    }*/

}
