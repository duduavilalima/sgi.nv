package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "UNIDADE")
public class Unidade extends PessoaJuridica {

	private static final long serialVersionUID = -5466151511063439943L;
	private String codnome;
	private TipoUnidade tipoUnidade;
	private Unidade matriz;
	private Boolean zerarNumeracaoNfAno;
	private String mascaraContaContabil;
	private Boolean emitirNfFaturamento;
	private Integer proximaNf;
	private Short anoProximaNf;
	private Integer proximaNfMaterial;
	private Integer proximaNd;

	public Unidade() {
	}

	public Unidade(Long cod) {
		setCodigo(cod);
	}

	public Unidade(Long cod, String codNome) {
		setCodigo(cod);
		setCodnome(codNome);
	}

	@Column(name = "CODNOME", nullable = false, length = 60)
	@NotNull
	@Length(max = 60)
	public String getCodnome() {
		return this.codnome;
	}

	public void setCodnome(final String codnome) {
		this.codnome = codnome;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoUnidade getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(final TipoUnidade tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MATRIZ")
	public Unidade getMatriz() {
		return matriz;
	}

	public void setMatriz(final Unidade matriz) {
		this.matriz = matriz;
	}

	@Column(name = "zerar_numeracao_nf_ano", insertable = false, updatable = false, nullable = false)
	@NotNull
	public Boolean getZerarNumeracaoNfAno() {
		return zerarNumeracaoNfAno;
	}

	public void setZerarNumeracaoNfAno(Boolean zerarNumeracaoNfAno) {
		this.zerarNumeracaoNfAno = zerarNumeracaoNfAno;
	}

	@Column(name = "mascara_conta_contabil", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getMascaraContaContabil() {
		return mascaraContaContabil;
	}

	public void setMascaraContaContabil(String mascaraContaContabil) {
		this.mascaraContaContabil = mascaraContaContabil;
	}

	@Column(name = "emite_nf_faturamento", insertable = false, updatable = false, nullable = false)
	@NotNull
	public Boolean getEmitirNfFaturamento() {
		return emitirNfFaturamento;
	}

	public void setEmitirNfFaturamento(Boolean emitirNfFaturamento) {
		this.emitirNfFaturamento = emitirNfFaturamento;
	}

	@Column(name = "proxima_nf")
	@NotNull
	public Integer getProximaNf() {
		return proximaNf;
	}

	public void setProximaNf(Integer proximaNf) {
		this.proximaNf = proximaNf;
	}

	@Column(name = "ano_proxima_nf")
	@NotNull
	public Short getAnoProximaNf() {
		return anoProximaNf;
	}

	public void setAnoProximaNf(Short anoProximaNf) {
		this.anoProximaNf = anoProximaNf;
	}

	@Column(name = "proxima_nf_material")
	public Integer getProximaNfMaterial() {
		return proximaNfMaterial;
	}

	public void setProximaNfMaterial(Integer proximaNfMaterial) {
		this.proximaNfMaterial = proximaNfMaterial;
	}

	@Column(name = "proxima_nd")
	public Integer getProximaNd() {
		return proximaNd;
	}

	public void setProximaNd(Integer proximaNd) {
		this.proximaNd = proximaNd;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Unidade)) return false;
		if (!super.equals(o)) return false;
		Unidade unidade = (Unidade) o;
		return Objects.equals(getCodnome(), unidade.getCodnome()) &&
			getTipoUnidade() == unidade.getTipoUnidade() &&
			Objects.equals(getZerarNumeracaoNfAno(), unidade.getZerarNumeracaoNfAno()) &&
			Objects.equals(getMascaraContaContabil(), unidade.getMascaraContaContabil()) &&
			Objects.equals(getEmitirNfFaturamento(), unidade.getEmitirNfFaturamento()) &&
			Objects.equals(getProximaNf(), unidade.getProximaNf()) &&
			Objects.equals(getAnoProximaNf(), unidade.getAnoProximaNf());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodnome(), getTipoUnidade(), getZerarNumeracaoNfAno(), getMascaraContaContabil(), getEmitirNfFaturamento(), getProximaNf(), getAnoProximaNf());
	}

	@Override
	public String toString() {
		return "Unidade [codnome=" + codnome + ", tipoUnidade=" + tipoUnidade + "]";
	}

	public enum TipoUnidade {
		MATRIZ("Matriz"),
		FILIAL("Filial");

		private final String nome;

		TipoUnidade(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}
}
