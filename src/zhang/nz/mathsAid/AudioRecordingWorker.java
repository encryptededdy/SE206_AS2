package zhang.nz.mathsAid;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class AudioRecordingWorker extends Task<String> {
    private String _creationName;
    public AudioRecordingWorker(String name) {
        _creationName = name;
    }

    @Override
    protected String call() {
        Process pb;
        File creation = Paths.get(Main.workingDir, _creationName, "audio.wav").toFile();
        try {
            pb = new ProcessBuilder("ffmpeg -nostats -loglevel panic -f pulse -i default -t 3 "+creation.getAbsolutePath()).start();
        } catch (IOException ex) {
            DialogHandler.displayErrorBox(ex.getMessage());
            return "FFMPEG encountered an error";
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
                    DialogHandler.displayErrorBox("FFMPEG encounted an error: "+error);
                    return "FFMPEG encountered an error";
                }
            }
        } catch (InterruptedException ex) {
            DialogHandler.displayErrorBox(ex.getMessage());
        }
        return "Recorded";
    }
}
