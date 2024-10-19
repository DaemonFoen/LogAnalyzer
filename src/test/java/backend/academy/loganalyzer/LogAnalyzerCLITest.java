package backend.academy.loganalyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import backend.academy.loganalyzer.cli.LogAnalyzerCLI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogAnalyzerCLITest {

    LogAnalyzerCLI logAnalyzer = new LogAnalyzerCLI();

    @Test
    void parseArgumentsTest() {
        String[] args = {"--path",
            "src/main/resources/logs/**/2024-08-31.txt",
            "--from",
            "2015-05-17",
            "--to",
            "2015-05-18",
            "--format",
            "adoc",
            "--filter",
            "agent:Debian",
            "--filter",
            "method:GET"};
        List<String> paths = List.of("src\\main\\resources\\logs\\logs1\\2024-08-31.txt", "src\\main\\resources\\logs\\logs2\\2024-08-31.txt", "src\\main\\resources\\logs\\logs3\\2024-08-31.txt");

        var result = logAnalyzer.parse(args);

        assertEquals(paths, result.paths());
        assertEquals(LocalDate.parse("2015-05-18", DateTimeFormatter.ISO_DATE).atStartOfDay(), result.to());
        assertEquals(LocalDate.parse("2015-05-17", DateTimeFormatter.ISO_DATE).atStartOfDay(), result.from());
        assertEquals("adoc", result.format());
        assertEquals("Debian", result.filters().getFirst().getValue());
        assertEquals("GET", result.filters().getLast().getValue());
    }

}