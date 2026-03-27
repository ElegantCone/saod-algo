cd ../..
java -jar target/benchmarks.jar BspTreeBenchmark.bspBuild \
  -p size=1000 \
  -p threshold=2.0 \
  -p queryType=NEAR_EDGE \
  -f 1 \
  -prof async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib\;output=flamegraph


#python3 graphs/lab2/graph.py


