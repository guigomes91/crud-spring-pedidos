package br.com.senior.pdv.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.form.AtualizacaoPedidoItemForm;
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;

@Service
public class PedidoItemServiceImpl {

	@Autowired
	PedidoItemRepository repository;
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	ItemRepository itemRepository;

	/**
	 * 
	 * @param id do item
	 * @param paginacao
	 * @return
	 */
	public Page<PedidoItemDTO> getPedidoItens(UUID id, Pageable paginacao) {
		Page<PedidoItem> itensPedido = null;
		if (id == null) {
			itensPedido = repository.findAll(paginacao);
			return PedidoItemDTO.converter(itensPedido);
		} else {
			itensPedido = repository.findByIdPaginacao(id, paginacao);
			return PedidoItemDTO.converter(itensPedido);
		}
	}

	/**
	 * 
	 * @param id do pedido
	 * @return
	 */
	public ResponseEntity<List<PedidoItemDTO>> getByPedido(UUID id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		if (pedido.isPresent()) {
			return ResponseEntity.ok(repository.consultarPorPedido(id));
		}
		
		return ResponseEntity.notFound().build();
	}

	/**
	 * 
	 * @param pedidoItemForm
	 * @param uriBuilder
	 * @return Retorno da requisição e o corpo com o PedidoItemDTO
	 */
	public PedidoItemDTO cadastrar(PedidoItem pedidoItem) {
		Optional<Pedido> pedido = pedidoRepository.findById(pedidoItem.getPedido().getId());
		Optional<Item> item = itemRepository.findById(pedidoItem.getItem().getId());
		
		/**
		 * Se existe o item cadastrado e a situação é inativa
		 * Não permite inserir o item no pedido
		 */
		if (item.isPresent()) {
			Item it = item.get();
			
			if (!it.getSituacao()) {
				return null;
			}
		}
		
		/**
		 * Se existe o pedido cadastrado
		 * Atualiza o valor total do pedido após inserir o item
		 */
		if (pedido.isPresent()) {
			repository.save(pedidoItem);
			
			double totalPedido = pedido.get().getTotal() + pedidoItem.getTotal();
			pedidoRepository.atualizarPedido(totalPedido, pedidoItem.getPedido().getId());

			return new PedidoItemDTO(pedidoItem);
		}

		return null;
	}

	/**
	 * 
	 * @param id do pedidoitem
	 * @param form
	 * @return
	 */
	public ResponseEntity<PedidoItemDTO> atualizar(UUID id, @Valid AtualizacaoPedidoItemForm form) {
		Optional<PedidoItem> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			PedidoItem pedidoItem = form.atualizar(id, repository);
			return ResponseEntity.ok(new PedidoItemDTO(pedidoItem));
		}
		
		return ResponseEntity.notFound().build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ResponseEntity<?> deletar(UUID id) {
		Optional<PedidoItem> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
