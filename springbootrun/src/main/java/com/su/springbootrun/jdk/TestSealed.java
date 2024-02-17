package com.su.springbootrun.jdk;

public class TestSealed {

    public sealed interface TestA permits TestB, TestC, TestD, TestF {
    }

    public final class TestB implements TestA {
    }

    public sealed class TestC implements TestA permits TestC_1 {
    }

    public non-sealed class TestC_1 extends TestC {
    }

    public non-sealed class TestD implements TestA {
    }

    public class TestE extends TestD {
    }

    public non-sealed interface TestF extends TestA {

    }
}
