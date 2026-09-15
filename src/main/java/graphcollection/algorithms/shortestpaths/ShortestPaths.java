/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.GraphPaths;
import graphcollection.algorithms.GraphPathBuilder;
import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public abstract class ShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        implements GraphAlgorithm, GraphPaths<V> {
    
    protected Graph<V, E> graph;
    protected V source;
    protected HashMap<V, V> p = new HashMap<V, V>();
    protected HashMap<V, Number> h = new HashMap<V, Number>();
    protected boolean decision = true;
    public static final double max_distance = Double.MAX_VALUE;
    
    protected abstract void shortestPathsSearch();
    
    protected ShortestPaths(Graph<V, E> g, V s) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.direction()) {
            throw new IllegalArgumentException("Illegal type of graph");
        }
        if (!g.containsVertex(s)) {
            throw new IllegalArgumentException("Graph does not contains vertex " + s);
        }
        graph = g;
        source = s;
        initialize();
    }
    
    private void initialize() {
        for (V u : graph) {
            h.put(u, max_distance);
            p.put(u, u);
        }
        h.put(source, 0.0);
    }
    
    protected boolean relaxed(V u, V v, Number w) {
        return (h.get(u).doubleValue() != max_distance
                && h.get(u).doubleValue() + w.doubleValue() < h.get(v).doubleValue());
    }
    
    protected void relax(V u, V v, Number w) {
        if (relaxed(u, v, w)) {
            h.put(v, h.get(u).doubleValue() + w.doubleValue());
            p.put(v, u);
        }
    }
    
    public boolean decision() {
        return decision;
    }
    
    @Override
    public Map<V, V> predecessors() {
        return (decision() ? p : null);
    }
    
    @Override
    public Map<V, Number> distances() {
        return (decision() ? h : null);
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
    public boolean isPath(V target) {
        if (decision() && graph.containsVertex(target)) {
            return source.equals(target) || !p.get(target).equals(target);
        } else {
            return false;
        }
    }
    
    @Override
    public Number getDistance(V target) {
        return (decision() ? h.get(target) : null);
    }
    
    @Override
    public V getPredecessor(V target) {
        return (decision() ? p.get(target) : null);
    }
    
    public String getPaths() {
        if (decision()) {
            String separator = System.getProperty("line.separator");
            StringBuilder result = new StringBuilder();
            for (V u : graph) {
                result.append("p(").append(getSource())
                        .append(",").append(u).append(") = ");
                if (isPath(u)) {
                    result.append(getPath(u));
                } else {
                    result.append("There isn't path between ").append(getSource())
                            .append(" and ").append(u);
                }
                result.append(separator);
            }
            return result.toString();
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        if (decision()) {
            String separator = System.getProperty("line.separator");
            StringBuilder result = new StringBuilder();
            for (V u : graph) {
                result.append("w[p(").append(getSource())
                        .append(",").append(u).append(")] = ");
                if (isPath(u)) {
                    result.append(getDistance(u));
                } else {
                    result.append("Infinity");
                }
                result.append(separator);
            }
            return result.toString();
        } else {
            return null;
        }
    }
    
} //End of class ShortestPaths<V,E>
