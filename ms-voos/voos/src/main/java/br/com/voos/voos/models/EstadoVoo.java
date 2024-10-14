package br.com.voos.voos.models;

import java.io.Serializable;

import br.com.voos.voos.enums.TipoEstadoVoo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="estado_voo")
public class EstadoVoo implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_estado_voo")
    private Long idEstadoVoo = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_estado_voo", insertable = false, updatable = false, nullable = false, unique = true)
    private TipoEstadoVoo tipoEstadoVoo = TipoEstadoVoo.CONFIRMADO;
}
