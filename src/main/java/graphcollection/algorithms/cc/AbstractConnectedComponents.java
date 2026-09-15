/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.cc;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Рома
 * @param <V>
 */
public abstract class AbstractConnectedComponents<V>
        implements GraphAlgorithm {

    protected HashMap<V, Integer> components = new HashMap<V, Integer>();
    protected HashMap<V, Integer> color = new HashMap<V, Integer>();
    protected int count;

    protected AbstractConnectedComponents(Graph<V, ? extends Edge<V>> graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        initialize(graph);
    }

    protected abstract void connectedComponentsSearch(Graph<V, ? extends Edge<V>> graph);

    private void initialize(Graph<V, ? extends Edge<V>> graph) {
        for (V u : graph) {
            components.put(u, count);
            color.put(u, VertexColor.WHITE);
        }
    }

    protected void dfsVisit(Graph<V, ? extends Edge<V>> g, V u) {
        color.put(u, VertexColor.GRAY);
        Iterator<V> adjV = g.adjacencyIterator(u);
        //------------------------------------------
        while (adjV.hasNext()) {
            V v = adjV.next();
            if (color.get(v).equals(VertexColor.WHITE)) {
                dfsVisit(g, v);
            }
        }
        //-----------------------------------------
        components.put(u, count);
        color.put(u, VertexColor.BLACK);
    }

    public Map<V, Integer> components() {
        return components;
    }

    public boolean connected() {
        return count == 1;
    }

    public int componentsNum() {
        return count;
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= componentsNum(); i++) {
            result.append("C").append(String.valueOf(i)).append(" = {");
            boolean flag = false;
            for (Map.Entry<V, Integer> entry : components().entrySet()) {
                if (entry.getValue() == i) {
                    if (flag) {
                        result.append(",");
                    }
                    result.append(entry.getKey());
                    flag = true;
                }
            }
            result.append("}").append(separator);
        }
        return result.toString();
    }
}
