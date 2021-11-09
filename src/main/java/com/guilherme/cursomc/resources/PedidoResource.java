package com.guilherme.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.cursomc.domain.Pedido;
import com.guilherme.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	PedidoService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> listar(@PathVariable Integer id) {
		Pedido pedido = service.findById(id);
		return ResponseEntity.ok().body(pedido);
	}
}
