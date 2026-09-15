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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @param <V>
 * @author Рома
 */
public class AllPathsSearch<V> implements GraphAlgorithm {

    private V s, t;
    private LinkedList<LinkedList<V>> paths = new LinkedList<LinkedList<V>>();
    private HashMap<V, Integer> color = new HashMap<V, Integer>();

    public AllPathsSearch(Graph<V, ? extends Edge<V>> g, V s, V t) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (!g.containsVertex(s) || !g.containsVertex(t)) {
            throw new IllegalArgumentException("graph does not contains vertex "
                    + s + " or " + t);
        }
        this.s = s;
        this.t = t;
        search(g, s, t);
    }

    private void search(Graph<V, ? extends Edge<V>> graph, V s, V t) {
        for (V u : graph) {
            color.put(u, VertexColor.WHITE);
        }
        LinkedList<V> list = new LinkedList<V>();
        list.add(s);
        color.put(s, VertexColor.GRAY);
        //--------------------------------
        Iterator<V> adjV = graph.adjacencyIterator(s);
        while (adjV.hasNext()) {
            visit(graph, adjV.next(), t, list);
        }
    }

    private void visit(Graph<V, ? extends Edge<V>> graph, V s, V t, LinkedList<V> list) {
        if (s.equals(t)) {
            list.addLast(s);
            paths.add(new LinkedList<V>(list));
            list.removeLast();
            return;
        }
        color.put(s, VertexColor.GRAY);
        list.addLast(s);
        //-------------------------------------
        Iterator<V> adjV = graph.adjacencyIterator(s);
        while (adjV.hasNext()) {
            V v = adjV.next();
            if (color.get(v).equals(VertexColor.WHITE)) {
                visit(graph, v, t, list);
            }
        }
        //----------------------------------------
        list.removeLast();
        color.put(s, VertexColor.WHITE);
    }

    public int size() {
        return paths.size();
    }

    public LinkedList<LinkedList<V>> paths() {
        return paths;
    }

    public V getSource() {
        return s;
    }

    public V getTarget() {
        return t;
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        String p = "(" + s + "," + t + ") = ";
        int i = 1;
        for (LinkedList<V> path : paths()) {
            result.append("p").append(String.valueOf(i))
                    .append(p).append(path).append(separator);
            i++;
        }
        return result.toString();
    }

} //End of class AllPathsSearch<V>
