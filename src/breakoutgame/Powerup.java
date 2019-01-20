package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

/**
 * Class for power-up objects in Breakout game
 * @author Harry Ross (hgr8)
 */
public class Powerup extends ImageView {
    private int myType;

    /**
     * Creates power-up object of specified type
     * @param type Type of power-up to create
     */
    public Powerup(int type) {
        myType = type;
    }

    /**
     * Removes power-up from specified Group
     * @param root Group to remove power-up from
     */
    public void flushPowerup(Group root) {
        root.getChildren().remove(this);
    }
}