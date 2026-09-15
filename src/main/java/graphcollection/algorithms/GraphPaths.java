/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms;

import java.util.Map;
import java.util.Collection;

/**
 *
 * @author Рома
 * @param <V>
 */
public interface GraphPaths<V> {

    public Map<V, V> predecessors();

    public Map<V, Number> distances();

    public Collection<V> getPath(V target);

    public V getSource();

    public boolean isPath(V target);

    public V getPredecessor(V target);

    public Number getDistance(V target);
}
