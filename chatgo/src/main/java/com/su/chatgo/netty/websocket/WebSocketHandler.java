package com.su.chatgo.netty.websocket;

import com.su.chatgo.Consts;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 00:30
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    WebSocketHandler(RedisTemplate redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }


    protected Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private RedisTemplate redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        logger.info("textwebsocketFrame={}", textWebSocketFrame.text());

        TextWebSocketFrame textWebSocketFrame1 = new TextWebSocketFrame(channelHandlerContext.channel() + " , hi");
        channelHandlerContext.writeAndFlush(textWebSocketFrame1);

        String key = Consts.prefix + channelHandlerContext.channel().toString();

        redisTemplate.opsForValue().set(key, ZonedDateTime.now().toString());
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);

        Object value = redisTemplate.opsForValue().get(key);
        logger.info("value {}", value);


    }

}
