package pathing;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

// Checkbox that allows the user to choose between allowing diagonal algorithm movement and only horizontal/vertical
class DiagonalCheckBox extends CheckBox {
    DiagonalCheckBox(App app) {
        super("Allow diagonal movement");

        this.setStyle("-fx-font-size: 15;");

        // Updates the property value when the box is checked/unchecked
        this.selectedProperty().addListener((ObservableValue<? extends Boolean> ov,
                                             Boolean old_val, Boolean new_val) -> app.setAllowDiagonal(new_val));
    }
}
