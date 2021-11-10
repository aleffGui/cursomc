package com.guilherme.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guilherme.cursomc.domain.Cidade;
import com.guilherme.cursomc.domain.Cliente;
import com.guilherme.cursomc.domain.Endereco;
import com.guilherme.cursomc.dtos.ClienteDTO;
import com.guilherme.cursomc.dtos.ClienteNewDTO;
import com.guilherme.cursomc.enums.TipoCliente;
import com.guilherme.cursomc.repositories.ClienteRepository;
import com.guilherme.cursomc.repositories.EnderecoRepository;
import com.guilherme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class));
	}
	public Cliente update(Cliente obj, Integer id) {
		Cliente newObj = findById(id);
		System.out.println(newObj);
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	public void deleteById(Integer id) {
		findById(id);
		try {
			clienteRepository.deleteById(id);	
		}catch(DataIntegrityViolationException e) {
			throw new com.guilherme.cursomc.services.exceptions.DataIntegrityViolationException("Não é possível excluir porque há entidades relacionadas!");
		}
	}
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null,null);
	}
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
}
