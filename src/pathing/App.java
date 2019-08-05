package pathing;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        App app = new App();
        app.runApp(stage);
    }

    private void runApp(Stage stage) {


        Scene sc = getScene(stage);


        stage.setScene(sc);
        stage.show();
    }

    private Scene getScene(Stage stage) {
        VBox vb = new VBox();
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: #85C1E9");

        GridPane gp = new GridPane();
        gp.setVgap(2);
        gp.setHgap(2);

        HashMap<Integer, Integer> start = new HashMap<>();
        HashMap<Integer, Integer> end = new HashMap<>();

        Board b = new Board(start);

        b.addToPane(gp);

        Button button = new Button("Reset");
        button.setMinHeight(50);
        button.setMinWidth(200);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                runApp(stage);
            }
        });

        vb.getChildren().addAll(gp, button);

        Scene sc = new Scene(vb, 1528, 985);

        sc.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F) {
                switch (b.getMode()) {
                    case SETTING_START:
                        b.setMode(Mode.SETTING_OBSTACLE, null);
                        break;
                    case SETTING_OBSTACLE:
                        b.setMode(Mode.SETTING_END, end);
                        break;
                    case SETTING_END:
                        Pathfinder pf = new Pathfinder(b);

                        Square beginning = b.getValues()[(int) start.values().toArray()[0]][(int) start.keySet().toArray()[0]];
                        Square ending = b.getValues()[(int) end.values().toArray()[0]][(int) end.keySet().toArray()[0]];

                        pf.run(beginning, ending);

                        break;
                }
            }
        });

        return sc;
    }
}
