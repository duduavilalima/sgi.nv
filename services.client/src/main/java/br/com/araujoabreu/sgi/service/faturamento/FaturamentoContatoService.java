package br.com.araujoabreu.sgi.service.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoContatoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface FaturamentoContatoService extends CrudService<FaturamentoContatoTO> {

	List<FaturamentoContatoTO> listaPorCodFaturamento(Long codFaturamento);

	void remover(Long id) throws Exception;

}
