package com.PhysicalED.config;

import jakarta.persistence.EntityManager;

/******************************************************
 * Classe di bootstrap per forzare l'inizializzazione *
 * di JPA/Hibernate all'avvio dell'applicazione.      *
 ******************************************************/

public final class DbBootstrap {
    private DbBootstrap() {}

    public static void initOrThrow() {
        EntityManager em = AppContext.em();
        em.getMetamodel();
    }
}
