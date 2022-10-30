package br.com.senior.pdv.modelo;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, unique = true, nullable = false)
	private UUID id;

	@Column(name = "descricao", nullable = false, length = 60)
	private String descricao;

	@Column(name = "valor", nullable = false)
	private double valor;

	@Column(name = "tipo", nullable = false)
	private int tipo;
	
	@Column(name = "situacao", nullable = false)
	private boolean situacao;

	public Item() {}

	public Item(UUID id, String descricao, double valor, int tipo, boolean situacao) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.tipo = tipo;
		this.situacao = situacao;
	}
	
	public Item(String descricao, double valor, int tipo, boolean situacao) {
		this.descricao = descricao;
		this.valor = valor;
		this.tipo = tipo;
		this.situacao = situacao;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public boolean isSituacao() {
		return situacao;
	}
}
