package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Liquidacion;
import java.util.List;

public interface LiquidacionDAO {

    void guardar(Liquidacion liquidacion);

    Liquidacion buscarPorId(Long id);

    List<Liquidacion> obtenerTodas();

    Liquidacion actualizar(Liquidacion liquidacion);

    void eliminar(Liquidacion liquidacion);

    // Consulta requerida
    Liquidacion buscarPorTarjetaYPeriodo(
            String numeroTarjeta,
            Integer anio,
            Integer mes);

}
