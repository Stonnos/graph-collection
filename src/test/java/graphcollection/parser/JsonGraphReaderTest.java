package graphcollection.parser;

import graphcollection.exception.GraphParseError;
import graphcollection.exception.GraphParseException;
import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static graphcollection.parser.TestHelperUtils.getGraphFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonGraphReaderTest {

    private static final String EMPTY_VERTEX_ID_JSON = "empty_vertex_id.json";
    private static final String VALID_GRAPH_JSON = "valid_graph.json";
    private static final String EMPTY_VERTEX_NAME_JSON = "empty_vertex_name.json";
    private static final String INVALID_VERTEX_NAME_FORMAT_JSON = "invalid_vertex_name_format.json";
    private static final String VERTEX_IDS_NOT_UNIQUE_JSON = "vertex_ids_not_unique.json";
    private static final String VERTEX_NAMES_NOT_UNIQUE_JSON = "vertex_names_not_unique.json";
    private static final String EMPTY_EDGE_VERTEX_JSON = "empty_edge_vertex.json";
    private static final String INVALID_EDGE_VERTEX_JSON = "invalid_edge_vertex.json";

    private JsonGraphReader reader;

    @BeforeEach
    void setUp() {
        reader = new JsonGraphReader();
    }

    @Test
    void testValidGraph_ReturnsCorrectGraph() {
        File file = getGraphFile(VALID_GRAPH_JSON);
        Graph<Vertex, Edge2D> graph = reader.read(file);
        assertNotNull(graph);
        assertTrue(graph.direction());
        assertEquals(2, graph.verticesNum());
        assertEquals(1, graph.edgesNum());
    }

    @Test
    void testEmptyVertexIdThrowsEmptyVertexIdError() {
        File file = getGraphFile(EMPTY_VERTEX_ID_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.EMPTY_VERTEX_ID, exception.getError());
    }

    @Test
    void testEmptyVertexNameThrowsEmptyVertexNameError() {
        File file = getGraphFile(EMPTY_VERTEX_NAME_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.EMPTY_VERTEX_NAME, exception.getError());
    }

    @Test
    void testInvalidVertexNameFormatThrowsInvalidVertexNameFormatError() {
        File file = getGraphFile(INVALID_VERTEX_NAME_FORMAT_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.INVALID_VERTEX_NAME_FORMAT, exception.getError());
    }

    @Test
    void testVertexIdsNotUniqueThrowsVertexIdsNotUniqueError() {
        File file = getGraphFile(VERTEX_IDS_NOT_UNIQUE_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.VERTEX_IDS_NOT_UNIQUE, exception.getError());
    }

    @Test
    void testVertexNamesNotUniqueThrowsVertexNamesNotUniqueError() {
        File file = getGraphFile(VERTEX_NAMES_NOT_UNIQUE_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.VERTEX_NAMES_NOT_UNIQUE, exception.getError());
    }

    @Test
    void testEmptyEdgeVertexThrowsEmptyEdgeVertexError() {
        File file = getGraphFile(EMPTY_EDGE_VERTEX_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.EMPTY_EDGE_VERTEX, exception.getError());
    }

    @Test
    void testInvalidEdgeVertexThrowsInvalidEdgeVertexError() {
        File file = getGraphFile(INVALID_EDGE_VERTEX_JSON);
        GraphParseException exception = assertThrows(GraphParseException.class, () -> reader.read(file));
        assertEquals(GraphParseError.INVALID_EDGE_VERTEX, exception.getError());
    }
}
