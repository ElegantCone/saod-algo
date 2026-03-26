package org.example.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private final List<Map<BandKey, List<Point>>> bands;

    public Table(int bandCount) {
        bands = new ArrayList<>(bandCount);
        for (int i = 0; i < bandCount; i++) {
            bands.add(new HashMap<>());
        }
    }

    public void add(int bandIdx, byte[] bandHash, Point point) {
        var key = new BandKey(bandHash);
        bands.get(bandIdx)
                .computeIfAbsent(key, ignored -> new ArrayList<>())
                .add(point);
    }

    public List<Point> getPoints(int bandIdx, byte[] bandHash) {
        return bands.get(bandIdx).getOrDefault(new BandKey(bandHash), List.of());
    }

    private record BandKey(byte[] hash) {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BandKey other)) return false;
            return Arrays.equals(hash, other.hash);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(hash);
        }
    }
}
