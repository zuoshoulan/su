package com.su.springbootrun;

import com.su.springbootrun.netty.NettyMain;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;


/**
 * todo
 * 1。@SpringBootApplication运行机制
 * 2。Autoconfig是怎么把bean注入到spring容器的
 */
@RestController
@SpringBootApplication
public class SpringbootrunApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootrunApplication.class, args);
    }

//    @Autowired
//    private Hello nihao;

    @GetMapping("/test")
    public Object test() {

//        nihao.sayHi();

        return ZonedDateTime.now();
    }

    @GetMapping("/setAutoRead")
    public Object setAutoRead(@RequestParam @NonNull Boolean autoRead) {
        for (Map.Entry<Channel, String> channelStringEntry : NettyMain.channelMap.entrySet()) {
            Channel channel = channelStringEntry.getKey();
            channel.config().setAutoRead(autoRead);
        }
        return ZonedDateTime.now();
    }

    @GetMapping("/server_setAutoRead")
    public Object server_setAutoRead(@RequestParam @NonNull Boolean autoRead) {
        NettyMain.serverChannel.config().setAutoRead(autoRead);
        return ZonedDateTime.now();
    }

    @GetMapping("/writeChannel")
    public Object writeChannel(@RequestParam @NonNull String str) {
        for (Map.Entry<Channel, String> channelStringEntry : NettyMain.channelMap.entrySet()) {
            Channel channel = channelStringEntry.getKey();
            if (channel.isActive() && channel.isWritable()) {
                channel.writeAndFlush(str + "\r\n");
            }
        }
        return ZonedDateTime.now();
    }


    @GetMapping("/flush")
    public Object flush() {
        for (Map.Entry<Channel, String> channelStringEntry : NettyMain.channelMap.entrySet()) {
            Channel channel = channelStringEntry.getKey();
            channel.flush();
        }
        return ZonedDateTime.now();
    }


}
