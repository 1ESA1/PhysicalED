package com.PhysicalED.config;

/********************************************************************
 * Eccezione runtime per errori di bootstrap DB/JPA.                *
 * Serve a distinguere i problemi di persistenza dagli errori di UI.*
 ********************************************************************/

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

