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
 * @param <V>
 * @author Рома
 */
public interface AllPairsGraphPaths<V> {

    MatrixMap<V, V, V> predecessorsMatrix();

    MatrixMap<V, V, Number> distancesMatrix();

    Number getDistance(V u, V v);

    V getPredecessor(V u, V v);

    boolean isPath(V u, V v);

    Collection<V> getPath(V u, V v);
}
