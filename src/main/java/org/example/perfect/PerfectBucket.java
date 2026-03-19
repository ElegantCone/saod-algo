package org.example.perfect;

import lombok.NoArgsConstructor;
import org.example.hashtable.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class PerfectBucket {
    Random random = new Random();
    Record[] table;
    int a;
    int b;
    int max = random.nextInt(1_000_000, 10_000_000);
    List<org.example.hashtable.Record> records = new ArrayList<>();

    public void add(String key, String value) {
        records.add(new org.example.hashtable.Record(key, value));
    }

    public void buildTable() {
        var build = false;
        while (!build) {
            build = build();
        }
    }

    public String get(String key) {
        var hash = hash(key);
        var record = table[hash];
        if (record == null) return null;
        if (record.key().equals(key)) {
            return record.value();
        }
        return null;
    }

    private int hash(String key) {
        long hash = 0;
        for (int i = 0; i < key.length(); i++) {
            var c = key.charAt(i);
            hash = ((hash * b + (long)a * c) % max) * (i+1);
        }
        return (int) (hash % table.length);
    }

    private boolean build() {
        table = new Record[records.size() * records.size()];
        a = random.nextInt(1_000, 100_000);
        b = random.nextInt(1_000, 100_000);
        for (var record : records) {
            var hash = hash(record.key());
            if (table[hash] == null) {
                table[hash] = record;
            } else {
                return false;
            }
        }
        return true;
    }
}
