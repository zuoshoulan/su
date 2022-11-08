package com.su.algo.charstring;

public class SimpleMatch implements StringMatchInterface {

    public static String text0 = "abcdefghigklmn";

    @Override
    public int firstMatch(String text, String pattern) {

        for (int i = 0; i < text.length(); i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length(); j++) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SimpleMatch simpleMatch = new SimpleMatch();
        int offset = simpleMatch.firstMatch(text0, "kh");
        System.out.println("offset:" + offset);
    }
}
