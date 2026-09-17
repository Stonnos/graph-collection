/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui.parse;


import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;

import java.util.Iterator;

/**
 *
 * @author Рома
 */
public class NumberParser {
    /**
     *
     * @param str
     * @return
     */
    public static Number parse(String str) {
        Number x = null;
        if (str != null) {
            try {
                x = Integer.parseInt(str);
            } catch (NumberFormatException exI) {
                try {
                    x = Double.parseDouble(str);
                } catch (NumberFormatException exD) {
                }
            }
        }
        return x;
    }

    public static <V, E extends WeightedEdge<V, ? extends Number>>
    boolean isNegativeWeights(Graph<V, E> g) {
        if (g == null) {
            return true;
        }
        Iterator<E> edge = g.edgeIterator();
        while (edge.hasNext()) {
            Number w = edge.next().getWeight();
            if (w == null || w.doubleValue() < 0) {
                return true;
            }
        }
        return false;
    }

    public static <V, E extends WeightedEdge<V, ? extends Number>>
    boolean isNullWeights(Graph<V, E> g) {
        if (g == null) {
            return true;
        }
        Iterator<E> edge = g.edgeIterator();
        while (edge.hasNext()) {
            if (edge.next().getWeight() == null) {
                return true;
            }
        }
        return false;
    }
}
