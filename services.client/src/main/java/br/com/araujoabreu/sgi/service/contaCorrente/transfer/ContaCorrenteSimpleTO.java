package br.com.araujoabreu.sgi.service.contaCorrente.transfer;


import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContaCorrenteSimpleTO extends BaseTO {

	private String tipoContaCorrente;
	private Boolean internetBanking;
	private Boolean chequeCarbono;
	private Long chequeAnterior;
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
	//  private Integer identificadorCliente;
//  private Integer identificadorFise;
//  private Integer prazoProcessamentoIb;
//  private Boolean boletoFaturamento;
//	private String criador;
//	private Date dataCriacao;
//	private String atualizador;
//	private Date dataAtualizacao;
	private boolean conciliacao;
	private Long chequeInicial;

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
}
