package pathing;

import javafx.scene.layout.GridPane;

import java.util.HashMap;

public class Board {
    private Square[][] values;
    private Mode mode;

    public Board(HashMap<Integer, Integer> start) {
        this.values = new Square[27][45];

        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 45; j++) {
                values[i][j] = new Square(j, i, 32, 32);

            }
        }

        setMode(Mode.SETTING_START, start);

        fillNeighbors();
    }

    public Square[][] getValues() {
        return values;
    }

    public Mode getMode() {
        return mode;
    }

    private void fillNeighbors() {
        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 45; j++) {
                values[i][j].fillNeighbors(values);
            }
        }
    }

    public void setMode(Mode m, HashMap<Integer, Integer> coordinates) {
        this.mode = m;
        setOnClickAction(coordinates);
    }

    public void addToPane(GridPane gp) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                gp.add(values[i][j], j, i);
            }
        }
    }

    public void setOnClickAction(HashMap<Integer, Integer> coordinates) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j].addAction(coordinates, this);
            }
        }
    }

}
