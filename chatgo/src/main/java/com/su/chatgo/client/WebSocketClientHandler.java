package com.su.chatgo.client;

import com.su.chatgo.Consts;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
public class WebSocketClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ByteBuf firstMessage;
    protected Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    public WebSocketClientHandler() {
        firstMessage = Unpooled.buffer(256);
        for (int i = 0; i < firstMessage.capacity(); i++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        logger.info("textwebsocketFrame={}", textWebSocketFrame.text());

        TextWebSocketFrame textWebSocketFrame1 = new TextWebSocketFrame(channelHandlerContext.channel() + " , hi");
        channelHandlerContext.writeAndFlush(textWebSocketFrame1);

        String key = Consts.prefix + channelHandlerContext.channel().toString();

    }
}
