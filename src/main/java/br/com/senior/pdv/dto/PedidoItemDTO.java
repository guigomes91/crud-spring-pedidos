package br.com.senior.pdv.dto;

import java.util.UUID;

import br.com.senior.pdv.modelo.PedidoItem;

public class PedidoItemDTO {

	private UUID idProduto;
	private UUID idPedido;
	private int quantidade;
	private double total;
	private double desconto;
	
	public PedidoItemDTO() {}
	
	public PedidoItemDTO(PedidoItem pedido) {
		this.idPedido = pedido.getPedido().getId();
		this.idProduto = pedido.getItem().getId();
		this.quantidade = pedido.getQuantidade();
		this.total = pedido.getTotal();
		this.desconto = pedido.getDesconto();
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
	
	
}
