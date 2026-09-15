/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.algorithms.dfs.TopologicalSort;
import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import java.util.Iterator;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class DAGShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        extends ShortestPaths<V, E> {

    public DAGShortestPaths(Graph<V, E> g, V s) {
        super(g, s);
        shortestPathsSearch();
    }

    @Override
    protected final void shortestPathsSearch() {
        TopologicalSort<V> topoSort = new TopologicalSort<V>(graph);
        if (topoSort.acyclic()) {
            for (V u : topoSort.sequence()) {
                Iterator<E> edge = graph.outEdgeIterator(u);
                while (edge.hasNext()) {
                    E e = edge.next();
                    relax(e.source(), e.target(), e.getWeight());
                }
            }
        } else {
            decision = false;
        }
    }

} //End of class DAGShortestPaths<V,E>
