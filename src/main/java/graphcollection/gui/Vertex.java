/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Рома
 */
public class Vertex implements Comparable<Vertex>, java.io.Serializable {
    @Getter
    private final String id;
    @Getter
    @Setter
    private String name;

    public static final Color default_color = Color.WHITE;
    public static final Color default_border_color = Color.BLACK;
    public static final Color default_name_color = Color.BLUE;
    public Color color = default_color;
    public Color borderColor = default_border_color;
    public Color nameColor = default_name_color;
    public Ellipse2D.Double ellipse;

    public Vertex(String id) {
        this.id = id;
        this.name = id;
    }

    public Vertex(String id, Color color) {
        this(id);
        this.color = color;
    }

    public Vertex(String id, Ellipse2D.Double ellipse) {
        this(id);
        this.ellipse = ellipse;
    }

    public Vertex(String id, Color color, Ellipse2D.Double ellipse) {
        this(id, color);
        this.ellipse = ellipse;
    }

    public boolean contains(double x, double y) {
        return ellipse.contains(x, y);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(Vertex v) {
        return id.compareTo(v.id);
    }
    
    @Override
    public boolean equals(Object obj) {
         if (this == obj)
             return true;
         if (obj instanceof Vertex) {
             return ((Vertex)obj).id.equals(id);
         }
         else return false;
    }

    public double getX() {
        return ellipse.getX();
    }

    public double getY() {
        return ellipse.getY();
    }

    public double getWidth() {
        return ellipse.getWidth();
    }

    public double getHeight() {
        return ellipse.getHeight();
    }

    public double getCenterX() {
        return ellipse.getCenterX();
    }

    public double getCenterY() {
        return ellipse.getCenterY();
    }

    public void drawVertexName(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        g.setPaint(nameColor);
        g.drawString(name, (float) getX() +
                        ((float) getWidth() - fm.stringWidth(name)) / 2,
                (float) getY() + fm.getAscent() +
                        ((float) getWidth() - (fm.getAscent() + fm.getDescent())) / 2);
    }

    public void drawVertex(Graphics2D g) {
        g.setPaint(color);
        g.fill(ellipse);
    }

    public void drawVertexBorder(Graphics2D g) {
        g.setPaint(borderColor);
        g.draw(ellipse);
    }

    public void draw(Graphics2D g) {
        drawVertex(g);
        drawVertexBorder(g);
        drawVertexName(g);
    }
}