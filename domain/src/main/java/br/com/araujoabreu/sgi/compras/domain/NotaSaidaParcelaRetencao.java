package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NOTA_SAIDA_PARCELA_RETENCAO")
@SequenceGenerator(name="SEQ_NOTA_SAIDA_PARCELA_RETENCAO", sequenceName="SEQ_NOTA_SAIDA_PARCELA_RETENCAO", allocationSize=1)
public class NotaSaidaParcelaRetencao extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 9160712001357498379L;
	public static final String NOME_TR_CONTRATUAL = "Contratual";
	public static final String NOME_TR_ACESSORIA = "Acessória";
	public static final String DESCRICAO_TR_CONTRATUAL = "Retenção contratual";
	public static final String DESCRICAO_TR_ACESSORIA = "Retenção acessória";
	private NotaSaidaParcela parcela;
	private TipoRetencao tipo;
	private BigDecimal valorAReter;
	private BigDecimal valorRetido;

	public NotaSaidaParcelaRetencao() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota_saida_parcela")
	public NotaSaidaParcela getParcela() {
		return parcela;
	}

	public void setParcela(NotaSaidaParcela parcela) {
		this.parcela = parcela;
	}

	@Column(name = "TIPO", nullable = false)
	public TipoRetencao getTipo() {
		return tipo;
	}

	public void setTipo(TipoRetencao tipo) {
		this.tipo = tipo;
	}

	@Column(name = "VALOR_A_RETER", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorAReter() {
		return valorAReter;
	}

	public void setValorAReter(BigDecimal valorAReter) {
		this.valorAReter = valorAReter;
	}

	@Column(name = "VALOR_RETIDO", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorRetido() {
		return valorRetido;
	}

	public void setValorRetido(BigDecimal valorRetido) {
		this.valorRetido = valorRetido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parcela == null) ? 0 : parcela.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((valorAReter == null) ? 0 : valorAReter.hashCode());
		result = prime * result + ((valorRetido == null) ? 0 : valorRetido.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotaSaidaParcelaRetencao other = (NotaSaidaParcelaRetencao) obj;
		if (parcela == null) {
			if (other.parcela != null)
				return false;
		} else if (!parcela.equals(other.parcela))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (valorAReter == null) {
			if (other.valorAReter != null)
				return false;
		} else if (!valorAReter.equals(other.valorAReter))
			return false;
		if (valorRetido == null) {
			if (other.valorRetido != null)
				return false;
		} else if (!valorRetido.equals(other.valorRetido))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotaSaidaParcelaRetencao [parcela=" + parcela + ", tipo=" + tipo + ", valorAReter="
			+ valorAReter + ", valorRetido=" + valorRetido + "]";
	}

	public enum TipoRetencao {
		CONTRATUAL(DESCRICAO_TR_CONTRATUAL, NOME_TR_CONTRATUAL),
		ACESSORIA(DESCRICAO_TR_ACESSORIA, NOME_TR_ACESSORIA);

		private final String descricao;
		private final String nome;

		TipoRetencao(String descricao, String nome) {
			this.descricao = descricao;
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return getNome();
		}
	}
}
