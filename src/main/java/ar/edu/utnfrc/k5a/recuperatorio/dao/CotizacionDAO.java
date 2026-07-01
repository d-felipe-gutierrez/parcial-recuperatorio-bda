package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Cotizacion;
import java.util.List;

public interface CotizacionDAO {

    void guardar(Cotizacion cotizacion);

    Cotizacion buscarPorCodigo(String moneda);

    List<Cotizacion> obtenerTodas();

    Cotizacion actualizar(Cotizacion cotizacion);

    void eliminar(Cotizacion cotizacion);

}
