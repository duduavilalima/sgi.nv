package br.com.araujoabreu.sgi.comercial.domain;

import br.com.araujoabreu.sgi.compras.domain.UnidadeMedida;
import br.com.fasolti.framework.domain.BaseEntity;
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
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "FECHO_DESPESA_GERAL")
@SequenceGenerator(name = "SEQ_FECHO_DESPESA_GERAL", sequenceName = "SEQ_FECHO_DESPESA_GERAL", allocationSize = 1)
public class FechoDespesaGeral extends BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = 5100467842441005844L;
	private Fecho fecho;
	private TipoDespesaGeral tipoDespesaGeral;
	private UnidadeMedida unidadeMedida;
	private Long codigoUnidadeMedida;
	private CategoriaDespesaGeral categoriaDespesaGeral;
	private Long codigoCategoriaDespesaGeral;
	private BigDecimal quantidade;
	private BigDecimal valorUnitario;
	private String quantidadeStr;
	private String descricao;

	public FechoDespesaGeral() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FECHO", nullable = false)
	@NotNull
	public Fecho getFecho() {
		return fecho;
	}

	public void setFecho(Fecho fecho) {
		this.fecho = fecho;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoDespesaGeral getTipoDespesaGeral() {
		return tipoDespesaGeral;
	}

	public void setTipoDespesaGeral(TipoDespesaGeral tipoDespesaGeral) {
		this.tipoDespesaGeral = tipoDespesaGeral;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CATEGORIA_DESPESA_GERAL")
	public CategoriaDespesaGeral getCategoriaDespesaGeral() {
		return categoriaDespesaGeral;
	}

	public void setCategoriaDespesaGeral(
		CategoriaDespesaGeral categoriaDespesaGeral) {
		this.categoriaDespesaGeral = categoriaDespesaGeral;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE_MEDIDA")
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	@Column(name = "QUANTIDADE", nullable = false)
	@NotNull
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Column(name = "VALOR_UNITARIO", nullable = false)
	@NotNull
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public Long getCodigoCategoriaDespesaGeral() {
		if (codigoCategoriaDespesaGeral == null && categoriaDespesaGeral != null && categoriaDespesaGeral.getCodigo() != null)
			this.codigoCategoriaDespesaGeral = categoriaDespesaGeral.getCodigo();
		return codigoCategoriaDespesaGeral;
	}

	public void setCodigoCategoriaDespesaGeral(Long codigoCategoriaDespesaGeral) {
		this.codigoCategoriaDespesaGeral = codigoCategoriaDespesaGeral;
	}

	@Transient
	public Long getCodigoUnidadeMedida() {
		if (codigoUnidadeMedida == null && unidadeMedida != null && unidadeMedida.getCodigo() != null)
			this.codigoUnidadeMedida = unidadeMedida.getCodigo();
		return codigoUnidadeMedida;
	}

	public void setCodigoUnidadeMedida(Long codigoUnidadeMedida) {
		this.codigoUnidadeMedida = codigoUnidadeMedida;
	}

	@Transient
	public BigDecimal getValorTotalItem() {
		if (valorUnitario != null && quantidade != null) {
			return valorUnitario.multiply(quantidade);
		}
		return new BigDecimal("0");
	}

	@Transient
	public String getQuantidadeStr() {
		return quantidadeStr;
	}

	public void setQuantidadeStr(String quantidadeStr) {
		this.quantidadeStr = quantidadeStr;
		if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
			String quantidadeStrAux = quantidadeStr.replace(".", "");
			quantidadeStrAux = quantidadeStr.replace(",", ".").replace("_", "");
			quantidade = new BigDecimal(quantidadeStrAux);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaDespesaGeral == null) ? 0 : categoriaDespesaGeral.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((codigoCategoriaDespesaGeral == null) ? 0 : codigoCategoriaDespesaGeral.hashCode());
		result = prime * result + ((codigoUnidadeMedida == null) ? 0 : codigoUnidadeMedida.hashCode());
		result = prime * result + ((fecho == null) ? 0 : fecho.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((quantidadeStr == null) ? 0 : quantidadeStr.hashCode());
		result = prime * result + ((tipoDespesaGeral == null) ? 0 : tipoDespesaGeral.hashCode());
		result = prime * result + ((valorUnitario == null) ? 0 : valorUnitario.hashCode());
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
		if (!(FechoDespesaGeral.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		FechoDespesaGeral other = (FechoDespesaGeral) obj;
		if (getCategoriaDespesaGeral() == null) {
			if (other.getCategoriaDespesaGeral() != null) {
				return false;
			}
		} else if (!getCategoriaDespesaGeral().equals(other.getCategoriaDespesaGeral())) {
			return false;
		}
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getCodigoCategoriaDespesaGeral() == null) {
			if (other.getCodigoCategoriaDespesaGeral() != null) {
				return false;
			}
		} else if (!getCodigoCategoriaDespesaGeral().equals(other.getCodigoCategoriaDespesaGeral())) {
			return false;
		}
		if (getCodigoUnidadeMedida() == null) {
			if (other.getCodigoUnidadeMedida() != null) {
				return false;
			}
		} else if (!getCodigoUnidadeMedida().equals(other.getCodigoUnidadeMedida())) {
			return false;
		}
		if (getFecho() == null) {
			if (other.getFecho() != null) {
				return false;
			}
		} else if (!getFecho().equals(other.getFecho())) {
			return false;
		}
		if (getQuantidade() == null) {
			if (other.getQuantidade() != null) {
				return false;
			}
		} else if (!getQuantidade().equals(other.getQuantidade())) {
			return false;
		}
		if (getQuantidadeStr() == null) {
			if (other.getQuantidadeStr() != null) {
				return false;
			}
		} else if (!getQuantidadeStr().equals(other.getQuantidadeStr())) {
			return false;
		}
		if (getTipoDespesaGeral() != other.getTipoDespesaGeral()) {
			return false;
		}
		if (getValorUnitario() == null) {
			if (other.getValorUnitario() != null) {
				return false;
			}
		} else if (!getValorUnitario().equals(other.getValorUnitario())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FechoDespesaGeral [codigo=" + getCodigo() + ", fecho=" + fecho + ", tipoDespesaGeral=" + tipoDespesaGeral + ", codigoUnidadeMedida="
			+ codigoUnidadeMedida + ", codigoCategoriaDespesaGeral=" + codigoCategoriaDespesaGeral + ", quantidade=" + quantidade + ", valorUnitario="
			+ valorUnitario + ", quantidadeStr=" + quantidadeStr + "]";
	}

	@Override
	public FechoDespesaGeral clone() {
		FechoDespesaGeral fechoDespesaGeral = new FechoDespesaGeral();
		fechoDespesaGeral.setFecho(getFecho());
		fechoDespesaGeral.setDescricao(getDescricao());
		fechoDespesaGeral.setCategoriaDespesaGeral(getCategoriaDespesaGeral());
		fechoDespesaGeral.setTipoDespesaGeral(getTipoDespesaGeral());
		fechoDespesaGeral.setUnidadeMedida(getUnidadeMedida());
		fechoDespesaGeral.setCategoriaDespesaGeral(getCategoriaDespesaGeral());
		fechoDespesaGeral.setQuantidade(getQuantidade());
		fechoDespesaGeral.setValorUnitario(getValorUnitario());
		fechoDespesaGeral.setQuantidadeStr(getQuantidadeStr());
		return fechoDespesaGeral;
	}

	@Transient
	public String getDescricaoCategoriaDespesaGeral() {
		String descricao = "";
		if (this.tipoDespesaGeral != null) {
			if (this.tipoDespesaGeral.equals(TipoDespesaGeral.GASTO_GERAL) && categoriaDespesaGeral != null) {
				descricao = categoriaDespesaGeral.getNome();
			} else {
				descricao = this.descricao;
			}
		}
		return descricao;
	}

	public enum TipoDespesaGeral {
		GASTO_GERAL("Gasto geral"),
		MATERIAL("Material"),
		SERVICO("Serviço de terceiro");

		private final String nome;

		TipoDespesaGeral(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return this.nome;
		}
	}
}

