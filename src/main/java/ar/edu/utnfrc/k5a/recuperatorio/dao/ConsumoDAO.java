package ar.edu.utnfrc.k5a.recuperatorio.dao;

import ar.edu.utnfrc.k5a.recuperatorio.entities.Consumo;
import java.util.List;

public interface ConsumoDAO {

    void guardar(Consumo consumo);

    Consumo buscarPorId(Long id);

    List<Consumo> obtenerTodos();

    Consumo actualizar(Consumo consumo);

    void eliminar(Consumo consumo);

    // Consulta requerida
    List<Consumo> obtenerPorTarjetaYPeriodo(
            String numeroTarjeta,
            Integer anio,
            Integer mes);

}
