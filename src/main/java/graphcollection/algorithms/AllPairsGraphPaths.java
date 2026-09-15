/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms;

import graphcollection.util.MatrixMap;
import java.util.Collection;

/**
 *
 * @author Рома
 * @param <V>
 */
public interface AllPairsGraphPaths<V> {

    public MatrixMap<V, V, V> predecessorsMatrix();

    public MatrixMap<V, V, Number> distancesMatrix();

    public Number getDistance(V u, V v);

    public V getPredecessor(V u, V v);

    public boolean isPath(V u, V v);

    public Collection<V> getPath(V u, V v);
}
