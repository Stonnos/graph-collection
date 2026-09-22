package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DagShortestPathsTest {

    @Test
    void testShortestPaths() {
        // example from page 677 Kormen_Tomas_Algoritmy_postroenie_i_analiz
        Graph<Vertex, Edge2D> graph = readGraph("dag_spt.json");
        Vertex source = new Vertex("s");
        DAGShortestPaths<Vertex, Edge2D> dijkstraShortestPaths = new DAGShortestPaths<>(graph, source);
        assertEquals(2d, dijkstraShortestPaths.getDistance(new Vertex("t")).doubleValue());
        assertEquals(5d, dijkstraShortestPaths.getDistance(new Vertex("y")).doubleValue());
        assertEquals(6d, dijkstraShortestPaths.getDistance(new Vertex("x")).doubleValue());
        assertEquals(3d, dijkstraShortestPaths.getDistance(new Vertex("z")).doubleValue());
    }
}
