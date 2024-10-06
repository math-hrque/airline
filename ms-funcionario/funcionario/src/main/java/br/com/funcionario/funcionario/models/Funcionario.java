package br.com.funcionario.funcionario.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="funcionario")
public class Funcionario implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_funcionario")
    private Long idFuncionario = 0L;

    @Column(name="cpf", unique = true)
    private String cpf;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="telefone", nullable = false)
    private String telefone;

    @Column(name="ativo", insertable = false)
    private boolean ativo = true;
}
