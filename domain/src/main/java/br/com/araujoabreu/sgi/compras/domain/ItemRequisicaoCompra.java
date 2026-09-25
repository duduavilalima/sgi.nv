package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.tools.FormatarBigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.Length;
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
import java.util.Objects;

@Entity
@Table(name = "ITEM_REQUISICAO_COMPRA")
@SequenceGenerator(name="SEQ_ITEM_REQUISICAO_COMPRA", sequenceName="SEQ_ITEM_REQUISICAO_COMPRA", allocationSize=1)
public class ItemRequisicaoCompra extends AuditEntity implements Detail {

	private static final long serialVersionUID = 1318316389457622655L;
	private BigDecimal quantidade;
	private String marca;
	private String justificativa;
	private String descricao;
	private ItemCompra itemCompra;
	private RequisicaoCompraRefinada requisicaoCompraRefinada;

	@Column(name = "QUANTIDADE", precision = 15, scale = 6)
	@NotNull
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Column(name = "MARCA")
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Column(name = "JUSTIFICATIVA")
	@Length(max = 255)
	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	@JoinColumn(name = "CODIGO_RC_REFINADA")
	@NotNull
	public RequisicaoCompraRefinada getRequisicaoCompraRefinada() {
		return requisicaoCompraRefinada;
	}

	public void setRequisicaoCompraRefinada(RequisicaoCompraRefinada requisicaoCompraRefinada) {
		this.requisicaoCompraRefinada = requisicaoCompraRefinada;
	}

	@Transient
	public String getQuantidadeStr() {
		if (getItemCompra() != null && getItemCompra().getUnidadeMedida() != null)
			quantidade = quantidade.setScale(getItemCompra().getUnidadeMedida().getNumCasasDecimais(), RoundingMode.HALF_EVEN);
		return FormatarBigDecimal.formatBigDecimal(quantidade);
	}

	@Transient
	public void setQuantidadeStr(String quantidadeStr) {
		this.quantidade = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
	}

	@Transient
	public Boolean getExisteJustificativa() {
		if (StringUtils.isEmpty(this.justificativa)) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Transient
	public Boolean getExisteMarca() {
		if (StringUtils.isEmpty(this.marca)) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Transient
	public Boolean getExisteDescricao() {
		if (StringUtils.isEmpty(this.descricao)) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemRequisicaoCompra)) return false;
		if (!super.equals(o)) return false;
		ItemRequisicaoCompra that = (ItemRequisicaoCompra) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getQuantidade(), that.getQuantidade()) &&
			Objects.equals(getMarca(), that.getMarca()) &&
			Objects.equals(getJustificativa(), that.getJustificativa()) &&
			Objects.equals(getDescricao(), that.getDescricao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigo(), getQuantidade(), getMarca(), getJustificativa(), getDescricao());
	}

	@Override
	public String toString() {
		return "ItemRequisicaoCompra [codigo=" + getCodigo() + ", quantidade=" + quantidade + ", marca=" + marca + "]";
	}

}
