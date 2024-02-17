package com.su.springbootrun.netty;

import com.su.springbootrun.factory.MyThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NettyMain {

    public static void main(String[] args) {
        NettyMain nettyMain = new NettyMain();
        nettyMain.start();
    }

    public static Channel serverChannel;
    public static Channel serverChannel2;

    public static Map<Channel, String> channelMap = new ConcurrentHashMap();

    @PostConstruct
    public void init() {
        new MyThreadFactory("netty-M").newThread(() -> {
            start();
        }).start();
    }

    public void start() {
        log.info("netty-start...");

        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new MyThreadFactory("boss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(1, new MyThreadFactory("worker"));
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            log.info("initChannel");
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("LoggingHandler", new LoggingHandler(LogLevel.DEBUG));
//                            pipeline.addLast("IdleStateHandler", new IdleStateHandler(2, 3, 5, TimeUnit.SECONDS));
                            pipeline.addLast("my-server-ChannelDuplexHandler", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info("server channelRead");
                                    ctx.fireChannelRead(msg);
                                }
                            });

                        }
                    })
                    .group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("child-IdleStateHandler", new IdleStateHandler(20, 30, 50, TimeUnit.SECONDS));
//                            pipeline.addLast("child-IdleStateHandler", new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS));
//                            pipeline.addLast("my-child-IdleStateHandler", new MyIdleHandler(20, 0, 0, TimeUnit.SECONDS));
//                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                            pipeline.addLast("stringEncoder", new StringEncoder());
                            ByteBuf delimiterByteBuf = ByteBufAllocator.DEFAULT.buffer();
                            delimiterByteBuf.writeByte('\r');
                            delimiterByteBuf.writeByte('\n');
                            pipeline.addLast("DelimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(1024, delimiterByteBuf));
                            pipeline.addLast("stringDecoder", new StringDecoder());
                            pipeline.addLast("MySimpleChannelInboundHandler", new MySimpleChannelInboundHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                    .childOption(ChannelOption.TCP_NODELAY, true)
//                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(140, 144))
            ;

//            initSchedule(bossGroup, workerGroup);
//            ChannelFuture bind1 = bootstrap.clone().bind(9999);
//            new Thread(new Runnable() {
//                @SneakyThrows
//                @Override
//                public void run() {
//                    ChannelFuture cf = bind1.sync();
//                    serverChannel2 = cf.channel();
//                    cf.addListener((ChannelFutureListener) future -> {
//                        if (cf.isSuccess()) {
//                            log.info("监听端口 9999 成功");
//                        } else {
//                            log.info("监听端口 9999 失败");
//                        }
//                    });
//                    ChannelFuture channelFuture = cf.channel().closeFuture();
//
//                    channelFuture.sync();
//                }
//            }).start();

            ChannelFuture channelFuture = bootstrap.bind(8888);
            serverChannel = channelFuture.channel();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()) {
                    log.info("监听端口 8888 成功");
                } else {
                    log.info("监听端口 8888 失败");
                }
            });
            ChannelFuture sync = channelFuture.sync();
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info("addListener.close.");
                }
            });
            closeFuture.sync();
        } catch (Exception e) {
        } finally {
            log.info("shutdownGracefully...");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private static class MySimpleChannelInboundHandler extends ChannelDuplexHandler {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
            if (event instanceof IdleStateEvent) {
                IdleStateEvent evt = (IdleStateEvent) event;
                if (evt.state().equals(IdleState.ALL_IDLE)) {
                    log.info("================全空闲================");
                } else if (evt.state().equals(IdleState.READER_IDLE)) {
                    log.info("================读空闲================");
                } else if (evt.state().equals(IdleState.WRITER_IDLE)) {
                    log.info("=================写空闲=====================");
                }
//                ctx.close();
            }

        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            super.channelRegistered(ctx);
            channelMap.put(ctx.channel(), "");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("channelRead");
            Channel channel = ctx.channel();
            EventLoop eventLoop = channel.eventLoop();
            DefaultPromise promise = new DefaultPromise<>(eventLoop);
            promise.setSuccess(null);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hello,you are welcome!");
            stringBuilder.append(" ");
            stringBuilder.append(msg + "\n");
            stringBuilder.append("-");
            stringBuilder.append(Thread.currentThread().getName());
            stringBuilder.append("-\n");
//            channel.writeAndFlush(stringBuilder.toString());


            ChannelPromise channelPromise = channel.newPromise();
            ChannelFutureListener listener0 = new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.isSuccess();
                    log.info("operationComplete...0");
                }
            };
            channelPromise.addListeners(listener0);
            ChannelFuture channelFuture = channel.writeAndFlush(stringBuilder.toString(), channelPromise);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info("operationComplete...ok");
                }
            });

//            channel.write(stringBuilder.toString(), channelPromise);

            if ("exit".equals(msg)) {
                channel.writeAndFlush("Bye~");
                ctx.close();
            }
//            if ("serverClose\r\n".equals(msg)) {
//                ChannelFuture close = serverChannel.close();
//            }


        }

    }

    private static void initSchedule(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        bossGroup.next().schedule(new MyCallThread("boss schedule"), 5, TimeUnit.SECONDS);
//        bossGroup.next().scheduleWithFixedDelay(new MyRunThread("boss scheduleWithFixedDelay"), 5, 10, TimeUnit.SECONDS);
        bossGroup.next().scheduleAtFixedRate(new MyRunThread("boss scheduleAtFixedRate"), 5, 10, TimeUnit.SECONDS);

        workerGroup.next().schedule(new MyCallThread("worker schedule"), 5, TimeUnit.SECONDS);
//        workerGroup.next().scheduleWithFixedDelay(new MyRunThread("worker scheduleWithFixedDelay"), 5, 10, TimeUnit.SECONDS);

    }


    private static class MyCallThread implements Callable {
        private String tag;

        MyCallThread(String tag) {
            this.tag = tag;
        }

        @Override
        public Object call() throws Exception {
            log.info("call " + tag);
            return LocalDateTime.now();
        }
    }

    private static class MyRunThread implements Runnable {
        private String tag;

        MyRunThread(String tag) {
            this.tag = tag;
        }

        @SneakyThrows
        @Override
        public void run() {
            log.info("run start... " + tag);
            Thread.sleep(2000);
            log.info("run end... " + tag);
        }
    }

}
