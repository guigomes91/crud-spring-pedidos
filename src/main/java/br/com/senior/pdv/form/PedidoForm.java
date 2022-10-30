package br.com.senior.pdv.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.Pedido;

public class PedidoForm {

	@NotNull
	private long cpf;

	@NotNull
	private Date emissao;

	private double total;
	private int desconto;
	
	@NotNull @NotEmpty
	private String status;

	public long getCpf() {
		return cpf;
	}

	public Date getEmissao() {
		return emissao;
	}

	public double getTotal() {
		return total;
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
		pedido.setEmissao(emissao);
		pedido.setStatus(status);
		pedido.setTotal(total);
		
		return pedido;
	}
}
