package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ColaboradorService extends CrudService<ColaboradorTO> {
	List<ColaboradorTO> buscarColaboradores(ColaboradorTO colaboradorTO);
}
