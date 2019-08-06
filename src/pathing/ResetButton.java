package pathing;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// Models a button that resets the application
public class ResetButton extends Button {
    public ResetButton(String text, App app, Stage stage) {
        super(text);
        this.setMinHeight(28);
        this.setMinWidth(200);

        // Rerun the app when clicked
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                app.runApp(stage);
            }
        });
    }
}
