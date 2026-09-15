/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.cc;

import graphcollection.algorithms.GraphReverse;
import graphcollection.algorithms.VertexColor;
import graphcollection.algorithms.dfs.DFSVisitor;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

/**
 *
 * @param <V>
 * @author Рома
 */
public class StronglyConnectedComponents<V>
        extends AbstractConnectedComponents<V> {

    public StronglyConnectedComponents(Graph<V, ? extends Edge<V>> graph) {
        super(graph);
        if (!graph.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
        connectedComponentsSearch(graph);
    }

    @Override
    protected final void connectedComponentsSearch(Graph<V, ? extends Edge<V>> graph) {
        DFSVisitor<V> dfs = new DFSVisitor<V>(graph);
        Graph<V, ?> reverseGraph = GraphReverse.reverse(graph);
        //---------------------------------------------
        for (V u : dfs.sequence()) {
            if (color.get(u).equals(VertexColor.WHITE)) {
                count++;
                dfsVisit(reverseGraph, u);
            }
        }
    }
} //End of class StronglyConnectedComponents<V>
