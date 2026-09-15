/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.trees;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import graphcollection.util.DisjointSets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class KruskalMinimumSpanningTree<V, E extends WeightedEdge<V, ? extends Number>>
        implements GraphAlgorithm, MinimumSpanningTree<E> {

    private ArrayList<E> tree;
    private double mstWeight;

    public KruskalMinimumSpanningTree(Graph<V, E> graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        if (graph.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        tree = new ArrayList(graph.verticesNum() - 1);
        createMst(graph);
    }

    private void createMst(Graph<V, E> graph) {
        DisjointSets<V> sets = new DisjointSets<V>();
        for (V u : graph) {
            sets.makeSet(u);
        }
        //---------------------------------------------
        PriorityQueue<E> Q = new PriorityQueue<E>(new Comparator<E>() {
            @Override
            public int compare(E x, E y) {
                return Double.valueOf(x.getWeight().doubleValue())
                        .compareTo(y.getWeight().doubleValue());
            }
        });
        //----------------------------------------------
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            Q.add(edge.next());
        }
        while (!Q.isEmpty()) {
            E e = Q.remove();
            if (!sets.findSet(e.source()).equals(sets.findSet(e.target()))) {
                tree.add(e);
                mstWeight += e.getWeight().doubleValue();
                sets.union(e.source(), e.target());
            }
        }
    }

    @Override
    public Collection<E> getMinimumSpanningTreeEdges() {
        return tree;
    }

    @Override
    public Number getMinimumSpanningTreeWeight() {
        return mstWeight;
    }

    @Override
    public String toString() {
        return "w(T) = " + getMinimumSpanningTreeWeight();
    }

} //End of class KruskalMinimumSpanningTree<V,E>
