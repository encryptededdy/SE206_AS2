package zhang.nz.mathsAid;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.nio.file.Paths;

public class AutoPlayer implements ChangeListener<String> {
    private MediaView _previewbox;
    private boolean _mute = true;
    private Button _playbtn;
    private Button _deletebtn;

    private MediaPlayer _player;

    public AutoPlayer (MediaView previewbox, Button playbtn, Button deletebtn) {
        _previewbox = previewbox;
        _playbtn = playbtn;
        _deletebtn = deletebtn;
    }

    public void setMute (boolean mute) {
        _mute = mute;
    }

    public void stop () {
        if (_player != null) {
            _player.dispose(); // Releases the file lock to work around a JFX bug
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) { // avoid issues after deletions etc.
            _playbtn.setDisable(false);
            _deletebtn.setDisable(false);
            File creation = Paths.get(Main.workingDir, newValue, "creation.mp4").toFile();
            if (!creation.isFile()) {
                _playbtn.setDisable(true);
                DialogHandler.displayErrorBox("Corrupted or Invalid creation; cannot be played!");
                return;
            }
            Media media = new Media(creation.toURI().toString());
            //System.out.println("Playing: " + creation);
            _player = new MediaPlayer(media);
            _player.setAutoPlay(true);
            _player.setMute(_mute);
            _previewbox.setMediaPlayer(_player);
        }
    }
}
