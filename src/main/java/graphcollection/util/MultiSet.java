/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.ListIterator;

/**
 *
 * @param <E>
 * @author Рома
 */
public class MultiSet<E> extends ListSet<E> {

    public MultiSet() {
    }

    @Override
    public boolean add(E obj) {
        ListIterator<E> iter = searchFirst(obj);
        if (iter != null) {
            iter.add(obj);
        } else {
            set.add(obj);
        }
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        ListIterator<E> iter = searchFirst(obj);
        if (iter != null) {
            iter.remove();
            while (iter.hasNext()) {
                if (iter.next().equals(obj)) {
                    iter.remove();
                } else {
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private ListIterator<E> searchFirst(Object obj) {
        ListIterator<E> iter = set.listIterator();
        while (iter.hasNext()) {
            if (iter.next().equals(obj)) {
                return iter;
            }
        }
        return null;
    }
} //End of class MultiSet<E>
