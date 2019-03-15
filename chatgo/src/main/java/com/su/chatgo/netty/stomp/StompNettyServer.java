package com.su.chatgo.netty.stomp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;
import io.netty.handler.codec.stomp.StompSubframeEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 00:14
 * 写一个基于redis分布式的聊天服务端
 */
@Slf4j
@Component
public class StompNettyServer {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            EventLoopGroup bossGroup = null;
            EventLoopGroup workerGroup = null;
            ServerBootstrap bootstrap = null;

            ChannelFuture serverChannelFuture = null;
            try {
                bossGroup = new NioEventLoopGroup(2);
                workerGroup = new NioEventLoopGroup(8);

                InetSocketAddress tcpPort = new InetSocketAddress(18801);
                System.out.println("Starting server at " + tcpPort);

                bootstrap = new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 512)
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .option(ChannelOption.SO_REUSEADDR, true)

                        .option(ChannelOption.SO_RCVBUF, 128000)

                        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childHandler(new ChannelInitializer() {
                            @Override
                            protected void initChannel(Channel channel) throws Exception {
                                log.info("建立新连接");
                                ChannelPipeline pipeline = channel.pipeline();

                                pipeline.addLast("decoder", new StompSubframeDecoder());
                                pipeline.addLast("encoder", new StompSubframeEncoder());
                                pipeline.addLast("aggregator", new StompSubframeAggregator(1048576));
//                                pipeline.addLast(new StompHandler(redisTemplate));

                            }
                        });

                serverChannelFuture = bootstrap.bind(tcpPort).sync();

                serverChannelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            } finally {

            }
        }
        ).start();

    }

}
