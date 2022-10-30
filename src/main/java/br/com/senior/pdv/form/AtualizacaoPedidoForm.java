package br.com.senior.pdv.form;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.PedidoRepository;

public class AtualizacaoPedidoForm {
	
	@NotNull
	private long cpf;

	private double total;
	private int desconto;
	
	@NotNull @NotEmpty
	private String status;

	public long getCpf() {
		return cpf;
	}

	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}

	public int getDesconto() {
		return desconto;
	}

	public String getStatus() {
		return status;
	}

	public Pedido converter() {
		Pedido pedido = new Pedido();
		
		pedido.setCpf(cpf);
		pedido.setDesconto(desconto);
		pedido.setStatus(status);
		pedido.setTotal(total);
		
		return pedido;
	}

	public Pedido atualizar(UUID id, PedidoRepository repository) {
		Pedido pedido = repository.getReferenceById(id);
		pedido.setCpf(cpf);
		pedido.setDesconto(desconto);
		pedido.setStatus(status);
		pedido.setTotal(total);
		
		return pedido;
	}
}
