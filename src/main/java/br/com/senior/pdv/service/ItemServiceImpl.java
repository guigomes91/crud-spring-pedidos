package br.com.senior.pdv.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.senior.pdv.dto.ItemDTO;
import br.com.senior.pdv.form.ItemForm;
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;

@Service
public class ItemServiceImpl {

	@Autowired
	ItemRepository repository;
	
	@Autowired
	PedidoItemRepository pedidoItemRepository;
	
	/**
	 * 
	 * @param id
	 * @param paginacao
	 * @return
	 */
	public Page<ItemDTO> getItens(UUID id, Pageable paginacao) {
		Page<Item> itens = null;
		if (id == null) {
			itens = repository.findAll(paginacao);
			return ItemDTO.converter(itens);
		} else {
			itens = repository.findByIdPaginacao(id, paginacao);
			return ItemDTO.converter(itens);
		}
	}
	
	/**
	 * 
	 * @param id será consultado o item em especifíco
	 * @return Item
	 */
	public Optional<Item> getById(UUID id) {
		Optional<Item> item = repository.findById(id);
		return item;
	}
	
	/**
	 * 
	 * @param itemForm
	 * @param uriBuilder
	 * @return Item
	 */
	public Item cadastrar(ItemForm itemForm) {
		Item item = itemForm.converter();
		repository.save(item);
		
		return item;
	}
	
	/**
	 * 
	 * @param id
	 * @param form ItemForm dados enviados pela requisição
	 * @return
	 */
	public ItemDTO atualizar(UUID id, ItemForm form) {
		Optional<Item> optional = repository.findById(id);
		
		if (optional.isPresent()) {
			Item item = form.atualizar(id, repository);
			return new ItemDTO(item);
		}
		
		return null;
	}

	/**
	 * 
	 * @param id do produto ao qual será deletado
	 * @return
	 */
	public boolean deletar(UUID id) {
		Optional<Item> optional = repository.findById(id);
		String itemEmPedido = pedidoItemRepository.itemEmPedido(id);
		
		if (optional.isPresent() && itemEmPedido == null) {
			repository.deleteById(id);
			return true;
		}
		
		return false;
	}
}
