package zhang.nz.mathsAid;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class CreationStitchWorker extends Task<Boolean> {
    private String _creationName;
    public CreationStitchWorker(String name) {
        _creationName = name;
    }

    @Override
    protected Boolean call() {
        Process pb;
        File creationA = Paths.get(Main.workingDir, _creationName, "audio.wav").toFile();
        File creationOut = Paths.get(Main.workingDir, _creationName, "creation.mp4").toFile();
        try {
            pb = new ProcessBuilder("bash", "-c","ffmpeg -nostats -loglevel panic -f lavfi -i color=c=orange:s=1280x720:d=3 -i \""+creationA.getAbsolutePath()+"\"-vf \"drawtext=fontfile=/usr/share/fonts/gnu-free/FreeSans.ttf:fontsize=120:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\""+_creationName+"\", drawtext=fontfile=/usr/share/fonts/gnu-free/FreeSans.ttf:fontsize=200:fontcolor=white@0.25:x=(w-text_w)/2:y=(h-text_h)/2:text=\""+_creationName+"\", drawtext=fontfile=/usr/share/fonts/gnu-free/FreeSans.ttf:fontsize=20:fontcolor=blue:x=(w-text_w)-10:y=(h-text_h)-10:text=Powered by maths_aid by Edward Zhang\" -c:a aac -strict experimental  \""+creationOut.getAbsolutePath()+"\"").start();
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
            Platform.runLater(() -> DialogHandler.displayErrorBox("Error running FFMPEG: "+ex.getMessage()));
            return false;
        }
        return true;
    }

}
