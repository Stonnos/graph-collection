package graphcollection.graph;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectedGraphTest {

    static Stream<Graph<Integer, Edge<Integer>>> graphProvider() {
        return Stream.of(
                new AdjacencyMatrixGraph<>(true),
                new ListSetGraph<>(true)
        );
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testAddVertex(Graph<Integer, Edge<Integer>> graph) {
        assertTrue(graph.addVertex(1));
        assertFalse(graph.addVertex(1)); // Дубликат не должен добавляться
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testRemoveVertex(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        assertTrue(graph.removeVertex(1));
        assertFalse(graph.removeVertex(2)); // Несуществующий вершина не должна удаляться
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testAddEdge(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        assertTrue(graph.addEdge(new Edge<>(true, 1, 2)));
        assertTrue(graph.addEdge(new Edge<>(true, 2, 1)));
        assertFalse(graph.addEdge(new Edge<>(true, 3, 4))); // Несуществующие вершины
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testRemoveEdge(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertEquals(1, graph.removeEdge(new Edge<>(true, 1, 2)));
        assertEquals(0, graph.edgesNum());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testContainsVertex(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        assertTrue(graph.containsVertex(1));
        assertFalse(graph.containsVertex(2));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testContainsEdge(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(new Edge<>(true, 1, 2));
        graph.addEdge(new Edge<>(true, 1, 3));
        assertTrue(graph.containsEdge(new Edge<>(true, 1, 2)));
        assertFalse(graph.containsEdge(new Edge<>(true, 2, 1)));
        assertFalse(graph.containsEdge(new Edge<>(true, 3, 4)));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testVerticesNum(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        assertEquals(2, graph.verticesNum());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testEdgesNum(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(new Edge<>(true, 1, 2));
        graph.addEdge(new Edge<>(true, 1, 3));
        assertEquals(2, graph.edgesNum());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testIsEmpty(Graph<Integer, Edge<Integer>> graph) {
        assertTrue(graph.isEmpty());
        graph.addVertex(1);
        assertFalse(graph.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testClearEdges(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(new Edge<>(true, 1, 2));
        graph.addEdge(new Edge<>(true, 1, 3));
        assertEquals(2, graph.edgesNum());
        graph.clearEdges();
        assertEquals(0, graph.edgesNum());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testClear(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertEquals(2, graph.verticesNum());
        assertEquals(1, graph.edgesNum());
        graph.clear();
        assertTrue(graph.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testOutEdgesNum(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertEquals(1, graph.outEdgesNum(1));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testInEdgesNum(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertEquals(1, graph.inEdgesNum(2));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testAdjacentVerticesNum(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertEquals(1, graph.adjacentVerticesNum(1));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testRemoveOutEdges(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertTrue(graph.removeOutEdges(1));
        assertEquals(0, graph.outEdgesNum(1));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testRemoveInEdges(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(new Edge<>(true, 1, 2));
        assertTrue(graph.removeInEdges(2));
        assertEquals(0, graph.inEdgesNum(2));
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testAdjacencyIterator(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(new Edge<>(true, 1, 2));
        graph.addEdge(new Edge<>(true, 3, 1));
        Iterator<Integer> iterator = graph.adjacencyIterator(1);
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    void testOutEdgesIterator(Graph<Integer, Edge<Integer>> graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addEdge(new Edge<>(true, 1, 2));
        graph.addEdge(new Edge<>(true, 3, 1));
        graph.addEdge(new Edge<>(true, 1, 4));
        graph.addEdge(new Edge<>(true, 5, 1));
        Iterator<Edge<Integer>> iterator = graph.outEdgeIterator(1);
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().target());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().target());
        assertFalse(iterator.hasNext());
    }
}