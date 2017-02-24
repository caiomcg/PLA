package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import sample.Utils.FileManager;

import java.io.File;
import java.util.ArrayList;

public class Controller {
    @FXML
    private Button analyze;
    @FXML
    private TextArea text;
    @FXML
    private MenuBar menuBar;

    private ArrayList<String> lines;

    public Controller() {
        lines = new ArrayList<>();
    }

    @FXML
    private void onAnalyze(ActionEvent event) {
        System.out.println("TAPPED");
        System.out.println(text.getText());

        //for (String line : text.getText().split("\\n")) lines.add(line);
    }

    @FXML
    private void onTextSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter( "Pascal files (*pas)", "*.pas"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null) {
            FileManager.saveFile(text.getText(), file);
        }
    }

    @FXML
    private void onTextOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pascal files (&pas)", "*.pas"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String content = FileManager.openFile(file);
            if (content != null) {
                text.setText(content);
            }
        }
    }
}
