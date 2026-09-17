/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.shortestpaths;

/**
 *
 * @param <V>
 * @param <D>
 * @author Рома
 */
public class VertexDistance<V, D extends Number>
        implements Comparable<VertexDistance<V, D>>, Cloneable, java.io.Serializable {

    public V vertex;
    public D distance;

    public VertexDistance(V vertex, D distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(VertexDistance<V, D> obj) {
        return (((Comparable<D>) distance).compareTo(obj.distance));
    }

    @Override
    public Object clone() {
        VertexDistance<V, Number> clone;
        try {
            clone = (VertexDistance<V, Number>) super.clone();
        } catch (CloneNotSupportedException e) {
             throw new IllegalStateException(e);
        }
        clone.vertex = vertex;
        clone.distance = distance;
        return clone;
    }

} //End of class VertexDistance<V,D>
