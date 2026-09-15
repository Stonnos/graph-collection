/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.trees;

import java.util.Collection;

/**
 *
 * @author Рома
 */
public interface MinimumSpanningTree<E> {

    public Number getMinimumSpanningTreeWeight();

    public Collection<E> getMinimumSpanningTreeEdges();
}
