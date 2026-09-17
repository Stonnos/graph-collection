package graphcollection.gui;

import graphcollection.graph.Edge;
import graphcollection.graph.Graph;
import graphcollection.graph.HashSetGraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphParser {

    private static final String VERTEX_FORMAT = "[a-zA-Zа-яА-Я0-9]{1,10}";
    private static final String WEIGHT_FORMAT = "(([-]?(([0-9]+)|([0-9]+[.][0-9]+)))|)";
    private static final String EDGE_FORMAT = "^[(]" + VERTEX_FORMAT + ","
            + VERTEX_FORMAT + "[,]?" + WEIGHT_FORMAT + "[)]$";

    public <V, E extends Edge<V>> void write(Graph<V, E> g, String fileName) {
        try (FileOutputStream out = new FileOutputStream(fileName);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            if (g.direction()) {
                writer.write("directed");
            } else {
                writer.write("undirected");
            }
            writer.newLine();
            for (V u : g) {
                writer.write(u.toString());
                writer.newLine();
            }
            Iterator<E> edge = g.edgeIterator();
            while (edge.hasNext()) {
                writer.write(edge.next().toString());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isVertex(String v) {
        Pattern p = Pattern.compile("^" + VERTEX_FORMAT + "$");
        Matcher m = p.matcher(v);
        return m.matches();
    }

    private boolean isEdge(String e) {
        Pattern p = Pattern.compile(EDGE_FORMAT);
        Matcher m = p.matcher(e);
        return m.matches();
    }

    public Graph<Vertex, Edge2D> read(String fileName) {
        Graph<Vertex, Edge2D> g;
        try (FileInputStream in = new FileInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            //-----------------------------------------------------
            String line = reader.readLine();
            if (line != null && line.equals("directed")) {
                g = new HashSetGraph<>(true);
            } else if (line != null && line.equals("undirected")) {
                g = new HashSetGraph<>(false);
            } else {
                throw new NumberFormatException("Тип графа не указан!");
            }
            //------------------------------------------------------
            LinkedList<String> elements = new LinkedList<String>();
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    elements.add(line);
                }
            }
            //------------------------------------------------------
            HashMap<String, Vertex> vmap = new HashMap<>();
            ListIterator<String> iter = elements.listIterator();
            while (iter.hasNext()) {
                String val = iter.next();
                if (isVertex(val)) {
                    if (!vmap.containsKey(val)) {
                        Vertex u = new Vertex(val);
                        vmap.put(val, u);
                        g.addVertex(u);
                    }
                    iter.remove();
                } else if (!isEdge(val)) {
                    elements.clear();
                    g.clear();
                    throw new NumberFormatException("Ошибка в задании графа!");
                }
            }
            //------------------------------------------------------
            for (String edge : elements) {
                String u, v, w = null;
                int first = edge.indexOf(','), last = edge.lastIndexOf(',');
                u = edge.substring(1, first);
                if (first == last) {
                    v = edge.substring(first + 1, edge.length() - 1);
                } else {
                    v = edge.substring(first + 1, last);
                    w = edge.substring(last + 1, edge.length() - 1);
                }
                Vertex v1 = vmap.get(u), v2 = vmap.get(v);
                if (v1 != null && v2 != null) {
                    g.addEdge(new Edge2D(g.direction(), v1, v2,
                            NumberParser.parse(w)));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return g;
    }
}
