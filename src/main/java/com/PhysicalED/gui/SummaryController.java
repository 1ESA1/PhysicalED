package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.Student;
import com.PhysicalED.repo.ScoreRepository;
import com.PhysicalED.repo.StudentRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * View di riepilogo: mostra media voti e numero specialità valutate per studente.
 *
 * Nota: la media è calcolata sui record Score presenti nel DB.
 */
public class SummaryController {

    public static final class SummaryRow {
        private final Student student;
        private final long numTest;
        private final Double media;

        public SummaryRow(Student student, long numTest, Double media) {
            this.student = student;
            this.numTest = numTest;
            this.media = media;
        }

        public Student getStudent() {
            return student;
        }

        public long getNumTest() {
            return numTest;
        }

        public Double getMedia() {
            return media;
        }
    }

    @FXML private ComboBox<Student> comboStudente;
    @FXML private Label lblInfo;

    @FXML private TableView<SummaryRow> tblSummary;
    @FXML private TableColumn<SummaryRow, String> colStudente;
    @FXML private TableColumn<SummaryRow, Long> colNumTest;
    @FXML private TableColumn<SummaryRow, Double> colMedia;

    private final StudentRepository studentRepo = AppContext.studentRepository();
    private final ScoreRepository scoreRepo = new ScoreRepository(AppContext.em());

    @FXML
    public void initialize() {
        if (colStudente != null) {
            colStudente.setCellValueFactory(r -> new SimpleStringProperty(
                    r.getValue().getStudent() != null
                            ? r.getValue().getStudent().getLastName() + " " + r.getValue().getStudent().getFirstName()
                            : ""));
        }
        if (colNumTest != null) {
            colNumTest.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getNumTest()));
        }
        if (colMedia != null) {
            colMedia.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getMedia()));
        }

        if (comboStudente != null) {
            comboStudente.setItems(FXCollections.observableArrayList(studentRepo.findAll()));
            comboStudente.valueProperty().addListener((obs, o, n) -> reload());
        }

        reload();
    }

    @FXML
    private void handleRefresh() {
        reload();
    }

    private void reload() {
        if (tblSummary == null) return;

        Student filtro = comboStudente != null ? comboStudente.getValue() : null;
        List<Student> students = (filtro != null) ? List.of(filtro) : studentRepo.findAll();

        List<SummaryRow> rows = new ArrayList<>();
        for (Student s : students) {
            if (s == null || s.getId() == null) continue;
            Long count = scoreRepo.countByStudentId(s.getId());
            Double avg = scoreRepo.averageVotoByStudentId(s.getId());
            rows.add(new SummaryRow(s, count == null ? 0L : count, avg));
        }

        rows.sort(Comparator.comparing(r -> {
            Student s = r.getStudent();
            return s != null ? (s.getLastName() + " " + s.getFirstName()) : "";
        }));

        tblSummary.setItems(FXCollections.observableArrayList(rows));
        showInfo("Studenti: " + rows.size(), false);
    }

    private void showInfo(String msg, boolean error) {
        if (lblInfo == null) return;
        lblInfo.setText(msg);
        lblInfo.setStyle(error ? "-fx-text-fill: #C62828;" : "-fx-text-fill: #546E7A;");
    }
}
