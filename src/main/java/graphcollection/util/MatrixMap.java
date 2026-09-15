/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Рома
 * @param <K1>
 * @param <K2>
 * @param <V>
 */
public class MatrixMap<K1, K2, V>
        implements Iterable<K1>, Cloneable, java.io.Serializable {

    private HashMap<K1, HashMap<K2, V>> matrix = new HashMap<K1, HashMap<K2, V>>();
    private int size;

    public int sizeX() {
        return matrix.size();
    }

    public int size() {
        return size;
    }

    public int sizeY(K1 x) {
        HashMap<K2, V> map = matrix.get(x);
        return (map != null ? map.size() : -1);
    }

    public boolean isEmpty() {
        return sizeX() == 0;
    }

    public void put(K1 x, K2 y, V obj) {
        HashMap<K2, V> map = matrix.get(x);
        if (map == null) {
            map = new HashMap<K2, V>();
            matrix.put(x, map);
        }
        map.put(y, obj);
        size++;
    }

    public void put(K1 x) {
        HashMap<K2, V> map = matrix.get(x);
        if (map == null) {
            map = new HashMap<K2, V>();
            matrix.put(x, map);
        }
    }

    public V get(K1 x, K2 y) {
        HashMap<K2, V> map = matrix.get(x);
        return (map != null ? map.get(y) : null);
    }

    public boolean containsKey(K1 x) {
        return matrix.containsKey(x);
    }

    public boolean containsKey(K1 x, K2 y) {
        HashMap<K2, V> map = matrix.get(x);
        return (map != null ? map.containsKey(y) : false);
    }

    public boolean remove(K1 x) {
        HashMap<K2, V> map = matrix.remove(x);
        if (map != null) {
            size -= map.size();
            return true;
        } else {
            return false;
        }
    }

    public V remove(K1 x, K2 y) {
        HashMap<K2, V> map = matrix.get(x);
        if (map != null) {
            V val = map.remove(y);
            if (val != null) {
                size--;
            }
            return val;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<K1> iterator() {
        return matrix.keySet().iterator();
    }

    public Iterator<Map.Entry<K2, V>> bucketsIteraror(K1 x) {
        HashMap<K2, V> map = matrix.get(x);
        return (map != null ? map.entrySet().iterator() : null);
    }

    @Override
    public Object clone() {
        MatrixMap<K1, K2, V> map;
        try {
            map = (MatrixMap<K1, K2, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        map.matrix = (HashMap<K1, HashMap<K2, V>>) matrix.clone();
        map.size = size;
        for (Map.Entry<K1, HashMap<K2, V>> p : map.matrix.entrySet()) {
            p.setValue((HashMap<K2, V>) matrix.get(p.getKey()).clone());
        }
        return map;
    }

    public void clearBuckets() {
        for (HashMap<K2, V> s : matrix.values()) {
            s.clear();
        }
        size = 0;
    }

    public void clear() {
        clearBuckets();
        matrix.clear();
    }

} //End of class MatrixMap<K1, K2, V>
