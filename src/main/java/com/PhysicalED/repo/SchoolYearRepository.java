package com.PhysicalED.repo;

import com.PhysicalED.model.SchoolYear;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SchoolYearRepository {
    private final EntityManager em;

    public SchoolYearRepository(EntityManager em) {
        this.em = em;
    }

    public void save(SchoolYear schoolYear) {
        em.getTransaction().begin();
        em.persist(schoolYear);
        em.getTransaction().commit();
    }

    public SchoolYear findById(Long id) {
        return em.find(SchoolYear.class, id);
    }

    public List<SchoolYear> findAll() {
        return em.createQuery("SELECT s FROM SchoolYear s ORDER BY s.description", SchoolYear.class)
                .getResultList();
    }

    public void update(SchoolYear schoolYear) {
        em.getTransaction().begin();
        em.merge(schoolYear);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        SchoolYear schoolYear = em.find(SchoolYear.class, id);
        if (schoolYear != null) {
            em.remove(schoolYear);
        }
        em.getTransaction().commit();
    }
}
