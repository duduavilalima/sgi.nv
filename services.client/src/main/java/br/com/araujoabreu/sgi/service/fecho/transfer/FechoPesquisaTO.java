package br.com.araujoabreu.sgi.service.fecho.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class FechoPesquisaTO extends BaseSearchTO {

	private CentroFinanceiroSimpleTO centroFinanceiro;
	private Integer numero;
	private Date dataReajuste;
	private ColaboradorTO responsavelReajuste;
	private BigDecimal percentualReajusteMaoDeObra;
	private BigDecimal percentualReajusteGastosGerais;
	private BigDecimal percentualReajusteMateriais;
	private BigDecimal percentualReajusteServicos;
	private String indiceRajuste;

	public CentroFinanceiroSimpleTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroSimpleTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataReajuste() {
		return dataReajuste;
	}

	public void setDataReajuste(Date dataReajuste) {
		this.dataReajuste = dataReajuste;
	}

	public ColaboradorTO getResponsavelReajuste() {
		return responsavelReajuste;
	}

	public void setResponsavelReajuste(ColaboradorTO responsavelReajuste) {
		this.responsavelReajuste = responsavelReajuste;
	}

	public BigDecimal getPercentualReajusteMaoDeObra() {
		return percentualReajusteMaoDeObra;
	}

	public void setPercentualReajusteMaoDeObra(BigDecimal percentualReajusteMaoDeObra) {
		this.percentualReajusteMaoDeObra = percentualReajusteMaoDeObra;
	}

	public BigDecimal getPercentualReajusteGastosGerais() {
		return percentualReajusteGastosGerais;
	}

	public void setPercentualReajusteGastosGerais(BigDecimal percentualReajusteGastosGerais) {
		this.percentualReajusteGastosGerais = percentualReajusteGastosGerais;
	}

	public BigDecimal getPercentualReajusteMateriais() {
		return percentualReajusteMateriais;
	}

	public void setPercentualReajusteMateriais(BigDecimal percentualReajusteMateriais) {
		this.percentualReajusteMateriais = percentualReajusteMateriais;
	}

	public BigDecimal getPercentualReajusteServicos() {
		return percentualReajusteServicos;
	}

	public void setPercentualReajusteServicos(BigDecimal percentualReajusteServicos) {
		this.percentualReajusteServicos = percentualReajusteServicos;
	}

	public String getIndiceRajuste() {
		return indiceRajuste;
	}

	public void setIndiceRajuste(String indiceRajuste) {
		this.indiceRajuste = indiceRajuste;
	}
}
