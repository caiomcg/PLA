package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import sample.PLA.PLA;
import sample.Utils.FileManager;

import java.io.File;
import java.util.Optional;

public class Controller {
    @FXML
    private Button analyze;
    @FXML
    private TextArea text;
    @FXML
    private MenuBar menuBar;
    private PLA analyzer;

    public Controller() {
    }

    @FXML
    private void onAnalyze(ActionEvent event) {
        analyzer = new PLA(text.getText());
        analyzer.analyze();
    }

    @FXML
    private void onTextSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter( "Pascal files (*.pas)", "*.pas"));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(file != null) {
            FileManager.saveFile(text.getText(), file);
        }
    }

    @FXML
    private void onTextOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pascal files (*.pas)", "*.pas"));
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            String content = FileManager.openFile(file);
            if (content != null) {
                text.setText(content);
            }
        }
    }

    @FXML
    private void onAboutPress(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.setHeaderText("PLA - Pascal Lexic Analyser");
        alert.setContentText("Developer: Caio Marcelo Campoy Guedes.\n\nFor more information visit: https://github.com/caiomcg/PLA");
        alert.show();
    }

    @FXML
    private void onQuit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(menuBar.getScene().getWindow());
        alert.setHeaderText("You are quitting the application");
        alert.setContentText("Do you want to proceed?");


        Optional<ButtonType> type = alert.showAndWait();

        if (type.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
}
