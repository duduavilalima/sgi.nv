package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface MovimentoAutorizadoService extends CrudService<MovimentoAutorizadoTO> {
	void cancelar(Long codigo);

	PagamentoTO pagamentoAtualizarMovimento(PagamentoTO pagamentoTO);
}
