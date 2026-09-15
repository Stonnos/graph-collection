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
        double srcX = source().getCenterX();
        double srcY = source().getCenterY();
        double tgtX = target().getCenterX();
        double tgtY = target().getCenterY();

        // 1. Вектор от исходной вершины к целевой
        double dx = tgtX - srcX;
        double dy = tgtY - srcY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < 1) return; // Защита от деления на ноль, если центры совпали

        // 2. Нормированный вектор направления (единичный вектор)
        double vx = dx / dist;
        double vy = dy / dist;

        // 3. Вычисление точки (x, y) на границе целевой вершины
        // Если вершины круглые, радиус target — это половина ширины
        double targetRadius = target().getWidth() / 2.0;
        double x = tgtX - vx * targetRadius;
        double y = tgtY - vy * targetRadius;

        // 4. Параметры геометрии самого наконечника стрелки
        double arrowLength = dist / 8.0; // Длина наконечника
        if (arrowLength > 15) arrowLength = 15; // Ограничение, чтобы стрелка не была огромной
        double arrowWidth = arrowLength * 0.6;  // Ширина раскрытия стрелки

        // Точка основания стрелки (отступает назад по вектору направления)
        double x3 = x - vx * arrowLength;
        double y3 = y - vy * arrowLength;

        // 5. Вычисление перпендикулярного вектора для "крыльев" стрелки
        // (vy, -vx) — перпендикуляр к направлению
        double x4 = x3 + vy * arrowWidth;
        double y4 = y3 - vx * arrowWidth;

        double x5 = x3 - vy * arrowWidth;
        double y5 = y3 + vx * arrowWidth;

        // 6. Отрисовка стрелки
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);   // Остриё на границе вершины
        path.lineTo(x4, y4); // Левое крыло
        path.lineTo(x5, y5); // Правое крыло
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
