package graphcollection.algorithms.trees;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimMinimumSpanningTreeTest {

    private static final double EXPECTED_TREE_WEIGHT = 37.0d;

    @Test
    void testTree() {
        Graph<Vertex, Edge2D> graph = readGraph("mst.json");
        PrimMinimumSpanningTree<Vertex, Edge2D> kruskalMinimumSpanningTree =
                new PrimMinimumSpanningTree<>(graph, graph.iterator().next());
        assertEquals(EXPECTED_TREE_WEIGHT, kruskalMinimumSpanningTree.getMinimumSpanningTreeWeight().doubleValue());
    }
}
