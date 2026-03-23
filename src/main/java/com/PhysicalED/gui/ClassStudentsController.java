package com.PhysicalED.gui;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.ClassSection;
import com.PhysicalED.model.Gender;
import com.PhysicalED.model.Student;
import com.PhysicalED.repo.StudentRepository;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * Schermata: Studenti appartenenti ad una specifica classe.
 *
 * Nota: per mantenere il progetto minimale e robusto, questa view NON carica
 * gli studenti via relazione JPA (OneToMany) ma tramite query repository.
 */
public class ClassStudentsController {
    private static final String CLASS_VIEW = "/com/PhysicalED/gui/ClassSectionView.fxml";

    @FXML private Label lblTitolo;

    @FXML private TableView<Student> tblStudenti;
    @FXML private TableColumn<Student, String> colNome;
    @FXML private TableColumn<Student, String> colCognome;
    @FXML private TableColumn<Student, Gender> colGenere;

    @FXML private TextField txtNome;
    @FXML private TextField txtCognome;
    @FXML private ComboBox<Gender> comboGenere;

    @FXML private Label lblEsito;

    private StudentRepository studentRepo;

    private ClassSection classSection;

    /**
     * Va chiamato subito dopo il load della view.
     */
    public void setClassSection(ClassSection classSection) {
        this.classSection = classSection;
        if (lblTitolo != null && classSection != null) {
            lblTitolo.setText("Studenti - " + classSection.getName());
        }
        reload();
    }

    @FXML
    public void initialize() {
        try {
            studentRepo = AppContext.studentRepository();
        } catch (Exception e) {
            showError("Errore DB: " + e.getMessage());
            return;
        }

        if (colNome != null) {
            colNome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstName()));
        }
        if (colCognome != null) {
            colCognome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastName()));
        }
        if (colGenere != null) {
            colGenere.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getGender()));
        }

        if (comboGenere != null) {
            comboGenere.setItems(FXCollections.observableArrayList(Gender.values()));
        }

        // Se la classe non è stata ancora iniettata, la view resta “in attesa”.
        reload();
    }

    @FXML
    private void handleAggiungiStudente() {
        if (studentRepo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (classSection == null || classSection.getId() == null) {
            showError("Classe non impostata.");
            return;
        }

        String nome = txtNome != null ? txtNome.getText().trim() : "";
        String cognome = txtCognome != null ? txtCognome.getText().trim() : "";
        Gender genere = comboGenere != null ? comboGenere.getValue() : null;

        if (nome.isEmpty() || cognome.isEmpty() || genere == null) {
            showError("Compila nome, cognome e genere.");
            return;
        }

        Student stud = new Student();
        stud.setFirstName(nome);
        stud.setLastName(cognome);
        stud.setGender(genere);
        stud.setClassSection(classSection);

        studentRepo.save(stud);
        showOk("Studente aggiunto.");

        if (txtNome != null) txtNome.clear();
        if (txtCognome != null) txtCognome.clear();
        if (comboGenere != null) comboGenere.setValue(null);

        reload();
    }

    @FXML
    private void handleRimuoviStudente() {
        if (studentRepo == null) {
            showError("DB non disponibile.");
            return;
        }
        if (tblStudenti == null) {
            return;
        }
        Student sel = tblStudenti.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        studentRepo.delete(sel.getId());
        showOk("Studente rimosso.");
        reload();
    }

    @FXML
    private void handleAggiorna() {
        reload();
    }

    @FXML
    private void handleBackToClasses() {
        try {
            StackPane mainPanel = findMainPanel();
            if (mainPanel == null) {
                showError("Impossibile navigare: mainPanel non trovato.");
                return;
            }
            Parent root = ViewLoader.load(CLASS_VIEW);
            mainPanel.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Errore navigazione: " + e.getMessage());
        }
    }

    private StackPane findMainPanel() {
        if (tblStudenti == null || tblStudenti.getScene() == null) {
            return null;
        }
        javafx.scene.Node n = tblStudenti.getScene().lookup("#mainPanel");
        if (n instanceof StackPane sp) {
            return sp;
        }
        return null;
    }

    private void reload() {
        if (tblStudenti == null) {
            return;
        }
        if (studentRepo == null || classSection == null || classSection.getId() == null) {
            tblStudenti.setItems(FXCollections.observableArrayList());
            if (lblTitolo != null && (classSection == null)) {
                lblTitolo.setText("Studenti");
            }
            return;
        }

        List<Student> studenti = studentRepo.findByClassSectionId(classSection.getId());
        tblStudenti.setItems(FXCollections.observableArrayList(studenti));
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

