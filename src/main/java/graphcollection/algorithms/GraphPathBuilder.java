/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms;

import graphcollection.util.MatrixMap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Рома
 */
public class GraphPathBuilder {

    public static <V> Collection<V> path(Map<V, V> p, V v) {
        LinkedList<V> path = new LinkedList<V>();
        path.addFirst(v);
        while (!p.get(v).equals(v)) {
            v = p.get(v);
            path.addFirst(v);
        }
        return path;
    }

    public static <V> Collection<V> path(MatrixMap<V, V, V> p, V u, V v) {
        LinkedList<V> path = new LinkedList<V>();
        path.addFirst(v);
        while (!p.get(u, v).equals(v)) {
            v = p.get(u, v);
            path.addFirst(v);
        }
        return path;
    }

} //End of class GraphPathBuilder
