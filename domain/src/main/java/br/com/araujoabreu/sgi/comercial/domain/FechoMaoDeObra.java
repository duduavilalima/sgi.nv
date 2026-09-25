package br.com.araujoabreu.sgi.comercial.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "FECHO_MAO_OBRA")
@SequenceGenerator(name = "SEQ_FECHO_MAO_OBRA", sequenceName = "SEQ_FECHO_MAO_OBRA", allocationSize = 1)
public class FechoMaoDeObra extends BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = -7650149274341376627L;
	private Fecho fecho;
	private CategoriaMaoDeObra categoriaMaoDeObra;
	private TipoContratacao tipoContratacao;
	private BigDecimal quantidade;
	private BigDecimal valorUnitario;
	private String quantidadeStr;
	private Long codigoCategoriaMaoDeObra;
	private List<FechoMaoDeObraAdicional> listaMaoDeObraAdicional = new ArrayList<>();

	public FechoMaoDeObra() {
		super();
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CATEGORIA_MAO_OBRA", nullable = false)
	@NotNull
	public CategoriaMaoDeObra getCategoriaMaoDeObra() {
		return categoriaMaoDeObra;
	}

	public void setCategoriaMaoDeObra(CategoriaMaoDeObra categoriaMaoDeObra) {
		this.categoriaMaoDeObra = categoriaMaoDeObra;
	}

	@Column(name = "CONTRATACAO", nullable = false)
	@NotNull
	public TipoContratacao getTipoContratacao() {
		return tipoContratacao;
	}

	public void setTipoContratacao(TipoContratacao tipoContratacao) {
		this.tipoContratacao = tipoContratacao;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fechoMaoDeObra")
	public List<FechoMaoDeObraAdicional> getListaMaoDeObraAdicional() {
		return listaMaoDeObraAdicional;
	}

	public void setListaMaoDeObraAdicional(List<FechoMaoDeObraAdicional> listaMaoDeObraAdicional) {
		this.listaMaoDeObraAdicional = listaMaoDeObraAdicional;
	}

	@Transient
	public BigDecimal getValorTotal() {
		BigDecimal total = new BigDecimal("0");
		if (valorUnitario != null && quantidade != null) {
			total = quantidade.multiply(valorUnitario.add(getTotalAdicionais()));
		}
		return total;
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

	@Transient
	public Long getCodigoCategoriaMaoDeObra() {
		return codigoCategoriaMaoDeObra;
	}

	public void setCodigoCategoriaMaoDeObra(Long codigoCategoriaMaoDeObra) {
		this.codigoCategoriaMaoDeObra = codigoCategoriaMaoDeObra;
	}

	@Transient
	public BigDecimal getTotalAdicionais() {
		BigDecimal total = new BigDecimal("0");
		if (listaMaoDeObraAdicional != null && !listaMaoDeObraAdicional.isEmpty()) {
			for (FechoMaoDeObraAdicional adicional : listaMaoDeObraAdicional) {
				if (adicional != null && adicional.getValorUnitario() != null) {
					total = total.add(adicional.getValorUnitario());
				}
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoriaMaoDeObra == null) ? 0 : categoriaMaoDeObra.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((codigoCategoriaMaoDeObra == null) ? 0 : codigoCategoriaMaoDeObra.hashCode());
		result = prime * result + ((fecho == null) ? 0 : fecho.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((quantidadeStr == null) ? 0 : quantidadeStr.hashCode());
		result = prime * result + ((tipoContratacao == null) ? 0 : tipoContratacao.hashCode());
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		FechoMaoDeObra other = (FechoMaoDeObra) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;

		if (categoriaMaoDeObra == null) {
			if (other.categoriaMaoDeObra != null)
				return false;
		} else if (!categoriaMaoDeObra.equals(other.categoriaMaoDeObra))
			return false;

		if (tipoContratacao != other.tipoContratacao)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "FechoMaoDeObra [codigo=" + getCodigo() + ", tipoContratacao=" + tipoContratacao + ", quantidade=" + quantidade + ", valorUnitario=" + valorUnitario
			+ ", quantidadeStr=" + quantidadeStr + "]";
	}

	@Override
	public FechoMaoDeObra clone() {
		FechoMaoDeObra fechoMaoDeObra = new FechoMaoDeObra();
		fechoMaoDeObra.setFecho(getFecho());
		fechoMaoDeObra.setCategoriaMaoDeObra(getCategoriaMaoDeObra());
		fechoMaoDeObra.setTipoContratacao(getTipoContratacao());
		fechoMaoDeObra.setQuantidade(getQuantidade());
		fechoMaoDeObra.setValorUnitario(getValorUnitario());
		fechoMaoDeObra.setQuantidadeStr(getQuantidadeStr());
		return fechoMaoDeObra;
	}

	public enum TipoContratacao {
		DIRETA("Direta"),
		INDIRETA("Indireta"),
		DIRETA_PLANTAO("Direta (Plantão)"),
		INDIRETA_PLANTAO("Indireta (Plantão)");

		private final String nome;

		TipoContratacao(String nome) {
			this.nome = nome;
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
