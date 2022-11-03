package com.su.concurrency.art.chapter4;

public class Daemon {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemondRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemondRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(10);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
