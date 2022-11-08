package com.su.algo.charstring;

public interface StringMatchInterface {

    default int firstMatch(String text, String pattern) {
        return -1;
    }

    default int[] allMatch(String text, String pattern) {
        return null;
    }
}
