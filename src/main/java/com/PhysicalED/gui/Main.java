package com.PhysicalED.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica FXML principale
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/com/PhysicalED/gui/main.fxml"))
        );
        primaryStage.setTitle("Physical Education App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}