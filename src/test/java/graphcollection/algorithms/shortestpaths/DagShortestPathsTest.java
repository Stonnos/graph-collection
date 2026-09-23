package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;


class DagShortestPathsTest {

    @Test
    void testShortestPaths() {
        // Example from page 677 of "Introduction to Algorithms" by Cormen, Leiserson, Rivest, Stein (CLRS)
        Graph<Vertex, Edge2D> graph = readGraph("dag_spt.json");

        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");

        DAGShortestPaths<Vertex, Edge2D> ddagShortestPaths = new DAGShortestPaths<>(graph, s);

        assertEquals(2d, ddagShortestPaths.getDistance(t).doubleValue());
        assertEquals(5d, ddagShortestPaths.getDistance(y).doubleValue());
        assertEquals(6d, ddagShortestPaths.getDistance(x).doubleValue());
        assertEquals(3d, ddagShortestPaths.getDistance(z).doubleValue());

        // Shortest path s -> t
        assertEquals(List.of(s, t), ddagShortestPaths.getPath(t));

        // Shortest path s -> x
        assertEquals(List.of(s, x), ddagShortestPaths.getPath(x));

        // Shortest path s -> x -> y (weight: 6 - 1 = 5)
        assertEquals(List.of(s, x, y), ddagShortestPaths.getPath(y));

        // Shortest path s -> x -> y -> z (weight: 6 - 1 - 2 = 3)
        assertEquals(List.of(s, x, y, z), ddagShortestPaths.getPath(z));
    }
}
