package org.example.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    List<KeyValue> table;

    public Table() {
        table = new ArrayList<>();
    }

    public List<Point> getPoints(byte[] hash) {
        for (KeyValue keyValue : table) {
            if (keyValue != null && Arrays.equals(keyValue.key, hash)) {
                return keyValue.value;
            }
        }
        return null;
    }

    public void add(byte[] hash, List<Point> points) {
        this.table.add(new KeyValue(hash, points));
    }

    public record KeyValue(byte[] key, List<Point> value) {}
}
