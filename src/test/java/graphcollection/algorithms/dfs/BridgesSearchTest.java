package graphcollection.algorithms.dfs;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgesSearchTest {

    @Test
    void testBridgesSearch() {
        Graph<Vertex, Edge2D> graph = readGraph("bridges.json");
        BridgesSearch<Vertex, Edge2D> bridgesSearch = new BridgesSearch<>(graph);
        assertThat(bridgesSearch.bridges()).hasSize(1);
        Edge2D bridge = bridgesSearch.bridges().iterator().next();
        assertEquals("C", bridge.source().getId());
        assertEquals("D", bridge.target().getId());
    }
}
