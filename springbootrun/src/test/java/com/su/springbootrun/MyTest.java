package com.su.springbootrun;

import com.su.springbootrun.factory.MyThreadFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class MyTest {
    @SneakyThrows
    public static void main(String[] args) {

        AtomicLong nextWakeupNanos = new AtomicLong(0);
        nextWakeupNanos.set(1);
        nextWakeupNanos.lazySet(2);
        nextWakeupNanos.setOpaque(3);
        nextWakeupNanos.setPlain(4);
        nextWakeupNanos.setRelease(5);

        EventLoopGroup myGroup = new NioEventLoopGroup(1, new MyThreadFactory("myGroup"));
//        EventLoopGroup myGroup = new KQueueEventLoopGroup(1, new MyThreadFactory("myGroup"));
        Future<Object> submit = myGroup.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "yes_no";
            }
        });
        Object o = submit.get();
        System.out.println("" + o);

    }
}
