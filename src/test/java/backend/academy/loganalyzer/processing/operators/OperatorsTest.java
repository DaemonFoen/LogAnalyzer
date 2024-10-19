package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.TestUtils;
import backend.academy.loganalyzer.data.HttpStatusCode;
import backend.academy.loganalyzer.data.HttpStatusCodeRecord;
import backend.academy.loganalyzer.data.LogRecord;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorsTest {

    @Test
    void correctProcessingAvrResponseOperatorTest() throws IOException {
        AvrResponseOperator operator = new AvrResponseOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();

        input.forEach(operator::apply);

        assertEquals(659509, operator.getMetricValue());
    }

    @Test
    void correctProcessingPercentileOperatorTest() throws IOException {
        PercentileOperator operator = new PercentileOperator(0.95, 100);
        Stream<LogRecord> input = TestUtils.getLogRecordStream();

        input.forEach(operator::apply);

        assertEquals(1762, operator.getMetricValue());
    }

    @Test
    void correctProcessingRequestsByHourOperatorTest() throws IOException {
        RequestsByHourOperator operator = new RequestsByHourOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();
        List<Integer> resultValue = List.of(2168, 2163, 2135, 2145, 2157, 2182, 2116, 2041, 2092, 2159, 2131,
            2102, 2174, 2170, 2151, 2178, 2119, 2120, 2166, 2124, 2209,
            2106, 2178, 2176);
        Map<Integer, Long> expected = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            expected.put(i, (long) resultValue.get(i));
        }

        input.forEach(operator::apply);

        assertEquals(expected, operator.getMetricValue());
    }

    @Test
    void correctProcessingResourceCountOperatorTest() throws IOException {
        ResourceCountOperator operator = new ResourceCountOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();
        var expected = Map.of("/downloads/product_3", 73L, "/downloads/product_1", 30285L, "/downloads/product_2", 21104L);

        input.forEach(operator::apply);

        assertEquals(expected, operator.getMetricValue());
    }

    @Test
    void correctProcessingStatusCountOperatorTest() throws IOException {
        StatusCountOperator operator = new StatusCountOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();
        var expected = Map.of(new HttpStatusCodeRecord(403, HttpStatusCode.of(403)), 38L,
            new HttpStatusCodeRecord(206, HttpStatusCode.of(206)), 186L,
            new HttpStatusCodeRecord(404, HttpStatusCode.of(404)), 33876L,
            new HttpStatusCodeRecord(416, HttpStatusCode.of(416)), 4L,
            new HttpStatusCodeRecord(200, HttpStatusCode.of(200)), 4028L,
            new HttpStatusCodeRecord(304, HttpStatusCode.of(304)), 13330L);

        input.forEach(operator::apply);

        assertEquals(expected, operator.getMetricValue());
    }

    @Test
    void correctProcessingTotalRequestsOperatorTest() throws IOException {
        TotalRequestsOperator operator = new TotalRequestsOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();

        input.forEach(operator::apply);

        assertEquals(51462, operator.getMetricValue());
    }

    @Test
    void correctProcessingUniqueIPCountOperatorTest() throws IOException {
        UniqueIPCountOperator operator = new UniqueIPCountOperator();
        Stream<LogRecord> input = TestUtils.getLogRecordStream();

        input.forEach(operator::apply);

        assertEquals(2660, operator.getMetricValue());
    }

}
