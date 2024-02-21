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

    @DisplayName("add和toString函数")
    @Order(0)
    @Test
    public void testAdd() {
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

        rangeList.add(new Range(3, 15));
        log.info("添加 {} 后 rangeList:{}", new Range(3, 15), rangeList);
        Assertions.assertEquals(rangeList.toString(), "[1, 21)");


        RangeList rangeList1 = new RangeList();

        rangeList1.add(new Range(1, 3));
        rangeList1.add(new Range(5, 6));
        rangeList1.add(new Range(8, 10));
        rangeList1.add(new Range(12, 15));
        rangeList1.add(new Range(17, 19));
        rangeList1.add(new Range(25, 29));
        rangeList1.add(new Range(2, 10));

        RangeList rangeList3 = new RangeList();
        Assertions.assertEquals("", rangeList3.toString());
        rangeList3.add(new Range(1, 10));
        Assertions.assertEquals("[1, 10)", rangeList3.toString());
        rangeList3.add(new Range(15, 21));
        Assertions.assertEquals("[1, 10) [15, 21)", rangeList3.toString());
        rangeList3.add(new Range(310, 612));
        Assertions.assertEquals("[1, 10) [15, 21) [310, 612)", rangeList3.toString());

    }

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
    }

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
    }


    @DisplayName("remove函数")
    @Order(3)
    @Test
    public void testRemove() {
        RangeList rangeList = new RangeList();
        rangeList.add(new Range(1, 8)).add(new Range(10, 21));
        log.info("初始时:rangeList:{}", rangeList);
        rangeList.remove(new Range(10, 10));
        log.info("移除 {} 后 rangeList:{}", new Range(10, 10), rangeList);
//        Assertions.assertEquals(rangeList, new RangeList().add(new Range(1, 8)).add(new Range(10, 21)));
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [10, 21)");
        rangeList.remove(new Range(10, 11));
        log.info("移除 {} 后 rangeList:{}", new Range(10, 11), rangeList);
//        Assertions.assertEquals(rangeList, new RangeList().add(new Range(1, 8)).add(new Range(11, 21)));
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [11, 21)");
        rangeList.remove(new Range(15, 17));
        log.info("移除 {} 后 rangeList:{}", new Range(15, 17), rangeList);
//        Assertions.assertEquals(rangeList, new RangeList().add(new Range(1, 8)).add(new Range(11, 15)).add(new Range(17, 21)));
        Assertions.assertEquals(rangeList.toString(), "[1, 8) [11, 15) [17, 21)");
        rangeList.remove(new Range(3, 19));
        log.info("移除 {} 后 rangeList:{}", new Range(3, 19), rangeList);
//        Assertions.assertEquals(rangeList, new RangeList().add(new Range(1, 3)).add(new Range(19, 21)));
        Assertions.assertEquals(rangeList.toString(), "[1, 3) [19, 21)");

    }


}
