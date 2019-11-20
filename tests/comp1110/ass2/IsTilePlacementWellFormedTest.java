package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsTilePlacementWellFormedTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private final static char[] VALID_PLACEMENT_CHARACTERS; //numeric 0-5, alpha 6-8
    private final static char[] VALID_LOCATION_CHARACTERS; //numeric 0-6, alpha 7-13
    private final static char[] INVALID_PLACEMENT_ALNUM; // C - G
    private final static char[] INVALID_FACE_NUMBER_A; // 6,7
    private final static char[] INVALID_FACE_NUMBER_B; // 3-7
    private final static char[] INVALID_LOCATION_CHARACTERS;
    private static Random r = new Random();

    static {
        //Populating valid placement characters
        VALID_PLACEMENT_CHARACTERS = new char[9];
        int index = 0;
        for (int i = 0; i <= 5; i++) {
            VALID_PLACEMENT_CHARACTERS[index] = (char) (i + '0');
            index++;
        }
        VALID_PLACEMENT_CHARACTERS[index++] = 'A';
        VALID_PLACEMENT_CHARACTERS[index++] = 'B';
        VALID_PLACEMENT_CHARACTERS[index] = 'S';

        //Populating location characters
        VALID_LOCATION_CHARACTERS = new char[14];
        index = 0;
        for (int i = 0; i <= 6; i++) {
            VALID_LOCATION_CHARACTERS[index] = (char) (i + '0');
            index++;
        }
        for (int i = 0; i <= 6; i++) {
            VALID_LOCATION_CHARACTERS[index] = (char) (i + 'A');
            index++;
        }

        INVALID_PLACEMENT_ALNUM = Arrays.copyOfRange(VALID_LOCATION_CHARACTERS, 9, VALID_LOCATION_CHARACTERS.length);
        INVALID_FACE_NUMBER_A = new char[]{'6', '7', '8'};
        INVALID_FACE_NUMBER_B = new char[]{'3', '4', '5', '6', '7'};
        INVALID_LOCATION_CHARACTERS = new char[]{'H', '!', 'I', '7', '8'};
    }

    private static String validPlacementGenerator() {
        char[] output = new char[5];
        output[0] = VALID_PLACEMENT_CHARACTERS[r.nextInt(3) + 6]; //A,B,S
        if (output[0] == 'B') {
            output[1] = VALID_PLACEMENT_CHARACTERS[r.nextInt(3)]; //0-3
        } else {
            output[1] = VALID_PLACEMENT_CHARACTERS[r.nextInt(6)]; //0-5
        }
        output[2] = VALID_LOCATION_CHARACTERS[r.nextInt(3) + 7]; // A - G
        output[3] = VALID_LOCATION_CHARACTERS[r.nextInt(7)]; //0-6
        output[4] = (char) ('0' + (r.nextInt(8))); //0-7
        return new String(output);
    }

    @Test
    public void validPlacements() {
        for (int i = 0; i <= 30; i++) {
            String validPlacement = validPlacementGenerator();
            assertTrue("Placement " + validPlacement + " is valid but was said to not be.", RailroadInk.isTilePlacementWellFormed(validPlacement));
        }
    }

    @Test
    public void testPlacementLength() {
        testTrivialCorrect();
        for (int i = 0; i <= 30; i++) {
            String validPlacement = validPlacementGenerator();
            if (r.nextBoolean()) {
                //Remove
                int random_location = r.nextInt(validPlacement.length());
                String lengthenedPlacement = validPlacement.substring(0, random_location)
                        + validPlacement.substring(random_location + 1);
                assertFalse(lengthenedPlacement +
                                " was said to be valid, but it is of the wrong length",
                        RailroadInk.isTilePlacementWellFormed(lengthenedPlacement));
            } else {
                int random_location = r.nextInt(validPlacement.length());
                int random_selection_from_valid = r.nextInt(VALID_PLACEMENT_CHARACTERS.length);
                String lengthenedPlacement = validPlacement.substring(0, random_location)
                        + VALID_PLACEMENT_CHARACTERS[random_selection_from_valid]
                        + validPlacement.substring(random_location);
                assertFalse(lengthenedPlacement +
                                " was said to be valid, but it is of the wrong length",
                        RailroadInk.isTilePlacementWellFormed(lengthenedPlacement));
            }
        }
    }

    @Test
    public void incorrectCharacters() {
        testTrivialCorrect();
        char[] invalidArray;
        for (int i = 0; i <= 30; i++) {
            String validPlacement = validPlacementGenerator();
            int selection = r.nextInt(3);
            switch (selection) {
                case 0:
                    //Change a placement and a location character
                    invalidArray = validPlacement.toCharArray();
                    invalidArray[0] = INVALID_PLACEMENT_ALNUM[r.nextInt(INVALID_PLACEMENT_ALNUM.length)];
                    invalidArray[1] = INVALID_FACE_NUMBER_A[r.nextInt(INVALID_FACE_NUMBER_A.length)];
                    invalidArray[2] = INVALID_LOCATION_CHARACTERS[r.nextInt(INVALID_LOCATION_CHARACTERS.length)];
                    invalidArray[3] = INVALID_LOCATION_CHARACTERS[r.nextInt(INVALID_LOCATION_CHARACTERS.length)];
                    assertFalse("Tile type, number and location characters are invalid. Expected placement " + new String(invalidArray) + " to return false, but returned true.", RailroadInk.isTilePlacementWellFormed(new String(invalidArray)));
                    break;
                case 1:
                    // Change just a placement number
                    invalidArray = validPlacement.toCharArray();
                    if (invalidArray[0] == 'B') {
                        invalidArray[1] = INVALID_FACE_NUMBER_B[r.nextInt(INVALID_FACE_NUMBER_B.length)];
                    } else {
                        invalidArray[1] = INVALID_FACE_NUMBER_A[r.nextInt(INVALID_FACE_NUMBER_A.length)];
                    }
                    assertFalse("Tile numbers are invalid. Expected placement " + new String(invalidArray) + " to return false, but returned true.", RailroadInk.isTilePlacementWellFormed(new String(invalidArray)));
                    break;
                case 2:
                    //Change just a location character
                    invalidArray = validPlacement.toCharArray();
                    invalidArray[2] = INVALID_LOCATION_CHARACTERS[r.nextInt(INVALID_LOCATION_CHARACTERS.length)];
                    invalidArray[3] = INVALID_LOCATION_CHARACTERS[r.nextInt(INVALID_LOCATION_CHARACTERS.length)];
                    assertFalse("Location characters are invalid. Expected placement " + new String(invalidArray) + " to return false, but returned true.", RailroadInk.isTilePlacementWellFormed(new String(invalidArray)));
                    break;
                default:
                    //error
                    return;
            }
        }
    }

    private void testTrivialCorrect() {
        String test = "A3D61";
        assertTrue("Tile placement " + test + " is well-formed but failed.", RailroadInk.isTilePlacementWellFormed(test));
    }
}

