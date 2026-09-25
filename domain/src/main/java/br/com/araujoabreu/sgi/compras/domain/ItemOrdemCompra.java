package br.com.araujoabreu.sgi.compras.domain;

import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * Entidade transiente para armazenar os itens de uma Ordem de Compra.
 *
 * @author Marcus Lacerda
 * @author Rodrigo Barreiros
 * @since 1.0
 */
public class ItemOrdemCompra {

	private Long codigo;
	private ItemCompra itemCompra;
	private BigDecimal quantidade;
	private BigDecimal preco;
	private String marca;
	private String descricao;
	private Boolean selecionado;

	public ItemOrdemCompra(ItemCompra itemCompra, BigDecimal quantidade, String marca, String descricao) {
		this.itemCompra = itemCompra;
		this.quantidade = quantidade;
		this.marca = marca;
		this.descricao = descricao;
	}

	public ItemOrdemCompra(ItemCompra itemCompra, BigDecimal quantidade, String marca, BigDecimal preco, String descricao) {
		this.itemCompra = itemCompra;
		this.marca = marca;
		this.quantidade = quantidade;
		this.preco = preco;
		this.descricao = descricao;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
}
