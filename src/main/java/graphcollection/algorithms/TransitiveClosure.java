/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms;

import graphcollection.graph.Edge;
import graphcollection.graph.Graph;
import graphcollection.util.MatrixMap;
import java.util.Iterator;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class TransitiveClosure<V, E extends Edge<V>> implements GraphAlgorithm {

    private Graph<V, E> graph;
    private MatrixMap<V, V, Boolean> t = new MatrixMap<V, V, Boolean>();

    public TransitiveClosure(Graph<V, E> g) {
        checkGraph(g);
        graph = g;
        initialize();
        //---------------------------------
        for (V k : graph) {
            for (V i : graph) {
                for (V j : graph) {
                    t.put(i, j, t.get(i, j) || (t.get(i, k) && t.get(k, j)));
                }
            }
        }
        //----------------------------------------------
    }

    private void initialize() {
        for (V i : graph) {
            for (V j : graph) {
                if (i.equals(j)) {
                    t.put(i, j, true);
                } else {
                    t.put(i, j, false);
                }
            }
        }
        //---------------------------------------
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            E e = edge.next();
            t.put(e.source(), e.target(), true);
        }
        //----------------------------------------
    }

    private void checkGraph(Graph<V, E> g) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
    }

    public MatrixMap<V, V, Boolean> transitiveClosureMatrix() {
        return t;
    }

    public boolean isPath(V u, V v) {
        if (graph.containsVertex(u) && graph.containsVertex(v)) {
            return t.get(u, v);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (V u : graph) {
            for (V v : graph) {
                result.append("t(").append(u).append(",")
                        .append(v).append(") = ").append(t.get(u, v)).append(separator);
            }
        }
        return result.toString();
    }

} //End of class TransitiveClosure<V,E>
