package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class CentroFinanceiroFluxoEtapaPesquisaTO extends BaseSearchTO {

	private CentroFinanceiroSimpleTO centroFinanceiro;
	private EtapaFluxoTO etapaFluxo;
	private ColaboradorTO colaborador;
	private Date dataRealizacao;

	public CentroFinanceiroSimpleTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroSimpleTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public EtapaFluxoTO getEtapaFluxo() {
		return etapaFluxo;
	}

	public void setEtapaFluxo(EtapaFluxoTO etapaFluxo) {
		this.etapaFluxo = etapaFluxo;
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public Date getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Date dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}
}
