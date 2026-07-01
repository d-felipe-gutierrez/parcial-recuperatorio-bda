package ar.edu.utnfrc.k5a.recuperatorio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TARJETA")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ID")
    private int id;

    @Column(name= "NUMERO", nullable = false)
    private long numero;

    @Column(name = "TITULAR", length = 40, nullable = false)
    private String titular;

    @Column(name = "LIMITE_CREDITO", nullable = false)
    private double limiteCredito;
}
