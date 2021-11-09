package com.guilherme.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.guilherme.cursomc.domain.Categoria;
import com.guilherme.cursomc.domain.Cidade;
import com.guilherme.cursomc.domain.Cliente;
import com.guilherme.cursomc.domain.Endereco;
import com.guilherme.cursomc.domain.Estado;
import com.guilherme.cursomc.domain.ItemPedido;
import com.guilherme.cursomc.domain.Pagamento;
import com.guilherme.cursomc.domain.PagamentoComBoleto;
import com.guilherme.cursomc.domain.PagamentoComCartao;
import com.guilherme.cursomc.domain.Pedido;
import com.guilherme.cursomc.domain.Produto;
import com.guilherme.cursomc.enums.EstadoPagamento;
import com.guilherme.cursomc.enums.TipoCliente;
import com.guilherme.cursomc.repositories.CategoriaRepository;
import com.guilherme.cursomc.repositories.CidadeRepository;
import com.guilherme.cursomc.repositories.ClienteRepository;
import com.guilherme.cursomc.repositories.EnderecoRepository;
import com.guilherme.cursomc.repositories.EstadoRepository;
import com.guilherme.cursomc.repositories.ItemPedidoRepository;
import com.guilherme.cursomc.repositories.PagamentoRepository;
import com.guilherme.cursomc.repositories.PedidoRepository;
import com.guilherme.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	CidadeRepository cidadeRepository;
	
	@Autowired
	EstadoRepository estadoRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		Categoria cat3 = new Categoria(null, "Varejo");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2,cat3));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Guilherme Ferreira", "guilherme@gmail.com", "43947557457", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("4233054785", "42998474587"));
		
		Endereco end1 = new Endereco(null, "Rua Flores","300", "Apto 303", "Jardim", "85040540", cli1, c1);
		Endereco end2 = new Endereco(null, "Avenida Matos","105", "Sala 800", "Centro", "81050250", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(end1,end2));
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(end1,end2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/07/2021 10:30"), cli1, end1);
		Pedido ped2 = new Pedido(null, sdf.parse("25/12/2020 08:44"), cli1, end2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("08/11/2021 00:00"), null);
		
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1,ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1,pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItems().addAll(Arrays.asList(ip1,ip2));
		ped2.getItems().addAll(Arrays.asList(ip3));
		
		p1.getItems().addAll(Arrays.asList(ip1));
		p2.getItems().addAll(Arrays.asList(ip3));
		p3.getItems().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
