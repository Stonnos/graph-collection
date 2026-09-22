package graphcollection.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphcollection.exception.GraphParseError;
import graphcollection.exception.GraphParseException;
import graphcollection.graph.Graph;
import graphcollection.graph.HashSetGraph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.EdgeModel;
import graphcollection.gui.model.GraphModel;
import graphcollection.gui.model.Vertex;
import graphcollection.gui.model.VertexModel;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonGraphReader {

    private static final String VERTEX_NAME_FORMAT = "^[a-zA-Zа-яА-Я0-9]{1,10}$";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public Graph<Vertex, Edge2D> read(File file) {
        GraphModel graphModel = OBJECT_MAPPER.readValue(file, GraphModel.class);
        validateGraph(graphModel);
        Graph<Vertex, Edge2D> graph = new HashSetGraph<>(graphModel.isDirected());
        Map<String, Vertex> vertexMap = new HashMap<>();
        graphModel.getVertices().forEach(vertexModel -> {
            Vertex vertex = new Vertex(vertexModel.getId());
            vertex.setName(vertexModel.getName());
            graph.addVertex(vertex);
            vertexMap.put(vertex.getId(), vertex);
        });
        graphModel.getEdges().forEach(edgeModel -> {
            Vertex source = vertexMap.get(edgeModel.getSource());
            Vertex target = vertexMap.get(edgeModel.getTarget());
            Edge2D edge2D = new Edge2D(graphModel.isDirected(), source, target, edgeModel.getWeight());
            graph.addEdge(edge2D);
        });
        return graph;
    }

    private void validateGraph(GraphModel graphModel) {
        Set<String> validVertexIds = validateVertices(graphModel);
        validateEdges(graphModel, validVertexIds);
    }

    private Set<String> validateVertices(GraphModel graphModel) {
        Set<String> vertexIds = new HashSet<>();
        Set<String> vertexNames = new HashSet<>();
        for (VertexModel vertexModel : graphModel.getVertices()) {
            if (StringUtils.isEmpty(vertexModel.getId())) {
                throw new GraphParseException(GraphParseError.EMPTY_VERTEX_ID);
            }
            if (StringUtils.isEmpty(vertexModel.getName())) {
                throw new GraphParseException(GraphParseError.EMPTY_VERTEX_NAME);
            }
            if (!vertexModel.getName().matches(VERTEX_NAME_FORMAT)) {
                throw new GraphParseException(GraphParseError.INVALID_VERTEX_NAME_FORMAT);
            }
            if (!vertexIds.add(vertexModel.getId())) {
                throw new GraphParseException(GraphParseError.VERTEX_IDS_NOT_UNIQUE);
            }
            if (!vertexNames.add(vertexModel.getName())) {
                throw new GraphParseException(GraphParseError.VERTEX_NAMES_NOT_UNIQUE);
            }
        }
        return vertexIds;
    }

    private void validateEdges(GraphModel graphModel, Set<String> validVertexIds) {
        for (EdgeModel edgeModel : graphModel.getEdges()) {
            String source = edgeModel.getSource();
            String target = edgeModel.getTarget();
            if (StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
                throw new GraphParseException(GraphParseError.EMPTY_EDGE_VERTEX);
            }
            if (!validVertexIds.contains(source)) {
                throw new GraphParseException(GraphParseError.INVALID_EDGE_VERTEX, source, target, source);
            }
            if (!validVertexIds.contains(target)) {
                throw new GraphParseException(GraphParseError.INVALID_EDGE_VERTEX, source, target, target);
            }
        }
    }
}
