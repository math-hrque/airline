package br.com.voos.voos.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="voo")
public class Voo implements Serializable {
    @Id
    @Column(name = "codigo_voo", length = 8)
    private String codigoVoo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="data_voo", nullable = false)
    private OffsetDateTime dataVoo;

    @Column(name="valor_passagem", nullable = false)
    private Double valorPassagem;

    @Column(name="quantidade_poltronas_total", nullable = false)
    private Integer quantidadePoltronasTotal;

    @Column(name="quantidade_poltronas_ocupadas", insertable = false, nullable = false)
    private Integer quantidadePoltronasOcupadas;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "codigo_aeroporto_origem", nullable = false)
    private Aeroporto aeroportoOrigem;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "codigo_aeroporto_destino", nullable = false)
    private Aeroporto aeroportoDestino;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_estado_voo", nullable = false)
    private EstadoVoo estadoVoo;
}
