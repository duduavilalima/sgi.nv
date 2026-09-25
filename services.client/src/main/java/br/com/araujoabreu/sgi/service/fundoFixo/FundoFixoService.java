package br.com.araujoabreu.sgi.service.fundoFixo;

import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface FundoFixoService extends CrudService<FundoFixoTO> {

	Long buscarQtdFundoFixoPorCfId(Long id);
}
