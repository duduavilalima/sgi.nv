package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface HistoricoConciliacaoService extends CrudService<HistoricoConciliacaoTO> {
	List<HistoricoConciliacaoContaContabilTO> buscarHistoricoCCC(String codigo);

	void remover(Long id);

	List<ContaContabilTO> buscarContaContabil(Long codigoHistoricoConciliacao) throws Exception;
}
