package br.com.senior.pdv.controller;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.senior.pdv.dto.PedidoDTO;
import br.com.senior.pdv.form.PedidoForm;
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
		return service.getById(id);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<PedidoDTO> cadastrar(@RequestBody @Valid PedidoForm pedidoForm,
			UriComponentsBuilder uriBuilder) {
		return service.cadastrar(pedidoForm, uriBuilder); 
	}
}
