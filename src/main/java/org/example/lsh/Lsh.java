package org.example.lsh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lsh {
    //parameters a, b, c, d for ax + by + cz + d = 0
    private int[][] lines;
    private final Random random = new Random();
    private final Table table;

    public Lsh() {
        generateLines();
        table = new Table();
    }

    /*public void buildTable(BufferedReader reader) throws IOException {
        var current = reader.readLine();
        while (current != null) {
            var coords = current.split(" ");
            var x = Integer.parseInt(coords[0]);
            var y = Integer.parseInt(coords[1]);
            var z = Integer.parseInt(coords[2]);
            insert(x, y, z);
            current = reader.readLine();
        }
    }*/

    public void insert(int x, int y, int z) {
        insert(new Point(x, y, z));
    }

    public void insert(Point point) {
        var h = hash(point.x(), point.y(), point.z());
        var points = table.getPoints(h);
        if (points != null) {
            points.add(point);
        } else {
            var newPoints = new ArrayList<Point>();
            newPoints.add(point);
            table.add(h, newPoints);
        }
    }

    public List<Point> get(Point point, double threshold) {
        return get(point.x(), point.y(), point.z(), threshold);

    }

    public List<Point> get(int x, int y, int z, double threshold) {
        var h = hash(x, y, z);
        var points = new ArrayList<Point>();
        for (var kv : table.table) {
            if (similarity(kv.key(), h) > threshold) {
                points.addAll(kv.value());
            }
        }
        return points;
    }

    public List<Point> getExact(Point point, double threshold, double radius) {
        return getExact(point.x(), point.y(), point.z(), threshold, radius);
    }

    public List<Point> getExact(int x, int y, int z, double threshold, double radius) {
        var points = get(x, y, z, threshold);
        var toReturn = new ArrayList<Point>();
        for (var p : points) {
            if (Math.sqrt(Math.pow(x - p.x(), 2) + Math.pow(y - p.y(), 2) + Math.pow(z - p.z(), 2)) < radius) {
                toReturn.add(p);
            }
        }
        return toReturn;
    }

    private void generateLines() {
        var size = random.nextInt(10,100);
        lines = new int[size][4];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 4; j++) {
                lines[i][j] = random.nextInt(-1_000,1_000);
            }
        }
    }

    private byte[] hash(int x, int y, int z) {
        var result = new byte[lines.length];
        for (int i = 0; i < lines.length; i++) {
            var line = lines[i];
            var s = line[0] * x + line[1] * y + line[2] * z + line[3];
            result[i] = (byte) (s > 0 ? 1 : 0);
        }
        return result;
    }

    private double similarity(byte[] ha, byte[] hb) {
        int count = 0;
        for (int i = 0; i < lines.length; i++) {
            if (ha[i] == hb[i]) {
                count++;
            }
        }
        return (double) count / lines.length;
    }

}
