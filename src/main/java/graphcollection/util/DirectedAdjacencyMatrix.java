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
 * @author Рома
 * @param <E>
 */
public class DirectedAdjacencyMatrix<E> extends AdjacencyMatrix<E> {

    public DirectedAdjacencyMatrix() {
        matrix = new ArrayList<ArrayList<E>>();
    }

    public DirectedAdjacencyMatrix(int dimension) {
        createMatrix(dimension);
    }

    @Override
    public void add() {
        for (int i = 0; i < dimension(); i++) {
            matrix.get(i).add(null);
        }
        matrix.add(createString(dimension() + 1));
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
        Iterator<E> column = columnIterator(i);
        while (column.hasNext()) {
            column.next();
            column.remove();
        }
        matrix.remove(i);
        for (int j = 0; j < dimension(); j++) {
            matrix.get(j).remove(i);
        }
        modCount++;
    }

    private void createMatrix(int dimension) {
        matrix = new ArrayList<ArrayList<E>>(dimension);
        for (int i = 0; i < dimension; i++) {
            matrix.add(createString(dimension));
        }
    }

    @Override
    public boolean add(int i, int j, E value) {
        rangeCheck(i);
        rangeCheck(j);
        if (i == j) {
            return false;
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
                    for (j++; j < dimension(); j++) {
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
    }  // End of class MatrixIterator

    /**
     *
     */
    private class RowIterator implements Iterator<E> {

        private int currentIndex = -1;
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
                    element = element(i, j);
                    if (element != null) {
                        return true;
                    }
                }
                j = dimension();
                element = null;
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
            currentIndex = j;
            return e;
        }

        @Override
        public void remove() {
            if (currentIndex < 0) {
                throw new NoSuchElementException();
            }
            matrix.get(i).set(currentIndex, null);
            size--;
        }

    } // End of class RowIterator

    /**
     *
     */
    private class ColumnIterator implements Iterator<E> {

        private int currentIndex = -1;
        private int j, i = -1;
        private E element;
        private final int expectedCount = modCount;

        public ColumnIterator(int j) {
            this.j = j;
        }

        @Override
        public boolean hasNext() {
            if (element == null) {
                for (i++; i < dimension(); i++) {
                    element = element(i, j);
                    if (element != null) {
                        return true;
                    }
                }
                j = dimension();
                element = null;
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
            currentIndex = i;
            return e;
        }

        @Override
        public void remove() {
            if (currentIndex < 0) {
                throw new NoSuchElementException();
            }
            matrix.get(currentIndex).set(j, null);
            size--;
        }

    } // End of class ColumnIterator

    @Override
    public Iterator<E> rowIterator(int i) {
        rangeCheck(i);
        return new RowIterator(i);
    }

    @Override
    public Iterator<E> columnIterator(int i) {
        rangeCheck(i);
        return new ColumnIterator(i);
    }

    @Override
    public Iterator<E> iterator() {
        return new MatrixIterator();
    }

} //End of class DirectedAdjacencyMatrix<E>
