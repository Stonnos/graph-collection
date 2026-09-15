/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.graph;

/**
 * 
 * @author Рома
 * @param <V>
 * @param <W> 
 */
public class WeightedEdge<V, W extends Number> extends Edge<V> {
    
    private W weight;

    public WeightedEdge(boolean direction, V v1, V v2) {
        super(direction,v1,v2);
    }
       
    public WeightedEdge(boolean direction, V v1, V v2, W weight) {
        this(direction,v1,v2);
        this.weight = weight;
    }
       
    public void setWeight(W weight) {
        this.weight = weight;
    }
       
    public W getWeight() {
        return weight;
    }
       
    @Override
    public String toString() {
        return weight == null ? super.toString() :
                "(" + source() + "," + target() + "," + weight + ")";
    }
       
} //End of class WeighttedEdge<V, E>
