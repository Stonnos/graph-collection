package graphcollection.algorithms.dfs;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GamiltonTourSearchTest {

    @Test
    void testGamiltonTourSearch() {
        Graph<Vertex, Edge2D> graph = readGraph("gamiltonTour.json");
        GamiltonTourSearch<Vertex, Edge2D> dGamiltonTourSearch = new GamiltonTourSearch<>(graph);
        assertThat(dGamiltonTourSearch.decision()).isTrue();
        assertEquals(graph.verticesNum(), dGamiltonTourSearch.tour().size() - 1);
        assertEquals(dGamiltonTourSearch.tour().getLast(), dGamiltonTourSearch.tour().getFirst());
    }
}
