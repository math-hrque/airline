package br.com.reserva.reserva.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="historico_alteracao_estado_reserva")
public class HistoricoAlteracaoEstadoReserva implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="historico_alteracao_estado_reserva")
    private Long idHistoricoAlteracaoEstadoReserva = 0L;

    @CreationTimestamp
    @Column(name = "data_alteracao_estado_reserva", nullable = false)
    private OffsetDateTime dataAlteracaoEstadoReserva;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "codigo_reserva", updatable = false, unique = true)
    private Reserva reserva;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_estado_reserva_origem", nullable = false)
    private EstadoReserva estadoReservaOrigem;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_estado_reserva_destino", nullable = false)
    private EstadoReserva estadoReservaDestino;
}
