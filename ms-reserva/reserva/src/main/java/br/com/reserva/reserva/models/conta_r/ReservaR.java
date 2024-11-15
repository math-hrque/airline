package br.com.reserva.reserva.models.conta_r;

import java.io.Serializable;
import java.time.OffsetDateTime;

import br.com.reserva.reserva.enums.SiglaEstadoReserva;
import br.com.reserva.reserva.enums.TipoEstadoReserva;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reserva")
public class ReservaR implements Serializable {
    @Id
    @Column(name = "codigo_reserva", length = 6, unique = true)
    private String codigoReserva;

    @Column(name = "codigo_voo", length = 8, nullable = false)
    private String codigoVoo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_reserva", nullable = false)
    private OffsetDateTime dataReserva;

    @Column(name="valor_reserva", nullable = false)
    private Double valorReserva;

    @Column(name="milhas_utilizadas", nullable = false)
    private Integer milhasUtilizadas;

    @Column(name="quantidade_poltronas", nullable = false)
    private Integer quantidadePoltronas;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "sigla_estado_reserva", nullable = false)
    private SiglaEstadoReserva siglaEstadoReserva = SiglaEstadoReserva.CONF;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado_reserva", nullable = false)
    private TipoEstadoReserva tipoEstadoReserva = TipoEstadoReserva.CONFIRMADO;
}
