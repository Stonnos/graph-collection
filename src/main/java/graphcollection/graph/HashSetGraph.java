/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.HashSet;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class HashSetGraph<V, E extends Edge<V>> extends AbstractGraph<V, E> {

    public HashSetGraph() {
    }

    public HashSetGraph(boolean direction) {
        super(direction);
    }

    public HashSetGraph(boolean direction, boolean loop) {
        super(direction, loop);
    }

    @Override
    public boolean addVertex(V v) {
        return addEdgeList(v, new HashSet<E>());
    }

} //End of class HashSetGraph<V, E>
