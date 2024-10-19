package backend.academy.loganalyzer.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record LogRecordImpl(
    String remoteAddr,
    LocalDateTime dateTime,
    String requestMethod,
    String requestUrl,
    int status,
    long bodyBytesSent,
    String userAgent
) implements LogRecord {

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(\\S+) - (\\S+) \\[(.+?)] \"(\\S+) (\\S+) \\S+\" (\\d{3}) (\\d+) \"([^\"]*)\" \"([^\"]*)\"");

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss X", Locale.ENGLISH);

    @SuppressWarnings("all")
    public static LogRecord of(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            String remoteAddr = matcher.group(1);
            String timeLocal = matcher.group(3);
            String requestMethod = matcher.group(4);
            String requestUrl = matcher.group(5);
            int status = Integer.parseInt(matcher.group(6));
            long bodyBytesSent = Long.parseLong(matcher.group(7));
            String userAgent = matcher.group(9);

            LocalDateTime dateTime = LocalDateTime.parse(timeLocal, FORMATTER);

            return new LogRecordImpl(remoteAddr, dateTime, requestMethod, requestUrl, status, bodyBytesSent, userAgent);
        } else {
            throw new IllegalArgumentException("Invalid log line: " + line);
        }
    }
}
