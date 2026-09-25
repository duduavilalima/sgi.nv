package br.com.araujoabreu.sgi.service.contrato;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface ContratoEnderecoService extends CrudService<ContratoEnderecoTO> {
	void remover(Long id) throws Exception;
}
