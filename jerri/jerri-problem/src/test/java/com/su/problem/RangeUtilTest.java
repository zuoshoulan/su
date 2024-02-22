package com.su.problem;

import com.su.problem.utils.RangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

@Slf4j
@DisplayName("RangeUtil 工具类单测")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RangeUtilTest {

    /**
     * 两个range的差集
     * 1.全部减完，没有剩下的range
     * 2.剩下两个range
     * 3.剩下左边一部分
     * 4.两个没有重合的range，没有减掉，全部剩下来
     * 5.有空集参与计算的，报错
     *
     * @see RangeUtil#subRange(Range, Range)
     */
    @DisplayName("两个range差集")
    @Order(0)
    @Test
    public void testSubRange() {
        Range range = new Range(3, 10);
        Range sub1 = new Range(5, 7);
        Range sub2 = new Range(1, 5);
        Range sub3 = new Range(6, 15);
        Range sub4 = new Range(1, 2);
        Range sub5 = new Range(10, 12);
        Range sub6 = new Range(3, 10);
        Range sub7 = new Range(1, 20);
        Range sub8 = new Range(3, 9);
        //剩下两个range
        List<Range> remainder1 = RangeUtil.subRange(range, sub1);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub1, remainder1);
        Assertions.assertEquals(remainder1.size(), 2);
        Assertions.assertEquals(remainder1.get(0), new Range(3, 5));
        Assertions.assertEquals(remainder1.get(1), new Range(7, 10));

        List<Range> remainder2 = RangeUtil.subRange(range, sub2);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub2, remainder2);
        Assertions.assertEquals(remainder2.size(), 1);
        Assertions.assertEquals(remainder2.get(0), new Range(5, 10));
        List<Range> remainder3 = RangeUtil.subRange(range, sub3);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub3, remainder3);
        Assertions.assertEquals(remainder3.size(), 1);
        Assertions.assertEquals(remainder3.get(0), new Range(3, 6));
        List<Range> remainder4 = RangeUtil.subRange(range, sub4);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub4, remainder4);
        Assertions.assertEquals(remainder4.size(), 1);
        Assertions.assertEquals(remainder4.get(0), new Range(3, 10));
        List<Range> remainder5 = RangeUtil.subRange(range, sub5);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub5, remainder5);
        Assertions.assertEquals(remainder5.size(), 1);
        Assertions.assertEquals(remainder5.get(0), new Range(3, 10));
        List<Range> remainder6 = RangeUtil.subRange(range, sub6);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub6, remainder6);
        Assertions.assertEquals(remainder6.size(), 0);
        List<Range> remainder7 = RangeUtil.subRange(range, sub7);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub7, remainder7);
        Assertions.assertEquals(remainder7.size(), 0);
        List<Range> remainder8 = RangeUtil.subRange(range, sub8);
        log.info("原始range:{} sub:{} ,结果是:{}", range, sub8, remainder8);
        Assertions.assertEquals(remainder8.size(), 1);
        Assertions.assertEquals(remainder8.get(0), new Range(9, 10));

        Assertions.assertThrows(Throwable.class, () -> RangeUtil.subRange(range, new Range(3, 3)));
        Assertions.assertThrows(Throwable.class, () -> RangeUtil.subRange(new Range(3, 3), range));

    }
}
