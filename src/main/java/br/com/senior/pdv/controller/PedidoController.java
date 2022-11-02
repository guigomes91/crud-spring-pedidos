package br.com.senior.pdv.controller;

import java.net.URI;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.senior.pdv.dto.PedidoDTO;
import br.com.senior.pdv.form.AtualizacaoPedidoForm;
import br.com.senior.pdv.form.PedidoForm;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.service.PedidoServiceImpl;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	PedidoServiceImpl service;
	
	@GetMapping("/listar")
	public Page<PedidoDTO> listar(
			@RequestParam(required = false) UUID id,
			@PageableDefault(sort = "emissao", 
							 direction = Direction.ASC,
							 page = 0,
							 size = 10) Pageable paginacao) {
		return service.getPedidos(id, paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> listarPorId(@PathVariable UUID id) {
		PedidoDTO pedidoDTO = service.getById(id);
		
		if (pedidoDTO == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(pedidoDTO);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<PedidoDTO> cadastrar(@RequestBody @Valid PedidoForm pedidoForm,
			UriComponentsBuilder uriBuilder) {
		Pedido pedido = service.cadastrar(pedidoForm);
		
		if (pedido != null) {
			URI uri = uriBuilder.path("/pedido/{id}").buildAndExpand(pedido.getId()).toUri();
			return ResponseEntity.created(uri).body(new PedidoDTO(pedido));
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<PedidoDTO> atualizar(@PathVariable UUID id,
			@RequestBody @Valid AtualizacaoPedidoForm form) {
		
		/**
		 * Desconto não permitido
		 */
		if (form.getDesconto() > 100) {
			return ResponseEntity.badRequest().build();
		}
		
		PedidoDTO pedidoDTO = service.atualizar(id, form);
		if (pedidoDTO == null) {
			return  ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(pedidoDTO);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletar(@PathVariable UUID id) {
		boolean retorno = service.deletar(id);
		
		if (retorno) {
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.badRequest().build(); 
	}
}
