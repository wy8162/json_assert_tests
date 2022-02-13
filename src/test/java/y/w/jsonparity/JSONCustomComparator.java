package y.w.jsonparity;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public class JSONCustomComparator {
    private static final String expected =
          "customerAccount: {"
              + "total: 10,"
              + "entries: ["
              + "{accountNumber: 001, type: card,  desc: card001},"
              + "{accountNumber: 002, type: check, desc: check001},"
              + "{accountNumber: 003, type: saving,desc: saving001},"
              + "]}";

    private static final String actual =
        "customerAccount: {"
            + "total: 10,"
            + "entries: ["
            + "{accountNumber: 001, type: card,  desc: card001},"
            + "{accountNumber: 002, type: check, desc: check001},"
            + "{accountNumber: 003, type: saving,desc: saving001},"
            + "]}";


    @Test
    void tAllMatched() throws JSONException {
        JSONCompareResult result = JSONCompare.compareJSON(
            "{\"total\": 10, \"entries\": ["
                + "{\"accountNumber\": \"001\", \"type\": \"card\", \"desc\": \"card001\"}, "
                + "{\"accountNumber\": \"002\", \"type\": \"check\", \"desc\": \"check001\"}, "
                + "{\"accountNumber\": \"003\", \"type\": \"saving\", \"desc\": \"saving001\"}]}\n",

            "{\"total\": 10, \"entries\": ["
                + "{\"accountNumber\": \"001\", \"type\": \"card\", \"desc\": \"card001\"}, "
                + "{\"accountNumber\": \"002\", \"type\": \"check\", \"desc\": \"check001\"}, "
                + "{\"accountNumber\": \"003\", \"type\": \"saving\", \"desc\": \"saving001\"}]}\n",
            JSONCompareMode.LENIENT
        );

        assertThat(result.passed()).isTrue();
    }

    @Test
    void tChangesUnMatched() throws JSONException {
        JSONCompareResult result = JSONCompare.compareJSON(
            "{\"total\": 10, \"entries\": ["
                + "{\"accountNumber\": \"001\", \"type\": \"card\", \"desc\": \"card001\"}, "
                + "{\"accountNumber\": \"002\", \"type\": \"check\", \"desc\": \"check001\"}, "
                + "{\"accountNumber\": \"003\", \"type\": \"saving\", \"desc\": \"saving001\"}]}\n",

            "{\"total\": 10, \"entries\": ["
                + "{\"accountNumber\": \"002\", \"type\": \"check\", \"desc\": \"check001\"}, "
                + "{\"accountNumber\": \"001\", \"type\": \"card\", \"desc\": \"card001\"}, "
                + "{\"accountNumber\": \"003\", \"type\": \"saving\", \"desc\": \"saving001\"}]}\n",

            JSONCompareMode.STRICT_ORDER
        );

        assertThat(result.passed()).isTrue();
    }
}
