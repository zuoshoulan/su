package com.su.concurrency.art.lock;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class SyncTest {

    public static Object object = new Object();

    public static void test1() {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseInstance(object).toPrintable());
        System.out.println("---");
        synchronized (object) {

            System.out.println(ClassLayout.parseInstance(object).toPrintable());
            System.out.println(object.hashCode());

        }
    }


    public static void main(String[] args) {
        SyncTest.test1();
    }
}
