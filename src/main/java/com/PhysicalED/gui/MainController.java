package com.PhysicalED.gui;

import com.PhysicalED.config.DatabaseException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {
    private static final String YEARS_VIEW = "/com/PhysicalED/gui/SchoolYearView.fxml";
    private static final String CLASS_VIEW = "/com/PhysicalED/gui/ClassSectionView.fxml";
    private static final String SPORT_VIEW = "/com/PhysicalED/gui/SportCategoryView.fxml";
    private static final String SCORE_VIEW = "/com/PhysicalED/gui/ScoreView.fxml";
    private static final String SUMMARY_VIEW = "/com/PhysicalED/gui/SummaryView.fxml";

    @FXML private javafx.scene.control.Button btnYears;
    @FXML private javafx.scene.control.Button btnClasses;
    @FXML private javafx.scene.control.Button btnSport;
    @FXML private javafx.scene.control.Button btnScores;
    @FXML private javafx.scene.control.Button btnSummary;

    @FXML private StackPane mainPanel;

    @FXML
    public void initialize() {
        if (mainPanel != null) {
            mainPanel.getChildren().setAll(new Label("Seleziona una sezione dal menu a sinistra."));
        }

        if (btnYears != null) btnYears.setOnAction(e -> loadView(YEARS_VIEW));
        if (btnClasses != null) btnClasses.setOnAction(e -> loadView(CLASS_VIEW));
        if (btnSport != null) btnSport.setOnAction(e -> loadView(SPORT_VIEW));
        if (btnScores != null) btnScores.setOnAction(e -> loadView(SCORE_VIEW));
        if (btnSummary != null) btnSummary.setOnAction(e -> loadView(SUMMARY_VIEW));
    }

    private void loadView(String fxmlFile) {
        if (mainPanel == null) return;

        try {
            Parent root = ViewLoader.load(fxmlFile);
            mainPanel.getChildren().setAll(root);
        } catch (DatabaseException db) {
            showAlert("Errore DB", "Inizializzazione database fallita", db.getMessage());
        } catch (Exception e) {
            showAlert("Errore caricamento", "Impossibile caricare la vista", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
