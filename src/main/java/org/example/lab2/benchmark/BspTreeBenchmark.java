package org.example.lab2.benchmark;

import org.example.lab2.BspTree;
import org.example.lab2.Point;
import org.example.lab2.figure.Figure;
import org.example.lab2.figure.Polygon;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@State(Scope.Benchmark)
public class BspTreeBenchmark {
    @Param({"100", "500", "1000", "3000"})
    public int size;
    @Param({"0.0", "0.5", "2.0", "5.0", "10.0"})
    public double threshold;
    @Param({"INSIDE", "NEAR_EDGE", "MISS"})
    public QueryType queryType;

    public BspTree tree;
    public List<Point> insideQueries;
    public List<Point> nearEdgeQueries;
    public List<Point> missQueries;
    public List<Polygon> polygons;
    public Random random = new Random(42);

    public enum QueryType {
        INSIDE,
        NEAR_EDGE,
        MISS
    }

    @Setup(Level.Trial)
    public void setup() {
        insideQueries = new ArrayList<>();
        nearEdgeQueries = new ArrayList<>();
        missQueries = new ArrayList<>();
        tree = new BspTree();
        polygons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            var minX = random.nextDouble(-100, 100);
            var maxX = random.nextDouble(minX + 1, 200);
            var minY = random.nextDouble(-100, 100);
            var maxY = random.nextDouble(minY + 1, 200);
            var s = square(minX, minY, maxX, maxY);
            insideQueries.add(new Point(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY)));
            nearEdgeQueries.add(new Point(maxX + 1, (minY + maxY) / 2));
            missQueries.add(new Point(maxX + 50, maxY + 50));
            polygons.add(s);
            tree.addFigure(s);
        }
        tree.buildTree();
    }

    private Polygon square(double minX, double minY, double maxX, double maxY) {
        return new Polygon(List.of(
                new Point(minX, minY),
                new Point(maxX, minY),
                new Point(maxX, maxY),
                new Point(minX, maxY)
        ));
    }

    @Benchmark
    public void bspBuild() {
        var t = new BspTree();
        for (var polygon : polygons) {
            t.addFigure(polygon);
        }
        t.buildTree();
    }

    @Benchmark
    public Set<Figure> bspFindWithThreshold() {
        var p = switch (queryType) {
            case INSIDE -> insideQueries.get(random.nextInt(insideQueries.size()));
            case NEAR_EDGE -> nearEdgeQueries.get(random.nextInt(nearEdgeQueries.size()));
            case MISS -> missQueries.get(random.nextInt(missQueries.size()));
        };
        return tree.find(p.x(), p.y(), threshold);
    }
}
