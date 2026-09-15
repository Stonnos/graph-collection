/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.dfs;

import graphcollection.graph.Edge;
import graphcollection.graph.Graph;
import java.util.Collection;

/**
 *
 * @author Рома
 * @param <V>
 */
public class TopologicalSort<V> extends DFSVisitor<V> {

    public TopologicalSort(Graph<V, ? extends Edge<V>> g) {
        super(g);
    }

    @Override
    public Collection<V> sequence() {
        return acyclic() ? super.sequence() : null;
    }

    @Override
    public String toString() {
        return acyclic() ? super.sequence().toString() : null;
    }

} //End of class TopologicalSort<V>
