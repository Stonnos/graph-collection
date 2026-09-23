package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraShortestPathsTest {

    @Test
    void testShortestPaths() {
        // example from page 682 Kormen_Tomas_Algoritmy_postroenie_i_analiz
        Graph<Vertex, Edge2D> graph = readGraph("dijkstra_spt.json");
        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");
        DijkstraShortestPaths<Vertex, Edge2D> dijkstraShortestPaths = new DijkstraShortestPaths<>(graph, s);
        assertEquals(8d, dijkstraShortestPaths.getDistance(t).doubleValue());
        assertEquals(5d, dijkstraShortestPaths.getDistance(y).doubleValue());
        assertEquals(9d, dijkstraShortestPaths.getDistance(x).doubleValue());
        assertEquals(7d, dijkstraShortestPaths.getDistance(z).doubleValue());

        assertEquals(List.of(s, y), dijkstraShortestPaths.getPath(y));
        assertEquals(List.of(s, y, z), dijkstraShortestPaths.getPath(z));
        assertEquals(List.of(s, y, t), dijkstraShortestPaths.getPath(t));
        assertEquals(List.of(s, y, t, x), dijkstraShortestPaths.getPath(x));
    }
}
