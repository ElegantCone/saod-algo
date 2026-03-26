package org.example.lab1.hashtable;

import java.util.List;

public record Bucket (List<Record> elements) {

    public boolean containsKey(String key) {
        return elements.stream().anyMatch(e -> e.key().equals(key));
    }
}

