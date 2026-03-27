package org.example.lab2;

import lombok.NoArgsConstructor;
import org.example.lab2.figure.Figure;

import java.util.*;

@NoArgsConstructor
public class BspTree {
    private static final int LEAF_SIZE = 8;
    private static final int MAX_SPLITTER_CANDIDATES = 16;

    List<Figure> figures = new ArrayList<>();
    BspTreeNode root;

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public BspTreeNode buildTree() {
        root = build(figures);
        return root;
    }

    public Set<Figure> find(double x, double y, double threshold) {
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
        if (figures.isEmpty()) {
            return null;
        }

        if (figures.size() <= LEAF_SIZE) {
            return new BspTreeNode(
                    figures.get(0).getRepresentativeLine(),
                    null,
                    null,
                    List.of(),
                    new ArrayList<>(figures)
            );
        }

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
        for (var line : candidateSplitters(figures)) {
            var score = calculateScore(figures, line);
            if (score < bestScore) {
                bestScore = score;
                bestLine = line;
            }
        }

        return bestLine;
    }

    private List<Line> candidateSplitters(List<Figure> figures) {
        if (figures.size() <= MAX_SPLITTER_CANDIDATES) {
            return figures.stream()
                    .map(Figure::getRepresentativeLine)
                    .toList();
        }

        var candidates = new ArrayList<Line>(MAX_SPLITTER_CANDIDATES);
        double step = (double) figures.size() / MAX_SPLITTER_CANDIDATES;
        for (int i = 0; i < MAX_SPLITTER_CANDIDATES; i++) {
            int index = Math.min((int) (i * step), figures.size() - 1);
            candidates.add(figures.get(index).getRepresentativeLine());
        }
        return candidates;
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
}
