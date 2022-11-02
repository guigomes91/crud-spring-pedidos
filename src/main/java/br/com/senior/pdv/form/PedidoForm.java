package br.com.senior.pdv.form;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.PedidoRepository;

public class PedidoForm {

	@NotNull
	private long cpf;

	private Date emissao;

	private double total;
	private int desconto;
	
	@NotNull @NotEmpty
	private String status;
	
	public PedidoForm() {}

	public PedidoForm(@NotNull long cpf, Date emissao, double total, int desconto,
			@NotNull @NotEmpty String status) {
		this.cpf = cpf;
		this.emissao = emissao;
		this.total = total;
		this.desconto = desconto;
		this.status = status;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

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

	public Pedido atualizar(UUID id, PedidoRepository repository) {
		Pedido pedido = repository.getReferenceById(id);
		pedido.setCpf(cpf);
		pedido.setDesconto(desconto);
		pedido.setEmissao(emissao);
		pedido.setStatus(status);
		pedido.setTotal(total);
		
		return pedido;
	}
}
