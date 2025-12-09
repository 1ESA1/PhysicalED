package com.PhysicalED.repo;

import com.PhysicalED.model.Student;
import jakarta.persistence.EntityManager;
import java.util.List;

public class StudentRepository {
    private final EntityManager em; // EntityManager for database operations

    public StudentRepository(EntityManager em) {
        this.em = em;
    }

    // Method CRUD operations (Create, Read, Update, Delete)
    // Create a new Student saving it to the database
    public void save(Student student) {
        em.getTransaction().begin(); // Begin transaction
        em.persist(student); // Send the Student entity to be saved
        em.getTransaction().commit(); // Commit the transaction
    }

    // Read a Student by its ID
    public Student findById(Long id) {
        return em.find(Student.class, id); // Find and return the Student entity by its ID
    }

    // READ ALL (find all)
    public List<Student> findAll() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    // Update an existing Student
    public void update(Student student) {
        em.getTransaction().begin();
        em.merge(student); // Merge the changes to the existing Student entity
        em.getTransaction().commit();
    }

    // Delete a Student by its ID
    public void delete(Long id) {
        em.getTransaction().begin();
        Student student = em.find(Student.class, id);
        if (student != null) {
            em.remove(student);
        }
        em.getTransaction().commit();
    }
}