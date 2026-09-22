package graphcollection.algorithms.dfs;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.graph.Edge;
import graphcollection.graph.Graph;

import java.util.List;
import java.util.LinkedList;

/**
 *
 * @param <V>
 * @author Рома
 */
public abstract class AbstractTourSearch<V, E extends Edge<V>>
        implements GraphAlgorithm {

    protected LinkedList<V> tour = new LinkedList<V>();
    protected boolean decision = false;

    protected AbstractTourSearch(Graph<V, E> g) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (g.direction()) {
            throw new IllegalArgumentException("illegal type of graph");
        }
    }

    protected abstract boolean search(Graph<V, E> g);

    public boolean decision() {
        return decision;
    }

    public List<V> tour() {
        return decision() ? tour : null;
    }

    @Override
    public String toString() {
        return decision() ? tour.toString() : null;
    }

}
