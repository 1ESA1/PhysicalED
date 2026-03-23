package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.ClassSection;
import com.PhysicalED.model.SchoolYear;
import com.PhysicalED.repo.ClassSectionRepository;
import com.PhysicalED.repo.SchoolYearRepository;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.List;

/******************************************
 * Gestione delle ClassSection,           *
 * necessaria per poter inserire Studenti.*
 ******************************************/

public class ClassSectionController {
    private static final String CLASS_STUDENTS_VIEW = "/com/PhysicalED/gui/ClassStudentsView.fxml";

    @FXML private TextField txtNomeClasse;
    @FXML private ComboBox<SchoolYear> comboAnno;
    @FXML private TableView<ClassSection> tblClassi;
    @FXML private TableColumn<ClassSection, String> colNome;
    @FXML private TableColumn<ClassSection, String> colAnno;
    @FXML private Label lblEsito;

    private ClassSectionRepository classSectionRepo;
    private SchoolYearRepository schoolYearRepo;

    @FXML
    public void initialize() {
        try {
            classSectionRepo = AppContext.classSectionRepository();
            schoolYearRepo = AppContext.schoolYearRepository();
        } catch (Exception e) {
            showError("Errore DB: " + e.getMessage());
            return;
        }

        if (colNome != null) {
            colNome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        }
        if (colAnno != null) {
            colAnno.setCellValueFactory(cell -> new SimpleStringProperty(
                    cell.getValue().getSchoolYear() != null ? cell.getValue().getSchoolYear().getDescription() : ""
            ));
        }

        // Doppio click su riga => apre studenti della classe
        if (tblClassi != null) {
            tblClassi.setRowFactory(tv -> {
                TableRow<ClassSection> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        openStudentsFor(row.getItem());
                    }
                });
                return row;
            });
        }

        ensureDefaultSchoolYear();

        if (comboAnno != null) {
            comboAnno.setItems(FXCollections.observableArrayList(schoolYearRepo.findAll()));
            if (!comboAnno.getItems().isEmpty()) {
                comboAnno.getSelectionModel().selectFirst();
            }
        }

        reloadClassiAndKeepSelection(null);
    }

    @FXML
    private void handleAggiungiClasse() {
        if (classSectionRepo == null || schoolYearRepo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (txtNomeClasse == null || comboAnno == null) {
            return;
        }
        String nome = txtNomeClasse.getText().trim();
        SchoolYear anno = comboAnno.getValue();

        if (nome.isEmpty() || anno == null) {
            showError("Inserisci nome classe e anno scolastico.");
            return;
        }

        ClassSection saved = new ClassSection(nome, anno);
        classSectionRepo.save(saved);
        txtNomeClasse.clear();
        showOk("Classe aggiunta.");

        reloadClassiAndKeepSelection(saved.getId());
    }

    @FXML
    private void handleRimuoviClasse() {
        if (classSectionRepo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (tblClassi == null) {
            return;
        }
        ClassSection sel = tblClassi.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        try {
            classSectionRepo.delete(sel.getId());
            showOk("Classe rimossa.");
        } catch (Exception e) {
            // Tipico caso: vincolo FK se esistono studenti collegati.
            showError("Impossibile rimuovere la classe: ci sono studenti associati.");
            return;
        }

        reloadClassiAndKeepSelection(null);
    }

    private void openStudentsFor(ClassSection classSection) {
        if (classSection == null || classSection.getId() == null) {
            return;
        }

        // Carichiamo la view e iniettiamo il parametro nel controller.
        try {
            ViewLoader.LoadedView<ClassStudentsController> loaded = ViewLoader.loadWithController(CLASS_STUDENTS_VIEW);
            loaded.controller().setClassSection(classSection);

            // Sostituiamo la view nel mainPanel (StackPane) del main.fxml.
            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                // Fallback: se non troviamo il pannello principale, almeno evitiamo crash.
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            Parent root = loaded.root();
            mainPanel.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Errore apertura studenti: " + e.getMessage());
        }
    }

    /**
     * Cerca lo StackPane mainPanel risalendo la gerarchia a partire dalla tabella.
     * Questo evita di dover passare dipendenze tra controller.
     */
    private StackPane findMainPanel() {
        if (tblClassi == null || tblClassi.getScene() == null) {
            return null;
        }
        // Lookup per fx:id funziona perché JavaFX assegna id e/o lo espone nel namespace.
        // Qui usiamo un approccio semplice e robusto via lookup CSS.
        javafx.scene.Node n = tblClassi.getScene().lookup("#mainPanel");
        if (n instanceof StackPane sp) {
            return sp;
        }
        return null;
    }

    private void reloadClassiAndKeepSelection(Long preferSelectId) {
        if (classSectionRepo == null || tblClassi == null) {
            return;
        }

        Long currentId = null;
        ClassSection current = tblClassi.getSelectionModel().getSelectedItem();
        if (current != null) {
            currentId = current.getId();
        }

        List<ClassSection> all = classSectionRepo.findAll();
        tblClassi.setItems(FXCollections.observableArrayList(all));

        Long idToSelect = preferSelectId != null ? preferSelectId : currentId;
        if (idToSelect != null) {
            for (ClassSection cs : all) {
                if (cs.getId() != null && cs.getId().equals(idToSelect)) {
                    tblClassi.getSelectionModel().select(cs);
                    return;
                }
            }
        }

        if (!all.isEmpty()) {
            tblClassi.getSelectionModel().selectFirst();
        }
    }

    /**
     * Seed minimale: se non c'è alcun SchoolYear, ne crea uno di default.
     */
    private void ensureDefaultSchoolYear() {
        if (schoolYearRepo == null) return;
        List<SchoolYear> anni = schoolYearRepo.findAll();
        if (anni.isEmpty()) {
            schoolYearRepo.save(new SchoolYear("2025/2026"));
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
