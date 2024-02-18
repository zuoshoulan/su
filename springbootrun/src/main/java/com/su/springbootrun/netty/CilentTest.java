package com.su.springbootrun.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;

@RestController
@Service
@Slf4j
public class CilentTest {

    public static Channel channel;

    public static Bootstrap bootstrap;

    @SneakyThrows
//    @PostConstruct
    public void init() {
        Executors.newFixedThreadPool(1).submit(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(3000);
                start();
            }
        });
    }

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("LoggingHandler", new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                String hexDump = ByteBufUtil.hexDump(msg);
                                log.info(hexDump);
                            }
                        });
                    }
                })
        ;
        reconnect();
    }

    @SneakyThrows
    public static Object reconnect() {
        try {
            if (channel != null) {
                channel.close();
            }

        } catch (Exception e) {
            log.error("", e);
        }
        ChannelFuture sync = bootstrap.connect("127.0.0.1", 8080).sync();
        channel = sync.channel();
        return channel;
    }

    public static Object writeAndFlush(String hex) {
        byte[] bytes = ByteBufUtil.decodeHexDump(hex);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(bytes);
        ChannelFuture channelFuture = channel.writeAndFlush(byteBuf);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("write...complete...");
            }
        });
        return ZonedDateTime.now();
    }

    @SneakyThrows
    @GetMapping("/client/client_writeAndFlush")
    public Object client_writeAndFlush(String hex) {
        CilentTest.writeAndFlush(hex);
        return ZonedDateTime.now() + " writeAndFlush";
    }

    @SneakyThrows
    @GetMapping("/client/client_reconnect")
    public Object client_reconnect() {
        CilentTest.reconnect();
        return ZonedDateTime.now() + " client_reconnect";
    }

}
