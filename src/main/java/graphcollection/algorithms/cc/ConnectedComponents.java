/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.cc;

import graphcollection.algorithms.VertexColor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

/**
 *
 * @author Рома
 * @param <V>
 */
public class ConnectedComponents<V>
        extends AbstractConnectedComponents<V> {

    public ConnectedComponents(Graph<V, ? extends Edge<V>> graph) {
        super(graph);
        if (graph.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        connectedComponentsSearch(graph);
    }

    @Override
    protected final void connectedComponentsSearch(Graph<V, ? extends Edge<V>> graph) {
        for (V u : graph) {
            if (color.get(u).equals(VertexColor.WHITE)) {
                count++;
                dfsVisit(graph, u);
            }
        }
    }

} //End of class ConnectedComponents<V>
