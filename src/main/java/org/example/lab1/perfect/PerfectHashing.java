package org.example.lab1.perfect;

import lombok.Getter;

import java.util.*;

@Getter
public class PerfectHashing {
    private final PerfectBucket[] tables;
    private Integer a;
    private Integer b;
    int max = 1_000_000_003;
    private final Integer tableSize;
    private final Random random = new Random();

    public PerfectHashing(Integer tableSize) {
        this.tableSize = tableSize;
        tables = new PerfectBucket[tableSize];
    }

    public void build(Map<String, String> data) {
        var build = false;
        while (!build) {
            clear();
            this.a = random.nextInt(10, max);
            this.b = random.nextInt(10, max);
            for (var entry : data.entrySet()) {
                var hash = hash(entry.getKey());
                if (tables[hash] == null) {
                    tables[hash] = new PerfectBucket();
                }
                tables[hash].add(entry.getKey(), entry.getValue());
            }

            build = true;
            for (var table : tables) {
                if (table == null) continue;
                var built = table.buildTable();
                if (!built) {
                    build = false;
                    break;
                }
            }
        }
    }

    public String get(String key) {
        var hash = hash(key);
        var bucket = tables[hash];
        if (bucket != null) {
            return bucket.get(key);
        }
        return null;
    }

    public int hash(String str) {
        long hash = str.hashCode();
        hash = hash == Integer.MIN_VALUE ? 0 : Math.abs(hash);
        hash = ((long) a * hash + b) % max;
        return (int) (hash % tableSize);
    }

    private void clear() {
        Arrays.fill(tables, null);
    }
}
