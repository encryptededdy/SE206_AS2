package zhang.nz.mathsAid;

import javafx.scene.control.Alert;

// Static class for handling various popup boxes
public class DialogHandler {
    protected static void displayErrorBox(String error) { // used for handling any errors in the application
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("MathsAid Error");
        alert.setHeaderText("MathsAid Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
