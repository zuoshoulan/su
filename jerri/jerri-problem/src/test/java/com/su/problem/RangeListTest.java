package com.su.problem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

@DisplayName("rangeList单测")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class RangeListTest {

    /**
     * add的各种情况
     * 初始化添加第一个range
     * 最前面添加range
     * 添加同一个range
     * 最后面添加range
     * 添加粘连的range
     * 添加已被包含的range
     * 添加空集
     * 添加跨两个range的
     * 顺序添加
     *
     * @see RangeList#add(Range)
     * @see RangeList#toString()
     */
    @DisplayName("add和toString函数")
    @Order(0)
    @Test
    public void testAdd() {
        RangeList rangeList = new RangeList();
        log.info("初始时:rangeList:{}", rangeList);

        //初始化添加第一个range
        rangeList.add(new Range(10, 20));
        log.info("添加 {} 后 rangeList:{}", new Range(10, 20), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[10, 20)");

        //最前面添加range
        rangeList.add(new Range(1, 5));
        log.info("添加 {} 后 rangeList:{}", new Range(1, 5), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 20)");

        //添加同一个range
        rangeList.add(new Range(10, 20));
        log.info("添加 {} 后 rangeList:{}", new Range(10, 20), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 20)");

        //最后面添加range
        rangeList.add(new Range(25, 30));
        log.info("添加 {} 后 rangeList:{}", new Range(25, 30), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 20) [25, 30)");

        //添加粘连的range
        rangeList.add(new Range(20, 21));
        log.info("添加 {} 后 rangeList:{}", new Range(20, 21), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 21) [25, 30)");

        //添加已被包含的range
        rangeList.add(new Range(2, 4));
        log.info("添加 {} 后 rangeList:{}", new Range(2, 4), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 21) [25, 30)");

        //添加空集
        rangeList.add(new Range(21, 21));
        log.info("添加 {} 后 rangeList:{}", new Range(3, 8), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 5) [10, 21) [25, 30)");

        //添加跨两个range的
        rangeList.add(new Range(3, 15));
        log.info("添加 {} 后 rangeList:{}", new Range(3, 15), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 21) [25, 30)");

        RangeList rangeList2 = new RangeList();
        log.info("顺序add 初始时:rangeList2:{}", rangeList2);
        Assertions.assertEquals("", rangeList2.toString());
        rangeList2.add(new Range(1, 10));
        log.info("顺序add 添加 {} 后 rangeList:{}", new Range(1, 10), rangeList2);
        Assertions.assertEquals("[1, 10)", rangeList2.toString());
        rangeList2.add(new Range(15, 21));
        log.info("顺序add 添加 {} 后 rangeList:{}", new Range(15, 21), rangeList2);
        Assertions.assertEquals("[1, 10) [15, 21)", rangeList2.toString());
        rangeList2.add(new Range(310, 612));
        log.info("顺序add 添加 {} 后 rangeList:{}", new Range(310, 612), rangeList2);
        Assertions.assertEquals("[1, 10) [15, 21) [310, 612)", rangeList2.toString());

    }

    /**
     * 两个目的
     * 1.包含相同元素的RangeList#hashCode()应该是相等的
     * 2.包含不同元素的RangeList#hashCode()应该是离散的
     *
     * @see RangeList#hashCode()
     */
    @DisplayName("hashCode函数")
    @Order(1)
    @Test
    public void testHashCode() {
        RangeList rangeList = new RangeList().add(new Range(1, 5)).add(new Range(6, 8));
        RangeList rangeList0 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8));
        RangeList rangeList1 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8)).add(new Range(10, 13));
        RangeList rangeList2 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8)).add(new Range(10, 11));
        RangeList rangeList3 = new RangeList().add(new Range(1, 4)).add(new Range(15, 18));
        RangeList rangeList4 = new RangeList().add(new Range(1, 15)).add(new Range(60, 80));
        RangeList rangeList5 = new RangeList().add(new Range(8, 9)).add(new Range(13, 24));
        RangeList rangeList6 = new RangeList().add(new Range(23, 25)).add(new Range(43, 58));
        RangeList rangeList7 = new RangeList().add(new Range(33, 55)).add(new Range(66, 88));
        RangeList rangeList8 = new RangeList().add(new Range(123, 155)).add(new Range(236, 889));
        RangeList rangeList9 = new RangeList().add(new Range(188, 3895)).add(new Range(9876, 77658));
        Assertions.assertEquals(rangeList.hashCode(), rangeList0.hashCode());
        log.info("测试RangeList#hashCode 包含一样的内容的两个对象hashCode是否相等 成功");
        Set<Integer> set = new HashSet<>();
        set.add(rangeList0.hashCode());
        set.add(rangeList1.hashCode());
        set.add(rangeList2.hashCode());
        set.add(rangeList3.hashCode());
        set.add(rangeList4.hashCode());
        set.add(rangeList5.hashCode());
        set.add(rangeList6.hashCode());
        set.add(rangeList7.hashCode());
        set.add(rangeList8.hashCode());
        set.add(rangeList9.hashCode());
        Assertions.assertTrue(set.size() >= 5);
        log.info("测试RangeList#hashCode的离散程度 成功");
    }

    /**
     * 两个目标
     * 1.包含相同元素的RangeList#equals()结果应该是true
     * 2.包含不同元素的RangeList#equals()结果应该是false
     *
     * @see RangeList#equals(Object)
     */
    @DisplayName("equals函数")
    @Order(2)
    @Test
    public void testEquals() {
        RangeList rangeList0 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8));
        RangeList rangeList1 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8));
        RangeList rangeList2 = new RangeList().add(new Range(1, 5)).add(new Range(6, 8)).add(new Range(10, 11));
        Assertions.assertEquals(rangeList0, rangeList1);
        Assertions.assertNotEquals(rangeList0, rangeList2);
        Assertions.assertNotEquals(rangeList1, rangeList2);
        log.info("测试RangeList#equals 包含一样的内容的两个对象equals是否相等 成功");
    }

    /**
     * remove的各种情况
     * 空集
     * 最左边不存在重合的range
     * 最右边不存在重合的range
     * 和某个range左边有重合部分
     * 和某个range右边有重合部分
     * 跨多个range
     * 某个相同的range
     *
     * @see RangeList#remove(Range)
     */
    @DisplayName("remove函数")
    @Order(3)
    @Test
    public void testRemove() {
        RangeList rangeList = new RangeList();
        rangeList.add(new Range(1, 8)).add(new Range(10, 21));
        log.info("初始时:rangeList:{}", rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");
        //空集
        rangeList.remove(new Range(10, 10));
        log.info("移除 {} 后 rangeList:{}", new Range(10, 10), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");
        //最左边不存在重合的range
        rangeList.remove(new Range(-5, -3));
        log.info("移除 {} 后 rangeList:{}", new Range(-5, -3), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");
        //最右边不存在重合的range
        rangeList.remove(new Range(35, 37));
        log.info("移除 {} 后 rangeList:{}", new Range(15, 17), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");

        //和某个range左边有重合部分
        rangeList.remove(new Range(10, 12));
        log.info("移除 {} 后 rangeList:{}", new Range(15, 17), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [12, 21)");

        //和某个range右边有重合部分
        rangeList.remove(new Range(6, 8));
        log.info("移除 {} 后 rangeList:{}", new Range(6, 8), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 6) [12, 21)");

        //跨多个range
        rangeList.remove(new Range(3, 19));
        log.info("移除 {} 后 rangeList:{}", new Range(3, 19), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 3) [19, 21)");

        //某个相同的range
        rangeList.remove(new Range(1, 3));
        log.info("移除 {} 后 rangeList:{}", new Range(1, 3), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[19, 21)");

    }


}
