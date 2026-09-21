package graphcollection.gui.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GraphModel {
    private boolean directed;
    private List<VertexModel> vertices = new ArrayList<>();
    private List<EdgeModel> edges = new ArrayList<>();
}
