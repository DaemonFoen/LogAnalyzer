package backend.academy.loganalyzer.data;

import java.time.LocalDateTime;

public interface LogRecord {
    String remoteAddr();
    LocalDateTime dateTime();
    String requestMethod();
    String requestUrl();
    int status();
    long bodyBytesSent();
    String userAgent();
}
