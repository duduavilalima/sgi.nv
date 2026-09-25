package br.com.araujoabreu.sgi.service.tributo;

import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface TributoService extends CrudService<TributoTO> {

	List<TributoTO> buscarTributoByExibeNota();

}
