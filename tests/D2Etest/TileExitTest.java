package D2Etest;

import comp1110.ass2.Tile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileExitTest {

    @Test
    public void TileTest (){/// i want to test that the tile is exit, How?: I think we can
        String answer = "A";
        assertEquals("try my first test", "A1", Tile.exitsPlacement(answer));

    }

}
