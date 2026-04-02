package org.example.lab1.benchmark;

import org.example.lab1.lsh.Lsh;
import org.example.lab1.lsh.Point;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Random;

@State(Scope.Benchmark)
public class LshBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkData {
        @Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3000"})
        public int size;
        public int getSize = 500;
        Random random = new Random(42);

        public Point[] basePoints;
        public Point[] insertPoints;

        @Setup(Level.Trial)
        public void setupTrial() {
            basePoints = new Point[size];
            insertPoints = new Point[Math.max(size, 1_000)];

            var bound = random.nextInt(1, 20_000);
            for (int i = 0; i < size; i++) {
                basePoints[i] = new Point(
                        random.nextInt(-bound, bound),
                        random.nextInt(-bound, bound),
                        random.nextInt(-bound, bound)
                );
            }

            for (int i = 0; i < insertPoints.length; i++) {
                insertPoints[i] = new Point(random.nextInt(), random.nextInt(), random.nextInt());
            }
        }
    }

    @State(Scope.Thread)
    public static class QueryState {
        public Lsh lsh;
        public Random random;

        @Setup(Level.Iteration)
        public void setupIteration(BenchmarkData data) {
            random = new Random(data.size);
            lsh = new Lsh();
            for (var point : data.basePoints) {
                lsh.insert(point);
            }
        }
    }

    @State(Scope.Thread)
    public static class InsertState {
        public Lsh lsh;
        public int nextInsertIdx;

        @Setup(Level.Trial)
        public void setupTrial() {
            nextInsertIdx = 0;
        }

        @Setup(Level.Iteration)
        public void setupIteration(BenchmarkData data) {
            lsh = new Lsh();
            for (var point : data.basePoints) {
                lsh.insert(point);
            }
        }
    }

    @Benchmark
    public void lshInsert(BenchmarkData data, InsertState state) {
        var point = data.insertPoints[state.nextInsertIdx % data.insertPoints.length];
        state.nextInsertIdx++;
        state.lsh.insert(point);
    }

    @Benchmark
    public void lshGetPool(BenchmarkData data, QueryState state) {
        for (int i = 0; i < data.getSize; i++) {
            state.lsh.get(data.basePoints[state.random.nextInt(data.size)], 0.7);
        }
    }

    @Benchmark
    public void lshGet(BenchmarkData data, QueryState state) {
        state.lsh.get(data.basePoints[state.random.nextInt(data.size)], 0.7);
    }

    @Benchmark
    public void lshGetExactPool(BenchmarkData data, QueryState state) {
        for (int i = 0; i < data.getSize; i++) {
            state.lsh.getExact(data.basePoints[state.random.nextInt(data.size)], 0.7, 500.0);
        }
    }

    @Benchmark
    public void lshGetExact(BenchmarkData data, QueryState state) {
        state.lsh.getExact(data.basePoints[state.random.nextInt(data.size)], 0.7, 500.0);
    }

}
