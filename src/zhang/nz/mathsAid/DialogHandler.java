package zhang.nz.mathsAid;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

// Static class for handling various popup boxes
public class DialogHandler {
    static void displayErrorBox(String error) { // used for handling any errors in the application
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("MathsAid Error");
        alert.setHeaderText("MathsAid Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
    static boolean confirmationBox(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }

    }
}
