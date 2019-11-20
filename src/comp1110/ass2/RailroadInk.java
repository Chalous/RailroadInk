package comp1110.ass2;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import gittest.A;
import jdk.jshell.spi.SPIResolutionException;

import javax.print.DocFlavor;
import java.awt.font.FontRenderContext;
import java.util.EventListener;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

public class RailroadInk {
    /**
     * Determine whether a tile placement string is well-formed:
     * - it consists of exactly 5 characters;
     * - the first character represents a die A or B, or a special tile S
     * - the second character indicates which tile or face of the die (0-5 for die A and special tiles, or 0-2 for die B)
     * - the third character represents the placement row A-G
     * - the fourth character represents the placement column 0-6
     * - the fifth character represents the orientation 0-7
     *
     * @param tilePlacementString a candidate tile placement string
     * @return true if the tile placement is well formed
     */
    public static boolean isTilePlacementWellFormed(String tilePlacementString) {
        if (tilePlacementString.length() == 5) {
            if ((tilePlacementString.charAt(0) == 'A') || (tilePlacementString.charAt(0) == 'B') || (tilePlacementString.charAt(0) == 'S')) {
                if ((tilePlacementString.charAt(1) <= '5') && (tilePlacementString.charAt(1) >= '0') && ((tilePlacementString.charAt(0) == 'A') || (tilePlacementString.charAt(0) == 'S'))) {
                    if ((tilePlacementString.charAt(2) <= 'G') && (tilePlacementString.charAt(2) >= 'A')) {
                        if ((tilePlacementString.charAt(3) <= '6') && (tilePlacementString.charAt(3) >= '0')) {
                            if ((tilePlacementString.charAt(4) <= '7') && (tilePlacementString.charAt(4) >= '0')) {
                                return true;
                            }
                        }
                    }
                } else {
                    if ((tilePlacementString.charAt(1) <= '2') && (tilePlacementString.charAt(1) >= '0')) {
                        if ((tilePlacementString.charAt(2) <= 'G') && (tilePlacementString.charAt(2) >= 'A')) {
                            if ((tilePlacementString.charAt(3) <= '6') && (tilePlacementString.charAt(3) >= '0')) {
                                if ((tilePlacementString.charAt(4) <= '7') && (tilePlacementString.charAt(4) >= '0')) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        // FIXME Task 2: determine whether a tile placement is well-formed
        return false;
    }

    /**
     * Determine whether a board string is well-formed:
     * - it consists of exactly N five-character tile placements (where N = 1 .. 31);
     * - each piece placement is well-formed
     * - no more than three special tiles are included
     *
     * @param boardString a board string describing the placement of one or more pieces
     * @return true if the board string is well-formed
     */
    public static boolean isBoardStringWellFormed(String boardString) {
        if (boardString != null) {
            if ((boardString.length() % 5 == 0) && (boardString.length() <= 31 * 5) && (boardString.length() > 0)) {
                int count = 0;
                for (int i = 0; i < boardString.length(); i += 5) {
                    String die = boardString.substring(i, i + 5);
                    if (isTilePlacementWellFormed(die)) {
                        if ((die.charAt(0) == 'S')) {
                            count++;
                        }
                    } else {
                        return false;
                    }
                    if (count > 3) {
                        return false;
                    }
                }
                return true;
            }
        }
        // FIXME Task 3: determine whether a board string is well-formed
        return false;
    }


    /**
     * Determine whether the provided placements are neighbours connected by at least one validly connecting edge.
     * For example,
     * - areConnectedNeighbours("A3C10", "A3C23") would return true as these tiles are connected by a highway edge;
     * - areConnectedNeighbours("A3C23", "B1B20") would return false as these neighbouring tiles are disconnected;
     * - areConnectedNeighbours("A0B30", "A3B23") would return false as these neighbouring tiles have an
     * invalid connection between highway and railway; and
     * areConnectedNeighbours("A0B30", "A3C23") would return false as these tiles are not neighbours.
     *
     * @return true if the placements are connected neighbours
     */

    public static boolean areConnectedNeighbours(String tilePlacementStringA, String tilePlacementStringB) {
        String dieA = tilePlacementStringA.substring(0, 5);             /***get first tile information***/
        String tileYA = tilePlacementStringA.substring(2, 3);
        String tileXA = tilePlacementStringA.substring(3, 4);

        String dieB = tilePlacementStringB.substring(0, 5);             /***get second tile information***/
        String tileYB = tilePlacementStringB.substring(2, 3);
        String tileXB = tilePlacementStringB.substring(3, 4);

        if ((tileYA.charAt(0) - tileYB.charAt(0)) == 1) {               /***Second tile is above First tile***/ //B
                                                                                                                //A
            if (tileXA.charAt(0) == tileXB.charAt(0)) {
                String gatesA = Tile.getGates(dieA);
                String gatesB = Tile.getGates(dieB);
                if (gatesA.charAt(0) == 'N' && (gatesB.charAt(2) == 'S')) {
                    return (gatesA.charAt(4) != '!') && (((int) (gatesA.charAt(4)) - gatesB.charAt(6)) == 0);
                }
            }
        } else if (tileYB.charAt(0) - (tileYA.charAt(0)) == 1) {        /***First tile is above Second tile***///A
            if (tileXA.charAt(0) == tileXB.charAt(0)) {                                                        //B
                String gatesA = Tile.getGates(dieA);
                String gatesB = Tile.getGates(dieB);
                if (gatesA.charAt(2) == 'S' && (gatesB.charAt(0) == 'N')) {
                    return (gatesA.charAt(6) != '!') && (((int) (gatesA.charAt(6)) - gatesB.charAt(4)) == 0);
                }
            }
        } else if (tileXB.charAt(0) - (tileXA.charAt(0)) == 1) {        /***First tile is on Second tile's left hand***/
            if (tileYA.charAt(0) == tileYB.charAt(0)) {                 //AB
                String gatesA = Tile.getGates(dieA);
                String gatesB = Tile.getGates(dieB);
                if (gatesA.charAt(1) == 'E' && (gatesB.charAt(3) == 'W')) {
                    return (gatesA.charAt(5) != '!') && (((int) (gatesA.charAt(5)) - gatesB.charAt(7)) == 0);
                }
            }
        } else if (tileXA.charAt(0) - (tileXB.charAt(0)) == 1) {        /***First tile is on Second tile's right hand***/
            if (tileYA.charAt(0) == tileYB.charAt(0)) {                 //BA
                String gatesA = Tile.getGates(dieA);
                String gatesB = Tile.getGates(dieB);
                if (gatesA.charAt(3) == 'W' && (gatesB.charAt(1) == 'E')) {
                    return (gatesA.charAt(7) != '!') && (((int) (gatesA.charAt(7)) - gatesB.charAt(5)) == 0);
                }
            }
        }
        // FIXME Task 5: determine whether neighbouring placements are connected
        return false;
    }

    /**
     * Given a well-formed board string representing an ordered list of placements,
     * determine whether the board string is valid.
     * A board string is valid if each tile placement is legal with respect to all previous tile
     * placements in the string, according to the rules for legal placements:
     * - A tile must be placed such that at least one edge connects to either an exit or a pre-existing route.
     * Such a connection is called a valid connection.
     * - Tiles may not be placed such that a highway edge connects to a railway edge;
     * this is referred to as an invalid connection.
     * Highways and railways may only join at station tiles.
     * - A tile may have one or more edges touching a blank edge of another tile;
     * this is referred to as disconnected, but the placement is still legal.
     *
     * @param boardString a board string representing some placement sequence
     * @return true if placement sequence is valid
     */

    public static boolean isValidPlacementSequence(String boardString) {
        int originLenght=boardString.length();
        String clear=clearDuplications(boardString);
        if (originLenght!=clear.length()){
            return false;
        }

        for (int i = 0; i < boardString.length(); i += 5) {                  /***test each tile***/
            if (!Tile.exitsPlacement(boardString.substring(0, 5))) {/***if the first tile satisfy the rule***/
                return false;
            }
            String die = boardString.substring(i, i + 5);
            String dieLoc = die.substring(2, 4);
            if (dieLoc.equals("A1") || dieLoc.equals("A3") || dieLoc.equals("A5") || dieLoc.equals("G1") || dieLoc.equals("G3") || dieLoc.equals("G5") || dieLoc.equals("B0") || dieLoc.equals("D0") || dieLoc.equals("F0") || dieLoc.equals("B6") || dieLoc.equals("D6") || dieLoc.equals("F6")) {
                if (!Tile.exitsPlacement(die)) {/***if tile which put on the exist satisfy the rule***/
                    return false;
                }
            } else {
                int count = 0;
                for (int k = i; k > 0; k -= 5) {
                    if (areConnectedNeighbours(boardString.substring(k - 5, k), die)) {
                        break;/***if this tile connect with the tile before***/
                    } else {
                        count++;
                        if (count == (boardString.substring(0, i).length() / 5)) {
                            return false;/***if it test all the tiles and haven't found the connected tile return false***/
                        }
                    }
                }
            }
        }
        return true;
        // FIXME Task 6: determine whether the given placement sequence is valid
    }

    /**
     * Generate a random dice roll as a string of eight characters.
     * Dice A should be rolled three times, dice B should be rolled once.
     * Die A has faces numbered 0-5.
     * Die B has faces numbered 0-2.
     * Each die roll is composed of a character 'A' or 'B' representing the dice,
     * followed by a digit character representing the face.
     *
     * @return a String representing the die roll e.g. A0A4A3B2
     */
    public static String generateDiceRoll() {
        Random random = new Random();
        int a = random.nextInt(6) % 6;
        int b = random.nextInt(6) % 6;
        int c = random.nextInt(6) % 6;
        int d = random.nextInt(3) % 3;
        // FIXME Task 7: generate a dice roll
        return "A" + a + "A" + b + "A" + c + "B" + d;
    }
   /**write it to avoid making a method's length is too long**/
    private static int getRouteScore(int existNumber){
        int score=0;
        switch (existNumber){
            case 2: score+=4;
                break;
            case 3: score+=8;
                break;
            case 4: score+=12;
                break;
            case 5: score+=16;
                break;
            case 6: score+=20;
                break;
            case 7: score+=24;
                break;
            case 8: score+=28;
                break;
            case 9: score+=32;
                break;
            case 10: score+=36;
                break;
            case 11: score+=40;
                break;
            case 12: score+=45;
                break;
        }
        return score;
    }
    public static int getDisconnectedScore(String boardString){
        int disconnectNum=0;
        for (int i=0;i<boardString.length();i+=5){
            String die=boardString.substring(i,i+5);
            String a=getNeighbor(die,boardString);
            String dieLoc=die.substring(2,4);
            char dieY=die.charAt(2);
            char dieX=die.charAt(3);
            char dieOri=die.charAt(4);
            String dieName=die.substring(0,2);
            if ((dieY!='A'&&dieY!='G'&&dieX!='0'&&dieX!='6')){//when the tile is in the middle of the board
                if (dieName.equals("S0")||dieName.equals("S1")||
                        dieName.equals("S2")||dieName.equals("S3")
                        ||dieName.equals("S4")||dieName.equals("S5")||dieName.equals("B2")){
                    disconnectNum+=(4-a.length()/5);
                }
                else if (dieName.equals("A2")||dieName.equals("A3")){
                    disconnectNum+=(3-a.length()/5);
                }
                else {
                    disconnectNum+=(2-a.length()/5);
                }
            }
            else {//not in the edge
                        if (dieName.equals("S0")||dieName.equals("S1")||
                                dieName.equals("S2")||dieName.equals("S3")
                                ||dieName.equals("S4")||dieName.equals("S5")||dieName.equals("B2")){
                            disconnectNum+=(3-a.length()/5);//tile with 4 gates
                        }
                        else if ((dieName.equals("A2")||dieName.equals("A3"))){//A2 A3
                            if (dieLoc.equals("A0")){                           //at the left top corner
                                if (dieOri=='0'||dieOri=='1'||dieOri=='6'||dieOri=='7'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if (dieLoc.equals("G0")){//at the left bottom corner
                                if (dieOri=='0'||dieOri=='3'||dieOri=='5'||dieOri=='7'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if (dieLoc.equals("A6")){//at the right top corner
                                if (dieOri=='1'||dieOri=='2'||dieOri=='4'||dieOri=='7'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if (dieLoc.equals("G6")){//at the right bottom corner
                                if (dieOri=='2'||dieOri=='3'||dieOri=='4'||dieOri=='5'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if (dieY=='A'&&dieX!='0'&&dieX!='6'){//at the top edge
                                if (dieOri=='1'||dieOri=='7'){
                                    disconnectNum+=(3-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(2-a.length()/5);
                                }
                            }
                            else if (dieY=='G'&&dieX!='0'&&dieX!='6'){//at the bottom edge
                                if (dieOri=='3'||dieOri=='5'){
                                    disconnectNum+=(3-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(2-a.length()/5);
                                }
                            }
                            else if (dieX=='0'&&dieY!='A'&&dieY!='G'){//at the left edge
                                if (dieOri=='0'||dieOri=='6'){
                                    disconnectNum+=(3-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(2-a.length()/5);
                                }
                            }
                            else if (dieX=='6'&&dieY!='A'&&dieY!='G'){//at the right edge
                                if (dieOri=='2'||dieOri=='4'){
                                    disconnectNum+=(3-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(2-a.length()/5);
                                }
                            }
                        }
                        else if ((dieName.equals("A1")||dieName.equals("A4")||dieName.equals("B0"))&&(dieX=='0'||dieX=='6')){
                                if (dieOri=='0'||dieOri=='2'||dieOri=='4'||dieOri=='6'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                        else if ((dieName.equals("A1")||dieName.equals("A4")||dieName.equals("B0"))&&(dieY=='A'||dieY=='G')){
                            if (dieOri=='0'||dieOri=='2'||dieOri=='4'||dieOri=='6'){
                                disconnectNum+=(1-a.length()/5);
                            }
                            else {
                                disconnectNum+=(2-a.length()/5);
                            }
                        }
                            else if ((dieName.equals("A0")||dieName.equals("A5"))){//A0 A5
                                if (dieLoc.equals("A0")){                           //at the left top corner
                                    if (dieOri=='2'||dieOri=='5'){
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                    else if (dieOri=='0'||dieOri=='7'){
                                        disconnectNum+=0;
                                    }
                                    else {
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                }
                            else if (dieLoc.equals("G0")){//at the left bottom corner
                                if (dieOri=='1'||dieOri=='4'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else if (dieOri=='3'||dieOri=='6'){
                                    disconnectNum+=0;
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if (dieLoc.equals("A6")){//at the right top corner
                                    if (dieOri=='3'||dieOri=='6'){
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                    else if (dieOri=='1'||dieOri=='4'){
                                        disconnectNum+=0;
                                    }
                                    else {
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                }
                                else if (dieLoc.equals("G6")){//at the right bottom corner
                                    if (dieOri=='0'||dieOri=='7'){
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                    else if (dieOri=='2'||dieOri=='5'){
                                        disconnectNum+=0;
                                    }
                                    else {
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                }
                                else if (dieY=='A'&&dieX!='0'&&dieX!='6'){//at the top edge
                                    if (dieOri=='2'||dieOri=='3'||dieOri=='5'||dieOri=='6'){
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                    else {
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                }
                                else if (dieY=='G'&&dieX!='0'&&dieX!='6'){//at the bottom edge
                                    if (dieOri=='2'||dieOri=='3'||dieOri=='5'||dieOri=='6'){
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                    else {
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                }
                                else if (dieX=='0'&&dieY!='A'&&dieY!='G'){//at the left edge
                                    if (dieOri=='0'||dieOri=='3'||dieOri=='6'||dieOri=='7'){
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                    else {
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                }
                                else if (dieX=='6'&&dieY!='A'&&dieY!='G'){//at the right edge
                                    if (dieOri=='0'||dieOri=='3'||dieOri=='6'||dieOri=='7'){
                                        disconnectNum+=(2-a.length()/5);
                                    }
                                    else {
                                        disconnectNum+=(1-a.length()/5);
                                    }
                                }
                            }
                            else if ((dieName.equals("B1"))&&(dieX=='0')){
                                if (dieOri=='0'||dieOri=='1'||dieOri=='5'||dieOri=='6'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                            else if ((dieName.equals("B1"))&&(dieX=='6')){
                                if (dieOri=='2'||dieOri=='3'||dieOri=='4'||dieOri=='7'){
                                    disconnectNum+=(2-a.length()/5);
                                }
                                else {
                                    disconnectNum+=(1-a.length()/5);
                                }
                            }
                        else if ((dieName.equals("B1"))&&(dieY=='A')){
                            if (dieOri=='1'||dieOri=='2'||dieOri=='6'||dieOri=='7'){
                                disconnectNum+=(2-a.length()/5);
                            }
                            else {
                                disconnectNum+=(1-a.length()/5);
                            }
                        }
                        else if ((dieName.equals("B1"))&&(dieY=='G')){
                            if (dieOri=='0'||dieOri=='3'||dieOri=='4'||dieOri=='5'){
                                disconnectNum+=(2-a.length()/5);
                            }
                            else {
                                disconnectNum+=(1-a.length()/5);
                            }
                        }
        }

    }
        return disconnectNum;
    }
    /**every placement should add only one tile, so duplication placement should not permitted**/
    public static String clearDuplications(String boardString){
        StringBuilder input=new StringBuilder(boardString);

        for (int i=0;i<input.length();i+=5){
            String tile=input.substring(i+2,i+4);//check according to the location
            for (int j=i+5;j<input.length();j+=5){
                String nextTile=input.substring(j+2,j+4);
                if (tile.equals(nextTile)){
                    input.delete(j,j+5);
                    j-=5;
                }
            }
        }
        return new String(input);
    }

    /**
     * Given the current state of a game board, output an integer representing the sum of all the following factors
     * that contribute to the player's final score.
     * <p>
     * * Number of exits mapped
     * * Number of centre tiles used
     * * Number of dead ends in the network
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for score *not* considering longest rail/highway
     */
    public static int getBasicScore(String boardString) {
        StringBuilder tiles = new StringBuilder(boardString);//make several string Builder to easily add and delete
        StringBuilder routes = new StringBuilder();
        StringBuilder crossTile = new StringBuilder();
        StringBuilder special =new StringBuilder();

        int one = 0;//store the fist exist(beginning)
        int existNum = 1;//start from a exist and count the exist number
        int existScore=0;
        int centerNum=0;//the number of the tiles which in the center of the board
        int count=0;
        boolean haveExist = true;//control the for loop
        do {
            for (int m = 0; m < tiles.length(); m += 5) {//start from the exist tiles, next time, find another exist
                String exit = tiles.substring(m, m + 5);
                if (Tile.ifExist(exit)) {
                    one = m;
                    tiles.append(special);
                    existNum=1;
                    break;
                }
                haveExist = false;
            }
            if (tiles.length()==0){//empty stringboard cannot get the mark
                break;
            }
            String first = tiles.substring(one, one + 5);//the first tile
            routes.append(first);//store the route that has been considerated
            String firstName = first.substring(0, 2);
            if ((!firstName.equals("A0")) && (!firstName.equals("A1")) && (!firstName.equals("A4")) && (!firstName.equals("A5"))
                    && (!firstName.equals("B0")) && (!firstName.equals("B1"))&& (!firstName.equals("B2"))) {
                crossTile.append(first);//store it if the tiles have another out way
            }
            tiles.delete(one, one + 5);//avoid searching it again
            while (tiles.length()>0) {
                String curTileInfo = findNext(first, new String(tiles));//find the neighbour from the current string
                String curTile = curTileInfo.substring(0, 5);
                String tileName = curTile.substring(0, 2);
                if (tileName.equals("B2")){//B2 is a spcial tile which need to be treated alone
                    special.append(curTile);
                    routes.append(curTile);
                    if (Tile.ifExist(curTile)){
                        existNum++;//**********************************************if it is a exist, add it
                    }
                    int targetTileLoc = getTileIndex(curTile, new String(tiles));//store the number and delete it
                    tiles.delete(targetTileLoc, targetTileLoc + 5);
                    curTile=findB2Next(first,curTile,new String(tiles));//2B is complicated one, it need the another tile to check its rail way and high way
                   if (curTile.length()>5||curTile.equals("No su")){
                       while (crossTile.length()>0){
                           int i=crossTile.length();
                               curTile = findNext(crossTile.substring(i - 5, i),new String(tiles)).substring(0,5);
                               if (!curTile.equals("No su")){//find one neighbour, go on
                                   break;
                               }
                               else {
                                   crossTile.delete(i-5,i);//if it cannot find the connnection, it will be useless
                               }
                       }
                   }
                }
                if (curTileInfo.length()<10){//"No such a tile!"is more than 10 words
                    routes.append(curTile);//it is a ordinate string(5-strings)
                }
                else {//***************************************not a B2 tile
                    boolean test=true;
                    if (special.length()>0){//begin searching the cross tiles, start searching the B2 tiles first to check if they are connected
                        for (int q=0;q<special.length();q+=5){
                            String specailTile=special.substring(q,q+5);
                            if (areConnectedNeighbours(first,specailTile)){//connected and go on
                                routes.append(specailTile);
                                curTile=findB2Next(first,specailTile,new String(tiles));//find the next and go on
                                test=false;
                                count++;//should not search the B2 tile over and over the time
                                break;
                            }
                            if (count>3){
                                test=true;
                            }
                        }
                    }
                    while (crossTile.length()>0&&test){//restart the route by find the latest cross tile
                        int i=crossTile.length();
                        crossTile.delete(i - 5, i);
                        if (crossTile.length()>0){
                            i-=5;
                            curTile = findNext(crossTile.substring(i - 5, i),new String(tiles)).substring(0,5);//find a new tile thought the cross tile
                            if (!curTile.equals("No su")){
                                break;//successufully find
                            }
                            else {
                                for (int g=0;g<special.length();g+=5){
                                    curTile = findB2Next(crossTile.substring(i - 5, i),special.substring(g,g+5),new String(tiles)).substring(0,5);
                                    if (curTile.length()==5){//else, try to find the next tile by the help of B2
                                        break;
                                    }
                                }
                            }
                            if (Tile.ifExist(curTile)){//check if it is the exist
                                existNum++;
                                routes.append(curTile);
                            }
                        }
                    }
                    if (crossTile.length()==0){//when there was no cross tile, end the searching
                        break;
                    }
                }
                if ((!tileName.equals("A0")) && (!tileName.equals("A1")) && (!tileName.equals("A4")) && (!tileName.equals("A5"))
                        && (!tileName.equals("B0")) && (!tileName.equals("B1"))&& (!tileName.equals("B2")) &&(!tileName.equals("No"))|| Tile.ifExist(curTile)) {
                    if (!Tile.ifExist(curTile)) {//if they are cross tile
                        crossTile.append(curTile);
                        first = curTile;
                        int targetTileLoc = getTileIndex(curTile, new String(tiles));
                        tiles.delete(targetTileLoc, targetTileLoc + 5);
                    } else {//if it is an exist
                            existNum++;
                            if ((!tileName.equals("A0")) && (!tileName.equals("A1")) && (!tileName.equals("A4")) && (!tileName.equals("A5"))
                                    && (!tileName.equals("B0")) && (!tileName.equals("B1"))&& (!tileName.equals("B2"))){
                                crossTile.append(curTile);
                            }
                            int existLoc = getTileIndex(curTile, new String(tiles));
                            tiles.delete(existLoc, existLoc + 5);
                            if (tiles.length()==0){
                                break;
                            }
                            first = crossTile.substring(crossTile.length() - 5, crossTile.length());
                    }
                }
                else {//ordinary searching
                    first = curTile;
                    if (curTile.substring(0,2).equals("B2")){
                        special.append(curTile);
                    }
                    if (curTile.length()<=5&&!curTile.equals("No su")){
                        int targetTileLoc = getTileIndex(curTile, new String(tiles));
                        tiles.delete(targetTileLoc, targetTileLoc + 5);
                        crossTile.append(curTile);
                    }

                }
            }
            existScore+=getRouteScore(existNum);
        } while (haveExist);
        for (int i=0;i<boardString.length();i+=5){
            String die=boardString.substring(i,i+5);
                    String curTileLoc=die.substring(2,4);
            if ((curTileLoc.equals("C2"))||(curTileLoc.equals("C3"))||(curTileLoc.equals("C4"))
                    ||(curTileLoc.equals("D2"))||(curTileLoc.equals("D3"))||(curTileLoc.equals("D4"))
                    ||(curTileLoc.equals("E2"))||(curTileLoc.equals("E3"))||(curTileLoc.equals("E4"))){
                centerNum++;//get centre tile's mark
            }
        }
        // FIXME Task 8: compute the basic score
        return existScore+centerNum-getDisconnectedScore(boardString);
    }
    /**Special tiles should only appear at most three times**/
    public static int getStilesNumber(String boardString){
        int count=0;
        for (int i=0;i<boardString.length();i+=5){
            if (boardString.charAt(i)=='S'){
                count++;
            }
        }
        return count;
    }


    /**
     * Given a valid boardString and a dice roll for the round,
     * return a String representing an ordered sequence of valid piece placements for the round.
     *
     * @param boardString a board string representing the current state of the game as at the start of the round
     * @param diceRoll    a String representing a dice roll for the round
     * @return a String representing an ordered sequence of valid piece placements for the current round
     * @see RailroadInk#generateDiceRoll()
     */
    public static String generateMove(String boardString, String diceRoll) {
        StringBuilder boardtiles=new StringBuilder(boardString);//a stringbuilder to update the latest boardstring
        StringBuilder diceContainer=new StringBuilder();//store the move
        boolean lalala=true;//a switch to control the for loop
        if (boardString.length()!=0){//otherwise, it will start from a exist
            for (int i=0;i<boardString.length();i+=5){//start from each tile include the tile's information
                String tile=boardString.substring(i,i+5);

                String validPlace=clearInvalidLoc(clearNeighborLoc(tile,boardString));//find the tile's neighbor index
                StringBuilder randomString=new StringBuilder();//to make a 5-strings tile to check if it connects with other
                for (int j=0;j<validPlace.length();j+=2){
                    for (int k=0;k<diceRoll.length()&&lalala;k+=2){
                        StringBuilder diceRollBuilder=new StringBuilder(diceRoll);
                        randomString.delete(0,5);//refresh
                        randomString.append(diceRoll.substring(k,k+2));
                        randomString.append(validPlace.substring(j,j+2));
                        randomString.append(0);//create a 5-string tile represent of the tile's placement and orientation
                        for (int t=0;t<8;t++){
                            randomString.delete(4,5);//refresh the orientation of the tile
                            randomString.append(t);
                            String random=new String(randomString);
                            if (areConnectedNeighbours(tile,random)){//if the random tile connects with the tile on the current board
                                diceContainer.append(random);//collect it

                                boardtiles.append(random);//at it into the current placement
                                diceRollBuilder.delete(k,k+2);//delete it because we don't use it anymore
                                boardString=new String(boardtiles);//update the boardString and diceRoll
                                diceRoll=new String(diceRollBuilder);
                                lalala=false;//start from tile's other side
                                k=0;//start at the random dice's beginning
                                break;

                            }
                        }

                    }
                    if (diceRoll.length()!=0){
                        for (int k=diceRoll.length();k>0&&lalala;k-=2){
                            StringBuilder diceRollBuilder=new StringBuilder(diceRoll);
                            randomString.delete(0,5);
                            randomString.append(diceRoll.substring(k-2,k));
                            randomString.append(validPlace.substring(j,j+2));
                            randomString.append(0);//create a 5-string tile represent of the tile's placement and orientation
                            for (int t=0;t<8;t++){
                                randomString.delete(4,5);//refresh the orientation of the tile
                                randomString.append(t);
                                String random=new String(randomString);
                                if (areConnectedNeighbours(tile,random)){//if the random tile connects with the tile on the current board
                                    diceContainer.append(random);//collect it

                                    boardtiles.append(random);//at it into the current placement
                                    diceRollBuilder.delete(k,k+2);//delete it because we don't use it anymore
                                    boardString=new String(boardtiles);//update the boardString and diceRoll
                                    diceRoll=new String(diceRollBuilder);
                                    lalala=false;//start from tile's other side
                                    k=0;//start at the random dice's beginning
                                    break;

                                }
                            }

                        }
                    }
                }
            }
        }
        // FIXME Task 10: generate a valid move
        //return new String(boardtiles);
        return new String(diceContainer);
    }
    public static String clearNeighborLoc(String tile,String boardString){

        String tileLoc=tile.substring(2,4);
        String neighborIndex=getNeighborIndex(tileLoc);//return a String like: "B2C3D2C1"-NESW
        StringBuilder editedNeighborIndex=new StringBuilder(neighborIndex);

        String neighbor=getNeighborSimple(tile,boardString);//return a String like "A0B10A3B32" which represent the tile's neighbors
        if (neighbor.length()!=0){
            for (int j=0;j<neighbor.length();j+=5){
                String neighborLoc=neighbor.substring(j+2,j+4);
                for (int i=0;i<editedNeighborIndex.length();i+=2){
                    if (editedNeighborIndex.substring(i,i+2).equals(neighborLoc)){
                        editedNeighborIndex.delete(i,i+2);
                        break;
                    }
                }
            }
        }
        return new String(editedNeighborIndex);
    }
    /**getNeighborSimple methods didn't consider the edge consideration so it may have some strange char
     * this method is to delete the invalid locaion**/
    private static String clearInvalidLoc(String locString) {
        StringBuilder loc = new StringBuilder(locString);
        for (int j = 0; j < locString.length() / 2; j++) {
            for (int i = 0; i < loc.length(); i += 2) {
                char first = loc.charAt(i);
                char second = loc.charAt(i + 1);
                if ((first < 'A' || first > 'G') || (second < '0' || second > '6')) {
                    loc.delete(i, i + 1 + 1);
                    break;
                }
            }
        }
        return new String(loc);
    }
    /**return 4 index around the tile**/
    private static String getNeighborSimple(String tile, String boardString) {
        String tileLoc = tile.substring(2, 4);//get the location
        StringBuilder r = new StringBuilder();

        char first = tileLoc.charAt(0);//test will each x and y coordinate
        char second = tileLoc.charAt(1);
        if (first == 'A' && second == '0') {//at left top
            String[] c = new String[2];
            c[0] = "A1";
            c[1] = "B0";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'A' && second == '6') {//at right top
            String[] c = new String[2];
            c[0] = "A5";
            c[1] = "B6";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'G' && second == '0') {//at left bottom
            String[] c = new String[2];
            c[0] = "G1";
            c[1] = "F0";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'G' && second == '6') {//at right bottom
            String[] c = new String[2];
            c[0] = "G5";
            c[1] = "F6";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'A' || first == 'G') {//on top and bottom side
            String[] c = new String[3];
            StringBuilder a = new StringBuilder();
            a.append(first);
            second -= 1;
            a.append(second);
            String t = new String(a);
            c[0] = t;
            a.deleteCharAt(1);
            second += 2;
            a.append(second);
            String g = new String(a);
            c[1] = g;
            StringBuilder q = new StringBuilder();
            second -= 1;
            if (first == 'A') {
                first += 1;
            } else {
                first -= 1;
            }
            q.append(first);
            q.append(second);
            String w = new String(q);
            c[2] = w;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) ) {
                        r.append(die);
                    }
                }
            }
        } else if (second == '0' || second == '6') {//on the left and right side
            String[] c = new String[3];
            StringBuilder a = new StringBuilder();
            first -= 1;
            a.append(first);
            a.append(second);
            String t = new String(a);
            c[0] = t;
            StringBuilder b = new StringBuilder();
            first += 2;
            b.append(first);
            b.append(second);
            String g = new String(b);
            c[1] = g;
            StringBuilder y = new StringBuilder();
            if (second == '0') {
                second += 1;
            } else {
                second -= 1;
            }
            first -= 1;
            y.append(first);
            y.append(second);
            String x = new String(y);
            c[2] = x;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc)) {
                        r.append(die);
                    }
                }
            }
        } else {//center of the board
            String[] c = new String[4];
            StringBuilder a = new StringBuilder();
            a.append(first);
            second += 1;
            a.append(second);
            String t = new String(a);
            c[0] = t;
            a.deleteCharAt(1);
            second -= 2;
            a.append(second);
            String g = new String(a);
            c[1] = g;
            StringBuilder b = new StringBuilder();
            first -= 1;
            second += 1;
            b.append(first);
            b.append(second);
            String m = new String(b);
            c[2] = m;
            StringBuilder x = new StringBuilder();
            first += 2;
            x.append(first);
            x.append(second);
            String n = new String(x);
            c[3] = n;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) ) {
                        r.append(die);
                    }
                }
            }
        }
        return new String(r);
    }
/**get index for north, east, south and west index**/
    private static String getNeighborIndex(String tileLoc){
        StringBuilder result=new StringBuilder();
        StringBuilder north=new StringBuilder();
        StringBuilder east=new StringBuilder();
        StringBuilder south=new StringBuilder();
        StringBuilder west=new StringBuilder();

        char first=tileLoc.charAt(0);
        char second=tileLoc.charAt(1);

        east.append(first);
        west.append(first);
        first-=1;
        north.append(first);
        first+=2;
        south.append(first);
        north.append(second);
        south.append(second);
        second-=1;
        west.append(second);
        second+=2;
        east.append(second);

        result.append(new String(north));
        result.append(new String(east));
        result.append(new String(south));
        result.append(new String(west));

        return new String(result);
    }
    /**
     * Given the current state of a game board, output an integer representing the sum of all the factors contributing
     * to `getBasicScore`, as well as those attributed to:
     * <p>
     * * Longest railroad
     * * Longest highway
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for final score (not counting expansion packs)
     */
    public static int getAdvancedScore(String boardString) {
        // FIXME Task 12: compute the total score including bonus points
        return -1;
    }
/**find any connected neighbor by using this method**/
    private static String getNeighbor(String tile, String boardString) {

        String tileLoc = tile.substring(2, 4);
        StringBuilder r = new StringBuilder();

        char first = tileLoc.charAt(0);
        char second = tileLoc.charAt(1);
        if (first == 'A' && second == '0') {//left top
            String[] c = new String[2];
            c[0] = "A1";
            c[1] = "B0";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'A' && second == '6') {//right top
            String[] c = new String[2];
            c[0] = "A5";
            c[1] = "B6";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'G' && second == '0') {//left bottom
            String[] c = new String[2];
            c[0] = "G1";
            c[1] = "F0";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'G' && second == '6') {//right bottom
            String[] c = new String[2];
            c[0] = "G5";
            c[1] = "F6";
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else if (first == 'A' || first == 'G') {//top and bottom side
            String[] c = new String[3];
            StringBuilder a = new StringBuilder();
            a.append(first);
            second -= 1;
            a.append(second);
            String t = new String(a);
            c[0] = t;
            a.deleteCharAt(1);
            second += 2;
            a.append(second);
            String g = new String(a);
            c[1] = g;
            StringBuilder q = new StringBuilder();
            second -= 1;
            if (first == 'A') {
                first += 1;
            } else {
                first -= 1;
            }
            q.append(first);
            q.append(second);
            String w = new String(q);
            c[2] = w;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else if (second == '0' || second == '6') {//left and right side
            String[] c = new String[3];
            StringBuilder a = new StringBuilder();
            first -= 1;
            a.append(first);
            a.append(second);
            String t = new String(a);
            c[0] = t;
            StringBuilder b = new StringBuilder();
            first += 2;
            b.append(first);
            b.append(second);
            String g = new String(b);
            c[1] = g;
            StringBuilder y = new StringBuilder();
            if (second == '0') {
                second += 1;
            } else {
                second -= 1;
            }
            first -= 1;
            y.append(first);
            y.append(second);
            String x = new String(y);
            c[2] = x;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        } else {//center
            String[] c = new String[4];
            StringBuilder a = new StringBuilder();
            a.append(first);
            second += 1;
            a.append(second);
            String t = new String(a);
            c[0] = t;
            a.deleteCharAt(1);
            second -= 2;
            a.append(second);
            String g = new String(a);
            c[1] = g;
            StringBuilder b = new StringBuilder();
            first -= 1;
            second += 1;
            b.append(first);
            b.append(second);
            String m = new String(b);
            c[2] = m;
            StringBuilder x = new StringBuilder();
            first += 2;
            x.append(first);
            x.append(second);
            String n = new String(x);
            c[3] = n;
            for (String z : c) {
                for (int j = 0; j < boardString.length(); j += 5) {
                    String die = boardString.substring(j, j + 5);
                    String targetLoc = die.substring(2, 4);
                    if (z.equals(targetLoc) && areConnectedNeighbours(tile, die)) {
                        r.append(die);
                    }
                }
            }
        }
        return new String(r);
    }
//find a connected neighbour according to the current boardString
    private static String findNext(String tile, String boardString) {
        String result = "No such a tile to connect";
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < boardString.length(); i += 5) {
            String die = boardString.substring(i, i + 5);
            if ((!die.equals(tile)) && areConnectedNeighbours(tile, die)) {
                result="";
                temp.append(die);
                temp.append(i);
                break;
            }
        }
        return result+ (new String(temp));
    }

    /***becasue B2 have both highway and railway,
     * but they are not conected, so B2's neighbor should depending the tiles that before it**/
    public static String findB2Next(String tile,String tileB2 ,String boardString){
                 /***get first tile information***/
        char tileYA = tile.substring(2, 3).charAt(0);
        char tileXA = tile.substring(3, 4).charAt(0);

                 /***get second tile information***/
        char tileYB = tileB2.substring(2, 3).charAt(0);
        char tileXB = tileB2.substring(3, 4).charAt(0);

            if (abs(tileYA - tileYB) == 1) {  //tile        //tileB2
                                                //tileB2        //tile
                if (tileXA == tileXB) {
                    StringBuilder one = new StringBuilder();
                    StringBuilder two = new StringBuilder();
                    tileYB += 1;
                    one.append(tileYB);
                    one.append(tileXB);
                    String first = new String(one);
                    tileYB -= 2;
                    two.append(tileYB);
                    two.append(tileXB);
                    String second = new String(two);
                    for (int i = 0; i < boardString.length(); i += 5) {
                        String die = boardString.substring(i, i + 5);
                        if (!die.equals(tile)){
                            String tileLoc = die.substring(2, 4);
                            if (tileLoc.equals(first) || tileLoc.equals(second)) {
                                if (areConnectedNeighbours(die, tileB2)) {
                                        return die;
                                }
                            }
                        }
                    }
                }
            }
        else if (abs(tileXA - tileXB) == 1) {  //tile|tileB2       //tileB2|tile
            if (tileYA == tileYB) {
                StringBuilder one = new StringBuilder();
                StringBuilder two = new StringBuilder();
                tileXB += 1;
                one.append(tileYB);
                one.append(tileXB);
                String first = new String(one);
                tileXB -= 2;
                two.append(tileYB);
                two.append(tileXB);
                String second = new String(two);
                for (int i = 0; i < boardString.length(); i += 5) {
                    String die = boardString.substring(i, i + 5);
                    if (!die.equals(tile)){
                        String tileLoc = die.substring(2, 4);
                        if (tileLoc.equals(first) || tileLoc.equals(second)) {
                            if (areConnectedNeighbours(die, tileB2)) {
                                return die;
                            }
                        }
                    }
                }
            }
        }
            return "No such a tile to connect";
    }

    public static int getTileIndex(String tile, String boardString) {
        int num = 0;
        for (int i = 0; i < boardString.length(); i += 5) {
            String curTile = boardString.substring(i, i + 5);
            if (tile.equals(curTile)) {
                num = i;
                break;
            }
        }
        return num;
    }

    public static void main(String[] args) {
    }
}

