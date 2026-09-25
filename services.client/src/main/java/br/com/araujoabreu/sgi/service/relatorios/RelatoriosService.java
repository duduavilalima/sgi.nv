package br.com.araujoabreu.sgi.service.relatorios;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;

import java.util.List;

public interface RelatoriosService {
	List<CentroFinanceiroTO> buscaPorUnidade(UnidadeTO exampleObj);

	List<CentroFinanceiroTO> buscaCcClPorGerencia(CentroFinanceiroTO exampleObj);

	List<CentroFinanceiroTO> buscarTodasGerencias();
}
