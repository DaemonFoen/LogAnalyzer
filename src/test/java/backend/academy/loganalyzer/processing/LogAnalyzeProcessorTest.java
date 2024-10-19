package backend.academy.loganalyzer.processing;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.loganalyzer.TestUtils;
import backend.academy.loganalyzer.cli.Args;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;

class LogAnalyzeProcessorTest {

    LogAnalyzeProcessor logAnalyzer = new LogAnalyzeProcessor(TestUtils.getOperators());


    @Test
    void computeWithHalfTime() throws IOException {
        Args args = new Args() {
            @Override
            public List<String> paths() {
                return List.of("src/test/resources/nginx_logs.txt");
            }

            @Override
            public LocalDateTime from() {
                return null;
            }

            @Override
            public LocalDateTime to() {
                return LocalDate.parse("2015-05-26", DateTimeFormatter.ISO_DATE).atStartOfDay();
            }

            @Override
            public String format() {
                return "markdown";
            }

            @Override
            public List<Entry<LogRecordField, String>> filters() {
                return List.of();
            }

            @Override
            public boolean help() {
                return false;
            }
        };

        var result = logAnalyzer.compute(args);

        assertEquals(24847L, result.getFirst().getMetricValue());
    }

    @Test
    void computeWithHalfTimeWithFilterByAgent() throws IOException {
        Args args = new Args() {
            @Override
            public List<String> paths() {
                return List.of("src/test/resources/nginx_logs.txt");
            }

            @Override
            public LocalDateTime from() {
                return null;
            }

            @Override
            public LocalDateTime to() {
                return LocalDate.parse("2015-05-26", DateTimeFormatter.ISO_DATE).atStartOfDay();
            }

            @Override
            public String format() {
                return "markdown";
            }

            @Override
            public List<Entry<LogRecordField, String>> filters() {
                return List.of(Map.entry(LogRecordField.getByName("agent"), "Wget"));
            }

            @Override
            public boolean help() {
                return false;
            }
        };

        var result = logAnalyzer.compute(args);

        assertEquals(292L, result.getFirst().getMetricValue());
    }

    @Test
    void computeWithSpecificDate() throws IOException {
        Args args = new Args() {
            @Override
            public List<String> paths() {
                return List.of("src/test/resources/nginx_logs.txt");
            }

            @Override
            public LocalDateTime from() {
                return LocalDate.parse("2015-05-17", DateTimeFormatter.ISO_DATE).atStartOfDay();
            }

            @Override
            public LocalDateTime to() {
                return LocalDate.parse("2015-05-18", DateTimeFormatter.ISO_DATE).atStartOfDay();
            }

            @Override
            public String format() {
                return "markdown";
            }

            @Override
            public List<Entry<LogRecordField, String>> filters() {
                return List.of();
            }

            @Override
            public boolean help() {
                return false;
            }
        };

        var result = logAnalyzer.compute(args);

        assertEquals(1966L, result.getFirst().getMetricValue());
    }
}
