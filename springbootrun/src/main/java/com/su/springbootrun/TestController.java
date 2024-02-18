package com.su.springbootrun;

import com.su.springbootrun.netty.CilentTest;
import com.su.springbootrun.netty.NettyMain;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    List<ChannelFuture> list = new LinkedList<>();

    @SneakyThrows
    @GetMapping("/test")
    public Object test() {
        int randomInt = ThreadLocalRandom.current().nextInt(0, 10);
        for (Map.Entry<Channel, String> channelStringEntry : NettyMain.channelMap.entrySet()) {
            Channel channel = channelStringEntry.getKey();
            if (channel.isActive() && channel.isWritable()) {
                ChannelFuture channelFuture = channel.writeAndFlush(ZonedDateTime.now() + " , ok" + "\r\n");
                list.add(channelFuture);
                log.info("channel:{} start...", channel);
                final AtomicInteger k = new AtomicInteger();
                for (int i = 0; i < 3; i++) {
                    channelFuture.addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            int p = k.incrementAndGet();
                            log.info("Listener...start- ok-,{}", p);
                            log.info("Listener...end- ok-,{}", p);
                        }
                    });
                    channelFuture.await();
                }

            }
        }
        return ZonedDateTime.now() + " randomInt: " + randomInt;
    }

    @SneakyThrows
    @GetMapping("/again")
    public Object again() {
        for (ChannelFuture channelFuture : list) {
            final AtomicInteger k = new AtomicInteger();
            for (int i = 0; i < 1; i++) {
                channelFuture.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        int p = k.incrementAndGet();
                        log.info("again Listener...start- ok-,{}", p);
                        log.info("again Listener...end- ok-,{}", p);
                    }
                });
            }
        }
        return ZonedDateTime.now() + "again";
    }

    @SneakyThrows
    @GetMapping("/close")
    public Object close() {
        NettyMain.serverChannel.close();
        return ZonedDateTime.now() + "again";
    }




}
