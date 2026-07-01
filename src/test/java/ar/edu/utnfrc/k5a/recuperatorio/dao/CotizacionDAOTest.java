package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Cotizacion;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de {@link CotizacionDAO}, que hereda el CRUD genérico de {@link GenericDAO}.
 */
class CotizacionDAOTest extends BaseJpaTest {

    @Test
    void findAll_deberiaTraerLasCotizacionesSemilla() {
        List<Cotizacion> todas = cotizacionDAO.findAll();

        // ARS, USD, EUR, BRL, CLP cargadas por data.sql
        assertTrue(todas.size() >= 5);
    }

    @Test
    void findById_existente_deberiaTraerLaTasaCorrecta() {
        Optional<Cotizacion> ars = cotizacionDAO.findById("ARS");

        assertTrue(ars.isPresent());
        assertEquals(1.00, ars.get().getTasaCambio(), 0.0001);
    }

    @Test
    void findById_inexistente_deberiaEstarVacio() {
        Optional<Cotizacion> inexistente = cotizacionDAO.findById("XXX");

        assertTrue(inexistente.isEmpty());
    }

    @Test
    void crud_deberiaCrearActualizarYEliminar() {
        Cotizacion nueva = Cotizacion.builder()
                .moneda("Z99")
                .tasaCambio(1234.56)
                .build();

        cotizacionDAO.create(nueva);

        Cotizacion encontrada = cotizacionDAO.findById("Z99").orElseThrow();
        assertEquals(1234.56, encontrada.getTasaCambio(), 0.0001);

        encontrada.setTasaCambio(1400.00);
        cotizacionDAO.update(encontrada);

        Cotizacion actualizada = cotizacionDAO.findById("Z99").orElseThrow();
        assertEquals(1400.00, actualizada.getTasaCambio(), 0.0001);

        cotizacionDAO.deleteById("Z99");
        assertTrue(cotizacionDAO.findById("Z99").isEmpty());
    }
}
