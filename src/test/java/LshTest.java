import org.example.lsh.Lsh;
import org.example.lsh.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LshTest {
    List<Point> points;
    Random rand = new Random();
    Lsh lsh;

    @BeforeEach
    public void beforeEach() {
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



}
