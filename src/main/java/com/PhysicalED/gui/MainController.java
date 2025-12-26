package com.PhysicalED.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML private Button btnYears;
    @FXML private Button btnClasses;
    @FXML private Button btnStudents;
    @FXML private Button btnSport;
    @FXML private Button btnScore;
    @FXML private StackPane mainPanel;
    @FXML private Label lblWelcome;

    @FXML
    public void initialize() {
        btnYears.setOnAction(e -> showMessage("Hai selezionato: Anni scolastici"));
        btnClasses.setOnAction(e -> showMessage("Hai selezionato: Classi"));
        btnStudents.setOnAction(e -> showMessage("Hai selezionato: Studenti"));
        btnStudents.setOnAction(e -> loadView("/com/PhysicalED/gui/StudentiView.fxml"));
        btnSport.setOnAction(e -> showMessage("Hai selezionato: Sport"));
        btnScore.setOnAction(e -> showMessage("Hai selezionato: Score"));
    }

    private void showMessage(String text) {
        // Cambia il messaggio visualizzato al centro (sovrascrive il label solo)
        lblWelcome.setText(text);
    }

    private void loadView(String fxmlFile) {
        // Carica e mostra la vista FXML specificata
        try {
            mainPanel.getChildren().clear();
            mainPanel.getChildren().add(
                    FXMLLoader.load(getClass().getResource(fxmlFile))
            );
        } catch (Exception e) {
            lblWelcome.setText("Errore nel caricamento della vista: " + fxmlFile);
            e.printStackTrace();
        }
    }
}