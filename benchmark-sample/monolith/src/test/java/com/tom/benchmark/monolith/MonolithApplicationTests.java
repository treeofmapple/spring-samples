package com.tom.benchmark.monolith;

import java.io.File;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import com.tom.benchmark.monolith.benchmark.JpaBenchmark;
import com.tom.benchmark.monolith.config.TestUtils;

class MonolithApplicationTests {
    public static void main(String[] args) throws RunnerException {
    	
        String outputDir = "benchmarks";
        String outputPath = outputDir + File.separator + "jmh_results_" + System.currentTimeMillis() + ".txt";

        TestUtils.storeContentOnFolder(outputDir, outputPath);
        
        Options opt = new OptionsBuilder()
                .include(JpaBenchmark.class.getSimpleName())
                .jvmArgs("-Dcom.sun.management.jmxremote=false")
                .forks(1)
                .output(outputPath)
                .verbosity(VerboseMode.EXTRA)
                .build();
        
        System.out.println("[INFO] Running JMH benchmark...");
        new Runner(opt).run();
        System.out.println("[INFO] Benchmark completed. Results saved to: " + outputPath);
    }	
}
