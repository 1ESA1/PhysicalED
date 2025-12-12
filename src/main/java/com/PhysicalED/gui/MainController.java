package com.PhysicalED.gui;

import com.PhysicalED.model.SportingDiscipline;
import javafx.fxml.FXML;
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
        btnSport.setOnAction(e -> showMessage("Hai selezionato: Sport"));
        btnScore.setOnAction(e -> showMessage("Hai selezionato: Score"));
    }

    private void showMessage(String text) {
        // Cambia il messaggio visualizzato al centro (sovrascrive il label solo)
        lblWelcome.setText(text);
    }
}