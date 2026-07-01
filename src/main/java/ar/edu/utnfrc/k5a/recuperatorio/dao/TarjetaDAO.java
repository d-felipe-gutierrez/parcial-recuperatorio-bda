package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Tarjeta;
import java.util.List;

public interface TarjetaDAO {

    void guardar(Tarjeta tarjeta);

    Tarjeta buscarPorNumero(String numero);

    List<Tarjeta> obtenerTodas();

    Tarjeta actualizar(Tarjeta tarjeta);

    void eliminar(Tarjeta tarjeta);

    // Consulta requerida
    List<Tarjeta> obtenerSinLiquidacion(Integer anio, Integer mes);

}