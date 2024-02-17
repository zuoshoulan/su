package com.su.springbootrun;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author zhengweikang
 * @date 2024/1/25 22:36
 */
public class ByteBufTest {

    int num = 1000000;

    public static void before() {
//        ByteBuf directByteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(400);
//        directByteBuf.release();
//        ByteBuf heapByteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(400);
//        heapByteBuf.release();
    }

    /**
     * 测试DirectMemory和Heap读写速度。
     */
    @Test
    public void testDirectMemoryWriteAndReadSpeed() {

        ByteBuf directByteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(400);
        ByteBuf heapByteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(400);

        long tsStart = System.currentTimeMillis();


        for (int i = 0; i < num; i++) {
            for (int j = 0; j < 100; j++) {
                directByteBuf.writeInt(j);
            }
            for (byte j = 0; j < 100; j++) {
                directByteBuf.readInt();
            }
        }
        System.out.println("DirectMemory读写耗用： " + (System.currentTimeMillis() - tsStart) + " ms");
        tsStart = System.currentTimeMillis();

        for (int i = 0; i < num; i++) {
            for (int j = 0; j < 100; j++) {
                heapByteBuf.writeInt(j);
            }
            for (byte j = 0; j < 100; j++) {
                heapByteBuf.readInt();
            }
        }
        System.out.println("Heap读写耗用： " + (System.currentTimeMillis() - tsStart) + " ms");
        directByteBuf.release();
        heapByteBuf.release();
    }

    /**
     * 测试DirectMemory和Heap内存申请速度。
     */
    @Test
    public void testDirectMemoryAllocate() {

        long tsStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            ByteBuf directByteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(400);
            directByteBuf.release();
        }
        System.out.println("DirectMemory申请内存耗用： " + (System.currentTimeMillis() - tsStart) + " ms");
        tsStart = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            ByteBuf heapByteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(400);
            heapByteBuf.release();
        }
        System.out.println("Heap申请内存耗用： " + (System.currentTimeMillis() - tsStart) + " ms");
    }


    @Test
    public void test() {
        ByteBuf heapByteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(1, 4);
        ByteBuf heapByteBuf0 = UnpooledByteBufAllocator.DEFAULT.heapBuffer(1, 4);
        ByteBuf heapByteBuf01 = PooledByteBufAllocator.DEFAULT.heapBuffer(1, 4);
        ByteBuf heapByteBuf02 = PooledByteBufAllocator.DEFAULT.directBuffer(1, 4);
        ByteBuf heapByteBuf1 = PooledByteBufAllocator.DEFAULT.directBuffer(1, 4);
        heapByteBuf.writeInt(2);
        heapByteBuf.writeInt(2);
        heapByteBuf.writeInt(2);
        String hexDump = ByteBufUtil.hexDump(heapByteBuf);
        System.out.println(hexDump);
        heapByteBuf.release();
    }
}
