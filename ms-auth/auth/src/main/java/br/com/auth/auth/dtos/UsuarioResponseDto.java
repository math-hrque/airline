package br.com.auth.auth.dtos;

import br.com.auth.auth.enums.Tipo;
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
