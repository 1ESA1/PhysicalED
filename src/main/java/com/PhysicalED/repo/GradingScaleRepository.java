package com.PhysicalED.repo;

import com.PhysicalED.model.Gender;
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.model.SpecialtyEntity;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Repository class for managing GradingScale entities.
 */
public class GradingScaleRepository {
    private final EntityManager em;

    public GradingScaleRepository(EntityManager em) {
        this.em = em;
    }

    public void save(GradingScale gs) {
        em.getTransaction().begin();
        em.persist(gs);
        em.getTransaction().commit();
    }

    public GradingScale findById(Long id) {
        return em.find(GradingScale.class, id);
    }

    public List<GradingScale> findAll() {
        return em.createQuery("SELECT g FROM GradingScale g ORDER BY g.specialty.description, g.gender, g.minValue", GradingScale.class)
                .getResultList();
    }

    public void update(GradingScale gs) {
        em.getTransaction().begin();
        em.merge(gs);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        GradingScale gs = em.find(GradingScale.class, id);
        if (gs != null) {
            em.remove(gs);
        }
        em.getTransaction().commit();
    }

    public List<GradingScale> findBySpecialtyAndGender(SpecialtyEntity specialty, Gender gender) {
        return em.createQuery(
                        "SELECT g FROM GradingScale g WHERE g.specialty = :sp AND g.gender = :gender ORDER BY g.minValue",
                        GradingScale.class)
                .setParameter("sp", specialty)
                .setParameter("gender", gender)
                .getResultList();
    }

    /**
     * Drill-down: restituisce tutte le fasce associate a una specialità.
     */
    public List<GradingScale> findBySpecialtyId(Long specialtyId) {
        return em.createQuery(
                        "SELECT g FROM GradingScale g WHERE g.specialty.id = :sid ORDER BY g.gender, g.minValue",
                        GradingScale.class)
                .setParameter("sid", specialtyId)
                .getResultList();
    }
}