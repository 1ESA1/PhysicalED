package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.SchoolYear;
import com.PhysicalED.repo.SchoolYearRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** CRUD minimale per SchoolYear. */
public class SchoolYearController {
    @FXML private TextField txtDescrizione;
    @FXML private TableView<SchoolYear> tblAnni;
    @FXML private TableColumn<SchoolYear, String> colDescrizione;
    @FXML private Label lblEsito;

    private SchoolYearRepository repo;

    @FXML
    public void initialize() {
        try {
            repo = AppContext.schoolYearRepository();
        } catch (Exception e) {
            showError("Errore DB: " + e.getMessage());
            return;
        }

        if (colDescrizione != null) {
            colDescrizione.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        }
        reload();
    }

    @FXML
    private void handleAggiungi() {
        if (repo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (txtDescrizione == null) return;
        String d = txtDescrizione.getText().trim();
        if (d.isEmpty()) {
            showError("Inserisci descrizione anno (es. 2025/2026)");
            return;
        }
        repo.save(new SchoolYear(d));
        txtDescrizione.clear();
        showOk("Anno aggiunto.");
        reload();
    }

    @FXML
    private void handleRimuovi() {
        if (repo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (tblAnni == null) return;
        SchoolYear sel = tblAnni.getSelectionModel().getSelectedItem();
        if (sel != null) {
            repo.delete(sel.getId());
            showOk("Anno rimosso.");
            reload();
        }
    }

    private void reload() {
        if (repo == null || tblAnni == null) return;
        tblAnni.setItems(FXCollections.observableArrayList(repo.findAll()));
    }

    private void showOk(String msg) {
        if (lblEsito == null) return;
        lblEsito.setText(msg);
        lblEsito.setStyle("-fx-text-fill: #00796B;");
    }

    private void showError(String msg) {
        if (lblEsito == null) return;
        lblEsito.setText(msg);
        lblEsito.setStyle("-fx-text-fill: #C62828;");
    }
}
