package org.example.lab1.extendible;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

@Getter
public class Bucket {
    File file;
    RandomAccessFile randomAccessFile;
    MappedByteBuffer buffer;
    String idx;
    int size;
    int capacity;
    int localDepth = 1;
    int batchSize = 32;
    int currentBatchSize = 0;
    boolean isInit = true;
    int[][] batch = new int[batchSize][1];

    public Bucket(String idx, int capacity) throws IOException {
        this(new File("output"), idx, capacity);
    }

    public Bucket(File baseDir, String idx, int capacity) throws IOException {
        this.capacity = capacity;
        this.idx = idx;
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        this.file = new File(baseDir, "data-" + idx);
        file.createNewFile();
        this.randomAccessFile = new RandomAccessFile(file, "rw");
        buffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, (long) Integer.BYTES * capacity * 2 + Byte.BYTES);
    }

    @SneakyThrows
    public Bucket(String idx, int capacity, int localDepth) throws IOException {
        this(idx, capacity);
        this.localDepth = localDepth;
    }

    @SneakyThrows
    public Bucket(File baseDir, String idx, int capacity, int localDepth) throws IOException {
        this(baseDir, idx, capacity);
        this.localDepth = localDepth;
    }

    public boolean insert(int key, int value) {
        if (size == capacity) return false;
        if (getValue(key) != null)
            throw new IllegalArgumentException("Key " + key + " already exists");
        if (size != 0 && size % batchSize == 0) {
            flush();
        }
        batch[currentBatchSize] = new int[]{key, value};
        size++;
        currentBatchSize++;
        return true;
    }

    public void flush() {
        if (isInit) {
            buffer.put(0, (byte) 1);
            buffer.position(1);
            isInit = false;
        }
        for (int i = 0; i < currentBatchSize; i++) {
            buffer.putInt(batch[i][0]);
            buffer.putInt(batch[i][1]);
        }
        currentBatchSize = 0;
    }

    protected KeyValue get(int idx) {
        if (currentBatchSize != 0) flush();
        if (buffer.get(0) == 0) return null;
        var arrayIdx = Byte.BYTES + idx * Integer.BYTES * 2;
        return new KeyValue(buffer.getInt(arrayIdx), buffer.getInt(arrayIdx + Integer.BYTES));
    }

    protected Integer getValue(int key) {
        for (int i = 0; i < currentBatchSize; i++) {
            if (key == batch[i][0]) return batch[i][1];
        }
        for (int i = 0; i < size; i++) {
            var kv = get(i);
            if (kv == null) continue;
            if (kv.key() == key) {
                return kv.value();
            }
        }
        return null;
    }

    //перемещение последнего элемента на место удаляемого
    protected boolean removeValue(int key) {
        if (currentBatchSize != 0) flush();
        for (int i = 0; i < size; i++) {
            var kv = get(i);
            if (kv == null) continue;
            if (kv.key() == key) {
                var end = Byte.BYTES + size * Integer.BYTES * 2;
                buffer.position(Byte.BYTES + i * Integer.BYTES * 2);
                buffer.putInt(buffer.getInt(end - Integer.BYTES * 2));
                buffer.putInt(buffer.getInt(end - Integer.BYTES));
                buffer.putInt(end - Integer.BYTES * 2, 0);
                buffer.putInt(end - Integer.BYTES, 0);
                buffer.position(end - Integer.BYTES * 2);
                size--;
                if (size == 0) {
                    buffer.put(0, (byte) 0);
                    isInit = true;
                }
                return true;
            }
        }
        return false;
    }

    protected boolean updateValue(int key, int value) {
        if (currentBatchSize != 0) flush();
        for (int i = 0; i < size; i++) {
            var kv = get(i);
            if (kv == null) continue;
            if (kv.key() == key) {
                buffer.putInt(Byte.BYTES + i * Integer.BYTES * 2 + Integer.BYTES, value);
                return true;
            }
        }
        return false;
    }

    public void remove() {
        try {
            randomAccessFile.close();
            file.delete();
        } catch (IOException ignored) {
        }
    }
}
