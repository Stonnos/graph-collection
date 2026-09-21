package graphcollection.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.EdgeModel;
import graphcollection.gui.model.GraphModel;
import graphcollection.gui.model.Vertex;
import graphcollection.gui.model.VertexModel;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Iterator;
import java.util.Optional;

public class JsonGraphWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public void write(File file, Graph<Vertex, Edge2D> graph) {
        GraphModel graphModel = new GraphModel();
        graphModel.setDirected(graph.direction());
        for (Vertex vertex : graph) {
            graphModel.getVertices().add(new VertexModel(vertex.getId(), vertex.getName()));
        }
        Iterator<Edge2D> edgeIterator = graph.edgeIterator();
        while (edgeIterator.hasNext()) {
            Edge2D edge2D = edgeIterator.next();
            Double weight = Optional.ofNullable(edge2D.getWeight()).map(Number::doubleValue).orElse(null);
            graphModel.getEdges().add(
                    new EdgeModel(edge2D.source().getId(), edge2D.target().getId(), weight));
        }
        OBJECT_MAPPER.writeValue(file, graphModel);
    }
}
