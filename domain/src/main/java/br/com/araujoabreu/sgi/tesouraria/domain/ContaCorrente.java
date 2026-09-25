package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CONTA_CORRENTE")
public class ContaCorrente extends DadosBancarios {

	private static final long serialVersionUID = 6773613352708768225L;

	private TipoContaCorrente tipoContaCorrente;
	private Boolean internetBanking = Boolean.FALSE;
	private Boolean chequeCarbono = Boolean.FALSE;
	private Long chequeAnterior = 0L;
	private Integer antesValor;
	private Integer saltoExtenso;
	private Integer antesExtenso;
	private Integer saltoExtenso2;
	private Integer antesExtenso2;
	private Integer saltoFavorecido;
	private Integer antesFavorecido;
	private Integer saltoData;
	private Integer antesData;
	private Integer saltoEndosso;
	private Integer antesEndosso;
	private Integer antesAssinatura;
	private Integer identificadorCliente;
	private Integer identificadorFise;
	private ContaContabil contaContabil;
	private Integer prazoProcessamentoIb;
	private List<RelacaoBancaria> listaRelacaoBancaria;
	private Boolean boletoFaturamento = Boolean.FALSE;
	private boolean conciliacao;
	private Long chequeInicial = 0L;

	public ContaCorrente() {
		super();
	}

	public ContaCorrente(Banco banco) {
		super(banco);
	}

	public ContaCorrente(Long codigo) {
		setCodigo(codigo);
	}

	public ContaCorrente(Long codigo, Banco banco) {
		super(banco);
		setCodigo(codigo);
	}

	@Column(name = "PRAZO_PROCESSAMENTO_IB")
	public Integer getPrazoProcessamentoIb() {
		return prazoProcessamentoIb;
	}

	public void setPrazoProcessamentoIb(Integer prazoProcessamentoIb) {
		this.prazoProcessamentoIb = prazoProcessamentoIb;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoContaCorrente getTipoContaCorrente() {
		return tipoContaCorrente;
	}

	public void setTipoContaCorrente(TipoContaCorrente tipoContaCorrente) {
		this.tipoContaCorrente = tipoContaCorrente;
	}

	@Column(name = "CHEQUE_CARBONO")
	@NotNull
	public Boolean getChequeCarbono() {
		return chequeCarbono;
	}

	public void setChequeCarbono(Boolean chequeCarbono) {
		this.chequeCarbono = chequeCarbono;
	}

	@Column(name = "INTERNET_BANKING")
	@NotNull
	public Boolean getInternetBanking() {
		return internetBanking;
	}

	public void setInternetBanking(Boolean internetBanking) {
		this.internetBanking = internetBanking;
	}

	@Column(name = "CHEQUE_ANTERIOR")
	@NotNull
	public Long getChequeAnterior() {
		return chequeAnterior;
	}

	public void setChequeAnterior(Long chequeAnterior) {
		this.chequeAnterior = chequeAnterior;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contaCorrente")
	public List<RelacaoBancaria> getListaRelacaoBancaria() {
		return listaRelacaoBancaria;
	}

	public void setListaRelacaoBancaria(List<RelacaoBancaria> listaRelacaoBancaria) {
		this.listaRelacaoBancaria = listaRelacaoBancaria;
	}

	@Column(name = "ESPACO_ANTES_VALOR")
	@NotNull
	public Integer getAntesValor() {
		return antesValor;
	}

	public void setAntesValor(Integer antesValor) {
		this.antesValor = antesValor;
	}

	@Column(name = "SALTO_VALOR_EXTENSO")
	@NotNull
	public Integer getSaltoExtenso() {
		return saltoExtenso;
	}

	public void setSaltoExtenso(Integer saltoExtenso) {
		this.saltoExtenso = saltoExtenso;
	}

	@Column(name = "ESPACO_ANTES_EXTENSO")
	@NotNull
	public Integer getAntesExtenso() {
		return antesExtenso;
	}

	public void setAntesExtenso(Integer antesExtenso) {
		this.antesExtenso = antesExtenso;
	}

	@Column(name = "SALTO_EXTENSO_EXTENSO2")
	@NotNull
	public Integer getSaltoExtenso2() {
		return saltoExtenso2;
	}

	public void setSaltoExtenso2(Integer saltoExtenso2) {
		this.saltoExtenso2 = saltoExtenso2;
	}

	@Column(name = "ESPACO_ANTES_EXTENSO2")
	@NotNull
	public Integer getAntesExtenso2() {
		return antesExtenso2;
	}

	public void setAntesExtenso2(Integer antesExtenso2) {
		this.antesExtenso2 = antesExtenso2;
	}

	@Column(name = "SALTO_EXTENSO2_FAVORECIDO")
	@NotNull
	public Integer getSaltoFavorecido() {
		return saltoFavorecido;
	}

	public void setSaltoFavorecido(Integer saltoFavorecido) {
		this.saltoFavorecido = saltoFavorecido;
	}

	@Column(name = "ESPACO_ANTES_FAVORECIDO")
	@NotNull
	public Integer getAntesFavorecido() {
		return antesFavorecido;
	}

	public void setAntesFavorecido(Integer antesFavorecido) {
		this.antesFavorecido = antesFavorecido;
	}

	@Column(name = "SALTO_FAVORECIDO_DATA")
	@NotNull
	public Integer getSaltoData() {
		return saltoData;
	}

	public void setSaltoData(Integer saltoData) {
		this.saltoData = saltoData;
	}

	@Column(name = "ESPACO_ANTES_DATA")
	@NotNull
	public Integer getAntesData() {
		return antesData;
	}

	public void setAntesData(Integer antesData) {
		this.antesData = antesData;
	}

	@Column(name = "SALTO_ENDOSSO")
	@NotNull
	public Integer getSaltoEndosso() {
		return saltoEndosso;
	}

	public void setSaltoEndosso(Integer saltoEndosso) {
		this.saltoEndosso = saltoEndosso;
	}

	@Column(name = "ESPACO_ANTES_ENDOSSO")
	@NotNull
	public Integer getAntesEndosso() {
		return antesEndosso;
	}

	public void setAntesEndosso(Integer antesEndosso) {
		this.antesEndosso = antesEndosso;
	}

	@Column(name = "ESPACO_ANTES_ASSINATURA")
	@NotNull
	public Integer getAntesAssinatura() {
		return antesAssinatura;
	}

	public void setAntesAssinatura(Integer antesAssinatura) {
		this.antesAssinatura = antesAssinatura;
	}

	@Column(name = "IDENTIFICADOR_CLIENTE")
	public Integer getIdentificadorCliente() {
		return identificadorCliente;
	}

	public void setIdentificadorCliente(Integer identificadorCliente) {
		this.identificadorCliente = identificadorCliente;
	}

	@Column(name = "IDENTIFICADOR_FISE")
	public Integer getIdentificadorFise() {
		return identificadorFise;
	}

	public void setIdentificadorFise(Integer identificadorFise) {
		this.identificadorFise = identificadorFise;
	}

	@Column(name = "BOLETO_FATURAMENTO")
	@NotNull
	public Boolean getBoletoFaturamento() {
		return boletoFaturamento;
	}

	public void setBoletoFaturamento(Boolean boletoFaturamento) {
		this.boletoFaturamento = boletoFaturamento;
	}

	@Column(name = "CONCILIACAO_BANCARIA")
	@NotNull
	public boolean getConciliacao() {
		return conciliacao;
	}

	public void setConciliacao(boolean conciliacao) {
		this.conciliacao = conciliacao;
	}

	@Column(name = "CHEQUE_INICIAL")
	@NotNull
	public Long getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Long chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	@Transient
	public Unidade getEmpresa() {
		if (listaRelacaoBancaria != null && !listaRelacaoBancaria.isEmpty()) {
			for (RelacaoBancaria relacao : listaRelacaoBancaria) {
				return relacao.getUnidade();
			}
		}
		return new Unidade();
	}

	@Override
	public String toString() {
		return "ContaCorrente [tipoContaCorrente=" + tipoContaCorrente + ", internetBanking=" + internetBanking
			+ ", chequeCarbono=" + chequeCarbono + ", chequeAnterior=" + chequeAnterior + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContaCorrente)) return false;
		if (!super.equals(o)) return false;
		ContaCorrente that = (ContaCorrente) o;
		return getConciliacao() == that.getConciliacao() &&
			getTipoContaCorrente() == that.getTipoContaCorrente() &&
			Objects.equals(getInternetBanking(), that.getInternetBanking()) &&
			Objects.equals(getChequeCarbono(), that.getChequeCarbono()) &&
			Objects.equals(getChequeAnterior(), that.getChequeAnterior()) &&
			Objects.equals(getAntesValor(), that.getAntesValor()) &&
			Objects.equals(getSaltoExtenso(), that.getSaltoExtenso()) &&
			Objects.equals(getAntesExtenso(), that.getAntesExtenso()) &&
			Objects.equals(getSaltoExtenso2(), that.getSaltoExtenso2()) &&
			Objects.equals(getAntesExtenso2(), that.getAntesExtenso2()) &&
			Objects.equals(getSaltoFavorecido(), that.getSaltoFavorecido()) &&
			Objects.equals(getAntesFavorecido(), that.getAntesFavorecido()) &&
			Objects.equals(getSaltoData(), that.getSaltoData()) &&
			Objects.equals(getAntesData(), that.getAntesData()) &&
			Objects.equals(getSaltoEndosso(), that.getSaltoEndosso()) &&
			Objects.equals(getAntesEndosso(), that.getAntesEndosso()) &&
			Objects.equals(getAntesAssinatura(), that.getAntesAssinatura()) &&
			Objects.equals(getIdentificadorCliente(), that.getIdentificadorCliente()) &&
			Objects.equals(getIdentificadorFise(), that.getIdentificadorFise()) &&
			Objects.equals(getContaContabil(), that.getContaContabil()) &&
			Objects.equals(getPrazoProcessamentoIb(), that.getPrazoProcessamentoIb()) &&
			Objects.equals(getListaRelacaoBancaria(), that.getListaRelacaoBancaria()) &&
			Objects.equals(getBoletoFaturamento(), that.getBoletoFaturamento()) &&
			Objects.equals(getChequeInicial(), that.getChequeInicial());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getTipoContaCorrente(), getInternetBanking(), getChequeCarbono(), getChequeAnterior(), getAntesValor(), getSaltoExtenso(), getAntesExtenso(), getSaltoExtenso2(), getAntesExtenso2(), getSaltoFavorecido(), getAntesFavorecido(), getSaltoData(), getAntesData(), getSaltoEndosso(), getAntesEndosso(), getAntesAssinatura(), getIdentificadorCliente(), getIdentificadorFise(), getContaContabil(), getPrazoProcessamentoIb(), getListaRelacaoBancaria(), getBoletoFaturamento(), getConciliacao(), getChequeInicial());
	}

	public enum TipoContaCorrente {
		NORMAL("Normal"),
		TRANSFERENCIA("Apenas Transferência"),
		INATIVA("Inativa");

		private final String descricao;

		TipoContaCorrente(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Normal";
				case 1:
					return "Apenas Transferência";
				case 2:
					return "Inativa";
				default:
					return "";
			}
		}
	}

	public enum TipoCheque {
		FORMULARIO("Formulário Contínuo"),
		TALAO("Talão");

		private final String descricao;

		private TipoCheque(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Formulário Contínuo";
				case 1:
					return "Talão";
				default:
					return "";
			}
		}

	}
}
