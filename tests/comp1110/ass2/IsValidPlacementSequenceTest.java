package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsValidPlacementSequenceTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testGood() {
        Random r = new Random();

        for (int i = 0; i < SampleGames.COMPLETED_GAMES.length; i++) {
            String p = SampleGames.COMPLETED_GAMES[i];
            for (int j = 5; j < p.length(); j += 5) {
                String test = p.substring(0, j);
                assertTrue("Placement sequence '" + test + "' is valid, but failed ", RailroadInk.isValidPlacementSequence(test));
            }
        }
    }

    @Test
    public void testDuplicate() {
        testTrivialCorrect();
        for (String test : DUPLICATE_PLACEMENT) {
            assertFalse("Placement sequence '" + test + "'was invalid, but passed", RailroadInk.isValidPlacementSequence(test));
        }
    }

    @Test
    public void testNoValidConnection() {
        testTrivialCorrect();
        for (String test : NO_VALID_CONNECTION) {
            assertFalse("Placement sequence '" + test + "'was invalid, but passed", RailroadInk.isValidPlacementSequence(test));
        }
    }

    @Test
    public void testInvalidConnection() {
        testTrivialCorrect();
        for (String test : INVALID_CONNECTION) {
            assertFalse("Placement sequence '" + test + "'was invalid, but passed", RailroadInk.isValidPlacementSequence(test));
        }
    }

    @Test
    public void testInvalidEdge() {
        testTrivialCorrect();
        for (String test : INVALID_EDGE) {
            assertFalse("Placement sequence '" + test + "'was invalid, but passed", RailroadInk.isValidPlacementSequence(test));
        }
    }

    private void testTrivialCorrect() {
        String test = "A4A50A4G12B2G54A1G36";
        assertTrue("Placement sequence '" + test + "' is valid, but failed ", RailroadInk.isValidPlacementSequence(test));
    }

    static final String[] DUPLICATE_PLACEMENT = {
            "A3D61A3D53B0C52A0B52A2B63A4D41B0E60A0F61A3D31A3D23A2G30B0F34A3E32A1B01B2B10A1B21A0A63A4D01A1G41B0G12A2F12S2D10A4C10B2A10A2B33A1A30S5F11A4E21A3C21A3C31S4E11",
            "A2A30A0A43A3A50B2B50A4C50A3B20A2B43B0G12B0A14A2B33A0B11A3D61A2B21A3G52B1G45A3F52A4E50B2F41A3F33A1E40A1D40A3E32A3E27B0F10S0E12B1D17A4D01A1B61A0C43",
            "A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35S3B63A4A41A2B31A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27A2B23S2C10A1D50A0F23B2G25A3E30A4E41",
            "B1B02B1B02",
            "B1B02A4C00A4C00",
            "B1B02A4C00A5C01"
    };

    static final String[] NO_VALID_CONNECTION = {
            "A4B10A4C10B2D10",
            "A3B50B0C50A2D50"
    };

    static final String[] INVALID_CONNECTION = {
            "A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35A4A41S5B34A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27S3B20A4C10A1D50A0F23B2G25A3E30A4E41",
            "A2A30A0A43A3A50B2B50A4C50A2B43B0G12B0A14A2B33A0B11A3G52A3D61A2B21A4F50B1G45A3E52B2F41A3F33A2E40A1D40A3E32A3E27B0F10S0E12B1D17A4D01A1B61A0C43",
            "A4A10A1A30A4A50S1B32A1B01A1B61B2B10A1B21S5B50A1B41A4D01A4D61A3D12B0C30A3D50A4C10A4C50A1F01A1F61A4G10B1F12A4G50B1E10A1E21A3E52B1F56S4E31A1D30A1F30A4E41B0G30"
    };

    static final String[] INVALID_EDGE = {
            "A3D61A3D53B0C52A0B52A2B63A4D41B0E60A0F61A3D31A3D23A2G30B0F34A3E32A1B01B2B10A1B21A0A63A4D01A1G41B0G12S2D10A4C10B2A10A2B33B0A30S4E11A4E21A3C21A3C31S5F11",
            "A4A50A1F61A0B61S5F50B1F46A1F01S1F12A2F23A1E20B2D21S0D01A1C20A0B22B1A61A4D11A4G10B1G44A2G30A3C01A3C12B0B31A1B01A4B50B0C50A2F32A0E32A0E40A4D31B1D47A1B11"
    };
}
