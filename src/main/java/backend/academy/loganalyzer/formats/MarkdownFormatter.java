package backend.academy.loganalyzer.formats;

import backend.academy.loganalyzer.processing.operators.MapOutputOperator;
import backend.academy.loganalyzer.processing.operators.Operator;
import java.util.ArrayList;
import java.util.List;

public class MarkdownFormatter implements Formatter {

    @Override
    @SuppressWarnings("all")
    public String format(List<Operator<?>> operators) {
        StringBuilder result = new StringBuilder();
        List<MapOutputOperator<?, ?>> mapOutputOperators = new ArrayList<>();

        result.append("""
            #### Общая информация
            | Метрика               | Значение   |
            |:---------------------:|-----------:|
            """);

        for (Operator<?> operator : operators) {
            if (operator instanceof MapOutputOperator) {
                mapOutputOperators.add((MapOutputOperator<?, ?>) operator);
            } else {
                result.append("| ").append(operator.getMetricName()).append(" | ").append(operator.getMetricValue())
                    .append(" | ").append("\n");
            }
        }

        for (MapOutputOperator<?, ?> mapOutputOperator : mapOutputOperators) {
            result.append("\n#### ").append(mapOutputOperator.getMetricName()).append("\n");
            result.append("|:--------------------:|-------------:|\n");

            mapOutputOperator.getMetricValue().forEach((k, v) -> result.append("| ").append(k).append(" | ").append(v).append(" |\n"));
        }

        return result.toString();
    }
}

