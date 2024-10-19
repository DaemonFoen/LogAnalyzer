package backend.academy.loganalyzer.data;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class LogRecordImplTest {

    private final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss X", Locale.ENGLISH);

    @Test
    void parsingRecordTest() {
        String value = "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        var result = LogRecordImpl.of(value);

        assertEquals(0L, result.bodyBytesSent());
        assertEquals("93.180.71.3", result.remoteAddr());
        assertEquals(LocalDateTime.parse("17/May/2015:08:05:32 +0000", formatter), result.dateTime());
        assertEquals("GET", result.requestMethod());
        assertEquals("/downloads/product_1", result.requestUrl());
        assertEquals("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)" ,result.userAgent());
        assertEquals(304, result.status());
    }
}
