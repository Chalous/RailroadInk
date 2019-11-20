package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GenerateDiceRollTest {

    private static int BASE_ITERATIONS = 10000;

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testFourDice() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            int numRolls = roll.length() / 2;
            assertFalse("Expected a string of four die rolls, but you rolled: " + numRolls, numRolls != 4);
        }
    }

    @Test
    public void testCorrectDice() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            int a = 0;
            int b = 0;
            int x = 0;
            for (int j = 0; j < roll.length(); j += 2) {
                if (roll.charAt(j) == 'A') {
                    a++;
                } else if (roll.charAt(j) == 'B') {
                    b++;
                } else {
                    x++;
                }
            }
            assertFalse("Expected dice A to be rolled 3 times, but you rolled it " + a + " time/s " + roll, a != 3);
            assertFalse("Expected dice B to be rolled once, but you rolled it " + b + " time/s: " + roll, b != 1);
            assertFalse("Expected dice A and B but you rolled a different die" + x + " time/s " + roll, x != 0);
        }

    }

    private char[][] validFace = {{'0', '1', '2', '3', '4', '5'}, {'0', '1', '2'}};

    @Test
    public void testCorrectFace() {
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            for (int j = 0; j < roll.length(); j += 2) {
                char a = roll.charAt(j);
                char b = roll.charAt(j + 1);
                assertTrue("Invalid die: " + a, a - 'A' >= 0 && a - 'A' <= 1);
                assertTrue("Invalid face for die " + roll.charAt(j) + ": " + b, Arrays.binarySearch(validFace[a - 'A'], b) >= 0);
            }
        }
    }

    @Test
    public void testDieFaces() {
        int[] countsA = new int[6];
        int[] countsB = new int[3];
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String roll = RailroadInk.generateDiceRoll();
            for (int j = 1; j < 6; j += 2) {
                int face = Character.getNumericValue(roll.charAt(j));
                switch (face) {
                    case 0:
                        countsA[0]++;
                        break;
                    case 1:
                        countsA[1]++;
                        break;
                    case 2:
                        countsA[2]++;
                        break;
                    case 3:
                        countsA[3]++;
                        break;
                    case 4:
                        countsA[4]++;
                        break;
                    case 5:
                        countsA[5]++;
                        break;
                }
                assertFalse("Expected a number between 0 and 5, but you rolled: " + face, face < 0 || face > 5);
            }
            for (int j = 7; j < 8; j += 2) {
                int face = Character.getNumericValue(roll.charAt(j));
                switch (face) {
                    case 0:
                        countsB[0]++;
                        break;
                    case 1:
                        countsB[1]++;
                        break;
                    case 2:
                        countsB[2]++;
                        break;
                }
                assertFalse("Expected a number between 0 and 2, but you rolled: " + face, face < 0 || face > 2);
            }
        }
        assertTrue("Expected your dice A to roll at least one of each value from 0-5 but missed a value", Arrays.stream(countsA).min().getAsInt() > 0);
        assertTrue("Expected your dice B to roll at least one of each value from 0-2 but missed a value", Arrays.stream(countsB).min().getAsInt() > 0);
        double[] probsA = new double[]{1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0};
        double[] probsB = new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0};
        int samples = BASE_ITERATIONS;
        double chiA = chiSquared(probsA, samples * 3, countsA);
        double chiB = chiSquared(probsB, samples, countsB);
        assertTrue("Distribution of A rolls don't appear to be uniform (chi squared value of " + chiA + ")", chiA < 14.45);
        assertTrue("Distribution of B rolls don't appear to be uniform (chi squared value of " + chiB + ")", chiB < 9.35);
    }


    private static double chiSquared(double[] expectedProbs, int samples, int[] counts) {
        double total = 0;
        for (int i = 0; i < expectedProbs.length; i++) {
            double mi = ((double) samples) * expectedProbs[i];
            total += ((double) counts[i] - mi) * ((double) counts[i] - mi) / mi;
        }
        return total;
    }

}
