/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

/**
 *
 * @author Рома
 */
public enum GraphView {
    HASH_SET_GRAPH("Cписки смежности (HashSet)"),
    TREE_SET_GRAPH("Cписки смежности (TreeSet)"),
    SORTED_SET_GRAPH("Cписки смежности (ListSet)"),
    LIST_SET_GRAPH("Cписки смежности (SortedSet)"),
    MATRIX_GRAPH("Матрица смежности");

    GraphView(String text) {
        this.text = text;
    }

    String getText() {
        return text;
    }

    private final String text;
}
