package br.com.araujoabreu.sgi.service.contato;

import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface RelacionamentoContatoService extends CrudService<RelacionamentoContatoTO> {

	List<RelacionamentoContatoTO> buscarTodos();

}
