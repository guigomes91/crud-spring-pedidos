package br.com.senior.pdv.form;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.repository.ItemRepository;

public class ItemForm {

	@NotNull @NotEmpty
	private String descricao;
	
	@NotNull
	private int tipo;
	
	@NotNull
	private double valor;
	
	@NotNull
	private boolean situacao;
	
	public Item converter() {
		return new Item(descricao, valor, tipo, situacao);
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

	public Item atualizar(UUID id, ItemRepository itemRepository) {
		Item item = itemRepository.getReferenceById(id);
		item.setDescricao(descricao);
		item.setTipo(tipo);
		item.setValor(valor);
		item.setSituacao(situacao);
		
		return item;
	}
}
