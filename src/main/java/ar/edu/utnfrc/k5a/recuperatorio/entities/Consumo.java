package ar.edu.utnfrc.k5a.recuperatorio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CONSUMOS")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Consumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TARJETA", nullable = false)
    @ToString.Exclude
    private Tarjeta tarjeta;

    @Column(name = "MONTO", nullable = false)
    private double monto;

    @Column(name = "DIA", nullable = false)
    private short dia;

    @Column(name = "MES", nullable = false)
    private short mes;

    @Column(name = "ANIO", nullable = false)
    private int anio;

    @Column(name = "RUBRO", length = 60, nullable = false)
    private String rubro;

    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;
}
