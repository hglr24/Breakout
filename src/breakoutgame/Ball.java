package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Ball extends ImageView {

    private int myWidth;
    private int myHeight;
    private int myXSpeed;
    private int myYSpeed;
    private int myXDirection;
    private int myYDirection;
    private int mySpeedRange = 40;
    private int myMinSpeed = 80;
    private static final String BALL_IMAGE = "ball.gif";

    public Ball(int w, int h){
        super();
        myWidth = w;
        myHeight = h;
    }

    public void initialize(){
        this.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)));
        this.setX(myWidth / 2.0 - this.getBoundsInLocal().getWidth() / 2);
        this.setY(myHeight / 2.0 - this.getBoundsInLocal().getHeight() / 2);
        myXSpeed = (int) ((Math.random() * mySpeedRange) + myMinSpeed) * randomDirection();
        myYSpeed = (int) (Math.random() * mySpeedRange) + myMinSpeed;
        myXDirection = 1;
        myYDirection = 1;
    }

    private int randomDirection() {
        return (int) Math.signum((Math.random() * 2) - 1);
    }

    public void flushBall(Group root) {
        root.getChildren().remove(this);
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

    public void reverseX(){
        myXDirection *= -1;
    }

    public void reverseY(){
        myYDirection *= -1;
    }
}