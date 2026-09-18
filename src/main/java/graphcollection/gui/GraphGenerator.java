package graphcollection.gui;

import graphcollection.graph.AdjacencyMatrixGraph;
import graphcollection.graph.Graph;
import graphcollection.graph.HashSetGraph;
import graphcollection.graph.ListSetGraph;
import graphcollection.graph.SortedSetGraph;
import graphcollection.graph.TreeSetGraph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.GraphView;
import graphcollection.gui.model.Vertex;

import java.util.Iterator;
import java.util.Random;

public class GraphGenerator {

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction) {
        return generate(gView, direction, 0, 0);
    }

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction, int vertices, int edges) {
        if (vertices < 0 || edges < 0) {
            throw new IllegalArgumentException("Argument must be positive");
        }
        Graph<Vertex, Edge2D> g = create(gView, direction);
        Vertex[] vertex = generateVertices(g, vertices);
        generateEdges(g, vertex, edges);
        return g;
    }

    private void checkWeights(double lowerBound, double upperBound) {
        if (upperBound <= lowerBound) {
            throw new IllegalArgumentException("%s must be higher then %s".formatted(upperBound, lowerBound));
        }
    }

    private double calculateDensity(Graph<Vertex, Edge2D> graph) {
        double a = graph.direction() ? 1.0 : 2.0;
        if (graph.verticesNum() <= 1) {
            return 1.0;
        } else {
            return a * graph.edgesNum() / (graph.verticesNum() * (graph.verticesNum() - 1));
        }
    }

    private Graph<Vertex, Edge2D> create(GraphView gView, boolean direction) {
        return switch (gView) {
            case HASH_SET_GRAPH -> new HashSetGraph<>(direction);
            case TREE_SET_GRAPH -> new TreeSetGraph<>(direction);
            case SORTED_SET_GRAPH -> new SortedSetGraph<>(direction);
            case LIST_SET_GRAPH -> new ListSetGraph<>(direction);
            case MATRIX_GRAPH -> new AdjacencyMatrixGraph<>(direction);
        };
    }

    private Vertex[] generateVertices(Graph<Vertex, Edge2D> g, int vertices) {
        Vertex[] vertex = new Vertex[vertices];
        for (int i = 0; i < vertices; i++) {
            Vertex v = new Vertex(String.valueOf(i));
            g.addVertex(v);
            vertex[i] = v;
        }
        return vertex;
    }

    private void generateEdges(Graph<Vertex, Edge2D> g, Vertex[] vertex, int edges) {
        Random r = new Random();
        while (edges != 0 && calculateDensity(g) != 1.0) {
            int u = r.nextInt(g.verticesNum()), v = r.nextInt(g.verticesNum());
            if (g.addEdge(new Edge2D(g.direction(), vertex[u], vertex[v]))) {
                edges--;
            }
        }
    }

    public void generateIntWeights(Graph<Vertex, Edge2D> g, int lowerBound, int upperBound) {
        checkWeights(lowerBound, upperBound);
        Iterator<Edge2D> edges = g.edgeIterator();
        Random r = new Random();
        while (edges.hasNext()) {
            Edge2D e = edges.next();
            e.setWeight(Math.abs(r.nextInt()) % (upperBound - lowerBound) + lowerBound);
        }
    }

    public void generateDoubleWeights(Graph<Vertex, Edge2D> g, double lowerBound, double upperBound) {
        checkWeights(lowerBound, upperBound);
        Iterator<Edge2D> edges = g.edgeIterator();
        Random r = new Random();
        while (edges.hasNext()) {
            Edge2D e = edges.next();
            e.setWeight(r.nextDouble() * (upperBound - lowerBound) + lowerBound);
        }
    }

}
