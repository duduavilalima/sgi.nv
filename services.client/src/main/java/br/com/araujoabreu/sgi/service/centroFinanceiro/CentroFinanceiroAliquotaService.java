package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CentroFinanceiroAliquotaService extends CrudService<CentroFinanceiroAliquotaTO> {
	List<CentroFinanceiroAliquotaTO> getListByCodCentroFinanceiro(String id) throws Exception;
}
