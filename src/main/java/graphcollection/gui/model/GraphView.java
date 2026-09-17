package graphcollection.gui.model;

import lombok.Getter;

@Getter
public enum GraphView {
    HASH_SET_GRAPH("Cписки смежности (HashSet)"),
    TREE_SET_GRAPH("Cписки смежности (TreeSet)"),
    SORTED_SET_GRAPH("Cписки смежности (ListSet)"),
    LIST_SET_GRAPH("Cписки смежности (SortedSet)"),
    MATRIX_GRAPH("Матрица смежности");

    GraphView(String text) {
        this.text = text;
    }

    private final String text;
}
