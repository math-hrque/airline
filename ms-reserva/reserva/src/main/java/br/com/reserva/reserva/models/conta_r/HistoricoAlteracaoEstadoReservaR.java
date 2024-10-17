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
@Table(name="historico_alteracao_estado_reserva")
public class HistoricoAlteracaoEstadoReservaR implements Serializable {
    @Id
    @Column(name="historico_alteracao_estado_reserva", unique = true)
    private Long idHistoricoAlteracaoEstadoReserva;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_alteracao_estado_reserva", nullable = false)
    private OffsetDateTime dataAlteracaoEstadoReserva;

    @Column(name = "codigo_reserva", length = 6, unique = true)
    private String codigoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "sigla_estado_reserva_origem", nullable = false)
    private SiglaEstadoReserva siglaEstadoReservaOrigem = SiglaEstadoReserva.CONF;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado_reserva_origem", nullable = false)
    private TipoEstadoReserva tipoEstadoReservaOrigem = TipoEstadoReserva.CONFIRMADO;

    @Enumerated(EnumType.STRING)
    @Column(name = "sigla_estado_reserva_destino", nullable = false)
    private SiglaEstadoReserva siglaEstadoReservaDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_estado_reserva_destino", nullable = false)
    private TipoEstadoReserva tipoEstadoReservaDestino;
}
