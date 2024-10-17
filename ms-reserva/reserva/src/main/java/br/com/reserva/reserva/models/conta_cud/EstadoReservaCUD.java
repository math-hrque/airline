package br.com.reserva.reserva.models.conta_cud;

import java.io.Serializable;

import br.com.reserva.reserva.enums.SiglaEstadoReserva;
import br.com.reserva.reserva.enums.TipoEstadoReserva;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="estado_reserva")
public class EstadoReservaCUD implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_estado_reserva")
    private Long idEstadoReserva = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name="sigla_estado_reserva", insertable = false, updatable = false, nullable = false, unique = true)
    private SiglaEstadoReserva siglaEstadoReserva = SiglaEstadoReserva.CONF;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_estado_reserva", insertable = false, updatable = false, nullable = false, unique = true)
    private TipoEstadoReserva tipoEstadoReserva = TipoEstadoReserva.CONFIRMADO;
}
