package zhang.nz.mathsAid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;

import java.io.File;

public class Controller {
    // Configuration
    private final String _workingDir = "creations";

    // Creating objects from FXML
    @FXML private Button playbtn;
    @FXML private Button deletebtn;
    @FXML private Button createbtn;
    @FXML private ListView<String> creationlist;
    @FXML private MediaView previewbox;
    @FXML private CheckBox autoplaymute;

    private AutoPlayer autoplayer;

    @FXML
    protected void initialize() {
        populateCreations();
        // add our autoplayer as a listener to selection changes
        autoplayer = new AutoPlayer(previewbox, _workingDir);
        creationlist.getSelectionModel().selectedItemProperty().addListener(autoplayer);
    }

    @FXML
    protected void playPressed() {
        System.out.println("Someone pressed play!");
    }

    @FXML
    protected void muteToggled() {
        autoplayer.setMute(autoplaymute.isSelected());
    }

    private void populateCreations() { // populates the list of creations
        File file = new File(_workingDir);
        System.out.println("Working in: " + file.getAbsolutePath());
        if (!file.isDirectory()) {
            System.out.println("Directory doesn't exist, creating it!");
            if (!file.mkdir()) { // make the directory
                DialogHandler.displayErrorBox("MathsAid was unable to create the creations directory! This may mean it doesn't have permission to write to the current working directory");
            }
        }
        String[] creations = file.list();
        ObservableList<String> items = FXCollections.observableArrayList(creations);
        creationlist.setItems(items);
    }

}
