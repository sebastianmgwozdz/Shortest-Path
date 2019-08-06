package pathing;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean allowDiagonal = false;

    void setAllowDiagonal(boolean b) {
        allowDiagonal = b;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        App app = new App();
        app.runApp(stage);
    }

    void runApp(Stage stage) {
        Scene sc = getScene(stage);

        stage.setScene(sc);
        stage.setTitle("Shortest Path");
        stage.show();
    }

    private Scene getScene(Stage stage) {
        // Coordinates of starting point
        HashMap<Integer, Integer> start = new HashMap<>();
        // Coordinates of ending point
        HashMap<Integer, Integer> end = new HashMap<>();

        // Container for all GUI elements (Grid, row of options/buttons)
        VBox vb = new VBox();
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: #85C1E9");

        GridPane gp = new GridPane();
        // Space out grid boxes
        gp.setVgap(2);
        gp.setHgap(2);

        Board b = new Board(start, pressedKeys);
        b.addToPane(gp);


        HBox optionRow = new HBox();
        optionRow.setAlignment(Pos.CENTER);

        DiagonalCheckBox diagonalOption = new DiagonalCheckBox(this);
        Button resetButton = new ResetButton("Reset", this, stage);
        Label currentMode = new Label(getLabelText(b));
        currentMode.setAlignment(Pos.CENTER);
        currentMode.setMinWidth(750);
        currentMode.setFont(new Font(28));

        optionRow.getChildren().addAll(diagonalOption, currentMode, resetButton);


        vb.getChildren().addAll(gp, optionRow);

        Scene sc = new Scene(vb, 1528, 985);
        setKeyAction(sc, b, start, end, currentMode);

        return sc;
    }

    // Read in key presses by the user to allow cycling through modes and laying down of obstacles
    private void setKeyAction(Scene sc, Board b, HashMap<Integer, Integer> start, HashMap<Integer, Integer> end, Label currentMode) {
        // Switches modes
        sc.setOnKeyPressed(e -> {
            // Signals key is pressed
            pressedKeys.add(e.getCode());

            if (e.getCode() == KeyCode.F) {
                switch (b.getMode()) {
                    case SETTING_START:
                        // Only proceeds if a starting point has been placed
                        if (start.size() == 1) {
                            b.setMode(Mode.SETTING_OBSTACLE, null);
                        }
                        break;
                    case SETTING_OBSTACLE:
                        // Locks in diagonal / no diagonal choice by user
                        b.fillNeighbors(allowDiagonal);
                        b.setMode(Mode.SETTING_END, end);
                        break;
                    case SETTING_END:
                        if (end.size() == 1) {
                            // Runs algorithm and draws solution
                            Pathfinder pf = new Pathfinder(b);

                            // Convert coordinates in hashset to squares from the board
                            Square beginning = b.getValues()[(int) start.values().toArray()[0]][(int) start.keySet().toArray()[0]];
                            Square ending = b.getValues()[(int) end.values().toArray()[0]][(int) end.keySet().toArray()[0]];

                            pf.run(beginning, ending);

                            b.setMode(Mode.DONE, null);
                        }

                        break;
                    default:
                        break;
                }
                currentMode.setText(getLabelText(b));
            }
        });

        // Key no longer pressed
        sc.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
    }

    // Updates displayed text based on the current mode
    private String getLabelText(Board b) {
        String s = ", F to continue";
        switch (b.getMode()) {
            case SETTING_START:
                return "Select starting point (Click to set)" + s;
            case SETTING_OBSTACLE:
                return "Add obstacles (Hold G)" + s;
            case SETTING_END:
                return "Select ending point (Click to set)" + s;
            default:
                return "Press reset to start over";
        }
    }
}
