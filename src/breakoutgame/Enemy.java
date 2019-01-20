package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

/**
 * Class for enemy objects in Breakout game
 * Depends on breakoutgame package and JavaFX library
 * @author Harry Ross (hgr8)
 */
public class Enemy extends ImageView {
    private int myType;

    /***
     * Creates enemy of given type (0 -> zig-zag, 1-> projectile launcher
     * @param type
     */
    public Enemy(int type) {
        myType = type;
    }

    /**
     *
     * @param root
     */
    public void flushEnemy(Group root) {
        root.getChildren().remove(this);
    }
}
