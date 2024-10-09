package br.com.saga.saga.saga.dtos;

import br.com.saga.saga.saga.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {
    String email;
    Tipo tipo;
}
