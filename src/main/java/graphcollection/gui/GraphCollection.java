/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import graphcollection.graph.*;
import java.util.*;
import graphcollection.algorithms.shortestpaths.*;
/**
 *
 * @author Рома
 */

public class GraphCollection {


    /**
     * @param args the command line arguments
     */   
    public static void main(String[] args) {
       JGraphFrame graph = new JGraphFrame();
       graph.setVisible(true);
       /* String[] vertices = {"A","B","C","D","E", "F"};
        AdjacencyMatrixGraph<String, WeightedEdge<String, Number>> graph =
                new AdjacencyMatrixGraph<>(true, vertices);
        Random r = new Random();
        graph.addEdge(EdgeFactory.directedWeightedEdge("A", "B", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("A", "C", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("A", "D", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("B", "D", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("B", "E", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("B", "A", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("D", "E", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("E", "C", r.nextInt(10)));
        graph.addEdge(EdgeFactory.directedWeightedEdge("D", "C", r.nextInt(10)));
        System.out.println("Graph's structure:");
        System.out.println(graph);
        AllPairsShortestPaths<String, WeightedEdge<String, Number>> spt =
                new JohnsonAllPairsShortestPaths<>(graph, "S");
        System.out.println("Distances:");
        System.out.println(spt);
        System.out.println("Paths:");
        System.out.println(spt.getPaths());
        //--------------------------------
        */
    }
    
}
