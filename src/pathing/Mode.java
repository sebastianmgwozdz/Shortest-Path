package pathing;

import javafx.scene.paint.Color;

public enum Mode {
    SETTING_START(Color.BLUE), SETTING_END(Color.ORANGE), SETTING_OBSTACLE(Color.web("#6C3483"));

    private Color color;

    Mode(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
