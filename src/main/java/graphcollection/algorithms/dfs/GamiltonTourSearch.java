/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @param <V>
 * @author Рома
 */
public class GamiltonTourSearch<V, E extends Edge<V>>
        extends AbstractTourSearch<V, E> {

    private HashMap<V, Integer> color = new HashMap<V, Integer>();

    public GamiltonTourSearch(Graph<V, E> g) {
        super(g);
        decision = search(g);
    }

    @Override
    protected final boolean search(Graph<V, E> graph) {
        for (V u : graph) {
            color.put(u, VertexColor.WHITE);
        }
        //-----------------------------------------
        Iterator<V> vertex = graph.iterator();
        if (vertex.hasNext()) {
            V s = vertex.next();
            Iterator<V> adjV = graph.adjacencyIterator(s);
            while (adjV.hasNext()) {
                V v = adjV.next();
                if (GamiltonVisit(graph, s, v, graph.verticesNum() - 1)) {
                    tour.add(s);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean GamiltonVisit(Graph<V, ? extends Edge<V>> g, V s, V u, int d) {
        if (u.equals(s) && d == 0) {
            tour.addFirst(u);
            return true;
        }
        color.put(u, VertexColor.GRAY);
        tour.addFirst(u);
        //----------------------------------------------------
        Iterator<V> adjV = g.adjacencyIterator(u);
        while (adjV.hasNext()) {
            V v = adjV.next();
            if (color.get(v).equals(VertexColor.WHITE)) {
                if (GamiltonVisit(g, s, v, d - 1)) {
                    return true;
                }
            }
        }
        //----------------------------------------------------
        tour.removeFirst();
        color.put(u, VertexColor.WHITE);
        return false;
    }

} //End of class GamiltonTourSearch<V, E>
