package org.example.extendible;

import java.io.File;
import java.io.IOException;

public class Table implements AutoCloseable {
    Bucket[] directory = new Bucket[2];
    int capacity;
    File dataDir;
    //сколько последних бит будут использоваться для того чтобы определить в какую емкость следует заносить значения
    int globalDepth = 1;

    public Table(int capacity) throws IOException {
        this(new File("output"), capacity);
    }

    public Table(File dataDir, int capacity) throws IOException {
        this.dataDir = dataDir;
        this.capacity = capacity;
        for (int i = 0; i < directory.length; i++) {
            directory[i] = new Bucket(dataDir, String.valueOf(i), capacity);
        }
    }

    public Table(int idx, int capacity) throws IOException {
        this(new File("output"), idx, capacity);
    }

    public Table(File dataDir, int idx, int capacity) throws IOException {
        this.dataDir = dataDir;
        this.capacity = capacity;
        for (int i = idx; i < directory.length + idx; i++) {
            directory[i] = new Bucket(dataDir, String.valueOf(i), capacity);
        }
    }
    //А из разницы локальной глубины и глобальной глубины можно понять сколько ячеек каталога ссылаются на емкость
    //K=2^{G−L} где G — глобальная глубина, L— локальная глубина, а K - количество ссылающихся ячеек

    public void insert(int key, int value) throws IOException {
        var idx = hash(key);
        var bucket = directory[idx];
        if (bucket.insert(key, value)) return;
        if (bucket.localDepth < globalDepth) {
            split(idx);
        } else {
            globalDepth++;
            resize(idx);
        }
        insert(key, value);
    }

    public Integer get(int key) {
        var idx = hash(key);
        return directory[idx].getValue(key);
    }

    public boolean remove(int key) {
        var removed = false;
        var idx = hash(key);
        removed = directory[idx].removeValue(key);
        if (directory[idx].size == 0) {
            var ld = directory[idx].localDepth;
            if (ld == 1) return removed; //two init buckets
            var mask = 1 << (ld - 1);
            var buddyIdx = (idx & mask) == 0 ? mask | idx : idx & ~mask;
            if (buddyIdx >= directory.length) return removed;
            if (directory[buddyIdx].localDepth != ld) return removed;
            shrink(idx, buddyIdx);
        }
        return removed;
    }

    public boolean update(int key, int value) {
        var idx = hash(key);
        return directory[idx].updateValue(key, value);
    }

    private void resize(int insertIdx) throws IOException {
        var old = directory;
        directory = new Bucket[directory.length << 1];
        for (int i = 0; i < old.length; i++) {
            directory[i] = old[i];
            directory[i + old.length] = old[i];
        }
        split(insertIdx);
    }

    private void split(int i) throws IOException {
        var initBucket = directory[i];
        var splitBit = 1 << initBucket.localDepth;
        var lowerBitsMask = splitBit - 1;
        var leftIdx = i & lowerBitsMask;
        var rightIdx = leftIdx | splitBit;

        directory[leftIdx] = new Bucket(dataDir, initBucket.idx + leftIdx, capacity, initBucket.localDepth + 1);
        directory[rightIdx] = new Bucket(dataDir, initBucket.idx + rightIdx, capacity, initBucket.localDepth + 1);
        for (int k = 0; k < directory.length; k++) {
            if (directory[k] == initBucket) {
                //k - это последние localDepth битов, нас интересует только старший бит
                if ((k & splitBit) == 0) {
                    directory[k] = directory[leftIdx];
                } else {
                    directory[k] = directory[rightIdx];
                }
            }
        }
        for (int k = 0; k < initBucket.size; k++) {
            var kv = initBucket.get(k);
            if (kv == null) continue;
            insert(kv.key(), kv.value());
        }
        initBucket.remove();
    }

    private void shrink(int idx, int buddyIdx) {
        var bucket = directory[idx];
        if (directory[buddyIdx] != null) {
            directory[idx] = directory[buddyIdx];
        }
        var maxLocalDepth = 0;
        for (int i = 0; i < directory.length; i++) {
            if (directory[i] == bucket) {
                directory[i] = directory[buddyIdx];
            }
            maxLocalDepth = Math.max(maxLocalDepth, directory[i].localDepth);
        }
        directory[buddyIdx].localDepth = directory[buddyIdx].localDepth - 1;
        if (maxLocalDepth < globalDepth) {
            globalDepth = maxLocalDepth;
        }
        bucket.remove();
    }

    private int hash(int key) {
        return Math.floorMod(key, 1 << globalDepth);
    }

    @Override
    public void close() {
        for (var b : directory) {
            if (b != null) {
                b.remove();
            }
        }
    }
}
