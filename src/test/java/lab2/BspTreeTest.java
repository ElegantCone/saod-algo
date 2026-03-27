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

        var found = tree.find(12, 5, 2.0);

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
        var spanning = square(-2, -1, 2, 1);
        var front = square(5, 5, 7, 7);

        var tree = new BspTree();
        tree.addFigure(spanning);
        tree.addFigure(front);
        tree.buildTree();

        var found = tree.find(0, 0, 0);

        Assertions.assertEquals(1, found.size());
        Assertions.assertSame(spanning, found.toArray()[0]);
    }

    @Test
    public void polygon_inside_returnsTrueForPointOnEdge() {
        var square = square(0, 0, 10, 10);

        Assertions.assertTrue(square.inside(10, 5));
        Assertions.assertTrue(square.inside(0, 0));
    }

    @Test
    public void polygon_inside_returnsFalseForOutsidePoint() {
        var square = square(0, 0, 10, 10);

        Assertions.assertFalse(square.inside(11, 5));
        Assertions.assertFalse(square.inside(-1, -1));
    }

    @Test
    public void polygon_distance_returnsDistanceToEdgeNotVertex() {
        var square = square(0, 0, 10, 10);

        Assertions.assertEquals(2.0, square.distance(12, 5), 1e-9);
    }

    @Test
    public void find_returnsFiguresFromBothBranchesNearSplitter() {
        var left = square(-6, 0, -2, 4);
        var right = square(2, 0, 6, 4);

        var tree = new BspTree();
        tree.addFigure(left);
        tree.addFigure(right);
        tree.buildTree();

        var found = tree.find(0, 2, 2.1);

        Assertions.assertEquals(2, found.size());
        Assertions.assertTrue(found.contains(left));
        Assertions.assertTrue(found.contains(right));
    }

    @Test
    public void find_onLine_returnsThisLine() {
        var line = new Polygon(List.of(new Point(0, 0), new Point(10, 0)));
        var tree = new BspTree();
        tree.addFigure(line);
        tree.buildTree();

        var found = tree.find(5, 0, 0);

        Assertions.assertEquals(1, found.size());
        Assertions.assertSame(line, found.toArray()[0]);

        var found2 = tree.find(11, 0, 0);
        Assertions.assertTrue(found2.isEmpty());

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
