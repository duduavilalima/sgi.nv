package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ConciliacaoLancamentoService extends CrudService<ConciliacaoLancamentoTO> {
	List<LancamentoTO> getLancamentosAssociados() throws Exception;

	ChequeContaCorrenteTO getChequeAssociado(String numeroCheque) throws Exception;
}
