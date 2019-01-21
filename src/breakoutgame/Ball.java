package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for ball objects in Breakout game
 * Depends on breakoutgame package, JavaFX library and java.util.concurrent.ThreadLocalRandom
 * @author Harry Ross (hgr8)
 */
public class Ball extends ImageView {
    private int myXSpeed = 0;
    private int myYSpeed = 0;
    private int myXDirection;
    private int myYDirection;
    private double myLastY;
    private double myLastX;
    private boolean myLaunchStatus;
    private boolean myRemoved;
    private static final String BALL_IMAGE = "ball.gif";
    private static final int OFFSCREEN = -100;

    /**
     * Creates ball object
     */
    public Ball() {
        super();
    }

    /**
     * Initializes ball, attaches to paddle
     * @param p Player paddle
     */
    public void initialize(Paddle p) {
        this.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)));
        this.attachToPaddle(p);
        myXDirection = 1;
        myYDirection = 1;
        myXSpeed = 0;
        myYSpeed = 0;
        myLaunchStatus = false;
    }

    /**
     * Launches ball in random direction from paddle
     */
    public void launch() {
        int mySpeedRange = 60;
        int myMinSpeed = 200;
        int minAngle = 15;
        int maxAngle = 40;
        double netSpeed = ThreadLocalRandom.current().nextInt(myMinSpeed, myMinSpeed + mySpeedRange);
        double randAngle = 2 * Math.PI / 180 * ThreadLocalRandom.current().nextInt(minAngle, maxAngle);
        myLaunchStatus = true;
        myXSpeed = (int) (netSpeed * Math.cos(randAngle) * randomDirection());
        myYSpeed = (int) (netSpeed * Math.sin(randAngle) * -1);
    }

    /**
     * Attaches ball to paddle position
     * @param p Player paddle
     */
    public void attachToPaddle(Paddle p) {
        this.setX(p.getX() + p.getBoundsInParent().getWidth() / 2 - this.getBoundsInParent().getWidth() / 2);
        this.setY(p.getY() - this.getBoundsInParent().getHeight() - 1);
    }

    /**
     * Calculates random starting direction for ball (-1 or 1)
     * @return
     */
    private int randomDirection() {
        return (int) Math.signum((Math.random() * 2) - 1);
    }

    /**
     * Reverts ball position to that of frame before collision detected
     */
    public void revert() {
        this.setX(myLastX);
        this.setY(myLastY);
    }

    /**
     * Removes ball from play area (use this over flushBall to avoid ConcurrentModificationException)
     */
    public void remove() {
        this.setX(OFFSCREEN);
        myXSpeed = 0;
        myYSpeed = 0;
        myRemoved = true;
    }

    /**
     * Removes ball from specified Group
     * @param root Group to remove ball from
     */
    public void flushBall(Group root) {
        root.getChildren().remove(this);
    }

    /**
     * Reverses x-direction of ball
     */
    public void reverseX(){
        myXDirection *= -1;
    }

    /**
     * Reverses y-direction of ball
     */
    public void reverseY(){
        myYDirection *= -1;
    }

    /**
     * Stores previous frame's y-position
     * @param y Previous y-position
     */
    public void setLastY(double y) {
        myLastY = y;
    }

    /**
     * Stores previous frame's x-position
     * @param x Previous x-position
     */
    public void setLastX(double x) {
        myLastX = x;
    }

    /**
     * Returns previous frame's y-location
     * @return Previous y-location
     */
    public double getLastY() {
        return myLastY;
    }

    /**
     * Returns current x-speed of ball
     * @return Current x-speed
     */
    public int getXSpeed(){
        return myXSpeed;
    }

    /**
     * Returns current y-speed of ball
     * @return Current y-speed
     */
    public int getYSpeed(){
        return myYSpeed;
    }

    /**
     * Returns current x-direction of ball
     * @return Current x-direction
     */
    public int getXDirection(){
        return myXDirection;
    }

    /**
     * Returns current y-direction of ball
     * @return Current y-direction
     */
    public int getYDirection(){
        return myYDirection;
    }

    /**
     * Returns whether or not ball has been launched
     * @return Launch status
     */
    public boolean isLaunched() {
        return myLaunchStatus;
    }

    /**
     * Returns whether or not ball has been removed from play
     * @return Removal status
     */
    public boolean isRemoved() {
        return myRemoved;
    }
}