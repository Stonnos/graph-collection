/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.Iterator;

public interface Graph<V, E extends Edge<V>>
        extends Iterable<V>, Copyable<Graph<V, E>> {

    int verticesNum();

    int edgesNum();

    boolean addVertex(V v);

    boolean removeVertex(V v);

    boolean addEdge(E e);

    int removeEdge(E e);

    E edge(V v1, V v2);

    boolean containsEdge(E e);

    boolean containsVertex(V v);

    boolean removeOutEdges(V v);

    boolean removeInEdges(V v);

    int outEdgesNum(V v);

    int inEdgesNum(V v);

    int adjacentVerticesNum(V v);

    void clearEdges();

    void clear();

    boolean direction();

    boolean isEmpty();

    boolean getUseLoops();

    Iterator<E> edgeIterator();

    Iterator<E> outEdgeIterator(V v);

    Iterator<E> inEdgeIterator(V v);

    Iterator<V> adjacencyIterator(V v);
}
