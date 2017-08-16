package zhang.nz.mathsAid;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class AudioRecordingWorker extends Task<Boolean> {
    private String _creationName;
    public AudioRecordingWorker(String name) {
        _creationName = name;
    }

    @Override
    protected Boolean call() {
        Process pb;
        File creation = Paths.get(Main.workingDir, _creationName, "audio.wav").toFile();
        try {
            pb = new ProcessBuilder("bash", "-c","ffmpeg -y -nostats -loglevel warning -f pulse -i default -t 3 \"" + creation.getAbsolutePath() + "\"").start();
        } catch (IOException ex) {
            Platform.runLater(() -> DialogHandler.displayErrorBox("Error running FFMPEG: "+ex.getMessage()));
            return false;
        }
        Scanner outputScanner = new Scanner(pb.getInputStream());
        while (outputScanner.hasNext()) {
        }
        try {
            int exitValue = pb.waitFor();
            if (exitValue != 0) {
                Scanner errorScanner = new Scanner(pb.getErrorStream());
                if (errorScanner.hasNext()) {
                    String error = errorScanner.useDelimiter("\\Z").next();
                    Platform.runLater(() -> DialogHandler.displayErrorBox("FFMPEG encountered an error: "+error));
                    return false;
                }
            }
        } catch (InterruptedException ex) {
            creation.delete();
            Paths.get(Main.workingDir, _creationName).toFile().delete();
            return false;
        }
        if (isCancelled()) { // if we've been cancelled, clean up.
            creation.delete();
            Paths.get(Main.workingDir, _creationName).toFile().delete();
        }
        return true;
    }

}
