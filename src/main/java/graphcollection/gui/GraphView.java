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
    Hash_Set_Graph("Cписки смежности(HashSet)"),
    Tree_Set_Graph("Cписки смежности(TreeSet)"),
    Sorted_Set_Graph("Cписки смежности(ListSet)"),
    List_Set_Graph("Cписки смежности(SortedSet)"),
    Matrix_Graph("Матрица смежности");

    GraphView(String text) {
        this.text = text;
    }

    String getText() {
        return text;
    }


    private final String text;
}
