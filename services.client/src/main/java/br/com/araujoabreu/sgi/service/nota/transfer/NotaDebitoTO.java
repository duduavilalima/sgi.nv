package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NotaDebitoTO extends BaseTO {

	private UnidadeTO empresa;
	private String numero;
	private String situacao;
	private String tipoDocumentoPrevisto;
	private String tipoDocumentoRecebimento;
	private String tipoFornecimento;
	private CentroFinanceiroTO centroFinanceiro;
	private ContaCorrenteTO contaCorrentePrevista;
	private ContaCorrenteTO contaCorrenteRecebimento;
	private String criador;
	private String atualizador;
	private Date dataAtualizacao;
	private Date dataCriacao;
	private Date dataEmissao;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataAgendamento;
	private Date dataContabilizacao;
	private String descricao;
	private ServicoTipoTO servicoTipo;
	private BigDecimal valorBruto = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private String protocoloEntrega;
	private FaturamentoEtapaTO faturamentoEtapaTO;

	private BigDecimal valorTibutoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorDescontoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorDevolucaoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorLiquidoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;

	private BigDecimal valorTributoRecebido = BigDecimal.ZERO;
	private BigDecimal valorLiquidoRecebido = BigDecimal.ZERO;
	private List<FaturamentoEtapaTO> faturamentoEtapas;
	private PessoaTO responsavelNumeracao;
	private PessoaTO responsavelCancelarNumeracao;
	private PessoaTO responsavelRecebimento;
	private PessoaTO responsavelCancelarRecebimento;
	private PessoaTO responsavelCancelamento;
	private PessoaTO responsavelReverterCancelamento;
	private Date dataResponsavelNumeracao;
	private Date dataResponsavelCancelarNumeracao;
	private Date dataResponsavelRecebimento;
	private Date dataResponsavelCancelarRecebimento;
	private Date dataResponsavelCancelamento;
	private Date dataResponsavelReverterCancelamento;
	private PessoaTO cliente;

	private BigDecimal valorDescontoRecebido = BigDecimal.ZERO;
	private BigDecimal valorDevolucaoRecebido = BigDecimal.ZERO;

	private int etapaNumero;
	private String etapaDescricao;

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
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

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public String getProtocoloEntrega() {
		return protocoloEntrega;
	}

	public void setProtocoloEntrega(String protocoloEntrega) {
		this.protocoloEntrega = protocoloEntrega;
	}

	public List<FaturamentoEtapaTO> getFaturamentoEtapas() {
		return faturamentoEtapas;
	}

	public void setFaturamentoEtapas(List<FaturamentoEtapaTO> faturamentoEtapas) {
		this.faturamentoEtapas = faturamentoEtapas;
	}

	public PessoaTO getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(PessoaTO responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getValorTributoRecebido() {
		return valorTributoRecebido;
	}

	public void setValorTributoRecebido(BigDecimal valorTributoRecebido) {
		this.valorTributoRecebido = valorTributoRecebido;
	}

	public BigDecimal getValorDescontoRecebido() {
		return valorDescontoRecebido;
	}

	public void setValorDescontoRecebido(BigDecimal valorDescontoRecebido) {
		this.valorDescontoRecebido = valorDescontoRecebido;
	}

	public BigDecimal getValorDevolucaoRecebido() {
		return valorDevolucaoRecebido;
	}

	public void setValorDevolucaoRecebido(BigDecimal valorDevolucaoRecebido) {
		this.valorDevolucaoRecebido = valorDevolucaoRecebido;
	}

	public FaturamentoEtapaTO getFaturamentoEtapaTO() {
		return faturamentoEtapaTO;
	}

	public void setFaturamentoEtapaTO(FaturamentoEtapaTO faturamentoEtapaTO) {
		this.faturamentoEtapaTO = faturamentoEtapaTO;
	}

	public int getEtapaNumero() {
		return etapaNumero;
	}

	public void setEtapaNumero(int etapaNumero) {
		this.etapaNumero = etapaNumero;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public String getSituacao() {
		return situacao;
	}

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorLiquidoRecebido() {
		return valorLiquidoRecebido;
	}

	public void setValorLiquidoRecebido(BigDecimal valorLiquidoRecebido) {
		this.valorLiquidoRecebido = valorLiquidoRecebido;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public PessoaTO getResponsavelRecebimento() {
		return responsavelRecebimento;
	}

	public void setResponsavelRecebimento(PessoaTO responsavelRecebimento) {
		this.responsavelRecebimento = responsavelRecebimento;
	}

	public Date getDataResponsavelCancelarRecebimento() {
		return dataResponsavelCancelarRecebimento;
	}

	public void setDataResponsavelCancelarRecebimento(Date dataResponsavelCancelarRecebimento) {
		this.dataResponsavelCancelarRecebimento = dataResponsavelCancelarRecebimento;
	}

	public Date getDataResponsavelCancelamento() {
		return dataResponsavelCancelamento;
	}

	public void setDataResponsavelCancelamento(Date dataResponsavelCancelamento) {
		this.dataResponsavelCancelamento = dataResponsavelCancelamento;
	}

	public Date getDataResponsavelRecebimento() {
		return dataResponsavelRecebimento;
	}

	public void setDataResponsavelRecebimento(Date dataResponsavelRecebimento) {
		this.dataResponsavelRecebimento = dataResponsavelRecebimento;
	}

	public String getTipoDocumentoPrevisto() {
		return tipoDocumentoPrevisto;
	}

	public void setTipoDocumentoPrevisto(String tipoDocumentoPrevisto) {
		this.tipoDocumentoPrevisto = tipoDocumentoPrevisto;
	}

	public String getTipoDocumentoRecebimento() {
		return tipoDocumentoRecebimento;
	}

	public void setTipoDocumentoRecebimento(String tipoDocumentoRecebimento) {
		this.tipoDocumentoRecebimento = tipoDocumentoRecebimento;
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

	public PessoaTO getResponsavelCancelarRecebimento() {
		return responsavelCancelarRecebimento;
	}

	public void setResponsavelCancelarRecebimento(PessoaTO responsavelCancelarRecebimento) {
		this.responsavelCancelarRecebimento = responsavelCancelarRecebimento;
	}

	public BigDecimal getValorTibutoPrevisto() {
		return valorTibutoPrevisto;
	}

	public void setValorTibutoPrevisto(BigDecimal valorTibutoPrevisto) {
		this.valorTibutoPrevisto = valorTibutoPrevisto;
	}

	public BigDecimal getValorDescontoPrevisto() {
		return valorDescontoPrevisto;
	}

	public void setValorDescontoPrevisto(BigDecimal valorDescontoPrevisto) {
		this.valorDescontoPrevisto = valorDescontoPrevisto;
	}

	public BigDecimal getValorDevolucaoPrevisto() {
		return valorDevolucaoPrevisto;
	}

	public void setValorDevolucaoPrevisto(BigDecimal valorDevolucaoPrevisto) {
		this.valorDevolucaoPrevisto = valorDevolucaoPrevisto;
	}

	public BigDecimal getValorLiquidoPrevisto() {
		return valorLiquidoPrevisto;
	}

	public void setValorLiquidoPrevisto(BigDecimal valorLiquidoPrevisto) {
		this.valorLiquidoPrevisto = valorLiquidoPrevisto;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public ServicoTipoTO getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipoTO servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	public PessoaTO getResponsavelNumeracao() {
		return responsavelNumeracao;
	}

	public void setResponsavelNumeracao(PessoaTO responsavelNumeracao) {
		this.responsavelNumeracao = responsavelNumeracao;
	}

	public PessoaTO getResponsavelCancelarNumeracao() {
		return responsavelCancelarNumeracao;
	}

	public void setResponsavelCancelarNumeracao(PessoaTO responsavelCancelarNumeracao) {
		this.responsavelCancelarNumeracao = responsavelCancelarNumeracao;
	}

	public PessoaTO getResponsavelReverterCancelamento() {
		return responsavelReverterCancelamento;
	}

	public void setResponsavelReverterCancelamento(PessoaTO responsavelReverterCancelamento) {
		this.responsavelReverterCancelamento = responsavelReverterCancelamento;
	}

	public Date getDataResponsavelNumeracao() {
		return dataResponsavelNumeracao;
	}

	public void setDataResponsavelNumeracao(Date dataResponsavelNumeracao) {
		this.dataResponsavelNumeracao = dataResponsavelNumeracao;
	}

	public Date getDataResponsavelCancelarNumeracao() {
		return dataResponsavelCancelarNumeracao;
	}

	public void setDataResponsavelCancelarNumeracao(Date dataResponsavelCancelarNumeracao) {
		this.dataResponsavelCancelarNumeracao = dataResponsavelCancelarNumeracao;
	}

	public Date getDataResponsavelReverterCancelamento() {
		return dataResponsavelReverterCancelamento;
	}

	public void setDataResponsavelReverterCancelamento(Date dataResponsavelReverterCancelamento) {
		this.dataResponsavelReverterCancelamento = dataResponsavelReverterCancelamento;
	}

	public String getEtapaDescricao() {
		return etapaDescricao;
	}

	public void setEtapaDescricao(String etapaDescricao) {
		this.etapaDescricao = etapaDescricao;
	}
}
