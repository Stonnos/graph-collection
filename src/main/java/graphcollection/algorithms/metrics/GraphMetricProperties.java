/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.algorithms.metrics;

import graphcollection.algorithms.shortestpaths.AllPairsShortestPaths;
import graphcollection.algorithms.shortestpaths.ShortestPaths;
import graphcollection.graph.WeightedEdge;

import java.util.Map;

/**
 *
 * @author Рома
 */
public class GraphMetricProperties {

    /**
     *
     * @param <V>
     * @param <E>
     * @param allSpt
     * @return
     */
    public static <V, E extends WeightedEdge<V, ? extends Number>>
    Number diametr(AllPairsShortestPaths<V, E> allSpt) {
        if (allSpt != null && allSpt.decision()) {
            double diametr = -Double.MAX_VALUE;
            for (V u : allSpt.distancesMatrix()) {
                for (V v : allSpt.distancesMatrix()) {
                    double x = allSpt.getDistance(u, v).doubleValue();
                    if (allSpt.isPath(u, v) && x > diametr) {
                        diametr = x;
                    }
                }
            }
            //-------------------------------
            return diametr;
        } else {
            return null;
        }
    }

    /**
     *
     * @param <V>
     * @param <E>
     * @param allSpt
     * @return
     */
    public static <V, E extends WeightedEdge<V, ? extends Number>>
    Number radius(AllPairsShortestPaths<V, E> allSpt) {
        if (allSpt != null && allSpt.decision()) {
            double radius = Double.MAX_VALUE;
            for (V u : allSpt.distancesMatrix()) {
                double max = -Double.MAX_VALUE;
                for (V v : allSpt.distancesMatrix()) {
                    double x = allSpt.getDistance(u, v).doubleValue();
                    if (allSpt.isPath(u, v) && x > max) {
                        max = x;
                    }
                }
                //---------------------------------
                if (max < radius) {
                    radius = max;
                }
            }
            //-------------------------------------
            return radius;
        } else {
            return null;
        }
    }

    /**
     *
     * @param <V>
     * @param <E>
     * @param spt
     * @return
     */
    public static <V, E extends WeightedEdge<V, ? extends Number>>
    Number eccentricity(ShortestPaths<V, E> spt) {
        if (spt != null && spt.decision()) {
            double eccentricity = -Double.MAX_VALUE;
            for (Map.Entry<V, Number> entry : spt.distances().entrySet()) {
                if (spt.isPath(entry.getKey())
                        && entry.getValue().doubleValue() > eccentricity) {
                    eccentricity = entry.getValue().doubleValue();
                }
            }
            return eccentricity;
        } else {
            return null;
        }
    }
}
