package com.PhysicalED;

import com.PhysicalED.config.AppContext;
import com.PhysicalED.model.SchoolYear;
import com.PhysicalED.repo.SchoolYearRepository;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Smoke test minimale: verifica che JPA su SQLite si inizializzi e possa persistere/leggere.
 */
public class PersistenceSmokeTest {

    @Test
    public void sqlite_persistenzaBase_funzionante() {
        SchoolYearRepository repo = AppContext.schoolYearRepository();
        repo.save(new SchoolYear("TEST"));
        assertFalse(repo.findAll().isEmpty());
    }
}

