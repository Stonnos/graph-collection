/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class BridgesSearch<V, E extends Edge<V>> implements GraphAlgorithm {

    private HashMap<V, Integer> d = new HashMap<V, Integer>();
    private HashMap<V, Integer> low = new HashMap<V, Integer>();
    private HashMap<V, Boolean> color = new HashMap<V, Boolean>();
    private int time, count;
    private Collection<E> bridges = new LinkedList<E>();

    public BridgesSearch(Graph<V, E> graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        if (graph.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        //---------------------------------------
        initialize(graph);
        for (V u : graph) {
            if (!color.get(u)) {
                count++;
                if (count > 1) {
                    break;
                }
                visit(graph, u, u);
            }
        }
    }

    private void initialize(Graph<V, E> graph) {
        for (V u : graph) {
            color.put(u, false);
            d.put(u, 0);
            low.put(u, 0);
        }
    }

    private void visit(Graph<V, E> graph, V u, V p) {
        color.put(u, true);
        time++;
        d.put(u, time);
        low.put(u, time);
        Iterator<E> edges = graph.outEdgeIterator(u);
        //-------------------------
        while (edges.hasNext()) {
            E e = edges.next();
            V v = e.target();
            if (!color.get(v)) {
                visit(graph, v, u);
                low.put(u, Math.min(low.get(u), low.get(v)));
                if (low.get(v).equals(d.get(v))) {
                    bridges.add(e);
                }
            } else if (!p.equals(v)) {
                low.put(u, Math.min(low.get(u), d.get(v)));
            }
        }
    }

    public boolean connected() {
        return count == 1;
    }

    public boolean edgeConnected() {
        return connected() && bridges.isEmpty();
    }

    public Collection<E> bridges() {
        return connected() ? bridges : null;
    }

    @Override
    public String toString() {
        return bridges.toString();
    }

} //End of class BridgesSearch<V,E>
