package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraShortestPathsTest {

    @Test
    void testShortestPaths() {
        // example from page 682 Kormen_Tomas_Algoritmy_postroenie_i_analiz
        Graph<Vertex, Edge2D> graph = readGraph("dijkstra_spt.json");
        Vertex source = new Vertex("s");
        DijkstraShortestPaths<Vertex, Edge2D> dijkstraShortestPaths = new DijkstraShortestPaths<>(graph, source);
        assertEquals(8d, dijkstraShortestPaths.getDistance(new Vertex("t")).doubleValue());
        assertEquals(5d, dijkstraShortestPaths.getDistance(new Vertex("y")).doubleValue());
        assertEquals(9d, dijkstraShortestPaths.getDistance(new Vertex("x")).doubleValue());
        assertEquals(7d, dijkstraShortestPaths.getDistance(new Vertex("z")).doubleValue());
    }
}
