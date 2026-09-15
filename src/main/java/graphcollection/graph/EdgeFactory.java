/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

/**
 *
 * @author Рома
 */
public class EdgeFactory {

    public static final <V> Edge<V> directedEdge(V v1, V v2) {
        return new Edge<>(true, v1, v2);
    }

    public static final <V> Edge<V> undirectedEdge(V v1, V v2) {
        return new Edge<>(false, v1, v2);
    }

    public static final <V, W extends Number>
    WeightedEdge<V, W> directedWeightedEdge(V v1, V v2, W weight) {
        return new WeightedEdge<>(true, v1, v2, weight);
    }

    public static final <V, W extends Number>
    WeightedEdge<V, W> undirectedWeightedEdge(V v1, V v2, W weight) {
        return new WeightedEdge<>(false, v1, v2, weight);
    }

}
