package br.com.senior.pdv.service;

import java.net.URI;
import java.util.List;
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
import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.form.AtualizacaoPedidoForm;
import br.com.senior.pdv.form.PedidoForm;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;

@Service
public class PedidoServiceImpl {

	@Autowired
	PedidoRepository repository;
	
	@Autowired
	PedidoItemRepository itemRepository;
	
	/**
	 * 
	 * @param id
	 * @param paginacao
	 * @return
	 */
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

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ResponseEntity<PedidoDTO> getById(UUID id) {
		Optional<Pedido> pedido = repository.findById(id);
		
		if (pedido.isPresent()) {
			return ResponseEntity.ok(new PedidoDTO(pedido.get()));
		}
		
		return ResponseEntity.notFound().build();
	}

	/**
	 * 
	 * @param pedidoForm
	 * @param uriBuilder
	 * @return
	 */
	public ResponseEntity<PedidoDTO> cadastrar(@Valid PedidoForm pedidoForm, UriComponentsBuilder uriBuilder) {
		Pedido pedido = pedidoForm.converter();
		repository.save(pedido);
		
		URI uri = uriBuilder.path("/pedido/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(new PedidoDTO(pedido)); 
	}

	/**
	 * 
	 * @param id
	 * @param form
	 * @return
	 */
	public ResponseEntity<PedidoDTO> atualizar(UUID id, @Valid AtualizacaoPedidoForm form) {
		Optional<Pedido> optional = repository.findById(id);
		List<PedidoItemDTO> itens = itemRepository.consultarPorPedido(id);
		
		if (optional.isPresent()) {
			if (form.getDesconto() > 0 
					&& itens.size() < 0) {
				return ResponseEntity.notFound().build();
			}
			
			if (!form.getStatus().equals("ABERTO") && form.getDesconto() > 0) {
				return ResponseEntity.notFound().build();
			}
			
			double novoValorTotalDoPedido = 0.0d;
			if (form.getDesconto() > 0 && itens.size() >= 0) {
				double percentualDesconto = (Double.valueOf(form.getDesconto()) / 100d),
						valorDoDesconto = 0.0, totalMenosDesconto = 0.0;
				
				for (PedidoItemDTO item : itens) {
					valorDoDesconto = item.getTotal() * percentualDesconto;
					totalMenosDesconto = item.getTotal() - valorDoDesconto;
					itemRepository.atualizarDescontoItem(item.getQuantidade(), 
														valorDoDesconto, 
														totalMenosDesconto, 
														item.getId());
					novoValorTotalDoPedido += totalMenosDesconto;
				}
			}
			
			form.setTotal(novoValorTotalDoPedido == 0 ? form.getTotal() : novoValorTotalDoPedido);
			Pedido pedido = form.atualizar(id, repository);
			return ResponseEntity.ok(new PedidoDTO(pedido));
		}		
		
		return ResponseEntity.notFound().build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ResponseEntity<?> deletar(UUID id) {
		Optional<Pedido> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}
