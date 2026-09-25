package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoTO;
import br.com.araujoabreu.sgi.service.util.AuditoriaBasicaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FaturamentoTO extends BaseTO implements AuditoriaBasicaTO {


	private ContaCorrenteTO contaCorrente;
	private String numero;
	private String situacao;
	private String tipo;
	private String periodo;
	private String identificacao;
	private Boolean issRetidoCliente;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private BigDecimal percentualServico;
	private String valor;
	private String tipoDocumentoPagamento;
	private CentroFinanceiroTO centroFinanceiro;
	private PessoaTO cliente;
	private PessoaTO subcliente;
	private TipoCobrancaFaturamentoTO tipoCobrancaFaturamento;
	private List<FaturamentoEtapaTO> faturamentoEtapas = new ArrayList<>();
	private List<FaturamentoAliquotaTO> faturamentoAliquotas = new ArrayList<>();
	private Date dataVencimento1;
	private Date dataVencimento2;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private PessoaTO cancelador;
	private Timestamp dataCancelamento;
	private String nomeCriador;
	private String nomeAtualizador;
	private ContratoCidadePrestacaoServicosTO cidadeFaturamento;
	private CidadeTO cidadePrestacaoServico;
	private List<FaturamentoRetencaoTO> faturamentoRetencoes = new ArrayList<>();
	private ServicoTipoTO ServicoTipo;
	private String resumo;
	private String gerencia;
	private String gerente;
	private String tipoFornecimento;
	private String tipoDocumentoFaturamento;
	private MenssagemPadraoTO menssagemPadraoTO;

	public MenssagemPadraoTO getMenssagemPadraoTO() {
		return menssagemPadraoTO;
	}

	public void setMenssagemPadraoTO(MenssagemPadraoTO menssagemPadraoTO) {
		this.menssagemPadraoTO = menssagemPadraoTO;
	}

	public ContaCorrenteTO getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteTO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public Boolean getIssRetidoCliente() {
		return issRetidoCliente;
	}

	public void setIssRetidoCliente(Boolean issRetidoCliente) {
		this.issRetidoCliente = issRetidoCliente;
	}

	public String getDiscriminacaoCentral() {
		return discriminacaoCentral;
	}

	public void setDiscriminacaoCentral(String discriminacaoCentral) {
		this.discriminacaoCentral = discriminacaoCentral;
	}

	public String getDiscriminacaoFinal() {
		return discriminacaoFinal;
	}

	public void setDiscriminacaoFinal(String discriminacaoFinal) {
		this.discriminacaoFinal = discriminacaoFinal;
	}

	public String getDiscriminacaoInicial() {
		return discriminacaoInicial;
	}

	public void setDiscriminacaoInicial(String discriminacaoInicial) {
		this.discriminacaoInicial = discriminacaoInicial;
	}

	public BigDecimal getPercentualServico() {
		return percentualServico;
	}

	public void setPercentualServico(BigDecimal percentualServico) {
		this.percentualServico = percentualServico;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public PessoaTO getSubcliente() {
		return subcliente;
	}

	public void setSubcliente(PessoaTO subcliente) {
		this.subcliente = subcliente;
	}

	public TipoCobrancaFaturamentoTO getTipoCobrancaFaturamento() {
		return tipoCobrancaFaturamento;
	}

	public void setTipoCobrancaFaturamento(TipoCobrancaFaturamentoTO tipoCobrancaFaturamento) {
		this.tipoCobrancaFaturamento = tipoCobrancaFaturamento;
	}

	public List<FaturamentoEtapaTO> getFaturamentoEtapas() {
		return faturamentoEtapas;
	}

	public void setFaturamentoEtapas(List<FaturamentoEtapaTO> faturamentoEtapas) {
		this.faturamentoEtapas = faturamentoEtapas;
	}

	public List<FaturamentoAliquotaTO> getFaturamentoAliquotas() {
		return faturamentoAliquotas;
	}

	public void setFaturamentoAliquotas(List<FaturamentoAliquotaTO> faturamentoAliquotas) {
		this.faturamentoAliquotas = faturamentoAliquotas;
	}

	public Date getDataVencimento1() {
		return dataVencimento1;
	}

	public void setDataVencimento1(Date dataVencimento1) {
		this.dataVencimento1 = dataVencimento1;
	}

	public Date getDataVencimento2() {
		return dataVencimento2;
	}

	public void setDataVencimento2(Date dataVencimento2) {
		this.dataVencimento2 = dataVencimento2;
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

	public PessoaTO getCancelador() {
		return cancelador;
	}

	public void setCancelador(PessoaTO cancelador) {
		this.cancelador = cancelador;
	}

	public Timestamp getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Timestamp dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
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

	public ContratoCidadePrestacaoServicosTO getCidadeFaturamento() {
		return cidadeFaturamento;
	}

	public void setCidadeFaturamento(ContratoCidadePrestacaoServicosTO cidadeFaturamento) {
		this.cidadeFaturamento = cidadeFaturamento;
	}

	public CidadeTO getCidadePrestacaoServico() {
		return cidadePrestacaoServico;
	}

	public void setCidadePrestacaoServico(CidadeTO cidadePrestacaoServico) {
		this.cidadePrestacaoServico = cidadePrestacaoServico;
	}

	public List<FaturamentoRetencaoTO> getFaturamentoRetencoes() {
		return faturamentoRetencoes;
	}

	public void setFaturamentoRetencoes(List<FaturamentoRetencaoTO> faturamentoRetencoes) {
		this.faturamentoRetencoes = faturamentoRetencoes;
	}

	public ServicoTipoTO getServicoTipo() {
		return ServicoTipo;
	}

	public void setServicoTipo(ServicoTipoTO servicoTipo) {
		ServicoTipo = servicoTipo;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getGerencia() {
		return gerencia;
	}

	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}

	public String getGerente() {
		return gerente;
	}

	public void setGerente(String gerente) {
		this.gerente = gerente;
	}

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	public String getTipoDocumentoFaturamento() {
		return tipoDocumentoFaturamento;
	}

	public void setTipoDocumentoFaturamento(String tipoDocumentoFaturamento) {
		this.tipoDocumentoFaturamento = tipoDocumentoFaturamento;
	}
}




