/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms;

import graphcollection.graph.Edge;
import graphcollection.graph.Graph;
import graphcollection.graph.HashSetGraph;

import java.util.Iterator;

/**
 *
 * @author Рома
 */
public class GraphReverse {

    public static <V, E extends Edge<V>> Graph<V, E> reverse(Graph<V, E> graph) {
        if (graph == null || !graph.direction()) {
            return graph;
        }
        Graph<V, E> reverseGraph = new HashSetGraph<V, E>(true);
        //----------------------------------------------------
        for (V v : graph) {
            reverseGraph.addVertex(v);
        }
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            E e = edge.next();
            reverseGraph.addEdge((E) new Edge<V>(true, e.target(), e.source()));
        }
        return reverseGraph;
    }
}
