package backend.academy.loganalyzer;

import backend.academy.loganalyzer.data.LogRecord;
import backend.academy.loganalyzer.data.LogRecordImpl;
import backend.academy.loganalyzer.processing.operators.AvrResponseOperator;
import backend.academy.loganalyzer.processing.operators.Operator;
import backend.academy.loganalyzer.processing.operators.PercentileOperator;
import backend.academy.loganalyzer.processing.operators.RequestsByHourOperator;
import backend.academy.loganalyzer.processing.operators.ResourceCountOperator;
import backend.academy.loganalyzer.processing.operators.StatusCountOperator;
import backend.academy.loganalyzer.processing.operators.TotalRequestsOperator;
import backend.academy.loganalyzer.processing.operators.UniqueIPCountOperator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

    public static Stream<LogRecord> getLogRecordStream() throws IOException {
        return Files.lines(Paths.get("src/test/resources/nginx_logs.txt")).map(LogRecordImpl::of);
    }

    public static List<Operator<?>> getOperators() {
        TotalRequestsOperator totalRequestsOperator = new TotalRequestsOperator();
        AvrResponseOperator avrResponseOperator = new AvrResponseOperator();
        UniqueIPCountOperator uniqueIPCountOperator = new UniqueIPCountOperator();
        RequestsByHourOperator requestsByHourOperator = new RequestsByHourOperator();
        ResourceCountOperator resourceCountOperator = new ResourceCountOperator();
        StatusCountOperator statusCountOperator = new StatusCountOperator();
        PercentileOperator percentileOperator = new PercentileOperator(0.95, 100);

        return List.of(totalRequestsOperator, avrResponseOperator, uniqueIPCountOperator, requestsByHourOperator,
            resourceCountOperator, statusCountOperator, percentileOperator);
    }

}
