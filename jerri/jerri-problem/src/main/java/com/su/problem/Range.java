package com.su.problem;

import lombok.Getter;

import java.util.Objects;

/**
 * 用一对int数字定义一个range，代表了一些数字的集合，这些数字是连续的并满足>=ge,且<lt
 * 特别的，[20, 20)表示为空，因为没有int型的数字能满足 >=20,且<20
 * A pair of integers define a range
 * for example: [1, 5). This range includes integers: 1, 2, 3, and 4.
 */
public class Range {
    /**
     * 大于等于的值
     * 定义为int，后续不需要使用equals来比较大小，直接使用==来比较
     */
    @Getter
    private final int valGe;
    /**
     * 小于的值
     * 定义为int，后续不需要使用equals来比较大小，直接使用==来比较
     */
    @Getter
    private final int valLt;

    /**
     * 构造函数，需要满足 valGe<=valLt
     *
     * @param valGe
     * @param valLt
     */
    public Range(int valGe, int valLt) {
        initCheck(valGe, valLt);
        this.valGe = valGe;
        this.valLt = valLt;
    }

    /**
     * 校验valGe和valLt
     */
    private void initCheck(int valGe, int valLt) {
        if (valGe > valLt) {
            throw new IllegalArgumentException("Range的valGe不能大于valLt,valGe=" + valGe + ",valLt=" + valLt);
        }
    }

    /**
     * 判断probe是否在本range中
     * judge if probe in this range
     *
     * @param probe
     * @return 本range是否包含probe
     */
    public boolean containsValue(int probe) {
        if (size() == 0) {
            return false;
        }
        return (probe >= valGe && probe < valLt);
    }

    /**
     * 本range包含的int数字个数
     * Returns the number of elements in this range.
     *
     * @return the number of elements in this range.
     */
    public int size() {
        return valLt - valGe;
    }

    /**
     * 重写了hashCode
     * Returns the hash code value for this list.
     *
     * @return the hash code value for this list.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.valGe) ^ Objects.hashCode(this.valLt);
    }

    /**
     * 重写了equals
     * 比较两个range包含的元素是否相同
     *
     * @param obj the object to be compared for equality with this list
     * @return 比较两个range包含的元素是否相同
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Range) {
            Range range = (Range) obj;
            return valGe == range.getValGe() && valLt == range.getValLt();
        }
        return false;
    }

    /**
     * 重写了toString
     *
     * @return
     */
    @Override
    public String toString() {
        return "[" + valGe + ", " + valLt + ")";
    }

    /**
     * 用于吸纳另一个range
     * 什么是吸纳？就是用一个range表示两个range的值（本range和另一个range）
     *
     * @param other range
     * @return 能吸纳就返回合并的新对象range，不能吸纳就返回this
     */
    public Range attract(Range other) {
        if (other.size() == 0 || this == other) {
            return this;
        }
        if (size() == 0) {
            return other;
        }
        if (valGe > other.valLt + 1 || valLt + 1 < other.valGe) {
            return this;
        }
        int newValGe = Math.min(valGe, other.valGe);
        Integer newValLt = Math.max(valLt, other.valLt);
        return new Range(newValGe, newValLt);
    }


}
