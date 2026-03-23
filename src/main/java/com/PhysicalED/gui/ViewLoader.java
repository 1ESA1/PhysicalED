package com.PhysicalED.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * Helper minimale per caricare viste FXML con un messaggio d'errore chiaro
 * se la risorsa non esiste.
 */
public final class ViewLoader {

    private ViewLoader() {
    }

    public static Parent load(String fxmlPath) throws IOException {
        URL url = ViewLoader.class.getResource(fxmlPath);
        if (url == null) {
            throw new IllegalArgumentException("FXML non trovato: " + fxmlPath);
        }
        return FXMLLoader.load(url);
    }

    /**
     * Carica una view e restituisce anche il controller per poter passare parametri.
     */
    public static <T> LoadedView<T> loadWithController(String fxmlPath) throws IOException {
        URL url = ViewLoader.class.getResource(fxmlPath);
        if (url == null) {
            throw new IllegalArgumentException("FXML non trovato: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        @SuppressWarnings("unchecked")
        T controller = (T) loader.getController();
        return new LoadedView<>(root, controller);
    }

    public record LoadedView<T>(Parent root, T controller) {
    }
}
