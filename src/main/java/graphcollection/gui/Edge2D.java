/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui;

import graphcollection.graph.WeightedEdge;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 *
 * @author Рома
 */
public class Edge2D extends WeightedEdge<Vertex, Number> {

    public static final Color default_color = Color.DARK_GRAY;
    public static final Color default_weight_color = Color.RED;
    public Color color = default_color;
    public Color weightColor = default_weight_color;
    public static final int default_dimension = 1;
    public int dimension = default_dimension;

    public Edge2D(boolean direction, Vertex source, Vertex target) {
        super(direction, source, target);
    }

    public Edge2D(boolean direction, Vertex source, Vertex target, Number weight) {
        super(direction, source, target, weight);
    }

    public Edge2D(boolean direction, Vertex source,
            Vertex target, Color color) {
        this(direction, source, target);
        this.color = color;
    }

    public String print() {
        return "(" + source() + "," + target() + ")";
    }

    private void drawArrow(Graphics2D g) {
        //Вычисление границы вершины через подобные треугольники
        double r = Math.sqrt(Math.pow(target().getCenterX() - source().getCenterX(), 2)
                + Math.pow(target().getCenterY() - source().getCenterY(), 2));
        //-------------------------------------------------------
        double dx = target().getWidth()
                * (Math.abs(target().getCenterX() - source().getCenterX())) / (2 * r);
        double dy = target().getWidth()
                * (Math.abs(target().getCenterY() - source().getCenterY())) / (2 * r);
        //------------------------------------------------------
        if (source().getCenterX() > target().getCenterX()) {
            dx = -dx;
        }
        if (source().getCenterY() < target().getCenterY()) {
            dy = -dy;
        }
        //-----------------------------------------------------
        double x = target().getCenterX() - dx;
        double y = target().getCenterY() + dy;
        r = Math.sqrt(Math.pow(x - source().getCenterX(), 2)
                + Math.pow(y - source().getCenterY(), 2));
        double vx = (target().getCenterX() - source().getCenterX()) / r; //нормировка вектора
        double vy = (target().getCenterY() - source().getCenterY()) / r; //нормировка вектора
        double x3 = x - r * vx / 8;  //вычисление коорд. x точки, лежащей на дуге
        double y3 = y - r * vy / 8;  //вычисление коорд. y точки, лежащей на дуге
        //вычисление граничных точек наконечника
        double x4 = x3 + source().getWidth() * vy / 10;
        double y4 = y3 - source().getWidth() * vx / 10;
        double x5 = x3 - source().getWidth() * vy / 10;
        double y5 = y3 + source().getWidth() * vx / 10;
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x4, y4);
        path.lineTo(x5, y5);
        path.closePath();
        g.fill(path);
    }

    public void drawWeight(Graphics2D g) {
        if (getWeight() != null) {
            double r = Math.sqrt(Math.pow(target().getCenterX() - source().getCenterX(), 2)
                    + Math.pow(target().getCenterY() - source().getCenterY(), 2));
            double vx = (target().getCenterX() - source().getCenterX()) / r; //нормировка вектора
            double vy = (target().getCenterY() - source().getCenterY()) / r; //нормировка вектора
            double x = target().getCenterX() - r * vx / 3;  //вычисление коорд. x точки, лежащей на дуге
            double y = target().getCenterY() - r * vy / 3;  //вычисление коорд. y точки, лежащей на дуге
            //----------------------------------------------          
            g.setPaint(weightColor);
            g.drawString(getWeight().toString(), (float) x, (float) y);
            //---------------------------------------------
        }
    }

    public void draw(Graphics2D g) {
        g.setPaint(color);
        g.setStroke(new BasicStroke(dimension));
        g.draw(new Line2D.Double(source().getCenterX(), source().getCenterY(),
                target().getCenterX(), target().getCenterY()));
        if (direction()) {
            drawArrow(g);
        }
        drawWeight(g);
    }
}
