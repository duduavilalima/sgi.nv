package br.com.araujoabreu.sgi.service.custos;

import br.com.araujoabreu.sgi.service.custos.transfer.DespesaCustosTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface DespesaCustosService extends CrudService<DespesaCustosTO> {
	long temDespesaCustoComMesmaDescricao(Long codigo, String descricao);
	List<DespesaCustosTO> obterListaDespesasSimplesAtivas();
}
