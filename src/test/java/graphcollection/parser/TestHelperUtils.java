package graphcollection.parser;

import graphcollection.graph.Graph;
import graphcollection.gui.model.Edge2D;
import graphcollection.gui.model.Vertex;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URL;

@UtilityClass
public class TestHelperUtils {

    private static final String GRAPH_TESTS_DIR = "graph_tests/%s";

    public static File getGraphFile(String fileName) {
        ClassLoader classLoader = TestHelperUtils.class.getClassLoader();
        URL resource = classLoader.getResource(GRAPH_TESTS_DIR.formatted(fileName));
        return new File(resource.getFile());
    }

    public static Graph<Vertex, Edge2D> readGraph(String fileName) {
        File file = getGraphFile(fileName);
        JsonGraphReader reader = new JsonGraphReader();
        return reader.read(file);
    }
}
