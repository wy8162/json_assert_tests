package y.w.jsonparity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.ArrayValueMatcher;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONParser;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

/**
 * http://jsonassert.skyscreamer.org/apidocs/index.html
 */
public class JSONAssertArrayMatcher {

    private final String ARRAY_OF_JSONOBJECTS =
        "{a:[{background:white, id:1, type:row},\n"
            + "     {background:grey,  id:2, type:row},\n"
            + "     {background:white, id:3, type:row},\n"
            + "     {background:grey,  id:4, type:row}]}";

    @Test
    @DisplayName("To verify that the 'id' attribute of first element of array 'a' is '1'")
    public void testIdOf1stElementIs1() throws JSONException {
        JSONComparator comparator = new DefaultComparator(JSONCompareMode.LENIENT);
        Customization customization = new Customization("a",
            new ArrayValueMatcher<Object>(comparator, 0));
        JSONAssert.assertEquals("{a:[{id:1}]}", ARRAY_OF_JSONOBJECTS,
            new CustomComparator(JSONCompareMode.LENIENT, customization));
    }

    @Test
    @DisplayName("To verify that the 'type' attribute of second and third elements of array 'a' is 'row'")
    public void test2nd3rdHaveTypeRow() throws JSONException {
        JSONComparator comparator = new DefaultComparator(JSONCompareMode.LENIENT);
        Customization customization = new Customization("a",
            new ArrayValueMatcher<Object>(comparator, 1, 2));
        JSONAssert.assertEquals("{a:[{type:row}]}", ARRAY_OF_JSONOBJECTS,
            new CustomComparator(JSONCompareMode.LENIENT, customization));
    }

    @Test
    @DisplayName("To verify that the 'id' attribute of every element of array 'a' matches digit only")
    public void moreAdvancedCase() throws JSONException {
        // get length of array we will verify
        int aLength = ((JSONArray) ((JSONObject) JSONParser.parseJSON(ARRAY_OF_JSONOBJECTS)).get(
            "a")).length();

        // create array of customizations one for each array element
        RegularExpressionValueMatcher<Object> regExValueMatcher =
            new RegularExpressionValueMatcher<Object>("\\d+");  // matches one or more digits

        Customization[] customizations = new Customization[aLength];
        for (int i = 0; i < aLength; i++) {
            String contextPath = "a[" + i + "].id";
            customizations[i] = new Customization(contextPath, regExValueMatcher);
        }

        CustomComparator regExComparator = new CustomComparator(JSONCompareMode.STRICT_ORDER,
            customizations);

        ArrayValueMatcher<Object> regExArrayValueMatcher = new ArrayValueMatcher<Object>(
            regExComparator);

        Customization regExArrayValueCustomization = new Customization("a", regExArrayValueMatcher);

        CustomComparator regExCustomArrayValueComparator =
            new CustomComparator(JSONCompareMode.STRICT_ORDER,
                new Customization[]{regExArrayValueCustomization});

        JSONAssert.assertEquals("{a:[{id:X}]}", ARRAY_OF_JSONOBJECTS,
            regExCustomArrayValueComparator);

    }
}
