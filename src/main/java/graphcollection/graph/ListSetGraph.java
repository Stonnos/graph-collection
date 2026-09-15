/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import graphcollection.util.ListSet;

/**
 * 
 * @author Рома
 * @param <V>
 * @param <E> 
 */
public class ListSetGraph<V, E extends Edge<V>>
  extends AbstractGraph<V, E> {

    public ListSetGraph() {}
     
    public ListSetGraph(boolean direction) {
        super(direction);
    }
    
    public ListSetGraph(boolean direction, boolean loop) {
        super(direction, loop);
    }
   
    @Override
    public boolean addVertex(V v) {
        return addEdgeList(v, new ListSet<E>());  
    }

} //End of class ListSetGraph<V, E>
