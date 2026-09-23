package graphcollection.algorithms.dfs;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static graphcollection.TestHelperUtils.readGraph;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TopologicalSortTest {

    @Test
    void testTopologicalSort() {
        Graph<Vertex, Edge2D> graph = readGraph("topological_sort.json");

        Vertex r = new Vertex("r");
        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        Vertex x = new Vertex("x");
        Vertex y = new Vertex("y");
        Vertex z = new Vertex("z");

        TopologicalSort<Vertex> topologicalSort = new TopologicalSort<>(graph);
        assertEquals(List.of(r, s, t, x, y, z), topologicalSort.sequence());
    }
}
