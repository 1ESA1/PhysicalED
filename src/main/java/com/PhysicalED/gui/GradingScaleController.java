package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.Gender;
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.model.SpecialtyEntity;
import com.PhysicalED.repo.GradingScaleRepository;
import com.PhysicalED.repo.SpecialtyRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/** CRUD minimale per GradingScale (fasce voto). */
public class GradingScaleController {
    @FXML private ComboBox<SpecialtyEntity> comboTest;
    @FXML private ComboBox<Gender> comboGender;
    @FXML private TextField txtMin;
    @FXML private TextField txtMax;
    @FXML private TextField txtVoto;

    @FXML private TableView<GradingScale> tblScale;
    @FXML private TableColumn<GradingScale, String> colTest;
    @FXML private TableColumn<GradingScale, Gender> colGender;
    @FXML private TableColumn<GradingScale, String> colRange;
    @FXML private TableColumn<GradingScale, Integer> colVoto;
    @FXML private Label lblEsito;

    private final SpecialtyRepository specialtyRepo = new SpecialtyRepository(AppContext.em());
    private final GradingScaleRepository scaleRepo = new GradingScaleRepository(AppContext.em());

    private SpecialtyEntity specialtyFilter;

    private static final String SPECIALTY_VIEW = "/com/PhysicalED/gui/SpecialtyView.fxml";

    /**
     * Drill-down: imposta la specialità e mostra solo le fasce relative.
     * Se chiamato, la combo specialità viene pre-selezionata e disabilitata.
     */
    public void setSpecialtyFilter(SpecialtyEntity specialty) {
        this.specialtyFilter = specialty;

        if (comboTest != null) {
            try {
                if (comboTest.getItems() == null || comboTest.getItems().isEmpty()) {
                    comboTest.setItems(FXCollections.observableArrayList(specialtyRepo.findAll()));
                }
            } catch (Exception ignored) {
            }
            comboTest.setValue(specialty);
            comboTest.setDisable(true);
        }

        if (lblEsito != null && specialty != null) {
            lblEsito.setText("Fasce - " + specialty.getDescription());
            lblEsito.setStyle("-fx-text-fill: #546E7A;");
        }

        reload();
    }

    @FXML
    public void initialize() {
        if (colTest != null) {
            colTest.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getSpecialty() != null ? c.getValue().getSpecialty().getDescription() : ""));
        }
        if (colGender != null) {
            colGender.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getGender()));
        }
        if (colRange != null) {
            colRange.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getMinValue() + " - " + c.getValue().getMaxValue()));
        }
        if (colVoto != null) {
            colVoto.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getVoto()));
        }

        if (comboTest != null) {
            comboTest.setItems(FXCollections.observableArrayList(specialtyRepo.findAll()));
            if (specialtyFilter != null) {
                comboTest.setValue(specialtyFilter);
                comboTest.setDisable(true);
            } else {
                comboTest.setDisable(comboTest.getItems().isEmpty());
                if (comboTest.getItems().isEmpty()) {
                    showError("Nessuna specialità presente. Crea prima una Specialità.");
                }
            }
        }
        if (comboGender != null) {
            comboGender.setItems(FXCollections.observableArrayList(Gender.values()));
        }

        reload();
    }

    @FXML
    private void handleAggiungi() {
        if (comboTest == null || comboGender == null || txtMin == null || txtMax == null || txtVoto == null) return;
        SpecialtyEntity specialty = comboTest.getValue();
        Gender gender = comboGender.getValue();
        if (specialty == null || gender == null) {
            showError("Seleziona specialità e genere.");
            return;
        }

        double min;
        double max;
        int voto;
        try {
            min = Double.parseDouble(txtMin.getText().trim());
            max = Double.parseDouble(txtMax.getText().trim());
            voto = Integer.parseInt(txtVoto.getText().trim());
        } catch (Exception ex) {
            showError("Min/Max/Voto non validi.");
            return;
        }
        if (max <= min) {
            showError("Max deve essere > Min.");
            return;
        }

        scaleRepo.save(new GradingScale(specialty, gender, min, max, voto));
        txtMin.clear();
        txtMax.clear();
        txtVoto.clear();
        showOk("Fascia aggiunta.");
        reload();
    }

    @FXML
    private void handleRimuovi() {
        if (tblScale == null) return;
        GradingScale sel = tblScale.getSelectionModel().getSelectedItem();
        if (sel != null) {
            scaleRepo.delete(sel.getId());
            showOk("Fascia rimossa.");
            reload();
        }
    }

    @FXML
    private void handleBackToSpecialty() {
        try {
            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            ViewLoader.LoadedView<SpecialtyController> loaded = ViewLoader.loadWithController(SPECIALTY_VIEW);
            // Se arriviamo da drill-down e abbiamo un filtro sport, non lo conosciamo qui.
            // Però possiamo almeno rientrare nella schermata generalista, o mantenere la selezione corrente.
            mainPanel.getChildren().setAll(loaded.root());
        } catch (Exception e) {
            showError("Errore navigazione: " + e.getMessage());
        }
    }

    private StackPane findMainPanel() {
        if (tblScale == null || tblScale.getScene() == null) {
            return null;
        }
        javafx.scene.Node n = tblScale.getScene().lookup("#mainPanel");
        if (n instanceof StackPane sp) {
            return sp;
        }
        return null;
    }

    private void reload() {
        if (tblScale == null) return;

        if (specialtyFilter != null && specialtyFilter.getId() != null) {
            tblScale.setItems(FXCollections.observableArrayList(scaleRepo.findBySpecialtyId(specialtyFilter.getId())));
        } else {
            tblScale.setItems(FXCollections.observableArrayList(scaleRepo.findAll()));
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
