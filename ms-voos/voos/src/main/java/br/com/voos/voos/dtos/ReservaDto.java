package br.com.voos.voos.dtos;

import java.time.LocalDateTime;

import br.com.voos.voos.enums.SiglaEstadoReserva;
import br.com.voos.voos.enums.TipoEstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDto {
    private String codigoReserva;
    private LocalDateTime dataReserva;
    private Double valorReserva;
    private Integer milhasUtilizadas;
    private Long idCliente;
    private SiglaEstadoReserva siglaEstadoReserva;
    private TipoEstadoReserva tipoEstadoReserva;
    private VooDto voo;
}
