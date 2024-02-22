package com.su.problem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
public class MainTest {

    /**
     * 题目中出现的情况
     * 主流程测试
     */
    @Test
    public void testMain() {
        RangeList rangeList = new RangeList();
        log.info("初始时:rangeList:{}", rangeList);
        rangeList.add(new Range(1, 5));
        log.info("添加 {} 后 rangeList:{}", new Range(1, 5), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5)");

        rangeList.add(new Range(10, 20));
        log.info("添加 {} 后 rangeList:{}", new Range(10, 20), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 20)");

        rangeList.add(new Range(20, 20));
        log.info("添加 {} 后 rangeList:{}", new Range(20, 20), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 20)");

        rangeList.add(new Range(20, 21));
        log.info("添加 {} 后 rangeList:{}", new Range(20, 21), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 21)");

        rangeList.add(new Range(2, 4));
        log.info("添加 {} 后 rangeList:{}", new Range(2, 4), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 21)");

        rangeList.add(new Range(3, 8));
        log.info("添加 {} 后 rangeList:{}", new Range(3, 8), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");

        rangeList.remove(new Range(10, 10));
        log.info("移除 {} 后 rangeList:{}", new Range(10, 10), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");

        rangeList.remove(new Range(10, 11));
        log.info("移除 {} 后 rangeList:{}", new Range(10, 11), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [11, 21)");

        rangeList.remove(new Range(15, 17));
        log.info("移除 {} 后 rangeList:{}", new Range(15, 17), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [11, 15) [17, 21)");

        rangeList.remove(new Range(3, 19));
        log.info("移除 {} 后 rangeList:{}", new Range(3, 19), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 3) [19, 21)");

    }
}
