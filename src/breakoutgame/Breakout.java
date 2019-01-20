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
import javafx.scene.shape.Rectangle;
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
    public static final int BLOCKS_PER_COL = 10;
    public static final int BLOCKS_PER_ROW = 10;
    public static final int HORIZ_OFFSET = 2;
    public static final int VERT_OFFSET = 25;
    public static final int FONT_SIZE_LRG = 70;
    public static final int FONT_SIZE_SML = 30;
    public static final double DEFAULT_STROKE = 1.9;
    public static final int STARTING_LIVES = 2;

    // some things we need to remember during our game
    private Scene myScene;
    private Stage myStage;
    private Group root;
    private Paddle myPaddle;
    private ArrayList<Ball> ballList;
    private ArrayList<Block> blockList;
    private ArrayList<Text> textList;
    private int currLevel = 0;
    private int playerScore;
    private static int livesCount;
    private static int activeBalls;
    private boolean levelComplete;
    private boolean gameOver;
    private Point currLocation;
    private Text scoreText;
    private Text livesText;

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
        textList = new ArrayList<>();
        livesCount = STARTING_LIVES;
        scoreText = new Text();
        root.getChildren().add(scoreText);
        livesText = new Text();
        root.getChildren().add(livesText);
        gameOver = false;

        myPaddle = new Paddle(width, height);
        root.getChildren().add(myPaddle);
        myPaddle.initialize();

        drawNewBall(width, height);
        drawBlocks(currLevel);
        drawText("Breakout", FONT_SIZE_LRG, SIZE / 5.0);

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
            if (!b.isLaunched()) {
                b.attachToPaddle(myPaddle);
            }
            else {
                b.setLastX(b.getX());
                b.setLastY(b.getY());
                b.setX(b.getX() + b.getXSpeed() * b.getXDirection() * elapsedTime);
                b.setY(b.getY() + b.getYSpeed() * b.getYDirection() * elapsedTime);

                if (b.getX() + b.getBoundsInParent().getWidth() >= SIZE || b.getX() <= 0) {
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
    }

    /**
     *
     */
    private void checkForCollisions() {
        levelComplete = true;

        for (Ball b : ballList) {
            if (myPaddle.getBoundsInParent().intersects(b.getBoundsInParent())) {
                rectangleCollision(myPaddle, b);
            }
            for (Block blo : blockList) {
                if (blo.getBoundsInParent().intersects(b.getBoundsInParent())) {
                    rectangleCollision(blo, b);
                    blo.updateHealth();
                    if (blo.getHealth() != -1) updatePlayerVar(scoreText, 200);
                }
                if (blo.getHealth() > 0 && !blo.isRemoved()) {
                    levelComplete = false;
                }
            }
            if (levelComplete) {
                updatePlayerVar(scoreText, 10000);
                goToLevel(currLevel + 1);
            }
        }
    }

    private void rectangleCollision(Rectangle r, Ball b) {
        if (b.getLastY() + b.getBoundsInParent().getHeight() < r.getY()) { //must be above brick
            b.reverseY();
        }
        else if (b.getLastY() > r.getY() + r.getBoundsInParent().getHeight()) { //must be below
            b.reverseY();
        }
        else { //must be left or right
            b.reverseX();
        }
        b.revert();
    }

    /**
     *
     * @param horiz
     * @param vert
     */
    private void drawNewBall(int horiz, int vert) {
        Ball newBall = new Ball(horiz, vert);
        newBall.initialize(myPaddle);
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
        if (livesCount > 0 && activeBalls == 0 && currLevel != 0){
            updatePlayerVar(livesText, -1);
            b.initialize(myPaddle);
            activeBalls++;
        }
        else if (livesCount == 0 && textList.size() == 0){
            updatePlayerVar(livesText, -1);
            flushObjects();
            drawText("Game Over", FONT_SIZE_LRG, SIZE / 2.0);
            gameOver = true;
        }
    }

    private void updatePlayerVar(Text t, int add) {
        double xpos = 0;
        t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, FONT_SIZE_SML));
        t.setFill(Color.LIGHTBLUE);
        if (t.equals(livesText)) {
            livesCount += add;
            t.setText("Lives: " + (livesCount + 1));
            xpos = SIZE - HORIZ_OFFSET - livesText.getBoundsInParent().getWidth();
        }
        if (t.equals(scoreText)) {
            playerScore += add;
            t.setText("" + playerScore);
            xpos = HORIZ_OFFSET;
        }
        t.setX(xpos);
        t.setY(SIZE - VERT_OFFSET);
    }

    /**
     *
     */
    private void goToLevel(int level) {
        if (level <= 3) {
            currLevel = level;

            flushObjects();

            drawNewBall(SIZE, SIZE);
            drawBlocks(currLevel);
            updatePlayerVar(scoreText, 0);
            updatePlayerVar(livesText, 0);
        }

        else {
            flushObjects();
            drawText("You Win!", FONT_SIZE_LRG, SIZE / 2.0);
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

        for (Text t : textList) {
            root.getChildren().remove(t);
        }

        blockList = new ArrayList<>();
        ballList = new ArrayList<>();
        textList = new ArrayList<>();
    }

    /**
     *
     * @param message
     */
    private void drawText(String message, int size, double vpos) {
        Text drawnText = new Text();
        drawnText.setText(message);
        drawnText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, size));
        drawnText.setFill(Color.WHITE);
        drawnText.setStroke(Color.GRAY);
        drawnText.setStrokeWidth(DEFAULT_STROKE);
        drawnText.setX(SIZE / 2.0 - drawnText.getBoundsInParent().getWidth() / 2);
        drawnText.setY(vpos);

        textList.add(drawnText);

        root.getChildren().add(drawnText);

        if (message.equals("Breakout")) {
            drawText("1 Hit     2 Hits       Unbreakable\n\n  " +
                            "Control paddle with mouse!\n\n         Press Enter to play!",
                    FONT_SIZE_SML, SIZE * 3 / 5.0); // TODO constants
        }
    }

    /**
     * What to do each time a key is pressed
     * @param code
     */
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.SPACE) {
            for (Ball b : ballList) {
                if (!b.isLaunched()) {
                    b.launch();
                }
            }
        }
        if (code == KeyCode.ENTER && currLevel == 0) {
            goToLevel(1);
        }
        if (!gameOver) {
            if (code == KeyCode.F1) {
                goToLevel(1);
            }
            if (code == KeyCode.F2) {
                goToLevel(2);
            }
            if (code == KeyCode.F3) {
                goToLevel(3);
            }
            if (code == KeyCode.F11) {
                updatePlayerVar(livesText, 1);
            }
        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}