/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.algorithms.AllPairsGraphPaths;
import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.GraphPathBuilder;
import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import graphcollection.util.MatrixMap;

import java.util.Collection;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public abstract class AllPairsShortestPaths<V, E extends WeightedEdge<V, ? extends Number>>
        implements GraphAlgorithm, AllPairsGraphPaths<V> {

    protected Graph<V, E> graph;
    protected MatrixMap<V, V, V> p = new MatrixMap<V, V, V>();
    protected MatrixMap<V, V, Number> h = new MatrixMap<V, V, Number>();
    protected boolean decision = true;
    public static final double max_distance = Double.MAX_VALUE;

    protected AllPairsShortestPaths(Graph<V, E> g) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.direction()) {
            throw new IllegalArgumentException("Illegal type of graph");
        }
        graph = g;
        initialize();
    }

    public boolean decision() {
        return decision;
    }

    @Override
    public MatrixMap<V, V, V> predecessorsMatrix() {
        return (decision() ? p : null);
    }

    @Override
    public MatrixMap<V, V, Number> distancesMatrix() {
        return (decision() ? h : null);
    }

    private void initialize() {
        for (V i : graph) {
            for (V j : graph) {
                p.put(i, j, j);
                if (i.equals(j)) {
                    h.put(i, j, 0.0);
                } else {
                    h.put(i, j, max_distance);
                }
            }
        }
    }

    @Override
    public Number getDistance(V u, V v) {
        return (decision() ? h.get(u, v) : null);
    }

    @Override
    public V getPredecessor(V u, V v) {
        return (decision() ? p.get(u, v) : null);
    }

    @Override
    public boolean isPath(V u, V v) {
        if (decision() && checkVertices(u, v)) {
            return u.equals(v) || !p.get(u, v).equals(v);
        } else {
            return false;
        }
    }

    @Override
    public Collection<V> getPath(V u, V v) {
        if (isPath(u, v)) {
            return GraphPathBuilder.path(p, u, v);
        } else {
            return null;
        }
    }

    private boolean checkVertices(V u, V v) {
        return graph.containsVertex(u) && graph.containsVertex(v);
    }

    protected abstract void searchAllShortestPaths();

    public String getPaths() {
        if (decision()) {
            String separator = System.getProperty("line.separator");
            StringBuilder result = new StringBuilder();
            for (V u : graph) {
                for (V v : graph) {
                    result.append("p(").append(u).append(",")
                            .append(v).append(") = ");
                    if (isPath(u, v)) {
                        result.append(getPath(u, v));
                    } else {
                        result.append("There isn't path between ").append(u)
                                .append(" and ").append(v);
                    }
                    result.append(separator);
                }
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
                for (V v : graph) {
                    result.append("w[p(").append(u).append(",")
                            .append(v).append(")] = ");
                    if (isPath(u, v)) {
                        result.append(getDistance(u, v));
                    } else {
                        result.append("Infinity");
                    }
                    result.append(separator);
                }
            }
            return result.toString();
        } else {
            return null;
        }
    }

} //End of class AllPairsShortestPaths<V,E>
