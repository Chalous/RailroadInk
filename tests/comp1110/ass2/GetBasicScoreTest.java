package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class GetBasicScoreTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    /**
     * Score:
     * Routes: 0
     * Centre: 1
     * Errors: -9
     * Highway: 4
     * Railway: 2
     * Basic Total: -8
     * Bonus Total: -2
     */
    String ex2 = "A4A16B2B14S2C13S5D12S3D25";


    /**
     * Route: 2 exits -> 4
     * Centre: 0
     * Errors: -1
     * Highway: 3
     * Railway: 3
     * Basic Total: 3
     * Bonus Total: 6
     */
    String ex3 = "A1A30A0B30A5A11B1B20S4A23";

    // TODO remove incomplete games (there's no requirement to score an incomplete game)

    @Test
    public void testSmallNoRoutes() {
        int basicScore = RailroadInk.getBasicScore(ex2);
        int[] scoreComponents = {-8, -2, 1, 9, 4, 2, 1};
        assertTrue(errorMessage(scoreComponents, basicScore), basicScore == scoreComponents[0]);
    }

    @Test
    public void testSmallWithRoutes() {
        int basicScore = RailroadInk.getBasicScore(ex3);
        int[] scoreComponents = {3, 6, 0, 1, 3, 3, 2};
        assertTrue(errorMessage(scoreComponents, basicScore), basicScore == scoreComponents[0]);
    }

    @Test
    public void testFullGameExample() {
        int basicScore = RailroadInk.getBasicScore(SampleGames.exampleInBook);
        assertTrue(errorMessage(SampleGames.scoreExample, basicScore), basicScore == SampleGames.scoreExample[0]);
    }

    @Test
    public void testSampleGames() {
        for (int i = 0; i < SampleGames.COMPLETED_GAMES.length; i++) {
            String g = SampleGames.COMPLETED_GAMES[i];
            int[] scoreComponents = SampleGames.SCORES[i];
            int basicScore = RailroadInk.getBasicScore(g);
            System.out.println("Sample game " + g + "\nscore " + basicScore);
            assertTrue(errorMessage(scoreComponents, basicScore), basicScore == scoreComponents[0]);
        }
    }

    /**
     * Construct an error message based on score components in the following order:
     * - total score (basic)
     * - total score (advanced)
     * - number of centre squares covered
     * - number of errors
     * - length of longest railway
     * - length of longest road
     * - [vararg] number of exits covered by each network (one or more values)
     * @param components the score components
     * @param providedScore the total score computed by RailroadInk.getBasicScore
     * @return an error message detailing the scoring components and the expected and computed total score
     */
    private String errorMessage(int[] components, int providedScore) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sample game including networks connecting [");
        boolean multipleNetworks = false;
        for (int i = 6; i < components.length; i++) {
            if (multipleNetworks) {
                sb.append(", ");
            } else {
                multipleNetworks = true;
            }
            sb.append(components[i]);
        }
        sb.append("] exits covering ").append(components[2]).append(" centre squares");
        sb.append(" with " + components[3]).append(" errors: expected total score ").append(components[0]);
        sb.append(" but RailroadInk.getBasicScore returned total score: ").append(providedScore);
        return sb.toString();
    }
}
