package pathing;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Set;

// Models a 45 x 27 grid of squares
class Board {
    private Square[][] values;
    private Mode mode;
    private final Set<KeyCode> pressedKeys;

    Board(HashMap<Integer, Integer> start, Set<KeyCode> pressedKeys) {
        this.pressedKeys = pressedKeys;
        this.values = new Square[27][45];

        // Fill 2D array with squares
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 45; j++) {
                values[i][j] = new Square(j, i, 32, 32);

            }
        }

        setMode(Mode.SETTING_START, start);
    }

    Square[][] getValues() {
        return values;
    }

    Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    Mode getMode() {
        return mode;
    }

    // For each square in the grid, fills the set of its neighboring blocks
    void fillNeighbors(boolean diagonalActive) {
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 45; j++) {
                values[i][j].fillNeighbors(values, diagonalActive);
            }
        }
    }

    void setMode(Mode m, HashMap<Integer, Integer> coordinates) {
        this.mode = m;

        if (m.equals(Mode.DONE)) {
            return;
        }
        setOnClickAction(coordinates);
    }

    void addToPane(GridPane gp) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                gp.add(values[i][j], j, i);
            }
        }
    }

    // Updates each square's on click behavior depending on the board mode
    private void setOnClickAction(HashMap<Integer, Integer> coordinates) {
        for (Square[] value : values) {
            for (int j = 0; j < values[0].length; j++) {
                value[j].addAction(coordinates, this);
            }
        }
    }

}
