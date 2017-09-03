package zhang.nz.mathsAid;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateController {
    @FXML private Button cancelcreate;
    @FXML private TextField nameField;
    @FXML private Label nameInfo;
    @FXML private Button nameOK;
    @FXML private Button recordAudiobtn;
    @FXML private Label recordAudioLabel;
    @FXML private Button previewAudiobtn;
    @FXML private Button savecreate;
    @FXML private Label creationIsBeingSaved;
    @FXML private ProgressBar creationSaving;

    private List<String> _existingcreations;
    private MediaPlayer _previewPlayer;
    private AudioRecordingWorker _recordingWorker;
    private CreationStitchWorker _stitchWorker;
    private boolean _saved = false;

    @FXML
    protected void initialize() {
        File file = new File(Main.workingDir);
        _existingcreations = Arrays.asList(file.list());
        BooleanBinding validationBinding = Bindings.createBooleanBinding(this::creationNameChecker, nameField.textProperty());
        nameOK.disableProperty().bind(validationBinding);
    }

    @FXML
    protected void createOKpressed() { // proceed to recording audio mode
        if (_existingcreations.contains(nameField.getText())) {
            if (DialogHandler.confirmationBox("Are you sure you want to overwrite "+nameField.getText())) {
                File creationOut = Paths.get(Main.workingDir, nameField.getText(), "creation.mp4").toFile();
                if (!creationOut.delete()) {
                    DialogHandler.displayErrorBox("Failed to overwrite");
                }
            } else {
                return;
            }
        } else {
            // Make the directory
            if (!Paths.get(Main.workingDir, nameField.getText()).toFile().mkdir()) {
                DialogHandler.displayErrorBox("MathsAid was unable to create the directory! This may mean it doesn't have permission to write to the current working directory");
            }
        }
        _saved = true;
        nameInfo.setText("Name saved");
        nameField.setDisable(true);
        nameOK.disableProperty().unbind();
        nameOK.setDisable(true);
        recordAudiobtn.setDisable(false);
    }

    private boolean creationNameChecker () {
        String name = nameField.getText();
        try {
            Paths.get(Main.workingDir, name); // try to generate a path for the creation name to see if it's valid
        } catch (Exception e){
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Invalid name");
            nameOK.setText("OK");
            return true;
        }
        Pattern p = Pattern.compile("[^a-zA-Z-_.\\d\\s:]");
        Matcher m = p.matcher(name);

        if (name.isEmpty()) {
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Please enter a name");
            nameOK.setText("OK");
            return true;
        } else if (_existingcreations.contains(name)) {
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Creation already exists - will overwrite");
            nameOK.setText("Overwrite");
            return false;
        } else if (m.find()) {
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Invalid name");
            nameOK.setText("OK");
            return true;
        } else {
            nameInfo.setTextFill(Color.BLACK);
            nameInfo.setText("Press OK to continue");
            nameOK.setText("OK");
            return false;
        }

    }

    @FXML
    protected void cancelledCreate() throws IOException {
        // Clean up after ourselves if any files were created
        File creationA = Paths.get(Main.workingDir, nameField.getText(), "audio.wav").toFile();
        File creationOut = Paths.get(Main.workingDir, nameField.getText(), "creation.mp4").toFile();
        File creationDir = Paths.get(Main.workingDir, nameField.getText()).toFile();

        if (_previewPlayer != null) {
            _previewPlayer.stop();
            _previewPlayer.dispose(); // release file locks
        }

        if (_recordingWorker != null && _recordingWorker.isRunning()) {
            _recordingWorker.cancel(); // cancel the worker if it's running
        } else if (_stitchWorker != null && _stitchWorker.isRunning()) {
            _stitchWorker.cancel(); // cancel the worker if it's running
        }
        if (_saved) {
            if (creationA.exists()) {
                creationA.delete();
            }
            if (creationOut.exists()) {
                creationOut.delete();
            }
            try {
                Thread.sleep(100); // wait for processes to die (bad solution, but only thing that seems to work)
            } catch (InterruptedException e) {
                DialogHandler.displayErrorBox("Failed to cancel");
            }
            if (creationDir.exists()) {
                creationDir.delete();
            }
        }

        Scene scene = cancelcreate.getScene();
        FXMLLoader loader = new FXMLLoader(Main.mainLayout);
        Parent root = loader.load();
        scene.setRoot(root);
    }

    @FXML
    protected void recordPressed() {
        cancelcreate.setDisable(true);
        recordAudiobtn.setDisable(true);
        _recordingWorker = new AudioRecordingWorker(nameField.getText());
        Thread recordingThread = new Thread(_recordingWorker);
        _recordingWorker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                t -> {
                    cancelcreate.setDisable(false);
                    recordAudiobtn.setDisable(false);
                    Boolean result = _recordingWorker.getValue();
                    if (result) {
                        recordAudioLabel.setText("Audio recorded. Preview or record again");
                        previewAudiobtn.setDisable(false);
                        savecreate.setDisable(false);
                    } else {
                        recordAudioLabel.setText("ffmpeg encountered an error");
                    }
                });
        recordingThread.start();
        recordAudioLabel.setText("Recording...");
    }

    @FXML
    protected void previewPressed() {
        if (_previewPlayer != null) {
            _previewPlayer.seek(new Duration(0));
            _previewPlayer.play();
        } else {
            File creation = Paths.get(Main.workingDir, nameField.getText(), "audio.wav").toFile();
            Media media = new Media(creation.toURI().toString());
            _previewPlayer = new MediaPlayer(media);
            _previewPlayer.play();
        }
    }

    @FXML
    protected void savePressed() {
        // save the creation
        savecreate.setDisable(true);
        recordAudiobtn.setDisable(true);
        creationIsBeingSaved.setVisible(true);
        creationSaving.setVisible(true);
        _stitchWorker = new CreationStitchWorker(nameField.getText());
        Thread stitchingThread = new Thread(_stitchWorker);
        _stitchWorker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                t -> {
                    Boolean result = _stitchWorker.getValue();
                    if (_previewPlayer != null) {
                        _previewPlayer.stop();
                        _previewPlayer.dispose(); // release file locks
                    }
                    if (!result) {
                        creationIsBeingSaved.setText("The creation was unable to be created");
                    } else {
                        Scene scene = savecreate.getScene();
                        FXMLLoader loader = new FXMLLoader(Main.mainLayout);
                        try {
                            Parent root = loader.load();
                            scene.setRoot(root);
                        } catch (IOException e) {
                            DialogHandler.displayErrorBox("Critical Error: Unable to switch views");
                        }
                    }
                });
        stitchingThread.start();
    }
}
