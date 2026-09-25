package br.com.araujoabreu.sgi.service.comercial;

import br.com.araujoabreu.sgi.service.comercial.transfer.ObraTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.TipoObraTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ObraService extends CrudService<ObraTO> {
	ObraTO findByNome(String nome);

	List<TipoObraTO> buscarTipoObra();
}
