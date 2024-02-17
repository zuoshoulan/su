package com.su.springbootrun.mysql;

/**
 * @author zhengweikang
 * @date 2024/1/24 00:53
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFileAndWrite {

    private static String dir = "/Users/wangnana/zwk/su/su/springbootrun/src/main/resources/mysql/";


    public static void write2file(String relativeFileName, String content) {

        try {
            FileWriter writer = new FileWriter(dir + relativeFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            System.out.println("文件 " + relativeFileName + " 写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}