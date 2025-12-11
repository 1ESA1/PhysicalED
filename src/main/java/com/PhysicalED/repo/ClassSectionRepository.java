package com.PhysicalED.repo;
import com.PhysicalED.model.ClassSection;
import jakarta.persistence.EntityManager;
import java.util.List;
/**
 * Repository class for managing ClassSection entities.
 * Provides CRUD operations and custom queries.
 */
public class ClassSectionRepository {
    private final EntityManager em; // EntityManager for database operations

    public ClassSectionRepository(EntityManager em) {
        this.em = em;
    }
    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new ClassSection saving it to the database
    public void save(ClassSection classSection) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(classSection); // Send the ClassSection entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a ClassSection by its ID
    public ClassSection findById(Long id) {
        return em.find(ClassSection.class, id); // Find and return the ClassSection entity by its ID
    }

    // READ ALL (find all)
    public List<ClassSection> findAll() {
        return em.createQuery("SELECT c FROM ClassSection c", ClassSection.class).getResultList();
    }

    // Update an existing ClassSection
    public void update(ClassSection classSection) {
        em.getTransaction().begin();
        em.merge(classSection); // Merge the changes to the existing ClassSection entity
        em.getTransaction().commit();
    }

    // Delete a ClassSection by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        ClassSection classSection = em.find(ClassSection.class, id);
        if (classSection != null) {
            em.remove(classSection);
        }
        em.getTransaction().commit();
    }

    // Find ClassSections by School Year
    public List<ClassSection> findBySchoolYearId(Long schoolYearId) {
        return em.createQuery(
                "SELECT c FROM ClassSection c WHERE c.schoolYear.id = :schoolYearId",
                ClassSection.class
        ).setParameter("schoolYearId", schoolYearId).getResultList();
    }
}
