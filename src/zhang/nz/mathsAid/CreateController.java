package zhang.nz.mathsAid;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateController {
    @FXML private Button cancelcreate;
    @FXML private TextField nameField;
    @FXML private Label nameInfo;
    @FXML private Button nameOK;
    private List<String> _existingcreations;

    @FXML
    protected void initialize() {
        File file = new File(Main.workingDir);
        _existingcreations = Arrays.asList(file.list());
        BooleanBinding validationBinding = Bindings.createBooleanBinding(this::creationNameChecker, nameField.textProperty());
        nameOK.disableProperty().bind(validationBinding);
    }

    private boolean creationNameChecker () {
        String name = nameField.getText();
        try {
            Paths.get(Main.workingDir, name); // try to generate a path for the creation name to see if it's valid
        } catch (Exception e){
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Invalid name");
            return true;
        }
        if (name.isEmpty()) {
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Please enter a name");
            return true;
        } else if (_existingcreations.contains(name)) {
            nameInfo.setTextFill(Color.RED);
            nameInfo.setText("Creation already exists");
            return true;
        } else {
            nameInfo.setTextFill(Color.BLACK);
            nameInfo.setText("Press OK to continue");
            return false;
        }

    }

    @FXML
    protected void cancelledCreate() throws IOException {
        Scene scene = cancelcreate.getScene();
        FXMLLoader loader = new FXMLLoader(Main.mainLayout);
        Parent root = loader.load();
        scene.setRoot(root);
    }
}