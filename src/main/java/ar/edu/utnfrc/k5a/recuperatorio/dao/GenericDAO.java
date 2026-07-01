package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Optional;
import java.util.List;

public abstract class GenericDAO<T, ID> {
    private final Class<T> clazz;

    // Constructor
    protected GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    // Crear fila
    public T create(T entity) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(entity);
            tx.commit();

            return entity;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Actualizar fila
    public T update(T entity) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();

            return merged;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Buscar fila por ID
    public Optional<T> findById(ID id) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return Optional.ofNullable(em.find(clazz, id));
        } finally {
            em.close();
        }
    }

    // Buscar todas las filas
    public List<T> findAll() {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            String jpql = "select e from " + clazz.getSimpleName() + " e";
            return em.createQuery(jpql, clazz).getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteById(ID id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T entity = em.find(clazz, id);
            if (entity != null) em.remove(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
