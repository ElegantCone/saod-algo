package org.example.lab2.figure;

import lombok.AllArgsConstructor;
import org.example.lab2.Line;
import org.example.lab2.Point;

import java.util.List;

@AllArgsConstructor
public class Polygon implements Figure {
    List<Point> points;

    @Override
    public Position getPosition(Line splitter) {
        int front = 0;
        int back = 0;
        for (var point : points) {
            double eval = splitter.eval(point.x(), point.y());
            if (eval == 0) {
                continue;
            }
            if (eval > 0) {
                if (back > 0) return Position.SPANNING;
                front++;
            } else {
                if (front > 0) return Position.SPANNING;
                back++;
            }
        }
        if (front > 0) return Position.FRONT;
        if (back > 0) return Position.BACK;
        return Position.COPLANAR;
    }

    @Override
    public double distance(double x, double y) {
        if (inside(x, y)) return 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            var a = points.get(i);
            var b = points.get((i + 1) % points.size());
            min = Math.min(min, distanceToSegmentSquared(x, y, a, b));
        }
        return Math.sqrt(min);
    }

    @Override
    public boolean inside(double x, double y) {
        int count = 0;
        for (int i = 0; i < points.size(); i++) {
            var a = points.get(i);
            var b = points.get((i + 1) % points.size());
            if (pointOnBorderLine(x, y, a, b)) {
                return true;
            }
            if ((a.y() > y && b.y() > y) || (a.y() < y && b.y() < y) || a.y() == b.y()) {
                continue;
            }
            //горизонтальный луч
            var lineX = (b.x() - a.x()) * (y - a.y()) / (b.y() - a.y()) + a.x();
            if (x < lineX) {
                count++;
            }
        }
        return count % 2 == 1;
    }

    @Override
    public Line getRepresentativeLine() {
        var first = points.get(0);
        var second = points.get(points.size() - 1);
        var a = first.y() - second.y();
        var b = second.x() - first.x();
        return new Line(a, b, -(a * first.x() + b * first.y()));
    }

    private double distanceToSegmentSquared(double x, double y, Point a, Point b) {
        var dx = b.x() - a.x();
        var dy = b.y() - a.y();
        var d = dx * dx + dy * dy;
        if (dx == 0 && dy == 0) {
            return d;
        }
        var t = ((x - a.x()) * dx + (y - a.y()) * dy) / d;
        t = Math.max(0, Math.min(1, t));
        var projectionX = a.x() + t * dx;
        var projectionY = a.y() + t * dy;
        return squaredDistance(x, y, projectionX, projectionY);
    }

    private double squaredDistance(double x1, double y1, double x2, double y2) {
        var dx = x1 - x2;
        var dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    private boolean pointOnBorderLine(double x, double y, Point a, Point b) {
        //точка лежит на прямой
        var cross = (x - a.x()) * (b.y() - a.y()) - (y - a.y()) * (b.x() - a.x());
        if (Math.abs(cross) > 1e-9) {
            return false;
        }
        //точка не выходит за границы отрезка
        var minX = Math.min(a.x(), b.x());
        var maxX = Math.max(a.x(), b.x());
        var minY = Math.min(a.y(), b.y());
        var maxY = Math.max(a.y(), b.y());
        return maxX >= x && x >= minX && maxY >= y && y >= minY;
    }
}
