package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@MappedSuperclass
public class Rateio extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4688246340167622952L;
	private BigDecimal valor;
	private BigDecimal valorAgrupado;
	private String situacao;
	private CentroFinanceiro centroFinanceiro;

	public Rateio() {
		super();
	}

	public Rateio(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Rateio(CentroFinanceiro centroFinanceiro, BigDecimal valorAgrupado) {
		this.centroFinanceiro = centroFinanceiro;
		this.valorAgrupado = valorAgrupado;
	}

	@Column(name = "valor", nullable = false, precision = 19, scale = 2)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@JoinColumn(name = "codigo_centro_financeiro", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Transient
	public BigDecimal getValorAgrupado() {
		return valorAgrupado;
	}

	public void setValorAgrupado(BigDecimal valorAgrupado) {
		this.valorAgrupado = valorAgrupado;
	}

	@Transient
	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centroFinanceiro == null) ? 0 : centroFinanceiro.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((valorAgrupado == null) ? 0 : valorAgrupado.hashCode());
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
		if (!(Rateio.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Rateio other = (Rateio) obj;
		if (centroFinanceiro == null) {
			if (other.centroFinanceiro != null) {
				return false;
			}
		} else if (!centroFinanceiro.equals(other.centroFinanceiro)) {
			return false;
		}
		if (situacao == null) {
			if (other.situacao != null) {
				return false;
			}
		} else if (!situacao.equals(other.situacao)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		if (valorAgrupado == null) {
			if (other.valorAgrupado != null) {
				return false;
			}
		} else if (!valorAgrupado.equals(other.valorAgrupado)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Rateio [valor=" + valor + ", valorAgrupado=" + valorAgrupado + ", situacao=" + situacao + "]";
	}

}
