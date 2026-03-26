package org.example.lab1.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class Lsh {
    private static final int LINE_COUNT = 32;
    private static final int BAND_COUNT = 8;
    private static final int ROWS_PER_BAND = LINE_COUNT / BAND_COUNT;

    //parameters a, b, c, d for ax + by + cz + d = 0
    private int[][] lines;
    private final Random random = new Random();
    private final Table table;

    public Lsh() {
        generateLines();
        table = new Table(BAND_COUNT);
    }

    public void insert(int x, int y, int z) {
        insert(new Point(x, y, z));
    }

    public void insert(Point point) {
        var h = hash(point.x(), point.y(), point.z());
        for (int bandIdx = 0; bandIdx < BAND_COUNT; bandIdx++) {
            table.add(bandIdx, band(h, bandIdx), point);
        }
    }

    public List<Point> get(Point point, double threshold) {
        return get(point.x(), point.y(), point.z(), threshold);

    }

    public List<Point> get(int x, int y, int z, double threshold) {
        var h = hash(x, y, z);
        var candidates = new LinkedHashSet<Point>();
        for (int bandIdx = 0; bandIdx < BAND_COUNT; bandIdx++) {
            candidates.addAll(table.getPoints(bandIdx, band(h, bandIdx)));
        }

        var points = new ArrayList<Point>();
        for (var point : candidates) {
            if (similarity(hash(point.x(), point.y(), point.z()), h) > threshold) {
                points.add(point);
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
            var dist = Math.sqrt(Math.pow(x - p.x(), 2) + Math.pow(y - p.y(), 2) + Math.pow(z - p.z(), 2));
            if (dist <= radius) {
                toReturn.add(p);
            }
        }
        return toReturn;
    }

    private void generateLines() {
        lines = new int[LINE_COUNT][4];
        for (int i = 0; i < LINE_COUNT; i++) {
            for (int j = 0; j < 4; j++) {
                lines[i][j] = random.nextInt(-1_000,1_000);
            }
        }
    }

    private byte[] hash(int x, int y, int z) {
        var result = new byte[lines.length];
        for (int i = 0; i < lines.length; i++) {
            var line = lines[i];
            long s = (long) line[0] * x + (long) line[1] * y + (long) line[2] * z + line[3];
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

    private byte[] band(byte[] hash, int bandIdx) {
        var from = bandIdx * ROWS_PER_BAND;
        var to = from + ROWS_PER_BAND;
        return Arrays.copyOfRange(hash, from, to);
    }

}
