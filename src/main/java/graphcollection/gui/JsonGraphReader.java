package graphcollection.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class JsonGraphReader {

    private static final String VERTEX_NAME_FORMAT = "^[a-zA-Zа-яА-Я0-9]{1,10}$";
    private static final String INVALID_EDGE_VERTEX_ERROR_FORMAT = "Ребро (%s, %s) содержит не существующую вершину %s";
    private static final String EMPTY_EDGE_VERTEX_ERROR = "Не заданы вершины для ребра!";
    private static final String VERTEX_NAMES_NOT_UNIQUE_ERROR = "Имена вершин должны быть уникальны!";
    private static final String VERTEX_IDS_NOT_UNIQUE_ERROR = "Идентификаторы вершин должны быть уникальны!";
    private static final String EMPTY_VERTEX_NAME_ERROR = "Не задано имя для вершины!";
    private static final String EMPTY_VERTEX_ID_ERROR = "Не задан идентификатор для вершины!";
    private static final String INVALID_VERTEX_NAME_FORMAT_ERROR =
            "Имя вершины должно содержать только цифры и символы, максимум 10";

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
        validateVertices(graphModel);
        validateEdges(graphModel);
    }

    private void validateVertices(GraphModel graphModel) {
        for (VertexModel vertexModel : graphModel.getVertices()) {
            if (StringUtils.isEmpty(vertexModel.getId())) {
                throw new IllegalStateException(EMPTY_VERTEX_ID_ERROR);
            }
            if (StringUtils.isEmpty(vertexModel.getName())) {
                throw new IllegalStateException(EMPTY_VERTEX_NAME_ERROR);
            }
            if (!vertexModel.getName().matches(VERTEX_NAME_FORMAT)) {
                throw new IllegalStateException(INVALID_VERTEX_NAME_FORMAT_ERROR);
            }
        }

        List<String> vertexIds = graphModel.getVertices()
                .stream()
                .map(VertexModel::getId)
                .toList();

        boolean allIdsUnique = vertexIds.stream().distinct().count() == vertexIds.size();
        if (!allIdsUnique) {
            throw new IllegalStateException(VERTEX_IDS_NOT_UNIQUE_ERROR);
        }

        List<String> vertexNames = graphModel.getVertices()
                .stream()
                .map(VertexModel::getName)
                .toList();

        boolean allNamesUnique = vertexNames.stream().distinct().count() == vertexNames.size();
        if (!allNamesUnique) {
            throw new IllegalStateException(VERTEX_NAMES_NOT_UNIQUE_ERROR);
        }
    }

    private void validateEdges(GraphModel graphModel) {
        for (EdgeModel edgeModel : graphModel.getEdges()) {
            String source = edgeModel.getSource();
            String target = edgeModel.getTarget();
            if (StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
                throw new IllegalStateException(EMPTY_EDGE_VERTEX_ERROR);
            }
            if (!containsVertex(graphModel, source)) {
                throw new IllegalStateException(INVALID_EDGE_VERTEX_ERROR_FORMAT.formatted(source, target, source));
            }
            if (!containsVertex(graphModel, target)) {
                throw new IllegalStateException(INVALID_EDGE_VERTEX_ERROR_FORMAT.formatted(source, target, target));
            }
        }
    }

    private boolean containsVertex(GraphModel graphModel, String vertexId) {
        return graphModel.getVertices()
                .stream()
                .anyMatch(vertexModel -> vertexModel.getId().equals(vertexId));
    }
}
