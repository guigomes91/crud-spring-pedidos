package br.com.senior.pdv.dto;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.senior.pdv.modelo.Item;

public class ItemDTO {

	private UUID id;
	private String descricao;
	private int tipo;
	private double valor;
	private boolean situacao;

	public ItemDTO(Item item) {
		this.id = item.getId();
		this.descricao = item.getDescricao();
		this.tipo = item.getTipo();
		this.valor = item.getValor();
		this.situacao = item.isSituacao();
	}
	
	public UUID getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getTipo() {
		return tipo;
	}

	public double getValor() {
		return valor;
	}

	public boolean getSituacao() {
		return situacao;
	}
	
	public static Page<ItemDTO> converter(Page<Item> itens) {
		return itens.map(ItemDTO::new);
	}
}
