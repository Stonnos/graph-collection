/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author Рома
 */
public class HashMultiSet<E> extends AbstractSet<E> {

    private HashMap<E, ArrayList<E>> set = new HashMap<>();
    private int size;
    private int modCount;

    @Override
    public int size() {
        return size;
    }

    public int keysSize() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object val) {
        return set.containsKey((E) val);
    }

    @Override
    public void clear() {
        for (Map.Entry<E, ArrayList<E>> entry : set.entrySet()) {
            entry.getValue().clear();
        }
        set.clear();
        size = 0;
        modCount++;
    }

    @Override
    public boolean remove(Object val) {
        ArrayList<E> list = set.remove((E) val);
        if (list != null) {
            size -= list.size();
            list.clear();
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add(E val) {
        if (!set.containsKey(val)) {
            ArrayList<E> list = new ArrayList<>();
            list.add(val);
            set.put(val, list);
        } else {
            ArrayList<E> list = set.get(val);
            list.add(val);
        }
        size++;
        modCount++;
        return true;
    }

    public Iterator<E> keysIterator() {
        return set.keySet().iterator();
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator(set.entrySet().iterator());
    }

    public Collection<E> elements(E key) {
        return set.containsKey(key) ? (Collection<E>) set.get(key).clone() : null;
    }

    /**
     *
     */
    private class HashSetIterator implements Iterator<E> {

        Iterator<Map.Entry<E, ArrayList<E>>> iter;
        Iterator<E> keys;
        int current;
        final int expectedCount = modCount;

        HashSetIterator(Iterator<Map.Entry<E, ArrayList<E>>> iter) {
            this.iter = iter;
        }

        void checkForComodification() {
            if (modCount != expectedCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public E next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (keys == null) {
                keys = iter.next().getValue().iterator();
            }
            E val = null;
            if (keys.hasNext()) {
                current++;
                val = keys.next();
                if (!keys.hasNext()) {
                    keys = null;
                }
            }
            return val;
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }
    }

}
