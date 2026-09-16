package graphcollection.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GraphGeneratorDialog extends JDialog {

    private final JTextField vertexNumText;
    private final JTextField edgeNumText;
    private final JRadioButton directedG;
    private final JComboBox<String> graphView;
    private final JCheckBox isWeighted;
    private final JTextField lower;
    private final JTextField upper;
    private boolean dialogResult = false;

    public GraphGeneratorDialog(JFrame parent) {
        super(parent, "Создание случайного графа", true);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.add(new JLabel("Количество вершин:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        vertexNumText = new JTextField(3);
        edgeNumText = new JTextField(3);
        this.add(vertexNumText, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        this.add(new JLabel("Количество ребер:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        this.add(edgeNumText, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        this.add(new JLabel("Тип графа:"), new GridBagConstraints(0, 2, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        ButtonGroup group = new ButtonGroup();
        directedG = new JRadioButton("Ориентированный", false);
        JRadioButton undirectedG = new JRadioButton("Неориентированный", true);
        group.add(directedG);
        group.add(undirectedG);
        this.add(directedG, new GridBagConstraints(0, 3, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 5, 0, 5), 0, 0));
        this.add(undirectedG, new GridBagConstraints(0, 4, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));

        JLabel graphViewLabel = new JLabel("Тип представления:");
        String[] items = {GraphView.MATRIX_GRAPH.getText(),
                GraphView.HASH_SET_GRAPH.getText(),
                GraphView.TREE_SET_GRAPH.getText(),
                GraphView.LIST_SET_GRAPH.getText(),
                GraphView.SORTED_SET_GRAPH.getText()};
        graphView = new JComboBox<String>(items);
        this.add(graphViewLabel, new GridBagConstraints(0, 5, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        this.add(graphView, new GridBagConstraints(0, 6, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 10, 5), 0, 0));

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String errorMessage = "Только целые числа!";
                try {
                    Integer.parseInt(vertexNumText.getText());
                    Integer.parseInt(edgeNumText.getText());
                    if (isWeighted()) {
                        int a = Integer.parseInt(lower.getText());
                        int b = Integer.parseInt(upper.getText());
                        if (a >= b) {
                            errorMessage = "Нижняя граница не должна превышать верхнюю!";
                            throw new NumberFormatException();
                        }
                    }
                    dialogResult = true;
                    setVisible(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(GraphGeneratorDialog.this,
                            errorMessage,
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
                    vertexNumText.requestFocusInWindow();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dialogResult = false;
                setVisible(false);
            }
        });

        isWeighted = new JCheckBox("Сгенерировать веса");

        isWeighted.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                lower.setEditable(isWeighted.isSelected());
                upper.setEditable(isWeighted.isSelected());
            }
        });

        this.add(isWeighted, new GridBagConstraints(0, 7, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        lower = new JTextField(12);
        lower.setToolTipText("Нижняя граница");
        upper = new JTextField(12);
        upper.setToolTipText("Верхняя граница");
        lower.setEditable(isWeighted.isSelected());
        upper.setEditable(isWeighted.isSelected());
        this.add(lower, new GridBagConstraints(0, 8, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 10, 0, 10), 0, 0));
        this.add(upper, new GridBagConstraints(1, 8, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(0, 10, 0, 10), 0, 0));

        this.add(okButton, new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(10, 5, 15, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 9, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(10, 5, 15, 5), 0, 0));
        this.pack();
        this.setLocationRelativeTo(parent);
        vertexNumText.requestFocusInWindow();
    }

    public int verticesNum() {
        return Integer.parseInt(vertexNumText.getText());
    }

    public int edgesNum() {
        return Integer.parseInt(edgeNumText.getText());
    }

    public boolean direction() {
        return directedG.isSelected();
    }

    public boolean isWeighted() {
        return isWeighted.isSelected();
    }

    public Integer lowerBound() {
        return isWeighted() ? Integer.valueOf(lower.getText()) : null;
    }

    public Integer upperBound() {
        return isWeighted() ? Integer.valueOf(upper.getText()) : null;
    }

    public boolean dialogResult() {
        return dialogResult;
    }

    public GraphView graphView() {
        GraphView view = null;
        switch (graphView.getSelectedIndex()) {
            case 0:
                view = GraphView.MATRIX_GRAPH;
                break;
            case 1:
                view = GraphView.HASH_SET_GRAPH;
                break;
            case 2:
                view = GraphView.TREE_SET_GRAPH;
                break;
            case 3:
                view = GraphView.LIST_SET_GRAPH;
                break;
            case 4:
                view = GraphView.SORTED_SET_GRAPH;
                break;
        }
        return view;
    }
}
