package pathing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gp = new GridPane();
        gp.setVgap(2);
        gp.setHgap(2);

        HashMap<Integer, Integer> start = new HashMap<>();
        HashMap<Integer, Integer> obstacle = new HashMap<>();
        HashMap<Integer, Integer> end = new HashMap<>();

        Board b = new Board(start);

        b.addToPane(gp);

        Scene sc = new Scene(gp, 1546, 948);

        sc.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                switch (b.getMode()) {
                    case SETTING_START:
                        b.setMode(Mode.SETTING_OBSTACLE, obstacle);
                        break;
                    case SETTING_OBSTACLE:
                        b.setMode(Mode.SETTING_END, end);
                        break;
                    case SETTING_END:
                        Pathfinder pf = new Pathfinder(b);

                        LinkedHashSet<Square> shortestPath = pf.getAllSquaresVisited(start, obstacle, end);
                        break;
                }
            }
        });

        stage.setScene(sc);
        stage.show();
    }
}
