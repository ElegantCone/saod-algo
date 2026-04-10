cd ../..
mkdir -p graphs/lab1_fix/alloc graphs/lab1_fix/cpu

#
# Perfect hashing
#

java -jar target/benchmarks.jar PerfectHashingBenchmark.perfectBuild \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1_fix/alloc1'

#python3 graphs/lab1/graph.py