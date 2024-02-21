package com.su.problem;

import com.su.problem.utils.RangeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程不安全，请在单线程中访问 not threadsafe
 * 本类是许多range的集合体
 * A range list is an aggregate of some ranges.
 * like ranges: [1, 5), [10, 11), [100, 201)
 * 规则是 list内的range的顺序递增，不会有任何一个元素存在于两个range（任意range之间不会交叉），也不会有存在两个range可以用一个range表示（任意range之间不可连接）
 * {@link Range}
 */
public class RangeList {
    /**
     * 本list元素个数
     */
    int size;
    /**
     * 本list有多少个Range
     */
    int nodeCount;
    /**
     * 头节点，final
     */
    private final Node head = new HeadNode();
    /**
     * 尾节点，final
     */
    private final Node tail = new TailNode();

    /**
     * 构造空list
     */
    public RangeList() {
        head.setNext(tail);
        tail.setPrev(head);
    }

    /**
     * 将range添加进list
     * <p>
     * 流程是
     * 1. 遍历list。
     * 2. 用一个变量tmpRange去合并list的每个元素，能合并就把合并后的range赋值给tmpRange，然后把list中的原range删除掉。
     * 3. 直到遇到某个元素的valGe中比tmpRange的valLe还大，就在那个元素前面插入tmpRange，流程结束。
     * 4. 如果经过前面的步骤并没有插入数据，等遍历结束后直接在尾部插入tmpRange即可。
     * <p>
     * Adds a range to the list
     *
     * @param {Array<number>} range - Array of two integers that specify beginning and
     *                        end of range.
     */
    public RangeList add(Range range) {
        if (range == null) {
            throw new IllegalArgumentException("add range, param can't be null");
        }
        if (range.size() == 0) {
            return this;
        }
        if (range.getValGe() == range.getValLt()) {
            return this;
        }
        Range tmpRange = range;
        Node<Range> tmpNode = head.next;
        boolean resloved = false;
        while (!(tmpNode instanceof TailNode)) {
            Range item = tmpNode.item;
            Node<Range> baseNode = tmpNode;
            tmpNode = tmpNode.next;
            //吸纳了就把原来的node删除掉，因为node的数值已经被attract包含了，后面添加attract节点即可
            Range attract = tmpRange.attract(item);
            if (tmpRange != attract) {
                remove0(baseNode);
                tmpRange = attract;
                continue;
            }
            //遇到了该插入的地方就提前结束循环
            if (tmpRange.getValLt() < baseNode.item.getValGe()) {
                addBefore0(baseNode, new Node(null, attract, null));
                resloved = true;
                break;
            }
        }
        /**上面没有添加的情况
         * 1.当前是空list
         * 2.后面没有节点比它大
         */
        if (!resloved) {
            Node node = new Node(null, tmpRange, null);
            addLast0(node);
        }
        return this;
    }

    /**
     * 删除list中range的元素
     * <p>
     * 流程是
     * 1.遍历list
     * 2.list的每个range都去减去参数range的值，有4种情况可以出现，1:全部剩下，没有减员，2:全部减完了没有剩下，3:剩下一部分（左半边的或右半边的），4:剩下两部分（中间的被减去了）
     * 3.删除原来的节点，增加剩下的range作为节点，没有就不加
     * <p>
     * Removes a range from the list
     *
     * @param {Array<number>} range - Array of two integers that specify beginning and
     *                        end of range.
     */
    public RangeList remove(Range range) {
        if (range == null) {
            throw new IllegalArgumentException("add range, param can't be null");
        }
        if (range.size() == 0) {
            return this;
        }
        Node<Range> tmpNode = head.next;
        while (!(tmpNode instanceof TailNode)) {
            Range item = tmpNode.item;
            Node<Range> baseNode = tmpNode;
            tmpNode = tmpNode.next;
            //不会发生减员的情况就continue
            if (item.getValLt() <= range.getValGe()) {
                continue;
            }

            List<Range> rangeList = RangeUtil.subRange(item, range);
            for (Range range1 : rangeList) {
                Node node = new Node(null, range1, null);
                addBefore0(baseNode, node);
            }
            remove0(baseNode);
            //提前结束循环，之后不会发生减员的情况了
            if (range.getValLt() <= baseNode.item.getValGe()) {
                break;
            }
        }
        return this;
    }

    /**
     * 将list转化为string格式
     * Convert the list of ranges in the range list to a string
     *
     * @returns A string representation of the range list
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Node tmp = head.next;
        if (tmp instanceof TailNode) {
            return "";
        }
        while (!(tmp instanceof TailNode)) {
            stringBuilder.append(tmp.item.toString()).append(" ");
            tmp = tmp.next;
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * 内部方法 链表的最后增加节点
     */
    private void addLast0(Node<Range> node) {
        Node prev = tail.prev;
        prev.next = node;
        node.next = tail;
        node.prev = prev;
        tail.prev = node;
        nodeCount++;
        size += node.item.size();
    }

    /**
     * 内部方法 链表的指定节点之后增加节点
     */
    private void addAfter0(Node<Range> node, Node<Range> newNode) {
        newNode.prev = node;
        newNode.next = node.next;
        node.next.prev = newNode;
        node.next = newNode;
        nodeCount++;
        size += newNode.item.size();
    }

    /**
     * 内部方法 链表的指定节点之前增加节点
     */
    private void addBefore0(Node node, Node<Range> newNode) {
        newNode.prev = node.prev;
        newNode.next = node;
        node.prev.next = newNode;
        node.prev = newNode;
        nodeCount++;
        size += newNode.item.size();
    }

    /**
     * 内部方法 删除链表的指定节点
     */
    private void remove0(Node<Range> node) {
        if (node == null || node instanceof HeadNode || node instanceof TailNode) {
            return;
        }
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        nodeCount--;
        size -= node.item.size();
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        Node<Range> tmp = head.next;
        while (!(tmp instanceof TailNode)) {
            Range item = tmp.item;
            tmp = tmp.next;
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        }
        return hashCode;
    }

    /**
     * equals方法，判断两个list的内容是否一样
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof RangeList))
            return false;

        Node<Range> tmp1 = head.next;
        Node<Range> tmp2 = ((RangeList) obj).head.next;
        while ((!(tmp1 instanceof TailNode)) && (!(tmp2 instanceof TailNode))) {
            if (!tmp1.item.equals(tmp1.item)) {
                return false;
            }
            tmp1 = tmp1.next;
            tmp2 = tmp2.next;
        }
        return Objects.equals(tmp1.item, tmp2.item);
    }

    /**
     * 头节点
     */
    final class HeadNode extends Node {
    }

    /**
     * 尾节点
     */
    final class TailNode extends Node {
    }

    /**
     * 普通节点
     */
    private class Node<E> {
        @Setter
        @Getter
        E item;
        @Setter
        @Getter
        Node<E> next;
        @Setter
        @Getter
        Node<E> prev;

        Node() {
        }

        Node(E element) {
            this(null, element, null);
        }

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + item +
                    "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(item, node.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item);
        }
    }
}
