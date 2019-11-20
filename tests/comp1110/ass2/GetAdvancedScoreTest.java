package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class GetAdvancedScoreTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testFullGameExample() {
        int advancedScore = RailroadInk.getAdvancedScore(SampleGames.exampleInBook);
        assertTrue(errorMessage(SampleGames.scoreExample, advancedScore), advancedScore == SampleGames.scoreExample[1]);
    }

    @Test
    public void testSampleGames() {
        for (int i = 0; i < SampleGames.COMPLETED_GAMES.length; i++) {
            String g = SampleGames.COMPLETED_GAMES[i];
            int[] scoreComponents = SampleGames.SCORES[i];
            int advancedScore = RailroadInk.getAdvancedScore(g);
            System.out.println("Sample game " + g + "\nscore " + advancedScore);
            assertTrue(errorMessage(scoreComponents, advancedScore), advancedScore == scoreComponents[1]);
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
     *
     * @param components    the score components
     * @param providedScore the total score computed by RailroadInk.getAdvancedScore
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
        sb.append(" with ").append(components[3]).append(" errors");
        sb.append(" longest railway ").append(components[4]);
        sb.append(" longest road ").append(components[5]).append(": expected total score ").append(components[1]);
        sb.append(" but RailroadInk.getAdvancedScore returned total score: ").append(providedScore);
        return sb.toString();
    }

}
