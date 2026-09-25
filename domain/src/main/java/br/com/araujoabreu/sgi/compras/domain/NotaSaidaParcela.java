package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "NOTA_SAIDA_PARCELA")
@SequenceGenerator(name="SEQ_NOTA_SAIDA_PARCELA", sequenceName="SEQ_NOTA_SAIDA_PARCELA", allocationSize=1)
public class NotaSaidaParcela extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 3133611477229596222L;
	private NotaSaida notaSaida;
	private Pessoa responsavelRecebimento;
	private Pessoa responsavelCancelamento;
	private Pessoa responsavelCancRecebimento;
	private Integer ordem;
	private SituacaoParcela situacao;
	private BigDecimal valorBruto;
	private BigDecimal valorLiquido;
	private BigDecimal valorDesconto;
	private BigDecimal valorMulta;
	private BigDecimal valorJuros;
	private BigDecimal valorPermuta;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataResponsavelRecebimento;
	private Date dataResponsavelCancelamento;
	private Date dataResponsavelCancRecebimento;
	private List<NotaSaidaParcelaRetencao> parcelaRetencoes;
	private ContaCorrente contaCorrentePrevista;
	private ContaCorrente contaCorrenteRecebimento;
	private TipoDocumentoPagamento tipoDocumentoPrevisto;
	private String codigoBarrasBoleto;
	private TipoDocumentoPagamento tipoDocumentoRecebimento;
	private String motivoDataVencimento;
	private String motivoDataPagamento;
	private String motivoDataAgendamento;
	private Date dataContabilizacao;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private Date dataAgendamento;
	private BigDecimal valorTributo = BigDecimal.ZERO;

	private Boolean cobrancaCronica = false;
	private Pessoa responsavelMarcaCobrancaCronica;
	private Pessoa responsavelDesmarcaCobrancaCronica;
	private Date dataResponsavelMarcaCobrancaCronica;
	private Date dataResponsavelDesmarcaCobrancaCronica;

	private BigDecimal valorRetencaoPrevista = BigDecimal.ZERO;
	private BigDecimal valorRetencaoRetida = BigDecimal.ZERO;
	private BigDecimal valorBaixaRetencao = BigDecimal.ZERO;
	private BigDecimal saldoRetencao = BigDecimal.ZERO;

	private String observacoesImpressao;

	public NotaSaidaParcela() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_SAIDA", nullable = false)
	public NotaSaida getNotaSaida() {
		return this.notaSaida;
	}

	public void setNotaSaida(NotaSaida notaSaida) {
		this.notaSaida = notaSaida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_RECEBIMENTO")
	public Pessoa getResponsavelRecebimento() {
		return responsavelRecebimento;
	}

	public void setResponsavelRecebimento(Pessoa responsavelRecebimento) {
		this.responsavelRecebimento = responsavelRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_CANCELAMENTO")
	public Pessoa getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(Pessoa responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_CANC_RECEBIMENTO")
	public Pessoa getResponsavelCancRecebimento() {
		return responsavelCancRecebimento;
	}

	public void setResponsavelCancRecebimento(Pessoa responsavelCancRecebimento) {
		this.responsavelCancRecebimento = responsavelCancRecebimento;
	}

	@Column(nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(nullable = false)
	public SituacaoParcela getSituacao() {
		return this.situacao;
	}

	public void setSituacao(SituacaoParcela situacao) {
		this.situacao = situacao;
	}

	@Column(name = "valor_bruto", nullable = false, precision = 19, scale = 2)
	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valor) {
		this.valorBruto = valor;
	}

	@Column(name = "valor_liquido", nullable = false, precision = 19, scale = 2)
	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	@Column(name = "VALOR_DESCONTO", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "VALOR_MULTA", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Column(name = "VALOR_JUROS", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	@Column(name = "VALOR_PERMUTA", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorPermuta() {
		return valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
	}

	@Column(name = "DATA_VENCIMENTO")
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@Column(name = "DATA_PAGAMENTO")
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Column(name = "DATA_RESP_RECEBIMENTO")
	public Date getDataResponsavelRecebimento() {
		return dataResponsavelRecebimento;
	}

	public void setDataResponsavelRecebimento(Date dataResponsavelRecebimento) {
		this.dataResponsavelRecebimento = dataResponsavelRecebimento;
	}

	@Column(name = "DATA_RESP_CANCELAMENTO")
	public Date getDataResponsavelCancelamento() {
		return dataResponsavelCancelamento;
	}

	public void setDataResponsavelCancelamento(Date dataResponsavelCancelamento) {
		this.dataResponsavelCancelamento = dataResponsavelCancelamento;
	}

	@Column(name = "DATA_RESP_CANC_RECEBIMENTO")
	public Date getDataResponsavelCancRecebimento() {
		return dataResponsavelCancRecebimento;
	}

	public void setDataResponsavelCancRecebimento(Date dataResponsavelCancRecebimento) {
		this.dataResponsavelCancRecebimento = dataResponsavelCancRecebimento;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parcela")
	public List<NotaSaidaParcelaRetencao> getParcelaRetencoes() {
		return parcelaRetencoes;
	}

	public void setParcelaRetencoes(List<NotaSaidaParcelaRetencao> parcelaRetencoes) {
		this.parcelaRetencoes = parcelaRetencoes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE_PREVISTA")
	public ContaCorrente getContaCorrentePrevista() {
		return contaCorrentePrevista;
	}

	public void setContaCorrentePrevista(ContaCorrente contaCorrentePrevista) {
		this.contaCorrentePrevista = contaCorrentePrevista;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE_RECEBIMENTO")
	public ContaCorrente getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(ContaCorrente contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	@Column(name = "TIPO_DOCUMENTO_PREVISTO")
	public TipoDocumentoPagamento getTipoDocumentoPrevisto() {
		return tipoDocumentoPrevisto;
	}

	public void setTipoDocumentoPrevisto(TipoDocumentoPagamento tipoDocumentoPrevisto) {
		this.tipoDocumentoPrevisto = tipoDocumentoPrevisto;
	}

	@Column(name = "CODIGO_BARRAS_BOLETO", length = 44)
	public String getCodigoBarrasBoleto() {
		return codigoBarrasBoleto;
	}

	public void setCodigoBarrasBoleto(String codigoBarrasBoleto) {
		this.codigoBarrasBoleto = codigoBarrasBoleto;
	}

	@Column(name = "TIPO_DOCUMENTO_RECEBIMENTO")
	public TipoDocumentoPagamento getTipoDocumentoRecebimento() {
		return tipoDocumentoRecebimento;
	}

	public void setTipoDocumentoRecebimento(TipoDocumentoPagamento tipoDocumentoRecebimento) {
		this.tipoDocumentoRecebimento = tipoDocumentoRecebimento;
	}

	@Column(name = "MOTIVO_DATA_VENCIMENTO")
	public String getMotivoDataVencimento() {
		return motivoDataVencimento;
	}

	public void setMotivoDataVencimento(String motivoDataVencimento) {
		this.motivoDataVencimento = motivoDataVencimento;
	}

	@Column(name = "MOTIVO_DATA_PAGAMENTO")
	public String getMotivoDataPagamento() {
		return motivoDataPagamento;
	}

	public void setMotivoDataPagamento(String motivoDataPagamento) {
		this.motivoDataPagamento = motivoDataPagamento;
	}

	@Column(name = "MOTIVO_DATA_AGENDAMENTO")
	public String getMotivoDataAgendamento() {
		return motivoDataAgendamento;
	}

	public void setMotivoDataAgendamento(String motivoDataAgendamento) {
		this.motivoDataAgendamento = motivoDataAgendamento;
	}

	@Column(name = "data_contabilizacao")
	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	@Column(name = "valor_devolucao", nullable = false, precision = 2)
	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_AGENDAMENTO", nullable = false)
	@NotNull
	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	@Column(name = "VALOR_TRIBUTO", precision = 2)
	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	@Column(name = "COBRANCA_CRONICA")
	public Boolean getCobrancaCronica() {
		return cobrancaCronica;
	}

	public void setCobrancaCronica(Boolean cobrancaCronica) {
		this.cobrancaCronica = cobrancaCronica;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_MARCA_COBRANCA_CRONICA")
	public Pessoa getResponsavelMarcaCobrancaCronica() {
		return responsavelMarcaCobrancaCronica;
	}

	public void setResponsavelMarcaCobrancaCronica(Pessoa responsavelMarcaCobrancaCronica) {
		this.responsavelMarcaCobrancaCronica = responsavelMarcaCobrancaCronica;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_DESMARCA_COBRANCA_CRONICA")
	public Pessoa getResponsavelDesmarcaCobrancaCronica() {
		return responsavelDesmarcaCobrancaCronica;
	}

	public void setResponsavelDesmarcaCobrancaCronica(Pessoa responsavelDesmarcaCobrancaCronica) {
		this.responsavelDesmarcaCobrancaCronica = responsavelDesmarcaCobrancaCronica;
	}

	@Column(name = "DATA_RESP_MARCA_COBRANCA_CRONICA")
	public Date getDataResponsavelMarcaCobrancaCronica() {
		return dataResponsavelMarcaCobrancaCronica;
	}

	public void setDataResponsavelMarcaCobrancaCronica(Date dataResponsavelMarcaCobrancaCronica) {
		this.dataResponsavelMarcaCobrancaCronica = dataResponsavelMarcaCobrancaCronica;
	}

	@Column(name = "DATA_RESP_DESMARCA_COBRANCA_CRONICA")
	public Date getDataResponsavelDesmarcaCobrancaCronica() {
		return dataResponsavelDesmarcaCobrancaCronica;
	}

	public void setDataResponsavelDesmarcaCobrancaCronica(Date dataResponsavelDesmarcaCobrancaCronica) {
		this.dataResponsavelDesmarcaCobrancaCronica = dataResponsavelDesmarcaCobrancaCronica;
	}

	@Length(max = 2000, message = "Observações deve ter no máximo 2000 caracteres.")
	@Column(name = "observacoes_impressao", length = 2000)
	public String getObservacoesImpressao() {
		return observacoesImpressao;
	}

	public void setObservacoesImpressao(String observacoesImpressao) {
		this.observacoesImpressao = observacoesImpressao;
	}

	@Column(name = "VALOR_RETENCAO_PREVISTA", nullable=false, precision=9, scale=2)
	public BigDecimal getValorRetencaoPrevista() {
		return valorRetencaoPrevista;
	}

	public void setValorRetencaoPrevista(BigDecimal valorRetencaoPrevista) {
		this.valorRetencaoPrevista = valorRetencaoPrevista;
	}

	@Column(name = "VALOR_RETENCAO_RETIDA", nullable=false, precision=9, scale=2)
	public BigDecimal getValorRetencaoRetida() {
		return valorRetencaoRetida;
	}

	public void setValorRetencaoRetida(BigDecimal valorRetencaoRetida) {
		this.valorRetencaoRetida = valorRetencaoRetida;
	}

	@Column(name = "VALOR_BAIXA_RETENCAO", nullable=false, precision=9, scale=2)
	public BigDecimal getValorBaixaRetencao() {
		return valorBaixaRetencao;
	}

	public void setValorBaixaRetencao(BigDecimal valorBaixaRetencao) {
		this.valorBaixaRetencao = valorBaixaRetencao;
	}

	@Column(name = "SALDO_RETENCAO", nullable=false, precision=9, scale=2)
	public BigDecimal getSaldoRetencao() {
		return saldoRetencao;
	}

	public void setSaldoRetencao(BigDecimal saldoRetencao) {
		this.saldoRetencao = saldoRetencao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaSaidaParcela)) return false;
		if (!super.equals(o)) return false;
		NotaSaidaParcela that = (NotaSaidaParcela) o;
		return Objects.equals(getNotaSaida(), that.getNotaSaida()) &&
			Objects.equals(getResponsavelRecebimento(), that.getResponsavelRecebimento()) &&
			Objects.equals(getResponsavelCancelamento(), that.getResponsavelCancelamento()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			getSituacao() == that.getSituacao() &&
			Objects.equals(getValorBruto(), that.getValorBruto()) &&
			Objects.equals(getValorLiquido(), that.getValorLiquido()) &&
			Objects.equals(getValorDesconto(), that.getValorDesconto()) &&
			Objects.equals(getValorMulta(), that.getValorMulta()) &&
			Objects.equals(getValorJuros(), that.getValorJuros()) &&
			Objects.equals(getValorPermuta(), that.getValorPermuta()) &&
			Objects.equals(getDataVencimento(), that.getDataVencimento()) &&
			Objects.equals(getDataPagamento(), that.getDataPagamento()) &&
			Objects.equals(getDataResponsavelRecebimento(), that.getDataResponsavelRecebimento()) &&
			Objects.equals(getDataResponsavelCancelamento(), that.getDataResponsavelCancelamento()) &&
			Objects.equals(getParcelaRetencoes(), that.getParcelaRetencoes()) &&
			Objects.equals(getContaCorrentePrevista(), that.getContaCorrentePrevista()) &&
			Objects.equals(getContaCorrenteRecebimento(), that.getContaCorrenteRecebimento()) &&
			getTipoDocumentoPrevisto() == that.getTipoDocumentoPrevisto() &&
			Objects.equals(getCodigoBarrasBoleto(), that.getCodigoBarrasBoleto()) &&
			getTipoDocumentoRecebimento() == that.getTipoDocumentoRecebimento() &&
			Objects.equals(getMotivoDataVencimento(), that.getMotivoDataVencimento()) &&
			Objects.equals(getMotivoDataPagamento(), that.getMotivoDataPagamento()) &&
			Objects.equals(getDataContabilizacao(), that.getDataContabilizacao()) &&
			Objects.equals(getValorDevolucao(), that.getValorDevolucao()) &&
			Objects.equals(getValorTributo(), that.getValorTributo()) &&
			Objects.equals(getCobrancaCronica(), that.getCobrancaCronica()) &&
			Objects.equals(getResponsavelMarcaCobrancaCronica(), that.getResponsavelMarcaCobrancaCronica()) &&
			Objects.equals(getResponsavelDesmarcaCobrancaCronica(), that.getResponsavelDesmarcaCobrancaCronica()) &&
			Objects.equals(getDataResponsavelMarcaCobrancaCronica(), that.getDataResponsavelMarcaCobrancaCronica()) &&
			Objects.equals(getDataResponsavelDesmarcaCobrancaCronica(), that.getDataResponsavelDesmarcaCobrancaCronica());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNotaSaida(), getResponsavelRecebimento(), getResponsavelCancelamento(), getOrdem(),
			getSituacao(), getValorBruto(), getValorLiquido(), getValorDesconto(), getValorMulta(), getValorJuros(), getValorPermuta(),
			getDataVencimento(), getDataPagamento(), getDataResponsavelRecebimento(), getDataResponsavelCancelamento(), getParcelaRetencoes(),
			getContaCorrentePrevista(), getContaCorrenteRecebimento(), getTipoDocumentoPrevisto(), getCodigoBarrasBoleto(), getTipoDocumentoRecebimento(),
			getMotivoDataVencimento(), getMotivoDataPagamento(), getDataContabilizacao(), getValorDevolucao(), getValorTributo(), getCobrancaCronica(),
			getResponsavelMarcaCobrancaCronica(), getResponsavelDesmarcaCobrancaCronica(), getDataResponsavelMarcaCobrancaCronica(), getDataResponsavelDesmarcaCobrancaCronica());
	}


	@Override
	public String toString() {
		return "NotaSaidaParcela{" +
			"notaSaida=" + notaSaida +
			", responsavelRecebimento=" + responsavelRecebimento +
			", responsavelCancelamento=" + responsavelCancelamento +
			", ordem=" + ordem +
			", situacao=" + situacao +
			", valorBruto=" + valorBruto +
			", valorLiquido=" + valorLiquido +
			", valorDesconto=" + valorDesconto +
			", valorMulta=" + valorMulta +
			", valorJuros=" + valorJuros +
			", valorPermuta=" + valorPermuta +
			", dataVencimento=" + dataVencimento +
			", dataPagamento=" + dataPagamento +
			", dataResponsavelRecebimento=" + dataResponsavelRecebimento +
			", dataResponsavelCancelamento=" + dataResponsavelCancelamento +
			", parcelaRetencoes=" + parcelaRetencoes +
			", contaCorrentePrevista=" + contaCorrentePrevista +
			", contaCorrenteRecebimento=" + contaCorrenteRecebimento +
			", tipoDocumentoPrevisto=" + tipoDocumentoPrevisto +
			", codigoBarrasBoleto='" + codigoBarrasBoleto + '\'' +
			", tipoDocumentoRecebimento=" + tipoDocumentoRecebimento +
			", motivoDataVencimento='" + motivoDataVencimento + '\'' +
			", motivoDataPagamento='" + motivoDataPagamento + '\'' +
			", dataContabilizacao=" + dataContabilizacao +
			", valorDevolucao=" + valorDevolucao +
			", cobrancaCronica=" + cobrancaCronica +
			", responsavelMarcaCobrancaCronica=" + responsavelMarcaCobrancaCronica +
			", responsavelDesmarcaCobrancaCronica=" + responsavelDesmarcaCobrancaCronica +
			", dataResponsavelMarcaCobrancaCronica=" + dataResponsavelMarcaCobrancaCronica +
			", dataResponsavelDesmarcaCobrancaCronica=" + dataResponsavelDesmarcaCobrancaCronica +
			'}';
	}

	public enum SituacaoParcela {
		ABERTA("Aberta (NF não processada)"),
		EM_RECEBIMENTO("Em recebimento"),
		FINALIZADA("Finalizada (recebida)"),
		CANCELADA("Cancelada");

		private final String descricao;

		SituacaoParcela(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}
}
