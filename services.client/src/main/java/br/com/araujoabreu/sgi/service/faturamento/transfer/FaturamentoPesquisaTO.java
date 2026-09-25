package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.List;

public class FaturamentoPesquisaTO extends BaseSearchTO {

	private String numero;
	private String situacao;
	private String tipo;
	private String identificacao;
	private String nomeCliente;
	private String numCentroFinanceiro;
	private CentroFinanceiroTO centroFinanceiro;
	private PessoaTO cliente;
	private Long codigoContrato;
	private String numContrato;
	private Double totalDe;
	private Double totalAte;
	private String descricaoItem;
	private String periodo;
	private Double valor;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private Long codigoCidadeFaturamento;
	private Long codigoCidadePrestacaoServico;
	private List<FaturamentoRetencaoTO> faturamentoRetencoes;
	private String tipoFornecimento;
	private String tipoFornecimentoDescr;
	private String tipoFornecimentoDescrCompleta;
	private String tipoDocumentoFaturamento;


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

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNumCentroFinanceiro() {
		return numCentroFinanceiro;
	}

	public void setNumCentroFinanceiro(String numCentroFinanceiro) {
		this.numCentroFinanceiro = numCentroFinanceiro;
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

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public String getNumContrato() {
		return numContrato;
	}

	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
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

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
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

	public Long getCodigoCidadeFaturamento() {
		return codigoCidadeFaturamento;
	}

	public void setCodigoCidadeFaturamento(Long codigoCidadeFaturamento) {
		this.codigoCidadeFaturamento = codigoCidadeFaturamento;
	}

	public List<FaturamentoRetencaoTO> getFaturamentoRetencoes() {
		return faturamentoRetencoes;
	}

	public void setFaturamentoRetencoes(List<FaturamentoRetencaoTO> faturamentoRetencoes) {
		this.faturamentoRetencoes = faturamentoRetencoes;
	}

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	public Long getCodigoCidadePrestacaoServico() {
		return codigoCidadePrestacaoServico;
	}

	public void setCodigoCidadePrestacaoServico(Long codigoCidadePrestacaoServico) {
		this.codigoCidadePrestacaoServico = codigoCidadePrestacaoServico;
	}

	public String getTipoFornecimentoDescr() {
		if (this.tipoFornecimento == null)
			tipoFornecimentoDescr = "";
		else if (this.tipoFornecimento.equals("SERVICOS"))
			tipoFornecimentoDescr = "Serv";
		else if (this.tipoFornecimento.equals("MATERIAIS"))
			tipoFornecimentoDescr = "Mat";
		else if (this.tipoFornecimento.equals("CONJUGADO"))
			tipoFornecimentoDescr = "S/M";

		return tipoFornecimentoDescr;
	}

	public void setTipoFornecimentoDescr(String tipoFornecimentoDescr) {
		this.tipoFornecimentoDescr = tipoFornecimentoDescr;
	}

	public String getTipoFornecimentoDescrCompleta() {
		if (this.tipoFornecimento == null)
			tipoFornecimentoDescrCompleta = "";
		else if (this.tipoFornecimento.equals("SERVICOS"))
			tipoFornecimentoDescrCompleta = "Só serviço";
		else if (this.tipoFornecimento.equals("MATERIAIS"))
			tipoFornecimentoDescrCompleta = "Só material";
		else if (this.tipoFornecimento.equals("CONJUGADO"))
			tipoFornecimentoDescrCompleta = "Serviço/Material";

		return tipoFornecimentoDescrCompleta;
	}

	public void setTipoFornecimentoDescrCompleta(String tipoFornecimentoDescrCompleta) {
		this.tipoFornecimentoDescrCompleta = tipoFornecimentoDescrCompleta;
	}

	public String getTipoDocumentoFaturamento() {
		return tipoDocumentoFaturamento;
	}

	public void setTipoDocumentoFaturamento(String tipoDocumentoFaturamento) {
		this.tipoDocumentoFaturamento = tipoDocumentoFaturamento;
	}
}
