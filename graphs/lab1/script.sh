cd ../..
mkdir -p graphs/lab1/alloc graphs/lab1/cpu

#
# Lsh
#

java -jar target/benchmarks.jar LshBenchmark.lshInsert \
  -p size=10000 \
  -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar LshBenchmark.lshGet \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar LshBenchmark.lshGetExact \
  -p size=10000 \
  -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'



java -jar target/benchmarks.jar LshBenchmark.lshInsert \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar LshBenchmark.lshGet \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar LshBenchmark.lshGetExact \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

#
# Perfect hashing
#

java -jar target/benchmarks.jar PerfectHashingBenchmark.perfectBuild \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar PerfectHashingBenchmark.perfectGetExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'


java -jar target/benchmarks.jar PerfectHashingBenchmark.perfectBuild \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar PerfectHashingBenchmark.perfectGetExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

#
# Extendible hashing
#

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleInsert \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleGetExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleUpdateExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleRemoveExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=alloc;output=flamegraph;dir=graphs/lab1/alloc'


java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleInsert \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleGetExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleUpdateExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

java -jar target/benchmarks.jar ExtendibleBenchmark.extendibleRemoveExisting \
  -p size=10000 -f 1 -w 2 -wi 2 -i 5 \
  -prof 'async:libPath=/opt/homebrew/opt/async-profiler/lib/libasyncProfiler.dylib;event=cpu;output=flamegraph;dir=graphs/lab1/cpu'

#python3 graphs/lab1/graph.py