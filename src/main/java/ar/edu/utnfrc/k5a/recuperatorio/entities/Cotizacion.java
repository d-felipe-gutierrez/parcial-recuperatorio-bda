package ar.edu.utnfrc.k5a.recuperatorio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COTIZACION")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "moneda")
public class Cotizacion {
    @Id
    @Column(name = "MONEDA", length = 3)
    private String moneda;

    @Column(name = "TASA_CAMBIO", nullable = false)
    private double tasaCambio;
}
