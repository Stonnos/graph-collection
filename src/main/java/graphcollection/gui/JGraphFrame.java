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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;

/**
 *
 * @author Рома
 */
public class JGraphFrame extends JFrame {

    //-------------------------------------------------------------------
    private static final String title = "Приложение для работы с графом";
    private static final String separator = System.getProperty("line.separator");
    private static final Color mainColor = Color.CYAN;
    private int animationSpeed = 2000;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel workPanel;
    private JPanel operationPanel;
    private JPanel graphInfoPanel;
    private JPanel buttonsPanel;
    private JPanel accessuryPanel;
    private JGraphDrawer graphPanel;
    private JScrollPane commentScrollPanel;
    private JTextArea commentTxt;
    private JPanel lowPanel;
    //------------------------------------------------------
    private JTextField graphViewText;
    private JTextField graphDirectedText;
    private JTextField vertexNumText;
    private JTextField edgeNumText;
    private InputComponent input;
    //----------------------------------------------------
    private JMenu graphMenu;
    private JMenu algoritmsMenu;
    private JMenu directedGraphAlgorithms;
    private JMenu undirectedGraphAlgorithms;

    //-----------------------------------------------------

    /**
     *
     */
    private class InputComponent {

        public JLabel inputLabel = new JLabel("Имя вершины:");
        public final JTextField inputText = new JTextField(8);
        public final JButton okButton = new JButton("OK");
        public final JButton cancelButton = new JButton("Отмена");
        public boolean mode;

        public InputComponent() {
            this.setEnabled(false);
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    inputText.setText("");
                }
            });
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String text = inputText.getText();
                    if (mode) {
                        if (text.isEmpty()) {
                            graphPanel.setNextVertex(null);
                        } else if (VertexFormat.isFormat(text)) {
                            graphPanel.setNextVertex(text);
                        } else {
                            JOptionPane.showMessageDialog(JGraphFrame.this,
                                    "Недопустимое имя вершины!", "Ошибка ввода",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        if (text.isEmpty()) {
                            graphPanel.setNextWeight(null);
                        } else {
                            Number w = NumberParser.parse(inputText.getText());
                            if (w == null) {
                                JOptionPane.showMessageDialog(JGraphFrame.this,
                                        "Вес ребра должен быть числовой!", "Ошибка ввода",
                                        JOptionPane.WARNING_MESSAGE);
                            } else {
                                graphPanel.setNextWeight(w);
                            }
                        }
                    }
                    inputText.setText("");
                    inputText.requestFocusInWindow();
                }
            });
        }

        public final void setLabelText(String text) {
            inputLabel.setText(text);
        }

        public final void setEnabled(boolean aFlag) {
            inputLabel.setEnabled(aFlag);
            inputText.setEnabled(aFlag);
            okButton.setEnabled(aFlag);
            cancelButton.setEnabled(aFlag);
        }

        public final void setMode(boolean mode) {
            this.mode = mode;
            this.setEnabled(true);
            inputText.requestFocusInWindow();
            this.setLabelText(mode ? "Имя вершины:" : "Вес ребра:");
        }

        public final boolean mode() {
            return mode;
        }

    } //End of class InputComponent

    private class PathDrawer extends TimerTask {

        private final java.util.Timer timer = new java.util.Timer();
        private final Collection<Vertex> path;
        private Iterator<Vertex> vertex;
        private Vertex s;

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
            } else {
                timer.cancel();
                JGraphFrame.this.setEnabled(true);
            }
        }

        public void start() {
            JGraphFrame.this.setEnabled(false);
            vertex = path.iterator();
            s = vertex.next();
            s.borderColor = Color.RED;
            timer.scheduleAtFixedRate(this, animationSpeed, animationSpeed);
        }
    } //End of class PathDrawer

    public JGraphFrame() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        makeGUI();
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
        this.mainPanel.setBackground(mainColor);
        this.infoPanel.setBackground(mainColor);
        this.buttonsPanel.setBackground(mainColor);
        this.operationPanel.setBackground(mainColor);
        this.lowPanel.setBackground(mainColor);
        this.workPanel.setBackground(mainColor);
        this.graphInfoPanel.setBackground(mainColor);
        this.accessuryPanel.setBackground(mainColor);
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
        CancelAllOperationsWithGraph();
    }

    private void setAlgorithmsEnabled() {
        if (graph().direction()) {
            undirectedGraphAlgorithms.setEnabled(false);
            directedGraphAlgorithms.setEnabled(true);
        } else {
            directedGraphAlgorithms.setEnabled(false);
            undirectedGraphAlgorithms.setEnabled(true);
        }
    }

    private void CancelAllOperationsWithGraph() {
        graphPanel.end();
        commentTxt.setText("Выберите операцию.");
    }

    private void addInputComponents() {
        input = new InputComponent();
        lowPanel.add(input.inputLabel);
        lowPanel.add(input.inputText);
        lowPanel.add(input.okButton);
        lowPanel.add(input.cancelButton);
    }

    private void createGraph(GraphView view, boolean direction) {
        input.setEnabled(false);
        graphPanel.create(view, direction);
        graphPanel.repaint();
        setInfo();
        setAlgorithmsEnabled();
    }

    private String graphBDDialog(String title) {
        String name = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                "Имя графа:",
                title, JOptionPane.INFORMATION_MESSAGE, null,
                null, null);
        if (name != null && name.equals("")) {
            JOptionPane.showMessageDialog(JGraphFrame.this,
                    "Граф не задан!",
                    null, JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return name;
    }

    private void createMenu() {
        JMenuBar menu = new JMenuBar();
        graphMenu = new JMenu("Граф");
        algoritmsMenu = new JMenu("Алгоритмы");
        JMenu referenceMenu = new JMenu("Справка");
        JMenu optionMenu = new JMenu("Настройки");
        menu.add(graphMenu);
        menu.add(algoritmsMenu);
        menu.add(optionMenu);
        menu.add(referenceMenu);
        //-----------------------------------------------
        JMenu create = new JMenu("Создать граф");
        JMenuItem generate = new JMenuItem("Создать случайный граф");
        JMenuItem open = new JMenuItem("Загрузить из файла");
        JMenuItem save = new JMenuItem("Сохранить в файл");
        JMenuItem saveImage = new JMenuItem("Сохранить изображение");
        graphMenu.add(create);
        graphMenu.add(generate);
        graphMenu.addSeparator();
        graphMenu.add(open);
        graphMenu.add(save);
        graphMenu.add(saveImage);
        graphMenu.addSeparator();
        //-----------------------------------------------
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                try {
                    GraphFileChooser fileChooser = new GraphFileChooser();
                    File file = fileChooser.openFile(JGraphFrame.this);
                    if (file != null) {
                        graphPanel.read(file.getPath());
                        setInfo();
                        setAlgorithmsEnabled();
                    }
                } catch (InternalError | NumberFormatException e) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, e,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //---------------------------------------------------------
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                try {
                    GraphFileChooser fileChooser = new GraphFileChooser();
                    File file = fileChooser.saveFile(JGraphFrame.this);
                    if (file != null) {
                        GraphParser writer = new GraphParser();
                        writer.write(graph(), file.getPath());
                    }
                } catch (InternalError e) {
                    JOptionPane.showMessageDialog(JGraphFrame.this, e,
                            null, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //---------------------------------------------------------
        saveImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                try {
                    GraphFileChooser fileChooser = new GraphFileChooser();
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
        //-----------------------------------------------
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
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
                        setAlgorithmsEnabled();
                        graphPanel.repaint();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(JGraphFrame.this, e,
                                "", JOptionPane.WARNING_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        });
        //-----------------------------------------------
        JMenu directedGraph = new JMenu("Ориентированный");
        JMenu undirectedGraph = new JMenu("Неориентированный");
        create.add(directedGraph);
        create.add(undirectedGraph);
        JMenuItem directedMatrixGraph = new JMenuItem(GraphView.Matrix_Graph.getText());
        JMenuItem directedHashSGraph = new JMenuItem(GraphView.Hash_Set_Graph.getText());
        JMenuItem directedTreeSGraph = new JMenuItem(GraphView.Tree_Set_Graph.getText());
        JMenuItem directedListSGraph = new JMenuItem(GraphView.List_Set_Graph.getText());
        JMenuItem directedSortedSGraph = new JMenuItem(GraphView.Sorted_Set_Graph.getText());
        directedGraph.add(directedMatrixGraph);
        directedGraph.add(directedHashSGraph);
        directedGraph.add(directedTreeSGraph);
        directedGraph.add(directedListSGraph);
        directedGraph.add(directedSortedSGraph);
        JMenuItem undirectedMatrixGraph = new JMenuItem(GraphView.Matrix_Graph.getText());
        JMenuItem undirectedHashSGraph = new JMenuItem(GraphView.Hash_Set_Graph.getText());
        JMenuItem undirectedTreeSGraph = new JMenuItem(GraphView.Tree_Set_Graph.getText());
        JMenuItem undirectedListSGraph = new JMenuItem(GraphView.List_Set_Graph.getText());
        JMenuItem undirectedSortedSGraph = new JMenuItem(GraphView.Sorted_Set_Graph.getText());
        undirectedGraph.add(undirectedMatrixGraph);
        undirectedGraph.add(undirectedHashSGraph);
        undirectedGraph.add(undirectedTreeSGraph);
        undirectedGraph.add(undirectedListSGraph);
        undirectedGraph.add(undirectedSortedSGraph);
        //-------------------------------------------------
        //-------------------------------------------------
        directedMatrixGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Matrix_Graph, true);
            }
        }
        );
        directedHashSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Hash_Set_Graph, true);
            }
        }
        );
        directedTreeSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Tree_Set_Graph, true);
            }
        }
        );
        directedListSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.List_Set_Graph, true);
            }
        }
        );
        directedSortedSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Sorted_Set_Graph, true);
            }
        }
        );
        //-------------------------------------------------
        undirectedMatrixGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Matrix_Graph, false);
            }
        }
        );
        undirectedHashSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Hash_Set_Graph, false);
            }
        }
        );
        undirectedTreeSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Tree_Set_Graph, false);
            }
        }
        );
        undirectedListSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.List_Set_Graph, false);
            }
        }
        );
        undirectedSortedSGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGraph(GraphView.Sorted_Set_Graph, false);
            }
        }
        );
        //-----------------------------------------------
        directedGraphAlgorithms = new JMenu("Алгоритмы для ориентированных графов");
        undirectedGraphAlgorithms = new JMenu("Алгоритмы для неориентированных графов");
        algoritmsMenu.add(directedGraphAlgorithms);
        algoritmsMenu.add(undirectedGraphAlgorithms);
        //------------------------------------------------
        JMenuItem reference = new JMenuItem("Посмотреть справку");
        JMenuItem aboutProgrammMenu = new JMenuItem("О программе");
        referenceMenu.add(reference);
        referenceMenu.add(aboutProgrammMenu);
        //------------------------------------------------
        aboutProgrammMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ReferenceBase frame = new ReferenceBase(aboutProgrammMenu);
                frame.setVisible(true);
                frame.readInfoFromFile("О программе.txt");
            }
        }
        );
        reference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ReferenceBase frame = new Reference(aboutProgrammMenu);
                frame.setVisible(true);
            }
        }
        );
        //------------------------------------------
        JMenuItem animationMenu = new JMenuItem("Cкорость анимации");
        optionMenu.add(animationMenu);
        //------------------------------------------
        animationMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                OptionFrame option = new OptionFrame(JGraphFrame.this, animationSpeed);
                option.setVisible(true);
                if (option.dialogResult()) {
                    animationSpeed = option.getAnimationSpeed();
                }
                option.dispose();
            }
        }
        );
        this.setJMenuBar(menu);
    }

    private void addGraphInfoComponents() {
        JLabel vertexNumLabel = new JLabel("Количество вершин:");
        JLabel edgeNumLabel = new JLabel("Количество ребер:");
        vertexNumText = new JTextField();
        edgeNumText = new JTextField();
        vertexNumText.setBackground(Color.WHITE);
        edgeNumText.setBackground(Color.WHITE);
        vertexNumText.setEditable(false);
        edgeNumText.setEditable(false);
        graphInfoPanel.add(vertexNumLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
        graphInfoPanel.add(vertexNumText, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        graphInfoPanel.add(edgeNumLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        graphInfoPanel.add(edgeNumText, new GridBagConstraints(0, 3, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
    }

    private void addInfoComponents() {
        JLabel graphViewLabel = new JLabel("Тип представления:");
        JLabel graphDirectedLabel = new JLabel("Тип графа:");
        graphViewText = new JTextField(16);
        graphDirectedText = new JTextField(12);
        graphViewText.setBackground(Color.WHITE);
        graphDirectedText.setBackground(Color.WHITE);
        graphViewText.setEditable(false);
        graphDirectedText.setEditable(false);
        infoPanel.add(graphViewLabel);
        infoPanel.add(graphViewText);
        infoPanel.add(graphDirectedLabel);
        infoPanel.add(graphDirectedText);
    }

    private void addOperationButtons() {
        JButton addV = new JButton("Вставка вершины");
        JButton removeV = new JButton("Удаление вершины");
        JButton addE = new JButton("Вставка ребра");
        JButton removeE = new JButton("Удаление ребра");
        JButton updateE = new JButton("Изменение веса ребра");
        JButton clearEdges = new JButton("Удаление всех ребер");
        JButton clear = new JButton("Очистка графа");
        JButton move = new JButton("Видоизменять граф");
        JButton removeOutE = new JButton("Удаление исходящих ребер");
        JButton removeInE = new JButton("Удаление входящих ребер");
        JButton transform = new JButton("Преобразование графа");
        buttonsPanel.add(addV, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(removeV, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(addE, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(removeE, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(updateE, new GridBagConstraints(0, 4, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(clearEdges, new GridBagConstraints(0, 5, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(clear, new GridBagConstraints(0, 6, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(move, new GridBagConstraints(0, 7, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(removeOutE, new GridBagConstraints(0, 8, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(removeInE, new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        buttonsPanel.add(transform, new GridBagConstraints(0, 10, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        //-------------------------------------------------------
        addV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startAddVertex(vertexNumText);
                commentTxt.setText("Щелкните по полю правой кнопкой мыши для того, "
                        + "чтобы добавить вершину.");
                input.setMode(true);
            }
        }
        );
        removeV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startRemoveVertex(vertexNumText, edgeNumText);
                commentTxt.setText("Щелкните правой кнопкой мыши по вершине, которую хотите удалить.");
                input.setEnabled(false);
            }
        }
        );
        addE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startAddEdge(edgeNumText);
                commentTxt.setText("Соедините две вершины на поле, для того чтобы добавить ребро.");
                input.setMode(false);
            }
        }
        );
        removeE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startRemoveEdge(edgeNumText);
                commentTxt.setText("Выберите правой кнопкой мыши вершины, "
                        + "для того, чтобы удалить ребро между ними.");
                input.setEnabled(false);
            }
        }
        );
        updateE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startUpdateEdge();
                commentTxt.setText("Выберите правой кнопкой мыши вершины, "
                        + "для того, чтобы изменить вес ребра между ними.");
                input.setEnabled(false);
            }
        }
        );
        clearEdges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.clearEdges();
                setNumbers();
                CancelAllOperationsWithGraph();
                input.setEnabled(false);
            }
        }
        );
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.clearGraph();
                setNumbers();
                CancelAllOperationsWithGraph();
                input.setEnabled(false);
            }
        }
        );
        move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startMoveGraph();
                commentTxt.setText("Начните передвигать правой кнопкой мыши любую вершину на поле.");
                input.setEnabled(false);
            }
        }
        );
        removeOutE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startRemoveOutEdges(edgeNumText);
                commentTxt.setText("Щелкните правой кнопкой мыши по вершине,"
                        + " для того, чтобы удалить исходяшие ребра.");
                input.setEnabled(false);
            }
        }
        );
        removeInE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                graphPanel.startRemoveInEdges(edgeNumText);
                commentTxt.setText("Щелкните правой кнопкой мыши по вершине,"
                        + " для того, чтобы удалить входящие ребра.");
                input.setEnabled(false);
            }
        }
        );
        //---------------------------------------------------
        transform.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String[] items = {GraphView.Matrix_Graph.getText(),
                    GraphView.Hash_Set_Graph.getText(),
                    GraphView.Tree_Set_Graph.getText(),
                    GraphView.List_Set_Graph.getText(),
                    GraphView.Sorted_Set_Graph.getText()};
                String result = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                        "Типа представления:", "Преобразование графа",
                        JOptionPane.INFORMATION_MESSAGE, null, items,
                        graphPanel.graphView().getText());
                //--------------------------------------------
                for (GraphView newView : GraphView.values()) {
                    if (newView.getText().equals(result)) {
                        graphPanel.transform(newView);
                        graphPanel.repaint();
                        setInfo();
                        return;
                    }
                }
            }
        }
        );
    }

    private void makeGUI() {
        this.setSize(1000, 700);
        this.setTitle(title);
        this.createMenu();
        this.setLayout(new GridBagLayout());
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        //----------------------------------------------------
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        workPanel = new JPanel(new GridBagLayout());
        workPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        //---------------------------------------------
        commentTxt = new JTextArea(1, 80);
        commentTxt.setEditable(false);
        commentTxt.setFont(new Font("Arial", Font.BOLD, 12));
        commentTxt.setForeground(Color.RED);
        commentTxt.setWrapStyleWord(true);
        commentTxt.setLineWrap(true);
        //-------------------------------------
        commentScrollPanel = new JScrollPane(commentTxt);
        commentScrollPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        //---------------------------------------------
        lowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lowPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        //----------------------------------------------
        mainPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 2, 0), 0, 0));
        mainPanel.add(workPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 2, 0), 0, 0));
        mainPanel.add(commentScrollPanel, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 2, 0), 0, 0));
        mainPanel.add(lowPanel, new GridBagConstraints(0, 3, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        //----------------------------------------------
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
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        //-----------------------------------------------
        graphInfoPanel = new JPanel(new GridBagLayout());
        graphInfoPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        accessuryPanel = new JPanel(new GridBagLayout());
        accessuryPanel.setBorder(BorderFactory.
                createEtchedBorder(new Color(0, 0, 0), null));
        //-----------------------------------------------
        operationPanel.add(graphInfoPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(buttonsPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(accessuryPanel, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        //-----------------------------------------------
        setColor();
        setComponentListener();
        setWindowListener();
        addInfoComponents();
        addOperationButtons();
        addGraphInfoComponents();
        addInputComponents();
        setInfo();
        setAlgorithmsEnabled();
        //--------------------------------
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
        this.setLocationRelativeTo(null);
    }

    /**
     *
     * @return
     */
    private Graph<Vertex, Edge2D> createClone() {
        return graph().copy();
    }

    /**
     *
     * @param component
     * @param result
     */
    private void createResultFrame(Component component, String result) {
        AlgorithmsResultsFrame resultFrame
                = new AlgorithmsResultsFrame(component, result);
        resultFrame.setVisible(true);
        CancelAllOperationsWithGraph();
        input.setEnabled(false);
    }

    private void showEdges(final Collection<Edge2D> tree, String title) {
        setEnabledForOperations(false);
        JLabel txt = new JLabel("Результаты:");
        JButton showTree = new JButton(title);
        JButton exit = new JButton("Выход");
        accessuryPanel.removeAll();
        accessuryPanel.add(txt, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        accessuryPanel.add(showTree, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.add(exit, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.revalidate();
        //--------------------------------------------
        showTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                for (Edge2D e : tree) {
                    e.color = Color.GREEN;
                    e.dimension = 3;
                }
                graphPanel.repaint();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                for (Edge2D e : tree) {
                    e.color = Edge2D.default_color;
                    e.dimension = Edge2D.default_dimension;
                }
                graphPanel.repaint();
                accessuryPanel.removeAll();
                accessuryPanel.repaint();
                setEnabledForOperations(true);
            }
        });
    }

    /**
     *
     */
    private void bridgesSearch() {
        JMenuItem bridgesSearchMenu = new JMenuItem("Поиск всех мостов");
        undirectedGraphAlgorithms.add(bridgesSearchMenu);
        bridgesSearchMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                BridgesSearch<Vertex, Edge2D> bs = new BridgesSearch<>(graph());
                if (bs.connected()) {
                    StringBuilder result
                            = new StringBuilder("Количество ребер-мостов: " + bs.bridges().size()
                                    + separator + "Ребра мосты: " + separator);
                    for (Edge2D e : bs.bridges()) {
                        result.append(e.print()).append(separator);
                    }
                    createResultFrame(bridgesSearchMenu, result.toString());
                    showEdges(bs.bridges(), "Отобразить мосты");
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this, "Граф должен быть связным!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        );
    }

    /**
     *
     */
    private void topologicalSort() {
        JMenuItem topoSortMenu = new JMenuItem("Топологическая сортировка");
        directedGraphAlgorithms.add(topoSortMenu);
        topoSortMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TopologicalSort<Vertex> topoSort = new TopologicalSort<>(graph());
                if (topoSort.acyclic()) {
                    StringBuilder result
                            = new StringBuilder("Последовательность вершин топологической сортировки:"
                                    + separator);
                    result.append(topoSort.sequence());
                    createResultFrame(topoSortMenu, result.toString());
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this, "Граф должен быть ациклическим!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        );
    }

    private void showConnectedComponents(AbstractConnectedComponents<Vertex> connComp,
            Component component) {
        StringBuilder result
                = new StringBuilder("Количество связных компонет: "
                        + connComp.componentsNum() + separator
                        + "Связные компонеты графа:" + separator);
        result.append(connComp);
        //--------------------------------------------------------
        createResultFrame(component, result.toString());
    }

    /**
     *
     */
    private void connectedComponents() {
        JMenuItem connectedComponentsMenu = new JMenuItem("Связные компоненты");
        JMenuItem stronglyConnectedComponentsMenu
                = new JMenuItem("Сильно связные компоненты");
        undirectedGraphAlgorithms.add(connectedComponentsMenu);
        directedGraphAlgorithms.add(stronglyConnectedComponentsMenu);
        connectedComponentsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showConnectedComponents(new ConnectedComponents<>(graph()),
                        connectedComponentsMenu);

            }
        }
        );
        stronglyConnectedComponentsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showConnectedComponents(new StronglyConnectedComponents<>(graph()),
                        stronglyConnectedComponentsMenu);

            }
        }
        );
    }

    /**
     *
     * @return
     */
    private Vertex firstVertex() {
        Iterator<Vertex> v = graph().iterator();
        if (v.hasNext()) {
            return v.next();
        }
        return null;
    }

    /**
     *
     * @param component
     */
    private void createMst(MstType type, Component component, String title) {
        ConnectedComponents<Vertex> connComp
                = new ConnectedComponents<>(graph());
        if (connComp.connected()) {
            if (!NumberParser.isNegativeWeights(graph())) {
                MinimumSpanningTree<Edge2D> mst = null;
                //--------------------------------------------
                switch (type) {
                    case Prim:
                        mst
                                = new PrimMinimumSpanningTree<Vertex, Edge2D>(graph(),
                                        firstVertex());
                        break;

                    case Kruskal:
                        mst
                                = new KruskalMinimumSpanningTree<Vertex, Edge2D>(graph());
                        break;
                }
                if (mst != null) {
                    StringBuilder result = new StringBuilder(title
                            + separator + "Вес остова: " + mst.getMinimumSpanningTreeWeight()
                            + separator + "Ребра остова:" + separator);
                    //--------------------------------------------
                    final Collection<Edge2D> tree = mst.getMinimumSpanningTreeEdges();
                    for (Edge2D e : tree) {
                        result.append(e.print()).append(separator);
                    }
                    //--------------------------------------------------
                    createResultFrame(component, result.toString());
                    showEdges(tree, "Отобразить дерево");
                    //-------------------------------------------
                }
            } else {
                JOptionPane.showMessageDialog(JGraphFrame.this,
                        "Весовая функция должна быть положительной!",
                        null, JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(JGraphFrame.this,
                    "Граф должен быть связным!",
                    null, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     */
    private void mstSearch() {
        JMenu mstMenu = new JMenu("Построение минимального остовного дерева");
        JMenuItem primMstMenu = new JMenuItem("Алгоритм Прима");
        JMenuItem kruskalMstMenu = new JMenuItem("Алгоритм Крускала");
        mstMenu.add(primMstMenu);
        mstMenu.add(kruskalMstMenu);
        undirectedGraphAlgorithms.add(mstMenu);
        primMstMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createMst(MstType.Prim, primMstMenu, "Результаты алгоритма Прима:");
            }
        }
        );
        kruskalMstMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createMst(MstType.Kruskal, kruskalMstMenu, "Результаты алгоритма Крускала:");
            }
        }
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
                input.setEnabled(false);
                if (!NumberParser.isNullWeights(graph())) {
                    Graph<Vertex, Edge2D> clone = createClone();
                    showAllSpt(new JohnsonAllPairsShortestPaths<>(clone, new Vertex("")),
                            "Результаты алгоритма Джонсона", allSptMenuJohnson);
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Не все веса ребер заданы!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
                //------------------------------------------------
            }
        });
        //--------------------------------------------------------
        allSptMenuFloydWarshall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                if (!NumberParser.isNullWeights(graph())) {
                    showAllSpt(new FloydWarshallAllPairsShortestPaths<>(graph()), "Результаты алгоритма Флойда", allSptMenuFloydWarshall);
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Не все веса ребер заданы!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
                //--------------------------------------------------------
            }
        });

    }

    /**
     *
     * @param allSpt
     * @return
     */
    private Pair<String, String> createAllSptDecision(AllPairsShortestPaths<Vertex, Edge2D> allSpt) {
        StringBuilder dist
                = new StringBuilder("Кратчайшие расстояния между вершинами:" + separator);
        StringBuilder paths
                = new StringBuilder("Структуры путей:" + separator);
        for (Vertex u : graph()) {
            for (Vertex v : graph()) {
                if (allSpt.isPath(u, v)) {
                    dist.append("w[p(").append(u).append(",")
                            .append(v).append(")] = ").append(allSpt.getDistance(u, v))
                            .append(separator);
                    paths.append("p(").append(u).append(",").append(v)
                            .append(") = ").append(allSpt.getPath(u, v)).append(separator);
                } else {
                    dist.append("Пути из ").append(u).append(" в ").append(v)
                            .append(" не существует!").append(separator);
                    paths.append("Пути из ").append(u).append(" в ").append(v)
                            .append(" не существует!").append(separator);
                }
            }
        }
        return new Pair<>(dist.toString(), paths.toString());
    }

    private void showAllPaths(final AllPairsShortestPaths<Vertex, Edge2D> allSpt) {
        //--------------------------------------------------         
        JComboBox<String> source = new JComboBox<String>();
        JComboBox<String> target = new JComboBox<String>();
        for (Vertex u : graph()) {
            source.addItem(u.toString());
            target.addItem(u.toString());
        }
        //-------------------------------------------
        JButton showPath = new JButton("Показать");
        showPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Vertex u = graphPanel.vertex((String) source.getSelectedItem());
                Vertex v = graphPanel.vertex((String) target.getSelectedItem());
                if (allSpt.isPath(u, v)) {
                    setDefaultColorForEdges();
                    setDefaultColorForVerticesBorders();
                    graphPanel.repaint();
                    PathDrawer drawer = new PathDrawer(allSpt.getPath(u, v));
                    drawer.start();
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Пути из '" + u + "' в '"
                            + v + "' не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //----------------------------------------------
        addSptComponents(source, target, showPath);
          //--------------------------------------------------------------

    }

    /**
     *
     * @param allSpt
     */
    private void showAllSpt(final AllPairsShortestPaths<Vertex, Edge2D> allSpt, String title, JMenuItem item) {
        if (allSpt.decision()) {
            Pair<String, String> result = createAllSptDecision(allSpt);
            SptResultsFrame resultFrame
                    = new SptResultsFrame(item, title,
                            result.getKey(), result.getValue());
            resultFrame.setVisible(true);
            showAllPaths(allSpt);
            CancelAllOperationsWithGraph();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Граф содержит цикл с отрицательным весом!",
                    null, JOptionPane.WARNING_MESSAGE);
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
                input.setEnabled(false);
                TransitiveClosure<Vertex, Edge2D> tc
                        = new TransitiveClosure<>(graph());
                StringBuilder result
                        = new StringBuilder("Результаты транзитивного замыкания:" + separator);
                for (Vertex u : graph()) {
                    for (Vertex v : graph()) {
                        String isPath = tc.isPath(u, v)
                                ? " существует!" : " не существует!";
                        result.append("Путь из ").append(u).append(" в ")
                                .append(v).append(isPath).append(separator);
                    }
                }
                //------------------------------------------
                createResultFrame(transitiveClosureMenu, result.toString());
            }
        });
    }

    private void graphProperties() {
        JMenu graphPropertiesMenu = new JMenu("Метрические характеристики графа");
        directedGraphAlgorithms.add(graphPropertiesMenu);
        JMenuItem graphRadiusMenu = new JMenuItem("Радиус графа");
        JMenuItem graphDiametrMenu = new JMenuItem("Диаметр графа");
        JMenuItem eccentricityMenu = new JMenuItem("Эксцентриситет вершины");
        graphPropertiesMenu.add(graphRadiusMenu);
        graphPropertiesMenu.add(graphDiametrMenu);
        graphPropertiesMenu.add(eccentricityMenu);
        graphRadiusMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                if (!NumberParser.isNullWeights(graph())) {
                    FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> allSpt
                            = new FloydWarshallAllPairsShortestPaths<>(graph());
                    if (allSpt.decision()) {
                        String result = "Радиус графа:" + separator + "r(G) = "
                                + GraphMetricProperties.radius(allSpt);
                        createResultFrame(graphRadiusMenu, result);
                    } else {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Граф содержит цикл с отрицательным весом!",
                                null, JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Не все веса ребер заданы!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
                //------------------------------------------
            }
        });
        //----------------------------------------------------
        graphDiametrMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                if (!NumberParser.isNullWeights(graph())) {
                    FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> allSpt
                            = new FloydWarshallAllPairsShortestPaths<>(graph());
                    if (allSpt.decision()) {
                        String result = "Диаметр графа:" + separator + "d(G) = "
                                + GraphMetricProperties.diametr(allSpt);
                        createResultFrame(graphDiametrMenu, result);
                    } else {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Граф содержит цикл с отрицательным весом!",
                                null, JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Не все веса ребер заданы!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
                //------------------------------------------
            }
        });
        //----------------------------------------------------
        eccentricityMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                String v = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                        "Вершина:",
                        "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                        null, null);
                if (v != null) {
                    Vertex s = graphPanel.vertex(v);
                    if (s == null) {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Такой вершины не существует!",
                                null, JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (!NumberParser.isNegativeWeights(graph())) {
                        String result = "Эксцентриситет вершины:" + separator
                                + "e(" + s + ") = "
                                + GraphMetricProperties.eccentricity(
                                        new DijkstraShortestPaths<>(graph(), s));
                        createResultFrame(graphDiametrMenu, result);
                    } else {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Весовая функция должна быть положительной!",
                                null, JOptionPane.WARNING_MESSAGE);
                    }
                }
                //------------------------------------------
            }
        });
    }

    private void setDefaultColorForEdges() {
        Iterator<Edge2D> edge = graph().edgeIterator();
        while (edge.hasNext()) {
            Edge2D e = edge.next();
            e.color = Edge2D.default_color;
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
        accessuryPanel.removeAll();
        accessuryPanel.repaint();
        setEnabledForOperations(true);
    }

    /**
     *
     * @param type
     * @param component
     * @param title
     * @param error
     */
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
                createResultFrame(component, result.toString());
                showTour(tour);
            } else {
                JOptionPane.showMessageDialog(JGraphFrame.this,
                        error, null, JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(JGraphFrame.this,
                    "Граф должен быть связным!",
                    null, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * @param tour
     */
    private void showTour(final Collection<Vertex> tour) {
        //--------------------------------------------------
        setEnabledForOperations(false);
        JLabel txt = new JLabel("Результаты:");
        JButton showTour = new JButton("Отобразить цикл");
        JButton exit = new JButton("Выход");
        accessuryPanel.removeAll();
        accessuryPanel.add(txt, new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        accessuryPanel.add(showTour, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.add(exit, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.revalidate();
        //--------------------------------------------
        showTour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setDefaultColorForEdges();
                setDefaultColorForVerticesBorders();
                graphPanel.repaint();
                PathDrawer drawer = new PathDrawer(tour);
                drawer.start();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                clearGraphPanel();
            }
        });
        //-------------------------------------------        
    }

    /**
     *
     */
    private void eulerTour() {
        JMenuItem eulerTourMenu = new JMenuItem("Поиск Эйлерова цикла");
        undirectedGraphAlgorithms.add(eulerTourMenu);
        eulerTourMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchTour(TourType.euler_tour, eulerTourMenu,
                        "Структура Эйлерова цикла:" + separator, "Эйлерова цикла не существует!");
            }
        });
    }

    /**
     *
     */
    private void gamiltonTour() {
        JMenuItem gamiltonTourMenu = new JMenuItem("Поиск Гамильтонова цикла");
        undirectedGraphAlgorithms.add(gamiltonTourMenu);
        gamiltonTourMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchTour(TourType.gamilton_tour, gamiltonTourMenu,
                        "Структура Гамильтонова цикла:" + separator, "Гамильтонова цикла не существует!");
            }
        });
    }

    /**
     *
     * @param source
     * @param target
     * @param showPath
     */
    private void addSptComponents(JComboBox<String> source, JComboBox<String> target,
            JButton showPath) {
        setEnabledForOperations(false);
        JLabel txt = new JLabel("Кратчайший путь:");
        JButton exit = new JButton("Выход");
          //------------------------------------------
        //-------------------------------------------
        accessuryPanel.removeAll();
        accessuryPanel.add(txt, new GridBagConstraints(0, 0, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        //-----------------------------------------------------------
        accessuryPanel.add(new JLabel("Из"), new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        accessuryPanel.add(source, new GridBagConstraints(1, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        accessuryPanel.add(new JLabel("в"), new GridBagConstraints(2, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        accessuryPanel.add(target, new GridBagConstraints(3, 1, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 0, 10, 0), 0, 0));
        //-----------------------------------------------------------
        accessuryPanel.add(showPath, new GridBagConstraints(0, 2, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.add(exit, new GridBagConstraints(0, 3, 4, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        accessuryPanel.revalidate();
        //-------------------------------------------------
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                clearGraphPanel();
            }
        });
    }

    /**
     *
     * @param spt
     */
    private void showPaths(final GraphPaths<Vertex> spt) {
         //--------------------------------------------------         
        //------------------------------------------
        JComboBox<String> source = new JComboBox<String>();
        source.addItem(spt.getSource().toString());
        JComboBox<String> target = new JComboBox<String>();
        for (Vertex u : graph()) {
            target.addItem(u.toString());
        }
        //-------------------------------------------
        JButton showPath = new JButton("Показать");
        showPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Vertex u = graphPanel.vertex((String) target.getSelectedItem());
                if (spt.isPath(u)) {
                    setDefaultColorForEdges();
                    setDefaultColorForVerticesBorders();
                    graphPanel.repaint();
                    PathDrawer drawer = new PathDrawer(spt.getPath(u));
                    drawer.start();
                } else {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Пути из '" + spt.getSource() + "' в '"
                            + u + "' не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //----------------------------------------------
        addSptComponents(source, target, showPath);
          //--------------------------------------------------------------

    }

    /**
     *
     * @param spt
     * @return
     */
    private Pair<String, String> createSptDecision(GraphPaths<Vertex> spt) {
        StringBuilder dist
                = new StringBuilder("Кратчайшие расстояния между вершинами:" + separator);
        StringBuilder paths
                = new StringBuilder("Структуры путей:" + separator);
        for (Vertex v : graph()) {
            if (spt.isPath(v)) {
                dist.append("w[p(").append(spt.getSource()).append(",")
                        .append(v).append(")] = ").append(spt.getDistance(v))
                        .append(separator);
                paths.append("p(").append(spt.getSource()).append(",")
                        .append(v).append(") = ").append(spt.getPath(v)).append(separator);
            } else {
                dist.append("Пути из ").append(spt.getSource()).append(" в ")
                        .append(v).append(" не существует!").append(separator);
                paths.append("Пути из ").append(spt.getSource()).append(" в ")
                        .append(v).append(" не существует!").append(separator);
            }
        }
        return new Pair<>(dist.toString(), paths.toString());
    }

    /**
     *
     * @param algorithm
     */
    private void sptDialog(SptAlgorithm algorithm, Component component) {
        input.setEnabled(false);
        if (!NumberParser.isNullWeights(graph())) {
            String v = (String) JOptionPane.showInputDialog(this,
                    "Вершина:",
                    "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                    null, null);
            if (v != null) {
                Vertex s = graphPanel.vertex(v);
                if (s == null) {
                    JOptionPane.showMessageDialog(JGraphFrame.this,
                            "Такой вершины не существует!",
                            null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //-----------------------------------------------
                switch (algorithm) {
                    case dijkstra:
                        sptSearch(new DijkstraShortestPaths<>(graph(), s),
                                component, "Результаты алгоритма Дейкстры", null);
                        break;
                    case bellman_ford:
                        sptSearch(new BellmanFordShortestPaths<>(graph(), s),
                                component, "Результаты алгоритма Беллмана-Форда",
                                "Граф содержит цикл с отрицательным весом!");
                        break;
                    case dag:
                        sptSearch(new DAGShortestPaths<>(graph(), s),
                                component, "Кратчайшие пути в DAG",
                                "Граф должен быть ациклическим!");
                        break;
                }
            }
            //----------------------------------------------------
        } else {
            JOptionPane.showMessageDialog(JGraphFrame.this,
                    "Не все веса ребер заданы!",
                    null, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * @param spt
     * @param title
     * @param error
     */
    private void sptSearch(ShortestPaths<Vertex, Edge2D> spt, Component component,
            String title, String error) {
        if (spt.decision()) {
            Pair<String, String> result = createSptDecision(spt);
            SptResultsFrame resultFrame
                    = new SptResultsFrame(component, title,
                            result.getKey(), result.getValue());
            resultFrame.setVisible(true);
            showPaths(spt);
            CancelAllOperationsWithGraph();
        } else {
            JOptionPane.showMessageDialog(JGraphFrame.this,
                    error, null, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     */
    private void shortestPaths() {
        JMenu sptMenu = new JMenu("Кратчайшие пути из одной вершины");
        directedGraphAlgorithms.add(sptMenu);
        JMenuItem dijkstraSptMenu = new JMenuItem("Алгоритм Дейкстры");
        JMenuItem bellmanFordSptMenu = new JMenuItem("Алгоритм Беллмана-Форда");
        JMenuItem dagSptMenu = new JMenuItem("Кратчайшие пути в DAG");
        sptMenu.add(dijkstraSptMenu);
        sptMenu.add(bellmanFordSptMenu);
        sptMenu.add(dagSptMenu);
        //----------------------------------------------------
        dijkstraSptMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                sptDialog(SptAlgorithm.dijkstra, dijkstraSptMenu);
            }
        });
        //----------------------------------------------------
        bellmanFordSptMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                sptDialog(SptAlgorithm.bellman_ford, bellmanFordSptMenu);
            }
        });
        //----------------------------------------------------
        dagSptMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                sptDialog(SptAlgorithm.dag, dagSptMenu);
            }
        });
    }

    private void bfsVisit() {
        JMenuItem bfsMenu = new JMenuItem("Кратчайшие пути (BFS)");
        algoritmsMenu.add(bfsMenu);
        bfsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                input.setEnabled(false);
                String v = (String) JOptionPane.showInputDialog(JGraphFrame.this,
                        "Вершина:",
                        "Выбор вершины", JOptionPane.INFORMATION_MESSAGE, null,
                        null, null);
                if (v != null) {
                    Vertex s = graphPanel.vertex(v);
                    if (s == null) {
                        JOptionPane.showMessageDialog(JGraphFrame.this,
                                "Такой вершины не существует!",
                                null, JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    //---------------------------------
                    BreadFirstSearch<Vertex> bfs
                            = new BreadFirstSearch<>(graph(), s);
                    Pair<String, String> result = createSptDecision(bfs);
                    SptResultsFrame resultFrame
                            = new SptResultsFrame(bfsMenu, "Кратчайшик пути (BFS)",
                                    result.getKey(), result.getValue());
                    resultFrame.setVisible(true);
                    showPaths(bfs);
                    CancelAllOperationsWithGraph();
                }
                //--------------------------------------------------               
            }
        });
    }

    private void dfsVisit() {
        JMenuItem dfsMenu = new JMenuItem("Обход в ширину (DFS)");
        algoritmsMenu.add(dfsMenu);
        dfsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //---------------------------------
                input.setEnabled(false);
                DepthFirstSearch<Vertex> dfs
                        = new DepthFirstSearch<>(graph());

                StringBuilder paths = new StringBuilder("Структуры путей:" + separator);
                StringBuilder discovery = new StringBuilder("Метки обнаружения" + separator);
                StringBuilder finishing = new StringBuilder("Метки завершения:" + separator);
                for (Vertex v : graph()) {
                    paths.append("p[").append(v).append("] = ")
                            .append(dfs.getPath(v)).append(separator);
                    discovery.append("d[").append(v).append("] = ")
                            .append(dfs.discoveryTime(v)).append(separator);
                    finishing.append("f[").append(v).append("] = ")
                            .append(dfs.finishingTime(v)).append(separator);
                }
                //--------------------------------------------
                DFSResultsFrame resultFrame
                        = new DFSResultsFrame(dfsMenu, paths.toString(), discovery.toString(),
                                finishing.toString());
                resultFrame.setVisible(true);
                CancelAllOperationsWithGraph();
                //--------------------------------------------------               
            }
        });
    }

    private void allPathsSearch() {
        JMenuItem allPathsMenu = new JMenuItem("Поиск всех путей");
        algoritmsMenu.add(allPathsMenu);
        allPathsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //---------------------------------
                VertexInputDialog dialog = new VertexInputDialog(JGraphFrame.this);
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
                            = new StringBuilder("Количество путей: " + allPaths.size() + separator
                                    + "Структуры путей:" + separator);
                    result.append(allPaths);
                    createResultFrame(allPathsMenu, result.toString());
                }
                dialog.dispose();
                //--------------------------------------------------               
            }
        });
    }

    /**
     *
     */
    private void mstClustering() {
        JMenuItem mstClusteringMenu = new JMenuItem("Кластеризация "
                + "(Алгоритм минимального покрывающего дерева)");
        algoritmsMenu.add(mstClusteringMenu);
        mstClusteringMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //---------------------------------
                ClusteringAlgorithmInputDialog dialog
                        = new ClusteringAlgorithmInputDialog(JGraphFrame.this);
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
                                        + separator
                                        + "Структуры кластеров:" + separator);
                        result.append(clustering);
                        createResultFrame(mstClusteringMenu, result.toString());
                    } catch (InternalError | IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(JGraphFrame.this, e,
                                null, JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
                //--------------------------------------------------               
            }
        });

    }

} //End of class JGraphFrame
