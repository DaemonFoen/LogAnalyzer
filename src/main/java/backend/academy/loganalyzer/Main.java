package backend.academy.loganalyzer;

import backend.academy.loganalyzer.cli.LogAnalyzerCLI;
import backend.academy.loganalyzer.formats.AdocFormatter;
import backend.academy.loganalyzer.formats.Formatter;
import backend.academy.loganalyzer.formats.MarkdownFormatter;
import backend.academy.loganalyzer.output.ToFileOutput;
import backend.academy.loganalyzer.processing.LogAnalyzeProcessor;
import backend.academy.loganalyzer.processing.operators.AvrResponseOperator;
import backend.academy.loganalyzer.processing.operators.Operator;
import backend.academy.loganalyzer.processing.operators.PercentileOperator;
import backend.academy.loganalyzer.processing.operators.RequestsByHourOperator;
import backend.academy.loganalyzer.processing.operators.ResourceCountOperator;
import backend.academy.loganalyzer.processing.operators.StatusCountOperator;
import backend.academy.loganalyzer.processing.operators.TotalRequestsOperator;
import backend.academy.loganalyzer.processing.operators.UniqueIPCountOperator;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) throws IOException {
        LogAnalyzerCLI logAnalyzerCLI = new LogAnalyzerCLI();

        var arg = logAnalyzerCLI.parse(args);
        var logAnalyzer = new LogAnalyzeProcessor(getOperators());
        var rep = logAnalyzer.compute(arg);
        Formatter formatter;

        formatter = arg.format().equals("markdown") ? new MarkdownFormatter() : new AdocFormatter();

        ToFileOutput.saveToFile(formatter.format(rep), arg.format());
    }

    @SuppressWarnings("all")
    private static List<Operator<?>> getOperators() {
        AvrResponseOperator avrResponseOperator = new AvrResponseOperator();
        UniqueIPCountOperator uniqueIPCountOperator = new UniqueIPCountOperator();
        RequestsByHourOperator requestsByHourOperator = new RequestsByHourOperator();
        ResourceCountOperator resourceCountOperator = new ResourceCountOperator();
        StatusCountOperator statusCountOperator = new StatusCountOperator();
        PercentileOperator percentileOperator = new PercentileOperator(0.95, 100);
        TotalRequestsOperator totalRequestsOperator = new TotalRequestsOperator();

        return List.of(totalRequestsOperator, avrResponseOperator, uniqueIPCountOperator, requestsByHourOperator,
            resourceCountOperator, statusCountOperator, percentileOperator);
    }
}
