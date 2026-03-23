package com.PhysicalED.repo;

import com.PhysicalED.model.Score;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ScoreRepository {
    private final EntityManager em;

    public ScoreRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Score score) {
        em.getTransaction().begin();
        em.persist(score);
        em.getTransaction().commit();
    }

    public List<Score> findAll() {
        return em.createQuery(
                        "SELECT s FROM Score s ORDER BY s.student.classSection.name, s.student.lastName, s.student.firstName, s.specialty.description",
                        Score.class)
                .getResultList();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        Score score = em.find(Score.class, id);
        if (score != null) {
            em.remove(score);
        }
        em.getTransaction().commit();
    }

    public Double averageVotoByStudentId(Long studentId) {
        return em.createQuery(
                        "SELECT AVG(s.voto) FROM Score s WHERE s.student.id = :studentId",
                        Double.class)
                .setParameter("studentId", studentId)
                .getSingleResult();
    }

    public Long countByStudentId(Long studentId) {
        return em.createQuery(
                        "SELECT COUNT(s) FROM Score s WHERE s.student.id = :studentId",
                        Long.class)
                .setParameter("studentId", studentId)
                .getSingleResult();
    }
}
