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
 */
public class Edge<V>
  implements Cloneable, java.io.Serializable {

    private final boolean direction;
    private V source;
    private V target;

    /**
     * Constructs edge 
     * @param direction type of direction.
     * <tt>true</tt> - directed edge,
     * <tt>false</tt> - undirected edge
     * @param source initial vertex
     * @param target end vertex
     * @throws NullPointerException if source or target is null
     */
    public Edge(boolean direction, V source, V target) {
         check(source,target);
         this.direction = direction;
         this.source = source;
         this.target = target;
    }

    /**
     * Returns type of edge's direction
     * @return type of edge's direction
     */
    public final boolean direction() {
       return direction;
    }

    /**
     * Exchange initial and end vertices if the edge's direction
     * is undirected
     */
    public final void exchange() {
      if (!direction()) {
         V u = source;
         source = target;
         target = u;
      }
    }
    
    /**
     * Returns initial vertex of this edge
     * @return initial vertex of this edge
     */
    public final V source() {
        return source;
    }

    /**
     * Returns end vertex of this edge
     * @return end vertex of this edge
     */
    public final V target() {
        return target;
    }

    @Override
    public final int hashCode() {
         return source.hashCode() | target.hashCode();
    }

    /**
     * Equals two edges
     * @param obj other edge
     * @return <tt>false</tt> if edges have different types of directions<br>
     * <tt>false</tt> if edges are not equal<br>
     * <tt>false</tt> if obj is null or obj is not subclass of class Edge<br>
     * <tt>true</tt> otherwise. If edges types are undirected, then (u,v) = (v,u)
     */
    @Override
    public final boolean equals(Object obj) {
         if (this == obj)
             return true;
         if (obj != null && obj instanceof Edge<?> &&
             direction() == ((Edge<V>) obj).direction()) {
                return ((source.equals(((Edge<V>) obj).source) &&
                         target.equals(((Edge<V>) obj).target)) ||
                        (!direction() && source.equals(((Edge<V>) obj).target) &&
                         target.equals(((Edge<V>) obj).source)));
         }
         else return false; 
    }

    @Override
    public String toString() {
        return "(" + source() + "," + target() + ")";
    }
    
    /**
     * Returns a copy of this <tt>Edge</tt> instance: the vertices
     * are not cloned.
     * @return copy of this edge
     */
    @Override
    public Object clone() {
       Edge<V> clone;
       try {
           clone = (Edge<V>)super.clone();
       }
       catch(CloneNotSupportedException e) {
           throw new InternalError(e);
       }
       return clone;
    }    

    private void check(V u, V v) {
        if (u == null || v == null)
            throw new NullPointerException();
    }

} //End of class Edge<V>
