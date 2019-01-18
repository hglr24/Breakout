package breakoutgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {

    private int myType;
    private Paint myColor;
    private int myHealth;
    private int myRow;
    private int myCol;
    private static final int BLOCK_WIDTH = 56;
    private static final int BLOCK_HEIGHT = 23;
    private boolean removed = false;
    private static final int offscreen = -100;

    public Block(int type, int hpos, int vpos, int r, int c) {
        super(vpos + Breakout.VERT_OFFSET, hpos + Breakout.HORIZ_OFFSET, 0, 0);
        myRow = r;
        myCol = c;
        myType = type;
        this.initialize(myType);
    }

    private void initialize(int type){
        this.setX(1.0 * Breakout.SIZE / Breakout.BLOCKS_PER_ROW * myCol + Breakout.HORIZ_OFFSET);
        this.setY(1.0 * Breakout.SIZE / Breakout.BLOCKS_PER_ROW / 2 * myRow + Breakout.VERT_OFFSET);
        this.setWidth(BLOCK_WIDTH);
        this.setHeight(BLOCK_HEIGHT);

        if (type != 3) myHealth = type;
        else myHealth = -1;
        updateColor();
    }

    private void updateColor(){
        switch(myHealth) {
            case -1:
                myColor = Color.GRAY;
                break;
            case 1:
                myColor = Color.SALMON;
                break;
            case 2:
                myColor = Color.GOLDENROD;
                break;
        }
        this.setFill(myColor);
    }

    private void remove(){
        this.setX(offscreen);
        this.removed = true;
    }

    public void updateHealth() {
        myHealth--;
        updateColor();
        if(myHealth == 0) this.remove();
    }

    public int getHealth() {
        return myHealth;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void flushBlock(Group root) {
        root.getChildren().remove(this);
    }
}
