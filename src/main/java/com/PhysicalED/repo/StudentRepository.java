package com.PhysicalED.repo;

import com.PhysicalED.model.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

public class StudentRepository {
    private final EntityManager em;

    public StudentRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Student student) {
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    public List<Student> findAll() {
        return em.createQuery("SELECT s FROM Student s ORDER BY s.classSection.name, s.lastName, s.firstName", Student.class)
                .getResultList();
    }

    public void update(Student student) {
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        Student student = em.find(Student.class, id);
        if (student != null) {
            em.remove(student);
        }
        em.getTransaction().commit();
    }

    public List<Student> findByClassSectionId(Long classSectionId) {
        return em.createQuery(
                        "SELECT s FROM Student s WHERE s.classSection.id = :classSectionId ORDER BY s.lastName, s.firstName",
                        Student.class
                ).setParameter("classSectionId", classSectionId)
                .getResultList();
    }
}
