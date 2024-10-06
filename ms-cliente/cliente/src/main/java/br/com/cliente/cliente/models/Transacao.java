package br.com.cliente.cliente.models;

import java.io.Serializable;

import br.com.cliente.cliente.enums.TipoTransacao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="transacao")
public class Transacao implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_transacao")
    private Long idTransacao = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_transacao", insertable = false, updatable = false, nullable = false, unique = true)
    private TipoTransacao tipoTransacao = TipoTransacao.ENTRADA;
}
