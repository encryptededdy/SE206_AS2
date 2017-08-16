package zhang.nz.mathsAid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainController {

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
        autoplayer = new AutoPlayer(previewbox, playbtn, deletebtn);
        creationlist.getSelectionModel().selectedItemProperty().addListener(autoplayer);
    }

    // Main Scene

    @FXML
    protected void playPressed() throws IOException {
        if (previewbox.getMediaPlayer() != null) {
            previewbox.getMediaPlayer().stop(); // stop the preview player
        }
        Scene scene = playbtn.getScene();
        FXMLLoader loader = new FXMLLoader(Main.bigPlayerLayout);
        Parent root = loader.load();
        scene.setRoot(root);
        loader.<BigPlayerController>getController().setVideo(creationlist.getSelectionModel().getSelectedItem()); // pass the video that was selected
    }

    @FXML
    protected void createPressed() throws IOException {
        if (previewbox.getMediaPlayer() != null) {
            previewbox.getMediaPlayer().stop(); // stop the preview player
        }
        Scene scene = createbtn.getScene();
        FXMLLoader loader = new FXMLLoader(Main.createLayout);
        Parent root = loader.load();
        scene.setRoot(root);
    }

    @FXML
    protected void muteToggled() {
        autoplayer.setMute(autoplaymute.isSelected());
    }

    private void populateCreations() { // populates the list of creations
        File file = new File(Main.workingDir);
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

    @FXML
    protected void deletePressed() { // delete a file
        String file = creationlist.getSelectionModel().getSelectedItem();
        if (DialogHandler.confirmationBox("Are you sure you want to delete the creation \"" + file + "\"?")) { // ask for confirmation of deletion
            File creationVideo = Paths.get(Main.workingDir, file, "creation.mp4").toFile();
            if (creationVideo.exists()) {
                if (!creationVideo.delete()) {
                    DialogHandler.displayErrorBox("File deletion failed - check write permissions");
                }
            }
            File creationDir = Paths.get(Main.workingDir, file).toFile();
            if (!creationDir.delete()) {
                DialogHandler.displayErrorBox("File deletion failed - check write permissions");
            }
            populateCreations(); // reload creation list
        }
    }

}
