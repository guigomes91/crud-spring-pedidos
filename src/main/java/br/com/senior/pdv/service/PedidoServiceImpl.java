package br.com.senior.pdv.service;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.senior.pdv.dto.PedidoDTO;
import br.com.senior.pdv.form.PedidoForm;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.PedidoRepository;

@Service
public class PedidoServiceImpl {

	@Autowired
	PedidoRepository repository;
	
	public Page<PedidoDTO> getPedidos(UUID id, Pageable paginacao) {
		Page<Pedido> pedidos = null;
		if (id == null) {
			pedidos = repository.findAll(paginacao);
			return PedidoDTO.converter(pedidos);
		} else {
			pedidos = repository.findByIdPaginacao(id, paginacao);
			return PedidoDTO.converter(pedidos);
		}
	}

	public ResponseEntity<PedidoDTO> getById(UUID id) {
		Optional<Pedido> pedido = repository.findById(id);
		
		if (pedido.isPresent()) {
			return ResponseEntity.ok(new PedidoDTO(pedido.get()));
		}
		
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<PedidoDTO> cadastrar(@Valid PedidoForm pedidoForm, UriComponentsBuilder uriBuilder) {
		Pedido pedido = pedidoForm.converter();
		repository.save(pedido);
		
		URI uri = uriBuilder.path("/pedido/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(new PedidoDTO(pedido)); 
	}

}
