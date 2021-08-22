package com.jrdeveloper.servicos.model.entity;

import com.jrdeveloper.servicos.model.enums.StatusLancamento;
import com.jrdeveloper.servicos.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="lancamento", schema = "financas")
public class Lancamento {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mes")
    private Integer mes;

    @Column(name="ano")
    private Integer ano;

    @JoinColumn(name="id_usuario")
    @ManyToOne
    private Usuario usuario;

    @Column(name = "tipo")
    @Enumerated(value = EnumType.STRING) // CONSIDERA A STRING DEFINIDA NAS CLASSES ENUM
    //@Enumerated(value = EnumType.ORDINAL)  CONSIDERA A ORDEM DOS ENUMS
    private TipoLancamento tipo;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING) // CONSIDERA A STRING DEFINIDA NAS CLASSES ENUM
    private StatusLancamento status;

    @Column(name="valor")
    private BigDecimal valor;

    @Column(name="data_cadastro")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;

    @Column(name="descricao")
    private String descricao;


}
