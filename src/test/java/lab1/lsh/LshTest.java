package lab1.lsh;

import org.example.lab1.lsh.Lsh;
import org.example.lab1.lsh.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LshTest {
    static List<Point> points;
    static Random rand = new Random();
    static Lsh lsh;

    @BeforeAll
    public static void beforeAll() {
        lsh = new Lsh();
        var size = rand.nextInt(1_000, 10_000);
        points = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            var point = new Point(rand.nextInt(), rand.nextInt(), rand.nextInt());
            points.add(point);
            lsh.insert(point);
        }
    }

    @Test
    public void insert() {
        for (var point : points) {
            var similar = lsh.get(point, 0.5);
            Assertions.assertNotNull(similar);
        }
    }

    @Test
    public void getExact() {
        for (var point : points) {
            var similar = lsh.getExact(point, 0, 1);
            Assertions.assertNotNull(similar);
            Assertions.assertTrue(similar.contains(point));
            Assertions.assertEquals(1, similar.size());
        }
    }

    @Test
    public void get() {
        for (var point : points) {
            var similar = lsh.get(point, 0.5);
            Assertions.assertNotNull(similar);
        }
    }

    @Test
    public void getNearestPoints() {
        var lshTest = new Lsh();
        var center = new Point(0, 0, 0);
        lshTest.insert(center);
        for (int i = 0; i < 100; i++) {
            var point = new Point(rand.nextInt(-10, 10), rand.nextInt(-10, 10), rand.nextInt(-10, 10));
            lshTest.insert(point);
        }
        var radius = Math.sqrt(3 * Math.pow(10, 2)); // чуть больше максимального расстояния для сгенерированных точек
        var similar = lshTest.getExact(center, 0, radius);
        Assertions.assertTrue(similar.contains(center));
        for (var point : similar) {
            var dist = Math.sqrt(Math.pow(point.x(), 2) + Math.pow(point.y(), 2) + Math.pow(point.z(), 2));
            Assertions.assertTrue(dist <= radius);
        }
    }

    @Test
    public void getExact_returnsSelfForZeroRadius() {
        var lshTest = new Lsh();
        var point = new Point(rand.nextInt(), rand.nextInt(), rand.nextInt());
        for (int i = 0; i < 100; i++) {
            var p = new Point(rand.nextInt(), rand.nextInt(), rand.nextInt());
            lshTest.insert(p);
        }
        lshTest.insert(point);
        var similar = lshTest.getExact(point, 0.1, 0);
        Assertions.assertNotNull(similar);
        Assertions.assertTrue(similar.contains(point));
        Assertions.assertEquals(1, similar.size());
    }

    @Test
    public void getExact_returnsEmptyForNonExistingPoint() {
        var lshTest = new Lsh();
        var point = new Point(rand.nextInt(), rand.nextInt(), rand.nextInt());
        var similar = lshTest.getExact(point, 0, 1);
        Assertions.assertNotNull(similar);
        Assertions.assertTrue(similar.isEmpty());
    }

    @Test
    public void getExact_returnsEmptyForDistantPoint() {
        var lshTest = new Lsh();
        var point = new Point(0, 0, 0);
        lshTest.insert(point);
        var points = List.of(
                new Point(0, 0, 1),
                new Point(0, 1, 0),
                new Point(1, 0, 0)
        );
        points.forEach(lshTest::insert);
        var excluded = new Point(1, 1, 1);
        lshTest.insert(excluded);
        var similar = lshTest.getExact(point, 0.5, 1);
        Assertions.assertNotNull(similar);
        Assertions.assertEquals(4, similar.size());
        Assertions.assertTrue(similar.contains(point));
        Assertions.assertTrue(similar.containsAll(points));
        Assertions.assertFalse(similar.contains(excluded));
    }
}
