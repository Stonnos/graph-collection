/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import graphcollection.util.AdjacencyMatrix;
import graphcollection.util.DirectedAdjacencyMatrix;
import graphcollection.util.UnDirectedAdjacencyMatrix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public class AdjacencyMatrixGraph<V, E extends Edge<V>>
        implements Graph<V, E>, Cloneable, java.io.Serializable {

    private HashMap<V, Integer> vertexMap = new HashMap<V, Integer>();
    private AdjacencyMatrix<E> matrix;
    private boolean direction = false;

    public AdjacencyMatrixGraph() {
        matrix = new UnDirectedAdjacencyMatrix<E>();
    }

    public AdjacencyMatrixGraph(boolean direction) {
        createMatrix(direction, 0);
        this.direction = direction;
    }

    public AdjacencyMatrixGraph(boolean direction, V[] vertices) {
        for (int i = 0; i < vertices.length; i++) {
            if (!containsVertex(vertices[i])) {
                vertexMap.put(vertices[i], i);
            }
        }
        createMatrix(direction, vertices.length);
        this.direction = direction;
    }

    private void createMatrix(boolean type, int size) {
        matrix = type ? new DirectedAdjacencyMatrix<>(size)
                : new UnDirectedAdjacencyMatrix<>(size);
    }

    @Override
    public boolean direction() {
        return direction;
    }

    @Override
    public boolean getUseLoops() {
        return false;
    }

    @Override
    public int verticesNum() {
        return vertexMap.size();
    }

    @Override
    public int edgesNum() {
        return matrix.size();
    }

    @Override
    public boolean isEmpty() {
        return vertexMap.isEmpty();
    }

    @Override
    public boolean addEdge(E e) {
        if (!checkEdge(e)) {
            return false;
        }
        Integer i = vertexMap.get(e.source());
        Integer j = vertexMap.get(e.target());
        if (checkVertexIndex(i) && checkVertexIndex(j)) {
            return matrix.add(i, j, e);
        } else {
            return false;
        }
    }

    @Override
    public int removeEdge(E e) {
        if (!checkEdge(e)) {
            return 0;
        }
        Integer i = vertexMap.get(e.source());
        Integer j = vertexMap.get(e.target());
        if (checkVertexIndex(i) && checkVertexIndex(j)) {
            return matrix.remove(i, j) ? 1 : 0;
        } else {
            return 0;
        }
    }

    @Override
    public final boolean containsVertex(V v) {
        return vertexMap.containsKey(v);
    }

    @Override
    public final boolean containsEdge(E e) {
        if (!checkEdge(e)) {
            return false;
        }
        Integer i = vertexMap.get(e.source());
        Integer j = vertexMap.get(e.target());
        if (checkVertexIndex(i) && checkVertexIndex(j)) {
            return matrix.get(i, j) != null;
        } else {
            return false;
        }
    }

    @Override
    public E edge(V u, V v) {
        Integer i = vertexMap.get(u);
        Integer j = vertexMap.get(v);
        if (checkVertexIndex(i) && checkVertexIndex(j)) {
            return matrix.get(i, j);
        } else {
            return null;
        }
    }

    @Override
    public void clearEdges() {
        matrix.clear();
    }

    @Override
    public void clear() {
        vertexMap.clear();
        matrix.clearAll();
    }

    @Override
    public boolean addVertex(V v) {
        if (!vertexMap.containsKey(v)) {
            vertexMap.put(v, verticesNum());
            matrix.add();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeVertex(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            vertexMap.remove(v);
            matrix.remove(i);
            for (Map.Entry<V, Integer> entry : vertexMap.entrySet()) {
                if (entry.getValue() > i) {
                    entry.setValue(entry.getValue() - 1);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int outEdgesNum(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            return matrix.rowCount(i);
        } else {
            return -1;
        }
    }

    @Override
    public int inEdgesNum(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            return matrix.columnCount(i);
        } else {
            return -1;
        }
    }

    @Override
    public int adjacentVerticesNum(V v) {
        return outEdgesNum(v);
    }

    @Override
    public boolean removeOutEdges(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            Iterator<E> iter = matrix.rowIterator(i);
            while (iter.hasNext()) {
                iter.next();
                iter.remove();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeInEdges(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            Iterator<E> iter = matrix.columnIterator(i);
            while (iter.hasNext()) {
                iter.next();
                iter.remove();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator<V> iterator() {
        return vertexMap.keySet().iterator();
    }

    @Override
    public Iterator<E> edgeIterator() {
        return matrix.iterator();
    }

    @Override
    public String toString() {
        return matrix.toString();
    }

    @Override
    public Iterator<E> outEdgeIterator(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            return new OutEdgeIterator(matrix.rowIterator(i));
        } else {
            return null;
        }
    }

    @Override
    public Iterator<E> inEdgeIterator(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            return new InEdgeIterator(matrix.columnIterator(i));
        } else {
            return null;
        }
    }

    @Override
    public Iterator<V> adjacencyIterator(V v) {
        Integer i = vertexMap.get(v);
        if (checkVertexIndex(i)) {
            return new AdjacencyIterator(v, new OutEdgeIterator(matrix.rowIterator(i)));
        } else {
            return null;
        }
    }

    protected final boolean checkEdge(E e) {
        return e != null && e.direction() == direction();
    }

    protected final boolean checkVertexIndex(Integer v) {
        return v != null;
    }

    /**
     *
     */
    private class OutEdgeIterator implements Iterator<E> {

        private final Iterator<E> adj;

        public OutEdgeIterator(Iterator<E> adj) {
            this.adj = adj;
        }

        @Override
        public boolean hasNext() {
            return adj.hasNext();
        }

        @Override
        public E next() {
            return adj.next();
        }
    }  //End of class OutEdgeIterator

    private class InEdgeIterator implements Iterator<E> {

        private final Iterator<E> edges;

        public InEdgeIterator(Iterator<E> edges) {
            this.edges = edges;
        }

        @Override
        public boolean hasNext() {
            return edges.hasNext();
        }

        @Override
        public E next() {
            return edges.next();
        }
    }  //End of class InEdgeIterator

    private class AdjacencyIterator implements Iterator<V> {

        private final V v;
        private final Iterator<E> adj;

        public AdjacencyIterator(V v, Iterator<E> adj) {
            this.v = v;
            this.adj = adj;
        }

        @Override
        public boolean hasNext() {
            return adj.hasNext();
        }

        @Override
        public V next() {
            E nextEdge = adj.next();
            return nextEdge.getOpposite(v);
        }
    } //End of class AdjacencyIterator

    @Override
    public Graph<V, E> copy() {
        return (Graph<V, E>) clone();
    }

    @Override
    public Object clone() {
        AdjacencyMatrixGraph<V, E> g;
        try {
            g = (AdjacencyMatrixGraph<V, E>) super.clone();
        } catch (CloneNotSupportedException e) {
             throw new IllegalStateException(e);
        }
        //----------------------------------------------
        g.vertexMap = (HashMap<V, Integer>) vertexMap.clone();
        g.direction = direction;
        if (g.direction) {
            g.matrix = new DirectedAdjacencyMatrix<E>(verticesNum());
        } else {
            g.matrix = new UnDirectedAdjacencyMatrix<E>(verticesNum());
        }
        //-----------------------------------------------
        Iterator<E> e = edgeIterator();
        while (e.hasNext()) {
            g.addEdge((E) e.next().clone());
        }
        //-----------------------------------------------
        return g;
    }
}
