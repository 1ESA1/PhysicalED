package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.ClassSection;
import com.PhysicalED.model.SpecialtyEntity;
import com.PhysicalED.model.SportCategory;
import com.PhysicalED.repo.ClassSectionRepository;
import com.PhysicalED.repo.SpecialtyRepository;
import com.PhysicalED.repo.SportCategoryRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.Date;

/** CRUD minimale per Specialità. */
public class SpecialtyController {
    private static final String SCALE_VIEW = "/com/PhysicalED/gui/GradingScaleView.fxml";
    private static final String SPORT_VIEW = "/com/PhysicalED/gui/SportCategoryView.fxml";

    @FXML private ComboBox<ClassSection> comboClasse;
    @FXML private ComboBox<SportCategory> comboSport;
    @FXML private DatePicker dpData;
    @FXML private TextField txtDescrizione;

    @FXML private TableView<SpecialtyEntity> tblSpecialita;
    @FXML private TableColumn<SpecialtyEntity, String> colDescrizione;
    @FXML private TableColumn<SpecialtyEntity, String> colClasse;
    @FXML private TableColumn<SpecialtyEntity, String> colSport;
    @FXML private TableColumn<SpecialtyEntity, String> colData;
    @FXML private Label lblEsito;

    private final SpecialtyRepository specialtyRepo = new SpecialtyRepository(AppContext.em());
    private final ClassSectionRepository classRepo = AppContext.classSectionRepository();
    private final SportCategoryRepository sportRepo = new SportCategoryRepository(AppContext.em());

    private SportCategory sportCategoryFilter;

    /**
     * Drill-down: imposta lo sport e mostra solo le specialità di quello sport.
     * Se chiamato, la combo sport viene pre-selezionata e disabilitata.
     */
    public void setSportCategoryFilter(SportCategory sportCategory) {
        this.sportCategoryFilter = sportCategory;

        if (comboSport != null) {
            // Se initialize non ha ancora popolato, initialize() lo farà.
            try {
                if (comboSport.getItems() == null || comboSport.getItems().isEmpty()) {
                    comboSport.setItems(FXCollections.observableArrayList(sportRepo.findAll()));
                }
            } catch (Exception ignored) {
            }
            comboSport.setValue(sportCategory);
            comboSport.setDisable(true);
        }

        reload();
        if (lblEsito != null && sportCategory != null) {
            lblEsito.setText("Filtro sport: " + sportCategory.getDescription());
            lblEsito.setStyle("-fx-text-fill: #546E7A;");
        }
    }

    @FXML
    public void initialize() {
        if (colDescrizione != null) {
            colDescrizione.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        }
        if (colClasse != null) {
            colClasse.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getClassSection() != null ? c.getValue().getClassSection().getName() : ""));
        }
        if (colSport != null) {
            colSport.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getSportCategory() != null ? c.getValue().getSportCategory().getDescription() : ""));
        }
        if (colData != null) {
            colData.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getTestDate() != null ? c.getValue().getTestDate().toString() : ""));
        }

        // Doppio click su specialità => apre fasce per quella specialità
        if (tblSpecialita != null) {
            tblSpecialita.setRowFactory(tv -> {
                TableRow<SpecialtyEntity> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        openScalesFor(row.getItem());
                    }
                });
                return row;
            });
        }

        if (comboClasse != null) {
            comboClasse.setItems(FXCollections.observableArrayList(classRepo.findAll()));
            comboClasse.setDisable(comboClasse.getItems().isEmpty());
        }
        if (comboSport != null) {
            comboSport.setItems(FXCollections.observableArrayList(sportRepo.findAll()));
            // Se abbiamo un filtro imposto prima di initialize, applicalo.
            if (sportCategoryFilter != null) {
                comboSport.setValue(sportCategoryFilter);
                comboSport.setDisable(true);
            } else {
                comboSport.setDisable(comboSport.getItems().isEmpty());
            }
        }

        if (comboClasse != null && comboClasse.getItems().isEmpty()) {
            showError("Nessuna classe presente. Crea prima una Classe.");
        } else if (comboSport != null && comboSport.getItems().isEmpty()) {
            showError("Nessuna categoria sport presente. Crea prima uno Sport.");
        }

        reload();
    }

    @FXML
    private void handleAggiungi() {
        if (comboClasse == null || comboSport == null || txtDescrizione == null) return;

        ClassSection classe = comboClasse.getValue();
        SportCategory sport = comboSport.getValue();
        String descr = txtDescrizione.getText().trim();

        if (classe == null || sport == null || descr.isEmpty()) {
            showError("Compila classe, sport e descrizione.");
            return;
        }

        Date data = null;
        if (dpData != null && dpData.getValue() != null) {
            data = Date.valueOf(dpData.getValue());
        }

        specialtyRepo.save(new SpecialtyEntity(sport, classe, data, descr));
        txtDescrizione.clear();
        if (dpData != null) dpData.setValue(null);
        showOk("Specialità aggiunta.");
        reload();
    }

    @FXML
    private void handleRimuovi() {
        if (tblSpecialita == null) return;
        SpecialtyEntity sel = tblSpecialita.getSelectionModel().getSelectedItem();
        if (sel != null) {
            specialtyRepo.delete(sel.getId());
            showOk("Specialità rimossa.");
            reload();
        }
    }

    @FXML
    private void handleBackToSport() {
        try {
            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            Parent root = ViewLoader.load(SPORT_VIEW);
            mainPanel.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Errore navigazione: " + e.getMessage());
        }
    }

    private void openScalesFor(SpecialtyEntity specialty) {
        if (specialty == null || specialty.getId() == null) {
            return;
        }

        try {
            ViewLoader.LoadedView<GradingScaleController> loaded = ViewLoader.loadWithController(SCALE_VIEW);
            loaded.controller().setSpecialtyFilter(specialty);

            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            Parent root = loaded.root();
            mainPanel.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Errore apertura fasce: " + e.getMessage());
        }
    }

    private StackPane findMainPanel() {
        if (tblSpecialita == null || tblSpecialita.getScene() == null) {
            return null;
        }
        javafx.scene.Node n = tblSpecialita.getScene().lookup("#mainPanel");
        if (n instanceof StackPane sp) {
            return sp;
        }
        return null;
    }

    private void reload() {
        if (tblSpecialita == null) return;

        if (sportCategoryFilter != null && sportCategoryFilter.getId() != null) {
            tblSpecialita.setItems(FXCollections.observableArrayList(
                    specialtyRepo.findBySportCategoryId(sportCategoryFilter.getId())));
        } else {
            tblSpecialita.setItems(FXCollections.observableArrayList(specialtyRepo.findAll()));
        }
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

