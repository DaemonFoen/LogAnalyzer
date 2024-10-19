package backend.academy.loganalyzer.formats;

import backend.academy.loganalyzer.TestUtils;
import backend.academy.loganalyzer.cli.Args;
import backend.academy.loganalyzer.processing.LogAnalyzeProcessor;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class MarkdownFormatterITCase {

    private final MarkdownFormatter formatter = new MarkdownFormatter();
    private final LogAnalyzeProcessor processor = new LogAnalyzeProcessor(TestUtils.getOperators());

    private final Args args = new Args() {
        @Override
        public List<String> paths() {
            return List.of("src/test/resources/nginx_logs.txt");
        }

        @Override
        public LocalDateTime from() {
            return null;
        }

        @Override
        public LocalDateTime to() {
            return null;
        }

        @Override
        public String format() {
            return "markdown";
        }

        @Override
        public List<Entry<LogRecordField, String>> filters() {
            return List.of();
        }

        @Override
        public boolean help() {
            return false;
        }
    };

    @Test
    void markdownCorrectFormatingTest() throws IOException {
        var operators = processor.compute(args);

        String result = formatter.format(operators);

        assertEquals("""
            #### Общая информация
            | Метрика               | Значение   |
            |:---------------------:|-----------:|
            | Количество запросов | 51462 |\s
            | Средний размер ответа | 659509 |\s
            | Уникальные IP-адреса | 2660 |\s
            | 95p размера ответа | 1762 |\s

            #### Количество запросов по часам
            |:--------------------:|-------------:|
            | 0 | 2168 |
            | 1 | 2163 |
            | 2 | 2135 |
            | 3 | 2145 |
            | 4 | 2157 |
            | 5 | 2182 |
            | 6 | 2116 |
            | 7 | 2041 |
            | 8 | 2092 |
            | 9 | 2159 |
            | 10 | 2131 |
            | 11 | 2102 |
            | 12 | 2174 |
            | 13 | 2170 |
            | 14 | 2151 |
            | 15 | 2178 |
            | 16 | 2119 |
            | 17 | 2120 |
            | 18 | 2166 |
            | 19 | 2124 |
            | 20 | 2209 |
            | 21 | 2106 |
            | 22 | 2178 |
            | 23 | 2176 |

            #### Запрашиваемые ресурсы
            |:--------------------:|-------------:|
            | /downloads/product_3 | 73 |
            | /downloads/product_1 | 30285 |
            | /downloads/product_2 | 21104 |

            #### Коды ответа
            |:--------------------:|-------------:|
            | 206  Partial Content | 186 |
            | 403  Forbidden | 38 |
            | 200  OK | 4028 |
            | 404  Not Found | 33876 |
            | 416  Range Not Satisfiable | 4 |
            | 304  Not Modified | 13330 |
            """,result);
    }
}
