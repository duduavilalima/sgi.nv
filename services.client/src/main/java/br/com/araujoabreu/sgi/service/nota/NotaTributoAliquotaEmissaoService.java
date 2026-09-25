package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaEmissaoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface NotaTributoAliquotaEmissaoService extends CrudService<NotaTributoAliquotaEmissaoTO> {

	List<NotaTributoAliquotaEmissaoTO> listarPorCodigoNota(Long codigoNota);

}
