/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;

import java.util.Iterator;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class FloydWarshallAllPairsShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        extends AllPairsShortestPaths<V, E> {

    public FloydWarshallAllPairsShortestPaths(Graph<V, E> g) {
        super(g);
        searchAllShortestPaths();
    }

    @Override
    protected final void searchAllShortestPaths() {
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            E e = edge.next();
            p.put(e.source(), e.target(), e.source());
            h.put(e.source(), e.target(), e.getWeight());
        }
        //---------------------------------
        for (V k : graph) {
            for (V i : graph) {
                for (V j : graph) {
                    if (h.get(i, k).doubleValue() != max_distance
                            && h.get(k, j).doubleValue() != max_distance) {
                        min(i, j, k);
                    }
                }
            }
        }
        //----------------------------------------------
        for (V u : graph) {
            if (h.get(u, u).doubleValue() < 0.0) {
                decision = false;
                return;
            }
        }
    }

    private void min(V u, V v, V k) {
        double distance = h.get(u, k).doubleValue() + h.get(k, v).doubleValue();
        if (distance < h.get(u, v).doubleValue()) {
            h.put(u, v, distance);
            p.put(u, v, p.get(k, v));
        }
    }

} //End of class FloydWarshallAllPairsShortestPaths<V,E>
