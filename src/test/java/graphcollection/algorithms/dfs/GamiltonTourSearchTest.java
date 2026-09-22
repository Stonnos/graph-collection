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

class GamiltonTourSearchTest {

    @Test
    void testGamiltonTourSearch() {
        Graph<Vertex, Edge2D> graph = readGraph("gamiltonTour.json");
        GamiltonTourSearch<Vertex, Edge2D> dGamiltonTourSearch = new GamiltonTourSearch<>(graph);

        assertThat(dGamiltonTourSearch.decision()).isTrue();

        var tour = dGamiltonTourSearch.tour();
        // 1. Basic cycle structure checks
        assertEquals(graph.verticesNum(), tour.size() - 1);
        assertEquals(tour.getLast(), tour.getFirst());
        // 2. Vertex-by-vertex validation (each vertex must be visited exactly once)
        Set<Vertex> visitedVertices = new HashSet<>();

        // We iterate up to tour.size() - 1 to exclude the last returning vertex from unique check
        for (int i = 0; i < tour.size() - 1; i++) {
            Vertex current = tour.get(i);
            Vertex next = tour.get(i + 1);

            // Optional: Verify that consecutive vertices are actually connected in the graph
            Edge2D edge = graph.edge(current, next);
            assertThat(edge)
                    .withFailMessage("No valid edge exists between %s and %s to support the tour path", current, next)
                    .isNotNull();
            // Verify that the vertex hasn't been visited yet
            boolean isNewVertex = visitedVertices.add(current);
            assertTrue(isNewVertex, "Vertex " + current + " was visited more than once in the tour!");
        }
        // Verify that all vertices from the graph were included in the tour
        assertEquals(graph.verticesNum(), visitedVertices.size(),
                "Not all graph vertices were included in the tour");
    }
}
