package org.example.lab1.benchmark;

import org.example.lab1.extendible.Table;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class ExtendibleBenchmark {
    private static final int CAPACITY = 64;
    private static final int INSERT_KEYS_COUNT = 1_000_000;

    @State(Scope.Benchmark)
    public static class BenchmarkData {
        @Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3000"})
        public int size;
        public int getSize = 500;

        public int[] keys;
        public int[] values;
        public int[] insertKeys;
        public int[] insertValues;
        public Random random = new Random(42);
        public Path benchmarkDir;

        @Setup(Level.Trial)
        public void setupTrial() throws IOException {
            int totalKeyCount = size + INSERT_KEYS_COUNT;
            int firstKey = random.nextInt(1, Integer.MAX_VALUE - totalKeyCount);

            keys = new int[size];
            values = new int[size];
            for (int i = 0; i < size; i++) {
                keys[i] = firstKey + i;
                values[i] = random.nextInt(Integer.MAX_VALUE);
            }

            insertKeys = new int[INSERT_KEYS_COUNT];
            insertValues = new int[INSERT_KEYS_COUNT];
            for (int i = 0; i < INSERT_KEYS_COUNT; i++) {
                insertKeys[i] = firstKey + size + i;
                insertValues[i] = random.nextInt(Integer.MAX_VALUE);
            }

            benchmarkDir = Files.createTempDirectory("extendible-benchmark-");
        }

        @TearDown(Level.Trial)
        public void cleanupTrial() {
            if (benchmarkDir == null) {
                return;
            }
            var output = benchmarkDir.toFile();
            var files = output.listFiles();
            if (files == null) {
                output.delete();
                return;
            }
            for (var file : files) {
                file.delete();
            }
            output.delete();
        }
    }

    @State(Scope.Thread)
    public static class TableState {
        public Table table;

        @Setup(Level.Iteration)
        public void setupIteration(BenchmarkData data) throws IOException {
            table = new Table(data.benchmarkDir.toFile(), CAPACITY);
            for (int i = 0; i < data.size; i++) {
                table.insert(data.keys[i], data.values[i]);
            }
        }

        @TearDown(Level.Iteration)
        public void cleanupIteration() {
            if (table != null) {
                table.close();
                table = null;
            }
        }
    }

    @State(Scope.Thread)
    public static class InsertState {
        public Table table;
        public int nextInsertIdx;

        @Setup(Level.Trial)
        public void setupTrial() {
            nextInsertIdx = 0;
        }

        @Setup(Level.Invocation)
        public void setupInvocation(BenchmarkData data) throws IOException {
            table = new Table(data.benchmarkDir.toFile(), CAPACITY);
            for (int i = 0; i < data.size; i++) {
                table.insert(data.keys[i], data.values[i]);
            }
        }

        @TearDown(Level.Invocation)
        public void cleanupInvocation() {
            if (table != null) {
                table.close();
                table = null;
            }
        }
    }

    @Benchmark
    public void extendibleInsert(BenchmarkData data, InsertState state) {
        int nextIdx = state.nextInsertIdx++;
        if (nextIdx >= data.insertKeys.length) {
            throw new IllegalStateException("Pre-generated insert keys exhausted");
        }
        try {
            state.table.insert(data.insertKeys[nextIdx], data.insertValues[nextIdx]);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to insert new key", e);
        }
    }

    @Benchmark
    public void extendibleGetExistingPool(BenchmarkData data, TableState state) {
        for (int i = 0; i < data.getSize; i++) {
            state.table.get(data.keys[data.random.nextInt(data.keys.length)]);
        }
    }

    @Benchmark
    public void extendibleGetExisting(BenchmarkData data, TableState state) {
        state.table.get(data.keys[data.random.nextInt(data.keys.length)]);
    }

    @Benchmark
    public void extendibleUpdateExisting(BenchmarkData data, TableState state) {
        int idx = data.random.nextInt(data.keys.length);
        state.table.update(data.keys[idx], data.random.nextInt(Integer.MAX_VALUE));
    }

    @Benchmark
    public void extendibleRemoveExisting(BenchmarkData data, TableState state) {
        int key = data.keys[data.random.nextInt(data.keys.length)];
        state.table.remove(key);
    }
}
