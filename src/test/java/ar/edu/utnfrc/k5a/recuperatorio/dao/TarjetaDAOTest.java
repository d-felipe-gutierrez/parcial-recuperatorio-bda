package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Tarjeta;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de {@link TarjetaDAO}: CRUD heredado de {@link GenericDAO}
 * y la consulta especial {@code findByNotId} (tarjetas sin liquidación
 * en un año/mes dado).
 */
class TarjetaDAOTest extends BaseJpaTest {

    private static final AtomicLong SEQ = new AtomicLong(9000);

    private Tarjeta nuevaTarjetaDummy() {
        String numero = String.valueOf(4500999900000000L + SEQ.getAndIncrement());
        return Tarjeta.builder()
                .numero(numero)
                .titular("Titular Test")
                .limiteCredito(500000d)
                .build();
    }

    private Tarjeta tarjetaPorNumero(String numero) {
        return tarjetaDAO.findAll().stream()
                .filter(t -> numero.equals(t.getNumero()))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void findAll_deberiaTraerLasTarjetasSemilla() {
        List<Tarjeta> todas = tarjetaDAO.findAll();

        assertTrue(todas.size() >= 10);
    }

    @Test
    void findById_existente_deberiaEncontrarla() {
        Tarjeta t1 = tarjetaPorNumero("4500123412340001");

        Tarjeta encontrada = tarjetaDAO.findById(t1.getId()).orElseThrow();

        assertEquals("Juan Perez", encontrada.getTitular());
    }

    @Test
    void findById_inexistente_deberiaEstarVacio() {
        assertTrue(tarjetaDAO.findById(999_999).isEmpty());
    }

    @Test
    void crud_deberiaCrearActualizarYEliminar() {
        Tarjeta t = nuevaTarjetaDummy();

        Tarjeta creada = tarjetaDAO.create(t);
        assertNotNull(creada);
        assertTrue(creada.getId() > 0);

        Tarjeta encontrada = tarjetaDAO.findById(creada.getId()).orElseThrow();
        assertEquals("Titular Test", encontrada.getTitular());

        encontrada.setTitular("Titular Modificado");
        tarjetaDAO.update(encontrada);

        Tarjeta actualizada = tarjetaDAO.findById(creada.getId()).orElseThrow();
        assertEquals("Titular Modificado", actualizada.getTitular());

        tarjetaDAO.deleteById(creada.getId());
        assertTrue(tarjetaDAO.findById(creada.getId()).isEmpty());
    }

    @Test
    void findByNotId_mayo2026_deberiaTraerSoloLasTarjetasSinLiquidacion() {
        // En data.sql tienen liquidación mayo/2026 las tarjetas 1,2,3,4,5.
        // Quedan sin liquidación: 6,7,8,9,10.
        List<Tarjeta> sinLiquidacion = tarjetaDAO.findByNotId((short) 2026, (short) 5);

        List<String> numeros = sinLiquidacion.stream()
                .map(Tarjeta::getNumero)
                .sorted()
                .toList();

        assertEquals(5, sinLiquidacion.size());
        assertEquals(List.of(
                "4500123412340006",
                "4500123412340007",
                "4500123412340008",
                "4500123412340009",
                "4500123412340010"
        ), numeros);
    }

    @Test
    void findByNotId_abril2026_deberiaExcluirUnicamenteALaTarjetaConLiquidacion() {
        // En data.sql solo la tarjeta 4 tiene liquidación abril/2026.
        List<Tarjeta> sinLiquidacion = tarjetaDAO.findByNotId((short) 2026, (short) 4);

        boolean incluyeTarjeta4 = sinLiquidacion.stream()
                .anyMatch(t -> "4500123412340004".equals(t.getNumero()));

        assertEquals(9, sinLiquidacion.size());
        assertFalse(incluyeTarjeta4);
    }
}
