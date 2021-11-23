package com.guilherme.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.guilherme.cursomc.domain.Cliente;
import com.guilherme.cursomc.domain.ItemPedido;
import com.guilherme.cursomc.domain.PagamentoComBoleto;
import com.guilherme.cursomc.domain.Pedido;
import com.guilherme.cursomc.domain.enums.EstadoPagamento;
import com.guilherme.cursomc.domain.enums.Perfil;
import com.guilherme.cursomc.repositories.ItemPedidoRepository;
import com.guilherme.cursomc.repositories.PagamentoRepository;
import com.guilherme.cursomc.repositories.PedidoRepository;
import com.guilherme.cursomc.security.UserSS;
import com.guilherme.cursomc.services.exceptions.AuthorizationException;
import com.guilherme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pr;
	
	@Autowired
	private BoletoService boletoService;
	
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	EmailService emailService;
	
	
	public Optional<Pedido> findById(Integer id) {
		Optional<Pedido> obj = pr.findById(id);
		if(obj == null || obj.isEmpty()) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class);
		}
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !obj.get().getCliente().getId().equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		return obj;
	}


	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = pr.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItems()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItems());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.findById(user.getId());
		return pr.findByCliente(cliente, pageRequest);
		
	}
}
