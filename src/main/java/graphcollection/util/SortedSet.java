/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Рома
 * @param <E>
 */
public class SortedSet<E> extends AbstractSet<E>
        implements Cloneable, java.io.Serializable {

    protected ArrayList<E> set;
    protected Comparator<? super E> comparator;

    public SortedSet() {
        set = new ArrayList<E>();
    }

    public SortedSet(Comparator<? super E> comparator) {
        this();
        this.comparator = comparator;
    }

    public SortedSet(int capacity) {
        set = new ArrayList<E>(capacity);
    }

    public SortedSet(int capacity, Comparator<? super E> comparator) {
        this(capacity);
        this.comparator = comparator;
    }

    @Override
    public boolean contains(Object obj) {
        return (search((E) obj) != -1);
    }

    @Override
    public boolean remove(Object obj) {
        int i = search((E) obj);
        if (i != -1 && compare((E) obj, set.get(i)) == 0) {
            set.remove(i);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add(E obj) {
        int i = search(obj);
        if (i == -1 || compare(obj, set.get(i)) != 0) {
            set.add(i + 1, obj);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public Object clone() {
        SortedSet<E> anotherSet;
        try {
            anotherSet = (SortedSet<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        anotherSet.set = (ArrayList<E>) set.clone();
        return anotherSet;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof SortedSet<?>) {
            ArrayList<E> anotherSet = ((SortedSet<E>) obj).set;
            return set.equals(anotherSet);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    protected final int search(E obj) {
        int l = 0, r = size() - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (compare(obj, set.get(m)) == 0) {
                return m;
            }
            if (compare(obj, set.get(m)) > 0) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return r;
    }

    protected final int compare(E k1, E k2) {
        return (comparator == null ? ((Comparable<E>) k1).compareTo(k2)
                : comparator.compare(k1, k2));
    }

    @Override
    public String toString() {
        return set.toString();
    }

} //End of class SortedSet<E>
