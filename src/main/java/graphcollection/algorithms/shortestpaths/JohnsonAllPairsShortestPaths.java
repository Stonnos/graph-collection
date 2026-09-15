/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Рома
 * @param <V>
 * @param <E>
 */
public class JohnsonAllPairsShortestPaths<V, E extends WeightedEdge<V, Number>>
        extends AllPairsShortestPaths<V, E> {

    private final V s;

    public JohnsonAllPairsShortestPaths(Graph<V, E> g, V s) {
        super(g);
        if (g.containsVertex(s)) {
            throw new IllegalArgumentException("Graph contains vertex " + s);
        }
        this.s = s;
        searchAllShortestPaths();
    }

    @Override
    protected final void searchAllShortestPaths() {
        graph.addVertex(s);
        for (V u : graph) {
            graph.addEdge((E) new WeightedEdge<V, Number>(true, s, u, 0.0));
        }
        //-------------------------------------------------------------------
        BellmanFordShortestPaths<V, E> bellman
                = new BellmanFordShortestPaths<V, E>(graph, s);
        if (!bellman.decision()) {
            graph.removeVertex(s);
            decision = false;
            return;
        }
        //-------------------------------------------------------------------
        Map<V, Number> d = bellman.distances();
        Iterator<E> edge = graph.edgeIterator();
        while (edge.hasNext()) {
            E e = edge.next();
            e.setWeight(e.getWeight().doubleValue()
                    + d.get(e.source()).doubleValue() - d.get(e.target()).doubleValue());
        }
        graph.removeVertex(s);
        //-------------------------------------------------------------------
        for (V u : graph) {
            DijkstraShortestPaths<V, E> dijkstra
                    = new DijkstraShortestPaths<V, E>(graph, u);
            Map<V, V> pred = dijkstra.predecessors();
            Map<V, Number> dist = dijkstra.distances();
            //---------------------------------------------
            for (V v : graph) {
                if (!pred.get(v).equals(v)) {
                    h.put(u, v, dist.get(v).doubleValue()
                            + d.get(v).doubleValue() - d.get(u).doubleValue());
                }
                p.put(u, v, pred.get(v));
            }
            //---------------------------------------------
        }
        //------------------------------------------------------------------
    }

} //End of class JohnsonAllPairsShortestPaths<V>
