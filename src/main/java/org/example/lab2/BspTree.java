package org.example.lab2;

import lombok.NoArgsConstructor;
import org.example.lab2.figure.Figure;

import java.util.*;

@NoArgsConstructor
public class BspTree {
    List<Figure> figures = new ArrayList<>();
    BspTreeNode root;

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public BspTreeNode buildTree() {
        root = build(figures);
        return root;
    }

    public Set<Figure> find(int x, int y, double threshold) {
        var result = new HashSet<Figure>();
        find(x, y, threshold, result, root);
        return result;
    }

    private void find(double x, double y, double threshold, Set<Figure> result, BspTreeNode node) {
        if (node == null) return;
        for (var figure : node.coplanarFigures) {
            if (figure.distance(x, y) <= threshold) {
                result.add(figure);
            }
        }
        for (var figure : node.figures) {
            if (figure.distance(x, y) <= threshold) {
                result.add(figure);
            }
        }
        var eval = node.splitter.eval(x, y);
        var distanceToSplitter = node.splitter.distance(x, y);
        if (distanceToSplitter <= threshold) {
            find(x, y, threshold, result, node.front);
            find(x, y, threshold, result, node.back);
        } else if (eval > 0) {
            find(x, y, threshold, result, node.front);
        } else if (eval < 0) {
            find(x, y, threshold, result, node.back);
        }
    }

    private BspTreeNode build(List<Figure> figures) {
        if (figures.isEmpty()) return null;
        var splitter = findSplitter(figures);
        var coplanar = new ArrayList<Figure>();
        var front = new ArrayList<Figure>();
        var back = new ArrayList<Figure>();
        for (var figure : figures) {
            switch (figure.getPosition(splitter)) {
                case FRONT -> front.add(figure);
                case BACK -> back.add(figure);
                case COPLANAR, SPANNING -> coplanar.add(figure);
            }
        }

        var node = new BspTreeNode(splitter, null, null, coplanar, new ArrayList<>());
        if (front.size() == figures.size() || back.size() == figures.size()) {
            node.figures.addAll(front);
            node.figures.addAll(back);
            return node;
        }
        node.front = build(front);
        node.back = build(back);
        return node;
    }

    private Line findSplitter(List<Figure> figures) {
        double bestScore = Double.MAX_VALUE;
        Line bestLine = null;
        for (var figure : figures) {
            var line = figure.getRepresentativeLine();
            var score = calculateScore(figures, line);
            if (score < bestScore) {
                bestScore = score;
                bestLine = line;
            }
        }

        return bestLine;
    }

    private double calculateScore(List<Figure> figures, Line splitter) {
        int front = 0;
        int back = 0;
        int coplanar = 0;
        for (var figure : figures) {
            var pos = figure.getPosition(splitter);
            switch (pos) {
                case FRONT -> front++;
                case BACK -> back++;
                case COPLANAR -> coplanar++;
                case SPANNING -> coplanar += 2;
            }
        }
        return Math.abs(front - back) + coplanar * 0.5;
    }


    public void clear() {
        figures.clear();
        root = null;
    }
    /*
    Что должна уметь система:
🔤Помещать географически расположенные объекты
🔤Искать объекты по координатам — очевидно, не по точным (тут бы хватило любого KV), а близким

В работе также требуется:
🔤Написать бенчмарки - изменение времени работы алгоритма в зависимости от размера данных (размер БД, запрашиваемые ключи и точность)
     */


}
