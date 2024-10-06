package br.com.cliente.cliente.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cliente")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_cliente")
    private Long idCliente = 0L;

    @Column(name="cpf", unique = true)
    private String cpf;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="saldo_milhas", insertable = false, nullable = false)
    private Integer saldoMilhas;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_endereco", nullable = false)
    private Endereco endereco;
}
