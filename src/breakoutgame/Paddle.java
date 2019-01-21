package breakoutgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for paddle objects in Breakout game
 * Depends on breakoutgame package, JavaFX library
 * Ex. Paddle p = new Paddle(int horizontal, int vertical)
 * Ex. p.lengthen()
 * @author Harry Ross (hgr8)
 */
public class Paddle extends Rectangle {
    private Paint myColor;
    private Timer timer;
    private boolean mySticky;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_ORIG_WIDTH = 50;
    private static final double PADDLE_VERT_POS = 8.8/10;

    /**
     * Creates a paddle object with specified properties
     * @param horizontal Horizontal location of paddle
     * @param vertical Vertical position of paddle
     */
    public Paddle(int horizontal, int vertical) {
        super(horizontal / 2.0 - PADDLE_ORIG_WIDTH / 2.0, vertical * PADDLE_VERT_POS, PADDLE_ORIG_WIDTH, PADDLE_HEIGHT);
        myColor = Color.LIGHTBLUE;
    }

    /**
     * Initializes paddle properties
     */
    public void initialize() {
        this.setFill(myColor);
        this.setWidth(PADDLE_ORIG_WIDTH);
        this.setHeight(PADDLE_HEIGHT);
        mySticky = false;
    }

    /**
     * Lengthens paddle to twice original size
     */
    public void lengthen() {
        this.setWidth(PADDLE_ORIG_WIDTH * 2);
    }

    /**
     * Shrinks paddle by half
     */
    public void shrink() {
        this.setWidth(PADDLE_ORIG_WIDTH / 2.0);
    }

    /**
     * Makes paddle sticky (ball will now stick to it)
     */
    public void stickify() {
        mySticky = true;
    }

    /**
     * "Kills" paddle when hit by enemy
     */
    public void kill() {
        timer = new Timer();
        this.initialize();
        this.flash();
    }

    private void flash() { //TODO: fix this timer
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                changeColor();
                count++;
                if (count == 6) {
                    this.cancel();
                    timer.purge();
                }
            }
        }, 0, 100);
        timer.cancel();
    }

    private void changeColor() {
        if (this.getFill().equals(Color.LIGHTBLUE)) setFill(Color.SALMON);
        else if (this.getFill().equals(Color.SALMON)) setFill(Color.LIGHTBLUE);
    }

    /**
     * Returns whether or not paddle is sticky
     * @return Sticky boolean value
     */
    public boolean isSticky() {
        return mySticky;
    }
}