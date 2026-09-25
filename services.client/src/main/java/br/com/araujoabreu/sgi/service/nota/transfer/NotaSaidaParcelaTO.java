package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoTO;
import br.com.araujoabreu.sgi.service.util.AuditoriaBasicaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotaSaidaParcelaTO extends BaseTO implements AuditoriaBasicaTO {

	private NotaSaidaTO notaSaida;
	private PessoaTO responsavelRecebimento;
	private PessoaTO responsavelCancelamento;
	private PessoaTO responsavelCancRecebimento;
	private Integer ordem;
	private String situacao;
	private BigDecimal valorBruto = BigDecimal.ZERO;
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorPermuta = BigDecimal.ZERO;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataResponsavelRecebimento;
	private Date dataResponsavelCancelamento;
	private Date dataResponsavelCancRecebimento;
	private Date dataAgendamento;
	private List<NotaSaidaParcelaRetencaoTO> parcelaRetencoes;

	private ContaCorrenteTO contaCorrentePrevista;
	private ContaCorrenteTO contaCorrenteRecebimento;
	private String tipoDocumentoPrevisto;
	private String codigoBarrasBoleto;
	private String tipoDocumentoRecebimento;
	private String motivoDataVencimento;
	private String motivoDataPagamento;
	private String motivoDataAgendamento;

	private BigDecimal valorRetencaoContratual = BigDecimal.ZERO;
	private BigDecimal valorRetencaoAcessoria = BigDecimal.ZERO;

	private String action;

	private List<NotaTributoAliquotaTO> listaTributos;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	private String nomeCriador;
	private String nomeAtualizador;

	private Date dataContabilizacao;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private String motivoDevolucao;
	private String motivoDesconto;

	private MotivoDescontoDevolucaoTO devolucaoMotivo;
	private MotivoDescontoDevolucaoTO descontoMotivo;
	private BigDecimal valorTributo = BigDecimal.ZERO;

	private List<NotaSaidaParcelaMotivoDescDevTO> listaNotaSaidaParcelaMotivoDescDev = new ArrayList<>();

	private Boolean cobrancaCronica = false;
	private PessoaTO responsavelMarcaCobrancaCronica;
	private PessoaTO responsavelDesmarcaCobrancaCronica;
	private Date dataResponsavelMarcaCobrancaCronica;
	private Date dataResponsavelDesmarcaCobrancaCronica;

	private String observacoesImpressao;

	private BigDecimal valorRetencaoPrevista;
	private BigDecimal valorRetencaoRetida;
	private BigDecimal valorBaixaRetencao;
	private BigDecimal saldoRetencao;

	public NotaSaidaParcelaTO() {

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

	public PessoaTO getResponsavelCancRecebimento() {
		return responsavelCancRecebimento;
	}

	public void setResponsavelCancRecebimento(PessoaTO responsavelCancRecebimento) {
		this.responsavelCancRecebimento = responsavelCancRecebimento;
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

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
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

	public BigDecimal getValorPermuta() {
		return valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
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

	public Date getDataResponsavelCancRecebimento() {
		return dataResponsavelCancRecebimento;
	}

	public void setDataResponsavelCancRecebimento(Date dataResponsavelCancRecebimento) {
		this.dataResponsavelCancRecebimento = dataResponsavelCancRecebimento;
	}

	public List<NotaSaidaParcelaRetencaoTO> getParcelaRetencoes() {
		return parcelaRetencoes;
	}

	public void setParcelaRetencoes(List<NotaSaidaParcelaRetencaoTO> parcelaRetencoes) {
		this.parcelaRetencoes = parcelaRetencoes;
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

	public String getMotivoDataAgendamento() {
		return motivoDataAgendamento;
	}

	public void setMotivoDataAgendamento(String motivoDataAgendamento) {
		this.motivoDataAgendamento = motivoDataAgendamento;
	}

	public BigDecimal getValorRetencaoContratual() {
		return valorRetencaoContratual;
	}

	public void setValorRetencaoContratual(BigDecimal valorRetencaoContratual) {
		this.valorRetencaoContratual = valorRetencaoContratual;
	}

	public BigDecimal getValorRetencaoAcessoria() {
		return valorRetencaoAcessoria;
	}

	public void setValorRetencaoAcessoria(BigDecimal valorRetencaoAcessoria) {
		this.valorRetencaoAcessoria = valorRetencaoAcessoria;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<NotaTributoAliquotaTO> getListaTributos() {
		return listaTributos;
	}

	public void setListaTributos(List<NotaTributoAliquotaTO> listaTributos) {
		this.listaTributos = listaTributos;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getNomeCriador() {
		return nomeCriador;
	}

	public void setNomeCriador(String nomeCriador) {
		this.nomeCriador = nomeCriador;
	}

	public String getNomeAtualizador() {
		return nomeAtualizador;
	}

	public void setNomeAtualizador(String nomeAtualizador) {
		this.nomeAtualizador = nomeAtualizador;
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

	@Deprecated
	public String getMotivoDevolucao() {
		return motivoDevolucao;
	}

	@Deprecated
	public void setMotivoDevolucao(String motivoDevolucao) {
		this.motivoDevolucao = motivoDevolucao;
	}

	@Deprecated
	public String getMotivoDesconto() {
		return motivoDesconto;
	}

	@Deprecated
	public void setMotivoDesconto(String motivoDesconto) {
		this.motivoDesconto = motivoDesconto;
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public MotivoDescontoDevolucaoTO getDevolucaoMotivo() {
		return devolucaoMotivo;
	}

	public void setDevolucaoMotivo(MotivoDescontoDevolucaoTO devolucaoMotivo) {
		this.devolucaoMotivo = devolucaoMotivo;
	}

	public MotivoDescontoDevolucaoTO getDescontoMotivo() {
		return descontoMotivo;
	}

	public void setDescontoMotivo(MotivoDescontoDevolucaoTO descontoMotivo) {
		this.descontoMotivo = descontoMotivo;
	}

	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	public List<NotaSaidaParcelaMotivoDescDevTO> getListaNotaSaidaParcelaMotivoDescDev() {
		return listaNotaSaidaParcelaMotivoDescDev;
	}

	public void setListaNotaSaidaParcelaMotivoDescDev(List<NotaSaidaParcelaMotivoDescDevTO> listaNotaSaidaParcelaMotivoDescDev) {
		this.listaNotaSaidaParcelaMotivoDescDev = listaNotaSaidaParcelaMotivoDescDev;
	}

	public Boolean getCobrancaCronica() {
		return cobrancaCronica;
	}

	public void setCobrancaCronica(Boolean cobrancaCronica) {
		this.cobrancaCronica = cobrancaCronica;
	}

	public PessoaTO getResponsavelMarcaCobrancaCronica() {
		return responsavelMarcaCobrancaCronica;
	}

	public void setResponsavelMarcaCobrancaCronica(PessoaTO responsavelMarcaCobrancaCronica) {
		this.responsavelMarcaCobrancaCronica = responsavelMarcaCobrancaCronica;
	}

	public PessoaTO getResponsavelDesmarcaCobrancaCronica() {
		return responsavelDesmarcaCobrancaCronica;
	}

	public void setResponsavelDesmarcaCobrancaCronica(PessoaTO responsavelDesmarcaCobrancaCronica) {
		this.responsavelDesmarcaCobrancaCronica = responsavelDesmarcaCobrancaCronica;
	}

	public Date getDataResponsavelMarcaCobrancaCronica() {
		return dataResponsavelMarcaCobrancaCronica;
	}

	public void setDataResponsavelMarcaCobrancaCronica(Date dataResponsavelMarcaCobrancaCronica) {
		this.dataResponsavelMarcaCobrancaCronica = dataResponsavelMarcaCobrancaCronica;
	}

	public Date getDataResponsavelDesmarcaCobrancaCronica() {
		return dataResponsavelDesmarcaCobrancaCronica;
	}

	public void setDataResponsavelDesmarcaCobrancaCronica(Date dataResponsavelDesmarcaCobrancaCronica) {
		this.dataResponsavelDesmarcaCobrancaCronica = dataResponsavelDesmarcaCobrancaCronica;
	}

	public String getObservacoesImpressao() {
		return observacoesImpressao;
	}

	public void setObservacoesImpressao(String observacoesImpressao) {
		this.observacoesImpressao = observacoesImpressao;
	}

	public BigDecimal getValorRetencaoPrevista() {
		return valorRetencaoPrevista;
	}

	public void setValorRetencaoPrevista(BigDecimal valorRetencaoPrevista) {
		this.valorRetencaoPrevista = valorRetencaoPrevista;
	}

	public BigDecimal getValorRetencaoRetida() {
		return valorRetencaoRetida;
	}

	public void setValorRetencaoRetida(BigDecimal valorRetencaoRetida) {
		this.valorRetencaoRetida = valorRetencaoRetida;
	}

	public BigDecimal getValorBaixaRetencao() {
		return valorBaixaRetencao;
	}

	public void setValorBaixaRetencao(BigDecimal valorBaixaRetencao) {
		this.valorBaixaRetencao = valorBaixaRetencao;
	}

	public BigDecimal getSaldoRetencao() {
		return saldoRetencao;
	}

	public void setSaldoRetencao(BigDecimal saldoRetencao) {
		this.saldoRetencao = saldoRetencao;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (!(o instanceof NotaSaidaParcelaTO)) return false;
		if (!super.equals(o)) return false;
		NotaSaidaParcelaTO that = (NotaSaidaParcelaTO) o;
		return Objects.equals(getNotaSaida(), that.getNotaSaida()) &&
			Objects.equals(getResponsavelRecebimento(), that.getResponsavelRecebimento()) &&
			Objects.equals(getResponsavelCancelamento(), that.getResponsavelCancelamento()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			Objects.equals(getSituacao(), that.getSituacao()) &&
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
			Objects.equals(getTipoDocumentoPrevisto(), that.getTipoDocumentoPrevisto()) &&
			Objects.equals(getCodigoBarrasBoleto(), that.getCodigoBarrasBoleto()) &&
			Objects.equals(getTipoDocumentoRecebimento(), that.getTipoDocumentoRecebimento()) &&
			Objects.equals(getMotivoDataVencimento(), that.getMotivoDataVencimento()) &&
			Objects.equals(getMotivoDataPagamento(), that.getMotivoDataPagamento()) &&
			Objects.equals(getValorRetencaoContratual(), that.getValorRetencaoContratual()) &&
			Objects.equals(getValorRetencaoAcessoria(), that.getValorRetencaoAcessoria()) &&
			Objects.equals(getAction(), that.getAction()) &&
			Objects.equals(getListaTributos(), that.getListaTributos()) &&
			Objects.equals(getCriador(), that.getCriador()) &&
			Objects.equals(getDataCriacao(), that.getDataCriacao()) &&
			Objects.equals(getAtualizador(), that.getAtualizador()) &&
			Objects.equals(getDataAtualizacao(), that.getDataAtualizacao()) &&
			Objects.equals(getNomeCriador(), that.getNomeCriador()) &&
			Objects.equals(getNomeAtualizador(), that.getNomeAtualizador()) &&
			Objects.equals(getDevolucaoMotivo(), that.getDevolucaoMotivo()) &&
			Objects.equals(getDescontoMotivo(), that.getDescontoMotivo()) &&
			Objects.equals(getValorTributo(), that.getValorTributo()) &&
			Objects.equals(getCobrancaCronica(), that.getCobrancaCronica()) &&
			Objects.equals(getResponsavelMarcaCobrancaCronica(), that.getResponsavelMarcaCobrancaCronica()) &&
			Objects.equals(getResponsavelDesmarcaCobrancaCronica(), that.getResponsavelDesmarcaCobrancaCronica()) &&
			Objects.equals(getDataResponsavelMarcaCobrancaCronica(), that.getDataResponsavelMarcaCobrancaCronica()) &&
			Objects.equals(getDataResponsavelDesmarcaCobrancaCronica(), that.getDataResponsavelDesmarcaCobrancaCronica());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNotaSaida(), getResponsavelRecebimento(), getResponsavelCancelamento(), getOrdem(), getSituacao(), getValorBruto(),
			getValorLiquido(), getValorDesconto(), getValorMulta(), getValorJuros(), getValorPermuta(), getDataVencimento(), getDataPagamento(), getDataResponsavelRecebimento(),
			getDataResponsavelCancelamento(), getParcelaRetencoes(), getContaCorrentePrevista(), getContaCorrenteRecebimento(), getTipoDocumentoPrevisto(), getCodigoBarrasBoleto(),
			getTipoDocumentoRecebimento(), getMotivoDataVencimento(), getMotivoDataPagamento(), getValorRetencaoContratual(), getValorRetencaoAcessoria(), getAction(), getListaTributos(),
			getCriador(), getDataCriacao(), getAtualizador(), getDataAtualizacao(), getNomeCriador(), getNomeAtualizador(), getDevolucaoMotivo(), getDescontoMotivo(), getValorTributo(),
			getCobrancaCronica(), getResponsavelMarcaCobrancaCronica(), getResponsavelDesmarcaCobrancaCronica(), getDataResponsavelMarcaCobrancaCronica(), getDataResponsavelDesmarcaCobrancaCronica());
	}

	@Override
	public String toString() {
		return "NotaSaidaParcelaTO{" +
			"notaSaida=" + notaSaida +
			", responsavelRecebimento=" + responsavelRecebimento +
			", responsavelCancelamento=" + responsavelCancelamento +
			", ordem=" + ordem +
			", situacao='" + situacao + '\'' +
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
			", tipoDocumentoPrevisto='" + tipoDocumentoPrevisto + '\'' +
			", codigoBarrasBoleto='" + codigoBarrasBoleto + '\'' +
			", tipoDocumentoRecebimento='" + tipoDocumentoRecebimento + '\'' +
			", motivoDataVencimento='" + motivoDataVencimento + '\'' +
			", motivoDataPagamento='" + motivoDataPagamento + '\'' +
			", valorRetencaoContratual=" + valorRetencaoContratual +
			", valorRetencaoAcessoria=" + valorRetencaoAcessoria +
			", action='" + action + '\'' +
			", listaTributos=" + listaTributos +
			", criador='" + criador + '\'' +
			", dataCriacao=" + dataCriacao +
			", atualizador='" + atualizador + '\'' +
			", dataAtualizacao=" + dataAtualizacao +
			", nomeCriador='" + nomeCriador + '\'' +
			", nomeAtualizador='" + nomeAtualizador + '\'' +
			", cobrancaCronica=" + cobrancaCronica +
			", responsavelMarcaCobrancaCronica=" + responsavelMarcaCobrancaCronica +
			", responsavelDesmarcaCobrancaCronica=" + responsavelDesmarcaCobrancaCronica +
			", dataResponsavelMarcaCobrancaCronica=" + dataResponsavelMarcaCobrancaCronica +
			", dataResponsavelDesmarcaCobrancaCronica=" + dataResponsavelDesmarcaCobrancaCronica +
			'}';
	}
}
