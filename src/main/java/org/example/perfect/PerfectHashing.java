package org.example.perfect;

import lombok.Getter;

import java.util.*;

@Getter
public class PerfectHashing {
    private final PerfectBucket[] tables;
    private final Integer a;
    private final Integer b;
    private final Integer tableSize;
    private final Random random = new Random();

    public PerfectHashing(Integer a, Integer b, Integer tableSize) {
        this.a = a;
        this.b = b;
        this.tableSize = tableSize;
        tables = new PerfectBucket[tableSize];
    }

    public void build(Map<String, String> data) {
        clear();
        for (var entry : data.entrySet()) {
            var hash = hash(entry.getKey());
            if (tables[hash] == null) {
                tables[hash] = new PerfectBucket();
            }
            tables[hash].add(entry.getKey(), entry.getValue());
        }
        for (var table : tables) {
            if (table == null) continue;
            table.buildTable();
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
        long sum = 0;
        for (var c : str.toCharArray()) {
            sum += c;
        }
        return (int) ((a * sum + b) % tableSize);
    }

    private void clear() {
        Arrays.fill(tables, null);
    }
}
