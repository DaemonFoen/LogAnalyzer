package backend.academy.loganalyzer.cli;

import backend.academy.loganalyzer.cli.LogAnalyzerCLI.FilterFieldValueConverter;
import backend.academy.loganalyzer.cli.LogAnalyzerCLI.FormatValidator;
import backend.academy.loganalyzer.cli.LogAnalyzerCLI.LocalDateTimeConverter;
import backend.academy.loganalyzer.cli.LogAnalyzerCLI.SplitPathConverter;
import com.beust.jcommander.Parameter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@SuppressWarnings("all")
public class ArgsImpl implements Args {

    @Parameter(names = "--path", description = "Путь к лог-файлам (локальный или URL)", required = true, splitter = SplitPathConverter.class)
    private List<String> paths;

    @Parameter(names = "--from", description = "Начальная дата в формате ISO8601", converter = LocalDateTimeConverter.class)
    private LocalDateTime from;

    @Parameter(names = "--to", description = "Конечная дата в формате ISO8601", converter = LocalDateTimeConverter.class)
    private LocalDateTime to;

    @Parameter(names = "--format", description = "Формат вывода: markdown или adoc (по умолчанию markdown", validateWith = FormatValidator.class)
    private String format = "markdown";

    @Parameter(names = "--filter", description = "Пары фильтрации в формате field:value (допустимые поля: address, method, url, status, agent)", converter = FilterFieldValueConverter.class)
    private List<Map.Entry<LogRecordField, String>> filters;

    @Parameter(names = "--help", help = true, description = "Вывод помощи") private boolean help;

}

