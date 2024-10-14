package br.com.cliente.cliente.dtos;

import java.time.LocalDateTime;

import br.com.cliente.cliente.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class MilhasDto {
    private Long idMilhas;
    private LocalDateTime dataTransacao;
    private Double valorReais;
    private Integer quantidadesMilhas;
    private String descricao;
    private String codigoReserva;
    private TipoTransacao tipoTransacao;
}
