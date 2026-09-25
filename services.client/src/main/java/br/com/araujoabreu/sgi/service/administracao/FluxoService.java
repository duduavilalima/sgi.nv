package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface FluxoService extends CrudService<FluxoTO> {
	List<FluxoTO> findFluxoByNome(String nome);
}
