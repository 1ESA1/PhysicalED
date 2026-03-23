package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.SportCategory;
import com.PhysicalED.repo.SportCategoryRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/** CRUD minimale per SportCategory. */
public class SportCategoryController {
    private static final String SPECIALTY_VIEW = "/com/PhysicalED/gui/SpecialtyView.fxml";

    @FXML private TextField txtDescrizione;
    @FXML private TableView<SportCategory> tblSport;
    @FXML private TableColumn<SportCategory, String> colDescrizione;
    @FXML private Label lblEsito;

    private final SportCategoryRepository repo = new SportCategoryRepository(AppContext.em());

    @FXML
    public void initialize() {
        if (colDescrizione != null) {
            colDescrizione.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        }

        // Doppio click su riga => apre specialità dello sport
        if (tblSport != null) {
            tblSport.setRowFactory(tv -> {
                TableRow<SportCategory> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        openSpecialtiesFor(row.getItem());
                    }
                });
                return row;
            });
        }

        reload();
    }

    @FXML
    private void handleAggiungi() {
        if (txtDescrizione == null) return;
        String d = txtDescrizione.getText().trim();
        if (d.isEmpty()) {
            showError("Inserisci descrizione sport (es. Atletica)");
            return;
        }
        repo.save(new SportCategory(d));
        txtDescrizione.clear();
        showOk("Sport aggiunto.");
        reload();
    }

    @FXML
    private void handleRimuovi() {
        if (tblSport == null) return;
        SportCategory sel = tblSport.getSelectionModel().getSelectedItem();
        if (sel != null) {
            repo.delete(sel.getId());
            showOk("Sport rimosso.");
            reload();
        }
    }

    private void openSpecialtiesFor(SportCategory sportCategory) {
        if (sportCategory == null || sportCategory.getId() == null) {
            return;
        }

        try {
            ViewLoader.LoadedView<SpecialtyController> loaded = ViewLoader.loadWithController(SPECIALTY_VIEW);
            loaded.controller().setSportCategoryFilter(sportCategory);

            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            Parent root = loaded.root();
            mainPanel.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Errore apertura specialità: " + e.getMessage());
        }
    }

    private StackPane findMainPanel() {
        if (tblSport == null || tblSport.getScene() == null) {
            return null;
        }
        javafx.scene.Node n = tblSport.getScene().lookup("#mainPanel");
        if (n instanceof StackPane sp) {
            return sp;
        }
        return null;
    }

    private void reload() {
        if (tblSport == null) return;
        tblSport.setItems(FXCollections.observableArrayList(repo.findAll()));
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
