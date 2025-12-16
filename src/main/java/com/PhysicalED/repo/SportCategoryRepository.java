package com.PhysicalED.repo;
import com.PhysicalED.model.SportCategory;
import jakarta.persistence.EntityManager;
import java.util.List;
/**
 * Repository class for managing SportCategory entities.
 * Provides CRUD operations to interact with the database.
 */
public class SportCategoryRepository {
    private final EntityManager em; // EntityManager for database operations

    public SportCategoryRepository(EntityManager em) {
        this.em = em;
    }
    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new SportCategory saving it to the database
    public void save(SportCategory sportCategory) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(sportCategory); // Send the SportCategory entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a SportCategory by its ID
    public SportCategory findById(Long id) {
        return em.find(SportCategory.class, id); // Find and return the SportCategory entity by its ID
    }

    // READ ALL (find all)
    public List<SportCategory> findAll() {
        return em.createQuery("SELECT s FROM SportCategory s", SportCategory.class).getResultList();
    }

    // Update an existing SportCategory
    public void update(SportCategory sportCategory) {
        em.getTransaction().begin();
        em.merge(sportCategory); // Merge the changes to the existing SportCategory entity
        em.getTransaction().commit();
    }

    // Delete a SportCategory by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        SportCategory sportCategory = em.find(SportCategory.class, id);
        if (sportCategory != null) {
            em.remove(sportCategory);
        }
        em.getTransaction().commit();
    }
}
