package com.guilherme.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Categoria;
import com.guilherme.cursomc.repositories.CategoriaRepository;
import com.guilherme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository cr;
	
	
	public Categoria findById(Integer id) {
		Optional<Categoria> obj = cr.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class));
	}
	public Categoria insert(Categoria categoria) {
		return cr.save(categoria);
	}
	public Categoria update(Categoria obj, Integer id) {
		findById(id);
		return cr.save(obj);
	}
	public void deleteById(Integer id) {
		findById(id);
		try {
			cr.deleteById(id);	
		}catch(DataIntegrityViolationException e) {
			throw new com.guilherme.cursomc.services.exceptions.DataIntegrityViolationException("Não é possível excluir uma categoria que possui produtos!");
		}
		
		
	}
}
