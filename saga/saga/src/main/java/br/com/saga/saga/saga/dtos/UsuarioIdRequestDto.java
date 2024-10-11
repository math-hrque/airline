package br.com.saga.saga.saga.dtos;

import br.com.saga.saga.saga.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioIdRequestDto {
    Long id;
    String oldEmail;
    String email;
    String senha = "";
    Tipo tipo;
}
