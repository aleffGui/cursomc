package com.guilherme.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Estado;
import com.guilherme.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	EstadoRepository estadoRepository;
	
	public List<Estado> findAll() {
		return estadoRepository.findAllByOrderByNome();
	}
	
}
