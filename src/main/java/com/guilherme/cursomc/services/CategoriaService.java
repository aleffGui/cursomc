package com.guilherme.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Categoria;
import com.guilherme.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository cr;
	
	
	public Categoria findById(Integer id) {
		Optional<Categoria> obj = cr.findById(id);
		return obj.get();
	}
}
