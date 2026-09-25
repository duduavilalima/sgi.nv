package br.com.araujoabreu.sgi.service.contato;

import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface ContatoService extends CrudService<ContatoTO> {
	void remover(Long id);
}
