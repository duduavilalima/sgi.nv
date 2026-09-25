package br.com.araujoabreu.sgi.service.contrato;

import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ContratoContatoService extends CrudService<ContratoContatoTO> {
	List<RelacionamentoContatoTO> buscarRelacionamento();

	void remover(Long id) throws Exception;

	List<ContratoContatoTO> listaContratoContatoByCodContrato(String id) throws Exception;
}
