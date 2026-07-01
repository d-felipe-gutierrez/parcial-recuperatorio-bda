package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Tarjeta;
import ar.edu.utnfrc.k5a.recuperatorio.dao.LiquidacionDAO;
import ar.edu.utnfrc.k5a.recuperatorio.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TarjetaDAO extends GenericDAO<Tarjeta, Integer> {
    public TarjetaDAO() { super(Tarjeta.class); }

    public List<Tarjeta> findByNotId(short anio, short mes) {
        EntityManager em = JpaUtil.getEntityManager();
        LiquidacionDAO liquidacionDAO = new LiquidacionDAO();
        List<Integer> ids = liquidacionDAO.findTarjetaIdByAnioMes(anio, mes);

        try {
            return em.createQuery("""
                SELECT t
                FROM Tarjeta t
                WHERE t.id NOT IN :ids
                """, Tarjeta.class)
                    .setParameter("ids", ids)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}