package com.PhysicalED.repo;
import com.PhysicalED.model.Score;
import jakarta.persistence.EntityManager;
import java.util.List;
/**
 * Repository class for managing Score entities in the database.
 * Provides CRUD operations and custom queries.
 */
public class ScoreRepository {
    private final EntityManager em; // EntityManager for database operations

    public ScoreRepository(EntityManager em) {
        this.em = em;
    }
    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new Score salving it to the database
    public void save(Score score) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(score); // Send the Score entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a Score by its ID
    public Score findById(Long id) {
        return em.find(Score.class, id); // Find and return the Score entity by its ID
    }

    // READ ALL (find all)
    public List<Score> findAll() {
        return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
    }

    // Update an existing Score
    public void update(Score score) {
        em.getTransaction().begin(); // Begin transaction
        em.merge(score); // Merge the changes to the existing Score entity
        em.getTransaction().commit(); // Commit the transaction
    }

    // Delete a Score by its ID
    public void delete(Long id) {
        em.getTransaction().begin(); // Begin transaction
        Score score = em.find(Score.class, id);
        if (score != null) {
            em.remove(score);
        }
        em.getTransaction().commit(); // Commit the transaction
    }

    // Custom query methods
    public List<Score> findByStudentId(Long studentId) {
        return em.createQuery(
                "SELECT s FROM Score s WHERE s.student.id = :studentId", Score.class
        ).setParameter("studentId", studentId).getResultList();
    }

    public List<Score> findByPhysicalTestId(Long testId) {
        return em.createQuery(
                "SELECT s FROM Score s WHERE s.physicalTest.id = :testId", Score.class
        ).setParameter("testId", testId).getResultList();
    }
}
