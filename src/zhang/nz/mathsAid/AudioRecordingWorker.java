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
        System.out.println(creation.toString());
        try {
            pb = new ProcessBuilder("ffmpeg -nostats -loglevel panic -f pulse -i default -t 3 \"" + creation.getPath() + "\"").start();
        } catch (IOException ex) {
            Platform.runLater(() -> DialogHandler.displayErrorBox("Error running FFMPEG: "+ex.getMessage()));
            return false;
        }
        //Scanner outputScanner = new Scanner(pb.getInputStream());
        //while (outputScanner.hasNext()) {
            // we don't actually want the output from ffmpeg
        //}
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
            Platform.runLater(() -> DialogHandler.displayErrorBox("Error running FFMPEG: "+ex.getMessage()));
            return false;
        }
        return true;
    }

}
