package org.example.lab1.benchmark;

import org.example.lab1.perfect.PerfectHashing;
import org.openjdk.jmh.annotations.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@State(Scope.Benchmark)
public class PerfectHashingBenchmark {
    //@Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3000"})

    @State(Scope.Benchmark)
    public static class BenchmarkData {
        @Param({"1000", "2000", "3000", "4000", "5000", "6000", "10000", "15000", "18000", "21000", "25000", "30000"})
        public int size;
        public Map<String, String> data;
        public String[] keys;
        public String[] values;
        public PerfectHashing perfectHashing;

        public Random random = new Random(42);

        @Setup(Level.Trial)
        public void setup() {
            perfectHashing = new PerfectHashing(size);
            data = new LinkedHashMap<>(size);
            keys = new String[size];
            values = new String[size];
            for (var i = 0; i < size; i++) {
                keys[i] = "key-" + i + "-" + Math.abs(random.nextInt());
                values[i] = "value-" + i + "-" + Math.abs(random.nextInt());
                data.put(keys[i], values[i]);
            }
        }
    }

    @Benchmark
    public void perfectBuild(BenchmarkData data) {
        data.perfectHashing.build(data.data);
    }

    @State(Scope.Thread)
    public static class GetState {
        public int getSize = 500;

        @Setup(Level.Iteration)
        public void setupTrial(BenchmarkData data) {
            data.perfectHashing.build(data.data);
        }
    }

    @Benchmark
    public void perfectGetExistingPool(GetState state, BenchmarkData data) {
        for (int i = 0; i < state.getSize; i++) {
            data.perfectHashing.get(data.keys[data.random.nextInt(data.keys.length)]);
        }
    }

    @Benchmark
    public void perfectGetExisting(GetState state, BenchmarkData data) {
        data.perfectHashing.get(data.keys[data.random.nextInt(data.keys.length)]);
    }

}
