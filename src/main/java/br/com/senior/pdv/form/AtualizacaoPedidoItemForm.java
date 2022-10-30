package br.com.senior.pdv.form;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.repository.PedidoItemRepository;

public class AtualizacaoPedidoItemForm {

	@NotNull
	private int quantidade;
	
	private double desconto;
	
	@NotNull
	private double total;

	public int getQuantidade() {
		return quantidade;
	}

	public double getDesconto() {
		return desconto;
	}

	public double getTotal() {
		return total;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public PedidoItem atualizar(UUID id, PedidoItemRepository repository) {
		PedidoItem pedidoItem = repository.getReferenceById(id);
		
		pedidoItem.setQuantidade(quantidade);
		pedidoItem.setTotal(total);
		pedidoItem.setDesconto(desconto);
		
		return pedidoItem;
	}
}
