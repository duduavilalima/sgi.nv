package br.com.araujoabreu.sgi.service.fluxoCaixa;

import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.DespesaFluxoCaixaTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface DespesaFluxoCaixaService extends CrudService<DespesaFluxoCaixaTO> {
	void remover(Long id);

	List<DespesaFluxoCaixaTO> buscarDespesaFluxoCaixaPorDescricao(String descricao);

	List<DespesaFluxoCaixaTO> getDespesaFluxoCaixa(AutocompleteTO autocompleteTO) throws Exception;
}
