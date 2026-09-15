/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import graphcollection.util.MutableQueue;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class DijkstraShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        extends ShortestPaths<V, E> {

    protected HashMap<V, Integer> finished = new HashMap<V, Integer>();

    public DijkstraShortestPaths(Graph<V, E> g, V s) {
        super(g, s);
        shortestPathsSearch();
    }

    @Override
    protected final void shortestPathsSearch() {
        //----------------------------
        MutableQueue<VertexDistance<V, Number>> Q
                = new MutableQueue<VertexDistance<V, Number>>(graph.verticesNum());
        //----------------------------
        for (V u : graph) {
            finished.put(u, Q.add(new VertexDistance<V, Number>(u, h.get(u).doubleValue())));
        }
        //-----------------------------
        while (!Q.isEmpty()) {
            V u = Q.pop().vertex;
            Iterator<E> outEdges = graph.outEdgeIterator(u);
            while (outEdges.hasNext()) {
                E e = outEdges.next();
                Number w = e.getWeight();
                V v = e.target();
                if (finished.get(v) != -1 && relaxed(u, v, w)) {
                    h.put(v, h.get(u).doubleValue() + w.doubleValue());
                    p.put(v, u);
                    Q.update(finished.get(v), new VertexDistance<V, Number>(v, h.get(v).doubleValue()));
                }
            }
            finished.put(u, -1);
        }
        //----------------------------------------------       
    }

} //End of class DijkstraShortestPaths<V,E>
