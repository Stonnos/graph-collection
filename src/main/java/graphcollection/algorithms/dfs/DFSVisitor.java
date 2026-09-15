/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @param <V>
 * @author Рома
 */
public class DFSVisitor<V> implements GraphAlgorithm {

    private LinkedList<V> vertices = new LinkedList<V>();
    private HashMap<V, Integer> color = new HashMap<V, Integer>();
    private boolean acyclic = true;

    public DFSVisitor(Graph<V, ? extends Edge<V>> g) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        search(g);
    }

    private void dfsVisit(Graph<V, ? extends Edge<V>> g, V u) {
        color.put(u, VertexColor.GRAY);
        Iterator<V> adjV = g.adjacencyIterator(u);
        //-------------------------
        while (adjV.hasNext()) {
            V v = adjV.next();
            if (color.get(v).equals(VertexColor.GRAY)) {
                acyclic = false;
            }
            if (color.get(v).equals(VertexColor.WHITE)) {
                dfsVisit(g, v);
            }
        }
        //-------------------------
        color.put(u, VertexColor.BLACK);
        vertices.addFirst(u);
    }

    private void initialize(Graph<V, ? extends Edge<V>> graph) {
        for (V u : graph) {
            color.put(u, VertexColor.WHITE);
        }
    }

    public Collection<V> sequence() {
        return vertices;
    }

    public boolean acyclic() {
        return acyclic;
    }

    private void search(Graph<V, ? extends Edge<V>> graph) {
        initialize(graph);
        for (V u : graph) {
            if (color.get(u).equals(VertexColor.WHITE)) {
                dfsVisit(graph, u);
            }
        }
    }
} //End of class DFSVisitor<V>
