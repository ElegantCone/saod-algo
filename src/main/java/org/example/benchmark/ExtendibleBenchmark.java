package org.example.benchmark;

import org.example.extendible.Table;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(3)
public class ExtendibleBenchmark {
    private static final int CAPACITY = 64;

    @Param({"100", "300", "500", "1000", "1200", "1600", "1800", "2000"})
    public int size;

    public int[] keys;
    public List<Integer> newKeys;
    public int idx = 0;
    public int[] values;
    public Table table;
    public Random random;

    @Setup(Level.Iteration)
    public void setup() throws IOException {
        random = new Random(size);
        keys = new int[size];
        values = new int[size];
        table = new Table(CAPACITY);
        var usedKeys = new HashSet<Integer>(size * 3);
        for (var i = 0; i < size; i++) {
            keys[i] = nextUniquePositiveKey(usedKeys);
            values[i] = random.nextInt(Integer.MAX_VALUE);
            table.insert(keys[i], values[i]);
        }
        idx = 0;
        newKeys = new ArrayList<>(size);
        while (newKeys.size() < size) {
            newKeys.add(nextUniquePositiveKey(usedKeys));
        }
    }

    @TearDown(Level.Iteration)
    public void cleanup() {
        if (table != null) {
            table.close();
        }
        var output = new File("output");
        if (!output.exists()) {
            output.mkdirs();
            return;
        }
        var files = output.listFiles();
        if (files == null) {
            return;
        }
        for (var file : files) {
            file.delete();
        }
    }

    @Benchmark
    public void insertNew() {
        int key = nextMissingKey();
        int value = random.nextInt(Integer.MAX_VALUE);
        try {
            table.insert(key, value);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to insert new key", e);
        }
    }

    @Benchmark
    public Integer getExisting() {
        return table.get(keys[random.nextInt(keys.length)]);
    }

    @Benchmark
    public Integer getSameEntry() {
        return table.get(keys[0]);
    }

    @Benchmark
    public void updateExisting() {
        int idx = random.nextInt(keys.length);
        table.update(keys[idx], random.nextInt(Integer.MAX_VALUE));
    }

    @Benchmark
    public void removeExisting() {
        int key = keys[random.nextInt(keys.length)];
        table.remove(key);
    }

    private int nextUniquePositiveKey(HashSet<Integer> usedKeys) {
        int key = random.nextInt(1, Integer.MAX_VALUE);
        while (!usedKeys.add(key)) {
            key = random.nextInt(1, Integer.MAX_VALUE);
        }
        return key;
    }

    private int nextMissingKey() {
        int key = random.nextInt(1, Integer.MAX_VALUE);
        while (table.get(key) != null) {
            key = random.nextInt(1, Integer.MAX_VALUE);
        }
        return key;
    }
}
