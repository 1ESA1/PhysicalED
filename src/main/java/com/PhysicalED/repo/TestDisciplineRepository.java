package com.PhysicalED.repo;

import com.PhysicalED.model.TestDiscipline;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TestDisciplineRepository {
    public final EntityManager em; // EntityManager for database operations

    public TestDisciplineRepository(EntityManager em) {
        this.em = em;
    }

    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new TestDiscipline saving it to the database
    public void save(TestDiscipline testDiscipline) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(testDiscipline); // Send the TestDiscipline entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a TestDiscipline by its ID
    public TestDiscipline findById(Long id) {
        return em.find(TestDiscipline.class, id); // Find and return the TestDiscipline entity by its ID
    }

    // READ ALL (find all)
    public List<TestDiscipline> findAll() {
        return em.createQuery("SELECT t FROM TestDiscipline t", TestDiscipline.class).getResultList();
    }

    // Update an existing TestDiscipline
    public void update(TestDiscipline testDiscipline) {
        em.getTransaction().begin();
        em.merge(testDiscipline); // Merge the changes to the existing TestDiscipline entity
        em.getTransaction().commit();
    }

    // Delete a TestDiscipline by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        TestDiscipline testDiscipline = em.find(TestDiscipline.class, id);
        if (testDiscipline != null) {
            em.remove(testDiscipline);
        }
        em.getTransaction().commit();
    }
}
