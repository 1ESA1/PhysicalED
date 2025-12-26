package com.PhysicalED.repo;
/*
 * Repository class for managing GradingScale entities.
 */
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.model.Gender;
import com.PhysicalED.model.PhysicalTest;

import jakarta.persistence.*;
import java.util.List;

public class GradingScaleRepository {

    public void save(GradingScale gs, EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(gs);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
        }
    }

    public List<GradingScale> findByPhysicalTestAndGender(PhysicalTest physicalTest, Gender gender, EntityManager em) {
        return em.createQuery(
                        "SELECT g FROM GradingScale g WHERE g.physicalTest = :pt AND g.gender = :gender",
                        GradingScale.class)
                .setParameter("pt", physicalTest)
                .setParameter("gender", gender)
                .getResultList();
    }
}