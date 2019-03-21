package com.su.chatgo;

import com.su.chatgo.demo.Mutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Weikang Lan
 * @Created 2019-03-20 15:29
 */
public class Demo {

    static Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        Mutex mutex = new Mutex();
        logger.info("name {}, flag {}", Thread.currentThread().getName(), mutex.hasQueuedThreads());
        mutex.lock();
        mutex.lock();
        mutex.lock();
//        for (int i = 0; i < 3; i++) {
//            new Thread(() -> {
//                try {
//                    mutex.lock();
//                    logger.info("before name {}, flag {}", Thread.currentThread().getName(), mutex.hasQueuedThreads());
//                    Thread.sleep(3000L);
//                    logger.info("after name {}, flag {}", Thread.currentThread().getName(), mutex.hasQueuedThreads());
//                    mutex.unlock();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }

        logger.info("name {}, flag {}", Thread.currentThread().getName(), mutex.hasQueuedThreads());

        mutex.unlock();
        mutex.unlock();
        mutex.unlock();

    }
}
