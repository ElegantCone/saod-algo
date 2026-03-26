package lab2;

import org.example.lab2.BspTree;
import org.example.lab2.Point;
import org.example.lab2.figure.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BspTreeTest {
    @Test
    public void find_returnsPolygonForPointInside() {
        var square = square(0, 0, 10, 10);
        var tree = new BspTree();
        tree.addFigure(square);
        tree.buildTree();

        var found = tree.find(5, 5, 0);

        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(square, found.toArray()[0]);
    }

    @Test
    public void find_returnsPolygonNearEdgeWithinThreshold() {
        var square = square(0, 0, 10, 10);
        var tree = new BspTree();
        tree.addFigure(square);
        tree.buildTree();

        var found = tree.find(12, 5, 2.1);

        Assertions.assertEquals(square, found.toArray()[0]);
    }

    @Test
    public void find_doesNotReturnPolygonOutsideThreshold() {
        var square = square(0, 0, 10, 10);
        var tree = new BspTree();
        tree.addFigure(square);
        tree.buildTree();

        var found = tree.find(13, 5, 2.0);

        Assertions.assertTrue(found.isEmpty());
    }

    @Test
    public void find_doesNotDuplicateSpanningFigure() {
        var spanning = new Polygon(List.of(
                new Point(-2, -1),
                new Point(2, -1),
                new Point(2, 1),
                new Point(-2, 1)
        ));
        var front = square(5, 5, 7, 7);

        var tree = new BspTree();
        tree.addFigure(spanning);
        tree.addFigure(front);
        tree.buildTree();

        var found = tree.find(0, 0, 0);

        Assertions.assertEquals(1, found.size());
        Assertions.assertSame(spanning, found.toArray()[0]);
    }

    private Polygon square(double minX, double minY, double maxX, double maxY) {
        return new Polygon(List.of(
                new Point(minX, minY),
                new Point(maxX, minY),
                new Point(maxX, maxY),
                new Point(minX, maxY)
        ));
    }
}
