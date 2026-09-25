package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CentroFinanceiroTO extends BaseTO {

	private String nome;
	private String identificador;
	private String identificadorFinal;
	private ContratoTO contratoTO;
	private UnidadeTO unidade;
	private List<Long> listaCodEmpresa;
	private CentroFinanceiroTO pai;

	private String situacao;
	private Boolean cumulativo;
	private ColaboradorTO engenheiroResponsavel;
	private ColaboradorTO contatoAA;
	private String observacao;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String nomeCriador;
	private String nomeAtualizador;

	private String nomeAtivacao;
	private Date dataAtivacao;
	private String nomeInativacao;
	private Date dataInativacao;

	private String nomeEncerramento;
	private Date dataEncerramento;
	private String nomeParalisacao;
	private Date dataParalisacao;
	private String nomeReativacao;
	private Date dataReativacao;
	private String nomeCancelamento;
	private Date dataCancelamento;
	private String nomeEncerramentoComFat;
	private Date dataEncerramentoComFat;

	private Boolean permiteClienteLivre;

	private String formaPagamento;
	private String garantias;
	private String criterioMedicao;
	private Date dataPrevisaoReajuste;
	private Integer totalHH;

	private BigDecimal valorTotalCentroFinanceiro;
	private BigDecimal valorMensalCentroFinanceiro;

	private Integer proximoNumOC;
	private String tipo;
	private String cei;

	private String nomeEngenheiroResposavel;
	private FluxoTO fluxo;

	private Date dataPrevisaoPrimeiraParcela;

	private Integer prazo;

	private BigDecimal custoMensal;
	private BigDecimal percentualHomologado;
	private BigDecimal custoHomologado;
	private BigDecimal percentualLancado;
	private BigDecimal custoLancado;

	private String custoMensalCurrency;
	private String percentualHomologadoCurrency;
	private String custoHomologadoCurrency;
	private String percentualLancadoCurrency;
	private String custoLancadoCurrency;
	private List<String> emailJobCabecalho =new ArrayList<String>() {
		{
			add("Gerência");
			add("CL");
			add("Orçado sem MO");
			add("Despesa homologada (CAP)");
			add("%");
			add("Despesa total");
			add("%");
		}
	};
	private List<String> custoLancadoFuturoCurrency;

	public CentroFinanceiroTO() {

	}

	public CentroFinanceiroTO(Long codigo) {
		this.setCodigo(codigo);
	}

	public CentroFinanceiroTO(Long codigo, String nome) {
		this.setCodigo(codigo);
		this.nome = nome;
	}

	public CentroFinanceiroTO(Long codigo, String nome, String identificador, String identificadorFinal) {
		this.setCodigo(codigo);
		this.nome = nome;
		this.identificador = identificador;
		this.identificadorFinal = identificadorFinal;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getIdentificadorFinal() {
		return identificadorFinal;
	}

	public void setIdentificadorFinal(String identificadorFinal) {
		this.identificadorFinal = identificadorFinal;
	}

	public ContratoTO getContratoTO() {
		return contratoTO;
	}

	public void setContratoTO(ContratoTO contratoTO) {
		this.contratoTO = contratoTO;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public CentroFinanceiroTO getPai() {
		return pai;
	}

	public void setPai(CentroFinanceiroTO pai) {
		this.pai = pai;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getCumulativo() {
		return cumulativo;
	}

	public void setCumulativo(Boolean cumulativo) {
		this.cumulativo = cumulativo;
	}

	public ColaboradorTO getEngenheiroResponsavel() {
		return engenheiroResponsavel;
	}

	public void setEngenheiroResponsavel(ColaboradorTO engenheiroResponsavel) {
		this.engenheiroResponsavel = engenheiroResponsavel;
	}

	public ColaboradorTO getContatoAA() {
		return contatoAA;
	}

	public void setContatoAA(ColaboradorTO contatoAA) {
		this.contatoAA = contatoAA;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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

	public String getNomeAtivacao() {
		return nomeAtivacao;
	}

	public void setNomeAtivacao(String nomeAtivacao) {
		this.nomeAtivacao = nomeAtivacao;
	}

	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public String getNomeInativacao() {
		return nomeInativacao;
	}

	public void setNomeInativacao(String nomeInativacao) {
		this.nomeInativacao = nomeInativacao;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public String getNomeEncerramento() {
		return nomeEncerramento;
	}

	public void setNomeEncerramento(String nomeEncerramento) {
		this.nomeEncerramento = nomeEncerramento;
	}

	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	public String getNomeParalisacao() {
		return nomeParalisacao;
	}

	public void setNomeParalisacao(String nomeParalisacao) {
		this.nomeParalisacao = nomeParalisacao;
	}

	public Date getDataParalisacao() {
		return dataParalisacao;
	}

	public void setDataParalisacao(Date dataParalisacao) {
		this.dataParalisacao = dataParalisacao;
	}

	public String getNomeReativacao() {
		return nomeReativacao;
	}

	public void setNomeReativacao(String nomeReativacao) {
		this.nomeReativacao = nomeReativacao;
	}

	public Date getDataReativacao() {
		return dataReativacao;
	}

	public void setDataReativacao(Date dataReativacao) {
		this.dataReativacao = dataReativacao;
	}

	public String getNomeCancelamento() {
		return nomeCancelamento;
	}

	public void setNomeCancelamento(String nomeCancelamento) {
		this.nomeCancelamento = nomeCancelamento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getNomeEncerramentoComFat() {
		return nomeEncerramentoComFat;
	}

	public void setNomeEncerramentoComFat(String nomeEncerramentoComFat) {
		this.nomeEncerramentoComFat = nomeEncerramentoComFat;
	}

	public Date getDataEncerramentoComFat() {
		return dataEncerramentoComFat;
	}

	public void setDataEncerramentoComFat(Date dataEncerramentoComFat) {
		this.dataEncerramentoComFat = dataEncerramentoComFat;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getGarantias() {
		return garantias;
	}

	public void setGarantias(String garantias) {
		this.garantias = garantias;
	}

	public String getCriterioMedicao() {
		return criterioMedicao;
	}

	public void setCriterioMedicao(String criterioMedicao) {
		this.criterioMedicao = criterioMedicao;
	}

	public Date getDataPrevisaoReajuste() {
		return dataPrevisaoReajuste;
	}

	public void setDataPrevisaoReajuste(Date dataPrevisaoReajuste) {
		this.dataPrevisaoReajuste = dataPrevisaoReajuste;
	}

	public Integer getTotalHH() {
		return totalHH;
	}

	public void setTotalHH(Integer totalHH) {
		this.totalHH = totalHH;
	}

	public BigDecimal getValorTotalCentroFinanceiro() {
		return valorTotalCentroFinanceiro;
	}

	public void setValorTotalCentroFinanceiro(BigDecimal valorTotalCentroFinanceiro) {
		this.valorTotalCentroFinanceiro = valorTotalCentroFinanceiro;
	}

	public BigDecimal getValorMensalCentroFinanceiro() {
		return valorMensalCentroFinanceiro;
	}

	public void setValorMensalCentroFinanceiro(BigDecimal valorMensalCentroFinanceiro) {
		this.valorMensalCentroFinanceiro = valorMensalCentroFinanceiro;
	}

	public Integer getProximoNumOC() {
		return proximoNumOC;
	}

	public void setProximoNumOC(Integer proximoNumOC) {
		this.proximoNumOC = proximoNumOC;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCei() {
		return cei;
	}

	public void setCei(String cei) {
		this.cei = cei;
	}

	public String getNomeEngenheiroResposavel() {
		return nomeEngenheiroResposavel;
	}

	public void setNomeEngenheiroResposavel(String nomeEngenheiroResposavel) {
		this.nomeEngenheiroResposavel = nomeEngenheiroResposavel;
	}

	public FluxoTO getFluxo() {
		return fluxo;
	}

	public void setFluxo(FluxoTO fluxo) {
		this.fluxo = fluxo;
	}

	public Date getDataPrevisaoPrimeiraParcela() {
		return dataPrevisaoPrimeiraParcela;
	}

	public void setDataPrevisaoPrimeiraParcela(Date dataPrevisaoPrimeiraParcela) {
		this.dataPrevisaoPrimeiraParcela = dataPrevisaoPrimeiraParcela;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public BigDecimal getCustoMensal() {
		return custoMensal;
	}

	public void setCustoMensal(BigDecimal custoMensal) {
		this.custoMensal = custoMensal;
	}

	public BigDecimal getCustoHomologado() {
		return custoHomologado;
	}

	public void setCustoHomologado(BigDecimal custoHomologado) {
		this.custoHomologado = custoHomologado;
	}

	public BigDecimal getCustoLancado() {
		return custoLancado;
	}

	public void setCustoLancado(BigDecimal custoLancado) {
		this.custoLancado = custoLancado;
	}

	public BigDecimal getPercentualHomologado() {
		return percentualHomologado;
	}

	public void setPercentualHomologado(BigDecimal percentualHomologado) {
		this.percentualHomologado = percentualHomologado;
	}

	public BigDecimal getPercentualLancado() {
		return percentualLancado;
	}

	public void setPercentualLancado(BigDecimal percentualLancado) {
		this.percentualLancado = percentualLancado;
	}

	public String getCustoMensalCurrency() {
		return custoMensalCurrency;
	}

	public void setCustoMensalCurrency(String custoMensalCurrency) {
		this.custoMensalCurrency = custoMensalCurrency;
	}

	public String getPercentualHomologadoCurrency() {
		return percentualHomologadoCurrency;
	}

	public void setPercentualHomologadoCurrency(String percentualHomologadoCurrency) {
		this.percentualHomologadoCurrency = percentualHomologadoCurrency;
	}

	public String getCustoHomologadoCurrency() {
		return custoHomologadoCurrency;
	}

	public void setCustoHomologadoCurrency(String custoHomologadoCurrency) {
		this.custoHomologadoCurrency = custoHomologadoCurrency;
	}

	public String getPercentualLancadoCurrency() {
		return percentualLancadoCurrency;
	}

	public void setPercentualLancadoCurrency(String percentualLancadoCurrency) {
		this.percentualLancadoCurrency = percentualLancadoCurrency;
	}

	public String getCustoLancadoCurrency() {
		return custoLancadoCurrency;
	}

	public void setCustoLancadoCurrency(String custoLancadoCurrency) {
		this.custoLancadoCurrency = custoLancadoCurrency;
	}

	public List<String> getEmailJobCabecalho() {
		return emailJobCabecalho;
	}

	public void setEmailJobCabecalho(List<String> emailJobCabecalho) {
		this.emailJobCabecalho = emailJobCabecalho;
	}

	public List<String> getCustoLancadoFuturoCurrency() {
		return custoLancadoFuturoCurrency;
	}

	public void setCustoLancadoFuturoCurrency(List<String> custoLancadoFuturoCurrency) {
		this.custoLancadoFuturoCurrency = custoLancadoFuturoCurrency;
	}

	public List<Long> getListaCodEmpresa() {
		return listaCodEmpresa;
	}

	public void setListaCodEmpresa(List<Long> listaCodEmpresa) {
		this.listaCodEmpresa = listaCodEmpresa;
	}

	public Boolean getPermiteClienteLivre() {
		return permiteClienteLivre;
	}

	public void setPermiteClienteLivre(Boolean permiteClienteLivre) {
		this.permiteClienteLivre = permiteClienteLivre;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CentroFinanceiroTO)) return false;
		if (!super.equals(o)) return false;
		CentroFinanceiroTO that = (CentroFinanceiroTO) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getIdentificador(), that.getIdentificador()) &&
			Objects.equals(getIdentificadorFinal(), that.getIdentificadorFinal()) &&
			Objects.equals(getSituacao(), that.getSituacao()) &&
			Objects.equals(getCumulativo(), that.getCumulativo()) &&
			Objects.equals(getObservacao(), that.getObservacao()) &&
			Objects.equals(getCriador(), that.getCriador()) &&
			Objects.equals(getDataCriacao(), that.getDataCriacao()) &&
			Objects.equals(getAtualizador(), that.getAtualizador()) &&
			Objects.equals(getDataAtualizacao(), that.getDataAtualizacao()) &&
			Objects.equals(getNomeCriador(), that.getNomeCriador()) &&
			Objects.equals(getNomeAtualizador(), that.getNomeAtualizador()) &&
			Objects.equals(getNomeAtivacao(), that.getNomeAtivacao()) &&
			Objects.equals(getDataAtivacao(), that.getDataAtivacao()) &&
			Objects.equals(getNomeInativacao(), that.getNomeInativacao()) &&
			Objects.equals(getDataInativacao(), that.getDataInativacao()) &&
			Objects.equals(getNomeEncerramento(), that.getNomeEncerramento()) &&
			Objects.equals(getDataEncerramento(), that.getDataEncerramento()) &&
			Objects.equals(getNomeParalisacao(), that.getNomeParalisacao()) &&
			Objects.equals(getDataParalisacao(), that.getDataParalisacao()) &&
			Objects.equals(getNomeReativacao(), that.getNomeReativacao()) &&
			Objects.equals(getDataReativacao(), that.getDataReativacao()) &&
			Objects.equals(getNomeCancelamento(), that.getNomeCancelamento()) &&
			Objects.equals(getDataCancelamento(), that.getDataCancelamento()) &&
			Objects.equals(getNomeEncerramentoComFat(), that.getNomeEncerramentoComFat()) &&
			Objects.equals(getDataEncerramentoComFat(), that.getDataEncerramentoComFat()) &&
			Objects.equals(getFormaPagamento(), that.getFormaPagamento()) &&
			Objects.equals(getGarantias(), that.getGarantias()) &&
			Objects.equals(getCriterioMedicao(), that.getCriterioMedicao()) &&
			Objects.equals(getDataPrevisaoReajuste(), that.getDataPrevisaoReajuste()) &&
			Objects.equals(getTotalHH(), that.getTotalHH()) &&
			Objects.equals(getValorTotalCentroFinanceiro(), that.getValorTotalCentroFinanceiro()) &&
			Objects.equals(getValorMensalCentroFinanceiro(), that.getValorMensalCentroFinanceiro()) &&
			Objects.equals(getProximoNumOC(), that.getProximoNumOC()) &&
			Objects.equals(getTipo(), that.getTipo()) &&
			Objects.equals(getCei(), that.getCei()) &&
			Objects.equals(getNomeEngenheiroResposavel(), that.getNomeEngenheiroResposavel());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getIdentificador(), getIdentificadorFinal(), getSituacao(), getCumulativo(), getObservacao(), getCriador(), getDataCriacao(), getAtualizador(), getDataAtualizacao(), getNomeCriador(), getNomeAtualizador(), getNomeAtivacao(), getDataAtivacao(), getNomeInativacao(), getDataInativacao(), getNomeEncerramento(), getDataEncerramento(), getNomeParalisacao(), getDataParalisacao(), getNomeReativacao(), getDataReativacao(), getNomeCancelamento(), getDataCancelamento(), getFormaPagamento(), getGarantias(), getCriterioMedicao(), getDataPrevisaoReajuste(), getTotalHH(), getValorTotalCentroFinanceiro(), getValorMensalCentroFinanceiro(), getProximoNumOC(), getTipo(), getCei(), getNomeEngenheiroResposavel(), getNomeEncerramentoComFat(), getDataEncerramentoComFat());
	}
}

