package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Consumo;
import ar.edu.utnfrc.k5a.recuperatorio.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ConsumoDAO extends GenericDAO<Consumo, Integer> {
    public ConsumoDAO() { super(Consumo.class); }

    /**
     * Obtiene todos los consumos junto con su tarjeta.
     */
    public List<Consumo> findAllConTarjeta() {
        EntityManager em = JpaUtil.getEntityManager();

        try {

            return em.createQuery("""
                    SELECT c
                    FROM Consumo c
                    JOIN FETCH c.tarjeta
                    """, Consumo.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Optional<Consumo> findByIdConTarjeta(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();

        try {

            Consumo consumo = em.createQuery("""
                SELECT c
                FROM Consumo c
                JOIN FETCH c.tarjeta
                WHERE c.id = :id
                """, Consumo.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(consumo);

        } finally {
            em.close();
        }
    }

    public List<Consumo> findByTarjetaIdAnioMes(int tarjetaId, int anio, short mes) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery("""
                SELECT c
                FROM Consumo c
                WHERE c.tarjeta.id = :id
                    AND c.anio = :anio
                    AND c.mes = :mes
                ORDER BY c.dia
                """, Consumo.class)
                    .setParameter("id", tarjetaId)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
