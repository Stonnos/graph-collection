/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.Comparator;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class EdgeComparator<V, E extends Edge<V>>
        implements Comparator<E> {

    /**
     * Сравнение двух ребер
     *
     * @param e1 ребро
     * @param e2 ребро
     * @return результат сравнения
     */
    @Override
    public int compare(E e1, E e2) {
        checkEdges(e1, e2);
        //--------------------------
        if ((((Comparable<V>) e1.source()).compareTo(e2.source()) == 0
                && ((Comparable<V>) e1.target()).compareTo(e2.target()) == 0)
                || (!e1.direction() && ((Comparable<V>) e1.source()).compareTo(e2.target()) == 0
                && ((Comparable<V>) e1.target()).compareTo(e2.source()) == 0)) {
            return 0;
        } else if (((Comparable<V>) e1.source()).compareTo(e2.source()) == 0) {
            return ((Comparable<V>) e1.target()).compareTo(e2.target());
        } else {
            return ((Comparable<V>) e1.source()).compareTo(e2.source());
        }
    }

    private void checkEdges(E e1, E e2) {
        if (e1.direction() != e2.direction()) {
            throw new IllegalArgumentException("Different types of edges " + e1
                    + " and " + e2);
        }
    }

} //End of class EdgeComparator<V, E>
