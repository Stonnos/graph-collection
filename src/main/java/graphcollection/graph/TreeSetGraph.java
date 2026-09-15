/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

import java.util.TreeSet;

/**
 * 
 * @author Рома
 * @param <V>
 * @param <E> 
 */
public class TreeSetGraph<V, E extends Edge<V>>
  extends AbstractGraph<V, E> {

    public TreeSetGraph() {}
     
    public TreeSetGraph(boolean direction) {
        super(direction);
    }
    
    public TreeSetGraph(boolean direction, boolean loop) {
        super(direction, loop);
    }
   
    @Override
    public boolean addVertex(V v) {
        return addEdgeList(v, new TreeSet<E>(new EdgeComparator<V,E>()));  
    }

} //End of class TreeSetGraph<V, E>