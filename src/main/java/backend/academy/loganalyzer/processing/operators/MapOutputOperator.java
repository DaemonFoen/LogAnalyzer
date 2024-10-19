package backend.academy.loganalyzer.processing.operators;

import java.util.Map;

/**
 * Реализация оператора, имеющего множество возвращаемых значений.
 * Данный тип оператора необходим для корректного формирования отчёта с метриками типа ключ:значение
 * @param <K> Тип ключа метрики
 * @param <V> Тип значения метрики
 */
public interface MapOutputOperator<K,V> extends Operator<Map<K,V>> {

}
