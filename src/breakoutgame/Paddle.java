package breakoutgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

/**
 * Class for paddle objects in Breakout game
 * @author Harry Ross (hgr8)
 */
public class Paddle extends Rectangle {
    private int myWidth;
    private Paint myColor;
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
        myWidth = PADDLE_ORIG_WIDTH;
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
        if (this.getWidth() == myWidth) {
            this.setWidth(2 * myWidth);
        }
    }

    /**
     * Shrinks paddle by half
     */
    public void shrink() {
        if (this.getWidth() > myWidth) {
            this.setWidth(myWidth / 2.0);
        }
    }

    /**
     * Makes paddle sticky (ball will now stick to it)
     */
    public void stickify() {
        mySticky = true;
    }

    /**
     * Returns whether or not paddle is sticky
     * @return Sticky boolean value
     */
    public boolean isSticky() {
        return mySticky;
    }
}