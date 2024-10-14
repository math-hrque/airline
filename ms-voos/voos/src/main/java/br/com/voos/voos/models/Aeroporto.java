package br.com.voos.voos.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="aeroporto")
public class Aeroporto implements Serializable {
    @Id
    @Column(name = "codigo_aeroporto", length = 3)
    private String codigoAeroporto;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cidade", nullable = false)
    private String cidade;

    @Column(name="estado", nullable = false)
    private String estado;
}
