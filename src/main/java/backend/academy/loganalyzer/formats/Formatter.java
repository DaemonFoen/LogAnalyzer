package backend.academy.loganalyzer.formats;

import backend.academy.loganalyzer.processing.operators.Operator;
import java.util.List;

public interface Formatter {
    String format(List<Operator<?>> operators);
}
