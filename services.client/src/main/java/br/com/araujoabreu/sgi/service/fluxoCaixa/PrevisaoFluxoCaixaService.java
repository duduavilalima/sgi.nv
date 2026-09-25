package br.com.araujoabreu.sgi.service.fluxoCaixa;

import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.PrevisaoFluxoCaixaTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface PrevisaoFluxoCaixaService extends CrudService<PrevisaoFluxoCaixaTO> {
	void remover(Long id);
}
