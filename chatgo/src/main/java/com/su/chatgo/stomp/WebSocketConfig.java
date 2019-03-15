package com.su.chatgo.stomp;//package com.lima.websocket.stomp;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptorAdapter;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@RestController
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry
////                .addEndpoint("/stompwebsocket")
////                .setAllowedOrigins("*")
////                .setHandshakeHandler(new DefaultHandshakeHandler())
////                .addInterceptors(new WebSocketHandshakeInterceptor())
//////                .withSockJS()
////        ;
//        registry
//                .addEndpoint("/stomp")
//                .setAllowedOrigins("*")
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //基于内存的STOMP消息代理
//        registry.enableSimpleBroker("/queue", "/topic");
//
//        //基于RabbitMQ 的STOMP消息代理
///*        registry.enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost(host)
//                .setRelayPort(port)
//                .setClientLogin(userName)
//                .setClientPasscode(password);*/
//
//        registry.setApplicationDestinationPrefixes("/app", "/foo");
//        registry.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptorAdapter() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                //1、判断是否首次连接
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    //2、判断用户名和密码
//                    String username = accessor.getNativeHeader("username").get(0);
//                    String password = accessor.getNativeHeader("password").get(0);
//
//                    if ("admin".equals(username) && "admin".equals(password)) {
//                        Principal principal = new Principal() {
//                            @Override
//                            public String getName() {
//                                return "lwk";
//                            }
//                        };
//                        accessor.setUser(principal);
//                        return message;
//                    } else {
//                        return null;
//                    }
//                }
//                //不是首次连接，已经登陆成功
//                return message;
//            }
//
//        });
//    }
//
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/send.message")
//    public void sendPublicMessage(String msg) {
//        simpMessagingTemplate.convertAndSend("/topic/public.messages", msg);
//    }
//
//
//}
