package com.su.problem.utils;

import com.su.problem.Range;

import java.util.LinkedList;
import java.util.List;

public class RangeUtil {

    public static void checkRangeValExisted(Range range) {
        assert range != null;
        assert range.size() > 0;
    }


    /**
     * 求差集
     * 结果有4种情况
     * 没有减掉，全部剩下来
     * 没有剩下的range
     * 剩下两个range
     * 剩下一个range
     *
     * @param minuend    原始集合，被减的集合 必须包含元素
     * @param subtrahend 需要减去的集合 必须包含元素
     * @return 差集
     */
    public static List<Range> subRange(Range minuend, Range subtrahend) {
        checkRangeValExisted(minuend);
        checkRangeValExisted(subtrahend);
        List list = new LinkedList();
        //全部减完，没有剩下的range
        if (subtrahend.getValGe() <= minuend.getValGe() && subtrahend.getValLt() >= minuend.getValLt()) {
            return list;
        }
        //剩下两个range
        if (subtrahend.getValGe() > minuend.getValGe() && subtrahend.getValLt() < minuend.getValLt()) {
            list.add(new Range(minuend.getValGe(), subtrahend.getValGe()));
            list.add(new Range(subtrahend.getValLt(), minuend.getValLt()));
            return list;
        }
        //剩下左边一部分
        if (minuend.containsValue(subtrahend.getValGe()) && subtrahend.getValLt() >= minuend.getValLt()) {
            list.add(new Range(minuend.getValGe(), subtrahend.getValGe()));
            return list;
        }
        //剩下右边一部分
        if (minuend.containsValue(subtrahend.getValLt()) && subtrahend.getValGe() <= minuend.getValGe()) {
            list.add(new Range(subtrahend.getValLt(), minuend.getValLt()));
            return list;
        }
        /**
         * 搭配 {@link com.su.problem.RangeList} 来，因为RangeList已经把全部剩下的情况排除了，所以放在最后判断，但是为了代码完整性，需要有这段逻辑
         * 两个没有重合的range，没有减掉，全部剩下来
         */
        if (subtrahend.getValGe() >= minuend.getValLt() || subtrahend.getValLt() <= minuend.getValGe()) {
            list.add(minuend);
            return list;
        }
        //理论上不应该执行到这里来
        throw new RuntimeException("");
    }

}
