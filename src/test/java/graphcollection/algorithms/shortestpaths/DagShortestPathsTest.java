package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


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

        DAGShortestPaths<Vertex, Edge2D> dagShortestPaths = new DAGShortestPaths<>(graph, s);

        assertEquals(2d, dagShortestPaths.getDistance(t).doubleValue());
        assertEquals(5d, dagShortestPaths.getDistance(y).doubleValue());
        assertEquals(6d, dagShortestPaths.getDistance(x).doubleValue());
        assertEquals(3d, dagShortestPaths.getDistance(z).doubleValue());

        // 2. Verification of vertex sequences in paths (from s to target)
        // Path to an unreachable vertex does not exist (returns empty or null depending on implementation)
        assertNull(dagShortestPaths.getPath(new Vertex("r")));

        // Path to itself consists only of the source vertex
        assertEquals(List.of(s), dagShortestPaths.getPath(s));

        // Shortest path s -> t
        assertEquals(List.of(s, t), dagShortestPaths.getPath(t));

        // Shortest path s -> x
        assertEquals(List.of(s, x), dagShortestPaths.getPath(x));

        // Shortest path s -> x -> y (weight: 6 - 1 = 5)
        assertEquals(List.of(s, x, y), dagShortestPaths.getPath(y));

        // Shortest path s -> x -> y -> z (weight: 6 - 1 - 2 = 3)
        assertEquals(List.of(s, x, y, z), dagShortestPaths.getPath(z));
    }
}
