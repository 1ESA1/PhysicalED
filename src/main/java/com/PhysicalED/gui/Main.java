package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.config.DatabaseException;
import com.PhysicalED.config.DbBootstrap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            DbBootstrap.initOrThrow();
        } catch (DatabaseException db) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore DB");
            alert.setHeaderText("Inizializzazione database fallita");
            alert.setContentText(db.getMessage());
            alert.showAndWait();
            throw db;
        }

        Scene scene = new Scene(ViewLoader.load("/com/PhysicalED/gui/main.fxml"));
        primaryStage.setTitle("Physical Education App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Chiude risorse JPA (EntityManager/Factory) quando la finestra viene chiusa.
        AppContext.shutdown();
    }

    public static void main(String[] args) {

        launch(args);
    }
}