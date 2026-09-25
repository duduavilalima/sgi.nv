package br.com.araujoabreu.sgi.service.fundoFixo.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class FundoFixoTO extends BaseTO {
	private ColaboradorTO colaborador;
	private CentroFinanceiroTO centroFinanceiro;
	private BigDecimal limite;
	private BigDecimal saldo;
	private String status;
	private String descricao;
	private BigDecimal saldoPendente;
	private ColaboradorTO alteradorLimite;
	private Date dataAlteracaoLimite;
	private BigDecimal limiteAnterior;
	private ColaboradorTO inativador;
	private Date dataInativacao;
	private String tipo;

	public FundoFixoTO() {
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getSaldoPendente() {
		return saldoPendente;
	}

	public void setSaldoPendente(BigDecimal saldoPendente) {
		this.saldoPendente = saldoPendente;
	}

	public ColaboradorTO getAlteradorLimite() {
		return alteradorLimite;
	}

	public void setAlteradorLimite(ColaboradorTO alteradorLimite) {
		this.alteradorLimite = alteradorLimite;
	}

	public Date getDataAlteracaoLimite() {
		return dataAlteracaoLimite;
	}

	public void setDataAlteracaoLimite(Date dataAlteracaoLimite) {
		this.dataAlteracaoLimite = dataAlteracaoLimite;
	}

	public BigDecimal getLimiteAnterior() {
		return limiteAnterior;
	}

	public void setLimiteAnterior(BigDecimal limiteAnterior) {
		this.limiteAnterior = limiteAnterior;
	}

	public ColaboradorTO getInativador() {
		return inativador;
	}

	public void setInativador(ColaboradorTO inativador) {
		this.inativador = inativador;
	}

	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
