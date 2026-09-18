package graphcollection.gui.dialog;

import graphcollection.gui.model.GraphView;
import graphcollection.gui.text.IntegerDocument;
import graphcollection.gui.text.TextFieldInputVerifier;
import graphcollection.gui.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static graphcollection.gui.JGraphDrawer.MAX_VERTEX_COUNT;
import static graphcollection.gui.util.ButtonUtils.createButton;

public class GraphGeneratorDialog extends JDialog {

    private static final String[] GRAPH_VIEWS = new String[] {
            GraphView.MATRIX_GRAPH.getText(),
            GraphView.HASH_SET_GRAPH.getText(),
            GraphView.TREE_SET_GRAPH.getText(),
            GraphView.LIST_SET_GRAPH.getText(),
            GraphView.SORTED_SET_GRAPH.getText()
    };
    private static final int DEFAULT_NUM_VERTICES = 6;
    private static final int DEFAULT_NUM_EDGES = 12;
    private static final int DEFAULT_EDGE_WEIGHT_LOWER_BOUND = 1;
    private static final int DEFAULT_EDGE_WEIGHT_UPPER_BOUND = 25;

    private final JTextField vertexNumText;
    private final JTextField edgeNumText;
    private final JRadioButton directedGraph;
    private final JComboBox<String> graphView;
    private final JCheckBox isWeighted;
    private final JTextField weighLowerBound;
    private final JTextField weighUpperBound;
    private boolean dialogResult = false;

    public GraphGeneratorDialog(JFrame parent) {
        super(parent, "Создание случайного графа", true);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        JLabel numVerticesLabel = new JLabel("Количество вершин:");
        numVerticesLabel.setFont(numVerticesLabel.getFont().deriveFont(Font.BOLD));
        JLabel numEdgesLabel = new JLabel("Количество ребер:");
        numEdgesLabel.setFont(numEdgesLabel.getFont().deriveFont(Font.BOLD));
        JLabel graphTypeLabel = new JLabel("Тип графа:");
        graphTypeLabel.setFont(graphTypeLabel.getFont().deriveFont(Font.BOLD));
        this.add(numVerticesLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        vertexNumText = new JTextField(3);
        vertexNumText.setInputVerifier(new TextFieldInputVerifier());
        vertexNumText.setDocument(new IntegerDocument(3));
        vertexNumText.setText(String.valueOf(DEFAULT_NUM_VERTICES));
        edgeNumText = new JTextField(3);
        edgeNumText.setInputVerifier(new TextFieldInputVerifier());
        edgeNumText.setDocument(new IntegerDocument(3));
        edgeNumText.setText(String.valueOf(DEFAULT_NUM_EDGES));
        this.add(vertexNumText, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        this.add(numEdgesLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
        this.add(edgeNumText, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
        this.add(graphTypeLabel, new GridBagConstraints(0, 2, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        ButtonGroup group = new ButtonGroup();
        directedGraph = new JRadioButton("Ориентированный", false);
        directedGraph.setFont(directedGraph.getFont().deriveFont(Font.BOLD));
        JRadioButton undirectedGraph = new JRadioButton("Неориентированный", true);
        undirectedGraph.setFont(undirectedGraph.getFont().deriveFont(Font.BOLD));
        group.add(directedGraph);
        group.add(undirectedGraph);
        this.add(directedGraph, new GridBagConstraints(0, 3, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 5, 0, 5), 0, 0));
        this.add(undirectedGraph, new GridBagConstraints(0, 4, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));

        JLabel graphViewLabel = new JLabel("Тип представления:");
        graphViewLabel.setFont(graphViewLabel.getFont().deriveFont(Font.BOLD));
        graphView = new JComboBox<>(GRAPH_VIEWS);
        this.add(graphViewLabel, new GridBagConstraints(0, 5, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        this.add(graphView, new GridBagConstraints(0, 6, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 10, 5), 0, 0));

        JButton okButton = createButton("OK");
        JButton cancelButton = createButton("Отмена");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (GuiUtils.isEmpty(vertexNumText) || GuiUtils.isEmpty(edgeNumText)) {
                    return;
                }
                int vertices = Integer.parseInt(vertexNumText.getText());
                if (vertices > MAX_VERTEX_COUNT) {
                    vertexNumText.setToolTipText(
                            "Превышено максимальное число вершин: %s!".formatted(MAX_VERTEX_COUNT));
                    GuiUtils.showToolTipProgrammatically(vertexNumText, vertexNumText.getWidth() / 10,
                            vertexNumText.getHeight() / 3);
                    return;
                }
                if (isWeighted()) {
                    if (GuiUtils.isEmpty(weighLowerBound) || GuiUtils.isEmpty(weighUpperBound)) {
                        return;
                    }
                    int lowerBound = Integer.parseInt(weighLowerBound.getText());
                    int upperBound = Integer.parseInt(weighUpperBound.getText());
                    if (lowerBound >= upperBound) {
                        weighLowerBound.setToolTipText("Нижняя граница не должна превышать верхнюю!");
                        GuiUtils.showToolTipProgrammatically(weighLowerBound, weighLowerBound.getWidth() / 10,
                                weighLowerBound.getHeight());
                        return;
                    }
                }
                dialogResult = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(evt -> {
            dialogResult = false;
            setVisible(false);
        });

        isWeighted = new JCheckBox("Сгенерировать веса");
        isWeighted.setFont(isWeighted.getFont().deriveFont(Font.BOLD));

        isWeighted.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                weighLowerBound.setEditable(isWeighted.isSelected());
                weighUpperBound.setEditable(isWeighted.isSelected());
                if (isWeighted.isSelected()) {
                    weighLowerBound.setInputVerifier(new TextFieldInputVerifier());
                    weighUpperBound.setInputVerifier(new TextFieldInputVerifier());
                } else {
                    weighLowerBound.setInputVerifier(null);
                    weighUpperBound.setInputVerifier(null);
                    weighLowerBound.setToolTipText(null);
                }
            }
        });

        this.add(isWeighted, new GridBagConstraints(0, 7, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 5, 5), 0, 0));
        weighLowerBound = new JTextField(12);
        weighLowerBound.setDocument(new IntegerDocument(12));
        weighLowerBound.setToolTipText("Нижняя граница");
        weighLowerBound.putClientProperty("JTextField.placeholderText", "Нижняя граница");
        weighUpperBound = new JTextField(12);
        weighUpperBound.putClientProperty("JTextField.placeholderText", "Верхняя граница");
        weighUpperBound.setDocument(new IntegerDocument(12));
        weighLowerBound.setEditable(isWeighted.isSelected());
        weighUpperBound.setEditable(isWeighted.isSelected());
        weighLowerBound.setText(String.valueOf(DEFAULT_EDGE_WEIGHT_LOWER_BOUND));
        weighUpperBound.setText(String.valueOf(DEFAULT_EDGE_WEIGHT_UPPER_BOUND));
        this.add(weighLowerBound, new GridBagConstraints(0, 8, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(0, 10, 0, 10), 0, 0));
        this.add(weighUpperBound, new GridBagConstraints(1, 8, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(0, 10, 0, 10), 0, 0));

        this.add(okButton, new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.CENTER, new Insets(25, 5, 15, 5), 0, 0));
        this.add(cancelButton, new GridBagConstraints(1, 9, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.CENTER, new Insets(25, 5, 15, 5), 0, 0));
        this.getRootPane().setDefaultButton(okButton);
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
        return directedGraph.isSelected();
    }

    public boolean isWeighted() {
        return isWeighted.isSelected();
    }

    public Integer lowerBound() {
        return isWeighted() ? Integer.valueOf(weighLowerBound.getText()) : null;
    }

    public Integer upperBound() {
        return isWeighted() ? Integer.valueOf(weighUpperBound.getText()) : null;
    }

    public boolean dialogResult() {
        return dialogResult;
    }

    public GraphView graphView() {
        return switch (graphView.getSelectedIndex()) {
            case 0 -> GraphView.MATRIX_GRAPH;
            case 1 -> GraphView.HASH_SET_GRAPH;
            case 2 -> GraphView.TREE_SET_GRAPH;
            case 3 -> GraphView.LIST_SET_GRAPH;
            case 4 -> GraphView.SORTED_SET_GRAPH;
            default -> null;
        };
    }
}
