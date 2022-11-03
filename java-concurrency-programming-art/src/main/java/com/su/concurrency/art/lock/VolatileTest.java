package com.su.concurrency.art.lock;

public class VolatileTest {


    public static volatile int i = 0;

    public static class VolatileRunnable implements Runnable {

        @Override
        public void run() {

            for (int j = 0; j < 100000; j++) {
                synchronized (VolatileRunnable.class) {
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread runnable1 = new Thread(new VolatileRunnable());
        Thread runnable2 = new Thread(new VolatileRunnable());

        runnable1.start();
        runnable2.start();
        Thread.sleep(100);
        runnable1.join();
        runnable2.join();

        System.out.println("i=" + i);
    }
}
