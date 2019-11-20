package comp1110.ass2.gui;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import comp1110.ass2.RailroadInk;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;

/**
 * A very simple viewer for tile placements in the Railroad Ink game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various tile placements.
 */
public class Viewer extends Application {
    /* board layout */
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private Group tileGroup=new Group();

    private Group gridding=new Group();//a group which stores the line and exist images
    private Group randomtiles=new Group();//a group which stores each four tiles which create randomly
    private Group randomtilesBg=new Group();//a group which create at first and have four yellow rectangle as background of each tile
    private Group tilesS=new Group();//a group which have S tiles
    private Group curString=new Group();//a group stores current tiles on the board

    private int height=VIEWER_HEIGHT-96;//grid's height
    private int wide=VIEWER_HEIGHT-96;//grid's width
    private int length=height/8;//each small grid's size

    private StringBuilder boardString=new StringBuilder();//easy for me to delete and append
    private int count;//count tile rotate times
    private int round=1;//count the round

    private Label label2=new Label();

    private Pane pane=new Pane();

    private TextField textField;

    private Point2D dragDistance=null;

    private int rotateCount=0;

    private double x=0;//store tile current coordinate
    private double y=0;

    private void drawGriding(){
       for (int i=0;i<height;i+=length ){//draw the vertical line
           Line vLine=new Line(length+i,length,length+i,height);
           gridding.getChildren().add(vLine);
       }
        for (int i=0;i<wide;i+=length ){//draw the horizontal line
            Line hLine=new Line(length,length+i,wide,length+i);
            gridding.getChildren().add(hLine);
        }

        for (int i=0;i<12;i+=2){//add exist images in top and bottom
            ImageView oneHigh = new ImageView();
            if (i==2||i==8){
                oneHigh.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "RailExit.png").toString()));
            }
            else {
                oneHigh.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "HighExit.png").toString()));
            }
            if (i<6){//top
                oneHigh.setX((double)length*(2+i));
                oneHigh.setY((double)length/2);
            }
            else {//bottom
                oneHigh.setX((double)length*(2+i-6));
                oneHigh.setY(height-(double)length/2);
                oneHigh.setScaleY(-1);
            }
            oneHigh.setFitHeight(length);
            oneHigh.setFitWidth(length);
            gridding.getChildren().add(oneHigh);
        }
        for (int i=0;i<12;i+=2){//add exist images in left and right
            ImageView oneHigh = new ImageView();
            if (i==2||i==8){
                oneHigh.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "HighExit.png").toString()));
            }
            else {
                oneHigh.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + "RailExit.png").toString()));
            }
            if (i<6){//left
                oneHigh.setX((double)length/2);
                oneHigh.setY((double)length*(2+i));
                oneHigh.setRotate(-90.0);
            }
            else {//right
                oneHigh.setX(height-(double)length/2);
                oneHigh.setY((double)length*(2+i-6));
                oneHigh.setRotate(90.0);
            }

            oneHigh.setFitHeight(length);
            oneHigh.setFitWidth(length);
            gridding.getChildren().add(oneHigh);
        }

        controls.getChildren().add(gridding);
        gridding.toBack();

    }

private void drawRandomDice() {//draw the random tile place at first
    int length = (VIEWER_HEIGHT - 96) / 8;//random tiles' size
    int borderwidth = 3;//background size
    String tile = RailroadInk.generateDiceRoll();//generate a 8-String string represent 4 random tiles' name
    for (int i = 0; i < tile.length(); i += 2) {
        Rectangle border = new Rectangle(VIEWER_HEIGHT - 20, (length - 20) * (i) + length, length + borderwidth, length + borderwidth);//draw a square as a background
        border.setFill(Color.LIGHTYELLOW);//make a background to make each tile clear on the white board
        border.setEffect(new DropShadow(20, Color.BLACK));//make a shadow to make the tile look good
        randomtilesBg.getChildren().add(border);//add it before the images
        String tileName = tile.substring(i, i + 2);//start to show random tile
        ImageView tileImage = new ImageView();
        tileImage.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + tileName + ".png").toString()));
        tileImage.setFitHeight(length);
        tileImage.setFitWidth(length);
        tileImage.setX(VIEWER_HEIGHT - 20);
        tileImage.setY((length - 20) * (i) + length);//finish

        //add event listener to each tile so that player can drag them
        tileImage.addEventFilter(MouseDragEvent.MOUSE_PRESSED, event -> {
            x=tileImage.getX();//get image coordinate so that if the tile's placement is invalid, it can turn back
            y=tileImage.getY();
            dragDistance = new Point2D(event.getSceneX(), event.getSceneY());//create a (x,y) point that store mouse location
            dragDistance = dragDistance.subtract(pane.localToScene(new Point2D(tileImage.getX(), tileImage.getY()))); //subtract them so the tile can follow the mouse
        });

        tileImage.addEventFilter(MouseDragEvent.MOUSE_DRAGGED,mouseEvent -> {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        Point2D px = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        px = pane.sceneToLocal(px.subtract(dragDistance));//image follow the mouse, mouse and tile have a fixed relative distance
                        tileImage.setX(px.getX());
                        tileImage.setY(px.getY());
                    }
        });
        tileImage.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
            rotate(tileImage);//when scrolling, rotating the image

        });

        tileImage.addEventFilter(MouseDragEvent.MOUSE_RELEASED,mouseDragEvent -> {
            double curX=mouseDragEvent.getX();
            double curY=mouseDragEvent.getY();
            String curIndex=getIndex(curX,curY);//know where the mouse is and return a "A0"-like index so we can but it into the board

            if (!curIndex.equals("!")){//if is not outside the board, start to have a deep check
                boardString.append(tileName);//make a 5-string String
                boardString.append(curIndex);
                updateC();
                if (RailroadInk.isValidPlacementSequence(new String(boardString))){//if this 5-string string have a valid placement according to the current boardstring, go on
                    double[] curCoordinate=getCoordinate(curIndex);
                    tileImage.setX(curCoordinate[0]);
                    tileImage.setY(curCoordinate[1]);
                    curString.getChildren().add(tileImage);//put it in to the current tiles String
            }
                else {
                    boardString.delete(boardString.length()-5,boardString.length());
                    tileImage.setX(x);//go back to its original place
                    tileImage.setY(y);
                }
            }
            else {
                tileImage.setX(x);//go back to its original place
                tileImage.setY(y);
            }
            clearCount();
        });
        randomtiles.getChildren().add(tileImage);//collect them
        tileImage.toFront();
    }
    root.getChildren().addAll(randomtilesBg,randomtiles,curString);//show them on the stage(should add once)
}
/**make sure that the random dice in each round can be update and dragged sucessfully**/
private void updateRandom() {//if you put the roll button, reshow the random tiles

        randomtiles.getChildren().clear();//clear the old random tiles

        if (round<7) {//if it isn't then end of the game, go on
            round++;//you should only roll once in one round
            showRound();//refresh the round number
            String tile = RailroadInk.generateDiceRoll();//generate 4 dices
            for (int i = 0; i < tile.length(); i += 2) {
                String tileName = tile.substring(i, i + 2);//start to show random tile
                ImageView tileImage = new ImageView();
                tileImage.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + tileName + ".png").toString()));
                tileImage.setFitHeight(length);
                tileImage.setFitWidth(length);
                tileImage.setX(VIEWER_HEIGHT - 20);
                tileImage.setY((length - 20) * (i) + length);//finish
                randomtiles.getChildren().add(tileImage);
                tileImage.toFront();

                tileImage.addEventFilter(MouseDragEvent.MOUSE_PRESSED, event -> {//same method with the drawRandom method
                    x = tileImage.getX();
                    y = tileImage.getY();
                    dragDistance = new Point2D(event.getSceneX(), event.getSceneY());
                    dragDistance = dragDistance.subtract(pane.localToScene(new Point2D(tileImage.getX(), tileImage.getY())));
                });

                tileImage.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, mouseEvent -> {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        Point2D px = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        px = pane.sceneToLocal(px.subtract(dragDistance));
                        tileImage.setX(px.getX());
                        tileImage.setY(px.getY());
                    }

                });
                tileImage.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
                    rotate(tileImage);

                });

                tileImage.addEventFilter(MouseDragEvent.MOUSE_RELEASED, mouseDragEvent -> {
                    double curX = mouseDragEvent.getX();
                    double curY = mouseDragEvent.getY();
                    String curIndex = getIndex(curX, curY);

                    if (!curIndex.equals("!")) {
                        boardString.append(tileName);
                        boardString.append(curIndex);
                        updateC();
                        if (RailroadInk.isValidPlacementSequence(new String(boardString))) {
                            double[] curCoordinate = getCoordinate(curIndex);
                            tileImage.setX(curCoordinate[0]);
                            tileImage.setY(curCoordinate[1]);
                            curString.getChildren().add(tileImage);
                        } else {
                            boardString.delete(boardString.length() - 5, boardString.length());
                            tileImage.setX(x);
                            tileImage.setY(y);
                        }
                        System.out.println(boardString);
                    } else {
                        tileImage.setX(x);
                        tileImage.setY(y);
                    }
                    clearCount();
                });
            }
        }
       if (round ==7){
            Button button = new Button("Get Score");//at final round, allow player to get score
            button.setLayoutX(760);
            button.setLayoutY(650);
            button.setOnAction(e -> {
                showScore();
            });
            root.getChildren().add(button);
        }
    }
    /**draw fixed 6 special tiles on the board
     * and have a similar method of the drawRandom method
     * **/
    private void drawSDice(){
        int length = (VIEWER_HEIGHT - 96) / 8;
        int borderwidth = 3;
        String tile = "S0S1S2S3S4S5";//the special tiles are fixed and should be shown at first
        for (int i = 0; i < tile.length(); i += 2) {
            Rectangle border = new Rectangle(VIEWER_WIDTH - 120, (length - 30) * (i) + 50, length + borderwidth, length + borderwidth);
            border.setFill(Color.LIGHTYELLOW);//make a background to make each tile clear on the white board
            border.setEffect(new DropShadow(20, Color.BLACK));//make a shadow to make the tile look good
            tilesS.getChildren().add(border);//add it before the images
            String tileName = tile.substring(i, i + 2);//start to show random tile
            ImageView tileImage = new ImageView();
            tileImage.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + tileName + ".png").toString()));
            tileImage.setFitHeight(length);
            tileImage.setFitWidth(length);
            tileImage.setX(VIEWER_WIDTH - 120);
            tileImage.setY((length - 30) * (i) + 50);//finish

            tileImage.addEventFilter(MouseDragEvent.MOUSE_PRESSED, event -> {//similar methods above
                System.out.println("Pressed");
                x = tileImage.getX();
                y = tileImage.getY();
                dragDistance = new Point2D(event.getSceneX(), event.getSceneY());
                dragDistance = dragDistance.subtract(pane.localToScene(new Point2D(tileImage.getX(), tileImage.getY())));
            });

            tileImage.addEventFilter(MouseDragEvent.MOUSE_DRAGGED, mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    Point2D px = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    px = pane.sceneToLocal(px.subtract(dragDistance));
                    tileImage.setX(px.getX());
                    tileImage.setY(px.getY());
                }
            });
            tileImage.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
                System.out.println("scrolled");
                rotate(tileImage);

            });

            tileImage.addEventFilter(MouseDragEvent.MOUSE_RELEASED, mouseDragEvent -> {
                double curX = mouseDragEvent.getX();
                double curY = mouseDragEvent.getY();
                String curIndex = getIndex(curX, curY);

                if (!curIndex.equals("!")) {
                    boardString.append(tileName);
                    boardString.append(curIndex);
                    updateC();
                    if (RailroadInk.isValidPlacementSequence(new String(boardString))&&RailroadInk.getStilesNumber(new String(boardString))<=3) {
                        double[] curCoordinate = getCoordinate(curIndex);
                        tileImage.setX(curCoordinate[0]);
                        tileImage.setY(curCoordinate[1]);
                    } else {
                        boardString.delete(boardString.length() - 5, boardString.length());
                        tileImage.setX(x);
                        tileImage.setY(y);
                    }
                    System.out.println(boardString);
                } else {
                    tileImage.setX(x);
                    tileImage.setY(y);
                }

                clearCount();
            });
            tilesS.getChildren().add(tileImage);
        }
        controls.getChildren().add(tilesS);
    }
    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    private void makePlacement(String placement) {
        root.getChildren().remove(tileGroup);//remove the image before
        tileGroup.getChildren().clear();

        for(int i=0;i<placement.length();i+=5){
            String die=placement.substring(i,i+5);
            int tileOri=Integer.parseInt(die.substring(4));
            System.out.println(tileOri);
            char tileX=die.charAt(3);
            char tileY=die.charAt(2);

            ImageView tileImage = new ImageView();
            tileImage.setImage(new Image(Viewer.class.getResource(Viewer.URI_BASE + die.substring(0,2)+".png").toString()));
            tileImage.setFitHeight(100);
            tileImage.setFitWidth(100);
            if(tileOri>=4){//follow the game rotate rule
                    tileImage.setScaleX(-1);
                    tileImage.setRotate((tileOri-4)*90.0);
            }
            else {
                    tileImage.setRotate(tileOri*90.0);
            }
            tileImage.setX((tileX-48)*100);
            tileImage.setY((tileY-65)*100);
            tileGroup.getChildren().add(tileImage);
        }
        root.getChildren().addAll(tileGroup);
        // FIXME Task 4: implement the simple placement viewer
    }
    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            makePlacement(textField.getText());
            textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }
    /**because lambda should only use final variable so i write a specific method to update it**/
    private void showRound() {
        label2.setText(String.valueOf(round));
    }

    /**when players clicked the button, score will be shown in the middle of the interface**/
    private void showScore() {
        Label label = new Label("Your Basic Score is: ");
        Label labelscore=new Label(String.valueOf(RailroadInk.getBasicScore(new String(boardString))));
        label.setScaleX(5);//set the size
        label.setScaleY(5);
        labelscore.setScaleX(5);
        labelscore.setScaleY(5);
        label.setLayoutX(300);///set the location
        label.setLayoutY(VIEWER_HEIGHT/2.0);
        labelscore.setLayoutX(VIEWER_WIDTH/1.5);
        labelscore.setLayoutY(VIEWER_HEIGHT/2.0);
        controls.getChildren().add(label);
        controls.getChildren().add(labelscore);
    }
/***because the game need to store the orientation when using teh scroll rotating*/
    private void rotate(ImageView target){
        rotateCount=(rotateCount+1)%7;
        if(rotateCount>=4){
            target.setScaleX(-1);
            target.setRotate((rotateCount-4)*90.0);
        }
        else {
            target.setRotate(rotateCount*90.0);
        }
        count=rotateCount;
    }
    /***specail method for updating information in lambda*/
    private void updateC(){
        boardString.append(count);
    }
    /***same reason above**/
    private void clearCount(){
        count=0;
    }
    /**get index according to the coordinate**/
    private String getIndex(double x, double y){
        StringBuilder result=new StringBuilder();
        if (y>length&&y<length*2){
            result.append('A');
        }
        else if (y>length*2&&y<length*3){
            result.append('B');
        }
        else if (y>length*3&&y<length*4){
            result.append('C');
        }
        else if (y>length*4&&y<length*5){
            result.append('D');
        }
        else if (y>length*5&&y<length*6){
            result.append('E');
        }
        else if (y>length*6&&y<length*7){
            result.append('F');
        }
        else if (y>length*7&&y<height){
            result.append('G');
        }
        else {
            return "!";
        }
        if (x>length&&x<length*2){
            result.append('0');
        }
        else if(x>length*2&&x<length*3){
            result.append('1');
        }
        else if(x>length*3&&x<length*4){
            result.append('2');
        }
        else if(x>length*4&&x<length*5){
            result.append('3');
        }
        else if(x>length*5&&x<length*6){
            result.append('4');
        }
        else if(x>length*6&&x<length*7){
            result.append('5');
        }
        else if(x>length*7&&x<wide){
            result.append('6');
        }
        else {
            return "!";
        }
        return new String(result);
    }

    /**give the coordinate according to the index**/
private double[] getCoordinate(String input){
        double[] output=new double[2];
        char yChar=input.charAt(0);
    char xChar=input.charAt(1);
    if (yChar=='A'){
        output[1]=length;
    }
    else if (yChar=='B'){
        output[1]=length*2;
    }
    else if (yChar=='C'){
        output[1]=length*3;
    }
    else if (yChar=='D'){
        output[1]=length*4;
    }
    else if (yChar=='E'){
        output[1]=length*5;
    }
    else if (yChar=='F'){
        output[1]=length*6;
    }
    else if (yChar=='G'){
        output[1]=length*7;
    }
    if (xChar=='0'){
        output[0]=length;
    }
    else if (xChar=='1'){
        output[0]=length*2;
    }
    else if (xChar=='2'){
        output[0]=length*3;
    }
    else if (xChar=='3'){
        output[0]=length*4;
    }
    else if (xChar=='4'){
        output[0]=length*5;
    }
    else if (xChar=='5'){
        output[0]=length*6;
    }
    else if (xChar=='6'){
        output[0]=length*7;
    }
    return output;
}

    @Override
    public void start(Stage primaryStage) throws Exception {
    /**initial the first interface**/
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        primaryStage.setResizable(false);
    /**show the round at first and update it in a propriate frequency.*/
        Label label1 = new Label("Round:");
        label2.setText(String.valueOf(round));
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, label2);
        hb.setSpacing(10);
        hb.setLayoutX(75);
        hb.setLayoutY(20);
        hb.setScaleX(3);
        hb.setScaleY(3);
        controls.getChildren().add(hb);
        /**button allow player to create random dice**/
        Button button1 = new Button("Roll!");
        button1.setLayoutX(760);
        button1.setLayoutY(590);
        button1.setOnAction(e -> {
            updateRandom();
        });
        root.getChildren().add(button1);

        drawGriding();
        drawRandomDice();
        drawSDice();
        showRound();

        root.getChildren().add(controls);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
