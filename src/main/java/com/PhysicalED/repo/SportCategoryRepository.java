package com.PhysicalED.repo;

import com.PhysicalED.model.SportCategory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SportCategoryRepository {
    private final EntityManager em;

    public SportCategoryRepository(EntityManager em) {
        this.em = em;
    }

    public void save(SportCategory sportCategory) {
        em.getTransaction().begin();
        em.persist(sportCategory);
        em.getTransaction().commit();
    }

    public SportCategory findById(Long id) {
        return em.find(SportCategory.class, id);
    }

    public List<SportCategory> findAll() {
        return em.createQuery("SELECT s FROM SportCategory s ORDER BY s.description", SportCategory.class)
                .getResultList();
    }

    public void update(SportCategory sportCategory) {
        em.getTransaction().begin();
        em.merge(sportCategory);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        SportCategory sportCategory = em.find(SportCategory.class, id);
        if (sportCategory != null) {
            em.remove(sportCategory);
        }
        em.getTransaction().commit();
    }
}
