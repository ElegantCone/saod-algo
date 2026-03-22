package org.example.benchmark;

import lombok.NoArgsConstructor;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@NoArgsConstructor
public final class JmhRunner {

    public static void main(String[] args) throws RunnerException {
        var include = args.length > 0 ? args[0] : ".*";
        Options options = new OptionsBuilder()
                .include(include)
                .shouldFailOnError(true)
                .forks(3)
                .warmupIterations(1)
                .measurementIterations(3)
                .result("target/jmh-results.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
