package graphcollection.algorithms.cc;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphcollection.TestHelperUtils.readGraph;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StronglyConnectedComponentsTest {

    @Test
    void testStronglyConnectedComponents() {
        Graph<Vertex, Edge2D> graph = readGraph("scc.json");
        StronglyConnectedComponents<Vertex> stronglyConnectedComponents = new StronglyConnectedComponents<>(graph);
        var groupedComponents = stronglyConnectedComponents.components()
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        mapping(Map.Entry::getKey, toList()))
                );
        assertEquals(4, groupedComponents.size());
        assertThat(groupedComponents.get(1)).hasSameElementsAs(List.of(
                new Vertex("a"), new Vertex("b"), new Vertex("e")
        ));
        assertThat(groupedComponents.get(2)).hasSameElementsAs(List.of(
                new Vertex("c"), new Vertex("d")
        ));
        assertThat(groupedComponents.get(3)).hasSameElementsAs(List.of(
                new Vertex("f"), new Vertex("g")
        ));
        assertThat(groupedComponents.get(4)).hasSameElementsAs(List.of(
                new Vertex("h")
        ));
    }
}
