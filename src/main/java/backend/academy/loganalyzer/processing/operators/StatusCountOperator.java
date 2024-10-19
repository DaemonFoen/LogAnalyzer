package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.HttpStatusCode;
import backend.academy.loganalyzer.data.HttpStatusCodeRecord;
import backend.academy.loganalyzer.data.LogRecord;
import java.util.HashMap;
import java.util.Map;

public class StatusCountOperator implements MapOutputOperator<HttpStatusCodeRecord, Long> {

    private final Map<HttpStatusCodeRecord, Long> commonStatuses = new HashMap<>();

    @Override
    public LogRecord apply(LogRecord r) {
        commonStatuses.merge(new HttpStatusCodeRecord(r.status(), HttpStatusCode.of(r.status())), 1L, Long::sum);
        return r;
    }

    @Override
    public Map<HttpStatusCodeRecord, Long> getMetricValue() {
        return commonStatuses;
    }

    @Override
    public String getMetricName() {
        return "Коды ответа";
    }
}
