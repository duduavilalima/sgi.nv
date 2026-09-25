package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

public class CentroFinanceiroFechoTO extends BaseTO {

	private CentroFinanceiroTO centroFinanceiro;
	private FechoTO fecho;
	private List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquota;

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public FechoTO getFecho() {
		return fecho;
	}

	public void setFecho(FechoTO fecho) {
		this.fecho = fecho;
	}

	public List<CentroFinanceiroAliquotaTO> getListaCentroFinanceiroAliquota() {
		return listaCentroFinanceiroAliquota;
	}

	public void setListaCentroFinanceiroAliquota(List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquota) {
		this.listaCentroFinanceiroAliquota = listaCentroFinanceiroAliquota;
	}
}
