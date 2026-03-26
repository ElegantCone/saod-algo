package org.example.benchmark;

import org.example.lsh.Lsh;
import org.example.lsh.Point;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 5)
@Fork(3)
public class LshBenchmark {

    @Param({"100", "300", "500", "700", "1000", "1300", "1600", "1900", "2100", "2500", "2800", "3000"})
    public int size;
    public Point[] basePoints;
    public Lsh lsh;

    public Random random = new Random();

    @Setup(Level.Trial)
    public void setup() {
        basePoints = new Point[size];
        lsh = new Lsh();
        var bound = random.nextInt(20_000);
        for (int i = 0; i < size; i++) {
            basePoints[i] = new Point(
                    random.nextInt(-bound, bound),
                    random.nextInt(-bound, bound),
                    random.nextInt(-bound, bound)
            );
            lsh.insert(basePoints[i]);
        }
    }

    @Benchmark
    public void lshInsert() {
        var point = new Point(random.nextInt(), random.nextInt(), random.nextInt());
        lsh.insert(point);
    }

    @Benchmark
    public List<Point> lshGet() {
        return lsh.get(basePoints[random.nextInt(size)], 0.7);
    }

    @Benchmark
    public List<Point> lshGetExact() {
        return lsh.getExact(basePoints[random.nextInt(size)], 0.7, 500.0);
    }

}
