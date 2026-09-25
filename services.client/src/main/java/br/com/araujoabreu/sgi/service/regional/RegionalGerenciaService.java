package br.com.araujoabreu.sgi.service.regional;

import br.com.araujoabreu.sgi.service.regional.transfer.RegionalGerenciaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface RegionalGerenciaService extends CrudService<RegionalGerenciaTO> {

	List<Long> listaCodGerenciaPorCodRegional(Long codigoRegional);

}
