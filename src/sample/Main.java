package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("screens/editor.fxml"));
        primaryStage.setTitle("PLA - Pascal Lexic Analyser");
        primaryStage.setScene(new Scene(root, 790, 590));
        primaryStage.getScene().getStylesheets().add("sample/res/layout.css");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("res/icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
