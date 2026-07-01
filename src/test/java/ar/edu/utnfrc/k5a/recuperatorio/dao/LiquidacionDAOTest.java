package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Liquidacion;
import ar.edu.utnfrc.k5a.recuperatorio.entities.Tarjeta;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de {@link LiquidacionDAO}: CRUD heredado de {@link GenericDAO} y las
 * consultas especiales {@code findAllConTarjeta}, {@code findByIdConTarjeta},
 * {@code findTarjetaIdByAnioMes} y {@code findByTarjetaIdAnioMes}.
 */
class LiquidacionDAOTest extends BaseJpaTest {

    private static final AtomicLong SEQ = new AtomicLong(9000);

    private Tarjeta crearTarjetaDummy() {
        String numero = String.valueOf(4500777700000000L + SEQ.getAndIncrement());
        Tarjeta t = Tarjeta.builder()
                .numero(numero)
                .titular("Titular Liquidacion Test")
                .limiteCredito(400000d)
                .build();
        return tarjetaDAO.create(t);
    }

    private Tarjeta tarjetaPorNumero(String numero) {
        return tarjetaDAO.findAll().stream()
                .filter(t -> numero.equals(t.getNumero()))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void findAll_deberiaTraerLasLiquidacionesSemilla() {
        List<Liquidacion> todas = liquidacionDAO.findAll();

        assertTrue(todas.size() >= 6);
    }

    @Test
    void findAllConTarjeta_deberiaTraerLaTarjetaCargada() {
        List<Liquidacion> todas = liquidacionDAO.findAllConTarjeta();

        assertFalse(todas.isEmpty());
        assertNotNull(todas.get(0).getTarjeta());
    }

    @Test
    void findByIdConTarjeta_existente_deberiaEncontrarlaConSuTarjeta() {
        Liquidacion cualquiera = liquidacionDAO.findAll().get(0);

        Optional<Liquidacion> encontrada = liquidacionDAO.findByIdConTarjeta(cualquiera.getId());

        assertTrue(encontrada.isPresent());
        assertNotNull(encontrada.get().getTarjeta());
    }

    @Test
    void findByIdConTarjeta_inexistente_deberiaLanzarNoResultException() {
        assertThrows(NoResultException.class, () -> liquidacionDAO.findByIdConTarjeta(-1));
    }

    @Test
    void findTarjetaIdByAnioMes_mayo2026_deberiaTraerLosIdsEsperados() {
        Tarjeta t1 = tarjetaPorNumero("4500123412340001");
        Tarjeta t2 = tarjetaPorNumero("4500123412340002");
        Tarjeta t3 = tarjetaPorNumero("4500123412340003");
        Tarjeta t4 = tarjetaPorNumero("4500123412340004");
        Tarjeta t5 = tarjetaPorNumero("4500123412340005");

        List<Integer> ids = liquidacionDAO.findTarjetaIdByAnioMes(2026, (short) 5);

        assertEquals(5, ids.size());
        assertTrue(ids.containsAll(List.of(
                t1.getId(), t2.getId(), t3.getId(), t4.getId(), t5.getId()
        )));
    }

    @Test
    void findByTarjetaIdAnioMes_existente_deberiaEncontrarla() {
        Tarjeta t4 = tarjetaPorNumero("4500123412340004");

        List<Liquidacion> liq = liquidacionDAO.findByTarjetaIdAnioMes(t4.getId(), 2026, (short) 5);

        assertEquals(1, liq.size());
        assertEquals(93106.25, liq.get(0).getTotalAPagar(), 0.01);
    }

    @Test
    void findByTarjetaIdAnioMes_inexistente_deberiaEstarVacio() {
        Tarjeta t10 = tarjetaPorNumero("4500123412340010");

        List<Liquidacion> liq = liquidacionDAO.findByTarjetaIdAnioMes(t10.getId(),2026, (short) 4);

        assertTrue(liq.isEmpty());
    }

    @Test
    void crud_deberiaCrearActualizarYEliminar() {
        Tarjeta tarjeta = crearTarjetaDummy();

        Liquidacion l = Liquidacion.builder()
                .tarjeta(tarjeta)
                .mes((short) 6)
                .anio(2026)
                .totalConsumos(100000d)
                .totalImpustos(15000d)
                .totalDescuentos(5000d)
                .totalAPagar(110000d)
                .build();

        Liquidacion creada = liquidacionDAO.create(l);
        assertTrue(creada.getId() > 0);

        Liquidacion encontrada = liquidacionDAO.findById(creada.getId()).orElseThrow();
        assertEquals(110000d, encontrada.getTotalAPagar(), 0.0001);

        encontrada.setTotalAPagar(120000d);
        liquidacionDAO.update(encontrada);

        Liquidacion actualizada = liquidacionDAO.findById(creada.getId()).orElseThrow();
        assertEquals(120000d, actualizada.getTotalAPagar(), 0.0001);

        liquidacionDAO.deleteById(creada.getId());
        assertTrue(liquidacionDAO.findById(creada.getId()).isEmpty());

        tarjetaDAO.deleteById(tarjeta.getId());
    }
}
