package br.com.araujoabreu.sgi.service.fecho;

import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface FechoMaoDeObraAdicionalService extends CrudService<FechoMaoDeObraAdicionalTO> {
	List<FechoMaoDeObraAdicionalTO> adicionalByCodMaoDeObra(String id) throws Exception;

	List<FechoMaoDeObraAdicionalTO> buscarListaAdicionalByCodMaoDeObra(Long codigo);

}
