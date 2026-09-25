package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface HistoricoConciliacaoContaContabilService extends CrudService<HistoricoConciliacaoContaContabilTO> {
	void remover(Long id);
}
