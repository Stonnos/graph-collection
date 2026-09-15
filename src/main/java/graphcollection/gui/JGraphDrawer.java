/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import graphcollection.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Рома
 */
public class JGraphDrawer extends JPanel {

    private Graph<Vertex, Edge2D> graph; //граф
    private GraphView graphView;
    private Map<String, Vertex> vertexMap = new HashMap<>(); //словарь имен вершин
    private Vertex u; //вспомогательная вершина;
    private int x, y; //координаты мыши;
    private boolean isAddEdgeMode; //режим (вставка ребра/др.)
    private String vertexName; //имя вершины
    private Number weight; //вес ребра
    private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private int dwidth, dheight; //вспомогательные поля для хранения предыдущего размера окна
    public static final int maxCount = 40;

    public JGraphDrawer() {
        this(GraphView.Hash_Set_Graph, false, 0, 0);
    }

    public JGraphDrawer(boolean direction) {
        this(GraphView.Hash_Set_Graph, direction, 0, 0);
    }

    public JGraphDrawer(GraphView gView, boolean direction) {
        this(gView, direction, 0, 0);
    }

    public JGraphDrawer(GraphView gView, boolean direction, int V, int E) {
        checkVertexCount(V);
        createGraph(gView, direction, V, E);
    }

    public JGraphDrawer(GraphView gView, boolean direction, int V, int E, int a, int b) {
        this(gView, direction, V, E);
        RandomGraph r = new RandomGraph();
        r.generateIntWeights(graph, a, b);
    }

    public JGraphDrawer(GraphView gView, boolean direction, int V, int E, double a, double b) {
        this(gView, direction, V, E);
        RandomGraph r = new RandomGraph();
        r.generateDoubleWeights(graph, a, b);
    }

    public void generate(GraphView gView, boolean direction, int V, int E) {
        checkVertexCount(V);
        clear();
        this.createGraph(gView, direction, V, E);
    }

    public void generate(GraphView gView, boolean direction, int V, int E, int a, int b) {
        this.generate(gView, direction, V, E);
        RandomGraph r = new RandomGraph();
        r.generateIntWeights(graph, a, b);
    }

    public void generate(GraphView gView, boolean direction, int V, int E, double a, double b) {
        this.generate(gView, direction, V, E);
        RandomGraph r = new RandomGraph();
        r.generateDoubleWeights(graph, a, b);
    }

    public void read(String fileName) {
        GraphParser p = new GraphParser();
        Graph<Vertex, Edge2D> g = p.read(fileName);
        fillGraph(g);
    }

    public void transform(GraphView gView) {
        if (!graphView().equals(gView)) {
            RandomGraph r = new RandomGraph();
            Graph<Vertex, Edge2D> newG = r.generate(gView, graph.direction(), 0, 0);
            for (Vertex v : graph) {
                newG.addVertex(v);
            }
            Iterator<Edge2D> edge = graph.edgeIterator();
            while (edge.hasNext()) {
                newG.addEdge(edge.next());
            }
            graph.clear();
            graph = newG;
            graphView = gView;
        }
    }

    public GraphView graphView() {
        return graphView;
    }

    public Graph<Vertex, Edge2D> graph() {
        return graph;
    }

    public Map<String, Vertex> vertexMap() {
        return vertexMap;
    }

    public Vertex vertex(String v) {
        return vertexMap.get(v);
    }

    public void setNextVertex(String vertexName) {
        this.vertexName = vertexName;
    }

    public void setNextWeight(Number weight) {
        this.weight = weight;
    }

    public void clear() {
        graph.clear();
        vertexMap.clear();
    }

    public void clearGraph() {
        clear();
        repaint();
    }

    public void clearEdges() {
        graph.clearEdges();
        repaint();
    }

    public Image getImage() {
        Image img = this.createImage(getWidth(), getHeight());
        Graphics2D g = (Graphics2D) img.getGraphics();
        drawGraph(g);
        return img;
    }

    public void create(GraphView gView, boolean direction) {
        clear();
        createGraph(gView, direction, 0, 0);
    }

    public void resize() {
        for (Vertex v : graph) {
            v.ellipse.setFrame(v.ellipse.getX() * getWidth() / dwidth,
                    v.ellipse.getY() * getHeight() / dheight,
                    getVertexSize(), getVertexSize());
        }
        dwidth = getWidth();
        dheight = getHeight();
    }

    public void setComponentListener() {
        this.addComponentListener(new ComponentListener() {

            private static final int minimumSize = 40;

            @Override
            public void componentShown(ComponentEvent evt) {

            }

            @Override
            public void componentResized(ComponentEvent evt) {
                if (getWidth() > minimumSize && getHeight() > minimumSize) {
                    resize();
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

    private void createGraph(GraphView gView, boolean direction, int V, int E) {
        RandomGraph r = new RandomGraph();
        graph = r.generate(gView, direction, V, E);
        fillMap();
        this.generateCoordinatesForVertices();
        graphView = gView;
    }

    private void fillGraph(Graph<Vertex, Edge2D> g) {
        if (g.verticesNum() > maxCount) {
            g.clear();
            throw new IllegalArgumentException("Количество вершин не должно превышать "
                    + String.valueOf(maxCount));
        }
        clear();
        graph = g;
        graphView = GraphView.Hash_Set_Graph;
        fillMap();
        this.generateCoordinatesForVertices();
        repaint();
    }

    private void fillMap() {
        for (Vertex v : graph) {
            vertexMap.put(v.name, v);
        }
    }

    private double getVertexSize() {
        return getHeight() / 20.0;
    }

    private void checkVertexCount(int V) {
        if (V > maxCount) {
            throw new IllegalArgumentException("Количество вершин не должно превышать "
                    + String.valueOf(maxCount));
        }
    }

    /**
     *
     */
    private class VertexDrawer implements Runnable {
        private final Vertex v;
        private static final int flashCount = 6;
        private final Thread thr;

        public VertexDrawer(Vertex v) {
            this.v = v;
            thr = new Thread(this);
        }

        @Override
        public void run() {
            Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
            try {
                setStrokeForVertex(g);
                setFont(g, (int) v.getWidth() / 2);
                double x = v.getX(), y = v.getY(), r = v.getWidth();
                for (int i = flashCount; i >= 1; i--) {
                    v.ellipse.setFrame(x - r / (2 * i), y - r / (2 * i),
                            r / i, r / i);
                    g.clearRect((int) v.getX(), (int) v.getY(),
                            (int) v.getWidth(), (int) v.getWidth());
                    g.setPaint(getBackground());
                    g.fillRect((int) v.getX(), (int) v.getY(),
                            (int) v.getWidth(), (int) v.getWidth());
                    v.drawVertex(g);
                    v.drawVertexBorder(g);
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
            }
            //---------------------------------
            v.drawVertexName(g);
            repaint();
        }

        public void start() {
            thr.start();
        }

    }

    private void setStrokeForVertex(Graphics2D g) {
        g.setStroke(new BasicStroke(3));
    }

    private void setFont(Graphics2D g, int size) {
        Font font = new Font("Arial", Font.BOLD, size);
        g.setFont(font);
    }

    //-------------------------------------
    //---------------------------------------

    private Vertex searchVertex(int x, int y) {
        for (Vertex v : graph) {
            if (v.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    public void end() {
        for (MouseListener listener : this.getMouseListeners()) {
            this.removeMouseListener(listener);
        }
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            this.removeMouseMotionListener(listener);
        }
        isAddEdgeMode = false;
        if (u != null) {
            u.borderColor = Color.BLACK;
            Graphics2D g = (Graphics2D) getGraphics();
            setStrokeForVertex(g);
            u.drawVertexBorder(g);
            u = null;
        }
    }

    public void startAddVertex(final JTextField text) {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {

            private Vertex generateVertex(int x, int y) {
                Random r = new Random();
                String v = vertexName;
                if (vertexName == null) {
                    do {
                        v = String.valueOf(r.nextInt(99));
                    }
                    while (vertexMap.containsKey(v));
                }
                //----------------------------------------
                return new Vertex(v, new Ellipse2D.Double(x, y, getVertexSize(), getVertexSize()));
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    if (graph.verticesNum() == maxCount) {
                        JOptionPane.showMessageDialog(JGraphDrawer.this,
                                "Добавлять вершины больше нельзя!",
                                "Добавление вершины", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Vertex v = searchVertex(me.getX(), me.getY());
                        if (v == null) {
                            if (vertexMap.containsKey(vertexName)) {
                                JOptionPane.showMessageDialog(JGraphDrawer.this,
                                        "Вершина '" + vertexName + "' существует!",
                                        "Добавление вершины", JOptionPane.WARNING_MESSAGE);
                            } else {
                                v = generateVertex(me.getX(), me.getY());
                                graph.addVertex(v);
                                vertexMap.put(v.name, v);
                                if (text != null) {
                                    text.setText(String.valueOf(graph.verticesNum()));
                                }
                                VertexDrawer vertex = new VertexDrawer(v);
                                vertex.start();
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }

    public void startRemoveVertex(final JTextField text1, final JTextField text2) {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        vertexMap.remove(u.name);
                        graph.removeVertex(u);
                        if (text1 != null) {
                            text1.setText(String.valueOf(graph.verticesNum()));
                        }
                        if (text2 != null) {
                            text2.setText(String.valueOf(graph.edgesNum()));
                        }
                        u = null;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }

    public void startRemoveOutEdges(final JTextField text) {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        graph.removeOutEdges(u);
                        if (text != null) {
                            text.setText(String.valueOf(graph.edgesNum()));
                        }
                        u = null;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }

    public void startRemoveInEdges(final JTextField text) {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        graph.removeInEdges(u);
                        if (text != null) {
                            text.setText(String.valueOf(graph.edgesNum()));
                        }
                        u = null;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }

    public void startAddEdge(final JTextField text) {
        end();
        isAddEdgeMode = true;
        //-------------------------------------
        //-----------------------------------------------
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        u.borderColor = Color.RED;
                        Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
                        setStrokeForVertex(g);
                        u.drawVertexBorder(g);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (u != null) {
                    Vertex v = searchVertex(me.getX(), me.getY());
                    if (v != null) {
                        if (!graph.addEdge(new Edge2D(graph.direction(), u, v, weight))) {
                            JOptionPane.showMessageDialog(JGraphDrawer.this,
                                    "Невозможно добавить ребро!",
                                    "Добавление ребра", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    if (text != null) {
                        text.setText(String.valueOf(graph.edgesNum()));
                    }
                    u.borderColor = Color.BLACK;
                    u = null;
                    repaint();
                }
            }
        });
        //-----------------------------------------------
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent me) {

            }

            @Override
            public void mouseDragged(MouseEvent me) {
                x = me.getX();
                y = me.getY();
                repaint();
            }

        });
    }

    public void startRemoveEdge(final JTextField text) {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    Vertex z = searchVertex(me.getX(), me.getY());
                    if (z != null) {
                        if (u == null) {
                            u = z;
                            u.borderColor = Color.RED;
                            Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
                            setStrokeForVertex(g);
                            u.drawVertexBorder(g);
                        } else {
                            if (graph.removeEdge(new Edge2D(graph.direction(), u, z)) == 0) {
                                JOptionPane.showMessageDialog(JGraphDrawer.this, "Ребра не существует!",
                                        "Удаление ребра", JOptionPane.WARNING_MESSAGE);
                            }
                            if (text != null) {
                                text.setText(String.valueOf(graph.edgesNum()));
                            }
                            u.borderColor = Color.BLACK;
                            u = null;
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }

    public void startUpdateEdge() {
        end();
        //-------------------------------------
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    Vertex z = searchVertex(me.getX(), me.getY());
                    if (z != null) {
                        if (u == null) {
                            u = z;
                            u.borderColor = Color.RED;
                            Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
                            setStrokeForVertex(g);
                            u.drawVertexBorder(g);
                        } else {
                            Edge2D e = graph.edge(u, z);
                            if (e == null) {
                                JOptionPane.showMessageDialog(JGraphDrawer.this, "Ребра не существует!",
                                        "Изменение веса ребра", JOptionPane.WARNING_MESSAGE);
                            } else {
                                //--------------------------------------------
                                String strWeight = (String) JOptionPane.showInputDialog(JGraphDrawer.this,
                                        "Введите вес:",
                                        "Изменение веса ребра", JOptionPane.INFORMATION_MESSAGE, null,
                                        null, e.getWeight());
                                if (strWeight != null) {
                                    Number newWeight = NumberParser.parse(strWeight);
                                    if (newWeight == null) {
                                        JOptionPane.showMessageDialog(JGraphDrawer.this,
                                                "Вес ребра должен быть числовой!",
                                                "Изменение веса ребра", JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        e.setWeight(newWeight);
                                    }
                                }
                                //---------------------------------------------
                            }
                            u.borderColor = Color.BLACK;
                            u = null;
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }
        });
        //-----------------------------------------------
    }


    public void startMoveGraph() {
        end();
        //-------------------------------------
        //-----------------------------------------------
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(handCursor);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setCursor(defaultCursor);
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        u.borderColor = Color.RED;
                        Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
                        setStrokeForVertex(g);
                        u.drawVertexBorder(g);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (u != null) {
                    u.borderColor = Color.BLACK;
                    u = null;
                    repaint();
                }
            }
        });
        //-----------------------------------------------
        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent me) {

            }

            @Override
            public void mouseDragged(MouseEvent me) {
                if (u != null) {
                    u.ellipse.setFrame(me.getX() - u.getWidth() / 2.0,
                            me.getY() - u.getWidth() / 2.0,
                            u.getWidth(), u.getWidth());
                    repaint();
                }
            }

        });
    }

    private void drawGraph(Graphics2D g) {
        Iterator<Edge2D> edges = graph.edgeIterator();
        while (edges.hasNext()) {
            Edge2D e = edges.next();
            e.draw(g);
        }
        setStrokeForVertex(g);
        setFont(g, (int) getVertexSize() / 2);
        for (Vertex v : graph) {
            v.draw(g);
        }
    }

    public final void generateCoordinatesForVertices() {
        if (!graph.isEmpty()) {
            double dl = 360.0 / graph.verticesNum(), a = 0.0,
                    r = getHeight() / 3.0;
            for (Vertex v : graph) {
                double x1 = r * Math.cos(Math.PI * a / 180);
                double y1 = r * Math.sin(Math.PI * a / 180);
                v.ellipse = new Ellipse2D.Double(x1 + getWidth() / 2.0, y1 + getHeight() / 2.0,
                        getVertexSize(), getVertexSize());
                a += dl;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (isAddEdgeMode && u != null) {
            g2d.draw(new Line2D.Double(u.getCenterX(), u.getCenterY(), x, y));
        }
        drawGraph(g2d);
    }

}