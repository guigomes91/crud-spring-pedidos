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
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;

@Service
public class PedidoServiceImpl {

	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	PedidoItemRepository pedidoItemRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * 
	 * @param id
	 * @param paginacao
	 * @return
	 */
	public Page<PedidoDTO> getPedidos(UUID id, Pageable paginacao) {
		Page<Pedido> pedidos = null;
		if (id == null) {
			pedidos = pedidoRepository.findAll(paginacao);
			return PedidoDTO.converter(pedidos);
		} else {
			pedidos = pedidoRepository.findByIdPaginacao(id, paginacao);
			return PedidoDTO.converter(pedidos);
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ResponseEntity<PedidoDTO> getById(UUID id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
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
		pedidoRepository.save(pedido);
		
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
		Optional<Pedido> optional = pedidoRepository.findById(id);
		List<PedidoItemDTO> itens = pedidoItemRepository.consultarPorPedido(id);
		
		if (optional.isPresent()) {
			if (form.getDesconto() > 0 
					&& itens.size() < 0) {
				return ResponseEntity.notFound().build();
			}
			
			if (!form.getStatus().equals("ABERTO") && form.getDesconto() > 0) {
				return ResponseEntity.badRequest().build();
			}
			
			double totalDeDesconto = 0.0d, 
				   totalDoPedido = form.getTotal();
			
			if (form.getDesconto() > 0 && itens.size() >= 0) {
				double percentualDesconto = (Double.valueOf(form.getDesconto()) / 100d),
						valorDoDesconto = 0.0, totalMenosDesconto = 0.0;
				
				for (PedidoItemDTO item : itens) {
					Item tipoItem = itemRepository.getReferenceById(item.getIdProduto());
					
					if (tipoItem.getTipo() == 1) {
						valorDoDesconto = item.getTotal() * percentualDesconto;
						totalMenosDesconto = item.getTotal() - valorDoDesconto;
						pedidoItemRepository.atualizarDescontoItem(item.getQuantidade(), 
															valorDoDesconto, 
															totalMenosDesconto, 
															item.getId());
						totalDeDesconto += valorDoDesconto;
					}
				}
			}
			
			totalDoPedido = totalDoPedido - totalDeDesconto;
			form.setTotal(totalDeDesconto == 0 ? form.getTotal() : totalDoPedido);
			Pedido pedido = form.atualizar(id, pedidoRepository);
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
		Optional<Pedido> optional = pedidoRepository.findById(id);
		
		if (optional.isPresent()) {
			pedidoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}
