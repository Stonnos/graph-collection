/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.GraphPathBuilder;
import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @param <V>
 * @author Рома
 */
public class DepthFirstSearch<V> implements GraphAlgorithm {

    private Graph<V, ? extends Edge<V>> graph;
    private HashMap<V, Integer> d = new HashMap<V, Integer>();
    private HashMap<V, Integer> f = new HashMap<V, Integer>();
    private HashMap<V, V> p = new HashMap<V, V>();
    private HashMap<V, Integer> color = new HashMap<V, Integer>();
    private int time;

    public DepthFirstSearch(Graph<V, ? extends Edge<V>> g) {
        if (g == null) {
            throw new NullPointerException();
        }
        graph = g;
        search();
    }

    private void dfsVisit(V u) {
        color.put(u, VertexColor.GRAY);
        time++;
        d.put(u, time);
        Iterator<V> adjV = graph.adjacencyIterator(u);
        //-------------------------
        while (adjV.hasNext()) {
            V v = adjV.next();
            if (color.get(v).equals(VertexColor.WHITE)) {
                p.put(v, u);
                dfsVisit(v);
            }
        }
        //-------------------------
        color.put(u, VertexColor.BLACK);
        time++;
        f.put(u, time);
    }

    private void initialize() {
        for (V u : graph) {
            color.put(u, VertexColor.WHITE);
            d.put(u, 0);
            f.put(u, 0);
            p.put(u, u);
        }
    }

    public Map<V, Integer> discoveryTimes() {
        return d;
    }

    public Map<V, Integer> finishingTimes() {
        return f;
    }

    public Map<V, V> predecessors() {
        return p;
    }

    public Integer discoveryTime(V u) {
        return d.get(u);
    }

    public Integer finishingTime(V u) {
        return f.get(u);
    }

    private void search() {
        initialize();
        for (V u : graph) {
            if (color.get(u).equals(VertexColor.WHITE)) {
                dfsVisit(u);
            }
        }
    }

    public Collection<V> getPath(V v) {
        if (graph.containsVertex(v)) {
            return GraphPathBuilder.path(p, v);
        } else {
            return null;
        }
    }

} //End of class DepthFirstSearch<V>
