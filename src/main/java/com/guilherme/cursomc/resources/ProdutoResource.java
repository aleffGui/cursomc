package com.guilherme.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.cursomc.domain.Produto;
import com.guilherme.cursomc.dtos.ProdutoDTO;
import com.guilherme.cursomc.resources.utils.URL;
import com.guilherme.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
	@Autowired
	ProdutoService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Produto> listar(@PathVariable Integer id) {
		Produto produto = service.findById(id);
		return ResponseEntity.ok().body(produto);
	}
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "0") String nome, 
			@RequestParam(value = "categorias", defaultValue = "0") String categorias, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "oderBY", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> list = service.search(nomeDecoded,ids,page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(listDto);
		
	}
}