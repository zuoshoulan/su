package com.su.chatgo.netty.stomp;

import com.su.chatgo.Consts;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.stomp.DefaultStompFrame;
import io.netty.handler.codec.stomp.StompCommand;
import io.netty.handler.codec.stomp.StompFrame;
import io.netty.handler.codec.stomp.StompHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 00:30
 */
public class StompHandler extends SimpleChannelInboundHandler<StompFrame> {

    StompHandler(RedisTemplate redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    protected Logger logger = LoggerFactory.getLogger(StompHandler.class);

    private RedisTemplate redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StompFrame stompFrame) throws Exception {
        logger.info("stompFrame={}", stompFrame.toString());

        String key = Consts.prefix + channelHandlerContext.channel().toString();

        redisTemplate.opsForValue().set(key, ZonedDateTime.now().toString());
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);

        Object value = redisTemplate.opsForValue().get(key);
        logger.info("value {}", value);

    }

//    public static void main(String[] args) {
//        List<Transport> transports = new ArrayList<>();
//        SockJsClient sockJsClient = new SockJsClient()
//        WebSocketTransport webSocketTransport≠new WebSocketTransport();
//    }


}
