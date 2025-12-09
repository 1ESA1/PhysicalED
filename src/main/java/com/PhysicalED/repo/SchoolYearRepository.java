package com.PhysicalED.repo;

import com.PhysicalED.model.SchoolYear;
import jakarta.persistence.EntityManager;
import java.util.List;

public class SchoolYearRepository {
    private final EntityManager em; // EntityManager for database operations

    public SchoolYearRepository(EntityManager em) {
        this.em = em;
    }

    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new SchoolYear salving it to the database
    public void save(SchoolYear schoolYear) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(schoolYear); // Send the SchoolYear entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a SchoolYear by its ID
    public SchoolYear findById(Long id) {
        return em.find(SchoolYear.class, id); // Find and return the SchoolYear entity by its ID
    }

    // READ ALL (find all)
    public List<SchoolYear> findAll() {
        return em.createQuery("SELECT s FROM SchoolYear s", SchoolYear.class).getResultList();
    }

    // Update an existing SchoolYear
    public void update(SchoolYear schoolYear) {
        em.getTransaction().begin();
        em.merge(schoolYear); // Merge the changes to the existing SchoolYear entity
        em.getTransaction().commit();
    }

    // Delete a SchoolYear by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        SchoolYear schoolYear = em.find(SchoolYear.class, id);
        if (schoolYear != null) {
            em.remove(schoolYear);
        }
        em.getTransaction().commit();
    }
}