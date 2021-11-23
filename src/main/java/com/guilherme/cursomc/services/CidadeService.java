package com.guilherme.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Cidade;
import com.guilherme.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> findCidades(Integer estadoId) {
		return cidadeRepository.findCidades(estadoId);
	}
}
