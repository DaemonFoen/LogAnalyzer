package backend.academy.loganalyzer.processing;

import backend.academy.loganalyzer.cli.Args;
import backend.academy.loganalyzer.cli.Args.LogRecordField;
import backend.academy.loganalyzer.data.LogRecord;
import backend.academy.loganalyzer.data.LogRecordImpl;
import backend.academy.loganalyzer.processing.operators.Operator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SuppressWarnings("all")
public class LogAnalyzeProcessor {

    private final List<Operator<?>> operators;

    public LogAnalyzeProcessor(List<Operator<?>> operators) {
        this.operators = operators;
    }

    public List<Operator<?>> compute(Args args) throws IOException {
        Stream<String> logLines = getLogLines(args.paths());

        Stream<LogRecord> records = logLines
            .map(LogRecordImpl::of)
            .filter(r -> filterByDate(r, args.from(), args.to()))
            .filter(r -> filterByField(r, args.filters()));

        computeInputStream(records, new ArrayList<>(operators));

        return operators;
    }

    private boolean filterByField(LogRecord record, List<Map.Entry<LogRecordField, String>> filters) {
        if (filters == null) {
            return true;

        }

        for (Map.Entry<LogRecordField, String> filter : filters) {
            switch (filter.getKey()) {
                case USER_AGENT -> {
                    if (!record.userAgent().contains(filter.getValue())) {
                        return false;
                    }
                }
                case REQUEST_METHOD -> {
                    if (!record.requestMethod().equalsIgnoreCase(filter.getValue())) {
                        return false;
                    }
                }
                case REQUEST_URL -> {
                    if (!record.requestUrl().contains(filter.getValue())) {
                        return false;
                    }
                }
                case STATUS -> {
                    if (!String.valueOf(record.status()).equals(filter.getValue())) {
                        return false;
                    }
                }
                case REMOTE_ADDR -> {
                    if (!record.remoteAddr().equals(filter.getValue())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean filterByDate(LogRecord r, LocalDateTime from, LocalDateTime to) {
        if (from != null && r.dateTime().isBefore(from)) {
            return false;
        }
        return to == null || !r.dateTime().isAfter(to);
    }

    private void computeInputStream(Stream<LogRecord> records, List<UnaryOperator<LogRecord>> operators) {
        var it = operators.iterator();

        Function<LogRecord, LogRecord> composedOperator = it.next();

        while (it.hasNext()) {
            composedOperator = composedOperator.compose(it.next());
        }

        records.map(composedOperator).count();
    }

    private Stream<String> getLogLines(List<String> paths) throws IOException {
        if ((paths.getFirst().startsWith("http://") || paths.getFirst().startsWith("https://"))) {
            return new BufferedReader(new InputStreamReader(new URL(paths.getFirst()).openStream())).lines();
        } else {
            var it = paths.iterator();
            Stream<String> composedStream = Files.lines(Paths.get(it.next()));
            while (it.hasNext()) {
                composedStream = Stream.concat(composedStream, Files.lines(Paths.get(it.next())));
            }
            return composedStream;
        }
    }
}
