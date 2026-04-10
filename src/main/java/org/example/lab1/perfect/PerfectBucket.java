package org.example.lab1.perfect;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
@Getter
public class PerfectBucket {
    Random random = new Random();
    int a;
    int b;
    int max = 1_000_000_003;
    int maxAttempts = 10_000;
    List<String> keys = new ArrayList<>();
    List<String> values = new ArrayList<>();

    String[] builtKeys;
    String[] builtValues;

    public void add(String key, String value) {
        keys.add(key);
        values.add(value);
    }

    public boolean buildTable() {
        var build = false;
        var keysHash = new ArrayList<Integer>(keys.size());
        builtKeys = new String[keys.size() * keys.size()];
        builtValues = new String[values.size() * values.size()];
        var maxSize = keys.size() * keys.size();
        int it = 0;
        while (!build && it != maxAttempts) {
            build = build(keysHash, maxSize);
            it++;
        }
        if (!build) {
            return false;
        }
        for (int i = 0; i < keys.size(); i++) {
            var hash = keysHash.get(i);
            builtKeys[hash] = keys.get(i);
            builtValues[hash] = values.get(i);
        }
        return true;
    }

    public String get(String key) {
        var hash = hash(key);
        if (hash >= builtKeys.length) return null;
        var recordKey = builtKeys[hash];
        if (recordKey == null) return null;
        if (recordKey.equals(key)) {
            return builtValues[hash];
        }
        return null;
    }

    private int hash(String key) {
        long hash = key.hashCode();
        hash = hash == Integer.MIN_VALUE ? 0 : Math.abs(hash);
        hash = ((long) a * hash + b) % max;
        return (int) (hash % builtKeys.length);
    }

    private boolean build(List<Integer> keysHash, int maxSize) {
        a = random.nextInt(10, max);
        b = random.nextInt(10, max);
        for (int i = 0; i < keys.size(); i++) {
            var hash = hash(keys.get(i));
            if (hash < maxSize && !keysHash.contains(hash)) {
                keysHash.add(hash);
            } else {
                keysHash.clear();
                return false;
            }
        }
        return true;
    }
}
