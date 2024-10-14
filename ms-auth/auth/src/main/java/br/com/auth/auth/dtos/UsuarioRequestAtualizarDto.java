package br.com.auth.auth.dtos;

import br.com.auth.auth.enums.Tipo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestAtualizarDto {
    private Long id;

    @Email(message = "Email invalido")
    @Size(min = 9, max = 100, message = "Email deve ter entre 9 e 100 caracteres")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email deve conter apenas letras minusculas, numeros, sublinhados (_), hifens (-), e pontos (.)")
    private String oldEmail;

    @Email(message = "Email invalido")
    @Size(min = 9, max = 100, message = "Email deve ter entre 9 e 100 caracteres")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email deve conter apenas letras minusculas, numeros, sublinhados (_), hifens (-), e pontos (.)")
    private String email;

    private String senha;

    @NotNull(message = "Tipo de usuario eh obrigatorio")
    private Tipo tipo;
}
