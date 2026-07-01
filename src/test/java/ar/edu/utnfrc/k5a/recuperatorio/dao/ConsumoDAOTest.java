package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Consumo;
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
 * Tests de {@link ConsumoDAO}: CRUD heredado de {@link GenericDAO} y las
 * consultas especiales {@code findAllConTarjeta}, {@code findByIdConTarjeta}
 * y {@code findByTarjetaIdAnioMes}.
 */
class ConsumoDAOTest extends BaseJpaTest {

    private static final AtomicLong SEQ = new AtomicLong(9000);

    private Tarjeta crearTarjetaDummy() {
        String numero = String.valueOf(4500888800000000L + SEQ.getAndIncrement());
        Tarjeta t = Tarjeta.builder()
                .numero(numero)
                .titular("Titular Consumo Test")
                .limiteCredito(300000d)
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
    void findAll_deberiaTraerLosConsumosSemilla() {
        List<Consumo> todos = consumoDAO.findAll();

        // 15+7+6+5+8+12+9+6+7+11 = 86 consumos cargados por data.sql
        assertTrue(todos.size() >= 86);
    }

    @Test
    void findAllConTarjeta_deberiaTraerLaTarjetaCargada() {
        List<Consumo> todos = consumoDAO.findAllConTarjeta();

        assertFalse(todos.isEmpty());
        assertNotNull(todos.get(0).getTarjeta());
        assertNotNull(todos.get(0).getTarjeta().getNumero());
    }

    @Test
    void findByIdConTarjeta_existente_deberiaEncontrarloConSuTarjeta() {
        Consumo cualquiera = consumoDAO.findAll().get(0);

        Optional<Consumo> encontrado = consumoDAO.findByIdConTarjeta(cualquiera.getId());

        assertTrue(encontrado.isPresent());
        assertNotNull(encontrado.get().getTarjeta());
    }

    @Test
    void findByIdConTarjeta_inexistente_deberiaLanzarNoResultException() {
        assertThrows(NoResultException.class, () -> consumoDAO.findByIdConTarjeta(-1));
    }

    @Test
    void findByTarjetaIdAnioMes_deberiaTraerLosConsumosOrdenadosPorDia() {
        Tarjeta tarjeta1 = tarjetaPorNumero("4500123412340001");

        List<Consumo> consumos = consumoDAO.findByTarjetaIdAnioMes(tarjeta1.getId(), 2026, (short) 5);

        assertEquals(15, consumos.size());
        assertTrue(consumos.stream().allMatch(c -> c.getAnio() == 2026 && c.getMes() == 5));

        for (int i = 1; i < consumos.size(); i++) {
            assertTrue(consumos.get(i - 1).getDia() <= consumos.get(i).getDia());
        }
    }

    @Test
    void findByTarjetaIdAnioMes_sinConsumosEnEsePeriodo_deberiaEstarVacio() {
        Tarjeta tarjeta1 = tarjetaPorNumero("4500123412340001");

        List<Consumo> consumos = consumoDAO.findByTarjetaIdAnioMes(tarjeta1.getId(), 2020, (short) 1);

        assertTrue(consumos.isEmpty());
    }

    @Test
    void crud_deberiaCrearActualizarYEliminar() {
        Tarjeta tarjeta = crearTarjetaDummy();

        Consumo c = Consumo.builder()
                .tarjeta(tarjeta)
                .monto(1000d)
                .dia((short) 10)
                .mes((short) 5)
                .anio(2026)
                .rubro("OTROS")
                .moneda("ARS")
                .build();

        Consumo creado = consumoDAO.create(c);
        assertTrue(creado.getId() > 0);

        Consumo encontrado = consumoDAO.findById(creado.getId()).orElseThrow();
        assertEquals(1000d, encontrado.getMonto(), 0.0001);

        encontrado.setMonto(2500d);
        consumoDAO.update(encontrado);

        Consumo actualizado = consumoDAO.findById(creado.getId()).orElseThrow();
        assertEquals(2500d, actualizado.getMonto(), 0.0001);

        consumoDAO.deleteById(creado.getId());
        assertTrue(consumoDAO.findById(creado.getId()).isEmpty());

        tarjetaDAO.deleteById(tarjeta.getId());
    }
}
