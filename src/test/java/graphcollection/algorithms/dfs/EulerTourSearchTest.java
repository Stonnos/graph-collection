package graphcollection.algorithms.dfs;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EulerTourSearchTest {

    @Test
    void testEulerTourSearch() {
        Graph<Vertex, Edge2D> graph = readGraph("eulerTour.json");
        EulerTourSearch<Vertex, Edge2D> eulerTourSearch = new EulerTourSearch<>(graph.copy());
        assertThat(eulerTourSearch.decision()).isTrue();
        assertEquals(graph.edgesNum(), eulerTourSearch.tour().size() - 1);
        assertEquals(eulerTourSearch.tour().getLast(), eulerTourSearch.tour().getFirst());
    }
}
