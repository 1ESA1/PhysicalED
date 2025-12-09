package com.PhysicalED;

import com.PhysicalED.model.*;
import com.PhysicalED.repo.*;
import jakarta.persistence.*;

/**
 * Programma che permette di gestire i voti e la media,
 * in base a punteggi assegnati nei vari test fisici.
 */
public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhysicalEDPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Test: crea e salva uno SchoolYear
            SchoolYear year = new SchoolYear();
            year.setDescription("2024/2025");
            em.persist(year);

            // Test: crea e salva una ClassSection
            ClassSection section = new ClassSection();
            section.setName("1A");
            section.setSchoolYear(year);
            em.persist(section);

            // Test: crea e salva uno Student
            Student student = new Student();
            student.setFirstName("Mario");
            student.setLastName("Rossi");
            student.setClassSection(section);
            em.persist(student);

            em.getTransaction().commit();

            // Recupera e stampa i dati inseriti
            SchoolYear found = em.find(SchoolYear.class, year.getId());
            System.out.println("SchoolYear trovato: " + found.getDescription());
        } finally {
            em.close();
            emf.close();
        }
    }
}