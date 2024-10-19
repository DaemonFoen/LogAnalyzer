package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;
import java.util.HashMap;
import java.util.Map;

public class RequestsByHourOperator implements MapOutputOperator<Integer, Long> {

    private final Map<Integer, Long> requestsByHour = new HashMap<>();

    @Override
    public LogRecord apply(LogRecord r) {
        requestsByHour.merge(r.dateTime().getHour(), 1L, Long::sum);
        return r;
    }

    @Override
    public Map<Integer, Long> getMetricValue() {
        return requestsByHour;
    }

    @Override
    public String getMetricName() {
        return "Количество запросов по часам";
    }
}
