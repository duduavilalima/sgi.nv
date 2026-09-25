package br.com.araujoabreu.sgi.service.unidademedida;

import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface UnidadeMedidaService extends CrudService<UnidadeMedidaTO> {

	List<UnidadeMedidaTO> buscarTodasUnidadesMedida();

}
