package graphcollection.algorithms.dfs;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static graphcollection.TestHelperUtils.readGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EulerTourSearchTest {
    @Test
    void testEulerTourSearch() {
        Graph<Vertex, Edge2D> graph = readGraph("eulerTour.json");
        EulerTourSearch<Vertex, Edge2D> eulerTourSearch = new EulerTourSearch<>(graph.copy());

        assertThat(eulerTourSearch.decision()).isTrue();
        var tour = eulerTourSearch.tour();
        // 1. Basic cycle structure checks
        assertEquals(graph.edgesNum(), tour.size() - 1);
        assertEquals(tour.getLast(), tour.getFirst());
        // 2. Edge-by-edge validation (each edge must be visited exactly once)
        Set<Edge2D> visitedEdges = new HashSet<>();
        for (int i = 0; i < tour.size() - 1; i++) {
            Vertex current = tour.get(i);
            Vertex next = tour.get(i + 1);
            // Retrieve the edge between two vertices from the original graph
            Edge2D edge = graph.edge(current, next);
            // Verify that the edge actually exists in the graph
            assertThat(edge)
                    .withFailMessage("Edge between %s and %s does not exist in the original graph", current, next)
                    .isNotNull();
            // Verify that the edge hasn't been visited yet (uniqueness)
            boolean isNewEdge = visitedEdges.add(edge);
            assertTrue(isNewEdge, "Edge " + edge + " was visited more than once!");
        }
        // Verify that all edges from the graph were included in the tour
        assertEquals(graph.edgesNum(), visitedEdges.size(), "Not all graph edges were included in the tour");
    }
}
