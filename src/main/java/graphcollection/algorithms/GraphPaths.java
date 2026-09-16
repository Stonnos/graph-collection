package graphcollection.algorithms;

import java.util.Collection;
import java.util.Map;

public interface GraphPaths<V> {

    Map<V, V> predecessors();

    Map<V, Number> distances();

    Collection<V> getPath(V target);

    V getSource();

    boolean isPath(V target);

    V getPredecessor(V target);

    Number getDistance(V target);
}
