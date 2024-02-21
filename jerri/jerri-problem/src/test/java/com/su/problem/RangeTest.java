package com.su.problem;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@DisplayName("range单测")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RangeTest {

    @DisplayName("构造函数")
    @SneakyThrows
    @Order(-1)
    @Test
    public void testNewInstance() {
        System.out.println("testNewInstance");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Range(3, 2));
    }

    @DisplayName("toString函数")
    @SneakyThrows
    @Order(0)
    @Test
    public void testToString() {
        Range range0 = new Range(0, 4);
        Assertions.assertEquals("[0, 4)", range0.toString());
        Range range1 = new Range(3, 3);
        Assertions.assertEquals("[3, 3)", range1.toString());
    }

    @DisplayName("hashCode函数")
    @SneakyThrows
    @Order(1)
    @Test
    public void testHashCode2() {
        Assertions.assertEquals(new Range(1, 4).hashCode(), new Range(1, 4).hashCode());
        Assertions.assertEquals(new Range(2, 3).hashCode(), new Range(2, 3).hashCode());

        Range range0 = new Range(0, 4);
        Range range1 = new Range(1, 4);
        Range range2 = new Range(1, 5);
        Range range3 = new Range(2, 3);
        Range range4 = new Range(3, 4);
        Range range5 = new Range(4, 4);
        Range range6 = new Range(5, 14);
        Range range7 = new Range(6, 11);
        Range range8 = new Range(7, 8);
        Range range9 = new Range(8, 25);
        Set<Integer> set = new HashSet<>();

        set.add(range0.hashCode());
        set.add(range1.hashCode());
        set.add(range2.hashCode());
        set.add(range3.hashCode());
        set.add(range4.hashCode());
        set.add(range5.hashCode());
        set.add(range6.hashCode());
        set.add(range7.hashCode());
        set.add(range8.hashCode());
        set.add(range9.hashCode());

        Assertions.assertTrue(set.size() >= 5);

    }


    @DisplayName("equals函数")
    @SneakyThrows
    @Order(2)
    @Test
    public void testEquals() {
        Range range0 = new Range(5, 6);
        Range range1 = new Range(5, 6);
        Range range2 = new Range(4, 5);
        Range range3 = new Range(6, 7);
        Assertions.assertEquals(range0, range1);
        Assertions.assertNotEquals(range0, range2);
        Assertions.assertNotEquals(range1, range3);
        Assertions.assertNotEquals(range2, range3);
    }

    @DisplayName("是否包含某值")
    @SneakyThrows
    @Order(3)
    @Test
    public void testContainsValue() {
        Range range0 = new Range(5, 6);
        Assertions.assertFalse(range0.containsValue(1));
        Assertions.assertTrue(range0.containsValue(5));
        Assertions.assertFalse(range0.containsValue(6));
        Assertions.assertFalse(range0.containsValue(7));

        Range range1 = new Range(8, 8);
        Assertions.assertFalse(range1.containsValue(7));
        Assertions.assertFalse(range1.containsValue(8));
        Assertions.assertFalse(range1.containsValue(9));
    }

    @DisplayName("吸纳另一个range")
    @SneakyThrows
    @Order(4)
    @Test
    public void testAttract() {
        Range range0 = new Range(6, 10);
        Map<Range, Boolean> map = new HashMap<>();
        map.put(range0, true);
        map.put(new Range(1, 3), false);
        map.put(new Range(1, 6), true);
        map.put(new Range(1, 8), true);
        map.put(new Range(1, 10), true);
        map.put(new Range(1, 13), true);
        map.put(new Range(6, 8), true);
        map.put(new Range(6, 10), true);
        map.put(new Range(6, 13), true);
        map.put(new Range(8, 9), true);
        map.put(new Range(8, 10), true);
        map.put(new Range(8, 13), true);
        map.put(new Range(10, 11), true);
        map.put(new Range(12, 15), false);

        for (Map.Entry<Range, Boolean> entry : map.entrySet()) {
            Range key = entry.getKey();
            Boolean value = entry.getValue();
            Range merged = range0.attract(key);
            log.info("range {} 吸纳 {} 结果:{} 可吸纳: {}", range0, key, merged, value);
            if (value) {
                Assertions.assertTrue(value);
            } else {
                Assertions.assertFalse(value);
            }

        }

        Assertions.assertDoesNotThrow(() -> range0.attract(new Range(3, 3)));
    }

}
