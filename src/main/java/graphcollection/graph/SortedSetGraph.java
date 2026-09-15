/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import graphcollection.util.SortedSet;

public class SortedSetGraph<V, E extends Edge<V>>
  extends AbstractGraph<V, E> {

    public SortedSetGraph() {}
     
    public SortedSetGraph(boolean direction) {
        super(direction);
    }
    
    public SortedSetGraph(boolean direction, boolean loop) {
        super(direction, loop);
    }
   
    @Override
    public boolean addVertex(V v) {
        return addEdgeList(v, new SortedSet<E>(new EdgeComparator<V,E>()));  
    }

} //End of class SortedSetGraph<V, E>

