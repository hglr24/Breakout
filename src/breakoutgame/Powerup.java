package breakoutgame;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class Powerup extends ImageView {

    private int myType;

    public Powerup(int type) {
        myType = type;
    }

    public void flushPowerup(Group root) {
        root.getChildren().remove(this);
    }
}