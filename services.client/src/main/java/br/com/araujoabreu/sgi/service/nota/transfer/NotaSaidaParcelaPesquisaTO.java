package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NotaSaidaParcelaPesquisaTO extends BaseSearchTO {

	private NotaSaidaTO notaSaida;
	private PessoaTO responsavelRecebimento;
	private PessoaTO responsavelCancelamento;
	private Integer ordem;
	private String situacao;
	private BigDecimal valorBruto = BigDecimal.ZERO;
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorPermuta = BigDecimal.ZERO;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataResponsavelRecebimento;
	private Date dataResponsavelCancelamento;
	private Date dataResponsavelCancelamentoDe;
	private Date dataResponsavelCancelamentoAte;
	private CidadeTO cidade;
	private Long codigoLote;
	private Long servicoTipoCodigo;
	private List<NotaSaidaParcelaRetencaoTO> parcelaRetencoes;
	private Long codigoCidade;
	private String nomeFantasiaDocumento;
	private FaturamentoEtapaTO etapa;
	private Date dataEmissaoDe;
	private Date dataEmissaoAte;
	private Date dataVencimentoDe;
	private Date dataVencimentoAte;
	private Date dataPagamentoDe;
	private Date dataPagamentoAte;
	private String tipoData;
	private Date dataDe;
	private Date dataAte;

	private ContaCorrenteTO contaCorrentePrevista;
	private String codigoBarrasBoleto;
	private String tipoDocumentoRecebimento;
	private String motivoDataVencimento;
	private String motivoDataPagamento;

	private Date dataContabilizacao;
	private Date dataContabilizacaoDe;
	private Date dataContabilizacaoAte;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private String motivoDevolucao;
	private String motivoDesconto;
	private Date dataAgendamentoDe;
	private Date dataAgendamentoAte;

	private String observacoesImpressao;

	@Deprecated
	private BigDecimal valorLiquidoDe;

	@Deprecated
	private BigDecimal valorLiquidoAte;

	@Deprecated
	private ContaCorrenteTO contaCorrenteRecebimento;

	@Deprecated
	private String tipoDocumentoPrevisto;

	public NotaSaidaParcelaPesquisaTO() {

	}

	public NotaSaidaTO getNotaSaida() {
		return notaSaida;
	}

	public void setNotaSaida(NotaSaidaTO notaSaida) {
		this.notaSaida = notaSaida;
	}

	public PessoaTO getResponsavelRecebimento() {
		return responsavelRecebimento;
	}

	public void setResponsavelRecebimento(PessoaTO responsavelRecebimento) {
		this.responsavelRecebimento = responsavelRecebimento;
	}

	public PessoaTO getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(PessoaTO responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorDe() {
		return valorDe;
	}

	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}

	public BigDecimal getValorAte() {
		return valorAte;
	}

	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}

	public BigDecimal getValorLiquidoDe() {
		return valorLiquidoDe;
	}

	public void setValorLiquidoDe(BigDecimal valorLiquidoDe) {
		this.valorLiquidoDe = valorLiquidoDe;
	}

	public BigDecimal getValorLiquidoAte() {
		return valorLiquidoAte;
	}

	public void setValorLiquidoAte(BigDecimal valorLiquidoAte) {
		this.valorLiquidoAte = valorLiquidoAte;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataResponsavelRecebimento() {
		return dataResponsavelRecebimento;
	}

	public void setDataResponsavelRecebimento(Date dataResponsavelRecebimento) {
		this.dataResponsavelRecebimento = dataResponsavelRecebimento;
	}

	public Date getDataResponsavelCancelamento() {
		return dataResponsavelCancelamento;
	}

	public void setDataResponsavelCancelamento(Date dataResponsavelCancelamento) {
		this.dataResponsavelCancelamento = dataResponsavelCancelamento;
	}

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

	public Long getCodigoLote() {
		return codigoLote;
	}

	public void setCodigoLote(Long codigoLote) {
		this.codigoLote = codigoLote;
	}

	public Long getServicoTipoCodigo() {
		return servicoTipoCodigo;
	}

	public void setServicoTipoCodigo(Long servicoTipoCodigo) {
		this.servicoTipoCodigo = servicoTipoCodigo;
	}

	public List<NotaSaidaParcelaRetencaoTO> getParcelaRetencoes() {
		return parcelaRetencoes;
	}

	public void setParcelaRetencoes(List<NotaSaidaParcelaRetencaoTO> parcelaRetencoes) {
		this.parcelaRetencoes = parcelaRetencoes;
	}

	public Long getCodigoCidade() {
		return codigoCidade;
	}

	public void setCodigoCidade(Long codigoCidade) {
		this.codigoCidade = codigoCidade;
	}

	public String getNomeFantasiaDocumento() {
		return nomeFantasiaDocumento;
	}

	public void setNomeFantasiaDocumento(String nomeFantasiaDocumento) {
		this.nomeFantasiaDocumento = nomeFantasiaDocumento;
	}

	public FaturamentoEtapaTO getEtapa() {
		return etapa;
	}

	public void setEtapa(FaturamentoEtapaTO etapa) {
		this.etapa = etapa;
	}

	public Date getDataEmissaoDe() {
		return dataEmissaoDe;
	}

	public void setDataEmissaoDe(Date dataEmissaoDe) {
		this.dataEmissaoDe = dataEmissaoDe;
	}

	public Date getDataEmissaoAte() {
		return dataEmissaoAte;
	}

	public void setDataEmissaoAte(Date dataEmissaoAte) {
		this.dataEmissaoAte = dataEmissaoAte;
	}

	public Date getDataVencimentoDe() {
		return dataVencimentoDe;
	}

	public void setDataVencimentoDe(Date dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}

	public Date getDataVencimentoAte() {
		return dataVencimentoAte;
	}

	public void setDataVencimentoAte(Date dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

	public Date getDataPagamentoDe() {
		return dataPagamentoDe;
	}

	public void setDataPagamentoDe(Date dataPagamentoDe) {
		this.dataPagamentoDe = dataPagamentoDe;
	}

	public Date getDataPagamentoAte() {
		return dataPagamentoAte;
	}

	public void setDataPagamentoAte(Date dataPagamentoAte) {
		this.dataPagamentoAte = dataPagamentoAte;
	}

	public ContaCorrenteTO getContaCorrentePrevista() {
		return contaCorrentePrevista;
	}

	public void setContaCorrentePrevista(ContaCorrenteTO contaCorrentePrevista) {
		this.contaCorrentePrevista = contaCorrentePrevista;
	}

	public ContaCorrenteTO getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(ContaCorrenteTO contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getTipoDocumentoPrevisto() {
		return tipoDocumentoPrevisto;
	}

	public void setTipoDocumentoPrevisto(String tipoDocumentoPrevisto) {
		this.tipoDocumentoPrevisto = tipoDocumentoPrevisto;
	}

	public String getCodigoBarrasBoleto() {
		return codigoBarrasBoleto;
	}

	public void setCodigoBarrasBoleto(String codigoBarrasBoleto) {
		this.codigoBarrasBoleto = codigoBarrasBoleto;
	}

	public String getTipoDocumentoRecebimento() {
		return tipoDocumentoRecebimento;
	}

	public void setTipoDocumentoRecebimento(String tipoDocumentoRecebimento) {
		this.tipoDocumentoRecebimento = tipoDocumentoRecebimento;
	}

	public String getMotivoDataVencimento() {
		return motivoDataVencimento;
	}

	public void setMotivoDataVencimento(String motivoDataVencimento) {
		this.motivoDataVencimento = motivoDataVencimento;
	}

	public String getMotivoDataPagamento() {
		return motivoDataPagamento;
	}

	public void setMotivoDataPagamento(String motivoDataPagamento) {
		this.motivoDataPagamento = motivoDataPagamento;
	}

	public BigDecimal getValorPermuta() {
		return valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	public String getMotivoDevolucao() {
		return motivoDevolucao;
	}

	public void setMotivoDevolucao(String motivoDevolucao) {
		this.motivoDevolucao = motivoDevolucao;
	}

	public String getMotivoDesconto() {
		return motivoDesconto;
	}

	public void setMotivoDesconto(String motivoDesconto) {
		this.motivoDesconto = motivoDesconto;
	}

	public Date getDataContabilizacaoDe() {
		return dataContabilizacaoDe;
	}

	public void setDataContabilizacaoDe(Date dataContabilizacaoDe) {
		this.dataContabilizacaoDe = dataContabilizacaoDe;
	}

	public Date getDataContabilizacaoAte() {
		return dataContabilizacaoAte;
	}

	public void setDataContabilizacaoAte(Date dataContabilizacaoAte) {
		this.dataContabilizacaoAte = dataContabilizacaoAte;
	}

	public Date getDataAgendamentoDe() {
		return dataAgendamentoDe;
	}

	public void setDataAgendamentoDe(Date dataAgendamentoDe) {
		this.dataAgendamentoDe = dataAgendamentoDe;
	}

	public Date getDataAgendamentoAte() {
		return dataAgendamentoAte;
	}

	public void setDataAgendamentoAte(Date dataAgendamentoAte) {
		this.dataAgendamentoAte = dataAgendamentoAte;
	}

	public String getObservacoesImpressao() {
		return observacoesImpressao;
	}

	public void setObservacoesImpressao(String observacoesImpressao) {
		this.observacoesImpressao = observacoesImpressao;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public Date getDataResponsavelCancelamentoDe() {
		return dataResponsavelCancelamentoDe;
	}

	public void setDataResponsavelCancelamentoDe(Date dataResponsavelCancelamentoDe) {
		this.dataResponsavelCancelamentoDe = dataResponsavelCancelamentoDe;
	}

	public Date getDataResponsavelCancelamentoAte() {
		return dataResponsavelCancelamentoAte;
	}

	public void setDataResponsavelCancelamentoAte(Date dataResponsavelCancelamentoAte) {
		this.dataResponsavelCancelamentoAte = dataResponsavelCancelamentoAte;
	}
}
