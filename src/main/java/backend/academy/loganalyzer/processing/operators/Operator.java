package backend.academy.loganalyzer.processing.operators;

import backend.academy.loganalyzer.data.LogRecord;
import java.util.function.UnaryOperator;

/**
 * Описание оператора, обрабатывающего поток и хранящего статистику
 * @param <T> Тип значения метрики
 */
public interface Operator<T> extends UnaryOperator<LogRecord> {

    T getMetricValue();

    String getMetricName();
}
