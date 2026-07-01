package ar.edu.utnfrc.k5a.recuperatorio;

import ar.edu.utnfrc.k5a.recuperatorio.dao.ConsumoDAO;
import ar.edu.utnfrc.k5a.recuperatorio.dao.CotizacionDAO;
import ar.edu.utnfrc.k5a.recuperatorio.dao.LiquidacionDAO;
import ar.edu.utnfrc.k5a.recuperatorio.dao.TarjetaDAO;
import ar.edu.utnfrc.k5a.recuperatorio.entities.*;
import ar.edu.utnfrc.k5a.recuperatorio.util.JpaUtil;

import java.awt.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ConsumoDAO consumoDAO = new ConsumoDAO();
        CotizacionDAO cotizacionDAO = new CotizacionDAO();
        LiquidacionDAO liquidacionDAO = new LiquidacionDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();

        try {
            System.out.println("----------------------------------------------------" +
                    "CONSULTAR TODAS LAS COTIZACIONES" +
                    "----------------------------------------------------");
            cotizacionDAO.findAll().forEach(t ->
                    System.out.println("-Moneda " + t.getMoneda() + "\n" +
                            "-Tasa de Cambio " + t.getTasaCambio() + "\n"
                    )
            );

            System.out.println("----------------------------------------------------" +
                    "CONSULTAR TODAS LAS TARJETAS" +
                    "----------------------------------------------------");
            tarjetaDAO.findAll().forEach(t ->
                    System.out.println("-ID " + t.getId() + "\n" +
                            "-Numero " + t.getNumero() + "\n" +
                            "-Titular " + t.getTitular() + "\n" +
                            "-Limite de Credito " + t.getLimiteCredito() + "\n"
                    )
            );

            System.out.println("----------------------------------------------------" +
                    "CONSULTAR TODOS LOS CONSUMOS" +
                    "----------------------------------------------------");
            consumoDAO.findAllConTarjeta().forEach(t ->
                    System.out.println("-ID " + t.getId() + "\n" +
                            "-ID Tarjeta " + t.getTarjeta().getId() + "\n" +
                            "-Monoto " + t.getMonto() + "\n" +
                            "-Dia " + t.getDia() + "\n" +
                            "-Mes " + t.getMes() + "\n" +
                            "-Anio " + t.getAnio() + "\n" +
                            "-Rubro " + t.getRubro() + "\n" +
                            "-Moneda " + t.getMoneda() + "\n"
                    )
            );

            System.out.println("----------------------------------------------------" +
                    "CONSULTAR TODOS LAS LIQUIDACIONES" +
                    "----------------------------------------------------");
            liquidacionDAO.findAllConTarjeta().forEach(t ->
                    System.out.println("-ID " + t.getId() + "\n" +
                            "-ID Tarjeta " + t.getTarjeta().getId() + "\n" +
                            "-Mes " + t.getMes() + "\n" +
                            "-Anio " + t.getAnio() + "\n" +
                            "-Total a Pagar " + t.getTotalAPagar() + "\n" +
                            "-Total de Consumos " + t.getTotalConsumos() + "\n" +
                            "-Total de Impuestos " + t.getTotalImpustos() + "\n" +
                            "-Total de Descuentos " + t.getTotalDescuentos() + "\n"
                    )
            );

            System.out.println("----------------------------------------------------" +
                    "CREAR TABLAS" +
                    "----------------------------------------------------");

            Tarjeta nuevaTar = Tarjeta.builder()
                    .numero("4500123412340099")
                    .titular("Ejemplo Parcial")
                    .limiteCredito(500000.0)
                    .build();
            tarjetaDAO.create(nuevaTar);
            System.out.println(tarjetaDAO.findById(nuevaTar.getId()));

            Consumo nuevoCons = Consumo.builder()
                    .tarjeta(nuevaTar)
                    .monto(10000.0)
                    .dia((short) 17)
                    .mes((short) 11)
                    .anio(2026)
                    .rubro("Soborno Parcial")
                    .moneda("EUR")
                    .build();
            consumoDAO.create(nuevoCons);
            System.out.println(consumoDAO.findByIdConTarjeta(nuevoCons.getId()));

            System.out.println("----------------------------------------------------" +
                    "ACTUALIZAR TABLAS" +
                    "----------------------------------------------------");

            nuevaTar.setLimiteCredito(99999.0);
            tarjetaDAO.update(nuevaTar);
            System.out.println(tarjetaDAO.findById(nuevaTar.getId()));

            System.out.println("----------------------------------------------------" +
                    "BORRAR TABLAS" +
                    "----------------------------------------------------");

            consumoDAO.deleteById(nuevoCons.getId());
            System.out.println(consumoDAO.findAllConTarjeta());

            System.out.println("----------------------------------------------------" +
                    "BUSCAR TABLAS DE LOS CONSUMOS DE UNA TARJETA POR ANIO Y MES" +
                    "----------------------------------------------------");

            System.out.println(consumoDAO.findByTarjetaIdAnioMes(1, 2026,(short) 5));

            System.out.println("----------------------------------------------------" +
                    "BUSCAR TABLAS DE TARJETAS SIN LIQUIDACIONES EN UN ANIO Y MES" +
                    "----------------------------------------------------");

            System.out.println(tarjetaDAO.findByNotId(2026,(short) 4));

            System.out.println("----------------------------------------------------" +
                    "BUSCAR TABLAS DE LAS LIQUIDACIONES DE UNA TARJETA, ANIO Y MES ESPECIFICOS" +
                    "----------------------------------------------------");

            System.out.println(liquidacionDAO.findByTarjetaIdAnioMes(1, 2026,(short) 5));
        } finally {
            JpaUtil.close();
        }
    }
}
