package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.PLA.PLA;
import sample.utils.FileManager;
import sample.utils.TableController;
import sample.utils.TableData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    private ArrayList<TableData> res;
    @FXML
    private Button analyze;
    @FXML
    private TextArea text;
    @FXML
    private MenuBar menuBar;

    private PLA analyzer;

    @FXML
    private void onAnalyze(ActionEvent event) {
        analyzer = new PLA(text.getText());
        res = analyzer.analyze();
        ObservableList<TableData> data = FXCollections.observableArrayList(res);
        TableController.lexicData = res;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("screens/table.fxml"));
            Stage stage = new Stage();
            stage.setTitle("PLA - Pascal Lexic Analyser - Analyzed File Table");
            stage.setScene(new Scene(root, 790, 590));
            stage.getScene().getStylesheets().add("sample/res/layout.css");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("res/icon.png")));
            TableView table = (TableView) stage.getScene().lookup("#table");

            TableColumn tokenColumn = new TableColumn("Token");
            tokenColumn.setMinWidth(10.0);
            tokenColumn.setPrefWidth(300.0);
            tokenColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("token"));

            TableColumn classificationColumn = new TableColumn("Classification");
            classificationColumn.setMinWidth(10.0);
            classificationColumn.setPrefWidth(358.0);
            classificationColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("classification"));

            TableColumn lineColumn = new TableColumn("Line");
            lineColumn.setMinWidth(10.0);
            lineColumn.setPrefWidth(114.0);
            lineColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("line"));

            table.setItems(data);
            table.getColumns().addAll(tokenColumn, classificationColumn, lineColumn);

            stage.show();
        } catch (IOException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(menuBar.getScene().getWindow());
            alert.setHeaderText("PLA - Pascal Lexic Analyser");
            alert.setContentText("Could not create the lexic table.");
            alert.show();
        }
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
