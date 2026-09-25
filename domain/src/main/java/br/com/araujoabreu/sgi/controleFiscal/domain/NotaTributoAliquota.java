package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.Status;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

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
@Table(name = "NOTA_TRIBUTO_ALIQUOTA")
@SequenceGenerator(name = "SEQ_NOTA_TRIBUTO_ALIQUOTA", sequenceName = "SEQ_NOTA_TRIBUTO_ALIQUOTA", allocationSize = 1)
public class NotaTributoAliquota extends BaseEntity {

	private static final long serialVersionUID = 6112236690943175473L;
	private BigDecimal baseCalculo;
	private BigDecimal percentual;
	private Integer individual = 0;
	private Nota nota;
	private Aliquota aliquota;
	private Pagamento pagamento;
	private Boolean selecao = false;
	private boolean editavel;
	private CategoriaTributo categoriaTributo;
	private Tributo tributo;
	private Boolean validaPrefeitura = Boolean.FALSE;
	private String uf;
	private Long codigoTributo;
	private BigDecimal totalTributos;
	private BigDecimal valor;
	private Integer ordem = 1;

	public NotaTributoAliquota() {
	}

	public NotaTributoAliquota(Long codigo, BigDecimal valor) {
		setCodigo(codigo);
		this.valor = valor;
	}

	@Column(name = "BASE_CALCULO")
	@NotNull
	public BigDecimal getBaseCalculo() {
		if (baseCalculo == null) {
			return baseCalculo;
		}
		return baseCalculo.setScale(2, RoundingMode.HALF_UP);
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	@Column(name = "PERCENTUAL")
	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	@Column(name = "INDIVIDUAL")
	@NotNull
	public Integer getIndividual() {
		return individual;
	}

	public void setIndividual(Integer individual) {
		this.individual = individual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ALIQUOTA")
	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	@Column(name = "VALIDA_PREFEITURA", nullable = false)
	public Boolean getValidaPrefeitura() {
		return validaPrefeitura;
	}

	public void setValidaPrefeitura(Boolean validaPrefeitura) {
		this.validaPrefeitura = validaPrefeitura;
	}

	@Column(name = "VALOR")
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "ORDEM")
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Transient
	public boolean isEditavel() {
		return editavel;
	}

	public void setEditavel(boolean editavel) {
		this.editavel = editavel;
	}

	//	Criado este atributo para ser setado na combo. O caminho através de 'alíquota'
//	está sempre nulo, mesmo se criarmos uma instancia na hora de adicionar uma nova linha.
	@Transient
	public CategoriaTributo getCategoriaTributo() {
		return categoriaTributo;
	}

	public void setCategoriaTributo(CategoriaTributo categoriaTributo) {
		this.categoriaTributo = categoriaTributo;
	}

	//	Criado este atributo para ser setado na combo. O caminho através de 'alíquota'
//	está sempre nulo, mesmo se criarmos uma instancia na hora de adicionar uma nova linha.
	@Transient
	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	@Transient
	public boolean isIss() {
		if (categoriaTributo != null && categoriaTributo.getTributo() != null && categoriaTributo.getTributo().getNome().equals(Tributo.ISS)) {
			return true;
		}
		if (tributo != null && tributo.getNome() != null && tributo.getNome().equals(Tributo.ISS)) {
			return true;
		}
		return false;
	}

	@Transient
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Transient
	public boolean isAcumulado() {
		if (pagamento != null && pagamento.getStatus() != null) {
			return (pagamento.getStatus().equals(Status.AGUARDANDO_AGRUPAMENTO) || pagamento.getStatus().equals(Status.AGRUPADO));
		}

		return false;
	}

	@Transient
	public BigDecimal getCalculoValorTributoPorcentagem() {
		BigDecimal base = getBaseCalculo();
		BigDecimal aliq = getAliquota().getPercentual();

		return (base.multiply(aliq)).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	@Transient
	public boolean isValorMenorMinimoGuia() {
		if (this.getPagamento() != null
			&& this.getAliquota() != null
			&& this.getAliquota().getCategoriaTributo() != null
			&& ((this.getPagamento().getValor().setScale(2, RoundingMode.HALF_EVEN).compareTo(this.getAliquota().getCategoriaTributo().getMinimoGuia().setScale(2, RoundingMode.HALF_EVEN)) < 1))) {

			return true;

		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result
			+ ((individual == null) ? 0 : individual.hashCode());
		result = prime * result
			+ ((percentual == null) ? 0 : percentual.hashCode());
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
		if (!(NotaTributoAliquota.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		NotaTributoAliquota other = (NotaTributoAliquota) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (individual == null) {
			if (other.getIndividual() != null) {
				return false;
			}
		} else if (!individual.equals(other.getIndividual())) {
			return false;
		}
		if (percentual == null) {
			if (other.getPercentual() != null) {
				return false;
			}
		} else if (!percentual.equals(other.getPercentual())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "NotaTributoAliquota [codigo=" + getCodigo() + ", baseCalculo="
			+ baseCalculo + ", percentual=" + percentual + ", individual="
			+ individual + "]";
	}

	@Transient
	public Long getCodigoTributo() {
		return codigoTributo;
	}

	public void setCodigoTributo(Long codigoTributo) {
		this.codigoTributo = codigoTributo;
	}

	@Transient
	public BigDecimal getTotalTributos() {
		return totalTributos;
	}

	public void setTotalTributos(BigDecimal totalTributos) {
		this.totalTributos = totalTributos;
	}
}
