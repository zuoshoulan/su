package com.su.springbootrun;


import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {


    @Test
    public void test() {
        Future<Object> future = Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("call start...");
                Thread.sleep(2000);
                return "01234-56789";
            }
        });

    }
}
