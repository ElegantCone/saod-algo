import org.example.extendible.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class ExtendibleTest {
    static Map<Integer, Integer> map;
    static Table table;
    static Random random = new Random();
    int iterations = 100;

    @BeforeEach
    public void beforeEach() {
        File file = new File("output");
        if (!file.exists()) {
            file.mkdir();
        } else {
            for (var f : file.listFiles()) {
                f.delete();
            }
        }
        var tableCapacity = random.nextInt(iterations, iterations * 10);
        table = new Table(tableCapacity);
        map = new HashMap<>();
        for (int i = 0; i < random.nextInt(tableCapacity, tableCapacity * 10); i++) {
            try {
                var key = random.nextInt();
                var value = random.nextInt();
                table.insert(key, value);
                map.put(key, value);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Test
    public void insert() {
        for (var entry : map.entrySet()) {
            Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
        }
    }

    @Test
    public void remove() {
        for (int i = 0; i < iterations; i++) {
            var keys = map.keySet().toArray(Integer[]::new);
            Integer key = keys[random.nextInt(0, map.size())];
            Assertions.assertTrue(table.remove(key));
            Assertions.assertNull(table.get(key));
            map.remove(key);
        }
        var nonExistingKeys = generateNonExistingKeys();
        for (var key : nonExistingKeys) {
            Assertions.assertFalse(table.remove(key));
        }
    }

    @Test
    public void update() {
        var keys = map.keySet().toArray(Integer[]::new);
        for (int i = 0; i < iterations; i++) {
            Integer key = keys[random.nextInt(0, map.size())];
            Integer value = random.nextInt();
            Assertions.assertTrue(table.update(key, value));
            Assertions.assertEquals(value, table.get(key));
        }
        var nonExistingKeys = generateNonExistingKeys();
        for (var key : nonExistingKeys) {
            Assertions.assertFalse(table.update(key, random.nextInt()));
        }
    }

    @Test
    public void get() {
        for (var entry : map.entrySet()) {
            Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
        }
        var nonExistingKeys = generateNonExistingKeys();
        for (var key : nonExistingKeys) {
            Assertions.assertNull(table.get(key));
        }
    }

    private List<Integer> generateNonExistingKeys() {
        var keys = new ArrayList<Integer>(iterations);
        while (keys.size() < iterations) {
            var key = random.nextInt();
            if (!map.containsKey(key)) {
                keys.add(key);
            }
        }
        return keys;
    }


}
