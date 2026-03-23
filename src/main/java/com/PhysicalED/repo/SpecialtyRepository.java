package com.PhysicalED.repo;

import com.PhysicalED.model.SpecialtyEntity;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SpecialtyRepository {
    private final EntityManager em;

    public SpecialtyRepository(EntityManager em) {
        this.em = em;
    }

    public void save(SpecialtyEntity specialty) {
        em.getTransaction().begin();
        em.persist(specialty);
        em.getTransaction().commit();
    }

    public SpecialtyEntity findById(Long id) {
        return em.find(SpecialtyEntity.class, id);
    }

    public List<SpecialtyEntity> findAll() {
        return em.createQuery(
                        "SELECT s FROM SpecialtyEntity s ORDER BY s.sportCategory.description, s.classSection.name, s.description",
                        SpecialtyEntity.class)
                .getResultList();
    }

    public void update(SpecialtyEntity specialty) {
        em.getTransaction().begin();
        em.merge(specialty);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        SpecialtyEntity sp = em.find(SpecialtyEntity.class, id);
        if (sp != null) {
            em.remove(sp);
        }
        em.getTransaction().commit();
    }

    public List<SpecialtyEntity> findByClassSectionId(Long classSectionId) {
        return em.createQuery(
                        "SELECT s FROM SpecialtyEntity s WHERE s.classSection.id = :classSectionId ORDER BY s.description",
                        SpecialtyEntity.class)
                .setParameter("classSectionId", classSectionId)
                .getResultList();
    }

    public List<SpecialtyEntity> findBySportCategoryId(Long sportCategoryId) {
        return em.createQuery(
                        "SELECT s FROM SpecialtyEntity s WHERE s.sportCategory.id = :sportCategoryId ORDER BY s.classSection.name, s.description",
                        SpecialtyEntity.class)
                .setParameter("sportCategoryId", sportCategoryId)
                .getResultList();
    }
}
