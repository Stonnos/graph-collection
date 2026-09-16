/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import graphcollection.graph.AdjacencyMatrixGraph;
import graphcollection.graph.Graph;
import graphcollection.graph.HashSetGraph;
import graphcollection.graph.ListSetGraph;
import graphcollection.graph.SortedSetGraph;
import graphcollection.graph.TreeSetGraph;

import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Рома
 */
public class RandomGraph {

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction) {
        return generate(gView, direction, 0, 0);
    }

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction, int V, int E) {
        if (V < 0 || E < 0) {
            throw new IllegalArgumentException("Argument must be positive");
        }
        Graph<Vertex, Edge2D> g = create(gView, direction);
        Vertex[] vertex = generateVertices(g, V);
        generateEdges(g, vertex, E);
        return g;
    }

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction, int V, int E, int a, int b) {
        Graph<Vertex, Edge2D> g = generate(gView, direction, V, E);
        generateIntWeights(g, a, b);
        return g;
    }

    public Graph<Vertex, Edge2D> generate(GraphView gView, boolean direction, int V, int E, double a, double b) {
        Graph<Vertex, Edge2D> g = generate(gView, direction, V, E);
        generateDoubleWeights(g, a, b);
        return g;
    }

    private void checkWeights(double a, double b) {
        if (b <= a) {
            throw new IllegalArgumentException(String.valueOf(b) + " must be higher then "
                    + String.valueOf(a));
        }
    }

    private double k(Graph<Vertex, Edge2D> g) {
        double a = g.direction() ? 1.0 : 2.0;
        if (g.verticesNum() <= 1) {
            return 1.0;
        } else {
            return a * g.edgesNum() / (g.verticesNum() * (g.verticesNum() - 1));
        }
    }

    private Graph<Vertex, Edge2D> create(GraphView gView, boolean direction) {
        Graph<Vertex, Edge2D> g = null;
        switch (gView) {
            case HASH_SET_GRAPH:
                g = new HashSetGraph<>(direction);
                break;
            case TREE_SET_GRAPH:
                g = new TreeSetGraph<>(direction);
                break;
            case SORTED_SET_GRAPH:
                g = new SortedSetGraph<>(direction);
                break;
            case LIST_SET_GRAPH:
                g = new ListSetGraph<>(direction);
                break;
            case MATRIX_GRAPH:
                g = new AdjacencyMatrixGraph<>(direction);
                break;
        }
        return g;
    }

    private Vertex[] generateVertices(Graph<Vertex, Edge2D> g, int V) {
        Vertex[] vertex = new Vertex[V];
        for (int i = 0; i < V; i++) {
            Vertex v = new Vertex(String.valueOf(i));
            g.addVertex(v);
            vertex[i] = v;
        }
        return vertex;
    }

    private void generateEdges(Graph<Vertex, Edge2D> g, Vertex[] vertex, int E) {
        Random r = new Random();
        while (E != 0 && k(g) != 1.0) {
            int u = r.nextInt(g.verticesNum()), v = r.nextInt(g.verticesNum());
            if (g.addEdge(new Edge2D(g.direction(), vertex[u], vertex[v]))) {
                E--;
            }
        }
    }

    public void generateIntWeights(Graph<Vertex, Edge2D> g, int a, int b) {
        checkWeights(a, b);
        Iterator<Edge2D> edges = g.edgeIterator();
        Random r = new Random();
        while (edges.hasNext()) {
            Edge2D e = edges.next();
            e.setWeight(Math.abs(r.nextInt()) % (b - a) + a);
        }
    }

    public void generateDoubleWeights(Graph<Vertex, Edge2D> g, double a, double b) {
        checkWeights(a, b);
        Iterator<Edge2D> edges = g.edgeIterator();
        Random r = new Random();
        while (edges.hasNext()) {
            Edge2D e = edges.next();
            e.setWeight(r.nextDouble() * (b - a) + a);
        }
    }

} //End of class RandomGraph
