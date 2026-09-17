/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import graphcollection.algorithms.GraphPaths;
import graphcollection.algorithms.TransitiveClosure;
import graphcollection.algorithms.cc.AbstractConnectedComponents;
import graphcollection.algorithms.cc.ConnectedComponents;
import graphcollection.algorithms.cc.StronglyConnectedComponents;
import graphcollection.algorithms.dfs.AbstractTourSearch;
import graphcollection.algorithms.dfs.AllPathsSearch;
import graphcollection.algorithms.dfs.BridgesSearch;
import graphcollection.algorithms.dfs.DepthFirstSearch;
import graphcollection.algorithms.dfs.EulerTourSearch;
import graphcollection.algorithms.dfs.GamiltonTourSearch;
import graphcollection.algorithms.dfs.TopologicalSort;
import graphcollection.algorithms.metrics.GraphMetricProperties;
import graphcollection.algorithms.shortestpaths.AllPairsShortestPaths;
import graphcollection.algorithms.shortestpaths.BellmanFordShortestPaths;
import graphcollection.algorithms.shortestpaths.BreadFirstSearch;
import graphcollection.algorithms.shortestpaths.DAGShortestPaths;
import graphcollection.algorithms.shortestpaths.DijkstraShortestPaths;
import graphcollection.algorithms.shortestpaths.FloydWarshallAllPairsShortestPaths;
import graphcollection.algorithms.shortestpaths.JohnsonAllPairsShortestPaths;
import graphcollection.algorithms.shortestpaths.ShortestPaths;
import graphcollection.algorithms.trees.KruskalMinimumSpanningTree;
import graphcollection.algorithms.trees.MinimumSpanningTree;
import graphcollection.algorithms.trees.MinimumSpanningTreeClustering;
import graphcollection.algorithms.trees.PrimMinimumSpanningTree;
import graphcollection.graph.Graph;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import static graphcollection.gui.ButtonUtils.createButton;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 *
 * @author Рома
 */
public class JGraphFrame extends JFrame {

    private static final String TITLE = "Приложение для работы с графом";
    private static final String SEPARATOR = System.getProperty("line.separator");
    private static final Color FRAME_COLOR = new Color(227, 232, 234);
    private static final int ICON_SIZE = 18;
    private static final int OPERATIONS_ICON_SIZE = 16;
    private static final int ALGORITHMS_STEP_PANEL_SIZE = 35;
    private int animationSpeed = 2000;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel workPanel;
    private JPanel operationPanel;
    private JPanel buttonsPanel;
    private JPanel accessuryPanel;
    private JGraphDrawer graphPanel;
    private JScrollPane commentScrollPanel;
    private JTextArea commentTxt;
    private JPanel algorithmsStepPanel;
    //------------------------------------------------------
    private JLabel graphViewText;
    private JLabel graphDirectedText;
    private JLabel vertexNumText;
    private JLabel edgeNumText;
    //----------------------------------------------------
    private JMenu graphMenu;
    private JMenu algoritmsMenu;
    private JMenu directedGraphAlgorithms;
    private JMenu undirectedGraphAlgorithms;

    private JCheckBox setVertexNameCheckBox;
    private JCheckBox setEdgeWeightCheckBox;

    public final PopupService popupService = new PopupService();

    private AlgorithmsResultsPanel algorithmsResultsPanel;

    private class PathDrawer extends TimerTask {

        private final java.util.Timer timer = new java.util.Timer();
        private final Collection<Vertex> path;
        private Iterator<Vertex> vertex;
        private Vertex s;
        @Setter
        private ActionListener finishListener;

        public PathDrawer(Collection<Vertex> path) {
            this.path = path;
        }

        @Override
        public void run() {
            if (vertex.hasNext()) {
                Vertex v = vertex.next();
                v.borderColor = Color.RED;
                Edge2D e = graph().edge(s, v);
                e.color = Color.GREEN;
                e.dimension = 3;
                s = v;
                graphPanel.repaint();
                if (!vertex.hasNext()) {
                    finish();
                }
            } else {
                finish();
            }
        }

        void finish() {
            timer.cancel();
            Optional.ofNullable(finishListener).ifPresent(
                    actionListener ->
                            actionListener.actionPerformed(new ActionEvent(this, 0, EMPTY)));
        }

        public void start() {
            vertex = path.iterator();
            s = vertex.next();
            s.borderColor = Color.RED;
            timer.scheduleAtFixedRate(this, animationSpeed, animationSpeed);
        }
    } //End of class PathDrawer

    public JGraphFrame() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        createGUI();
    }

    private void setWindowListener() {
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosed(WindowEvent evt) {

            }

            @Override
            public void windowClosing(WindowEvent evt) {
                int result = JOptionPane.showConfirmDialog(JGraphFrame.this,
                        "Вы уверены, что хотите выйти?", null,
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    JGraphFrame.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dispose();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent evt) {

            }

            @Override
            public void windowActivated(WindowEvent evt) {

            }

            @Override
            public void windowOpened(WindowEvent evt) {

            }

            @Override
            public void windowDeiconified(WindowEvent evt) {

            }

            @Override
            public void windowIconified(WindowEvent evt) {

            }
        });
    }

    private void setComponentListener() {
        this.addComponentListener(new ComponentListener() {
            private static final int minimumSize = 250;

            @Override
            public void componentShown(ComponentEvent evt) {

            }

            @Override
            public void componentResized(ComponentEvent evt) {
                if (getWidth() > minimumSize && getHeight() > minimumSize) {
                    graphPanel.resize();
                }
            }

            @Override
            public void componentHidden(ComponentEvent evt) {

            }

            @Override
            public void componentMoved(ComponentEvent evt) {

            }
        });
    }

    private void setColor() {
        this.mainPanel.setBackground(FRAME_COLOR);
        this.infoPanel.setBackground(FRAME_COLOR);
        this.buttonsPanel.setBackground(FRAME_COLOR);
        this.operationPanel.setBackground(FRAME_COLOR);
        this.algorithmsStepPanel.setBackground(FRAME_COLOR);
        this.workPanel.setBackground(FRAME_COLOR);
        this.accessuryPanel.setBackground(FRAME_COLOR);
    }

    private void setEnabledForOperations(boolean aFlag) {
        for (Component comp : buttonsPanel.getComponents()) {
            comp.setEnabled(aFlag);
        }
        graphMenu.setEnabled(aFlag);
        algoritmsMenu.setEnabled(aFlag);
    }

    private void setNumbers() {
        vertexNumText.setText(String.valueOf(graph().verticesNum()));
        edgeNumText.setText(String.valueOf(graph().edgesNum()));
    }

    private Graph<Vertex, Edge2D> graph() {
        return graphPanel.graph();
    }

    private void setInfo() {
        graphViewText.setText(graphPanel.graphView().getText());
        graphDirectedText.setText(graph().direction()
                ? "Ориентированный" : "Неориентированный");
        setNumbers();
        cancelAllOperationsWithGraph();
    }

    private void setAlgorithmsEnabledFlag() {
        if (graph().isEmpty()) {
            algoritmsMenu.setEnabled(false);
        } else {
            algoritmsMenu.setEnabled(true);
            if (graph().direction()) {
                undirectedGraphAlgorithms.setEnabled(false);
                directedGraphAlgorithms.setEnabled(true);
            } else {
                directedGraphAlgorithms.setEnabled(false);
                undirectedGraphAlgorithms.setEnabled(true);
            }
        }
    }

    private void cancelAllOperationsWithGraph() {
        graphPanel.reset();
        commentTxt.setText("Выберите операцию.");
    }

    private void createGraph(GraphView view, boolean direction) {
        graphPanel.create(view, direction);
        graphPanel.repaint();
        setInfo();
        setAlgorithmsEnabledFlag();
    }

    private void createMenu() {
        JMenuBar menu = new JMenuBar();
        graphMenu = new JMenu("Граф");
        graphMenu.setIcon(IconFontSwing.buildIcon(FontAwesome.SHARE_ALT, ICON_SIZE, Color.BLACK));
        algoritmsMenu = new JMenu("Алгоритмы");
        algoritmsMenu.setIcon(IconFontSwing.buildIcon(FontAwesome.SITEMAP, ICON_SIZE, Color.BLACK));
        JMenu referenceMenu = new JMenu("Справка");
        referenceMenu.setIcon(IconFontSwing.buildIcon(FontAwesome.QUESTION, ICON_SIZE, Color.BLACK));
        JMenu optionMenu = new JMenu("Настройки");
        optionMenu.setIcon(IconFontSwing.buildIcon(FontAwesome.COGS, ICON_SIZE));
        menu.add(graphMenu);
        menu.add(algoritmsMenu);
        menu.add(optionMenu);
        menu.add(referenceMenu);

        JMenu create = new JMenu("Создать граф");
        JMenuItem generate = new JMenuItem("Создать случайный граф");
        JMenuItem open = new JMenuItem("Загрузить из файла");
        open.setIcon(IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, ICON_SIZE, Color.ORANGE));
        JMenuItem save = new JMenuItem("Сохранить в файл");
        save.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, ICON_SIZE, Color.BLUE));
        JMenuItem saveImage = new JMenuItem("Сохранить изображение");
        saveImage.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, ICON_SIZE, Color.BLUE));
        graphMenu.add(create);
        graphMenu.add(generate);
        graphMenu.addSeparator();
        graphMenu.add(open);
        graphMenu.add(save);
        graphMenu.add(saveImage);
        graphMenu.addSeparator();

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                try {
                    GraphFileChooser fileChooser = SingletonRegistry.getSingleton(GraphFileChooser.class);
                    File file = fileChooser.openFile(JGraphFrame.this);
                    if (file != null) {
                        graphPanel.read(file.getPath());
                        setInfo();
                        setAlgorithmsEnabledFlag();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, ex,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                try {
                    GraphFileChooser fileChooser = SingletonRegistry.getSingleton(GraphFileChooser.class);
                    File file = fileChooser.saveFile(JGraphFrame.this);
                    if (file != null) {
                        GraphParser writer = new GraphParser();
                        writer.write(graph(), file.getPath());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, ex,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                try {
                    GraphFileChooser fileChooser = SingletonRegistry.getSingleton(GraphFileChooser.class);
                    File file = fileChooser.saveImageFile(JGraphFrame.this);
                    if (file != null) {
                        Image img = graphPanel.getImage();
                        ImageIO.write((BufferedImage) img, "png", file);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, e,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                GraphGeneratorDialog dialog = new GraphGeneratorDialog(JGraphFrame.this);
                dialog.setVisible(true);
                if (dialog.dialogResult()) {
                    try {
                        graphPanel.generate(dialog.graphView(),
                                dialog.direction(), dialog.verticesNum(), dialog.edgesNum());
                        if (dialog.isWeighted()) {
                            RandomGraph r = new RandomGraph();
                            r.generateIntWeights(graph(),
                                    dialog.lowerBound(), dialog.upperBound());
                        }
                        setInfo();
                        setAlgorithmsEnabledFlag();
                        graphPanel.repaint();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(JGraphFrame.this, e,
                                "", JOptionPane.WARNING_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        });

        JMenu directedGraph = new JMenu("Ориентированный");
        JMenu undirectedGraph = new JMenu("Неориентированный");
        create.add(directedGraph);
        create.add(undirectedGraph);
        JMenuItem directedMatrixGraph = new JMenuItem(GraphView.MATRIX_GRAPH.getText());
        JMenuItem directedHashSGraph = new JMenuItem(GraphView.HASH_SET_GRAPH.getText());
        JMenuItem directedTreeSGraph = new JMenuItem(GraphView.TREE_SET_GRAPH.getText());
        JMenuItem directedListSGraph = new JMenuItem(GraphView.LIST_SET_GRAPH.getText());
        JMenuItem directedSortedSGraph = new JMenuItem(GraphView.SORTED_SET_GRAPH.getText());
        directedGraph.add(directedMatrixGraph);
        directedGraph.add(directedHashSGraph);
        directedGraph.add(directedTreeSGraph);
        directedGraph.add(directedListSGraph);
        directedGraph.add(directedSortedSGraph);
        JMenuItem undirectedMatrixGraph = new JMenuItem(GraphView.MATRIX_GRAPH.getText());
        JMenuItem undirectedHashSGraph = new JMenuItem(GraphView.HASH_SET_GRAPH.getText());
        JMenuItem undirectedTreeSGraph = new JMenuItem(GraphView.TREE_SET_GRAPH.getText());
        JMenuItem undirectedListSGraph = new JMenuItem(GraphView.LIST_SET_GRAPH.getText());
        JMenuItem undirectedSortedSGraph = new JMenuItem(GraphView.SORTED_SET_GRAPH.getText());
        undirectedGraph.add(undirectedMatrixGraph);
        undirectedGraph.add(undirectedHashSGraph);
        undirectedGraph.add(undirectedTreeSGraph);
        undirectedGraph.add(undirectedListSGraph);
        undirectedGraph.add(undirectedSortedSGraph);
        directedMatrixGraph.addActionListener(evt -> createGraph(GraphView.MATRIX_GRAPH, true)
        );
        directedHashSGraph.addActionListener(evt -> createGraph(GraphView.HASH_SET_GRAPH, true)
        );
        directedTreeSGraph.addActionListener(evt -> createGraph(GraphView.TREE_SET_GRAPH, true)
        );
        directedListSGraph.addActionListener(evt -> createGraph(GraphView.LIST_SET_GRAPH, true)
        );
        directedSortedSGraph.addActionListener(evt -> createGraph(GraphView.SORTED_SET_GRAPH, true)
        );

        undirectedMatrixGraph.addActionListener(evt -> createGraph(GraphView.MATRIX_GRAPH, false)
        );
        undirectedHashSGraph.addActionListener(evt -> createGraph(GraphView.HASH_SET_GRAPH, false)
        );
        undirectedTreeSGraph.addActionListener(evt -> createGraph(GraphView.TREE_SET_GRAPH, false)
        );
        undirectedListSGraph.addActionListener(evt -> createGraph(GraphView.LIST_SET_GRAPH, false)
        );
        undirectedSortedSGraph.addActionListener(evt -> createGraph(GraphView.SORTED_SET_GRAPH, false)
        );

        directedGraphAlgorithms = new JMenu("Алгоритмы для ориентированных графов");
        undirectedGraphAlgorithms = new JMenu("Алгоритмы для неориентированных графов");
        algoritmsMenu.add(directedGraphAlgorithms);
        algoritmsMenu.add(undirectedGraphAlgorithms);

        JMenuItem reference = new JMenuItem("Посмотреть справку");
        JMenuItem aboutProgrammMenu = new JMenuItem("О программе");
        referenceMenu.add(reference);
        referenceMenu.add(aboutProgrammMenu);

        aboutProgrammMenu.addActionListener(evt -> {
                    BaseReference frame = new BaseReference(aboutProgrammMenu);
                    frame.readInfoFromFile("О программе.html");
                    frame.setVisible(true);
                }
        );
        reference.addActionListener(evt -> {
                    BaseReference frame = new Reference(aboutProgrammMenu);
                    frame.readInfoFromFile("Руководство пользователя.html");
                    frame.setVisible(true);
                }
        );

        JMenuItem animationMenu = new JMenuItem("Cкорость анимации");
        optionMenu.add(animationMenu);

        animationMenu.addActionListener(evt -> {
                    OptionFrame option = new OptionFrame(JGraphFrame.this, animationSpeed);
                    option.setVisible(true);
                    if (option.dialogResult()) {
                        animationSpeed = option.getAnimationSpeed();
                    }
                    option.dispose();
                }
        );
        this.setJMenuBar(menu);
    }

    private void addInfoComponents() {
        JLabel vertexNumLabel = new JLabel("Количество вершин:");
        vertexNumLabel.setFont(vertexNumLabel.getFont().deriveFont(Font.BOLD));
        JLabel edgeNumLabel = new JLabel("Количество ребер:");
        edgeNumLabel.setFont(edgeNumLabel.getFont().deriveFont(Font.BOLD));
        vertexNumText = new JLabel();
        vertexNumText.setBorder(new EmptyBorder(0, 5, 0, 15));
        edgeNumText = new JLabel();
        edgeNumText.setBorder(new EmptyBorder(0, 5, 0, 15));
        JLabel graphViewLabel = new JLabel("Тип представления:");
        graphViewLabel.setFont(graphViewLabel.getFont().deriveFont(Font.BOLD));
        JLabel graphDirectedLabel = new JLabel("Тип графа:");
        graphDirectedLabel.setFont(graphDirectedLabel.getFont().deriveFont(Font.BOLD));
        graphViewText = new JLabel();
        graphViewText.setBorder(new EmptyBorder(0, 5, 0, 15));
        graphDirectedText = new JLabel();
        graphDirectedText.setBorder(new EmptyBorder(0, 5, 0, 15));
        infoPanel.add(vertexNumLabel);
        infoPanel.add(vertexNumText);
        infoPanel.add(edgeNumLabel);
        infoPanel.add(edgeNumText);
        infoPanel.add(graphDirectedLabel);
        infoPanel.add(graphDirectedText);
        infoPanel.add(graphViewLabel);
        infoPanel.add(graphViewText);
    }

    private void addOperationButtons() {
        JButton addVertex = new JButton("Вставка вершины");
        addVertex.setIcon(IconFontSwing.buildIcon(FontAwesome.CIRCLE_O, OPERATIONS_ICON_SIZE));
        JButton removeVertex = new JButton("Удаление вершины");
        removeVertex.setIcon(IconFontSwing.buildIcon(FontAwesome.MINUS_CIRCLE, OPERATIONS_ICON_SIZE));
        JButton addEdge = new JButton("Вставка ребра");
        addEdge.setIcon(IconFontSwing.buildIcon(FontAwesome.LONG_ARROW_RIGHT, OPERATIONS_ICON_SIZE));
        JButton removeEdge = new JButton("Удаление ребра");
        removeEdge.setIcon(IconFontSwing.buildIcon(FontAwesome.MINUS, OPERATIONS_ICON_SIZE));
        JButton updateEdge = new JButton("Изменение веса ребра");
        updateEdge.setIcon(IconFontSwing.buildIcon(FontAwesome.REFRESH, OPERATIONS_ICON_SIZE));
        JButton clearEdges = new JButton("Удаление всех ребер");
        clearEdges.setIcon(IconFontSwing.buildIcon(FontAwesome.MINUS_SQUARE, OPERATIONS_ICON_SIZE));
        JButton clearGraph = new JButton("Очистка графа");
        clearGraph.setIcon(IconFontSwing.buildIcon(FontAwesome.TRASH_O, OPERATIONS_ICON_SIZE));
        JButton moveGraph = new JButton("Видоизменение граф");
        moveGraph.setIcon(IconFontSwing.buildIcon(FontAwesome.ARROWS, OPERATIONS_ICON_SIZE));
        JButton removeOutEdges = new JButton("Удаление исходящих ребер");
        JButton removeInEdges = new JButton("Удаление входящих ребер");
        JButton transformGraph = new JButton("Преобразование графа");

        setVertexNameCheckBox = new JCheckBox("Задавать имена вершин");
        setEdgeWeightCheckBox = new JCheckBox("Задавать веса ребер");

        JLabel verticesActionsLabel = new JLabel("Действия с вершинами:");
        verticesActionsLabel.setFont(verticesActionsLabel.getFont().deriveFont(Font.BOLD));

        JLabel edgesActionsLabel = new JLabel("Действия с ребрами:");
        edgesActionsLabel.setFont(verticesActionsLabel.getFont().deriveFont(Font.BOLD));

        JLabel otherActionsLabel = new JLabel("Другие действия:");
        otherActionsLabel.setFont(verticesActionsLabel.getFont().deriveFont(Font.BOLD));

        buttonsPanel.add(verticesActionsLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 2), 0, 0));
        buttonsPanel.add(addVertex, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        buttonsPanel.add(removeVertex, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 15, 5), 0, 0));

        buttonsPanel.add(edgesActionsLabel, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        buttonsPanel.add(addEdge, new GridBagConstraints(0, 4, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(removeEdge, new GridBagConstraints(0, 5, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(updateEdge, new GridBagConstraints(0, 6, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 15, 5), 0, 0));

        buttonsPanel.add(otherActionsLabel, new GridBagConstraints(0, 7, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 2), 0, 0));
        buttonsPanel.add(clearEdges, new GridBagConstraints(0, 8, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(clearGraph, new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(moveGraph, new GridBagConstraints(0, 10, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(removeOutEdges, new GridBagConstraints(0, 11, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(removeInEdges, new GridBagConstraints(0, 12, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonsPanel.add(transformGraph, new GridBagConstraints(0, 13, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 15, 5), 0, 0));

        buttonsPanel.add(setEdgeWeightCheckBox, new GridBagConstraints(0, 14, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 2), 0, 0));
        buttonsPanel.add(setVertexNameCheckBox, new GridBagConstraints(0, 16, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));

        addVertex.addActionListener(evt -> {
                    graphPanel.startAddVertex(setVertexNameCheckBox.isSelected());
                    commentTxt.setText("Щелкните по полю левой кнопкой мыши для того, "
                            + "чтобы добавить вершину.");
                }
        );
        removeVertex.addActionListener(evt -> {
                    graphPanel.startRemoveVertex();
                    commentTxt.setText("Щелкните левой кнопкой мыши по вершине, которую хотите удалить.");

                }
        );
        addEdge.addActionListener(evt -> {
                    graphPanel.startAddEdge(setEdgeWeightCheckBox.isSelected());
                    commentTxt.setText("Соедините две вершины на поле, для того чтобы добавить ребро.");
                }
        );
        removeEdge.addActionListener(evt -> {
                    graphPanel.startRemoveEdge();
                    commentTxt.setText("Выберите левой кнопкой мыши вершины, "
                            + "для того, чтобы удалить ребро между ними.");

                }
        );
        updateEdge.addActionListener(evt -> {
                    graphPanel.startUpdateEdge();
                    commentTxt.setText("Выберите левой кнопкой мыши вершины, "
                            + "для того, чтобы изменить вес ребра между ними.");

                }
        );
        clearEdges.addActionListener(evt -> {
                    graphPanel.clearEdges();
                    setNumbers();
                    cancelAllOperationsWithGraph();

                }
        );
        clearGraph.addActionListener(evt -> {
                    graphPanel.clearGraph();
                    setNumbers();
                    setAlgorithmsEnabledFlag();
                    cancelAllOperationsWithGraph();

                }
        );
        moveGraph.addActionListener(evt -> {
                    graphPanel.startMoveGraph();
                    commentTxt.setText("Начните передвигать левой кнопкой мыши любую вершину на поле.");

                }
        );
        removeOutEdges.addActionListener(evt -> {
                    graphPanel.startRemoveOutEdges();
                    commentTxt.setText("Щелкните левой кнопкой мыши по вершине,"
                            + " для того, чтобы удалить исходяшие ребра.");

                }
        );
        removeInEdges.addActionListener(evt -> {
                    graphPanel.startRemoveInEdges();
                    commentTxt.setText("Щелкните левой кнопкой мыши по вершине,"
                            + " для того, чтобы удалить входящие ребра.");

                }
        );

        transformGraph.addActionListener(evt -> {
                    String[] items = {GraphView.MATRIX_GRAPH.getText(),
                            GraphView.HASH_SET_GRAPH.getText(),
                            GraphView.TREE_SET_GRAPH.getText(),
                            GraphView.LIST_SET_GRAPH.getText(),
                            GraphView.SORTED_SET_GRAPH.getText()};
                    String result = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                            "Типа представления:", "Преобразование графа",
                            JOptionPane.INFORMATION_MESSAGE, null, items,
                            graphPanel.graphView().getText());

                    for (GraphView newView : GraphView.values()) {
                        if (newView.getText().equals(result)) {
                            graphPanel.transform(newView);
                            graphPanel.repaint();
                            setInfo();
                            return;
                        }
                    }
                }
        );
    }

    private void createGUI() {
        this.setSize(1000, 700);
        this.setTitle(TITLE);
        this.createMenu();
        this.setLayout(new GridBagLayout());
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        workPanel = new JPanel(new GridBagLayout());

        commentTxt = new JTextArea(1, 80);
        commentTxt.setEditable(false);
        commentTxt.setFont(new Font("Arial", Font.BOLD, 12));
        commentTxt.setForeground(Color.BLUE);
        commentTxt.setWrapStyleWord(true);
        commentTxt.setLineWrap(true);

        commentScrollPanel = new JScrollPane(commentTxt);
        commentScrollPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));

        algorithmsStepPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        algorithmsStepPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        algorithmsStepPanel.setMinimumSize(new Dimension(ALGORITHMS_STEP_PANEL_SIZE, ALGORITHMS_STEP_PANEL_SIZE));
        algorithmsStepPanel.setPreferredSize(new Dimension(ALGORITHMS_STEP_PANEL_SIZE, ALGORITHMS_STEP_PANEL_SIZE));

        mainPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 2, 2, 2), 0, 0));
        mainPanel.add(workPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
        mainPanel.add(commentScrollPanel, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
        mainPanel.add(algorithmsStepPanel, new GridBagConstraints(0, 3, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));

        operationPanel = new JPanel(new GridBagLayout());
        operationPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        graphPanel = new JGraphDrawer();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        workPanel.add(operationPanel, new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        workPanel.add(graphPanel, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));

        buttonsPanel = new JPanel(new GridBagLayout());
        accessuryPanel = new JPanel(new GridBagLayout());

        operationPanel.add(buttonsPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(accessuryPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));

        setColor();
        setComponentListener();
        setWindowListener();
        addInfoComponents();
        addOperationButtons();
        setInfo();
        setAlgorithmsEnabledFlag();

        topologicalSort();
        connectedComponents();
        mstSearch();
        shortestPaths();
        allShortestPaths();
        transitiveClosure();
        graphProperties();
        eulerTour();
        gamiltonTour();
        bfsVisit();
        dfsVisit();
        allPathsSearch();
        mstClustering();
        bridgesSearch();
        addGraphDrawerListeners();
        addResizeListener();
        this.setLocationRelativeTo(null);
    }

    private Graph<Vertex, Edge2D> createClone() {
        return graph().copy();
    }

    private void createResultFrame(String result) {
        SimpleResultsPanel resultsPanel = new SimpleResultsPanel(JGraphFrame.this, result);
        createAndShowResultsPanel(resultsPanel);
        cancelAllOperationsWithGraph();
    }

    private void addGraphDrawerListeners() {
        graphPanel.setVertexErrorListener(e -> {
            setEnabledForOperations(!Boolean.parseBoolean(e.getActionCommand()));
        });
        graphPanel.setUpdateGraphListener(e -> {
            vertexNumText.setText(String.valueOf(graph().verticesNum()));
            edgeNumText.setText(String.valueOf(graph().edgesNum()));
            setAlgorithmsEnabledFlag();
        });
    }

    private void showEdges(final Collection<Edge2D> tree, String title) {
        setEnabledForOperations(false);
        JButton showTree = createButton(title);
        JButton exit = createButton("Выход");
        algorithmsStepPanel.removeAll();
        algorithmsStepPanel.add(showTree, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(exit, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.revalidate();

        showTree.addActionListener(evt -> {
            for (Edge2D e : tree) {
                e.color = Color.GREEN;
                e.dimension = 3;
            }
            graphPanel.repaint();
        });
        exit.addActionListener(evt -> {
            for (Edge2D e : tree) {
                e.color = Edge2D.DARK_GRAY;
                e.dimension = Edge2D.default_dimension;
            }
            graphPanel.repaint();
            algorithmsStepPanel.removeAll();
            algorithmsStepPanel.repaint();
            setEnabledForOperations(true);
        });
    }

    private void bridgesSearch() {
        JMenuItem bridgesSearchMenu = new JMenuItem("Поиск всех мостов");
        undirectedGraphAlgorithms.add(bridgesSearchMenu);
        bridgesSearchMenu.addActionListener(evt -> {
                    BridgesSearch<Vertex, Edge2D> bs = new BridgesSearch<>(graph());
                    if (bs.connected()) {
                        StringBuilder result
                                = new StringBuilder("Количество ребер-мостов: " + bs.bridges().size()
                                + SEPARATOR + "Ребра мосты: " + SEPARATOR);
                        for (Edge2D e : bs.bridges()) {
                            result.append(e.print()).append(SEPARATOR);
                        }
                        createResultFrame(result.toString());
                        showEdges(bs.bridges(), "Отобразить мосты");
                    } else {
                        popupService.showErrorPopup("Граф должен быть связным!", JGraphFrame.this);
                    }
                }
        );
    }

    private void topologicalSort() {
        JMenuItem topoSortMenu = new JMenuItem("Топологическая сортировка");
        directedGraphAlgorithms.add(topoSortMenu);
        topoSortMenu.addActionListener(evt -> {
                    TopologicalSort<Vertex> topoSort = new TopologicalSort<>(graph());
                    if (topoSort.acyclic()) {
                        StringBuilder result
                                = new StringBuilder("Последовательность вершин топологической сортировки:"
                                + SEPARATOR);
                        result.append(topoSort.sequence());
                        createResultFrame(result.toString());
                    } else {
                        popupService.showErrorPopup("Граф должен быть ациклическим!",
                                JGraphFrame.this);
                    }
                }
        );
    }

    private void showConnectedComponents(AbstractConnectedComponents<Vertex> connComp,
                                         Component component) {
        StringBuilder result
                = new StringBuilder("Количество связных компонет: "
                + connComp.componentsNum() + SEPARATOR
                + "Связные компонеты графа:" + SEPARATOR);
        result.append(connComp);
        createResultFrame(result.toString());
    }

    private void connectedComponents() {
        JMenuItem connectedComponentsMenu = new JMenuItem("Связные компоненты");
        JMenuItem stronglyConnectedComponentsMenu
                = new JMenuItem("Сильно связные компоненты");
        undirectedGraphAlgorithms.add(connectedComponentsMenu);
        directedGraphAlgorithms.add(stronglyConnectedComponentsMenu);
        connectedComponentsMenu.addActionListener(evt ->
                showConnectedComponents(new ConnectedComponents<>(graph()), connectedComponentsMenu)
        );
        stronglyConnectedComponentsMenu.addActionListener(evt ->
                showConnectedComponents(new StronglyConnectedComponents<>(graph()), stronglyConnectedComponentsMenu)
        );
    }

    private Vertex firstVertex() {
        Iterator<Vertex> v = graph().iterator();
        if (v.hasNext()) {
            return v.next();
        }
        return null;
    }

    private void createMst(MstType type, Component component, String title) {
        ConnectedComponents<Vertex> connComp = new ConnectedComponents<>(graph());
        if (connComp.connected()) {
            if (!NumberParser.isNegativeWeights(graph())) {
                MinimumSpanningTree<Edge2D> mst = null;
                switch (type) {
                    case Prim:
                        mst = new PrimMinimumSpanningTree<>(graph(), firstVertex());
                        break;

                    case Kruskal:
                        mst = new KruskalMinimumSpanningTree<>(graph());
                        break;
                }
                if (mst != null) {
                    StringBuilder result = new StringBuilder(title
                            + SEPARATOR + "Вес остова: " + mst.getMinimumSpanningTreeWeight()
                            + SEPARATOR + "Ребра остова:" + SEPARATOR);
                    final Collection<Edge2D> tree = mst.getMinimumSpanningTreeEdges();
                    for (Edge2D e : tree) {
                        result.append(e.print()).append(SEPARATOR);
                    }
                    createResultFrame(result.toString());
                    showEdges(tree, "Отобразить дерево");
                }
            } else {
                popupService.showErrorPopup("Весовая функция должна быть положительной!",
                        JGraphFrame.this);
            }
        } else {
            popupService.showErrorPopup("Граф должен быть связным!", JGraphFrame.this);
        }
    }

    private void mstSearch() {
        JMenu mstMenu = new JMenu("Построение минимального остовного дерева");
        JMenuItem primMstMenu = new JMenuItem("Алгоритм Прима");
        JMenuItem kruskalMstMenu = new JMenuItem("Алгоритм Крускала");
        mstMenu.add(primMstMenu);
        mstMenu.add(kruskalMstMenu);
        undirectedGraphAlgorithms.add(mstMenu);
        primMstMenu.addActionListener(evt ->
                createMst(MstType.Prim, primMstMenu, "Результаты алгоритма Прима:")
        );
        kruskalMstMenu.addActionListener(
                evt -> createMst(MstType.Kruskal, kruskalMstMenu, "Результаты алгоритма Крускала:")
        );
    }

    private void allShortestPaths() {
        JMenu allSptMenu = new JMenu("Кратчайшие пути между всеми парами вершин");
        directedGraphAlgorithms.add(allSptMenu);
        JMenuItem allSptMenuJohnson = new JMenuItem("Алгоритм Джонсона");
        JMenuItem allSptMenuFloydWarshall = new JMenuItem("Алгоритм Флойда-Варшалла");
        allSptMenu.add(allSptMenuJohnson);
        allSptMenu.add(allSptMenuFloydWarshall);
        allSptMenuJohnson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!NumberParser.isNullWeights(graph())) {
                    Graph<Vertex, Edge2D> clone = createClone();
                    showAllSpt(new JohnsonAllPairsShortestPaths<>(clone, new Vertex("")),
                            "Результаты алгоритма Джонсона");
                } else {
                    popupService.showErrorPopup("Не все веса ребер заданы!", JGraphFrame.this);
                }
            }
        });
        allSptMenuFloydWarshall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                if (!NumberParser.isNullWeights(graph())) {
                    showAllSpt(new FloydWarshallAllPairsShortestPaths<>(graph()), "Результаты алгоритма Флойда");
                } else {
                    popupService.showErrorPopup("Не все веса ребер заданы!", JGraphFrame.this);
                }
            }
        });

    }

    private Pair<String, String> createAllSptDecision(AllPairsShortestPaths<Vertex, Edge2D> allSpt) {
        StringBuilder dist
                = new StringBuilder("Кратчайшие расстояния между вершинами:" + SEPARATOR);
        StringBuilder paths
                = new StringBuilder("Структуры путей:" + SEPARATOR);
        for (Vertex u : graph()) {
            for (Vertex v : graph()) {
                if (allSpt.isPath(u, v)) {
                    dist.append("w[p(").append(u).append(",")
                            .append(v).append(")] = ").append(allSpt.getDistance(u, v))
                            .append(SEPARATOR);
                    paths.append("p(").append(u).append(",").append(v)
                            .append(") = ").append(allSpt.getPath(u, v)).append(SEPARATOR);
                } else {
                    dist.append("Пути из ").append(u).append(" в ").append(v)
                            .append(" не существует!").append(SEPARATOR);
                    paths.append("Пути из ").append(u).append(" в ").append(v)
                            .append(" не существует!").append(SEPARATOR);
                }
            }
        }
        return new Pair<>(dist.toString(), paths.toString());
    }

    private void showAllPaths(final AllPairsShortestPaths<Vertex, Edge2D> allSpt) {
        JComboBox<String> source = new JComboBox<>();
        JComboBox<String> target = new JComboBox<>();
        for (Vertex u : graph()) {
            source.addItem(u.getName());
            target.addItem(u.getName());
        }
        JButton showPath = createButton("Показать");
        JButton exit = createButton("Выход");
        showPath.addActionListener(evt -> {
            Vertex u = graphPanel.vertex((String) source.getSelectedItem());
            Vertex v = graphPanel.vertex((String) target.getSelectedItem());
            if (allSpt.isPath(u, v)) {
                setDefaultColorForEdges();
                setDefaultColorForVerticesBorders();
                showPath.setEnabled(false);
                source.setEnabled(false);
                target.setEnabled(false);
                exit.setEnabled(false);
                graphPanel.repaint();
                PathDrawer drawer = new PathDrawer(allSpt.getPath(u, v));
                drawer.setFinishListener(e -> {
                    showPath.setEnabled(true);
                    source.setEnabled(true);
                    target.setEnabled(true);
                    exit.setEnabled(true);
                });
                drawer.start();
            } else {
                popupService.showErrorPopup("Пути из '%s' в '%s' не существует!".formatted(u, v),
                        JGraphFrame.this);
            }
        });
        addSptComponents(source, target, showPath, exit);
    }

    private void showAllSpt(final AllPairsShortestPaths<Vertex, Edge2D> allSpt, String title) {
        if (allSpt.decision()) {
            Pair<String, String> result = createAllSptDecision(allSpt);
            SptResultsPanel resultFrame
                    = new SptResultsPanel(JGraphFrame.this, title,
                    result.getKey(), result.getValue());
            createAndShowResultsPanel(resultFrame);
            showAllPaths(allSpt);
            cancelAllOperationsWithGraph();
        } else {
            popupService.showErrorPopup("Граф содержит цикл с отрицательным весом!",
                    JGraphFrame.this);
        }
    }

    /**
     *
     */
    private void transitiveClosure() {
        JMenuItem transitiveClosureMenu = new JMenuItem("Транзитивное замыкание");
        directedGraphAlgorithms.add(transitiveClosureMenu);
        transitiveClosureMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TransitiveClosure<Vertex, Edge2D> tc
                        = new TransitiveClosure<>(graph());
                StringBuilder result
                        = new StringBuilder("Результаты транзитивного замыкания:" + SEPARATOR);
                for (Vertex u : graph()) {
                    for (Vertex v : graph()) {
                        String isPath = tc.isPath(u, v)
                                ? " существует!" : " не существует!";
                        result.append("Путь из ").append(u).append(" в ")
                                .append(v).append(isPath).append(SEPARATOR);
                    }
                }
                createResultFrame(result.toString());
            }
        });
    }

    private void graphProperties() {
        JMenu graphPropertiesMenu = new JMenu("Метрические характеристики графа");
        directedGraphAlgorithms.add(graphPropertiesMenu);
        JMenuItem graphRadiusMenu = new JMenuItem("Радиус графа");
        JMenuItem graphDiameterMenu = new JMenuItem("Диаметр графа");
        JMenuItem eccentricityMenu = new JMenuItem("Эксцентриситет вершины");
        graphPropertiesMenu.add(graphRadiusMenu);
        graphPropertiesMenu.add(graphDiameterMenu);
        graphPropertiesMenu.add(eccentricityMenu);
        graphRadiusMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                if (!NumberParser.isNullWeights(graph())) {
                    FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> allSpt
                            = new FloydWarshallAllPairsShortestPaths<>(graph());
                    if (allSpt.decision()) {
                        String result = "Радиус графа:" + SEPARATOR + "r(G) = "
                                + GraphMetricProperties.radius(allSpt);
                        createResultFrame(result);
                    } else {
                        popupService.showErrorPopup("Граф содержит цикл с отрицательным весом!",
                                JGraphFrame.this);
                    }
                } else {
                    popupService.showErrorPopup("Не все веса ребер заданы!", JGraphFrame.this);
                }
            }
        });
        graphDiameterMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                if (!NumberParser.isNullWeights(graph())) {
                    FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> allSpt
                            = new FloydWarshallAllPairsShortestPaths<>(graph());
                    if (allSpt.decision()) {
                        String result = "Диаметр графа:" + SEPARATOR + "d(G) = "
                                + GraphMetricProperties.diametr(allSpt);
                        createResultFrame(result);
                    } else {
                        popupService.showErrorPopup("Граф содержит цикл с отрицательным весом!", JGraphFrame.this);
                    }
                } else {
                    popupService.showErrorPopup("Не все веса ребер заданы!", JGraphFrame.this);
                }
            }
        });
        eccentricityMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                String v = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                        "Вершина:",
                        "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                        getVertices(), null);
                if (v != null) {
                    Vertex s = graphPanel.vertex(v);
                    if (s == null) {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Такой вершины не существует!",
                                null, JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (!NumberParser.isNegativeWeights(graph())) {
                        String result = "Эксцентриситет вершины:" + SEPARATOR
                                + "e(" + s + ") = "
                                + GraphMetricProperties.eccentricity(
                                new DijkstraShortestPaths<>(graph(), s));
                        createResultFrame(result);
                    } else {
                        popupService.showErrorPopup("Весовая функция должна быть положительной!",
                                JGraphFrame.this);
                    }
                }
            }
        });
    }

    private String[] getVertices() {
        List<String> vertices = new ArrayList<>();
        for (Vertex vertex : graph()) {
            vertices.add(vertex.getName());
        }
        return vertices.toArray(new String[0]);
    }

    private void setDefaultColorForEdges() {
        Iterator<Edge2D> edge = graph().edgeIterator();
        while (edge.hasNext()) {
            Edge2D e = edge.next();
            e.color = Edge2D.DARK_GRAY;
            e.dimension = Edge2D.default_dimension;
        }
    }

    private void setDefaultColorForVerticesBorders() {
        for (Vertex u : graph()) {
            u.borderColor = Vertex.default_border_color;
        }
    }

    private void clearGraphPanel() {
        setDefaultColorForEdges();
        setDefaultColorForVerticesBorders();
        graphPanel.repaint();
        algorithmsStepPanel.removeAll();
        algorithmsStepPanel.repaint();
        setEnabledForOperations(true);
    }

    private void createAndShowResultsPanel(AlgorithmsResultsPanel algorithmsResultsPanel) {
        hideCurrentResultsPanel();
        this.algorithmsResultsPanel = algorithmsResultsPanel;
        this.algorithmsResultsPanel.initialize();
        this.algorithmsResultsPanel.showPanel();
    }

    private void hideCurrentResultsPanel() {
        Optional.ofNullable(this.algorithmsResultsPanel).ifPresent(AlgorithmsResultsPanel::hidePanel);
        this.algorithmsResultsPanel = null;
    }

    private void addResizeListener() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Optional.ofNullable(algorithmsResultsPanel).ifPresent(panel -> {
                    panel.hidePanel();
                    panel.showPanel();
                });
            }
        });
    }

    private void searchTour(TourType type, Component component,
                            String title, String error) {
        ConnectedComponents<Vertex> connComp
                = new ConnectedComponents<>(graph());
        if (connComp.connected()) {
            AbstractTourSearch<Vertex, Edge2D> tourSearch = null;
            switch (type) {
                case euler_tour:
                    Graph<Vertex, Edge2D> clone = createClone();
                    tourSearch = new EulerTourSearch<>(clone);
                    break;
                case gamilton_tour:
                    tourSearch
                            = new GamiltonTourSearch<>(graph());
                    break;
            }

            if (tourSearch != null && tourSearch.decision()) {
                Collection<Vertex> tour = tourSearch.tour();
                StringBuilder result = new StringBuilder(title);
                result.append(tour);
                createResultFrame(result.toString());
                showTour(tour);
            } else {
                popupService.showErrorPopup(error, JGraphFrame.this);
            }
        } else {
            popupService.showErrorPopup("Граф должен быть связным!",
                    JGraphFrame.this);
        }
    }

    private void showTour(final Collection<Vertex> tour) {
        setEnabledForOperations(false);
        JButton showTour = createButton("Отобразить цикл");
        JButton exit = createButton("Выход");
        algorithmsStepPanel.removeAll();
        algorithmsStepPanel.add(showTour, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(exit, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.revalidate();

        showTour.addActionListener(evt -> {
            setDefaultColorForEdges();
            setDefaultColorForVerticesBorders();
            showTour.setEnabled(false);
            exit.setEnabled(false);
            graphPanel.repaint();
            PathDrawer drawer = new PathDrawer(tour);
            drawer.setFinishListener(e -> {
                showTour.setEnabled(true);
                exit.setEnabled(true);
            });
            drawer.start();
        });
        exit.addActionListener(evt -> clearGraphPanel());
    }

    private void eulerTour() {
        JMenuItem eulerTourMenu = new JMenuItem("Поиск Эйлерова цикла");
        undirectedGraphAlgorithms.add(eulerTourMenu);
        eulerTourMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchTour(TourType.euler_tour, eulerTourMenu,
                        "Структура Эйлерова цикла:" + SEPARATOR, "Эйлерова цикла не существует!");
            }
        });
    }

    private void gamiltonTour() {
        JMenuItem gamiltonTourMenu = new JMenuItem("Поиск Гамильтонова цикла");
        undirectedGraphAlgorithms.add(gamiltonTourMenu);
        gamiltonTourMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchTour(TourType.gamilton_tour, gamiltonTourMenu,
                        "Структура Гамильтонова цикла:" + SEPARATOR, "Гамильтонова цикла не существует!");
            }
        });
    }

    private void addSptComponents(JComboBox<String> source,
                                  JComboBox<String> target,
                                  JButton showPath,
                                  JButton exit) {
        setEnabledForOperations(false);
        JLabel sptLabel = new JLabel("Кратчайший путь:");
        sptLabel.setFont(sptLabel.getFont().deriveFont(Font.BOLD));
        algorithmsStepPanel.removeAll();
        algorithmsStepPanel.add(sptLabel, new GridBagConstraints(0, 0, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 0, 2, 0), 0, 0));

        algorithmsStepPanel.add(new JLabel("из"), new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(source, new GridBagConstraints(1, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(new JLabel("в"), new GridBagConstraints(2, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(target, new GridBagConstraints(3, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(2, 0, 2, 0), 0, 0));

        algorithmsStepPanel.add(showPath, new GridBagConstraints(0, 2, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.add(exit, new GridBagConstraints(0, 3, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(2, 0, 2, 0), 0, 0));
        algorithmsStepPanel.revalidate();

        exit.addActionListener(evt -> clearGraphPanel());
    }

    private void showPaths(final GraphPaths<Vertex> spt) {
        JComboBox<String> source = new JComboBox<>();
        source.addItem(spt.getSource().toString());
        JComboBox<String> target = new JComboBox<>();
        for (Vertex u : graph()) {
            target.addItem(u.getName());
        }
        JButton showPath = createButton("Показать");
        JButton exit = createButton("Выход");
        showPath.addActionListener(evt -> {
            Vertex u = graphPanel.vertex((String) target.getSelectedItem());
            if (spt.isPath(u)) {
                setDefaultColorForEdges();
                setDefaultColorForVerticesBorders();
                showPath.setEnabled(false);
                source.setEnabled(false);
                target.setEnabled(false);
                exit.setEnabled(false);
                graphPanel.repaint();
                PathDrawer drawer = new PathDrawer(spt.getPath(u));
                drawer.setFinishListener(e -> {
                    showPath.setEnabled(true);
                    source.setEnabled(true);
                    target.setEnabled(true);
                    exit.setEnabled(true);
                });
                drawer.start();
            } else {
                popupService.showErrorPopup("Пути из '%s' в '%s' не существует!".formatted(spt.getSource(), u),
                        JGraphFrame.this);
            }
        });
        addSptComponents(source, target, showPath, exit);
    }

    private Pair<String, String> createSptDecision(GraphPaths<Vertex> spt) {
        StringBuilder dist
                = new StringBuilder("Кратчайшие расстояния между вершинами:" + SEPARATOR);
        StringBuilder paths
                = new StringBuilder("Структуры путей:" + SEPARATOR);
        for (Vertex v : graph()) {
            if (spt.isPath(v)) {
                dist.append("w[p(").append(spt.getSource()).append(",")
                        .append(v).append(")] = ").append(spt.getDistance(v))
                        .append(SEPARATOR);
                paths.append("p(").append(spt.getSource()).append(",")
                        .append(v).append(") = ").append(spt.getPath(v)).append(SEPARATOR);
            } else {
                dist.append("Пути из ").append(spt.getSource()).append(" в ")
                        .append(v).append(" не существует!").append(SEPARATOR);
                paths.append("Пути из ").append(spt.getSource()).append(" в ")
                        .append(v).append(" не существует!").append(SEPARATOR);
            }
        }
        return new Pair<>(dist.toString(), paths.toString());
    }

    private void sptDialog(SptAlgorithm algorithm) {

        if (!NumberParser.isNullWeights(graph())) {
            String v = (String) JOptionPane.showInputDialog(this,
                    "Вершина:",
                    "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                    getVertices(), null);
            if (v != null) {
                Vertex s = graphPanel.vertex(v);
                if (s == null) {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Такой вершины не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                switch (algorithm) {
                    case dijkstra:
                        sptSearch(new DijkstraShortestPaths<>(graph(), s),
                                "Результаты алгоритма Дейкстры", null);
                        break;
                    case bellman_ford:
                        sptSearch(new BellmanFordShortestPaths<>(graph(), s),
                                "Результаты алгоритма Беллмана-Форда",
                                "Граф содержит цикл с отрицательным весом!");
                        break;
                    case dag:
                        sptSearch(new DAGShortestPaths<>(graph(), s),
                                "Кратчайшие пути в DAG",
                                "Граф должен быть ациклическим!");
                        break;
                }
            }
        } else {
            popupService.showErrorPopup("Не все веса ребер заданы!",
                    JGraphFrame.this);
        }
    }

    private void sptSearch(ShortestPaths<Vertex, Edge2D> spt, String title, String error) {
        if (spt.decision()) {
            Pair<String, String> result = createSptDecision(spt);
            SptResultsPanel resultFrame
                    = new SptResultsPanel(JGraphFrame.this, title,
                    result.getKey(), result.getValue());
            createAndShowResultsPanel(resultFrame);
            showPaths(spt);
            cancelAllOperationsWithGraph();
        } else {
            popupService.showErrorPopup(error, JGraphFrame.this);
        }
    }

    private void shortestPaths() {
        JMenu sptMenu = new JMenu("Кратчайшие пути из одной вершины");
        directedGraphAlgorithms.add(sptMenu);
        JMenuItem dijkstraSptMenu = new JMenuItem("Алгоритм Дейкстры");
        JMenuItem bellmanFordSptMenu = new JMenuItem("Алгоритм Беллмана-Форда");
        JMenuItem dagSptMenu = new JMenuItem("Кратчайшие пути в DAG");
        sptMenu.add(dijkstraSptMenu);
        sptMenu.add(bellmanFordSptMenu);
        sptMenu.add(dagSptMenu);

        dijkstraSptMenu.addActionListener(evt -> sptDialog(SptAlgorithm.dijkstra));

        bellmanFordSptMenu.addActionListener(evt -> sptDialog(SptAlgorithm.bellman_ford));

        dagSptMenu.addActionListener(evt -> sptDialog(SptAlgorithm.dag));
    }

    private void bfsVisit() {
        JMenuItem bfsMenu = new JMenuItem("Кратчайшие пути (BFS)");
        algoritmsMenu.add(bfsMenu);
        bfsMenu.addActionListener(evt -> {

            String v = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                    "Вершина:",
                    "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                    getVertices(), null);
            if (v != null) {
                Vertex s = graphPanel.vertex(v);
                if (s == null) {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Такой вершины не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                BreadFirstSearch<Vertex> bfs
                        = new BreadFirstSearch<>(graph(), s);
                Pair<String, String> result = createSptDecision(bfs);
                SptResultsPanel sptResultsPanel
                        = new SptResultsPanel(JGraphFrame.this, "Кратчайшик пути (BFS)",
                        result.getKey(), result.getValue());
                createAndShowResultsPanel(sptResultsPanel);
                showPaths(bfs);
                cancelAllOperationsWithGraph();
            }
        });
    }

    private void dfsVisit() {
        JMenuItem dfsMenu = new JMenuItem("Обход в ширину (DFS)");
        algoritmsMenu.add(dfsMenu);
        dfsMenu.addActionListener(evt -> {
            DepthFirstSearch<Vertex> dfs = new DepthFirstSearch<>(graph());
            StringBuilder paths = new StringBuilder("Структуры путей:" + SEPARATOR);
            StringBuilder discovery = new StringBuilder("Метки обнаружения" + SEPARATOR);
            StringBuilder finishing = new StringBuilder("Метки завершения:" + SEPARATOR);
            for (Vertex v : graph()) {
                paths.append("p[").append(v).append("] = ")
                        .append(dfs.getPath(v)).append(SEPARATOR);
                discovery.append("d[").append(v).append("] = ")
                        .append(dfs.discoveryTime(v)).append(SEPARATOR);
                finishing.append("f[").append(v).append("] = ")
                        .append(dfs.finishingTime(v)).append(SEPARATOR);
            }
            DfsResultsPanel dfsResultsPanel = new DfsResultsPanel(JGraphFrame.this,
                    paths.toString(),
                    discovery.toString(),
                    finishing.toString()
            );
            createAndShowResultsPanel(dfsResultsPanel);
            cancelAllOperationsWithGraph();

        });
    }

    private void allPathsSearch() {
        JMenuItem allPathsMenu = new JMenuItem("Поиск всех путей");
        algoritmsMenu.add(allPathsMenu);
        allPathsMenu.addActionListener(evt -> {
            VertexInputDialog dialog = new VertexInputDialog(JGraphFrame.this, getVertices());
            dialog.setVisible(true);
            if (dialog.dialogResult()) {
                Vertex u = graphPanel.vertex(dialog.source());
                Vertex v = graphPanel.vertex(dialog.target());
                if (u == null || v == null) {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Таких вершин не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                AllPathsSearch<Vertex> allPaths = new AllPathsSearch<Vertex>(graph(), u, v);
                StringBuilder result
                        = new StringBuilder("Количество путей: " + allPaths.size() + SEPARATOR
                        + "Структуры путей:" + SEPARATOR);
                result.append(allPaths);
                createResultFrame(result.toString());
            }
            dialog.dispose();
        });
    }

    private void mstClustering() {
        JMenuItem mstClusteringMenu = new JMenuItem("Кластеризация "
                + "(Алгоритм минимального покрывающего дерева)");
        algoritmsMenu.add(mstClusteringMenu);
        mstClusteringMenu.addActionListener(evt -> {
            ClusteringAlgorithmInputDialog dialog = new ClusteringAlgorithmInputDialog(JGraphFrame.this);
            dialog.setVisible(true);
            if (dialog.dialogResult()) {
                int n = dialog.number();
                String file = dialog.matrixFile();
                MatrixParser parser = new MatrixParser();
                try {
                    double[][] distances = parser.read(file);
                    MinimumSpanningTreeClustering clustering
                            = new MinimumSpanningTreeClustering(n, distances);
                    StringBuilder result
                            = new StringBuilder("Количество кластеров: " + clustering.clustersSize()
                            + SEPARATOR
                            + "Структуры кластеров:" + SEPARATOR);
                    result.append(clustering);
                    createResultFrame(result.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, ex,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
            dialog.dispose();

        });

    }

}
