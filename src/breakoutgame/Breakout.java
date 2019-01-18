/**
 *
 * @author Harry Ross (hgr8)
 */

package breakoutgame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import java.awt.*;
import java.util.*;

public class Breakout extends Application {

    public static final String TITLE = "Breakout Game (hgr8)";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.NAVY;
    public static final int BLOCKS_PER_COL = 4;
    public static final int BLOCKS_PER_ROW = 10;
    public static final int HORIZ_OFFSET = 2;
    public static final int VERT_OFFSET = 25;
    public static final int FONT_SIZE_LRG = 70;
    public static final int STARTING_LIVES = 2;

    // some things we need to remember during our game
    private Scene myScene;
    private Stage myStage;
    private Group root;
    private Paddle myPaddle;
    private ArrayList<Ball> ballList;
    private ArrayList<Block> blockList;
    private int currLevel = 2;
    private static int livesCount;
    private static int activeBalls;
    private boolean levelComplete;
    private Point currLocation;

    /**
     *
     * @param stage
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * Create the game's "scene": what shapes will be in the game and their starting properties
     * @param width
     * @param height
     * @param background
     * @return
     */
    private Scene setupGame (int width, int height, Paint background) {
        root = new Group();
        Scene scene = new Scene(root, width, height, background);

        ballList = new ArrayList<>(); // Create new lists to keep track of blocks and balls
        blockList = new ArrayList<>();
        livesCount = STARTING_LIVES;

        myPaddle = new Paddle(width, height);
        root.getChildren().add(myPaddle);
        myPaddle.initialize();

        drawNewBall(width, height);
        drawBlocks(currLevel);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode())); // Handle input
        return scene;
    }

    /**
     * Change properties of shapes to animate them
     * @param elapsedTime
     */
    private void step (double elapsedTime) {
        handleMouseControl();
        updateBallAttributes(elapsedTime);
        checkForCollisions();
    }

    /**
     *
     */
    private void handleMouseControl() {
        currLocation = MouseInfo.getPointerInfo().getLocation();
        myPaddle.setX(currLocation.x - myStage.getX() - myPaddle.getBoundsInParent().getWidth() / 2);
    }

    /**
     *
     * @param elapsedTime
     */
    private void updateBallAttributes(double elapsedTime) {
        for (Ball b : ballList) {
            b.setX(b.getX() + b.getXSpeed() * b.getXDirection() * elapsedTime);
            b.setY(b.getY() + b.getYSpeed() * b.getYDirection() * elapsedTime);

            if (b.getX() >= SIZE || b.getX() <= 0) {
                b.reverseX();
            }

            if (b.getY() <= 0) {
                b.reverseY();
            }

            if (b.getY() >= SIZE) {
                activeBalls--;
                resetBall(b);
            }
        }
    }

    /**
     *
     */
    private void checkForCollisions() {
        levelComplete = true;

        for (Ball b : ballList) {
            if (myPaddle.getBoundsInParent().intersects(b.getBoundsInParent())) {
                b.reverseY();
            }

            for (Block blo : blockList) {
                if (blo.getBoundsInParent().intersects(b.getBoundsInParent())) {
                    //TODO: Fix collisions
                    b.reverseY();
                    blo.updateHealth();
                }
                if (blo.getHealth() > 0 && !blo.isRemoved()) {
                    levelComplete = false;
                }
            }
            if(levelComplete) {
                nextLevel();
            }
        }
    }

    /**
     *
     * @param horiz
     * @param vert
     */
    private void drawNewBall(int horiz, int vert) {
        Ball newBall = new Ball(horiz, vert);
        newBall.initialize();
        ballList.add(newBall);
        root.getChildren().add(newBall);
        activeBalls++;
    }

    /**
     *
     * @param level
     */
    private void drawBlocks(int level) {
        //draw full grid for each block layout from text
        Scanner scan = new Scanner(this.getClass().getClassLoader().getResourceAsStream(level + ".txt"));

        for (int j = 0; j < BLOCKS_PER_COL; j++) {
            for (int k = 0; k < BLOCKS_PER_ROW; k++) {
                int next = scan.nextInt();
                if (next != 0) {
                    Block b = new Block(next, SIZE / BLOCKS_PER_ROW * j,
                            SIZE / BLOCKS_PER_ROW * k * 2, j, k);
                    blockList.add(b);
                    root.getChildren().add(b);
                }
            }
        }
    }

    /**
     *
     * @param b
     */
    private void resetBall(Ball b) {
        if(livesCount > 0 && activeBalls == 0){
            livesCount--;
            b.initialize();
            activeBalls++;
            //pause for 2 seconds //TODO pause
        }
        else if (livesCount == 0){
            gameEnd("Game Over");
        }
    }

    /**
     *
     */
    private void nextLevel() {
        if(currLevel <= 2) {
            currLevel++;

            flushObjects();

            drawNewBall(SIZE, SIZE);
            drawBlocks(currLevel);
        }

        else {
            gameEnd("You Win!");
        }
    }

    /**
     *
     */
    private void flushObjects() {
        for (Ball b : ballList) {
            b.flushBall(root);
        }

        for (Block b : blockList) {
            b.flushBlock(root);
        }

        blockList = new ArrayList<>();
        ballList = new ArrayList<>();
    }

    /**
     *
     * @param message
     */
    private void gameEnd(String message) {
        Text gameOver = new Text();
        gameOver.setText(message);
        gameOver.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, FONT_SIZE_LRG));
        gameOver.setFill(Color.WHITE);
        gameOver.setX(SIZE / 2.0 - gameOver.getBoundsInParent().getWidth() / 2);
        gameOver.setY(SIZE / 2.0);

        flushObjects();

        root.getChildren().add(gameOver);
    }

    /**
     * What to do each time a key is pressed
     * @param code
     */
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}