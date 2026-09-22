package graphcollection.algorithms.metrics;

import graphcollection.algorithms.shortestpaths.DijkstraShortestPaths;
import graphcollection.algorithms.shortestpaths.FloydWarshallAllPairsShortestPaths;
import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphMetricPropertiesTest {

    @Test
    void testMetrics() {
        Graph<Vertex, Edge2D> graph = readGraph("metrics_graph.json");
        FloydWarshallAllPairsShortestPaths<Vertex, Edge2D> shortestPaths =
                new FloydWarshallAllPairsShortestPaths<>(graph);
        assertEquals(16d, GraphMetricProperties.diametr(shortestPaths).doubleValue());
        assertEquals(8.5d, GraphMetricProperties.radius(shortestPaths).doubleValue());
        assertEquals(9.0d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("A"))));
        assertEquals(9.5d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("B"))));
        assertEquals(16.0d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("C"))));
        assertEquals(12.0d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("D"))));
        assertEquals(10.5d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("E"))));
        assertEquals(8.5d,
                GraphMetricProperties.eccentricity(new DijkstraShortestPaths<>(graph, new Vertex("F"))));
    }
}
