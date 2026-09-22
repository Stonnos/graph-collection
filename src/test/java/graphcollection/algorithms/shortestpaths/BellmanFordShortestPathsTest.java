package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BellmanFordShortestPathsTest {

    @Test
    void testShortestPaths() {
        // example from page 673 Kormen_Tomas_Algoritmy_postroenie_i_analiz
        Graph<Vertex, Edge2D> graph = readGraph("bellman_ford_spt.json");
        Vertex source = new Vertex("s");
        BellmanFordShortestPaths<Vertex, Edge2D> dijkstraShortestPaths = new BellmanFordShortestPaths<>(graph, source);
        assertEquals(2d, dijkstraShortestPaths.getDistance(new Vertex("t")).doubleValue());
        assertEquals(7d, dijkstraShortestPaths.getDistance(new Vertex("y")).doubleValue());
        assertEquals(4d, dijkstraShortestPaths.getDistance(new Vertex("x")).doubleValue());
        assertEquals(-2d, dijkstraShortestPaths.getDistance(new Vertex("z")).doubleValue());
    }
}
