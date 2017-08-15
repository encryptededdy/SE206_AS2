package zhang.nz.mathsAid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.nio.file.Files;

public class Controller {
    // Configuration
    private final String workingDir = "creations";

    // Creating objects from FXML
    @FXML private Button playbtn;
    @FXML private Button deletebtn;
    @FXML private Button createbtn;
    @FXML private ListView<String> creationlist;

    @FXML
    protected void initialize() {
        populateCreations();
    }

    @FXML
    protected void playPressed(ActionEvent event) {
        System.out.println("Someone pressed play!");
    }

    private void populateCreations() { // populates the list of creations
        File file = new File(workingDir);
        System.out.println("Working in: " + file.getAbsolutePath());
        if (!file.isDirectory()) {
            System.out.println("Directory doesn't exist, creating it!");
            if (!file.mkdir()) { // make the directory
                displayErrorBox("MathsAid was unable to create the creations directory! This may mean it doesn't have permission to write to the current working directory");
            }
        }
        String[] creations = file.list();
        ObservableList<String> items = FXCollections.observableArrayList(creations);
        creationlist.setItems(items);
    }

    private void displayErrorBox(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("MathsAid Error");
        alert.setHeaderText("MathsAid Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
