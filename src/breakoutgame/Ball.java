package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Ball extends ImageView {

    private int myWidth;
    private int myHeight;
    private int myXSpeed = 0;
    private int myYSpeed = 0;
    private int myXDirection;
    private int myYDirection;
    private int mySpeedRange = 40;
    private int myMinSpeed = 150;
    private double myLastY;
    private double myLastX;
    private boolean myLaunchStatus;
    private static final String BALL_IMAGE = "ball.gif";

    public Ball(int w, int h) {
        super();
        myWidth = w;
        myHeight = h;
    }

    public void initialize(Paddle p) {
        this.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)));
        this.attachToPaddle(p);
        myXDirection = 1;
        myYDirection = 1;
        myLaunchStatus = false;
    }

    public void launch() {
        myLaunchStatus = true;
        myXSpeed = (int) ((Math.random() * mySpeedRange) + myMinSpeed) * randomDirection();
        myYSpeed = (int) (Math.random() * mySpeedRange) + myMinSpeed * -1;
    }

    public void attachToPaddle(Paddle p) {
        this.setX(p.getX() + p.getBoundsInParent().getWidth() / 2 - this.getBoundsInParent().getWidth() / 2);
        this.setY(p.getY() - this.getBoundsInParent().getHeight() - 1);
    }

    private int randomDirection() {
        return (int) Math.signum((Math.random() * 2) - 1);
    }

    public void revert() {
        this.setX(myLastX);
        this.setY(myLastY);
    }

    public void flushBall(Group root) {
        root.getChildren().remove(this);
    }

    public void reverseX(){
        myXDirection *= -1;
    }

    public void reverseY(){
        myYDirection *= -1;
    }

    public void setLastY(double y) {
        myLastY = y;
    }

    public void setLastX(double x) {
        myLastX = x;
    }

    public double getLastY() {
        return myLastY;
    }

    public int getXSpeed(){
        return myXSpeed;
    }

    public int getYSpeed(){
        return myYSpeed;
    }

    public int getXDirection(){
        return myXDirection;
    }

    public int getYDirection(){
        return myYDirection;
    }

    public boolean isLaunched() {
        return myLaunchStatus;
    }
}