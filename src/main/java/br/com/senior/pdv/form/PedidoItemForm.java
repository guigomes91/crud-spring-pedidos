package br.com.senior.pdv.form;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.repository.PedidoItemRepository;

public class PedidoItemForm {

	@NotNull
	private UUID idProduto;

	@NotNull
	private UUID idPedido;

	@NotNull
	private int quantidade;

	@NotNull
	private double total;

	private double desconto;

	public PedidoItemForm() {
	}

	public UUID getIdProduto() {
		return idProduto;
	}

	public UUID getIdPedido() {
		return idPedido;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public double getTotal() {
		return total;
	}

	public double getDesconto() {
		return desconto;
	}

	public PedidoItem converter() {
		PedidoItem pedidoItem = new PedidoItem();
		
		Pedido pedido = new Pedido();
		pedido.setId(idPedido);
		pedidoItem.setPedido(pedido);
		
		Item item = new Item();
		item.setId(idProduto);
		pedidoItem.setItem(item);
		
		pedidoItem.setQuantidade(quantidade);
		pedidoItem.setTotal(total);
		pedidoItem.setDesconto(desconto);
		
		return pedidoItem;
	}

	public PedidoItem atualizar(UUID id, PedidoItemRepository repository) {
		PedidoItem pedidoItem = repository.getReferenceById(id);
		
		Pedido pedido = new Pedido();
		pedido.setId(idPedido);
		pedidoItem.setPedido(pedido);
		
		Item item = new Item();
		item.setId(idProduto);
		pedidoItem.setItem(item);
		
		pedidoItem.setQuantidade(quantidade);
		pedidoItem.setTotal(total);
		pedidoItem.setDesconto(desconto);
		
		return pedidoItem;
	}
}
