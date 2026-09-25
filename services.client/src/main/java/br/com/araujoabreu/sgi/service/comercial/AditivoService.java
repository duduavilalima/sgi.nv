package br.com.araujoabreu.sgi.service.comercial;

import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface AditivoService extends CrudService<AditivoTO> {
	void remover(Long id);

}
