package lab1.perfect;

import org.example.lab1.perfect.PerfectHashing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PerfectHashingTest {
    static Random random = new Random();
    private PerfectHashing table;
    Map<String, String> map;

    @BeforeEach
    public void beforeEach() {
        table = new PerfectHashing(random.nextInt(100, 1_000));
        map = new HashMap<>(table.getTableSize());
        for (int i = 0; i < table.getTableSize(); i++) {
            var key = "key" + random.nextInt();
            var value = "value" + random.nextInt();
            map.put(key, value);
        }
        table.build(map);
    }

    @Test
    public void insert() {
        for (var entry : map.entrySet()) {
            Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
        }
    }

    @Test
    public void getNotExistingKey_nullExpected() {
        Assertions.assertNull(table.get("notExistingKey"));
    }

    @Test
    public void getNotExistingKey_WithExistingHash() {
        var collidingKeys = generateCollisions();
        for (var key : collidingKeys) {
            Assertions.assertEquals(map.get(key), table.get(key));
        }
    }

    private List<String> generateCollisions() {
        var collidingKeys = new ArrayList<String>();
        var hashes = new HashMap<Integer, String>();
        for (var entry : map.entrySet()) {
            hashes.put(table.hash(entry.getKey()), entry.getKey());
        }
        while (collidingKeys.size() < 3) {
            var key = "key" + random.nextInt();
            var hash = table.hash(key);
            if (hashes.containsKey(hash)) {
                collidingKeys.add(key);
            }
        }
        return collidingKeys;
    }
}
