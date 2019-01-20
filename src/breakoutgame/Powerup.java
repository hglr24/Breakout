package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for power-up objects in Breakout game
 * Depends on breakoutgame package, JavaFX library and Java.util.concurrent.ThreadLocalRandom
 * @author Harry Ross (hgr8)
 */
public class Powerup extends ImageView {
    private int myType;
    private int mySpeed;
    private static final int OFFSCREEN = -100;
    private static final int PUP_SPEED = 70;

    /**
     * Creates power-up object and initializes
     */
    public Powerup(double xpos, double ypos) {
        super();
        this.initialize(xpos, ypos);
    }

    /**
     * Initializes power-up at specified position
     * @param xpos Starting x-position
     * @param ypos Starting y-position
     */
    public void initialize(double xpos, double ypos) {
        myType = ThreadLocalRandom.current().nextInt(0, 4); // 0 -> length, 1 -> shrink, 2 -> sticky, 3 -> multiball
        mySpeed = PUP_SPEED;
        this.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream("pup" + myType + ".gif")));
        this.setX(xpos - this.getBoundsInParent().getWidth() / 2.0);
        this.setY(ypos - this.getBoundsInParent().getWidth() / 2.0);
    }

    /**
     * Moves power-up to next location
     * @param elapsedTime Time elapsed
     */
    public void move(double elapsedTime) {
        this.setY(this.getY() + mySpeed * elapsedTime);
    }

    /**
     * Remove power-up from play (use this over flushPowerup to avoid ConcurrentModificationException)
     */
    public void remove() {
        this.setX(OFFSCREEN);
        mySpeed = 0;
    }

    /**
     * Removes power-up from specified Group
     * @param root Group to remove power-up from
     */
    public void flushPowerup(Group root) {
        root.getChildren().remove(this);
    }

    /**
     * Returns type of power-up
     * @return Power-up type
     */
    public int getType() {
        return myType;
    }
}