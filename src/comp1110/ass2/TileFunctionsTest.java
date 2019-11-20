package comp1110.ass2;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;

import java.awt.font.FontRenderContext;
import java.util.Random;


public class TileFunctionsTest extends Assert {
    @Test
    public void IfExistTest(){
        for (int i=0;i<7;i++){//create a group of String from A1 to G6
            for (int j=0;j<7;j++){
                StringBuilder a=new StringBuilder();
                char first='A';
                first+=i;
                a.append(first);
                a.append(j);
                String t=new String(a);
                String result="A0"+a+"0";
                if (t.equals("A1") || t.equals("A3") || t.equals("A5")//start to test
                        || t.equals("G1") || t.equals("G3") || t.equals("G5")
                        || t.equals("B0") || t.equals("D0") || t.equals("F0")
                        || t.equals("B6") || t.equals("D6") || t.equals("F6")){
                    assertTrue("expected true but get false "+a,Tile.ifExist(result));
                }
                else {
                    assertFalse("expected false but get true "+a,Tile.ifExist(result));
                }

            }
        }
    }
    String[] tiles={"S0A0","S1A0","S2A0","S3A0","S4A0","S5A0",
            "A0A0","A1A0","A2A0","A3A0","A4A0","A5A0",
            "B0A0","B1A0","B2A0",};
    @Test
    public void GetGatesTest(){
        for (String tile:tiles){
                String tileName=tile.substring(0,2);
                if (tileName.equals("A0")||tileName.equals("A1")||tileName.equals("A4")||tileName.equals("A5")||tileName.equals("B0")||tileName.equals("B1")){
                    for (int t=0;t<8;t++){                  //test if two-gates tile can be tested
                        String testTile = tile + t;
                        int count=0;
                        char[] tileChar = Tile.getGates(testTile).toCharArray();
                        for (char i : tileChar) {
                            if (i=='!'){
                                count++;
                            }
                        }
                        assertFalse("Some tiles should only have 2 no-gate sides, but get "+count+" gate(s)", count!=2);
                    }
                }
                if (tileName.charAt(0)=='S'||tileName.equals("B2")){//test if four-gates tile can be tested
                    for (int t=0;t<8;t++){
                        String testTile = tile + t;
                        char[] tileChar = Tile.getGates(testTile).toCharArray();
                        for (char i : tileChar) {
                                assertNotEquals("S tile should not have no-gate sides", i=='!');
                        }
                    }
                }
                if (tileName.equals("A2")||tileName.equals("A3")){//test if three-gates tile can be tested
                    for (int t=0;t<8;t++){
                        String testTile = tile + t;
                        int count=0;
                        char[] tileChar = Tile.getGates(testTile).toCharArray();
                        for (char i : tileChar) {
                            if (i=='!'){
                                count++;
                            }
                        }
                        assertFalse("Some tiles should only have 1 no-gate sides, but get "+count+" gate(s)", count!=1);
                    }
                }
                if (tileName.equals("S2")||tileName.equals("A3")||tileName.equals("A4")||tileName.equals("A5")){
                    for (int t=0;t<8;t++){
                        String testTile = tile + t;
                        char[] tileChar = Tile.getGates(testTile).toCharArray();
                        for (char i : tileChar) {
                            assertNotEquals("Some tiles should not have Railway", i=='R');
                        }
                    }
                }

        }
    }

}
