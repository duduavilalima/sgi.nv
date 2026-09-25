package br.com.araujoabreu.sgi.service.regional;

import br.com.araujoabreu.sgi.service.regional.transfer.RegionalTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface RegionalService extends CrudService<RegionalTO> {
	List<RegionalTO> buscarPorCodigoUnidade(String idEmpresa);

	List<RegionalTO> buscarAtivas();
}
