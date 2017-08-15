package zhang.nz.mathsAid;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

public class BigPlayerController {

    // Creating objects from FXML
    @FXML private MediaView bigmediaview;
    @FXML private Button bigplayerreplay;
    @FXML private Button bigplayerback;

    private String _video;

    private MediaPlayer _mediaplayer;

    // BigPlayer Scene

    @FXML
    protected void bigPlayerBackPressed() throws IOException{
        if (_mediaplayer != null) {
            _mediaplayer.stop(); // stop the video
        }
        Scene scene = bigplayerback.getScene();
        Parent root = FXMLLoader.load(Main.mainLayout);
        scene.setRoot(root);
    }

    private void backToMain() throws IOException {
        Scene scene = bigplayerback.getScene();
        Parent root = FXMLLoader.load(Main.mainLayout);
        scene.setRoot(root);
    }

    protected void setVideo(String video){
        _video = video;
        playVideo();
    }

    @FXML
    protected void bigPlayerReplayPressed() {
        _mediaplayer.seek(new javafx.util.Duration(0));
        _mediaplayer.play();
    }

    private void playVideo() {
        File creation = Paths.get(Main.workingDir, _video, "creation.mp4").toFile();
        if (!creation.isFile()) {
            DialogHandler.displayErrorBox("Corrupted or Invalid creation; cannot be played!");
            return;
        }
        Media media = new Media(creation.toURI().toString());
        _mediaplayer = new MediaPlayer(media);
        _mediaplayer.setAutoPlay(true);
        bigmediaview.setMediaPlayer(_mediaplayer);
    }

}
