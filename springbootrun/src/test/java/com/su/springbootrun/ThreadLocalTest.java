package com.su.springbootrun;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class ThreadLocalTest {
    public static ThreadLocal<String> threadName = new ThreadLocal<String>() {
        protected String initialValue() {
            return "-1";
        }
    };

    public static ThreadLocal<String> threadName1 = ThreadLocal.withInitial(new Supplier<String>() {
        @Override
        public String get() {
            return "99";
        }
    });
    public static ThreadLocal<String> threadName2 = new ThreadLocal<>();


    @Test
    @SneakyThrows
    public void test() {
        threadName.set("789");
        Thread t1 = new Thread() {
            public void run() {
//                threadName.set("123");
                System.out.println("threadName in t1 is : " + threadName.get());
                threadName.remove();
                System.out.println("threadName in t1 is : " + threadName.get());
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                threadName.set("456");
                System.out.println("threadName in t2 is : " + threadName.get());
                threadName.remove();
                System.out.println("threadName in t2 is : " + threadName.get());
            }
        };
//        Thread.ofVirtual().start(t1);
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        System.out.println("threadName in main is : " + threadName.get());
    }


}
