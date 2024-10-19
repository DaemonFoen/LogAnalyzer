package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;
import com.tdunning.math.stats.TDigest;

public class PercentileOperator implements Operator<Long> {

    private final TDigest tDigest;

    private final double percentile;

    public PercentileOperator(double percentile, double compression) {
        tDigest = TDigest.createDigest(compression);
        this.percentile = percentile;
    }

    @Override
    public LogRecord apply(LogRecord r) {
        tDigest.add(r.bodyBytesSent());
        return r;
    }

    @Override
    public Long getMetricValue() {
        return (long) tDigest.quantile(percentile);
    }

    @Override
    public String getMetricName() {
        return "95p размера ответа";
    }
}
