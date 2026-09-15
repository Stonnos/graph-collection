/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.Iterator;

public interface Graph<V, E extends Edge<V>>
        extends Iterable<V>, Copyable<Graph<V, E>> {

    public int verticesNum();

    public int edgesNum();

    public boolean addVertex(V v);

    public boolean removeVertex(V v);

    public boolean addEdge(E e);

    public int removeEdge(E e);

    public E edge(V v1, V v2);

    public boolean containsEdge(E e);

    public boolean containsVertex(V v);

    public boolean removeOutEdges(V v);

    public boolean removeInEdges(V v);

    public int outEdgesNum(V v);

    public int inEdgesNum(V v);

    public int adjacentVerticesNum(V v);

    public void clearEdges();

    public void clear();

    public boolean direction();

    public boolean isEmpty();

    public boolean getUseLoops();

    public Iterator<E> edgeIterator();

    public Iterator<E> outEdgeIterator(V v);

    public Iterator<E> inEdgeIterator(V v);

    public Iterator<V> adjacencyIterator(V v);
}
