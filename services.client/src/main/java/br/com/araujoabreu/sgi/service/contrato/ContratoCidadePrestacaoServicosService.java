package br.com.araujoabreu.sgi.service.contrato;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface ContratoCidadePrestacaoServicosService extends CrudService<ContratoCidadePrestacaoServicosTO> {
	Iterable<ContratoCidadePrestacaoServicosPesquisaTO> buscarTodosPorExemplo(ContratoCidadePrestacaoServicosPesquisaTO parametros);
}
