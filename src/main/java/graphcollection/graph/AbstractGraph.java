/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public abstract class AbstractGraph<V, E extends Edge<V>>
        implements Graph<V, E>, Cloneable, java.io.Serializable {

    protected HashMap<V, AbstractSet<E>> edgeList = new HashMap<>();
    protected boolean direction;
    protected int edgeSize;
    protected int modCount;
    private boolean use_loop;

    protected AbstractGraph() {
    }

    protected AbstractGraph(boolean direction) {
        this.direction = direction;
    }

    protected AbstractGraph(boolean direction, boolean loop) {
        this.direction = direction;
        this.use_loop = loop;
    }

    @Override
    public boolean direction() {
        return direction;
    }

    @Override
    public boolean isEmpty() {
        return edgeList.isEmpty();
    }

    @Override
    public boolean getUseLoops() {
        return use_loop;
    }

    @Override
    public int verticesNum() {
        return edgeList.size();
    }

    @Override
    public int edgesNum() {
        return edgeSize;
    }

    @Override
    public boolean addEdge(E e) {
        if (checkEdge(e)) {
            return false;
        } else if (edgeList.containsKey(e.source()) && edgeList.containsKey(e.target())) {
            if (edgeList.get(e.source()).add(e)) {
                if (!direction() && !isLoop(e)) {
                    edgeList.get(e.target()).add(e);
                }
                edgeSize++;
                modCount++;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected final boolean isLoop(E e) {
        return e.source().equals(e.target());
    }

    @Override
    public int removeEdge(E e) {
        if (checkEdge(e)) {
            return 0;
        } else if (edgeList.containsKey(e.source()) && edgeList.containsKey(e.target())) {
            AbstractSet<E> edges = edgeList.get(e.source());
            int n = edges.size();
            if (edges.remove(e)) {
                if (!direction()) {
                    edgeList.get(e.target()).remove(e);
                }
                int count = n - edges.size();
                edgeSize -= count;
                modCount++;
                return count;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public E edge(V v1, V v2) {
        if (edgeList.containsKey(v1) && edgeList.containsKey(v2)) {
            Iterator<E> outEdges = outEdgeIterator(v1);
            while (outEdges.hasNext()) {
                E e = outEdges.next();
                if (e.target().equals(v2)) {
                    return e;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public boolean containsEdge(E e) {
        if (checkEdge(e)) {
            return false;
        } else if (edgeList.containsKey(e.source()) && edgeList.containsKey(e.target())) {
            return edgeList.get(e.source()).contains(e);
        } else {
            return false;
        }
    }

    @Override
    public boolean containsVertex(V v) {
        return edgeList.containsKey(v);
    }

    @Override
    public boolean removeOutEdges(V v) {
        if (!direction()) {
            return removeInEdges(v);
        }
        if (edgeList.containsKey(v)) {
            AbstractSet<E> outEdges = edgeList.get(v);
            edgeSize -= outEdges.size();
            modCount++;
            outEdges.clear();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeInEdges(V v) {
        if (edgeList.containsKey(v)) {
            for (V u : edgeList.keySet()) {
                removeEdge((E) new Edge<V>(direction(), u, v));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeVertex(V v) {
        if (edgeList.containsKey(v)) {
            removeOutEdges(v);
            if (direction()) {
                removeInEdges(v);
            }
            edgeList.remove(v);
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int outEdgesNum(V v) {
        return (edgeList.containsKey(v) ? edgeList.get(v).size() : -1);
    }

    @Override
    public int inEdgesNum(V v) {
        if (edgeList.containsKey(v)) {
            if (!direction()) {
                return outEdgesNum(v);
            } else {
                Iterator<E> inEdge = this.inEdgeIterator(v);
                int count = 0;
                while (inEdge.hasNext()) {
                    inEdge.next();
                    count++;
                }
                return count;
            }
        } else {
            return -1;
        }
    }

    @Override
    public int adjacentVerticesNum(V v) {
        return outEdgesNum(v);
    }

    @Override
    public void clearEdges() {
        for (AbstractSet<E> edges : edgeList.values()) {
            edges.clear();
        }
        modCount++;
        edgeSize = 0;
    }

    @Override
    public void clear() {
        clearEdges();
        edgeList.clear();
        modCount++;
    }

    @Override
    public Graph<V, E> copy() {
        return (Graph<V, E>) clone();
    }

    @Override
    public Object clone() {
        AbstractGraph<V, E> g;
        try {
            g = (AbstractGraph<V, E>) super.clone();
        } catch (CloneNotSupportedException e) {
             throw new IllegalStateException(e);
        }
        //----------------------------------------------
        g.edgeList = new HashMap<V, AbstractSet<E>>();
        g.direction = direction;
        g.edgeSize = 0;
        g.modCount = 0;
        for (V v : this) {
            g.addVertex(v);
        }
        Iterator<E> e = edgeIterator();
        while (e.hasNext()) {
            g.addEdge((E) e.next().clone());
        }
        //-----------------------------------------------
        return g;
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder g = new StringBuilder();
        for (V v : this) {
            g.append(v).append(": ");
            Iterator<E> e = outEdgeIterator(v);
            while (e.hasNext()) {
                g.append(e.next()).append(" ");
            }
            g.append(separator);
        }
        return g.toString();
    }

    protected final boolean checkDirection(E e) {
        return (e != null && e.direction() == direction());
    }

    protected final boolean checkEdge(E e) {
        return !checkDirection(e) || (!use_loop && isLoop(e));
    }

    protected final boolean isInEdge(E e, V v) {
        return (e.target().equals(v) || (!direction() && e.source().equals(v)));
    }

    protected final boolean addEdgeList(V v, AbstractSet<E> edgeSet) {
        if (!edgeList.containsKey(v)) {
            edgeList.put(v, edgeSet);
            return true;
        } else {
            return false;
        }
    }

    /**
     * OutEdgeIterator
     */
    protected class OutEdgeIterator implements Iterator<E> {

        private final V v;
        private final Iterator<E> outEdges;

        public OutEdgeIterator(V v, Iterator<E> outEdges) {
            this.v = v;
            this.outEdges = outEdges;
        }

        @Override
        public boolean hasNext() {
            return outEdges.hasNext();
        }

        @Override
        public E next() {
            E e = outEdges.next();
            if (!e.source().equals(v)) {
                e.exchange();
            }
            return e;
        }

    } //End of class OutEdgeIterator

    /**
     * EdgeIterator
     */
    private class EdgeIterator implements Iterator<E> {

        private final Iterator<HashMap.Entry<V, AbstractSet<E>>> vSet;
        private Iterator<E> edges;
        private V v;
        private int current;
        private final int expectedCount = modCount;

        public EdgeIterator(Iterator<HashMap.Entry<V, AbstractSet<E>>> vSet) {
            this.vSet = vSet;
        }

        private void checkForComodification() {
            if (modCount != expectedCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return current < edgeSize;
        }

        @Override
        public E next() {
            checkForComodification();
            if (hasNext()) {
                while (v != null || vSet.hasNext()) {
                    if (v == null || !edges.hasNext()) {
                        HashMap.Entry<V, AbstractSet<E>> pair = vSet.next();
                        v = pair.getKey();
                        edges = pair.getValue().iterator();
                    }
                    //--------------------------------
                    while (edges.hasNext()) {
                        E e = edges.next();
                        if (e.source().equals(v)) {
                            current++;
                            return e;
                        }
                    }
                    //--------------------------------
                }
            }
            throw new NoSuchElementException();
        }

    } //End of class EdgeIterator

    /**
     * AdjacencyIterator
     */
    protected class AdjacencyIterator implements Iterator<V> {

        protected final Iterator<E> adjV;

        public AdjacencyIterator(Iterator<E> adjV) {
            this.adjV = adjV;
        }

        @Override
        public boolean hasNext() {
            return adjV.hasNext();
        }

        @Override
        public V next() {
            return adjV.next().target();
        }

    } //End of class AdjacencyIterator

    /**
     *
     */
    private class InEdgeIterator implements Iterator<E> {

        private final Iterator<E> edge;
        private final V v;
        private E currentEdge;
        private final int expectedCount = modCount;

        public InEdgeIterator(Iterator<E> edge, V v) {
            this.edge = edge;
            this.v = v;
        }

        @Override
        public boolean hasNext() {
            if (currentEdge == null) {
                while (edge.hasNext()) {
                    currentEdge = edge.next();
                    if (isInEdge(currentEdge, v)) {
                        return true;
                    }
                }
                currentEdge = null;
                return false;
            }
            return true;
        }

        @Override
        public E next() {
            if (modCount != expectedCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (!currentEdge.target().equals(v)) {
                currentEdge.exchange();
            }
            E e = currentEdge;
            currentEdge = null;
            return e;
        }
    }  //End of class InEdgeIterator

    @Override
    public Iterator<V> iterator() {
        return edgeList.keySet().iterator();
    }

    @Override
    public Iterator<E> edgeIterator() {
        return new EdgeIterator(edgeList.entrySet().iterator());
    }

    @Override
    public Iterator<E> outEdgeIterator(V v) {
        if (edgeList.containsKey(v)) {
            return new OutEdgeIterator(v, edgeList.get(v).iterator());
        } else {
            return null;
        }
    }

    @Override
    public Iterator<V> adjacencyIterator(V v) {
        if (edgeList.containsKey(v)) {
            return new AdjacencyIterator(new OutEdgeIterator(v, edgeList.get(v).iterator()));
        } else {
            return null;
        }
    }

    @Override
    public Iterator<E> inEdgeIterator(V v) {
        if (edgeList.containsKey(v)) {
            if (direction()) {
                return new InEdgeIterator(edgeIterator(), v);
            } else {
                return new InEdgeIterator(outEdgeIterator(v), v);
            }
        } else {
            return null;
        }
    }

} //End of class AbstractGraph<V, E>
