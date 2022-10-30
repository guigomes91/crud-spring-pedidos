package br.com.senior.pdv.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.modelo.PedidoItem;

public class PedidoDTO {

	private long cpf;
	private Date emissao;
	private double total;
	private int desconto;
	private String status;
	private List<PedidoItemDTO> itens = new ArrayList<>();

	public PedidoDTO() {}
	
	public PedidoDTO(Pedido pedido) {
		this.cpf = pedido.getCpf();
		this.emissao = pedido.getEmissao();
		this.total = pedido.getTotal();
		this.desconto = pedido.getDesconto();
		this.status = pedido.getStatus();
		
		if (pedido.getItens() != null) {
			this.itens = this.converterPedidoItemDTO(pedido.getItens());
		}
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

	public List<PedidoItemDTO> getItens() {
		return itens;
	}

	public static Page<PedidoDTO> converter(Page<Pedido> pedidos) {
		return pedidos.map(PedidoDTO::new);
	}
	
	private List<PedidoItemDTO> converterPedidoItemDTO(List<PedidoItem> itens) {
		return itens.stream().map(p -> new PedidoItemDTO(p)).collect(Collectors.toList());
	}
}
