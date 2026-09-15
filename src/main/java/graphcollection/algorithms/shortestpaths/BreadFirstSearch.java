/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.GraphPathBuilder;
import graphcollection.algorithms.GraphPaths;
import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @param <V>
 * @author Рома
 */
public class BreadFirstSearch<V> implements GraphAlgorithm, GraphPaths<V> {

    protected Graph<V, ? extends Edge<V>> graph;
    protected V source;
    protected HashMap<V, V> p = new HashMap<V, V>();
    protected HashMap<V, Number> h = new HashMap<V, Number>();
    protected HashMap<V, Integer> color = new HashMap<V, Integer>();

    private void initialize() {
        for (V u : graph) {
            p.put(u, u);
            color.put(u, VertexColor.WHITE);
            h.put(u, Integer.MAX_VALUE);
        }
    }

    public BreadFirstSearch(Graph<V, ? extends Edge<V>> g, V s) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.containsVertex(s)) {
            throw new IllegalArgumentException("graph does not contains vertex " + s);
        }
        graph = g;
        source = s;
        search(s);
    }

    private void search(V s) {
        initialize();
        ArrayDeque<V> Q = new ArrayDeque<V>(graph.verticesNum());
        //-----------------------------
        h.put(s, 0);
        color.put(s, VertexColor.GRAY);
        Q.add(s);
        //-----------------------------
        while (!Q.isEmpty()) {
            V u = Q.pop();
            Iterator<V> adjV = graph.adjacencyIterator(u);
            while (adjV.hasNext()) {
                V v = adjV.next();
                if (color.get(v).equals(VertexColor.WHITE)) {
                    color.put(v, VertexColor.GRAY);
                    p.put(v, u);
                    h.put(v, h.get(u).intValue() + 1);
                    Q.add(v);
                }
            }
            color.put(u, VertexColor.BLACK);
        }
        //------------------------------
    }

    @Override
    public Map<V, V> predecessors() {
        return p;
    }

    @Override
    public Map<V, Number> distances() {
        return h;
    }

    @Override
    public boolean isPath(V target) {
        return graph.containsVertex(target)
                && (source.equals(target) || !p.get(target).equals(target));
    }

    @Override
    public Collection<V> getPath(V target) {
        if (isPath(target)) {
            return GraphPathBuilder.path(p, target);
        } else {
            return null;
        }
    }

    @Override
    public V getSource() {
        return source;
    }

    @Override
    public Number getDistance(V target) {
        return h.get(target);
    }

    @Override
    public V getPredecessor(V target) {
        return p.get(target);
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (V u : graph) {
            result.append("w[p(").append(getSource()).append(",")
                    .append(u).append(")] = ");
            if (isPath(u)) {
                result.append(getDistance(u));
            } else {
                result.append("infinity");
            }
            result.append(separator);
        }
        return result.toString();
    }

} //End of class BreadFirstSearch<V>
