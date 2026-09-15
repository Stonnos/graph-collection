/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.Collection;

/**
 * 
 * @author Рома
 * @param <V>
 * @param <E> 
 */
public interface MultiGraph<V, E extends Edge<V>> {
    public Collection<E> edges(V v1, V v2);
}
