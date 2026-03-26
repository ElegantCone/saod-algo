package org.example.lab1.benchmark;

import lombok.NoArgsConstructor;
import one.profiler.AsyncProfiler;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public final class JmhRunner {

    public static void main(String[] args) throws RunnerException {
        var include = args.length > 0 ? args[0] : "LshBenchmark.lshInsert.*";
        Options options = new OptionsBuilder()
                .include(include)
                .shouldFailOnError(true)
                .forks(3)
                .warmupIterations(3)
                .measurementIterations(5)
                .result("target/jmh-results.json")
                .resultFormat(ResultFormatType.JSON)
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.NANOSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .measurementTime(TimeValue.seconds(3))
                .build();
        new Runner(options).run();
    }
}
