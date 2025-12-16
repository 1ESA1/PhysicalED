package com.PhysicalED.model;
/**
 * Classe per rappresentare il genere di uno studente.
 */
public enum Gender {
    M("Maschio"),
    F("Femmina");

    private final String description;

    Gender(String descrizione) {
        this.description = descrizione;
    }

    public String getDescription() {
        return description;
    }
}
