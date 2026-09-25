package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface LancamentoService extends CrudService<LancamentoTO> {
	void cancelar(Long codigo);
}
