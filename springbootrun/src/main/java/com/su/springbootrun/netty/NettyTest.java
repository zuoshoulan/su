package com.su.springbootrun.netty;

import io.netty.buffer.*;
import io.netty.util.ReferenceCounted;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author zhengweikang
 * @date 2024/1/25 17:29
 */
public class NettyTest {
    public static void main(String[] args) {

//        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(1025);
//        UnpooledByteBufAllocator.DEFAULT.heapBuffer(10);
//        UnpooledByteBufAllocator.DEFAULT.heapBuffer();
//        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
//        Unpooled.buffer();
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(10, 20);
        printLog(byteBuf, "init");
        byteBuf.writeByte(5);
        byteBuf.writeByte(6);
        printLog(byteBuf, "write");

        byteBuf.capacity(1);
        byteBuf.capacity();
        byteBuf.maxCapacity();

        printLog(byteBuf);


        byteBuf.release();
        System.out.println();
    }

    private static void printLog(ByteBuf byteBuf, String... str) {
        System.out.println("printLog: " + byteBuf);
        String collect = Arrays.stream(str).collect(Collectors.joining(","));
        if (!StringUtils.isEmpty(str)) {
            collect += " ";
        }
        System.out.println(collect + "writableBytes:" + byteBuf.writableBytes() + " ,readableBytes:" + byteBuf.readableBytes()
                + " ," + byteBuf.markReaderIndex() + byteBuf.markWriterIndex());
        System.out.println("hexDump " + ByteBufUtil.hexDump(byteBuf));
        System.out.println("hexDump all " + ByteBufUtil.hexDump(byteBuf, 0, byteBuf.capacity()));
    }
}
