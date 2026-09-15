/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import graphcollection.util.HashMultiSet;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class HashSetMultiGraph<V, E extends Edge<V>>
        extends AbstractGraph<V, E> implements MultiGraph<V, E> {

    public HashSetMultiGraph() {
        super(false, true);
    }

    public HashSetMultiGraph(boolean direction) {
        super(direction, true);
    }

    @Override
    public boolean addVertex(V v) {
        return addEdgeList(v, new HashMultiSet<E>());
    }

    @Override
    public Collection<E> edges(V v1, V v2) {
        if (edgeList.containsKey(v1) && edgeList.containsKey(v2)) {
            return ((HashMultiSet<E>) edgeList.get(v1)).
                    elements((E) new Edge<>(direction(), v1, v2));
        } else {
            return null;
        }
    }

    @Override
    public Iterator<V> adjacencyIterator(V v) {
        if (edgeList.containsKey(v)) {
            return new AdjacencyIterator(new OutEdgeIterator(v,
                    ((HashMultiSet<E>) edgeList.get(v)).keysIterator()));
        } else {
            return null;
        }
    }

    @Override
    public int adjacentVerticesNum(V v) {
        if (edgeList.containsKey(v)) {
            return ((HashMultiSet<E>) edgeList.get(v)).keysSize();
        } else {
            return -1;
        }
    }

} //End of class HashSetMultiGraph<V, E>
