package com.guilherme.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Categoria;
import com.guilherme.cursomc.dtos.CategoriaDTO;
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
		categoria.setId(null);
		return cr.save(categoria);
	}
	public Categoria update(Categoria obj, Integer id) {
		Categoria newObj = findById(id);
		System.out.println(newObj);
		updateData(newObj, obj);
		return cr.save(newObj);
	}
	public void deleteById(Integer id) {
		findById(id);
		try {
			cr.deleteById(id);	
		}catch(DataIntegrityViolationException e) {
			throw new com.guilherme.cursomc.services.exceptions.DataIntegrityViolationException("Não é possível excluir uma categoria que possui produtos!");
		}
	}
	public List<Categoria> findAll() {
		return cr.findAll();
	}
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return cr.findAll(pageRequest);
	}
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
