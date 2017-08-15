package zhang.nz.mathsAid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    static URL mainLayout;
    static URL bigPlayerLayout;
    static final String workingDir = "creations";

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainLayout = getClass().getResource("mainscreen.fxml");
        bigPlayerLayout = getClass().getResource("playerscreen.fxml");
        Parent root = FXMLLoader.load(mainLayout); // load FXML layout
        primaryStage.setTitle("Maths Authoring Aid");
        primaryStage.setResizable(false); // please don't resize
        primaryStage.sizeToScene(); // for some reason setresizable expands the window???
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
