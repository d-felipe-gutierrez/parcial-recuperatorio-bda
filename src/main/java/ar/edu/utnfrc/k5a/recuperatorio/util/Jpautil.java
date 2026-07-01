package ar.edu.utnfrc.k5a.recuperatorio.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Jpautil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("RecuperatorioPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void clise() {
        emf.close();
    }
}