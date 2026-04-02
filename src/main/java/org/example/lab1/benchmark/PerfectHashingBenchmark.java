package org.example.lab1.benchmark;

import org.example.lab1.perfect.PerfectHashing;
import org.openjdk.jmh.annotations.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@State(Scope.Benchmark)
public class PerfectHashingBenchmark {
    @Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3000"})
    public int size;
    public int getSize = 500;
    public Map<String, String> data;
    public String[] keys;
    public String[] values;
    public PerfectHashing perfectHashing;

    public Random random = new Random(42);

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
    public void perfectBuild() {
        var table = new PerfectHashing(7, 31, 30);
        var newData = new LinkedHashMap<String, String>(size);
        for (var i = 0; i < size; i++) {
            newData.put("key-" + i + "-" + Math.abs(random.nextInt()), "value-" + i + "-" + Math.abs(random.nextInt()));
        }
        table.build(newData);
    }

    @Benchmark
    public void perfectGetExistingPool() {
        for (int i = 0; i < getSize; i++) {
            perfectHashing.get(keys[random.nextInt(keys.length)]);
        }
    }

    @Benchmark
    public void perfectGetExisting() {
        perfectHashing.get(keys[random.nextInt(keys.length)]);
    }

}
