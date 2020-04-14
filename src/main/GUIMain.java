package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(this.getClass().getResource("../view/MainView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("AirDefender");
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't open main app view", ex);
            return;
        }
    }
}
