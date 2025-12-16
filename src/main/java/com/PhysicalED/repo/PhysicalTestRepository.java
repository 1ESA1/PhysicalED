package com.PhysicalED.repo;
import com.PhysicalED.model.PhysicalTest;
import jakarta.persistence.EntityManager;
import java.util.List;
/**
 * Repository class for managing PhysicalTest entities.
 * Provides CRUD operations and custom queries.
 */
public class PhysicalTestRepository {
    public final EntityManager em; // EntityManager for database operations

    public PhysicalTestRepository(EntityManager em) {
        this.em = em;
    }
    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new PhysicalTest saving it to the database
    public void save(PhysicalTest physicalTest) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(physicalTest); // Send the PhysicalTest entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a PhysicalTest by its ID
    public PhysicalTest findById(Long id) {
        return em.find(PhysicalTest.class, id); // Find and return the PhysicalTest entity by its ID
    }

    // READ ALL (find all)
    public List<PhysicalTest> findAll() {
        return em.createQuery("SELECT t FROM physicalTest t", PhysicalTest.class).getResultList();
    }

    // Update an existing PhysicalTest
    public void update(PhysicalTest physicalTest) {
        em.getTransaction().begin();
        em.merge(physicalTest); // Merge the changes to the existing PhysicalTest entity
        em.getTransaction().commit();
    }

    // Delete a PhysicalTest by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        PhysicalTest physicalTest = em.find(PhysicalTest.class, id);
        if (physicalTest != null) {
            em.remove(physicalTest);
        }
        em.getTransaction().commit();
    }

    // Find PhysicalTests by ClassSection ID
    public List<PhysicalTest> findByClassSectionId(Long classSectionId) {
        return em.createQuery("SELECT t FROM PhysicalTest t WHERE t.classSection.id = :classSectionId", PhysicalTest.class)
                 .setParameter("classSectionId", classSectionId)
                 .getResultList();
    }

    // Find PhysicalTests by SportCategory
    public List<PhysicalTest> findBySportCategoryId(Long sportCategoryId) {
        return em.createQuery(
                "SELECT t FROM PhysicalTest t WHERE t.sportCategory.id = :sportCategoryId",
                PhysicalTest.class
        ).setParameter("sportCategoryId", sportCategoryId).getResultList();
    }
}
