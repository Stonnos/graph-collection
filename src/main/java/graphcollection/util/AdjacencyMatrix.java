/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @param <E>
 * @author Рома
 */
public abstract class AdjacencyMatrix<E> implements Iterable<E>, Cloneable,
        java.io.Serializable {

    protected int size;
    protected int modCount;
    protected int elementsModCount;
    protected ArrayList<ArrayList<E>> matrix;

    public abstract void add();

    public abstract void remove(int i);

    public abstract E get(int i, int j);

    public abstract boolean add(int i, int j, E value);

    public abstract boolean remove(int i, int j);

    public abstract Iterator<E> rowIterator(int i);

    public abstract Iterator<E> columnIterator(int i);

    public final int size() {
        return size;
    }

    public final int dimension() {
        return matrix.size();
    }

    public final void clear() {
        for (int i = 0; i < dimension(); i++) {
            Iterator<E> row = rowIterator(i);
            while (row.hasNext()) {
                row.next();
                row.remove();
            }
        }
        elementsModCount = 0;
    }

    public final void clearAll() {
        for (ArrayList<E> row : matrix) {
            row.clear();
        }
        matrix.clear();
        size = 0;
        modCount = elementsModCount = 0;
    }

    public final int rowCount(int i) {
        rangeCheck(i);
        Iterator<E> row = rowIterator(i);
        int count = 0;
        while (row.hasNext()) {
            row.next();
            count++;
        }
        return count;
    }

    public final int columnCount(int i) {
        rangeCheck(i);
        Iterator<E> column = columnIterator(i);
        int count = 0;
        while (column.hasNext()) {
            column.next();
            count++;
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < dimension(); i++) {
            str.append(matrix.get(i)).append(System.getProperty("line.separator"));
        }
        return str.toString();
    }

    @Override
    public Object clone() {
        AdjacencyMatrix<E> clone;
        try {
            clone = (AdjacencyMatrix<E>) super.clone();
        } catch (CloneNotSupportedException e) {
             throw new IllegalStateException(e);
        }
        //----------------------------------------------
        clone.matrix = (ArrayList<ArrayList<E>>) matrix.clone();
        clone.size = size;
        for (int i = 0; i < dimension(); i++) {
            clone.matrix.set(i, (ArrayList<E>) matrix.get(i).clone());
        }
        //-----------------------------------------------
        return clone;
    }

    protected final E element(int i, int j) {
        return matrix.get(i).get(j);
    }

    protected final void rangeCheck(int i) {
        if (i < 0 || i > dimension()) {
            throw new IndexOutOfBoundsException();
        }
    }

    protected ArrayList<E> createString(int dimension) {
        ArrayList<E> array = new ArrayList<E>(dimension);
        for (int i = 0; i < dimension; i++) {
            array.add(null);
        }
        return array;
    }

} //End of class AdjacencyMatrix<E>
