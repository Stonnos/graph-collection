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
    private final String name;
    @Getter
    @Setter
    private String displayName;

    public static final Color default_color = Color.WHITE;
    public static final Color default_border_color = Color.BLACK;
    public static final Color default_name_color = Color.BLUE;
    public Color color = default_color;
    public Color borderColor = default_border_color;
    public Color nameColor = default_name_color;
    public Ellipse2D.Double ellipse;

    public Vertex(String name) {
        this.name = name;
        this.displayName = name;
    }

    public Vertex(String name, Color color) {
        this(name);
        this.color = color;
    }

    public Vertex(String name, Ellipse2D.Double ellipse) {
        this(name);
        this.ellipse = ellipse;
    }

    public Vertex(String name, Color color, Ellipse2D.Double ellipse) {
        this(name, color);
        this.ellipse = ellipse;
    }

    public boolean contains(double x, double y) {
        return ellipse.contains(x, y);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Vertex v) {
        return name.compareTo(v.name);
    }
    
    @Override
    public boolean equals(Object obj) {
         if (this == obj)
             return true;
         if (obj instanceof Vertex) {
             return ((Vertex)obj).name.equals(name);
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
        g.drawString(displayName, (float) getX() +
                        ((float) getWidth() - fm.stringWidth(displayName)) / 2,
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