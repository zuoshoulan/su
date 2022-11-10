package com.su.algo.charstring;

import java.util.Map;

public class RabinKarpMatch implements StringMatchInterface {

    public static String text0 = "abcdefghigklmn";

    public static int asciiSalt = 128;

    @Override
    public int firstMatch(String text, String pattern) {
        int patternHash = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            patternHash = patternHash + Integer.valueOf(c) * (int) Math.pow(asciiSalt, i);
        }
        int currentHash = -1;
        Character preChar = null;
        for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
            Character firstChar = text.charAt(i);
            Character lastChar = text.charAt(i + pattern.length() - 1);
            if (currentHash == -1) {
                int tmpHash = 0;
                for (int j = 0; j < pattern.length(); j++) {
                    char c = text.charAt(i + j);
                    tmpHash = tmpHash + Integer.valueOf(c) * (int) Math.pow(asciiSalt, j);
                }
                currentHash = tmpHash;
            } else {
                currentHash = currentHash - Integer.valueOf(preChar);
                currentHash = currentHash / asciiSalt;
                int tmp = Integer.valueOf(lastChar) * (int) Math.pow(asciiSalt, pattern.length() - 1);
                currentHash = currentHash + tmp;
                System.out.println();
            }
            if (currentHash == patternHash) {
                //todo test
                boolean match = true;
                for (int k = 0; k < pattern.length(); k++) {
                    if (text.charAt(i + k) != pattern.charAt(k)) {
                        match = false;
                    }
                }
                if (match) {
                    return i;
                }
            }
            preChar = text.charAt(i);
        }
        return -1;
    }

    public static void main(String[] args) {
//        RabinKarpMatch rabinKarpMatch = new RabinKarpMatch();
//        System.out.println(text0.length());
//        System.out.println("---");
//        int offset = rabinKarpMatch.firstMatch(text0, "igk");
//        System.out.println("offset:" + offset);

//        String text = "3141592653589793";
//
//        String pattern = "26";
//        for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(text.charAt(i));
//            sb.append(text.charAt(i + 1));
//            int tmp = Integer.valueOf(sb.toString());
//            System.out.println("tmp:" + tmp + " , " + tmp % 11);
//        }


        System.out.println(30000 % 13);
        System.out.println(1000 % 13);
        System.out.println(400 % 13);
        System.out.println(10 % 13);
        System.out.println(5);
        System.out.println("---");
        System.out.println((9 + 12 + 10 + 10 + 5));
        System.out.println((9 + 12 + 10 + 10 + 5) % 13);
        System.out.println("---");
        System.out.println((-20 % 13));

        System.out.println(110 % 13);


    }
}
