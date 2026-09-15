/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;

/**
 *
 * @author Рома
 * @param <E>
 */
public class MutableQueue<E>
        implements Cloneable, java.io.Serializable, Iterable<E> {

    private Comparator<? super E> comparator;
    private int freeHead;
    private int capacity;
    private ArrayList<ValueDescriptor<E>> pq;
    private ArrayList<Integer> qp;

    /**
     *
     * @param <E>
     */
    private static class ValueDescriptor<E> {

        public E item;
        public int index;

        ValueDescriptor() {
        }

        ValueDescriptor(E item) {
            this.item = item;
        }

        ValueDescriptor(E item, int index) {
            this(item);
            this.index = index;
        }

    } //End of class ValueDescriptor<E>

    public MutableQueue(int capacity) {
        this.capacity = capacity;
        create();
    }

    public MutableQueue(int capacity, Comparator<? super E> comparator) {
        this(capacity);
        this.comparator = comparator;
    }

    private void resize() {
        for (int i = qp.size(); i < capacity(); i++) {
            qp.add(i + 1);
        }
    }

    private void create() {
        pq = new ArrayList<ValueDescriptor<E>>(capacity + 1);
        qp = new ArrayList<Integer>(capacity);
        pq.add(new ValueDescriptor<E>());
        resize();
    }

    private int indexed() {
        int i = freeHead;
        freeHead = qp.get(freeHead);
        return i;
    }

    private void exch(int i, int j) {
        ValueDescriptor<E> t = pq.get(i);
        pq.set(i, pq.get(j));
        qp.set(pq.get(i).index, i);
        pq.set(j, t);
        qp.set(pq.get(j).index, j);
    }

    private void fixUp(int i) {
        while (i > 1 && compare(pq.get(i).item, pq.get(i / 2).item) == -1) {
            exch(i, i / 2);
            i = i / 2;
        }
    }

    private void fixDown(int i) {
        int j;
        while (i <= size() / 2) {
            if (2 * i < size() && compare(pq.get(2 * i + 1).item, pq.get(2 * i).item) == -1) {
                j = 2 * i + 1;
            } else {
                j = 2 * i;
            }

            if (compare(pq.get(j).item, pq.get(i).item) == -1) {
                exch(j, i);
                i = j;
            } else {
                i = size();
            }
        }
    }

    private void remove(int j) {
        exch(j, size());
        qp.set(pq.get(size()).index, freeHead);
        freeHead = pq.get(size()).index;
        pq.remove(pq.size() - 1);
        fixDown(j);
    }

    private int compare(E k1, E k2) {
        return (comparator == null ? ((Comparable<E>) k1).compareTo(k2)
                : comparator.compare(k1, k2));
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return pq.size() - 1;
    }

    /**
     * Возвращает емкость очереди
     *
     * @return емкость очереди
     */
    public int capacity() {
        return capacity;
    }

    public Comparator<? super E> comparator() {
        return comparator;
    }

    /**
     * Добавление элемента в очередь
     *
     * @param val
     * @return идентификатор элемента
     */
    public int add(E val) {
        if (val == null || size() == capacity()) {
            return -1;
        }
        int i = indexed();
        pq.add(new ValueDescriptor<E>(val, i));
        qp.set(i, size());
        fixUp(size());
        return i;
    }

    /**
     * Обновление приоритета элемента
     *
     * @param i
     * @param val
     * @return
     */
    public boolean update(int i, E val) {
        if (i < 0 || i >= capacity()) {
            return false;
        }
        int j = qp.get(i);
        if (j > size() || pq.get(j).index != i) {
            return false;
        }
        pq.get(j).item = val;
        fixUp(j);
        fixDown(j);
        return true;
    }

    /**
     * Удаление из очереди элемента с заданным идентификатором
     *
     * @param i
     * @return
     */
    public boolean erase(int i) {
        if (i < 0 || i >= capacity()) {
            return false;
        }
        int j = qp.get(i);
        if (j > size() || pq.get(j).index != i) {
            return false;
        }
        remove(j);
        fixUp(j);
        return true;
    }

    /**
     * Удаление элемента из начала очереди
     *
     * @return
     */
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        E val = pq.get(1).item;
        remove(1);
        return val;
    }

    @Override
    public Object clone() {
        MutableQueue<E> clone;
        try {
            clone = (MutableQueue<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        clone.freeHead = freeHead;
        clone.capacity = capacity;
        clone.pq = (ArrayList<ValueDescriptor<E>>) pq.clone();
        clone.qp = (ArrayList<Integer>) qp.clone();
        return clone;
    }

    /**
     *
     */
    private class QueueIterator implements Iterator<E> {

        private final Iterator<ValueDescriptor<E>> iter;

        public QueueIterator(Iterator<ValueDescriptor<E>> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public E next() {
            return iter.next().item;
        }

    } //End of class QueueIterator

    @Override
    public Iterator<E> iterator() {
        Iterator<ValueDescriptor<E>> iter = pq.iterator();
        iter.next();
        return new QueueIterator(iter);
    }

    public void clear() {
        if (!isEmpty()) {
            pq.clear();
            pq.add(new ValueDescriptor<E>());
            for (int i = 0; i < capacity(); i++) {
                qp.set(i, i + 1);
            }
            freeHead = 0;
        }
    }

} //End of class MutableQueue<E>
