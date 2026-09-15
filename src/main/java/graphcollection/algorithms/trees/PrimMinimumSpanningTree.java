/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.trees;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.GraphPaths;
import graphcollection.algorithms.GraphPathBuilder;
import graphcollection.algorithms.shortestpaths.VertexDistance;
import graphcollection.graph.Graph;
import graphcollection.util.MutableQueue;
import graphcollection.graph.WeightedEdge;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class PrimMinimumSpanningTree<V, E extends WeightedEdge<V, ? extends Number>>
        implements GraphAlgorithm, MinimumSpanningTree<E>, GraphPaths<V> {

    private Graph<V, E> graph;
    private V source;
    private HashMap<V, V> p = new HashMap<V, V>();
    private HashMap<V, Number> h = new HashMap<V, Number>();
    private HashMap<V, Integer> finished = new HashMap<V, Integer>();
    private double mstWeight;
    public static final double max_distance = Double.MAX_VALUE;

    public PrimMinimumSpanningTree(Graph<V, E> g, V s) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (g.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        if (!g.containsVertex(s)) {
            throw new IllegalArgumentException("graph does not contains vertex " + s);
        }
        graph = g;
        source = s;
        createMst();
    }

    private void initialize() {
        for (V u : graph) {
            h.put(u, max_distance);
            p.put(u, u);
        }
        h.put(source, 0.0);
    }

    private void createMst() {
        initialize();
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
                if (finished.get(v) != -1 && w.doubleValue() < h.get(v).doubleValue()) {
                    h.put(v, w.doubleValue());
                    p.put(v, u);
                    Q.update(finished.get(v),
                            new VertexDistance<V, Number>(v, h.get(v).doubleValue()));
                }
            }
            finished.put(u, -1);
            mstWeight += h.get(u).doubleValue();
        }
        //----------------------------------------------       
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
    public Collection<E> getMinimumSpanningTreeEdges() {
        ArrayList<E> tree = new ArrayList<E>(graph.verticesNum() - 1);
        for (V u : p.keySet()) {
            V v = p.get(u);
            if (!v.equals(u)) {
                tree.add(graph.edge(u, v));
            }
        }
        return tree;
    }

    @Override
    public Number getMinimumSpanningTreeWeight() {
        return mstWeight;
    }

    @Override
    public V getSource() {
        return source;
    }

    @Override
    public V getPredecessor(V target) {
        return p.get(target);
    }

    @Override
    public Number getDistance(V target) {
        return h.get(target);
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
    public String toString() {
        return "w(T) = " + getMinimumSpanningTreeWeight();
    }

} //End of class PrimMinimumSpanningTree<V, E>
