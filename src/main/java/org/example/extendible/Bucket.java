package org.example.extendible;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Getter
public class Bucket {
    File file;
    RandomAccessFile randomAccessFile;
    MappedByteBuffer buffer;
    String idx;
    int size;
    int capacity;
    int localDepth = 1;


    @SneakyThrows
    public Bucket(String idx, int capacity) {
        this.capacity = capacity;
        this.idx = idx;
        this.file = new File("output/data-" + idx);
        file.createNewFile();
        this.randomAccessFile = new RandomAccessFile(file, "rw");
        buffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, (long) Integer.BYTES * capacity * 2 + Byte.BYTES);
    }

    public Bucket(String idx, int capacity, int localDepth) {
        this(idx, capacity);
        this.localDepth = localDepth;
    }

    public boolean insert(int key, int value) {
        if (size == capacity) return false;
        if (getValue(key) != null) throw new IllegalArgumentException("Key " + key + " already exists");
        if (size == 0) {
            buffer.put(0, (byte) 1);
            buffer.position(1);
        }
        buffer.putInt(key);
        buffer.putInt(value);
        size++;
        return true;
    }

    protected KeyValue get(int idx) {
        if (buffer.get(0) == 0) return null;
        var arrayIdx = Byte.BYTES + idx * Integer.BYTES * 2;
        return new KeyValue(buffer.getInt(arrayIdx), buffer.getInt(arrayIdx + Integer.BYTES));
    }

    protected Integer getValue(int key) {
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
                }
                return true;
            }
        }
        return false;
    }

    protected boolean updateValue(int key, int value) {
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
