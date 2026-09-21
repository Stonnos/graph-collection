package graphcollection.gui;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.GraphView;
import graphcollection.gui.model.Vertex;
import graphcollection.gui.parse.NumberParser;
import graphcollection.gui.text.DoubleDocument;
import graphcollection.gui.text.VertexNameDocument;
import graphcollection.gui.util.ButtonUtils;
import graphcollection.gui.util.GuiUtils;
import graphcollection.gui.util.PanelBorderUtils;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static graphcollection.gui.model.Edge2D.WEIGHT_FONT_SIZE;
import static graphcollection.gui.util.GuiUtils.showToolTipProgrammatically;

public class JGraphDrawer extends JPanel {

    public static final int MAX_VERTEX_COUNT = 100;
    public static final int VERTEX_NAME_LENGTH = 10;
    private static final double FORCE_DETECTED_AREA_PADDING = 15.0; // Отступ от краев рамок
    private static final int EDGE_WEIGHT_TEXT_LENGTH = 8;
    private static final int POPUP_MARGIN = 50;
    private static final int MAX_VERTEX_COUNT_EXCEEDED_ERROR_MESSAGE_MARGIN_TOP = 25;
    private static final int CIRCLE_COORDINATES_VERTICES_THRESHOLD = 25;
    private static final int FORCE_DIRECTED_ITERATIONS = 150;
    private int vertexSize = 40;
    private Graph<Vertex, Edge2D> graph; //граф
    private GraphView graphView;
    private final Map<String, Vertex> vertexIdMap = new HashMap<>(); //словарь имен вершин
    private final Map<String, Vertex> vertexNameMap = new HashMap<>();
    private Vertex u; //вспомогательная вершина;
    private int x; //координаты мыши;
    private int y; //координаты мыши;
    private boolean isAddEdgeMode; //режим (вставка ребра/др.)вес ребра
    private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private int dwidth, dheight; //вспомогательные поля для хранения предыдущего размера окна
    private final PopupFactory popupFactory = PopupFactory.getSharedInstance();
    private final Random random = new Random();
    private EdgeWeightPopup edgeWeightPopup;
    private VertexNamePopup vertexNamePopup;
    private Popup errorPopup;

    private final AtomicBoolean hasVertexError = new AtomicBoolean();

    @Getter
    @Setter
    private ActionListener vertexErrorListener;
    @Getter
    @Setter
    private ActionListener updateGraphListener;
    @Getter
    @Setter
    private boolean manuallySetVertexName;
    @Getter
    @Setter
    private boolean manuallySetEdgeWeight;

    public JGraphDrawer() {
        this(GraphView.HASH_SET_GRAPH, false, 0, 0);
    }

    public JGraphDrawer(boolean direction) {
        this(GraphView.HASH_SET_GRAPH, direction, 0, 0);
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
        GraphGenerator r = new GraphGenerator();
        r.generateIntWeights(graph, a, b);
    }

    public JGraphDrawer(GraphView gView, boolean direction, int V, int E, double a, double b) {
        this(gView, direction, V, E);
        GraphGenerator r = new GraphGenerator();
        r.generateDoubleWeights(graph, a, b);
    }

    public void generate(GraphView gView, boolean direction, int V, int E) {
        checkVertexCount(V);
        clear();
        this.createGraph(gView, direction, V, E);
    }

    public void generate(GraphView gView, boolean direction, int V, int E, int a, int b) {
        this.generate(gView, direction, V, E);
        GraphGenerator r = new GraphGenerator();
        r.generateIntWeights(graph, a, b);
    }

    public void generate(GraphView gView, boolean direction, int V, int E, double a, double b) {
        this.generate(gView, direction, V, E);
        GraphGenerator r = new GraphGenerator();
        r.generateDoubleWeights(graph, a, b);
    }

    public void read(File file) {
        JsonGraphReader graphReader = new JsonGraphReader();
        Graph<Vertex, Edge2D> g = graphReader.read(file);
        fillGraph(g);
    }

    public void transform(GraphView gView) {
        if (!graphView().equals(gView)) {
            GraphGenerator r = new GraphGenerator();
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

    public Vertex vertex(String v) {
        return vertexNameMap.get(v);
    }

    public void clear() {
        graph.clear();
        vertexIdMap.clear();
        vertexNameMap.clear();
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

    private void createGraph(GraphView gView, boolean direction, int V, int E) {
        GraphGenerator r = new GraphGenerator();
        graph = r.generate(gView, direction, V, E);
        fillVerticesMap();
        this.generateCoordinatesForceDirected();
        graphView = gView;
    }

    private void fillGraph(Graph<Vertex, Edge2D> g) {
        if (g.verticesNum() > MAX_VERTEX_COUNT) {
            g.clear();
            throw new IllegalArgumentException("Количество вершин не должно превышать " + MAX_VERTEX_COUNT);
        }
        clear();
        graph = g;
        graphView = GraphView.HASH_SET_GRAPH;
        fillVerticesMap();
        this.generateCoordinatesForceDirected();
        repaint();
    }

    private void fillVerticesMap() {
        for (Vertex v : graph) {
            vertexIdMap.put(v.getId(), v);
            vertexNameMap.put(v.getName(), v);
        }
    }

    private void notifyUpdateGraphEvent() {
        Optional.ofNullable(updateGraphListener).ifPresent(actionListener ->
                actionListener.actionPerformed(new ActionEvent(this, 0, null)));
    }

    private double getVertexSize() {
        return vertexSize;
    }

    private void checkVertexCount(int V) {
        if (V > MAX_VERTEX_COUNT) {
            throw new IllegalArgumentException("Количество вершин не должно превышать " + MAX_VERTEX_COUNT);
        }
    }

    private class VertexDrawer extends SwingWorker<Void, Void> {
        private final Vertex v;
        private final boolean manuallySetName;
        private static final int FLASH_COUNT = 6;

        public VertexDrawer(Vertex v, boolean manuallySetName) {
            this.v = v;
            this.manuallySetName = manuallySetName;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Graphics2D g = (Graphics2D) JGraphDrawer.this.getGraphics();
            try {
                setStrokeForVertex(g);
                setFont(g, (int) v.getWidth() / 2);
                double x = v.getX(), y = v.getY(), r = v.getWidth();
                for (int i = FLASH_COUNT; i >= 1; i--) {
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
            } catch (InterruptedException ignored) {
            }
            v.drawVertexName(g);
            repaint();
            return null;
        }

        @Override
        protected void done() {
            if (manuallySetName) {
                vertexNamePopup = new VertexNamePopup(v);
                vertexNamePopup.show();
            }
        }

    }

    private void setStrokeForVertex(Graphics2D g) {
        g.setStroke(new BasicStroke(3));
    }

    private void setFont(Graphics2D g, int size) {
        Font font = new Font("Arial", Font.BOLD, size);
        g.setFont(font);
    }

    private Vertex searchVertex(int x, int y) {
        for (Vertex v : graph) {
            if (v.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    public void reset() {
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
        hideVertexNamePopup();
        hideEdgeWeightPopup();
    }

    public void startAddVertex() {
        reset();
        this.addMouseListener(new MouseListener() {

            private Vertex generateVertex(int x, int y) {
                String nextVertexName = generateVertexName();
                return new Vertex(nextVertexName, new Ellipse2D.Double(x, y, getVertexSize(), getVertexSize()));
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
                if (me.getButton() == MouseEvent.BUTTON1 && !hasVertexError.get()) {
                    hideVertexNamePopup();
                    if (graph.verticesNum() == MAX_VERTEX_COUNT) {
                        createErrorMessagePopup("Превышено максимальное число вершин: %s!".formatted(MAX_VERTEX_COUNT),
                                me.getX(), me.getY() + MAX_VERTEX_COUNT_EXCEEDED_ERROR_MESSAGE_MARGIN_TOP);
                    } else {
                        Vertex v = searchVertex(me.getX(), me.getY());
                        if (v == null) {
                            v = generateVertex(me.getX(), me.getY());
                            graph.addVertex(v);
                            vertexIdMap.put(v.getId(), v);
                            vertexNameMap.put(v.getName(), v);
                            VertexDrawer vertexDrawer = new VertexDrawer(v, manuallySetVertexName);
                            vertexDrawer.execute();
                            notifyUpdateGraphEvent();
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
    }

    public void startRemoveVertex() {
        reset();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        vertexIdMap.remove(u.getId());
                        vertexNameMap.remove(u.getName());
                        graph.removeVertex(u);
                        notifyUpdateGraphEvent();
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
    }

    public void startRemoveOutEdges() {
        reset();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        graph.removeOutEdges(u);
                        notifyUpdateGraphEvent();
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
    }

    public void startRemoveInEdges() {
        reset();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
                if (me.getButton() == MouseEvent.BUTTON1) {
                    u = searchVertex(me.getX(), me.getY());
                    if (u != null) {
                        graph.removeInEdges(u);
                        notifyUpdateGraphEvent();
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
    }

    public void startAddEdge() {
        reset();
        isAddEdgeMode = true;
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
                hideErrorPopup();
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
                        Edge2D edge2D = new Edge2D(graph.direction(), u, v);
                        if (u.getId().equals(v.getId())) {
                            createErrorMessagePopup("Не допускается создание петель!",
                                    (int) (v.getX() + POPUP_MARGIN), (int) (v.getY() + POPUP_MARGIN));
                        } else if (!graph.addEdge(edge2D)) {
                            createErrorMessagePopup(
                                    String.format("Ребро между вершинами %s и %s уже существует!", u.getName(),
                                            v.getName()),
                                    (int) (v.getX() + POPUP_MARGIN), (int) (v.getY() + POPUP_MARGIN));
                        } else if (manuallySetEdgeWeight) {
                            edgeWeightPopup = new EdgeWeightPopup(edge2D);
                            edgeWeightPopup.show();
                        }
                    }
                    notifyUpdateGraphEvent();
                    u.borderColor = Color.BLACK;
                    u = null;
                    repaint();
                }
            }
        });
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

    public void startRemoveEdge() {
        reset();
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
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
                                createErrorMessagePopup(String.format("Ребра между вершинами %s и %s не существует!",
                                                u.getName(), z.getName()),
                                        (int) (z.getX() + POPUP_MARGIN), (int) (z.getY() + POPUP_MARGIN));
                            }
                            notifyUpdateGraphEvent();
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
    }

    public void startUpdateEdge() {
        reset();
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                hideErrorPopup();
                if (me.getButton() == MouseEvent.BUTTON1) {
                    hideEdgeWeightPopup();
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
                                createErrorMessagePopup(String.format("Ребра между вершинами %s и %s не существует!",
                                                u.getName(), z.getName()),
                                        (int) (z.getX() + POPUP_MARGIN), (int) (z.getY() + POPUP_MARGIN));
                            } else {
                                edgeWeightPopup = new EdgeWeightPopup(e);
                                edgeWeightPopup.show();
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
    }


    public void startMoveGraph() {
        reset();
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
                hideErrorPopup();
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

    public final void generateCoordinatesCircle() {
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

    public final void generateCoordinatesForceDirected() {
        if (graph.isEmpty()) {
            return;
        }
        if (graph.verticesNum() <= CIRCLE_COORDINATES_VERTICES_THRESHOLD) {
            generateCoordinatesCircle();
            return;
        }
        // Идеально использовать круговую расстановку в качестве стартовой позиции
        generateCoordinatesCircle();

        int verticesNum = graph.verticesNum();
        double width = getWidth();
        double height = getHeight();

        double area = width * height;
        double k = Math.sqrt(area / (double) verticesNum) * 0.75;

        Map<Vertex, Double> dispX = new HashMap<>();
        Map<Vertex, Double> dispY = new HashMap<>();

        int iterations = FORCE_DIRECTED_ITERATIONS;
        double temp = width / 10.0;
        double coolingFactor = temp / iterations;

        for (int iter = 0; iter < iterations; iter++) {
            initializeDisplacements(dispX, dispY);
            calculateRepulsionForces(k, dispX, dispY);
            calculateAttractionForces(k, dispX, dispY);
            applyDisplacements(temp, width, height, dispX, dispY);
            temp -= coolingFactor;
        }
    }

    /**
     * Сбрасывает смещения для всех вершин графа в начале итерации.
     */
    private void initializeDisplacements(Map<Vertex, Double> dispX, Map<Vertex, Double> dispY) {
        for (Vertex v : graph) {
            dispX.put(v, 0.0);
            dispY.put(v, 0.0);
        }
    }

    /**
     * Вычисляет силы отталкивания между всеми парами вершин.
     */
    private void calculateRepulsionForces(double k, Map<Vertex, Double> dispX, Map<Vertex, Double> dispY) {
        for (Vertex v : graph) {
            double vX = v.ellipse.getCenterX();
            double vY = v.ellipse.getCenterY();

            for (Vertex u : graph) {
                if (v == u) {
                    continue;
                }
                double dx = vX - u.ellipse.getCenterX();
                double dy = vY - u.ellipse.getCenterY();
                double distance = Math.hypot(dx, dy);

                if (distance > 0) {
                    double fr = (k * k) / distance;
                    dispX.put(v, dispX.get(v) + (dx / distance) * fr);
                    dispY.put(v, dispY.get(v) + (dy / distance) * fr);
                }
            }
        }
    }

    /**
     * Вычисляет силы притяжения вдоль ребер графа.
     */
    private void calculateAttractionForces(double k, Map<Vertex, Double> dispX, Map<Vertex, Double> dispY) {
        Iterator<Edge2D> edge2DIterator = graph.edgeIterator();
        while (edge2DIterator.hasNext()) {
            Edge2D edge = edge2DIterator.next();
            Vertex v = edge.source();
            Vertex u = edge.target();

            double dx = v.ellipse.getCenterX() - u.ellipse.getCenterX();
            double dy = v.ellipse.getCenterY() - u.ellipse.getCenterY();
            double distance = Math.hypot(dx, dy);

            if (distance > 0) {
                double fa = (distance * distance) / k;
                double deltaX = (dx / distance) * fa;
                double deltaY = (dy / distance) * fa;

                dispX.put(v, dispX.get(v) - deltaX);
                dispY.put(v, dispY.get(v) - deltaY);
                dispX.put(u, dispX.get(u) + deltaX);
                dispY.put(u, dispY.get(u) + deltaY);
            }
        }
    }

    /**
     * Применяет накопленные смещения к вершинам с учетом текущей температуры и границ экрана.
     */
    private void applyDisplacements(double temp, double width, double height,
                                    Map<Vertex, Double> dispX, Map<Vertex, Double> dispY) {
        double vertexSize = getVertexSize();
        double halfSize = vertexSize / 2.0;

        for (Vertex v : graph) {
            double dX = dispX.get(v);
            double dY = dispY.get(v);
            double dispDist = Math.hypot(dX, dY);
            if (dispDist > 0) {
                double limitedDist = Math.min(dispDist, temp);
                double targetX = v.ellipse.getCenterX() + (dX / dispDist) * limitedDist;
                double targetY = v.ellipse.getCenterY() + (dY / dispDist) * limitedDist;
                // Вычисляем левый верхний угол, как требует setFrame
                double posX = targetX - halfSize;
                double posY = targetY - halfSize;
                // Ограничиваем posX и posY, чтобы левый и верхний край жестко соблюдали PADDING
                posX = Math.max(
                        FORCE_DETECTED_AREA_PADDING, Math.min(width - vertexSize - FORCE_DETECTED_AREA_PADDING, posX));
                posY = Math.max(
                        FORCE_DETECTED_AREA_PADDING, Math.min(height - vertexSize - FORCE_DETECTED_AREA_PADDING, posY));
                v.ellipse.setFrame(posX, posY, vertexSize, vertexSize);
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

    private class EdgeWeightPopup {

        Edge2D edge2D;
        Popup popup;
        JTextField edgeWeightText;

        EdgeWeightPopup(Edge2D edge2D) {
            this.edge2D = edge2D;
        }

        void show() {
            JPanel infoPanel = createInputTextPanel();
            double targetX = edge2D.target().getCenterX();
            double targetY = edge2D.target().getCenterY();
            double sourceX = edge2D.source().getCenterX();
            double sourceY = edge2D.source().getCenterY();

            double dx = targetX - sourceX;
            double dy = targetY - sourceY;
            double r = Math.sqrt(dx * dx + dy * dy);
            if (r == 0) {
                r = 1; // Защита от деления на ноль
            }
            double vx = dx / r;
            double vy = dy / r;
            // Вычисляем вектор перпендикуляра (нормаль), смотрящий в одну из сторон от ребра
            double nx = -vy;
            double ny = vx;
            // 2. Параметры смещения текста
            double distanceFromTarget;
            double sideOffset;

            if (!edge2D.direction()) {
                // Для неориентированных: строго по центру ребра (r / 2)
                distanceFromTarget = r / 2;
                // Сдвиг вбок на 12 пикселей, чтобы текст был РЯДОМ с ребром, а не НА нем
                sideOffset = 12;
            } else {
                // Для ориентированных/двунаправленных: ближе к целевой вершине
                distanceFromTarget = r / 2.5; // Чуть дальше от вершины, чем r/3, чтобы не прижималось близко
                sideOffset = 15;              // Сдвиг вбок, чтобы веса «туда» и «обратно» разъехались
            }
            // 3. Расчет итоговых координат точки для текста
            double x = targetX - distanceFromTarget * vx + nx * sideOffset - 10;
            double y = targetY - distanceFromTarget * vy + ny * sideOffset - 10;

            Point point = new Point((int) x, (int) y);
            SwingUtilities.convertPointToScreen(point, JGraphDrawer.this);
            this.popup = popupFactory.getPopup(JGraphDrawer.this, infoPanel,
                    (int) point.getX(), (int) point.getY());
            popup.show();
            edgeWeightText.requestFocusInWindow();
        }

        void hide() {
            updatedWeight();
            Optional.ofNullable(popup).ifPresent(Popup::hide);
        }

        JPanel createInputTextPanel() {
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.WHITE);
            edgeWeightText = new JTextField(2);
            edgeWeightText.setBackground(Color.WHITE);
            edgeWeightText.setFont(new Font("Arial", Font.PLAIN, WEIGHT_FONT_SIZE));
            edgeWeightText.setDocument(new DoubleDocument(EDGE_WEIGHT_TEXT_LENGTH));
            if (edge2D.getWeight() != null) {
                edgeWeightText.setText(String.valueOf(edge2D.getWeight()));
            }
            edgeWeightText.addActionListener(e -> {
                hide();
            });
            infoPanel.add(edgeWeightText, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            return infoPanel;
        }

        void updatedWeight() {
            if (edgeWeightText.getText() != null && !edgeWeightText.getText().isEmpty()) {
                Number newWeight = NumberParser.parse(edgeWeightText.getText());
                edge2D.setWeight(newWeight);
            }
        }
    }

    private class VertexNamePopup {
        static final int VERTEX_NAME_TEXT_FIELD_PADDING = 2;
        static final int VERTEX_NAME_FONT_SIZE = 20;
        Vertex vertex;
        Popup popup;
        JTextField vertexNameText;

        VertexNamePopup(Vertex vertex) {
            this.vertex = vertex;
        }

        void show() {
            JPanel infoPanel = createInputTextPanel();
            Point point = new Point((int) vertex.getX(), (int) vertex.getY());
            SwingUtilities.convertPointToScreen(point, JGraphDrawer.this);
            double radius = (double) vertexSize / 2;
            int fieldSize = (int) (radius * Math.sqrt(2));
            int popupX = (int) point.getX() + fieldSize / 4;
            int popupY = (int) point.getY() + fieldSize / 4;
            this.popup = popupFactory.getPopup(JGraphDrawer.this, infoPanel, popupX, popupY);
            popup.show();
            vertexNameText.requestFocusInWindow();
        }

        void hide() {
            if (updatedDisplayName()) {
                Optional.ofNullable(popup).ifPresent(Popup::hide);
            }
        }

        JPanel createInputTextPanel() {
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.WHITE);
            vertexNameText = new JTextField();
            double radius = (double) vertexSize / 2;
            int fieldSize = (int) (radius * Math.sqrt(2)) - VERTEX_NAME_TEXT_FIELD_PADDING;
            Dimension dimension = new Dimension(fieldSize, fieldSize);
            vertexNameText.setMinimumSize(dimension);
            vertexNameText.setPreferredSize(dimension);
            vertexNameText.setMaximumSize(dimension);
            vertexNameText.setBorder(BorderFactory.createEmptyBorder());
            vertexNameText.setHorizontalAlignment(JTextField.CENTER);
            vertexNameText.setFont(new Font("Arial", Font.BOLD, VERTEX_NAME_FONT_SIZE));
            vertexNameText.setBackground(Color.WHITE);
            vertexNameText.setDocument(new VertexNameDocument(VERTEX_NAME_LENGTH));
            vertexNameText.setText(vertex.getName());
            vertexNameText.addActionListener(e -> {
                hide();
            });
            infoPanel.add(vertexNameText, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            return infoPanel;
        }

        boolean updatedDisplayName() {
            if (!GuiUtils.isEmpty(vertexNameText)) {
                if (!Objects.equals(vertex.getName(), vertexNameText.getText())
                        && vertexNameMap.containsKey(vertexNameText.getText())) {
                    vertexNameText.setForeground(Color.RED);
                    vertexNameText.setToolTipText(String.format("Вершина %s существует!", vertexNameText.getText()));
                    showToolTipProgrammatically(vertexNameText);
                    hasVertexError.set(true);
                    Optional.ofNullable(vertexErrorListener).ifPresent(actionListener ->
                            actionListener.actionPerformed(
                                    new ActionEvent(this, 0, hasVertexError.toString())));
                    return false;
                } else {
                    vertexNameMap.remove(vertex.getName());
                    vertex.setName(vertexNameText.getText());
                    vertexNameMap.put(vertexNameText.getText(), vertex);
                    hasVertexError.set(false);
                    hideErrorPopup();
                    Optional.ofNullable(vertexErrorListener)
                            .ifPresent(actionListener -> actionListener.actionPerformed(
                                    new ActionEvent(this, 0, hasVertexError.toString())));
                    return true;
                }
            }
            return true;
        }
    }

    private void createErrorMessagePopup(String message, int x, int y) {
        hideErrorPopup();
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(PanelBorderUtils.createEtchedBorder());
        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.RED);
        JButton closeButton = ButtonUtils.createCloseButton();
        infoPanel.add(messageLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                new Insets(0, 5, 0, 5), 0, 0));
        infoPanel.add(closeButton, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(4, 0, 4, 0), 0, 0));
        Point point = new Point(x, y);
        SwingUtilities.convertPointToScreen(point, JGraphDrawer.this);
        Popup popup = popupFactory.getPopup(JGraphDrawer.this, infoPanel, (int) point.getX(), (int) point.getY());
        closeButton.addActionListener(evt -> {
            popup.hide();
        });
        popup.show();
        errorPopup = popup;
    }

    private void hideErrorPopup() {
        Optional.ofNullable(errorPopup).ifPresent(Popup::hide);
    }

    private void hideVertexNamePopup() {
        Optional.ofNullable(vertexNamePopup).ifPresent(VertexNamePopup::hide);
        vertexNamePopup = null;
    }

    private void hideEdgeWeightPopup() {
        Optional.ofNullable(edgeWeightPopup).ifPresent(EdgeWeightPopup::hide);
        edgeWeightPopup = null;
    }

    private String generateVertexName() {
        String nextVertexName;
        do {
            nextVertexName = String.valueOf(random.nextInt(99));
        }
        while (vertexIdMap.containsKey(nextVertexName));
        return nextVertexName;
    }
}