package org.example.hashtable;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

@Getter
public class HashTable {
    private final Bucket[] table;
    private Integer a;
    private Integer b;
    private final Integer tableSize;

    public HashTable(Integer a, Integer b, Integer tableSize) {
        this.a = a;
        this.b = b;
        this.tableSize = tableSize;
        table = new Bucket[tableSize];
    }

    public void insert(String key, String element) {
        var hash = hash(key);
        var bucket = table[hash];
        if (bucket == null) {
            table[hash] = new Bucket(new ArrayList<>());
            bucket = table[hash];
        }
        if (!bucket.containsKey(key)) {
            bucket.elements().add(new Record(key, element));
        } else {
            throw new IllegalArgumentException("Duplicate key: " + key);
        }
    }

    public String get(String key) {
        var hash = hash(key);
        var bucket = table[hash];
        if (bucket == null) return null;
        for (var element : bucket.elements()) {
            if (element.key().equals(key)) {
                return element.value();
            }
        }
        return null;
    }

    public boolean delete(String key) {
        var hash = hash(key);
        var bucket = table[hash];
        if (bucket == null) return false;
        for (var element : bucket.elements()) {
            if (element.key().equals(key)) {
                bucket.elements().remove(element);
                return true;
            }
        }
        return false;
    }

    public boolean update(String key, String newValue) {
        var hash = hash(key);
        var bucket = table[hash];
        if (bucket == null) return false;
        for (var element : bucket.elements()) {
            if (element.key().equals(key)) {
                bucket.elements().remove(element);
                bucket.elements().add(new Record(key, newValue));
                return true;
            }
        }
        return false;
    }

    public int hash(String str) {
        long sum = 0;
        for (var c : str.toCharArray()) {
            sum += c;
        }
        return (int) ((a * sum + b) % tableSize);
    }

}
