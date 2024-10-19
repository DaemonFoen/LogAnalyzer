package backend.academy.loganalyzer.cli;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface Args {

    List<String> paths();

    LocalDateTime from();

    LocalDateTime to();

    String format();

    List<Map.Entry<LogRecordField, String>> filters();

    boolean help();

    enum LogRecordField {
        REMOTE_ADDR("address"), REQUEST_METHOD("method"), REQUEST_URL("url"), STATUS("status"), USER_AGENT("agent");

        private final String name;

        LogRecordField(String name) {
            this.name = name;
        }

        public static LogRecordField getByName(String name) {
            for (LogRecordField field : values()) {
                if (field.name.equals(name)) {
                    return field;
                }
            }
            return null;
        }

        @Override public String toString() {
            return name;
        }
    }
}
