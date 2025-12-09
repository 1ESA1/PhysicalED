package com.PhysicalED.repo;

import com.PhysicalED.model.SportingDiscipline;
import jakarta.persistence.EntityManager;
import java.util.List;

public class SportingDisciplineRepository {
    private final EntityManager em; // EntityManager for database operations

    public SportingDisciplineRepository(EntityManager em) {
        this.em = em;
    }

    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new SportingDiscipline saving it to the database
    public void save(SportingDiscipline sportingDiscipline) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(sportingDiscipline); // Send the SportingDiscipline entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a SportingDiscipline by its ID
    public SportingDiscipline findById(Long id) {
        return em.find(SportingDiscipline.class, id); // Find and return the SportingDiscipline entity by its ID
    }

    // READ ALL (find all)
    public List<SportingDiscipline> findAll() {
        return em.createQuery("SELECT s FROM SportingDiscipline s", SportingDiscipline.class).getResultList();
    }

    // Update an existing SportingDiscipline
    public void update(SportingDiscipline sportingDiscipline) {
        em.getTransaction().begin();
        em.merge(sportingDiscipline); // Merge the changes to the existing SportingDiscipline entity
        em.getTransaction().commit();
    }

    // Delete a SportingDiscipline by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        SportingDiscipline sportingDiscipline = em.find(SportingDiscipline.class, id);
        if (sportingDiscipline != null) {
            em.remove(sportingDiscipline);
        }
        em.getTransaction().commit();
    }
}
