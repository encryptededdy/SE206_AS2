package zhang.nz.mathsAid;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.nio.file.Paths;

public class AutoPlayer implements ChangeListener<String> {
    private MediaView _previewbox;
    private String _workingDir;
    private boolean _mute = true;

    public AutoPlayer (MediaView previewbox, String workingDir) {
        _previewbox = previewbox;
        _workingDir = workingDir;
    }

    public void setMute (boolean mute) {
        _mute = mute;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        File creation = Paths.get(_workingDir, newValue, "creation.mp4").toFile();
        if (!creation.isFile()) {
            DialogHandler.displayErrorBox("Corrupted or Invalid creation; cannot be played!");
            return;
        }
        Media media = new Media(creation.toURI().toString());
        System.out.println("Playing: " + creation);
        MediaPlayer mediaplayer = new MediaPlayer(media);
        mediaplayer.setAutoPlay(true);
        mediaplayer.setMute(_mute);
        _previewbox.setMediaPlayer(mediaplayer);
    }
}
