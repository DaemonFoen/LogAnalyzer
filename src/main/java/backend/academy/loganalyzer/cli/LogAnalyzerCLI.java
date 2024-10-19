package backend.academy.loganalyzer.cli;

import backend.academy.loganalyzer.cli.Args.LogRecordField;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.IParameterSplitter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogAnalyzerCLI {

    public Args parse(String[] args) {
        Args arg = new ArgsImpl();
        JCommander jCommander = JCommander.newBuilder().addObject(arg).build();

        try {
            jCommander.parse(args);

            if (arg.from() != null && arg.to() != null && arg.from().isAfter(arg.to())) {
                throw new ParameterException("дата from должна быть до to");
            }

            if (arg.help()) {
                jCommander.usage();
                System.exit(0);
            }

        } catch (ParameterException ex) {
            log.error(ex.getMessage());
            jCommander.usage();
            System.exit(1);
        }
        return arg;
    }

    public static class LocalDateTimeConverter implements IStringConverter<LocalDateTime> {

        private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_DATE_TIME,       // yyyy-MM-ddTHH:mm:ss
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"), // yyyy-MM-ddTHH:mm
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"), // yyyy-MM-ddTHH:mm:ss.SSS
            DateTimeFormatter.ISO_DATE             // yyyy-MM-dd
        );

        @Override
        public LocalDateTime convert(String value) {
            for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
                try {
                    return LocalDateTime.parse(value, formatter);
                } catch (DateTimeParseException e) {
                    // Если формат не подошел, продолжаем с другими
                }
            }

            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_DATE).atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты/времени: " + value);
            }
        }
    }

    public static class SplitPathConverter implements IParameterSplitter {

        @Override
        public List<String> split(String value) {
            if (value.startsWith("http://") || value.startsWith("https://")) {
                return List.of(value);
            } else {
                String[] splitPath = splitPath(value);
                return findLogFiles(splitPath[0], splitPath[1]);
            }
        }

        private String[] splitPath(String fullPath) {
            String[] ans = new String[2];
            if (fullPath.contains("*") || fullPath.contains("?")) {

                int lastSeparatorIndex = fullPath.lastIndexOf('/', fullPath.lastIndexOf("*"));

                if (lastSeparatorIndex == -1) {
                    ans[0] = ".";
                    ans[1] = fullPath;
                } else {
                    ans[0] = fullPath.substring(0, lastSeparatorIndex);
                    ans[1] = fullPath.substring(lastSeparatorIndex + 1);
                }
            } else {
                ans[0] = fullPath;
                ans[1] = "*";
            }
            return ans;
        }

        private static List<String> findLogFiles(String directory, String pattern) {
            List<String> logFiles = new ArrayList<>();
            Path dir = Paths.get(directory);

            if (!Files.isDirectory(dir)) {
                throw new IllegalArgumentException("Указанная директория не существует: " + directory);
            }

            // Преобразуем шаблон в регулярное выражение
            String regexPattern = pattern.replace("**", ".*").replace("*", "[^/]*");
            final Pattern compiledPattern = Pattern.compile(regexPattern);

            try {
                Files.walkFileTree(dir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            String relativePath = dir.relativize(file).toString().replace("\\", "/");
                            if (compiledPattern.matcher(relativePath).matches()) {
                                logFiles.add(file.toString());
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            return FileVisitResult.CONTINUE;
                        }
                    });
            } catch (IOException e) {
                log.error(e);
                throw new RuntimeException(e);
            }

            log.debug("Найденные файлы {}", logFiles);
            return logFiles;
        }

    }

    public static class FormatValidator implements IParameterValidator {

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!value.equals("markdown") && !value.equals("adoc")) {
                throw new ParameterException(name + " не валидный формат");
            }
        }
    }

    public static class FilterFieldValueConverter implements IStringConverter<Map.Entry<LogRecordField, String>> {

        @Override
        public Map.Entry<LogRecordField, String> convert(String value) {
            String[] parts = value.split(":", 2);
            if (parts.length != 2) {
                throw new ParameterException(
                    "Неправильный формат для фильтрации: '" + value + "'. Ожидается: field:value");
            }
            if (LogRecordField.getByName(parts[0]) == null) {
                throw new ParameterException(
                    "Недопустимый параметр фильтрации: '" + parts[0] + "'. Допустимые значения:" + Arrays.toString(
                        LogRecordField.values()));
            }
            return new HashMap.SimpleEntry<>(LogRecordField.getByName(parts[0]), parts[1]);
        }
    }

}



