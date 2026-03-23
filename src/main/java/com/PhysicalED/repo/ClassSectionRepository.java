package com.PhysicalED.repo;

import com.PhysicalED.model.ClassSection;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClassSectionRepository {
    private final EntityManager em;

    public ClassSectionRepository(EntityManager em) {
        this.em = em;
    }

    public void save(ClassSection classSection) {
        em.getTransaction().begin();
        em.persist(classSection);
        em.getTransaction().commit();
    }

    public ClassSection findById(Long id) {
        return em.find(ClassSection.class, id);
    }

    public List<ClassSection> findAll() {
        return em.createQuery("SELECT c FROM ClassSection c ORDER BY c.schoolYear.description, c.name", ClassSection.class)
                .getResultList();
    }

    public void update(ClassSection classSection) {
        em.getTransaction().begin();
        em.merge(classSection);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        ClassSection classSection = em.find(ClassSection.class, id);
        if (classSection != null) {
            em.remove(classSection);
        }
        em.getTransaction().commit();
    }

    public List<ClassSection> findBySchoolYearId(Long schoolYearId) {
        return em.createQuery(
                        "SELECT c FROM ClassSection c WHERE c.schoolYear.id = :schoolYearId ORDER BY c.name",
                        ClassSection.class
                ).setParameter("schoolYearId", schoolYearId)
                .getResultList();
    }
}
