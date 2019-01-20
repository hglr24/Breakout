package breakoutgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Class for block objects in Breakout game
 * @author Harry Ross (hgr8)
 */
public class Block extends Rectangle {
    private Paint myColor;
    private int myHealth;
    private int myRow;
    private int myCol;
    private static final int BLOCK_WIDTH = 56;
    private static final int BLOCK_HEIGHT = 23;
    private boolean removed = false;
    private static final int offscreen = -100;

    /**
     * Creates block with given parameters
     * @param type Type of block (1-hit, 2-hit, unbreakable)
     * @param hpos Horizontal position of block
     * @param vpos Vertical position of block
     * @param r Row coordinate of block
     * @param c Column coordinate of block
     */
    public Block(int type, int hpos, int vpos, int r, int c) {
        super(vpos + Breakout.VERT_OFFSET, hpos + Breakout.HORIZ_OFFSET, 0, 0);
        myRow = r;
        myCol = c;
        this.initialize(type);
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

    /**
     * Update health of block when struck by ball
     */
    public void updateHealth() {
        if (myHealth != -1) {
            myHealth--;
            updateColor();
            if (myHealth == 0) this.remove();
        }
    }

    private void remove(){
        this.setX(offscreen);
        this.removed = true;
    }

    /**
     * Removes block from specified Group
     * @param root Group to remove block from
     */
    public void flushBlock(Group root) {
        root.getChildren().remove(this);
    }

    /**
     * Returns current health of block
     * @return Current block health
     */
    public int getHealth() {
        return myHealth;
    }

    /**
     * Returns whether or not a block has been removed from play
     * @return Removal status
     */
    public boolean isRemoved() {
        return removed;
    }
}