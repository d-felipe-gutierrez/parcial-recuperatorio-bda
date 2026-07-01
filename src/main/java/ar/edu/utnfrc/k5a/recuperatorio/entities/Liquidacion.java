package ar.edu.utnfrc.k5a.recuperatorio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LIQUIDACIONES")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Liquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TARJETA", nullable = false)
    @ToString.Exclude
    private Tarjeta tarjeta;

    @Column(name = "MES")
    private short mes;

    @Column(name = "ANIO")
    private short anio;

    @Column(name = "TOTAL_A_PAGAR")
    private double totalAPagar;

    @Column(name = "TOTAL_CONSUMOS")
    private double totalConsumos;

    @Column(name = "TOTAL_IMPUESTOS")
    private double totalImpustos;

    @Column(name = "TOTAL_DESCUENTOS")
    private double totalDescuentos;
}
