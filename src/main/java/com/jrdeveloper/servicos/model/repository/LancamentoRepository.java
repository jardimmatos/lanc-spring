package com.jrdeveloper.servicos.model.repository;

import com.jrdeveloper.servicos.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
