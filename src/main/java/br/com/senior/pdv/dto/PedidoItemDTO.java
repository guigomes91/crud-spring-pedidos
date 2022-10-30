package br.com.senior.pdv.dto;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.senior.pdv.modelo.PedidoItem;

public class PedidoItemDTO {

	private UUID id;
	private UUID idProduto;
	private String produto;
	private UUID idPedido;
	private int quantidade;
	private double total;
	private double desconto;

	public PedidoItemDTO() {
	}

	public PedidoItemDTO(PedidoItem pedidoItem) {
		this.id = pedidoItem.getId();
		this.idProduto = pedidoItem.getItem().getId();
		this.produto = pedidoItem.getItem().getDescricao();
		this.idPedido = pedidoItem.getPedido().getId();
		this.quantidade = pedidoItem.getQuantidade();
		this.total = pedidoItem.getTotal();
		this.desconto = pedidoItem.getDesconto();
	}
	
	public UUID getId() {
		return id;
	}

	public UUID getIdProduto() {
		return idProduto;
	}

	public String getProduto() {
		return produto;
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

	public static Page<PedidoItemDTO> converter(Page<PedidoItem> itensPedido) {
		return itensPedido.map(PedidoItemDTO::new);
	}
}
