/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.Collection;

/**
 *
 * @param <V>
 * @param <E>
 * @author Рома
 */
public interface MultiGraph<V, E extends Edge<V>> {
    Collection<E> edges(V v1, V v2);
}
