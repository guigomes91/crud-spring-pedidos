package br.com.senior.pdv.controller;

import java.net.URI;
import java.util.List;
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

import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.form.AtualizacaoPedidoItemForm;
import br.com.senior.pdv.form.PedidoItemForm;
import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.service.PedidoItemServiceImpl;

@RestController
@RequestMapping("/pedidoitem")
public class PedidoItemController {

	@Autowired
	PedidoItemServiceImpl service;
	
	@GetMapping("/listar")
	public Page<PedidoItemDTO> listar(
			@RequestParam(required = false) UUID id,
			@PageableDefault(sort = "item_id", 
							 direction = Direction.ASC,
							 page = 0,
							 size = 10) Pageable paginacao) {
		return service.getPedidoItens(id, paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<PedidoItemDTO>> listarPorPedido(@PathVariable UUID id) {
		if (id == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return service.getByPedido(id);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<PedidoItemDTO> cadastrar(@RequestBody @Valid PedidoItemForm pedidoItemForm,
			UriComponentsBuilder uriBuilder) {
		
		PedidoItem pedidoItem = pedidoItemForm.converter();
		PedidoItemDTO pedidoItemDTO = service.cadastrar(pedidoItem); 
		
		if (pedidoItemDTO == null) {
			return ResponseEntity.badRequest().build();
		}
		
		URI uri = uriBuilder.path("/pedidoitem/{id}").buildAndExpand(pedidoItem.getId()).toUri();
		return ResponseEntity.created(uri).body(new PedidoItemDTO(pedidoItem));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<PedidoItemDTO> atualizar(@PathVariable UUID id,
			@RequestBody @Valid AtualizacaoPedidoItemForm form) {
		return service.atualizar(id, form);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletar(@PathVariable UUID id) {
		return service.deletar(id);
	}
}
