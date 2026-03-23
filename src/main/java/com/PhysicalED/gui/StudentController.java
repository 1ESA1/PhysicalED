package com.PhysicalED.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller legacy: la gestione studenti ora avviene da Classi -> Studenti.
 * Manteniamo questo file solo per non rompere build/risorse eventualmente presenti,
 * ma la view non è più linkata dalla navigazione principale.
 */
public class StudentController {
    @FXML private Label lblEsito;

    @FXML
    public void initialize() {
        if (lblEsito != null) {
            lblEsito.setText("Gestione studenti disponibile in: Classi → (doppio click) → Studenti");
        }
    }
}
