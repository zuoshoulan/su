package com.su.chatgo.client;

import com.su.chatgo.netty.websocket.WebSocketHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;
import io.netty.handler.codec.stomp.StompSubframeEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 22:58
 */
@Component
public class WebSocketClient {


    public void test() {

    }

    public static void main(String[] args) {

        String HOST = "localhost";
        int PORT = 18800;

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast(new HttpObjectAggregator(8192));
                    pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                    pipeline.addLast(new WebSocketClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

//            b.connect(HOST, PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
