package com.PhysicalED.config;

import com.PhysicalED.repo.ClassSectionRepository;
import com.PhysicalED.repo.SchoolYearRepository;
import com.PhysicalED.repo.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/***************************************************************************
 * AppContext è una classe singleton che gestisce l'EntityManagerFactory e *
 * l'EntityManager di JPA/Hibernate.                                       *
 * ************************************************************************/

public final class AppContext {

    private static final String PERSISTENCE_UNIT = "PhysicalEDPU";

    private static volatile EntityManagerFactory emf;
    private static volatile EntityManager em;
    private static volatile boolean closed = false;

    private AppContext() {}

    private static EntityManagerFactory emf() {
        EntityManagerFactory local = emf;
        if (local == null) {
            synchronized (AppContext.class) {
                local = emf;
                if (local == null) {
                    try {
                        Map<String, Object> props = new HashMap<>();
                        String url = System.getProperty("db.url");
                        if (url != null && !url.isBlank()) {
                            props.put("jakarta.persistence.jdbc.url", url);
                        }
                        local = props.isEmpty()
                                ? Persistence.createEntityManagerFactory(PERSISTENCE_UNIT)
                                : Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, props);
                        emf = local;
                    } catch (Exception e) {
                        throw new DatabaseException(
                                "Impossibile inizializzare JPA/Hibernate. Controlla persistence.xml e dipendenze SQLite. " +
                                        "Dettaglio: " + e.getMessage(),
                                e
                        );
                    }
                }
            }
        }
        return local;
    }

    public static EntityManager em() {
        EntityManager local = em;
        if (local == null || !local.isOpen()) {
            synchronized (AppContext.class) {
                local = em;
                if (local == null || !local.isOpen()) {
                    em = local = emf().createEntityManager();
                }
            }
        }
        return local;
    }

    public static SchoolYearRepository schoolYearRepository() {
        return new SchoolYearRepository(em());
    }

    public static ClassSectionRepository classSectionRepository() {
        return new ClassSectionRepository(em());
    }

    public static StudentRepository studentRepository() {
        return new StudentRepository(em());
    }

    /** Da chiamare quando l'app termina. È idempotente.*/

    public static void shutdown() {
        if (closed) return;
        closed = true;

        try {
            EntityManager localEm = em;
            if (localEm != null && localEm.isOpen()) {
                localEm.close();
            }
        } finally {
            em = null;
            EntityManagerFactory local = emf;
            if (local != null && local.isOpen()) {
                local.close();
            }
        }
    }
}
