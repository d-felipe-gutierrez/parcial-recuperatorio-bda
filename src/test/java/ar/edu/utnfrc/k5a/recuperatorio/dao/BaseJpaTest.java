package ar.edu.utnfrc.k5a.recuperatorio.dao;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * Clase base para los tests de los DAO.
 * <p>
 * Todos los tests comparten la misma base H2 (definida en persistence.xml,
 * cargada una sola vez por JVM con drop-and-create + schema.sql/data.sql),
 * por lo que se ejecutan en el mismo hilo para evitar condiciones de carrera
 * sobre esa base.
 */
@Execution(ExecutionMode.SAME_THREAD)
public abstract class BaseJpaTest {

    protected final TarjetaDAO tarjetaDAO = new TarjetaDAO();
    protected final ConsumoDAO consumoDAO = new ConsumoDAO();
    protected final CotizacionDAO cotizacionDAO = new CotizacionDAO();
    protected final LiquidacionDAO liquidacionDAO = new LiquidacionDAO();
}
