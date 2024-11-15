package br.com.reserva.reserva.models.conta_cud;

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
public class HistoricoAlteracaoEstadoReservaCUD implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_historico_alteracao_estado_reserva")
    private Long idHistoricoAlteracaoEstadoReserva = 0L;

    @CreationTimestamp
    @Column(name = "data_alteracao_estado_reserva", nullable = false)
    private OffsetDateTime dataAlteracaoEstadoReserva = OffsetDateTime.now();

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "codigo_reserva", unique = true)
    private ReservaCUD reserva;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_estado_reserva_origem", nullable = false)
    private EstadoReservaCUD estadoReservaOrigem;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_estado_reserva_destino", nullable = false)
    private EstadoReservaCUD estadoReservaDestino;
}
