package breakoutgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

public class Paddle extends Rectangle {

    private int myWidth;
    private Paint myColor;
    private boolean mySticky;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_ORIG_WIDTH = 50;
    public static final double PADDLE_VERT_POS = 5.0/6.0;

    public Paddle(int horizontal, int vertical) {
        super(horizontal / 2 - PADDLE_ORIG_WIDTH / 2, vertical * PADDLE_VERT_POS, PADDLE_ORIG_WIDTH, PADDLE_HEIGHT);
        myColor = Color.LIGHTBLUE;
        myWidth = PADDLE_ORIG_WIDTH;
    }

    public void initialize() {
        this.setFill(myColor);
    }

    public void lengthen() {
        if (this.getWidth() == myWidth) {
            this.setWidth(2 * myWidth);
        }
    }

    public void shrink() {
        if (this.getWidth() > myWidth) {
            this.setWidth(myWidth / 2.0);
        }
    }

    public void stickify() {
        mySticky = !mySticky;
    }

    public boolean isSticky() {
        return mySticky;
    }
}
