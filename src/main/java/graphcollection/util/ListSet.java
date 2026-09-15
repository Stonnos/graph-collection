/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Рома
 */
public class ListSet<E> extends AbstractSet<E>
        implements Cloneable, java.io.Serializable {

    protected LinkedList<E> set = new LinkedList<E>();

    @Override
    public boolean contains(Object obj) {
        return set.contains(obj);
    }

    @Override
    public boolean remove(Object obj) {
        return set.remove(obj);
    }

    @Override
    public boolean add(E obj) {
        if (!contains(obj)) {
            set.add(obj);
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
        ListSet<E> anotherSet;
        try {
            anotherSet = (ListSet<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        anotherSet.set = (LinkedList<E>) set.clone();
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
        if (obj != null && obj instanceof ListSet<?>) {
            LinkedList<E> anotherSet = ((ListSet<E>) obj).set;
            return set.equals(anotherSet);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public String toString() {
        return set.toString();
    }

} //End of class ListSet<E>
