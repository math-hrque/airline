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
@Table(name="reserva")
public class ReservaCUD implements Serializable {
    @Id
    @Column(name = "codigo_reserva", length = 6)
    private String codigoReserva;

    @Column(name = "codigo_voo", length = 8, nullable = false)
    private String codigoVoo;

    @CreationTimestamp
    @Column(name = "data_reserva", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime dataReserva;

    @Column(name="valor_reserva", nullable = false)
    private Double valorReserva;

    @Column(name="milhas_utilizadas", nullable = false)
    private Integer milhasUtilizadas;

    @Column(name="quantidade_poltronas", nullable = false)
    private Integer quantidadePoltronas;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "id_estado_reserva", nullable = false)
    private EstadoReservaCUD estadoReserva;
}
