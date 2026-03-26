package perfect;

import org.example.lab1.hashtable.HashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashTableTest {
    Random random = new Random();
    HashTable table;
    Map<String, String> map;

    @BeforeEach
    public void beforeEach() {
        table = new HashTable(random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000));
        map = new HashMap<>();
        for (int i = 0; i < table.getTableSize(); i++) {
            try {
                var key = "key" + random.nextInt();
                var value = "value" + random.nextInt();
                table.insert(key, value);
                map.put(key, value);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }


    @Test
    public void insertRandomValues_success() {
        for (var entry : map.entrySet()) {
            Assertions.assertNotNull(table.get(entry.getKey()));
            Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
        }
    }

    @Test
    public void insertDuplicateValues_fail() {
        var key = getRandomKey();
        Assertions.assertThrows(IllegalArgumentException.class, () -> table.insert(key, "value1"));
    }

    @Test
    public void getNotExistingKey_nullExpected() {
        Assertions.assertNull(table.get("notExistingKey"));
    }

    @Test
    public void deleteExistingKey_removesOnlyThatKey() {
        var key = getRandomKey();
        Assertions.assertTrue(table.delete(key));
        Assertions.assertNull(table.get(key));
        for (var entry : map.entrySet()) {
            if (entry.getKey().equals(key)) continue;
            Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
        }
    }

    @Test
    public void deleteMissingKey_returnsFalse() {
        Assertions.assertFalse(table.delete("notExistingKey"));
    }

    @Test
    public void updateExistingKey_updatesValue() {
        var key = getRandomKey();
        var newValue = "newValue" + random.nextInt();
        Assertions.assertTrue(table.update(key, newValue));
        Assertions.assertEquals(newValue, table.get(key));
    }

    @Test
    public void updateMissingKey_returnsFalse() {
        Assertions.assertFalse(table.update("notExistingKey", "value"));
    }

    enum Operation {
        INSERT, DELETE, UPDATE
    }

    @Test
    public void randomOperations_matchesHashMap() {
        for (int i = 0; i < 1000; i++) {
            var operationIdx = random.nextInt(3);
            var operation = Operation.values()[operationIdx];
            var key = getRandomKey();
            switch (operation) {
                case INSERT -> {
                    var value = "value" + random.nextInt();
                    try {
                        table.insert(key, value);
                        map.put(key, value);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                case DELETE -> {
                    if (table.delete(key)) {
                        map.remove(key);
                    }
                }
                case UPDATE -> {
                    var newValue = "newValue" + random.nextInt();
                    if (table.update(key, newValue)) {
                        map.put(key, newValue);
                    }
                }
            }
            for (var entry : map.entrySet()) {
                Assertions.assertEquals(entry.getValue(), table.get(entry.getKey()));
            }
        }
    }

    private String getRandomKey() {
        var keyIdx = random.nextInt(0, map.size());
        return map.keySet().toArray()[keyIdx].toString();
    }
}
