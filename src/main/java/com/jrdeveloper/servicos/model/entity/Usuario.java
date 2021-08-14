package com.jrdeveloper.servicos.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuario", schema="financas")
public class Usuario {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome")
    private String nome;

    @Column(name="email")
    private String email;

    @Column(name="senha")
    private String senha;

}
