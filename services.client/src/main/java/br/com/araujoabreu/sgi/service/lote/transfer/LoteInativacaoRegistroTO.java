package br.com.araujoabreu.sgi.service.lote.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;

import java.math.BigDecimal;

public class LoteInativacaoRegistroTO {

	private LoteInativacaoTO loteInativacao;
	private ColaboradorTO inativado;
	private CentroFinanceiroTO centroFinanceiro;
	private String situacao;
	private String cpfArquivo;
	private String nomeArquivo;
	private String tipoPrestacaoContas;
	private BigDecimal valorPrestacaoContas;

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public LoteInativacaoTO getLoteInativacao() {
		return loteInativacao;
	}

	public void setLoteInativacao(LoteInativacaoTO loteInativacao) {
		this.loteInativacao = loteInativacao;
	}

	public ColaboradorTO getInativado() {
		return inativado;
	}

	public void setInativado(ColaboradorTO inativado) {
		this.inativado = inativado;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCpfArquivo() {
		return cpfArquivo;
	}

	public void setCpfArquivo(String cpfArquivo) {
		this.cpfArquivo = cpfArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getTipoPrestacaoContas() {
		return tipoPrestacaoContas;
	}

	public void setTipoPrestacaoContas(String tipoPrestacaoContas) {
		this.tipoPrestacaoContas = tipoPrestacaoContas;
	}

	public BigDecimal getValorPrestacaoContas() {
		return valorPrestacaoContas;
	}

	public void setValorPrestacaoContas(BigDecimal valorPrestacaoContas) {
		this.valorPrestacaoContas = valorPrestacaoContas;
	}

}
