package com.su.springbootrun.netty;

import com.su.springbootrun.factory.MyThreadFactory;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HashedWheelTimerTest {

    static Timeout myTimeout = null;

    @SneakyThrows
    public static void main(String[] args) {
        AtomicInteger loopCount = new AtomicInteger();
        MyThreadFactory myThreadFactory = new MyThreadFactory("my-HashedWheelTimer");
        Executor executor = Executors.newFixedThreadPool(5, new MyThreadFactory("my-Executor"));
        My_HashedWheelTimer timer = new My_HashedWheelTimer(myThreadFactory, 1, TimeUnit.NANOSECONDS,
                512, true, -1, executor);
        log.info("12..");
        timer.start();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                int count = loopCount.incrementAndGet();
                try {
//                    myTimeout = timer.newTimeout(this, 3, TimeUnit.SECONDS);
                    log.info("task1 start!.. {}", count);
                    long sleepTime = RandomUtils.nextLong(100, 5000);
                    Thread.sleep(sleepTime);
                } finally {
                    log.info("task1 end!.. {}", count);
                }
            }
        };
        myTimeout = timer.newTimeout(task1, 2, TimeUnit.SECONDS);
        myTimeout.cancel();
//        Thread.sleep(15000);
//        if (myTimeout != null) {
//            myTimeout.cancel();
//        }
        System.in.read();
    }
}
