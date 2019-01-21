package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for enemy objects in Breakout game
 * Depends on breakoutgame package and JavaFX library
 * Ex. Enemy e = new Enemy(int type)
 * Ex. Enemy e = new Enemy(Enemy e) -> for projectiles
 * Ex. e.reverseX()
 * @author Harry Ross (hgr8)
 */
public class Enemy extends ImageView {
    private int myType;
    private int myXSpeed;
    private int myYSpeed;
    private int myXDirection;
    private boolean myRemoved;
    private static final int ZZ_XSPEED = 180;
    private static final int ZZ_YSPEED = 80;
    private static final int PR_XSPEED = 120;
    private static final int PR_YSPEED = 50;
    private static final int PROJ_SPEED = 300;
    private static final int STARTING_Y = -50;
    private static final int MAX_ENEMY_STARTX = 500;
    private static final int OFFSCREEN = -100;

    /***
     * Creates enemy of given type (0 -> zig-zag, 1-> projectile launcher
     * @param type Type of enemy
     */
    public Enemy(int type) {
        super();
        myType = type;
        myXDirection = randomDirection();
        myRemoved = false;
        this.initialize();
    }

    /**
     * Overloaded constructor for projectiles that are bound to other enemies
     * @param e Enemy that is shooting projectile
     */
    public Enemy(Enemy e) {
        this(2);
        this.setX(e.getX());
        this.setY(e.getY());
        myYSpeed = PROJ_SPEED;
        myXDirection = 0;
        myXDirection = e.myXDirection * -1;
    }

    /**
     * Initializes enemy with starting properites based on type
     */
    private void initialize() {
        this.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream("en" + myType + ".gif")));
        if (myType == 0) {
            myXSpeed = ZZ_XSPEED;
            myYSpeed = ZZ_YSPEED;
        }
        if (myType == 1) {
            myXSpeed = PR_XSPEED;
            myYSpeed = PR_YSPEED;
        }
        if (myType != 2) {
            this.setX(ThreadLocalRandom.current().nextInt(0, MAX_ENEMY_STARTX));
            this.setY(STARTING_Y);
        }
    }

    /**
     * Moves enemy to next location
     */
    public void move(double elapsedTime, Group root) {
        this.setX(this.getX() + myXSpeed * myXDirection * elapsedTime);
        this.setY(this.getY() + myYSpeed * elapsedTime);
        if (this.getX() + this.getBoundsInParent().getWidth() >= Breakout.SIZE || this.getX() <= 0) {
            this.reverseX();
        }
        if (this.getY() >= Breakout.SIZE) {
            this.myXSpeed = 0;
            this.myYSpeed = 0;
            this.flushEnemy(root);
        }
    }

    /**
     * Calculates random starting direction for enemy movement (-1 or 1)
     * @return Random direction
     */
    private int randomDirection() {
        return (int) Math.signum((Math.random() * 2) - 1);
    }

    private void reverseX(){
        myXDirection *= -1;
    }

    /**
     * Removes enemy from play (use instead of flushEnemy to avoid ConcurrentModificationException
     */
    public void remove() {
        this.setX(OFFSCREEN);
        this.setY(OFFSCREEN);
        this.myXSpeed = 0;
        this.myYSpeed = 0;
        myRemoved = true;
    }

    /**
     * Removes enemy from specified group
     * @param root Group to remove enemy from
     */
    public void flushEnemy(Group root) {
        root.getChildren().remove(this);
    }

    /**
     * Returns type of enemy
     * @return Enemy type
     */
    public int getType() {
        return myType;
    }

    /**
     * Returns whether or not enemy has been removed from play
     * @return Removal status
     */
    public boolean isRemoved() {
        return myRemoved;
    }
}
