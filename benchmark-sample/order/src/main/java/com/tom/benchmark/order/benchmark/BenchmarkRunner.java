package com.tom.benchmark.order.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

public class BenchmarkRunner {

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(JpaBenchmark.class.getSimpleName())
                .output("results/jmh_results.txt")
                .verbosity(VerboseMode.EXTRA)
                .build();

        new Runner(opt).run();
    }	
}
