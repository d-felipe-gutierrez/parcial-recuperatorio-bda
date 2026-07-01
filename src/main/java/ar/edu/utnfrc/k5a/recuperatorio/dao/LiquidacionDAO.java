package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Consumo;
import ar.edu.utnfrc.k5a.recuperatorio.entities.Liquidacion;
import ar.edu.utnfrc.k5a.recuperatorio.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class LiquidacionDAO extends GenericDAO<Liquidacion, Integer> {
    public LiquidacionDAO() { super(Liquidacion.class); }

    /**
     * Obtiene todos los consumos junto con su tarjeta.
     */
    public List<Liquidacion> findAllConTarjeta() {

        EntityManager em = JpaUtil.getEntityManager();

        try {

            return em.createQuery("""
                    SELECT l
                    FROM Liquidacion l
                    JOIN FETCH l.tarjeta
                    """, Liquidacion.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Optional<Liquidacion> findByIdConTarjeta(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();

        try {

            Liquidacion liquidacion = em.createQuery("""
                SELECT l
                FROM Liquidacion l
                JOIN FETCH l.tarjeta
                WHERE l.id = :id
                """, Liquidacion.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(liquidacion);

        } finally {
            em.close();
        }
    }

    public List<Integer> findTarjetaIdByAnioMes(short anio, short mes) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery("""
            SELECT l.tarjeta.id
            FROM Liquidacion l
            WHERE l.anio = :anio
                AND l.mes = :mes
            """, Integer.class)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultList();
        } finally {
            em.close();
        }

    }

    public List<Liquidacion> findByTarjetaIdAnioMes(int tarjetaId, short anio, short mes) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery("""
                SELECT l
                FROM Liquidacion l
                WHERE l.tarjeta.id = :id
                    AND l.anio = :anio
                    AND l.mes = :mes
                """, Liquidacion.class)
                    .setParameter("id", tarjetaId)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
