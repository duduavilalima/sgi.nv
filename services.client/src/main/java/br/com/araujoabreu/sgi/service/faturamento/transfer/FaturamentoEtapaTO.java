package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class FaturamentoEtapaTO extends BaseTO {
	private String numero;
	private Date dataCancelamento;
	private Date dataFaturamento;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private Date inicio;
	private Date termino;
	private String situacao;
	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorPermuta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	private String descricao;
	private String valorTributos;
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
	private Boolean exibeRetencaoContratual = Boolean.TRUE;
	private Boolean exibeRetencaoAcessoria = Boolean.TRUE;
	private Boolean exibePermuta = Boolean.TRUE;
	private BigDecimal ultAlteracaoValor;
	private Date dataUltAlteracaoValor;
	private String nomeResponsavelUltAlteracao;
	private NotaDebitoTO notaDebito;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getValorTributos() {
		return valorTributos;
	}

	public void setValorTributos(String valorTributos) {
		this.valorTributos = valorTributos;
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

	public String getNomeCriadorFaturamento() {
		return nomeCriadorFaturamento;
	}

	public void setNomeCriadorFaturamento(String nomeCriadorFaturamento) {
		this.nomeCriadorFaturamento = nomeCriadorFaturamento;
	}

	public PessoaTO getCancelador() {
		return cancelador;
	}

	public void setCancelador(PessoaTO cancelador) {
		this.cancelador = cancelador;
	}

	public NotaSaidaTO getNotaSaida() {
		return notaSaida;
	}

	public void setNotaSaida(NotaSaidaTO notaSaida) {
		this.notaSaida = notaSaida;
	}

	public Boolean getExibeRetencaoContratual() {
		return exibeRetencaoContratual;
	}

	public void setExibeRetencaoContratual(Boolean exibeRetencaoContratual) {
		this.exibeRetencaoContratual = exibeRetencaoContratual;
	}

	public Boolean getExibeRetencaoAcessoria() {
		return exibeRetencaoAcessoria;
	}

	public void setExibeRetencaoAcessoria(Boolean exibeRetencaoAcessoria) {
		this.exibeRetencaoAcessoria = exibeRetencaoAcessoria;
	}

	public Boolean getExibePermuta() {
		return exibePermuta;
	}

	public void setExibePermuta(Boolean exibePermuta) {
		this.exibePermuta = exibePermuta;
	}

	public BigDecimal getUltAlteracaoValor() {
		return ultAlteracaoValor;
	}

	public void setUltAlteracaoValor(BigDecimal ultAlteracaoValor) {
		this.ultAlteracaoValor = ultAlteracaoValor;
	}

	public Date getDataUltAlteracaoValor() {
		return dataUltAlteracaoValor;
	}

	public void setDataUltAlteracaoValor(Date dataUltAlteracaoValor) {
		this.dataUltAlteracaoValor = dataUltAlteracaoValor;
	}

	public String getNomeResponsavelUltAlteracao() {
		return nomeResponsavelUltAlteracao;
	}

	public void setNomeResponsavelUltAlteracao(String nomeResponsavelUltAlteracao) {
		this.nomeResponsavelUltAlteracao = nomeResponsavelUltAlteracao;
	}

	public NotaDebitoTO getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebitoTO notaDebito) {
		this.notaDebito = notaDebito;
	}
}
