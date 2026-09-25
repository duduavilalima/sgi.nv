package br.com.araujoabreu.sgi.service.security;

import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface PerfilService extends CrudService<PerfilTO> {

	Iterable<PerfilTO> listarPerfisAtivos();
}
