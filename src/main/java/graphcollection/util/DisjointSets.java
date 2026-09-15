/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @param <E>
 * @author Рома
 */
public class DisjointSets<E> implements Cloneable,
        Iterable<E>, java.io.Serializable {

    private HashMap<E, E> p = new HashMap<E, E>();
    private HashMap<E, Integer> rank = new HashMap<E, Integer>();
    private int size;
    private int setsSize;

    private E link(E x, E y) {
        E z = null;
        if (p.containsKey(x) && p.containsKey(y)) {
            if (rank.get(x) > rank.get(y)) {
                p.put(y, x);
                z = x;
            } else {
                p.put(x, y);
                if (rank.get(x).equals(rank.get(y))) {
                    rank.put(y, rank.get(y) + 1);
                }
                z = y;
            }
            setsSize--;
        }
        return z;
    }

    public boolean makeSet(E x) {
        if (!p.containsKey(x)) {
            p.put(x, x);
            rank.put(x, 0);
            size++;
            setsSize++;
        }
        return false;
    }

    public E findSet(E x) {
        if (!x.equals(p.get(x))) {
            p.put(x, findSet(p.get(x)));
        }
        return p.get(x);
    }

    public E union(E x, E y) {
        return link(findSet(x), findSet(y));
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        p.clear();
        rank.clear();
        setsSize = size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return p.keySet().iterator();
    }

    @Override
    public Object clone() {
        DisjointSets<E> set;
        try {
            set = (DisjointSets<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        set.p = (HashMap<E, E>) p.clone();
        set.rank = (HashMap<E, Integer>) rank.clone();
        set.size = size;
        set.setsSize = setsSize;
        return set;
    }

}
