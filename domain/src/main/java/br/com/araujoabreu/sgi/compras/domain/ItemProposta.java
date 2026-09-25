package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.tools.FormatarBigDecimal;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "ITEM_PROPOSTA")
@SequenceGenerator(name="SEQ_ITEM_PROPOSTA", sequenceName="SEQ_ITEM_PROPOSTA", allocationSize=1)
public class ItemProposta extends AuditEntity implements Detail, Comparable<ItemProposta> {

	private static final long serialVersionUID = 5329271394398881943L;
	private Proposta proposta;
	private ItemCompra itemCompra;
	private BigDecimal quantidadeSolicitada;
	private BigDecimal quantidadeInformada;
	private BigDecimal preco;
	private BigDecimal precoNegociado;
	private String marca;
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PROPOSTA")
	@NotNull
	public Proposta getProposta() {
		return proposta;
	}

	public void setProposta(Proposta proposta) {
		this.proposta = proposta;
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

	@Column(name = "QUANTIDADE_SOLICITADA", precision = 15, scale = 4)
	@NotNull
	public BigDecimal getQuantidadeSolicitada() {
		return quantidadeSolicitada;
	}

	public void setQuantidadeSolicitada(BigDecimal quantidadeSolicitada) {
		this.quantidadeSolicitada = quantidadeSolicitada;
	}

	@Transient
	public String getQuantidadeSolicitadaStr() {
		if (getItemCompra() != null && getItemCompra().getUnidadeMedida() != null)
			quantidadeSolicitada = quantidadeSolicitada.setScale(4, RoundingMode.HALF_EVEN);

		return FormatarBigDecimal.formatBigDecimal(quantidadeSolicitada);
	}

	@Transient
	public void setQuantidadeSolicitadaStr(String quantidadeStr) {
		this.quantidadeSolicitada = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Column(name = "QUANTIDADE_INFORMADA", precision = 15, scale = 4)
	public BigDecimal getQuantidadeInformada() {
		return quantidadeInformada;
	}

	public void setQuantidadeInformada(BigDecimal quantidadeInformada) {
		this.quantidadeInformada = quantidadeInformada;
	}

	@Transient
	public String getQuantidadeInformadaStr() {
		if (getItemCompra() != null && getItemCompra().getUnidadeMedida() != null)
			quantidadeInformada = quantidadeInformada.setScale(getItemCompra().getUnidadeMedida().getNumCasasDecimais(), RoundingMode.HALF_EVEN);
		return FormatarBigDecimal.formatBigDecimal(quantidadeInformada);
	}

	@Transient
	public void setQuantidadeInformadaStr(String quantidadeStr) {
		this.quantidadeInformada = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Column(name = "PRECO_INICIAL", precision = 15, scale = 4)
	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@Column(name = "PRECO_NEGOCIADO", precision = 15, scale = 4)
	public BigDecimal getPrecoNegociado() {
		return precoNegociado;
	}

	public void setPrecoNegociado(BigDecimal precoNegociado) {
		this.precoNegociado = precoNegociado;
	}

	@Transient
	public String getPrecoStr() {
		preco = preco.setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal valor = preco;

		if (precoNegociado != null) {
			precoNegociado = precoNegociado.setScale(4, RoundingMode.HALF_EVEN);
			valor = precoNegociado;
		}
		return FormatarBigDecimal.formatBigDecimal(valor);
	}

	@Transient
	public void setPrecoStr(String quantidadeStr) {
		this.preco = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Column(name = "MARCA")
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Column(name = "descricao")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public BigDecimal getValor() {
		if (preco != null && quantidadeInformada != null) {
			return preco.multiply(quantidadeInformada);
		}
		return null;
	}

	@Transient
	public BigDecimal getValorNegociado() {
		if (precoNegociado != null && quantidadeInformada != null) {
			return precoNegociado.multiply(quantidadeInformada);
		}
		return null;
	}

	@Transient
	public BigDecimal getValorExibido() {
		if (getValorNegociado() != null) {
			return getValorNegociado();
		}
		return getValor();
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
	}

	@Override
	public int compareTo(ItemProposta arg0) {
		if (this.descricao != null && !this.descricao.isEmpty()) {
			return this.descricao.compareToIgnoreCase(arg0.getDescricao());
		}

		if (this.itemCompra != null && this.itemCompra.getCodigo() != null && arg0.getItemCompra() != null && arg0.getItemCompra().getCodigo() != null) {
			return this.itemCompra.getNome().compareToIgnoreCase(arg0.getItemCompra().getNome());
		}

		return 0;
	}

	@Transient
	public String getDescricaoAbreviado() {
		if (descricao != null && !descricao.isEmpty()) {
			if (descricao.length() > 30) {
				return descricao.substring(0, 27) + "...";
			}
		}
		return descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ItemProposta.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemProposta other = (ItemProposta) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemProposta [codigo=" + getCodigo() + ", proposta=" + proposta + ", itemCompra=" + itemCompra + ", quantidadeSolicitada="
			+ quantidadeSolicitada + ", quantidadeInformada=" + quantidadeInformada + ", preco=" + preco + ", precoNegociado=" + precoNegociado
			+ ", marca=" + marca + ", descricao=" + descricao + "]";
	}

}
