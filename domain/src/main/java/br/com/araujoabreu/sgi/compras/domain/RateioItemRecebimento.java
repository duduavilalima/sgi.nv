package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
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
@Table(name = "RATEIO_ITEM_RECEBIMENTO")
@SequenceGenerator(name="SEQ_RATEIO_ITEM_RECEBIMENTO", sequenceName="SEQ_RATEIO_ITEM_RECEBIMENTO", allocationSize = 1)
public class RateioItemRecebimento extends BaseEntity {

	private static final long serialVersionUID = 8480418168737399732L;
	private ItemRecebimento itemRecebimento;
	private CentroFinanceiro centroFinanceiro;
	private BigDecimal quantidade;
	private RequisicaoCompra requisicaoCompra;
	private BigDecimal quantidadeSolicitada;
	private BigDecimal quantidadeRecebida;
	private BigDecimal quantidadeAnterior; //utilizado para o botão cancelar no pop-up de edição

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_ITEM_RECEBIMENTO", nullable = false)
	public ItemRecebimento getItemRecebimento() {
		return itemRecebimento;
	}

	public void setItemRecebimento(ItemRecebimento itemRecebimento) {
		this.itemRecebimento = itemRecebimento;
	}

	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "quantidade", nullable = false)
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Transient
	public BigDecimal getQuantidadeAnterior() {
		return quantidadeAnterior;
	}

	public void setQuantidadeAnterior(BigDecimal quantidadeAnterior) {
		this.quantidadeAnterior = quantidadeAnterior;
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
		if (!(RateioItemRecebimento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		RateioItemRecebimento other = (RateioItemRecebimento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Transient
	public RequisicaoCompra getRequisicaoCompra() {
		return requisicaoCompra;
	}

	public void setRequisicaoCompra(RequisicaoCompra requisicaoCompra) {
		this.requisicaoCompra = requisicaoCompra;
	}


	@Transient
	public BigDecimal getQuantidadeSolicitada() {
		return quantidadeSolicitada;
	}

	public void setQuantidadeSolicitada(BigDecimal quantidadeSolicitada) {
		this.quantidadeSolicitada = quantidadeSolicitada;
	}

	@Transient
	public BigDecimal getQuantidadeRecebida() {
		return quantidadeRecebida;
	}

	public void setQuantidadeRecebida(BigDecimal quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
	}

	@Transient
	public BigDecimal calcularRestanteEsteRecebimento() {
		BigDecimal total = new BigDecimal("0");
		if (itemRecebimento != null && itemRecebimento.getItemPedido() != null
			&& itemRecebimento.getItemPedido().getItemProposta() != null
			&& itemRecebimento.getItemPedido().getItemProposta().getQuantidadeInformada() != null) {
			total = itemRecebimento.getItemPedido().getItemProposta().getQuantidadeInformada().subtract(itemRecebimento.getItemPedido().getQuantidadeRecebida());
		}
		total = total.setScale(2, RoundingMode.HALF_EVEN);
		return total;
	}

}
