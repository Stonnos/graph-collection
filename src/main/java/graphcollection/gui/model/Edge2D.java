/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcollection.gui.model;

import graphcollection.graph.WeightedEdge;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 *
 * @author Рома
 */
public class Edge2D extends WeightedEdge<Vertex, Number> {

    public static final Color DARK_GRAY = Color.DARK_GRAY;
    public static final Color DEFAULT_WEIGHT_COLOR = Color.BLUE;
    public static final int WEIGHT_FONT_SIZE = 14;
    public static final String WEIGHT_FONT = "Arial";
    public Color color = DARK_GRAY;
    public Color weightColor = DEFAULT_WEIGHT_COLOR;
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
        return "(" + source().getName() + "," + target().getName() + ")";
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

        if (dist < 1) {
            return; // Защита от деления на ноль, если центры совпали
        }

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
        if (arrowLength > 15) {
            arrowLength = 15; // Ограничение, чтобы стрелка не была огромной
        }
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
            double targetX = target().getCenterX();
            double targetY = target().getCenterY();
            double sourceX = source().getCenterX();
            double sourceY = source().getCenterY();

            double dx = targetX - sourceX;
            double dy = targetY - sourceY;
            double r = Math.sqrt(dx * dx + dy * dy);
            if (r == 0) r = 1; // Защита от деления на ноль
            double vx = dx / r;
            double vy = dy / r;
         // Вычисляем вектор перпендикуляра (нормаль), смотрящий в одну из сторон от ребра
            double nx = -vy;
            double ny = vx;
            // 2. Параметры смещения текста
            double distanceFromTarget;
            double sideOffset;

            if (!direction()) {
                // Для неориентированных: строго по центру ребра (r / 2)
                distanceFromTarget = r / 2;
                // Сдвиг вбок на 12 пикселей, чтобы текст был РЯДОМ с ребром, а не НА нем
                sideOffset = 12;
            } else {
                // Для ориентированных/двунаправленных: ближе к целевой вершине
                distanceFromTarget = r / 2.5; // Чуть дальше от вершины, чем r/3, чтобы не прижималось близко
                sideOffset = 15;              // Сдвиг вбок, чтобы веса «туда» и «обратно» разъехались
            }
            // 3. Расчет итоговых координат точки для текста
            double x = targetX - distanceFromTarget * vx + nx * sideOffset;
            double y = targetY - distanceFromTarget * vy + ny * sideOffset;
            // 4. Отрисовка текста с центрированием
            Font oldFont = g.getFont();
            Font weightFont = new Font(WEIGHT_FONT, Font.BOLD, WEIGHT_FONT_SIZE);
            g.setFont(weightFont);
            g.setPaint(weightColor);

            FontMetrics fm = g.getFontMetrics(weightFont);
            String weightStr = getWeight().toString();
            float textWidth = fm.stringWidth(weightStr);
            float textHeight = fm.getAscent();
            // Точное центрирование bounding box текста относительно рассчитанной точки (x, y)
            float drawX = (float) (x - textWidth / 2);
            float drawY = (float) (y + textHeight / 3);
            g.drawString(weightStr, drawX, drawY);
            g.setFont(oldFont);
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
