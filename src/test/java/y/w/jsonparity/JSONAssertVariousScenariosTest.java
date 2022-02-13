package y.w.jsonparity;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.ArraySizeComparator;

@DisplayName("Test JSONAssert")
public class JSONAssertVariousScenariosTest {

    @Nested
    @DisplayName("STRICT mode")
    class TestSTRICTMode {

        @Test
        @DisplayName("Verify equality of two JSON objects - STRICTly equal")
        public void assertEquality_Strictly() throws JSONException {
            JSONAssert.assertEquals(
                "{name: \"jack\", age: 10}",
                "{name: \"jack\", age: 10}",
                JSONCompareMode.STRICT);
        }

        @Test
        @DisplayName("Orders of object elements can be different")
        public void assertEquality_Orders_Can_Be_Different() throws JSONException {
            JSONAssert.assertEquals(
                "{name: \"jack\", age: 10}",
                "{age: 10, name: \"jack\"}",
                JSONCompareMode.STRICT);
        }

        @Test
        @DisplayName("Actual can not have extended elements")
        public void assertEquality_No_Extended_Elements() throws JSONException {
            assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
                JSONAssert.assertEquals(
                    "{name: \"jack\", age: 10}",
                    "{name: \"jack\", age: 10, lastName: \"w\"}",
                    JSONCompareMode.STRICT);
            });
        }
    }

    @Nested
    @DisplayName("STRICT mode")
    class TestLENIENTMode {

        @Test
        @DisplayName("Verify equality of two JSON objects - STRICTly equal")
        public void assertEquality_Strictly() throws JSONException {
            JSONAssert.assertEquals(
                "{name: \"jack\", age: 10}",
                "{name: \"jack\", age: 10}",
                JSONCompareMode.LENIENT);
        }

        @Test
        @DisplayName("Orders of object elements can be different")
        public void assertEquality_Orders_Can_Be_Different() throws JSONException {
            JSONAssert.assertEquals(
                "{name: \"jack\", age: 10}",
                "{age: 10, name: \"jack\"}",
                JSONCompareMode.LENIENT);
        }

        @Test
        @DisplayName("Actual can have extended elements")
        public void assertEquality_Has_Extended_Elements() throws JSONException {
            JSONAssert.assertEquals(
                "{name: \"jack\", age: 10}",
                "{name: \"jack\", age: 10, lastName: \"w\"}",
                JSONCompareMode.LENIENT);
        }
    }


    @Nested
    @DisplayName("Order of Array Elements - LENIENT and STRICT")
    class ArrayElementSequenceLENIENTMode {

        @Test
        @DisplayName("LENIENT - Orders Can Be Different")
        public void assertEquality_ArrayElementsSameOrder() throws JSONException {
            JSONAssert.assertEquals(
                "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                "[{name: \"jane\", age: 20}, { age: 10, name: \"jack\"}]",
                JSONCompareMode.LENIENT);
        }

        @Test
        @DisplayName("STRICT - Order Must Be SAME")
        public void assertEquality_ArrayElementsDifferentOrder() throws JSONException {
            assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
                JSONAssert.assertEquals(
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                    "[{name: \"jane\", age: 20}, { age: 10, name: \"jack\"}]",
                    JSONCompareMode.STRICT);
            });
        }
    }

    @Nested
    @DisplayName("Behavior of Extended Elements In Array")
    class ExtendedElementInArrayElements {
        @Test
        @DisplayName("LENIENT - Extended Element Allowed")
        public void assertEquality_ExtendedElementsAllowed() throws JSONException {
            JSONAssert.assertEquals(
                "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20, lastName: \"W\"}]",
                JSONCompareMode.LENIENT);
        }

        @Test
        @DisplayName("STRICT - Extended Element Allowed")
        public void assertEquality_ExtendedElementsNotAllowed() throws JSONException {
            assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
                JSONAssert.assertEquals(
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20, lastName: \"W\"}]",
                    JSONCompareMode.STRICT);
            });
        }

        @Test
        @DisplayName("LENIENT - Do Not Allows More Array Elements in Actual")
        public void assertEquality_NumberOfElementsMustMatch_LENIENT() throws JSONException {
            assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
                JSONAssert.assertEquals(
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}, {name: \"mike\", age: 30}]",
                    JSONCompareMode.LENIENT);
            });
        }

        @Test
        @DisplayName("STRICT - Do Not Allows More Array Elements in Actual")
        public void assertEquality_NumberOfElementsMustMatch_STRICT() throws JSONException {
            assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
                JSONAssert.assertEquals(
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}]",
                    "[{name: \"jack\", age: 10}, {name: \"jane\", age: 20}, {name: \"mike\", age: 30}]",
                    JSONCompareMode.STRICT);
            });
        }
    }

    @Test
    void testArratSize() throws JSONException {
        JSONAssert.assertEquals(
            // Specify the expected number of elements in actual
            "{a:[4]}",
            "{a: [1,2,3,4]}",
            new ArraySizeComparator(JSONCompareMode.LENIENT)
        );

        JSONAssert.assertEquals(
            // Specify the expected range of number of elements in actual
            "{a:[2,4]}",
            "{a: [1,2,3,4]}",
            new ArraySizeComparator(JSONCompareMode.LENIENT)
        );

        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
            JSONAssert.assertEquals(
                // Specify the expected range of number of elements in actual
                "{a:[2,4]}",
                "{a: [1,2,3,4,5]}",
                new ArraySizeComparator(JSONCompareMode.LENIENT)
            );
        });
    }
}