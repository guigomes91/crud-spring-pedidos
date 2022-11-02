package br.com.senior.pdv.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	 * @return PedidoDTO
	 */
	public PedidoDTO getById(UUID id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		if (pedido.isPresent()) {
			return new PedidoDTO(pedido.get());
		}
		
		return null;
	}

	/**
	 * 
	 * @param pedidoForm
	 * @param uriBuilder
	 * @return Pedido
	 */
	public Pedido cadastrar(PedidoForm pedidoForm) {
		if (pedidoForm.getEmissao() == null) {
			pedidoForm.setEmissao(new Date());
		}
		
		Pedido pedido = pedidoForm.converter();
		pedidoRepository.save(pedido);
		
		return pedido;
	}

	/**
	 * 
	 * @param id
	 * @param form
	 * @return PedidoDTO
	 */
	public PedidoDTO atualizar(UUID id, AtualizacaoPedidoForm form) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		List<PedidoItemDTO> itens = pedidoItemRepository.consultarPorPedido(id);
		
		/**
		 * Verifica se existe o pedido cadastrado e se o desconto foi informado
		 * e se não existe item no pedido, não é permitido o desconto
		 */
		if (optional.isPresent()) {
			if (form.getDesconto() > 0 
					&& itens == null || itens.isEmpty()) {
				return null;
			}
			
			/**
			 * Se situação não estiver em ABERTO e desconto foi informado
			 * não é permitido o desconto
			 */
			if (!form.getStatus().equals("ABERTO") && form.getDesconto() > 0) {
				return null;
			}
			
			double totalDeDesconto = 0.0d, 
				   totalDoPedido = form.getTotal();
			
			/**
			 * Desconto foi informado e existe itens no pedido
			 * Realiza o desconto em cima do valor total dos itens
			 */
			if (form.getDesconto() > 0 && !itens.isEmpty()) {
				double percentualDesconto = (Double.valueOf(form.getDesconto()) / 100d),
						valorDoDesconto = 0.0, totalMenosDesconto = 0.0;
				
				for (PedidoItemDTO item : itens) {
					Item tipoItem = itemRepository.getItem(item.getIdProduto());
					
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
			return new PedidoDTO(pedido);
		}		
		
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean deletar(UUID id) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		Pedido pedido = optional.get();
		
		/**
		 * Não é permitido excluir pedido quando situação não esta em "ABERTO"
		 */
		if (optional.isPresent() && !pedido.getStatus().equals("ABERTO")) {
			return false;
		} 
		
		pedidoRepository.deleteById(id);
		return true;
	}

}
