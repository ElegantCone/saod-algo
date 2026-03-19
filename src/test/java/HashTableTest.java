import org.example.hashtable.HashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;

public class HashTableTest {
    Random random = new Random();

    @Test
    public void insertRandomValues_success() {
        var table = new HashTable(random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000));
        for (int i = 0; i < 1000; i++) {
            var key = "key" + i;
            var value = "value" + i;
            table.insert(key, value);
            Assertions.assertEquals(value, table.get(key));
        }
    }

    @Test
    public void insertDuplicateValues_fail() {
        var table = new HashTable(random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000));
        for (int i = 0; i < 1000; i++) {
            var key = "key" + i;
            var value = "value" + i;
            table.insert(key, value);
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> table.insert("key1", "value1"));
    }

    @Test
    public void getNotExistingKey_nullExpected() {
        var table = new HashTable(random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000));
        for (int i = 0; i < 1000; i++) {
            var key = "key" + i;
            var value = "value" + i;
            table.insert(key, value);
        }
        Assertions.assertNull(table.get("notExistingKey"));
    }

    @Test
    public void insertValue_tryGetByHash_failed() {
        var table = new HashTable(random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000), random.nextInt(1_000, 100_000));
        for (int i = 0; i < 1000; i++) {
            var key = "key" + i;
            var value = "value" + i;
            table.insert(key, value);
        }
        Assertions.assertNull(table.get(String.valueOf(table.hash("key1"))));
    }
}
