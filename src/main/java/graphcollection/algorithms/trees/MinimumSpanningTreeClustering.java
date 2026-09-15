/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.trees;

import graphcollection.algorithms.GraphAlgorithm;
import graphcollection.graph.AdjacencyMatrixGraph;
import graphcollection.graph.Graph;
import graphcollection.graph.WeightedEdge;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Рома
 */
public class MinimumSpanningTreeClustering implements GraphAlgorithm {

    private int clustersSize;
    private double[][] distances;
    private HashMap<Integer, Integer> clusters;

    public MinimumSpanningTreeClustering(int clustersSize, double[][] distances) {
        if (distances == null) {
            throw new NullPointerException();
        }
        if (clustersSize < 0 || clustersSize > distances.length) {
            throw new IllegalArgumentException("Illegal clustersSize: "
                    + String.valueOf(clustersSize));
        }
        checkDistances(distances);
        //------------------------------------------------------
        this.clustersSize = clustersSize;
        this.distances = distances;
        Graph<Integer, WeightedEdge<Integer, Double>> graph = createGraph(this.distances);
        createClusters(graph);
    }

    public Map<Integer, Integer> clusters() {
        return clusters;
    }

    public int clustersSize() {
        return clustersSize;
    }

    public int find(Map<Integer, Integer> p, int x) {
        if (x != p.get(x)) {
            p.put(x, find(p, p.get(x)));
        }
        return p.get(x);
    }

    private Graph<Integer, WeightedEdge<Integer, Double>> createGraph(double[][] distances) {
        Integer[] v = new Integer[distances.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = i;
        }
        //--------------------------------------------------------
        Graph<Integer, WeightedEdge<Integer, Double>> graph
                = new AdjacencyMatrixGraph<>(false, v);
        //---------------------------------------------------------
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                graph.addEdge(new WeightedEdge<>(graph.direction(), i, j, distances[i][j]));
            }
        }
        return graph;
    }

    private void createClusters(Graph<Integer, WeightedEdge<Integer, Double>> graph) {
        PrimMinimumSpanningTree<Integer, WeightedEdge<Integer, Double>> mst
                = new PrimMinimumSpanningTree<>(graph, 0);
        Map<Integer, Integer> p = mst.predecessors();
        Map<Integer, Number> d = mst.distances();
        //--------------------------------------------------
        Map<Integer, Integer> clusterRepresentative = new HashMap<>();
        clusterRepresentative.put(0, 0);
        //--------------------------------------------------
        for (int i = 1; i < clustersSize(); i++) {
            double max = -Double.MAX_VALUE;
            int root = 0;
            //--------------------------------
            for (int v : graph) {
                double r = d.get(v).doubleValue();
                if (max < r) {
                    max = r;
                    root = v;
                }
            }
            //-------------------------------
            p.put(root, root);
            d.put(root, -Double.MAX_VALUE);
            clusterRepresentative.put(root, i);
        }
        //-----------------------------------------------
        clusters = new HashMap<>();
        for (int v : graph) {
            clusters.put(v, clusterRepresentative.get(find(p, v)));
        }
    }

    private void checkDistances(double[][] distances) {
        int size = distances.length;
        if (size == 0) {
            throw new IllegalArgumentException("Illegal MatrixSize: "
                    + String.valueOf(size));
        }
        for (int i = 0; i < size; i++) {
            if (distances[i].length != size) {
                throw new IllegalArgumentException("Illegal matrix's size");
            }
        }
    }

    @Override
    public String toString() {
        final String separator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clustersSize(); i++) {
            result.append("C").append(String.valueOf(i)).append(" = {");
            boolean flag = false;
            for (Map.Entry<Integer, Integer> entry : clusters().entrySet()) {
                if (entry.getValue() == i) {
                    if (flag) {
                        result.append(",");
                    }
                    result.append(entry.getKey());
                    flag = true;
                }
            }
            result.append("}").append(separator);
        }
        return result.toString();
    }

} //End of class MinimumSpanningTreeClustering
