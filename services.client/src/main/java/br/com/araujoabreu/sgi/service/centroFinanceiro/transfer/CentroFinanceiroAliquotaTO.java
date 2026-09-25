package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class CentroFinanceiroAliquotaTO extends BaseTO {

	private AliquotaTO aliquota;

	public AliquotaTO getAliquota() {
		return aliquota;
	}

	public void setAliquota(AliquotaTO aliquota) {
		this.aliquota = aliquota;
	}
}
