package br.com.cliente.cliente.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="milhas")
public class Milhas implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_milhas")
    private Long idMilhas = 0L;

    @CreationTimestamp
    @Column(name = "data_transacao", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime dataTransacao;

    @Column(name="valor_reais", nullable = false)
    private Double valorReais;

    @Column(name="quantidade_milhas", nullable = false)
    private Integer quantidadeMilhas;

    @Column(name="descricao", nullable = false)
    private String descricao = "COMPRA DE MILHAS";

    @Column(name="codigo_reserva")
    private String codigoReserva;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_transacao", updatable = false, nullable = false)
    private Transacao transacao;
}
