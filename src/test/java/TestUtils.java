import net.datafaker.Faker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initKeyValuePairs() throws IOException {
        try {
            var existingFile = new File("words.txt");
            if (existingFile.exists()) {
                existingFile.delete();
            } else {
                existingFile.createNewFile();
            }
        } catch (Exception ignored) {}
        var writer = new BufferedWriter(new FileWriter("words.txt"));
        var faker = new Faker();
        for (int i = 0; i < 100_000; i++) {
            writer.write(faker.expression("#{json 'key','#{Name.first_name}','value','#{Name.last_name}'}"));
            writer.newLine();
        }
    }
}
