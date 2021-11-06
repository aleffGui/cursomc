package com.guilherme.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guilherme.cursomc.domain.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
