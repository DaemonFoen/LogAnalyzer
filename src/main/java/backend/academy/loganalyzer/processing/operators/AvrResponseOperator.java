package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;

public class AvrResponseOperator implements Operator<Long> {

    private long avrResponseSize;

    private long totalRequests;

    @Override
    public LogRecord apply(LogRecord r) {
        avrResponseSize += r.bodyBytesSent();
        totalRequests++;
        return r;
    }

    @Override
    public Long getMetricValue() {
        return totalRequests > 0 ? avrResponseSize / totalRequests : 0L;
    }

    @Override
    public String getMetricName() {
        return "Средний размер ответа";
    }
}
