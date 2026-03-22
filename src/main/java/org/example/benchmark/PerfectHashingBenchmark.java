package org.example.benchmark;

import org.example.lsh.Lsh;
import org.example.lsh.Point;
import org.example.perfect.PerfectHashing;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(3)
public class PerfectHashingBenchmark {
    @Param({"100", "300", "500", "1000", "1200", "1600", "1800", "2000"})
    public int size;
    public Map<String, String> data;
    public String[] keys;
    public String[] values;
    public PerfectHashing perfectHashing;

    public Random random = new Random();

    @Setup(Level.Trial)
    public void setup() {
        perfectHashing = new PerfectHashing(7, 31, 30);
        data = new LinkedHashMap<>(size);
        keys = new String[size];
        values = new String[size];
        for (var i = 0; i < size; i++) {
            keys[i] = "key-" + i + "-" + Math.abs(random.nextInt());
            values[i] = "value-" + i + "-" + Math.abs(random.nextInt());
            data.put(keys[i], values[i]);
        }
        perfectHashing.build(data);
    }

    @Benchmark
    public void build() {
        var table = new PerfectHashing(7, 31, 30);
        var newData = new LinkedHashMap<String, String>(size);
        for (var i = 0; i < size; i++) {
            newData.put("key-" + i + "-" + Math.abs(random.nextInt()), "value-" + i + "-" + Math.abs(random.nextInt()));
        }
        table.build(newData);
    }

    @Benchmark
    public String getExisting() {
        return perfectHashing.get(keys[random.nextInt(keys.length)]);
    }
}
