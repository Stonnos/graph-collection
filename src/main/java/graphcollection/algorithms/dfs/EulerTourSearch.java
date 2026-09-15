/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.Iterator;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class EulerTourSearch<V, E extends Edge<V>>
        extends AbstractTourSearch<V, E> {

    public EulerTourSearch(Graph<V, E> g) {
        super(g);
        decision = search(g);
    }

    private void eulerVisit(Graph<V, E> g, V u) {
        while (true) {
            Iterator<E> adjV = g.outEdgeIterator(u);
            if (adjV.hasNext()) {
                E e = adjV.next();
                V v = e.target();
                g.removeEdge(e);
                eulerVisit(g, v);
                tour.addFirst(v);
            } else {
                return;
            }
        }
    }

    @Override
    protected final boolean search(Graph<V, E> graph) {
        for (V u : graph) {
            int degV = graph.outEdgesNum(u);
            if (degV % 2 != 0 || degV == 0) {
                return false;
            }
        }
        //------------------------------
        Iterator<V> v = graph.iterator();
        if (v.hasNext()) {
            V s = v.next();
            eulerVisit(graph, s);
            tour.addFirst(s);
            return true;
        }
        return false;
    }

} //End of class EulerTourSearch<V,E>
