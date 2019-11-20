package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 * <p>
 * Determine whether a board string is well-formed:
 * - it consists of exactly N five-character tile placements (where N = 1 .. 31);
 * - each piece placement is well-formed
 * - no more than three special tiles are included
 */
public class IsBoardStringWellFormedTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private static final int BASE_ITERATIONS = 100;

    @Test
    public void testEmpty() {
        testTrivialCorrect();
        assertFalse("Null board string is not OK, but passed", RailroadInk.isBoardStringWellFormed(null));
        assertFalse("Empty board string is not OK, but passed", RailroadInk.isBoardStringWellFormed(""));
    }

    @Test
    public void testIncomplete() {
        testTrivialCorrect();
        String incomplete = SampleGames.COMPLETED_GAMES[0].substring(0, 23);
        assertFalse("Board string '" + incomplete + "'was incomplete, but passed", RailroadInk.isBoardStringWellFormed(incomplete));
    }

    @Test
    public void testGood() {
        Random r = new Random();

        for (int i = 0; i < SampleGames.COMPLETED_GAMES.length; i++) {
            String p = SampleGames.COMPLETED_GAMES[i];
            for (int j = 0; j < BASE_ITERATIONS / 4; j++) {
                int start = r.nextInt(4);
                int end = start + r.nextInt(6 - start) + 1;
                String test = p.substring(5 * start, 5 * end);
                assertTrue("Board string '" + test + "' is well-formed, but failed ", RailroadInk.isBoardStringWellFormed(test));
            }
        }
    }

    @Test
    public void testBad() {
        testTrivialCorrect();
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String test = SampleGames.badlyFormedPiecePlacement(r);
            assertFalse("Board string '" + test + "' is badly formed, but passed", RailroadInk.isBoardStringWellFormed(test));
        }
    }

    @Test
    public void testSpecials() {
        testTrivialCorrect();
        for (String specials : TOO_MANY_SPECIALS) {
            assertFalse("Board string \"" + specials + "\" has too many special placements, but passed", RailroadInk.isBoardStringWellFormed(specials));
        }
    }

    @Test
    public void testTooLong() {
        testTrivialCorrect();
        for (String longString : TOO_LONG) {
            assertFalse("Board string \"" + longString + "\" is too long, but passed", RailroadInk.isBoardStringWellFormed(longString));
        }
    }

    private void testTrivialCorrect() {
        String test = "A4A50";
        assertTrue("Board string " + test + " is well-formed but failed.", RailroadInk.isTilePlacementWellFormed(test));
    }

    static final String[] TOO_MANY_SPECIALS = {
            "A4A50A0B61A3B52B1A35S0B41A1B31A0B22B2C50S1C44A1D40A1E40B2F41A0C27S4F50B0G52A2C30A2D32A1D21S2F30",
            "A4A10A1A30A1B30B0C32S0D30A3D41A4D51B1E44A2E30S5F31A3D21A3D13A1G30S2C10B2B10A1B01A3D01A0B23S4C23"
    };

    static final String[] TOO_LONG = {
            "A3A10A3A52A3G10B2F10S1B50A2B61A0C60A1B41B1A35A4A41A2B31A1C30B0D32A2C50A4E10A3D12B2B10A2F01A0G00A4D01B1A27S3B20A4C10A1D50A0F23B2G25A3E30A4E41S5E51B1E67A3G52B2G31A0F32A4G41",
            "A4A50A1F61A0B61S5F50B1F46A1F01S1F12A2F23A1E20B2D21A3D03A1C20A0B22B1A61A4D11A4G10B1G44A2G30A3C01A3C12B0B31A1B01A4B50B0C50A2F32A0E32A0E40A4D31B1D47A1B11B0D61A2D50A3G50A4G61"
    };
}
