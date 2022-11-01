package br.com.senior.pdv.controller;

import java.util.Optional;
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

import br.com.senior.pdv.dto.ItemDTO;
import br.com.senior.pdv.form.ItemForm;
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.service.ItemServiceImpl;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemServiceImpl service;
	
	@GetMapping("/listar")
	public Page<ItemDTO> listar(
			@RequestParam(required = false) UUID id,
			@PageableDefault(sort = "descricao", 
							 direction = Direction.ASC,
							 page = 0,
							 size = 10) Pageable paginacao) {
		return service.getItens(id, paginacao);
	}
		
	@GetMapping("/{id}")
	public ResponseEntity<ItemDTO> listarPorId(@PathVariable UUID id) {
		Optional<Item> item = service.getById(id);
		
		if (item.isPresent()) {
			return ResponseEntity.ok(new ItemDTO(item.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<ItemDTO> cadastrar(@RequestBody @Valid ItemForm itemForm,
			UriComponentsBuilder uriBuilder) {
		return service.cadastrar(itemForm, uriBuilder); 
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ItemDTO> atualizar(@PathVariable UUID id,
			@RequestBody @Valid ItemForm form) {
		ItemDTO itemDTO = service.atualizar(id, form);
		
		if (itemDTO != null) {
			return ResponseEntity.ok(itemDTO);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletar(@PathVariable UUID id) {
		boolean status = service.deletar(id);
		
		if (status) {
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.badRequest().build();
	}
}
