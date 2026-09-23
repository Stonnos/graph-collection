package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BellmanFordShortestPathsTest {

    @Test
    void testShortestPaths() {
        // example from page 673 Kormen_Tomas_Algoritmy_postroenie_i_analiz
        Graph<Vertex, Edge2D> graph = readGraph("bellman_ford_spt.json");
        Vertex source = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");
        BellmanFordShortestPaths<Vertex, Edge2D> shortestPaths = new BellmanFordShortestPaths<>(graph, source);
        assertEquals(2d, shortestPaths.getDistance(t).doubleValue());
        assertEquals(7d, shortestPaths.getDistance(y).doubleValue());
        assertEquals(4d, shortestPaths.getDistance(x).doubleValue());
        assertEquals(-2d, shortestPaths.getDistance(z).doubleValue());

        assertEquals(List.of(source), shortestPaths.getPath(source));
        assertEquals(List.of(source, y), shortestPaths.getPath(y));
        assertEquals(List.of(source, y, x), shortestPaths.getPath(x));
        assertEquals(List.of(source, y, x, t), shortestPaths.getPath(t));
        assertEquals(List.of(source, y, x, t, z), shortestPaths.getPath(z));
    }
}
