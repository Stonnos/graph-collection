/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E>
 * @author Рома
 */
public class UnDirectedAdjacencyMatrix<E> extends AdjacencyMatrix<E> {

    public UnDirectedAdjacencyMatrix() {
        matrix = new ArrayList<ArrayList<E>>();
    }

    public UnDirectedAdjacencyMatrix(int dimension) {
        createMatrix(dimension);
    }

    @Override
    public void add() {
        matrix.add(createString(dimension()));
        modCount++;
    }

    @Override
    public void remove(int i) {
        rangeCheck(i);
        Iterator<E> row = rowIterator(i);
        while (row.hasNext()) {
            row.next();
            row.remove();
        }
        matrix.remove(i);
        for (int j = i; j < dimension(); j++) {
            matrix.get(j).remove(i);
        }
        modCount++;
    }

    private void createMatrix(int dimension) {
        matrix = new ArrayList<ArrayList<E>>(dimension);
        for (int i = 0; i < dimension; i++) {
            matrix.add(createString(i));
        }
    }

    @Override
    public boolean add(int i, int j, E value) {
        rangeCheck(i);
        rangeCheck(j);
        if (i == j) {
            return false;
        }
        if (i < j) {
            j ^= (i ^= j);
            i ^= j;
        }
        if (element(i, j) == null) {
            matrix.get(i).set(j, value);
            size++;
            elementsModCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(int i, int j) {
        rangeCheck(i);
        rangeCheck(j);
        if (i == j) {
            return false;
        }
        if (i < j) {
            j ^= (i ^= j);
            i ^= j;
        }
        if (element(i, j) != null) {
            matrix.get(i).set(j, null);
            size--;
            elementsModCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E get(int i, int j) {
        rangeCheck(i);
        rangeCheck(j);
        if (i == j) {
            return null;
        }
        if (i < j) {
            j ^= (i ^= j);
            i ^= j;
        }
        return element(i, j);
    }

    /**
     *
     */
    private class MatrixIterator implements Iterator<E> {

        private int current;
        private int i, j = -1;
        private final int expectedCount = elementsModCount;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public E next() {
            if (elementsModCount != expectedCount) {
                throw new ConcurrentModificationException();
            }
            if (hasNext()) {
                for (; i < dimension(); i++) {
                    for (j++; j < i; j++) {
                        if (element(i, j) != null) {
                            current++;
                            return element(i, j);
                        }
                    }
                    j = -1;
                }
            }
            throw new NoSuchElementException();
        }
    } // End of class MatrixIterator

    /**
     *
     */
    private class RowIterator implements Iterator<E> {

        private int currentIndexI = -1;
        private int currentIndexJ = -1;
        private int i, j = -1;
        private E element;
        private final int expectedCount = modCount;

        public RowIterator(int i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            if (element == null) {
                for (j++; j < dimension(); j++) {
                    if (j == i) {
                        continue;
                    }
                    element = (j < i ? element(i, j) : element(j, i));
                    if (element != null) {
                        return true;
                    }
                }
                element = null;
                j = dimension();
                return false;
            }
            return true;
        }

        @Override
        public E next() {
            if (modCount != expectedCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E e = element;
            element = null;
            currentIndexI = (j < i ? i : j);
            currentIndexJ = (j < i ? j : i);
            return e;
        }

        @Override
        public void remove() {
            if (currentIndexJ < 0) {
                throw new NoSuchElementException();
            }
            matrix.get(currentIndexI).set(currentIndexJ, null);
            size--;
        }

    } //End of class RowIterator

    @Override
    public Iterator<E> rowIterator(int i) {
        rangeCheck(i);
        return new RowIterator(i);
    }

    @Override
    public Iterator<E> columnIterator(int i) {
        return rowIterator(i);
    }

    @Override
    public Iterator<E> iterator() {
        return new MatrixIterator();
    }
}
