package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class AreConnectedNeighboursTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testJavadocExamples() {
        testExpected("A3C10", "A3C23", true);
        testExpected("A3C23", "B1B20", false);
        testExpected("A0B30", "A3B23", false);
        testExpected("A0B30", "A3C23", false);
    }

    @Test
    public void testValidConnections() {
        for (String[] invalid : VALID) {
            testExpected(invalid[0], invalid[1], true);
        }
    }

    @Test
    public void testInvalidConnections() {
        testTrivialCorrect();
        for (String[] invalid : INVALID) {
            testExpected(invalid[0], invalid[1], false);
        }
    }

    @Test
    public void testDisconnected() {
        testTrivialCorrect();
        for (String[] invalid : DISCONNECTED) {
            testExpected(invalid[0], invalid[1], false);
        }
    }

    @Test
    public void testNotNeighbours() {
        testTrivialCorrect();
        for (String[] invalid : NOT_NEIGHBOURS) {
            testExpected(invalid[0], invalid[1], false);
        }
    }

    static final String[][] VALID = {
            {"A4A10", "A3B10"},
            {"A4A10", "A3B13"},
            {"A3B13", "A4B21"},
            {"A3B13", "B0B01"},
            {"A4B21", "S1B37"},
            {"S1B37", "B0C32"},
            {"B0C32", "A4D34"},
            {"A2B42", "B0A40"},
            {"A2B42", "S4C43"},
            {"S4C43", "S3C51"},
            {"S2D40", "S5E46"},
            {"S5E46", "A4F42"},
            {"A0C30", "A0C24"},
            {"A3A12", "A5A02"},
            {"A3G10", "B1G23"},
            {"A3G16", "B1G27"}
    };

    static final String[][] INVALID = {
            {"A4A50", "A1B50"},
            {"B2G50", "A1F50"},
            {"A3C10", "A0C20"},
            {"A2G40", "S2F40"},
            {"A3C21", "S5C34"},
            {"S3F40", "S2E40"},
            {"S3F40", "S4E42"},
            {"B1C33", "S0B35"},
            {"S1F32", "S5E36"},
            {"A2E36", "B0E47"},
            {"B2B42", "A1C42"},
            {"A3G10", "B1G22"}
    };

    static final String[][] DISCONNECTED = {
            {"B2G50", "A1G42"},
            {"A3C10", "B1C20"},
            {"A2G40", "A0F40"},
            {"A3C21", "B1C31"},
            {"B1C33", "A1D31"},
            {"A2B30", "A0A34"},
            {"S1F32", "B0E33"},
            {"A2E36", "B0E44"},
            {"B2B42", "A0C42"},
            {"A3A12", "A5A06"},
            {"A3G10", "B1G25"}
    };

    static final String[][] NOT_NEIGHBOURS = {
            {"A4A10", "A3B20"},
            {"A4A00", "A3B13"},
            {"A3C13", "A4B21"},
            {"A3B13", "B0A01"},
            {"A4B21", "S1B57"},
            {"S1B37", "B0E32"},
            {"B0B32", "A4D34"},
            {"A2B32", "B0A40"},
            {"A2C32", "S4B43"},
            {"S4A43", "S3C51"},
            {"S2E60", "S5E46"},
            {"S5A46", "A4F42"}
    };

    private void testTrivialCorrect() {
        testExpected("A3C10", "A3C23", true);
    }

    private void testExpected(String placementA, String placementB, boolean expected) {
        boolean result = RailroadInk.areConnectedNeighbours(placementA, placementB);
        assertTrue("RailroadInk.areConnectedNeighbours(" + placementA + ", " + placementB + ") expected " + expected + " but returned " + result, result == expected);

    }
}
