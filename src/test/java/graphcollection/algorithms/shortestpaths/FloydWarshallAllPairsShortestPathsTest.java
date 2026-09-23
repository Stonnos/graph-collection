package graphcollection.algorithms.shortestpaths;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FloydWarshallAllPairsShortestPathsTest {

    private final Vertex a = new Vertex("A");
    private final Vertex b = new Vertex("B");
    private final Vertex c = new Vertex("C");
    private final Vertex d = new Vertex("D");
    private final Vertex e = new Vertex("E");
    private final Vertex f = new Vertex("F");

    private final List<Vertex> allVertices = List.of(a, b, c, d, e, f);

    // expected distances: Map<Source, Map<Target, Distance>>
    private final Map<Vertex, Map<Vertex, Double>> expectedDistances = Map.of(
            a, Map.of(a, 0.0, b, 2.5, c, 3.5, d, 7.5, e, 9.0, f, 7.5),
            b, Map.of(a, 9.5, b, 0.0, c, 1.0, d, 5.0, e, 6.5, f, 5.0),
            c, Map.of(a, 8.5, b, 11.0, c, 0.0, d, 4.0, e, 5.5, f, 16.0),
            d, Map.of(a, 4.5, b, 7.0, c, 8.0, d, 0.0, e, 1.5, f, 12.0),
            e, Map.of(a, 3.0, b, 5.5, c, 6.5, d, 10.5, e, 0.0, f, 10.5),
            f, Map.of(a, 5.0, b, 7.5, c, 8.5, d, 0.5, e, 2.0, f, 0.0)
    );

    // expected paths map: Map<Source, Map<Target, List<Vertex>>>
    private final Map<Vertex, Map<Vertex, List<Vertex>>> expectedPaths = Map.of(
            a, Map.of(
                    a, List.of(a), b, List.of(a, b), c, List.of(a, b, c),
                    d, List.of(a, b, c, d), e, List.of(a, b, c, d, e), f, List.of(a, b, f)
            ),
            b, Map.of(
                    a, List.of(b, c, d, e, a), b, List.of(b), c, List.of(b, c),
                    d, List.of(b, c, d), e, List.of(b, c, d, e), f, List.of(b, f)
            ),
            c, Map.of(
                    a, List.of(c, d, e, a), b, List.of(c, d, e, a, b), c, List.of(c),
                    d, List.of(c, d), e, List.of(c, d, e), f, List.of(c, d, e, a, b, f)
            ),
            d, Map.of(
                    a, List.of(d, e, a), b, List.of(d, e, a, b), c, List.of(d, e, a, b, c),
                    d, List.of(d), e, List.of(d, e), f, List.of(d, e, a, b, f)
            ),
            e, Map.of(
                    a, List.of(e, a), b, List.of(e, a, b), c, List.of(e, a, b, c),
                    d, List.of(e, a, b, c, d), e, List.of(e), f, List.of(e, a, b, f)
            ),
            f, Map.of(
                    a, List.of(f, d, e, a), b, List.of(f, d, e, a, b), c, List.of(f, d, e, a, b, c),
                    d, List.of(f, d), e, List.of(f, d, e), f, List.of(f)
            )
    );

    @Test
    void testShortestPaths() {
        Graph<Vertex, Edge2D> graph = readGraph("metrics_graph.json");
        FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> shortestPaths =
                new FloydWarshallAllPairsShortestPaths<>(graph);

        for (Vertex src : allVertices) {
            for (Vertex dst : allVertices) {
                // 1. check distance
                double expectedDist = expectedDistances.get(src).get(dst);
                assertEquals(expectedDist, shortestPaths.getDistance(src, dst));
                // 2. check path
                List<Vertex> expectedPath = expectedPaths.get(src).get(dst);
                assertEquals(expectedPath, shortestPaths.getPath(src, dst));
            }
        }

    }
}
