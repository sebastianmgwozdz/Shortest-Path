package pathing;

import javafx.scene.paint.Color;

// Represents the current mode of the app, each associated with a square color
// SETTING_START: selecting starting point, SETTING_END: selecting ending point, SETTING_OBSTACLE: selecting obstacle
// points, DONE: game is over and algorithm has been completed
public enum Mode {
    SETTING_START(Color.BLUE), SETTING_END(Color.ORANGE), SETTING_OBSTACLE(Color.web("#6C3483")), DONE(null);

    private Color color;

    Mode(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }
}
