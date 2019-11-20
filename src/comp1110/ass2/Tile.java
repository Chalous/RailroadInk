package comp1110.ass2;

import java.util.IllegalFormatCodePointException;
import java.util.SplittableRandom;

/**
 * This class is used for creating tile
 * contains die, tie, location and direction variables
 */

public class Tile {
    private String die;
    private int row;
    private int col;

    public Tile(String die) {
        this.die = die;
    }

    public Tile(String die, int row, int col) {
        this.die = die;
        this.row = row;
        this.col = col;
    }

    public char getDieType() {
        return this.die.charAt(0);
    }
    public int getDieNum() { return this.die.charAt(1); }
    public char getDieRow() { return this.die.charAt(2); }
    public int getDieCol() {
        return this.die.charAt(3);
    }
    public int getDieOri() {
        return this.die.charAt(4);
    }
    public String getDie(){
        return die;
    }

    /**
     * Each tile have different gates with different orientation
     * give each tile with special String
     * This String contain 8 value
     * 1 N ( North ) 2 E ( East ) 3 S ( South ) 4 W ( West )
     * 5, 6, 7, 8 H/R Highway/Railway
     * if that don't have gate, mark '!'
     */
    public static String getGates(String dieString){
        String tileName=dieString.substring(0,2);
        int tileOri=Integer.parseInt(dieString.substring(4));
        String b="";
        //give different tile different string according to the orientation
        switch (tileName){
            case "S0":
                switch (tileOri){
                    case 0: case 4: b="NESWHHRH";
                    break;
                    case 1: case 5: b="NESWHHHR";
                    break;
                    case 2: case 6:  b="NESWRHHH";
                    break;
                    case 3: case 7: b="NESWHRHH";
                    break;
            }
            break;
            case "S1":
                switch (tileOri){
                    case 0: case 4:b="NESWHRRR";
                        break;
                    case 1: case 5: b="NESWRHRR";
                        break;
                    case 2: case 6:b="NESWRRHR";
                        break;
                    case 3: case 7:b="NESWRRRH";
                        break;
                }
                break;
            case "S2": b="NESWHHHH";
            break;
            case "S3": b="NESWRRRR";
            break;
            case "S4":
                switch (tileOri){
                    case 0: case 7:b="NESWHRRH";
                    break;
                    case 1: case 4:  b="NESWHHRR";
                    break;
                    case 2: case 5: b="NESWRHHR";
                    break;
                    case 3: case 6: b="NESWRRHH";
                    break;
                }
                break;
            case "S5":
                switch (tileOri){
                    case 0: case 2: case 4: case 6: b="NESWHRHR";
                    break;
                    case 1: case 3: case 5: case 7: b="NESWRHRH";
                    break;
                }
                break;
            case "A0":
                switch (tileOri){
                    case 0: case 7:b="NESWR!!R";
                        break;
                    case 1: case 4: b="NESWRR!!";
                        break;
                    case 2: case 5: b="NESW!RR!";
                        break;
                    case 3: case 6: b="NESW!!RR";
                        break;
                }
                break;
            case "A1":
                switch (tileOri){
                    case 0: case 2: case 4: case 6: b="NESWR!R!";
                        break;
                    case 1: case 3: case 5: case 7: b="NESW!R!R";
                        break;
                }
                break;
            case "A2":
                switch (tileOri){
                    case 0: case 6:b="NESWRRR!";
                        break;
                    case 1: case 7: b="NESW!RRR";
                        break;
                    case 2: case 4: b="NESWR!RR";
                        break;
                    case 3: case 5: b="NESWRR!R";
                        break;
                }
                break;
            case "A3":
                switch (tileOri){
                    case 0: case 6:b="NESWHHH!";
                        break;
                    case 1: case 7: b="NESW!HHH";
                        break;
                    case 2: case 4: b="NESWH!HH";
                        break;
                    case 3: case 5: b="NESWHH!H";
                        break;
                }
                break;
            case "A4":
                switch (tileOri){
                    case 0: case 2: case 4: case 6: b="NESWH!H!";
                        break;
                    case 1: case 3: case 5: case 7: b="NESW!H!H";
                        break;
                }
                break;
            case "A5":
                switch (tileOri){
                    case 0: case 7:b="NESWH!!H";
                        break;
                    case 1: case 4: b="NESWHH!!";
                        break;
                    case 2: case 5: b="NESW!HH!";
                        break;
                    case 3: case 6: b="NESW!!HH";
                        break;
                }
                break;
            case "B0":
                switch (tileOri){
                    case 0: case 4:b="NESWH!R!";
                        break;
                    case 1: case 5: b="NESW!H!R";
                        break;
                    case 2: case 6: b="NESWR!H!";
                        break;
                    case 3: case 7: b="NESW!R!H";
                        break;
                }
                break;
            case "B2":
                switch (tileOri){
                    case 0: case 2: case 4: case 6: b="NESWHRHR";
                        break;
                    case 1: case 3: case 5: case 7: b="NESWRHRH";
                        break;
                }
                break;
            case "B1":
                switch (tileOri){
                    case 0:  b="NESWHR!!";
                        break;
                    case 1:  b="NESW!HR!";
                        break;
                    case 2:  b="NESW!!HR";
                        break;
                    case 3:  b="NESWR!!H";
                        break;
                    case 4:  b="NESWH!!R";
                        break;
                    case 5:  b="NESWRH!!";
                        break;
                    case 6:  b="NESW!RH!";
                        break;
                    case 7:  b="NESW!!RH";
                        break;
                }
                break;

        }
        return b;
    }
/**checked if the tile on the exists are will placed**/
    public static boolean exitsPlacement(String die){
        //tileLoc means tile Location
        String tileLoc = die.substring(2, 4);

        if (tileLoc.equals("A1") || tileLoc.equals("A5")) {/***top exits***/
            String gates = Tile.getGates(die);
            if (gates.charAt(4) == 'H') {
                return true;
            }
        } else if (tileLoc.equals("A3")) {
            String gates = Tile.getGates(die);
            if (gates.charAt(4) == 'R') {
                return true;
            }
        } else if (tileLoc.equals("G1") || tileLoc.equals("G5")) {/***bottom exits***/
            String gates = Tile.getGates(die);
            if (gates.charAt(6) == 'H') {
                return true;
            }
        } else if (tileLoc.equals("G3")) {
            String gates = Tile.getGates(die);
            if (gates.charAt(6) == 'R') {
                return true;
            }
        } else if (tileLoc.equals("B0") || tileLoc.equals("F0")) {/***right exits***/
            String gates = Tile.getGates(die);
            if (gates.charAt(7) == 'R') {
                return true;
            }
        } else if (tileLoc.equals("D0")) {
            String gates = Tile.getGates(die);
            if (gates.charAt(7) == 'H') {
                return true;
            }
        } else if (tileLoc.equals("B6") || tileLoc.equals("F6")) {/***left exits***/
            String gates = Tile.getGates(die);
            if (gates.charAt(5) == 'R') {
                return true;
            }
        } else if (tileLoc.equals("D6")) {
            String gates = Tile.getGates(die);
            if (gates.charAt(5) == 'H') {
                return true;
            }
        }
        return false;
    }
    /**write it because the number of the exist is too many
     * and it is no appropriate to use a long if statement so many time**/
    public static boolean ifExist(String die){
        //find the tile location from given String
        String dieLoc=die.substring(2,4);
        //test if the die is put on the exist place
        if (dieLoc.equals("A1") || dieLoc.equals("A3") || dieLoc.equals("A5")
                || dieLoc.equals("G1") || dieLoc.equals("G3") || dieLoc.equals("G5")
                || dieLoc.equals("B0") || dieLoc.equals("D0") || dieLoc.equals("F0")
                || dieLoc.equals("B6") || dieLoc.equals("D6") || dieLoc.equals("F6")) {
            return true;
        }
        return false;
    }
}
