package backend.academy.loganalyzer.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class ToFileOutput {

    public static void saveToFile(String content, String format) {
        String fileName = "log-analyze-output." + format;

        String currentDirectory = System.getProperty("user.dir");
        File file = new File(currentDirectory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            log.info("Файл сохранён: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
