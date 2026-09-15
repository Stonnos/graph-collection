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
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class BellmanFordShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        extends ShortestPaths<V, E> {

    public BellmanFordShortestPaths(Graph<V, E> g, V s) {
        super(g, s);
        shortestPathsSearch();
    }

    @Override
    protected final void shortestPathsSearch() {
        for (int i = 0; i < graph.verticesNum() - 1; i++) {
            Iterator<E> edge = graph.edgeIterator();
            while (edge.hasNext()) {
                E e = edge.next();
                relax(e.source(), e.target(), e.getWeight());
            }
        }
        //----------------------------------------------------
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            E e = edge.next();
            if (relaxed(e.source(), e.target(), e.getWeight())) {
                decision = false;
                return;
            }
        }
        //----------------------------------------------------
    }

} //End of class BellmanFordShortestPaths<V,E>
