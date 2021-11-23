package com.guilherme.cursomc.dtos;

import java.io.Serializable;

import com.guilherme.cursomc.domain.Estado;

public class EstadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;

	public EstadoDTO() {
		super();
	}

	public EstadoDTO(Estado obj) {
		id = obj.getId();
		nome = obj.getNome();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
