package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "ITEM_COMPRA_CONTA_CONTABIL")
@SequenceGenerator(name = "SEQ_ITEM_COMPRA_CONTA_CONTABIL", sequenceName = "SEQ_ITEM_COMPRA_CONTA_CONTABIL", allocationSize = 1)
public class ItemCompraContaContabil extends BaseEntity {

	private static final long serialVersionUID = -5320133562580756186L;
	private ItemCompra itemCompra;
	private ContaContabil contaContabil;
	private TipoRelacionamentoItemCompra tipo;
	private Unidade unidade;
	private String numeroContaContabil;

	public ItemCompraContaContabil() {

	}

	public ItemCompraContaContabil(ItemCompra itemCompra, ContaContabil contaContabil, TipoRelacionamentoItemCompra tipo) {
		this.itemCompra = itemCompra;
		this.contaContabil = contaContabil;
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_COMPRA")
	@NotNull
	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	@NotNull
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoRelacionamentoItemCompra getTipo() {
		return tipo;
	}

	public void setTipo(TipoRelacionamentoItemCompra tipo) {
		this.tipo = tipo;
	}

	@Transient
	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

	@Transient
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemCompraContaContabil)) return false;
		if (!super.equals(o)) return false;
		ItemCompraContaContabil that = (ItemCompraContaContabil) o;
		return Objects.equals(getNumeroContaContabil(), that.getNumeroContaContabil());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNumeroContaContabil());
	}

	public enum TipoRelacionamentoItemCompra {

		CENTRO_CUSTO("Centro de custo"), CENTRO_LUCRO("Centro de lucro");

		private final String nome;

		private TipoRelacionamentoItemCompra(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}
}
