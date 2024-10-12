package br.com.cliente.cliente.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.br.CPF;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    private Long idCliente = 0L;

    @CPF(message = "CPF invalido")
    private String cpf;

    @Email(message = "Email invalido")
    @Size(min = 9, max = 100, message = "Email deve ter entre 9 e 100 caracteres")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email deve conter apenas letras minusculas, numeros, sublinhados (_), hifens (-), e pontos (.)")
    private String email;

    @NotBlank(message = "Nome e obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s]+$", message = "Nome invalido")
    @Size(min = 4, max = 100, message = "Nome deve ter entre 4 e 100 caracteres")
    private String nome;

    @Min(value = 0, message = "SaldoMilhas deve ser numeros positivo")
    private Integer saldoMilhas = 0;

    @Valid
    private EnderecoDto endereco;
}
