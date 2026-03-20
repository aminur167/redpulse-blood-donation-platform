package com.example.projectredpulsenew;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader dasboard_fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Newsfeed.fxml"));
        Scene dashboard_scene = new Scene(dasboard_fxmlLoader.load(), 1366, 768);
        stage.setTitle("Newsfeed");
        stage.setScene(dashboard_scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application
    }
}