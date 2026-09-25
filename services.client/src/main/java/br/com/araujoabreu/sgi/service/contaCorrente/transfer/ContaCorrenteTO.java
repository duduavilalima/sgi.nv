package br.com.araujoabreu.sgi.service.contaCorrente.transfer;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaCorrenteTO extends DadosBancariosTO {

	private String tipoContaCorrente;
	private Boolean internetBanking = Boolean.FALSE;
	private Boolean chequeCarbono;
	private Long chequeAnterior = 0L;
	private Integer antesValor;
	private Integer saltoExtenso;
	private Integer antesExtenso;
	private Integer saltoExtenso2;
	private Integer antesExtenso2;
	private Integer saltoFavorecido;
	private Integer antesFavorecido;
	private Integer saltoData;
	private Integer antesData;
	private Integer saltoEndosso;
	private Integer antesEndosso;
	private Integer antesAssinatura;
	private Integer identificadorCliente;
	private Integer identificadorFise;
	private Integer prazoProcessamentoIb;
	private Boolean boletoFaturamento = Boolean.FALSE;
	private ContaContabilTO contaContabil;
	private boolean conciliacao;
	private Long chequeInicial = 0L;
	private List<FaixaChequeTO> listaFaixaDeCheques;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String nomeCriador;
	private String nomeAtualizador;
	private String nomeInativador;

	private UnidadeTO unidade;

	public ContaCorrenteTO() {
	}

	public String getTipoContaCorrente() {
		return tipoContaCorrente;
	}

	public void setTipoContaCorrente(String tipoContaCorrente) {
		this.tipoContaCorrente = tipoContaCorrente;
	}

	public Boolean getInternetBanking() {
		return internetBanking;
	}

	public void setInternetBanking(Boolean internetBanking) {
		this.internetBanking = internetBanking;
	}

	public Boolean getChequeCarbono() {
		return chequeCarbono;
	}

	public void setChequeCarbono(Boolean chequeCarbono) {
		this.chequeCarbono = chequeCarbono;
	}

	public Long getChequeAnterior() {
		return chequeAnterior;
	}

	public void setChequeAnterior(Long chequeAnterior) {
		this.chequeAnterior = chequeAnterior;
	}

	public Integer getAntesValor() {
		return antesValor;
	}

	public void setAntesValor(Integer antesValor) {
		this.antesValor = antesValor;
	}

	public Integer getSaltoExtenso() {
		return saltoExtenso;
	}

	public void setSaltoExtenso(Integer saltoExtenso) {
		this.saltoExtenso = saltoExtenso;
	}

	public Integer getAntesExtenso() {
		return antesExtenso;
	}

	public void setAntesExtenso(Integer antesExtenso) {
		this.antesExtenso = antesExtenso;
	}

	public Integer getSaltoExtenso2() {
		return saltoExtenso2;
	}

	public void setSaltoExtenso2(Integer saltoExtenso2) {
		this.saltoExtenso2 = saltoExtenso2;
	}

	public Integer getAntesExtenso2() {
		return antesExtenso2;
	}

	public void setAntesExtenso2(Integer antesExtenso2) {
		this.antesExtenso2 = antesExtenso2;
	}

	public Integer getSaltoFavorecido() {
		return saltoFavorecido;
	}

	public void setSaltoFavorecido(Integer saltoFavorecido) {
		this.saltoFavorecido = saltoFavorecido;
	}

	public Integer getAntesFavorecido() {
		return antesFavorecido;
	}

	public void setAntesFavorecido(Integer antesFavorecido) {
		this.antesFavorecido = antesFavorecido;
	}

	public Integer getSaltoData() {
		return saltoData;
	}

	public void setSaltoData(Integer saltoData) {
		this.saltoData = saltoData;
	}

	public Integer getAntesData() {
		return antesData;
	}

	public void setAntesData(Integer antesData) {
		this.antesData = antesData;
	}

	public Integer getSaltoEndosso() {
		return saltoEndosso;
	}

	public void setSaltoEndosso(Integer saltoEndosso) {
		this.saltoEndosso = saltoEndosso;
	}

	public Integer getAntesEndosso() {
		return antesEndosso;
	}

	public void setAntesEndosso(Integer antesEndosso) {
		this.antesEndosso = antesEndosso;
	}

	public Integer getAntesAssinatura() {
		return antesAssinatura;
	}

	public void setAntesAssinatura(Integer antesAssinatura) {
		this.antesAssinatura = antesAssinatura;
	}

	public Integer getIdentificadorCliente() {
		return identificadorCliente;
	}

	public void setIdentificadorCliente(Integer identificadorCliente) {
		this.identificadorCliente = identificadorCliente;
	}

	public Integer getIdentificadorFise() {
		return identificadorFise;
	}

	public void setIdentificadorFise(Integer identificadorFise) {
		this.identificadorFise = identificadorFise;
	}

	public Integer getPrazoProcessamentoIb() {
		return prazoProcessamentoIb;
	}

	public void setPrazoProcessamentoIb(Integer prazoProcessamentoIb) {
		this.prazoProcessamentoIb = prazoProcessamentoIb;
	}

	public Boolean getBoletoFaturamento() {
		return boletoFaturamento;
	}

	public void setBoletoFaturamento(Boolean boletoFaturamento) {
		this.boletoFaturamento = boletoFaturamento;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public boolean isConciliacao() {
		return conciliacao;
	}

	public void setConciliacao(boolean conciliacao) {
		this.conciliacao = conciliacao;
	}

	public Long getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Long chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public List<FaixaChequeTO> getListaFaixaDeCheques() {
		return listaFaixaDeCheques;
	}

	public void setListaFaixaDeCheques(List<FaixaChequeTO> listaFaixaDeCheques) {
		this.listaFaixaDeCheques = listaFaixaDeCheques;
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

	public String getNomeInativador() {
		return nomeInativador;
	}

	public void setNomeInativador(String nomeInativador) {
		this.nomeInativador = nomeInativador;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContaCorrenteTO)) return false;
		ContaCorrenteTO that = (ContaCorrenteTO) o;
		return isConciliacao() == that.isConciliacao() &&
			Objects.equals(getTipoContaCorrente(), that.getTipoContaCorrente()) &&
			Objects.equals(getInternetBanking(), that.getInternetBanking()) &&
			Objects.equals(getChequeCarbono(), that.getChequeCarbono()) &&
			Objects.equals(getChequeAnterior(), that.getChequeAnterior()) &&
			Objects.equals(getAntesValor(), that.getAntesValor()) &&
			Objects.equals(getSaltoExtenso(), that.getSaltoExtenso()) &&
			Objects.equals(getAntesExtenso(), that.getAntesExtenso()) &&
			Objects.equals(getSaltoExtenso2(), that.getSaltoExtenso2()) &&
			Objects.equals(getAntesExtenso2(), that.getAntesExtenso2()) &&
			Objects.equals(getSaltoFavorecido(), that.getSaltoFavorecido()) &&
			Objects.equals(getAntesFavorecido(), that.getAntesFavorecido()) &&
			Objects.equals(getSaltoData(), that.getSaltoData()) &&
			Objects.equals(getAntesData(), that.getAntesData()) &&
			Objects.equals(getSaltoEndosso(), that.getSaltoEndosso()) &&
			Objects.equals(getAntesEndosso(), that.getAntesEndosso()) &&
			Objects.equals(getAntesAssinatura(), that.getAntesAssinatura()) &&
			Objects.equals(getIdentificadorCliente(), that.getIdentificadorCliente()) &&
			Objects.equals(getIdentificadorFise(), that.getIdentificadorFise()) &&
			Objects.equals(getPrazoProcessamentoIb(), that.getPrazoProcessamentoIb()) &&
			Objects.equals(getBoletoFaturamento(), that.getBoletoFaturamento()) &&
			Objects.equals(getContaContabil(), that.getContaContabil()) &&
			Objects.equals(getChequeInicial(), that.getChequeInicial()) &&
			Objects.equals(getListaFaixaDeCheques(), that.getListaFaixaDeCheques()) &&
			Objects.equals(getCriador(), that.getCriador()) &&
			Objects.equals(getDataCriacao(), that.getDataCriacao()) &&
			Objects.equals(getAtualizador(), that.getAtualizador()) &&
			Objects.equals(getDataAtualizacao(), that.getDataAtualizacao()) &&
			Objects.equals(getNomeCriador(), that.getNomeCriador()) &&
			Objects.equals(getNomeAtualizador(), that.getNomeAtualizador()) &&
			Objects.equals(getNomeInativador(), that.getNomeInativador()) &&
			Objects.equals(getUnidade(), that.getUnidade());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getTipoContaCorrente(), getInternetBanking(), getChequeCarbono(), getChequeAnterior(), getAntesValor(), getSaltoExtenso(), getAntesExtenso(), getSaltoExtenso2(), getAntesExtenso2(), getSaltoFavorecido(), getAntesFavorecido(), getSaltoData(), getAntesData(), getSaltoEndosso(), getAntesEndosso(), getAntesAssinatura(), getIdentificadorCliente(), getIdentificadorFise(), getPrazoProcessamentoIb(), getBoletoFaturamento(), getContaContabil(), isConciliacao(), getChequeInicial(), getListaFaixaDeCheques(), getCriador(), getDataCriacao(), getAtualizador(), getDataAtualizacao(), getNomeCriador(), getNomeAtualizador(), getNomeInativador(), getUnidade());
	}

	@Override
	public String toString() {
		return "ContaCorrenteTO{" +
			"tipoContaCorrente='" + tipoContaCorrente + '\'' +
			", internetBanking=" + internetBanking +
			", chequeCarbono=" + chequeCarbono +
			", chequeAnterior=" + chequeAnterior +
			", antesValor=" + antesValor +
			", saltoExtenso=" + saltoExtenso +
			", antesExtenso=" + antesExtenso +
			", saltoExtenso2=" + saltoExtenso2 +
			", antesExtenso2=" + antesExtenso2 +
			", saltoFavorecido=" + saltoFavorecido +
			", antesFavorecido=" + antesFavorecido +
			", saltoData=" + saltoData +
			", antesData=" + antesData +
			", saltoEndosso=" + saltoEndosso +
			", antesEndosso=" + antesEndosso +
			", antesAssinatura=" + antesAssinatura +
			", identificadorCliente=" + identificadorCliente +
			", identificadorFise=" + identificadorFise +
			", prazoProcessamentoIb=" + prazoProcessamentoIb +
			", boletoFaturamento=" + boletoFaturamento +
			", contaContabil=" + contaContabil +
			", conciliacao=" + conciliacao +
			", chequeInicial=" + chequeInicial +
			", listaFaixaDeCheques=" + listaFaixaDeCheques +
			", criador='" + criador + '\'' +
			", dataCriacao=" + dataCriacao +
			", atualizador='" + atualizador + '\'' +
			", dataAtualizacao=" + dataAtualizacao +
			", nomeCriador='" + nomeCriador + '\'' +
			", nomeAtualizador='" + nomeAtualizador + '\'' +
			", nomeInativador='" + nomeInativador + '\'' +
			", unidade=" + unidade +
			'}';
	}
}
