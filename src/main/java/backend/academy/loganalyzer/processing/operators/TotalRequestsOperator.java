package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;

public class TotalRequestsOperator implements Operator<Long> {

    private long totalRequests;

    @Override
    public LogRecord apply(LogRecord r) {
        totalRequests++;
        return r;
    }

    @Override
    public Long getMetricValue() {
        return totalRequests;
    }

    @Override
    public String getMetricName() {
        return "Количество запросов";
    }
}
