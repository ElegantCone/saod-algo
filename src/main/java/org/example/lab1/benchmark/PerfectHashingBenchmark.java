package org.example.lab1.benchmark;

import org.example.lab1.perfect.PerfectHashing;
import org.openjdk.jmh.annotations.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@State(Scope.Benchmark)
public class PerfectHashingBenchmark {
    @State(Scope.Benchmark)
    public static class BenchmarkData {
        //@Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3200", "3500", "4000", "4500", "5000", "5500", "6000", "6500", "7000", "7500", "8000", "8500", "9000", "9500", "10000"})
        @Param({"500", "1000", "3000", "5000", "10000"})
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
                if (data.containsKey(keys[i])) {
                    i--;
                    continue;
                }
                data.put(keys[i], values[i]);
            }
        }
    }

    @Benchmark
    public void perfectBuild(BenchmarkData data) {
        data.perfectHashing.build(data.data);
    }

    @State(Scope.Benchmark)
    public static class GetState {
        public int getSize = 500;

        @Setup(Level.Trial)
        public void setupTrial(BenchmarkData data) {
            data.perfectHashing.build(data.data);
        }
    }

    @Benchmark
    public void perfectGetExistingPool(GetState state, BenchmarkData data) {
        for (int i = 0; i < state.getSize; i++) {
            data.perfectHashing.get(data.keys[data.random.nextInt(0, state.getSize)]);
        }
    }

    /*@Benchmark
    public void perfectGetExisting(GetState state, BenchmarkData data) {
        data.perfectHashing.get(data.keys[data.random.nextInt(data.keys.length)]);
    }*/

}
