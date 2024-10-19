package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;
import java.util.HashMap;
import java.util.Map;

public class UniqueIPCountOperator implements Operator<Integer> {

    private final Map<String, Integer> uniqueIpCount = new HashMap<>();

    @Override
    public LogRecord apply(LogRecord r) {
        uniqueIpCount.merge(r.remoteAddr(), 1, Integer::sum);
        return r;
    }

    @Override
    public Integer getMetricValue() {
        return uniqueIpCount.size();
    }

    @Override
    public String getMetricName() {
        return "Уникальные IP-адреса";
    }

}
