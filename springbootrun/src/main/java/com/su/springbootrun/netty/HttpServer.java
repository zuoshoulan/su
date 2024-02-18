package com.su.springbootrun.netty;

import com.su.springbootrun.factory.MyThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class HttpServer {
    private static final int PORT = 8080; // 监听的端口

    //    @PostConstruct
    public void init() {
        new MyThreadFactory("netty-http").newThread(() -> {
            start();
        }).start();
    }

    @SneakyThrows
    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("LoggingHandler", new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(

                                    new HttpServerCodec(),
//                                    new ChunkedWriteHandler(),
//                                    new HttpObjectAggregator(65536),
                                    new SimpleChannelInboundHandler<HttpRequest>() {
                                        @Override
                                        protected void channelRead0(io.netty.channel.ChannelHandlerContext ctx, io.netty.handler.codec.http.HttpRequest req) {

                                            log.info("req.uri():{} req.method():{} req.headers():{}", req.uri(), req.method(), req.headers());
                                            // 响应请求
                                            Channel channel = ctx.channel();
                                            ByteBuf content = Unpooled.copiedBuffer("Hello, Netty HTTP Server!", CharsetUtil.UTF_8);
                                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                                                    HttpVersion.HTTP_1_1,
                                                    HttpResponseStatus.OK,
                                                    content);
                                            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
                                            ctx.writeAndFlush(response);
                                        }
                                    }
                            );
                        }
                    })
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
            ;

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


}