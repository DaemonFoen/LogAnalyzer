package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;
import java.util.HashMap;
import java.util.Map;

public class ResourceCountOperator implements MapOutputOperator<String, Long> {

    private final Map<String, Long> requestedResources = new HashMap<>();

    @Override
    public LogRecord apply(LogRecord r) {
        requestedResources.merge(r.requestUrl(), 1L, Long::sum);
        return r;
    }

    @Override
    public Map<String, Long> getMetricValue() {
        return requestedResources;
    }

    @Override
    public String getMetricName() {
        return "Запрашиваемые ресурсы";
    }
}
