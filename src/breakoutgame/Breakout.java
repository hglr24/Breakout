package breakoutgame;

import java.util.*;
import java.awt.*;
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
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

/**
 * Main game class for Breakout, developed for CS308
 * @author Harry Ross (hgr8)
 */
public class Breakout extends Application {
    private static final String TITLE = "Breakout Game (hgr8)";
    public static final int SIZE = 600;
    public static final int BLOCKS_PER_COL = 10;
    public static final int BLOCKS_PER_ROW = 10;
    public static final int HORIZ_OFFSET = 2;
    public static final int VERT_OFFSET = 25;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final int SCENE_DIM_OFFSET = 14;
    private static final Paint BACKGROUND_COLOR = Color.NAVY;
    private static final double INSTRUCTION_TXT_POS = 3.0 / 5;
    private static final int FONT_SIZE_LRG = 70;
    private static final int FONT_SIZE_SML = 30;
    private static final double DEFAULT_STROKE = 1.9;
    private static final int STARTING_LIVES = 2;

    private Stage myStage;
    private Group root;
    private Paddle myPaddle;
    private ArrayList<Ball> ballList;
    private ArrayList<Block> blockList;
    private ArrayList<Text> textList;
    private ArrayList<Powerup> powerupList;
    private int currLevel;
    private int playerScore;
    private int livesCount;
    private int activeBalls;
    private boolean gameOver;
    private Text scoreText;
    private Text livesText;
    private Text levelText;
    private double sceneDim;

    /**
     * Starts the JavaFX application
     * @param stage Game stage
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        Scene myScene = setupGame(SIZE);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame (int squareDim) {
        root = new Group();
        Scene scene = new Scene(root, squareDim, squareDim, BACKGROUND_COLOR);
        sceneDim = scene.getWidth() + SCENE_DIM_OFFSET;
        initializeGameVars();

        drawNewPaddle(squareDim, squareDim);
        drawNewBall();
        drawBlocks(currLevel);
        drawText("Breakout", FONT_SIZE_LRG, SIZE / 5.0);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode())); // Handle input
        return scene;
    }

    private void initializeGameVars() {
        ballList = new ArrayList<>(); // Create new lists to keep track of objects
        blockList = new ArrayList<>();
        powerupList = new ArrayList<>();
        textList = new ArrayList<>();

        currLevel = 0;
        livesCount = STARTING_LIVES;
        gameOver = false;

        scoreText = new Text(); // Create score and lives text
        livesText = new Text();
        levelText = new Text();
        root.getChildren().add(scoreText);
        root.getChildren().add(livesText);
        root.getChildren().add(levelText);
    }

    private void step (double elapsedTime) {
        maintainSceneDims();
        handleMouseControl();
        updateBallAttributes(elapsedTime);
        checkForCollisions();
    }

    private void maintainSceneDims() {
        myStage.setHeight(sceneDim);
        myStage.setWidth(sceneDim);
    }

    private void updateBallAttributes(double elapsedTime) {
        for (Ball b : ballList) {
            if (!b.isRemoved()) {
                if (!b.isLaunched()) {
                    b.attachToPaddle(myPaddle);
                } else {
                    moveBall(b, elapsedTime);
                }
            }
        }
    }

    private void moveBall(Ball b, double elapsedTime) {
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
            if (activeBalls > 0) {
                b.flushBall(root);
                b.remove();
            }
            else {
                resetBall(b);
            }
        }
    }

    private void checkForCollisions() {
        boolean levelComplete = true;
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
        boolean paddleFree = true;
        if (b.getLastY() + b.getBoundsInParent().getHeight() < r.getY()) { //must be above brick
            b.reverseY();
            if (r instanceof Paddle && ((Paddle) r).isSticky()) {
                for (Ball ball : ballList) {
                    if (!ball.isLaunched()) paddleFree = false;
                }
                if (paddleFree) b.initialize((Paddle) r);
            }
        }
        else if (b.getLastY() > r.getY() + r.getBoundsInParent().getHeight()) { //must be below
            b.reverseY();
        }
        else { //must be left or right
            b.reverseX();
        }
        b.revert();
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
        if (t.equals(levelText)) {
            t.setText("Level " + currLevel);
            xpos = SIZE / 2.0 - t.getBoundsInParent().getWidth() / 2.0;
        }
        t.setX(xpos);
        t.setY(SIZE - VERT_OFFSET);
    }

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

    private void flushObjects() {
        for (Ball b : ballList) {
            b.flushBall(root);
        }
        for (Block b : blockList) {
            b.flushBlock(root);
        }
        for (Powerup p : powerupList) {
            p.flushPowerup(root);
        }
        for (Text t : textList) {
            root.getChildren().remove(t);
        }
        activeBalls = 0;
        blockList = new ArrayList<>();
        ballList = new ArrayList<>();
        powerupList = new ArrayList<>();
        textList = new ArrayList<>();
    }


    private void drawNewBall() {
        Ball newBall = new Ball();
        newBall.initialize(myPaddle);
        ballList.add(newBall);
        root.getChildren().add(newBall);
        activeBalls++;
    }

    private void drawNewPaddle(int horiz, int vert) {
        myPaddle = new Paddle(horiz, vert);
        root.getChildren().add(myPaddle);
        myPaddle.initialize();
    }

    private void drawBlocks(int level) {
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
                    FONT_SIZE_SML, SIZE * INSTRUCTION_TXT_POS);
        }
    }

    private void goToLevel(int level) {
        if (level <= 3) {
            currLevel = level;

            flushObjects();

            drawNewBall();
            myPaddle.initialize();
            drawBlocks(currLevel);
            updatePlayerVar(scoreText, 0);
            updatePlayerVar(livesText, 0);
            updatePlayerVar(levelText, 0);
        }

        else {
            flushObjects();
            levelText.setVisible(false);
            drawText("You Win!", FONT_SIZE_LRG, SIZE / 2.0);
        }
    }

    private void handleMouseControl() {
        Point currBallPos = MouseInfo.getPointerInfo().getLocation();
        myPaddle.setX(currBallPos.x - myStage.getX() - myPaddle.getBoundsInParent().getWidth() / 2);
    }

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
            if (code == KeyCode.B) {
                drawNewBall();
            }
            if (code == KeyCode.L) {
                myPaddle.lengthen();
            }
            if (code == KeyCode.S) {
                myPaddle.shrink();
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