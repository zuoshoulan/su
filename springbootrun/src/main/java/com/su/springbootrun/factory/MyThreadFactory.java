package com.su.springbootrun.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {

    private AtomicInteger seq = new AtomicInteger();

    private String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, poolName + "-pool-" + seq.getAndIncrement());
    }

//    public static void main(String[] args) {
//        MyThreadFactory myThreadFactory = new MyThreadFactory("boss");
//        myThreadFactory.newThread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());
//            }
//        }).start();
//    }
}
