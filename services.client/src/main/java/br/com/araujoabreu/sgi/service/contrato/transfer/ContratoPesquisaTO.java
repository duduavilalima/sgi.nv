package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class ContratoPesquisaTO extends BaseSearchTO {

	private String numero;
	private String tipo;
	private String situacao;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private String prazoAtual;
	private Double valorMensal;
	private String periodoReajuste;
	private String resumo;
	private String resumoFaturamento;
	private String contratoOriginal;
	private Double valorTotal;
	private BigDecimal percentualRetencao;
	private String estagioComercial;
	private DiretoriaSimpleTO diretoria;
	private PessoaTO cliente;
	private Date dataAssinatura;
	private String proposta;
	private Double valorTotalDe;
	private Double valorTotalAte;
	private UnidadeTO empresa;
	private Date assinaturaDe;
	private Date assinaturaAte;
	private Date vigenciaDe;
	private Date vigenciaAte;
	private GerenciaTO gerencia;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	public String getPrazoAtual() {
		return prazoAtual;
	}

	public void setPrazoAtual(String prazoAtual) {
		this.prazoAtual = prazoAtual;
	}

	public Double getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(Double valorMensal) {
		this.valorMensal = valorMensal;
	}

	public String getPeriodoReajuste() {
		return periodoReajuste;
	}

	public void setPeriodoReajuste(String periodoReajuste) {
		this.periodoReajuste = periodoReajuste;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getResumoFaturamento() {
		return resumoFaturamento;
	}

	public void setResumoFaturamento(String resumoFaturamento) {
		this.resumoFaturamento = resumoFaturamento;
	}

	public String getContratoOriginal() {
		return contratoOriginal;
	}

	public void setContratoOriginal(String contratoOriginal) {
		this.contratoOriginal = contratoOriginal;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getPercentualRetencao() {
		return percentualRetencao;
	}

	public void setPercentualRetencao(BigDecimal percentualRetencao) {
		this.percentualRetencao = percentualRetencao;
	}

	public String getEstagioComercial() {
		return estagioComercial;
	}

	public void setEstagioComercial(String estagioComercial) {
		this.estagioComercial = estagioComercial;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public String getProposta() {
		return proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	public Double getValorTotalDe() {
		return valorTotalDe;
	}

	public void setValorTotalDe(Double valorTotalDe) {
		this.valorTotalDe = valorTotalDe;
	}

	public Double getValorTotalAte() {
		return valorTotalAte;
	}

	public void setValorTotalAte(Double valorTotalAte) {
		this.valorTotalAte = valorTotalAte;
	}

	public UnidadeTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeTO empresa) {
		this.empresa = empresa;
	}

	public Date getAssinaturaDe() {
		return assinaturaDe;
	}

	public void setAssinaturaDe(Date assinaturaDe) {
		this.assinaturaDe = assinaturaDe;
	}

	public Date getAssinaturaAte() {
		return assinaturaAte;
	}

	public void setAssinaturaAte(Date assinaturaAte) {
		this.assinaturaAte = assinaturaAte;
	}

	public Date getVigenciaDe() {
		return vigenciaDe;
	}

	public void setVigenciaDe(Date vigenciaDe) {
		this.vigenciaDe = vigenciaDe;
	}

	public Date getVigenciaAte() {
		return vigenciaAte;
	}

	public void setVigenciaAte(Date vigenciaAte) {
		this.vigenciaAte = vigenciaAte;
	}

	public GerenciaTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(GerenciaTO gerencia) {
		this.gerencia = gerencia;
	}
}
