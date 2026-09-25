package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class FaturamentoEtapaPesquisaTO extends BaseSearchTO {

	private Long codigoFaturamento;
	private Long codigoContrato;
	private String tipo;
	private Double valorServico;
	private Double totalDe;
	private Double totalAte;
	private String identificacao;
	private String numCentroFinanceiro;
	private String nomeCliente;
	private String numero;
	private Date dataCancelamento;
	private Date dataFaturamento;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private Date inicio;
	private Date termino;
	private String situacao;
	private BigDecimal valor;
	private BigDecimal valorPermuta;
	private BigDecimal valorDesconto;
	private BigDecimal valorMulta;
	private BigDecimal valorJuros;
	private String descricao;
	private String valorTributos;
	private String valorLiquido;
	private FaturamentoTO faturamento;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private PessoaTO cancelador;
	private String nomeCriador;
	private String nomeAtualizador;
	private String nomeCriadorFaturamento;
	private NotaSaidaTO notaSaida;
	private NotaDebitoTO notaDebito;
	private String tipoFornecimento;
	private String tipoDocumentoFaturamento;

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getValorServico() {
		return valorServico;
	}

	public void setValorServico(Double valorServico) {
		this.valorServico = valorServico;
	}

	public Double getTotalDe() {
		return totalDe;
	}

	public void setTotalDe(Double totalDe) {
		this.totalDe = totalDe;
	}

	public Double getTotalAte() {
		return totalAte;
	}

	public void setTotalAte(Double totalAte) {
		this.totalAte = totalAte;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getNumCentroFinanceiro() {
		return numCentroFinanceiro;
	}

	public void setNumCentroFinanceiro(String numCentroFinanceiro) {
		this.numCentroFinanceiro = numCentroFinanceiro;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public Date getDataFaturamento() {
		return dataFaturamento;
	}

	public void setDataFaturamento(Date dataFaturamento) {
		this.dataFaturamento = dataFaturamento;
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

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorPermuta() {
		return valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValorTributos() {
		return valorTributos;
	}

	public void setValorTributos(String valorTributos) {
		this.valorTributos = valorTributos;
	}

	public String getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(String valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
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

	public String getNomeCriadorFaturamento() {
		return nomeCriadorFaturamento;
	}

	public void setNomeCriadorFaturamento(String nomeCriadorFaturamento) {
		this.nomeCriadorFaturamento = nomeCriadorFaturamento;
	}

	public NotaSaidaTO getNotaSaida() {
		return notaSaida;
	}

	public void setNotaSaida(NotaSaidaTO notaSaida) {
		this.notaSaida = notaSaida;
	}

	public NotaDebitoTO getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebitoTO notaDebito) {
		this.notaDebito = notaDebito;
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
