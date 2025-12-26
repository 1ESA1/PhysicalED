package com.PhysicalED.gui;

import com.PhysicalED.model.*;
import com.PhysicalED.repo.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StudentViewController {
    @FXML private TableView<Student> tblStudenti;
    @FXML private TableColumn<Student, String> colNome;
    @FXML private TableColumn<Student, String> colCognome;
    @FXML private TableColumn<Student, Gender> colGenere;
    @FXML private TableColumn<Student, String> colClasse;
    @FXML private TextField txtNome, txtCognome;
    @FXML private ComboBox<Gender> comboGenere;
    @FXML private ComboBox<ClassSection> comboClasse;
    @FXML private Label lblEsito;

    // Dovrai iniettare/ottenere queste istanze a seconda del tuo setup.
    private final StudentRepository studentRepo = new StudentRepository();
    private final ClassSectionRepository classSectionRepo = new ClassSectionRepository();

    @FXML
    public void initialize() {
        // Setup delle colonne tabella
        colNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFirstName()));
        colCognome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLastName()));
        colGenere.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getGender()));
        colClasse.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getClassSection() != null ? cell.getValue().getClassSection().getName() : ""
        ));

        comboGenere.setItems(FXCollections.observableArrayList(Gender.values()));
        comboClasse.setItems(FXCollections.observableArrayList(classSectionRepo.findAll()));

        ricaricaTabellaStudenti();
    }

    @FXML
    private void handleAggiungiStudente() {
        String nome = txtNome.getText().trim();
        String cognome = txtCognome.getText().trim();
        Gender genere = comboGenere.getValue();
        ClassSection classe = comboClasse.getValue();

        if (nome.isEmpty() || cognome.isEmpty() || genere == null || classe == null) {
            lblEsito.setText("Compila tutti i campi!");
            lblEsito.setStyle("-fx-text-fill: red;");
            return;
        }

        Student stud = new Student();
        stud.setFirstName(nome);
        stud.setLastName(cognome);
        stud.setGender(genere);
        stud.setClassSection(classe);

        studentRepo.save(stud);
        lblEsito.setText("Studente aggiunto con successo!");
        lblEsito.setStyle("-fx-text-fill: green;");

        txtNome.clear();
        txtCognome.clear();
        comboGenere.setValue(null);
        comboClasse.setValue(null);
        ricaricaTabellaStudenti();
    }

    @FXML
    private void handleRimuoviStudente() {
        Student selezionato = tblStudenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            studentRepo.delete(selezionato.getId());
            lblEsito.setText("Studente rimosso.");
            lblEsito.setStyle("-fx-text-fill: green;");
            ricaricaTabellaStudenti();
        }
    }

    private void ricaricaTabellaStudenti() {
        tblStudenti.setItems(FXCollections.observableArrayList(studentRepo.findAll()));
    }
}
