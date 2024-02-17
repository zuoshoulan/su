package com.su.springbootrun.mysql;

import io.netty.buffer.*;
import org.springframework.data.redis.util.ByteUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhengweikang
 * @date 2024/1/23 23:55
 */
public class MysqlTest {

    private static int pageBytes = 16384;

    //File Space Header
    private static int FIL_PAGE_TYPE_FSP_HDR = 0x0008;


    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("/usr/local/mysql/data/test/single_table.ibd")); // 读取文件内容到字节数组
//            StringBuilder sb = new StringBuilder();
//            for (byte b : bytes) { // 遍历字节数组，将每个字节转换为十六进制字符串
//                sb.append(String.format("%02X ", b));
//            }
            String allHexDump = ByteBufUtil.hexDump(bytes);

            System.out.println(bytes.length);
            System.out.println(bytes.length / pageBytes);
            System.out.println(bytes.length / pageBytes * pageBytes);
            System.out.println();
            int maxPage = bytes.length / pageBytes;
            StringBuilder stringBuilder = new StringBuilder(pageBytes * 4);
            for (int i = 0; i < maxPage; i++) {
                stringBuilder.setLength(0);
                System.out.println("page " + i);
                int fromIndex = i * pageBytes;
//                if (!isAllZero(bytes, fromIndex, pageBytes)) {
                String hexDump = ByteBufUtil.hexDump(bytes, fromIndex, pageBytes);
                stringBuilder.append("page hex 0-16383").append("\n").append(hexDump).append("\n");
                stringBuilder.append("file header 0-37").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 38)).append("\n");
                stringBuilder.append("file header file_page_space").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 4)).append("\n");
                fromIndex += 4;
                stringBuilder.append("file header file_page_offset").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 4)).append("\n");
                fromIndex += 4;
                stringBuilder.append("file header file_page_prev").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 4)).append("\n");
                fromIndex += 4;
                stringBuilder.append("file header file_page_next").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 4)).append("\n");
                fromIndex += 4;
                stringBuilder.append("file header file_page_lsn").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 8)).append("\n");
                fromIndex += 8;
                stringBuilder.append("file header file_page_type").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 2)).append("\n");
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
                int filPageType = 0;
                try {
                    byteBuf.writeBytes(bytes, fromIndex, 2);
                    filPageType = byteBuf.readUnsignedShort();
                } finally {
                    byteBuf.release();
                }

                fromIndex += 2;
                stringBuilder.append("file header file_page_file_flush_lsn").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 8)).append("\n");
                fromIndex += 8;
                stringBuilder.append("file header file_page_arch_log_no").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 4)).append("\n");
                stringBuilder.append("\n");
                fromIndex += 4;
                if (filPageType == FIL_PAGE_TYPE_FSP_HDR) {
                    stringBuilder.append("page header 38开始- 112字节").append("\n").append(ByteBufUtil.hexDump(bytes, fromIndex, 112)).append("\n");
                }


                CreateFileAndWrite.write2file("page" + i, stringBuilder.toString());
//                } else {
//                    CreateFileAndWrite.write2file("page" + i, "全是0x00");
//                }
            }
            CreateFileAndWrite.write2file("allHexDump", allHexDump);
//            System.out.println(sb.toString()); // 输出十六进制字符串
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean isAllZero(byte[] bytes, int fromIndex, int length) {
        int maxIndex = fromIndex + length;
        for (int i = fromIndex; i < maxIndex; i++) {
            if (bytes[i] != 0x00) {
                return false;
            }
        }
        return true;
    }
}
