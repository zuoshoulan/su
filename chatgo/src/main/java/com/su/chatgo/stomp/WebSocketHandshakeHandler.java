package com.su.chatgo.stomp;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * STOMP 握手处理器
 * @author luchen
 * @since 1.1
 */
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

    ///该方法可以重写用来为用户 添加标识 返回principal
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // TODO Auto-generated method stub
        return super.determineUser(request, wsHandler, attributes);
    }

}
