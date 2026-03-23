package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.Score;
import com.PhysicalED.model.SpecialtyEntity;
import com.PhysicalED.model.Student;
import com.PhysicalED.repo.GradingScaleRepository;
import com.PhysicalED.repo.ScoreRepository;
import com.PhysicalED.repo.SpecialtyRepository;
import com.PhysicalED.repo.StudentRepository;
import com.PhysicalED.service.GradingService;
import com.PhysicalED.service.ScoreService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** CRUD minimale per Score (inserimento punteggio + calcolo voto). */
public class ScoreController {
    @FXML private ComboBox<Student> comboStudente;
    @FXML private ComboBox<SpecialtyEntity> comboTest;
    @FXML private TextField txtValore;

    @FXML private TableView<Score> tblScore;
    @FXML private TableColumn<Score, String> colStudente;
    @FXML private TableColumn<Score, String> colTest;
    @FXML private TableColumn<Score, Double> colValore;
    @FXML private TableColumn<Score, Integer> colVoto;
    @FXML private Label lblEsito;

    private final StudentRepository studentRepo = AppContext.studentRepository();
    private final SpecialtyRepository specialtyRepo = new SpecialtyRepository(AppContext.em());
    private final ScoreRepository scoreRepo = new ScoreRepository(AppContext.em());
    private final ScoreService scoreService = new ScoreService(
            scoreRepo,
            new GradingService(new GradingScaleRepository(AppContext.em()))
    );

    @FXML
    public void initialize() {
        if (colStudente != null) {
            colStudente.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getStudent() != null
                            ? c.getValue().getStudent().getLastName() + " " + c.getValue().getStudent().getFirstName()
                            : ""));
        }
        if (colTest != null) {
            colTest.setCellValueFactory(c -> new SimpleStringProperty(
                    c.getValue().getSpecialty() != null ? c.getValue().getSpecialty().getDescription() : ""));
        }
        if (colValore != null) {
            colValore.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue()));
        }
        if (colVoto != null) {
            colVoto.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getVoto()));
        }

        if (comboStudente != null) {
            comboStudente.setItems(FXCollections.observableArrayList(studentRepo.findAll()));
            comboStudente.valueProperty().addListener((obs, oldV, newV) -> refreshMedia(newV));
        }
        if (comboTest != null) {
            comboTest.setItems(FXCollections.observableArrayList(specialtyRepo.findAll()));
        }

        reload();
        refreshMedia(comboStudente != null ? comboStudente.getValue() : null);
    }

    @FXML
    private void handleAggiungi() {
        if (comboStudente == null || comboTest == null || txtValore == null) return;

        Student stud = comboStudente.getValue();
        SpecialtyEntity specialty = comboTest.getValue();
        if (stud == null || specialty == null) {
            showError("Seleziona studente e specialità.");
            return;
        }

        double val;
        try {
            val = Double.parseDouble(txtValore.getText().trim());
        } catch (Exception e) {
            showError("Valore non valido.");
            return;
        }

        try {
            scoreService.aggiungiScore(stud, specialty, val);
            txtValore.clear();

            reload();
            refreshMedia(stud);
            refreshCombosKeepingSelection();

            showOk("Punteggio aggiunto.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleRimuovi() {
        if (tblScore == null) return;
        Score sel = tblScore.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        scoreRepo.delete(sel.getId());
        reload();
        refreshMedia(comboStudente != null ? comboStudente.getValue() : null);
        refreshCombosKeepingSelection();
        showOk("Punteggio rimosso.");
    }

    private void reload() {
        if (tblScore == null) return;
        tblScore.setItems(FXCollections.observableArrayList(scoreRepo.findAll()));
    }

    private void refreshCombosKeepingSelection() {
        if (comboStudente != null) {
            Student selected = comboStudente.getValue();
            comboStudente.setItems(FXCollections.observableArrayList(studentRepo.findAll()));
            comboStudente.setValue(selected);
        }
        if (comboTest != null) {
            SpecialtyEntity selected = comboTest.getValue();
            comboTest.setItems(FXCollections.observableArrayList(specialtyRepo.findAll()));
            comboTest.setValue(selected);
        }
    }

    private void refreshMedia(Student student) {
        if (lblEsito == null) return;

        if (student == null) {
            lblEsito.setText("Seleziona uno studente per vedere la media.");
            lblEsito.setStyle("-fx-text-fill: #546E7A;");
            return;
        }

        try {
            Double media = scoreService.mediaVotiStudente(student);
            long n = scoreService.numeroSpecialtyValutate(student);
            if (media == null || n == 0) {
                lblEsito.setText("Media: - (nessun punteggio per lo studente)");
                lblEsito.setStyle("-fx-text-fill: #546E7A;");
            } else {
                lblEsito.setText(String.format("Media voti: %.2f (%d specialità)", media, n));
                lblEsito.setStyle("-fx-text-fill: #00796B;");
            }
        } catch (Exception e) {
            showError("Media non disponibile: " + e.getMessage());
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
