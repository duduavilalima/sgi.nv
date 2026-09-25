package br.com.araujoabreu.sgi.service.telefone;

import br.com.araujoabreu.sgi.service.telefone.transfer.OperadoraTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface TelefoneService extends CrudService<TelefoneTO> {
	List<OperadoraTO> buscarOperadora();

	List<TelefoneTO> buscarTelefones(TelefoneTO telefoneTO) throws Exception;
}
