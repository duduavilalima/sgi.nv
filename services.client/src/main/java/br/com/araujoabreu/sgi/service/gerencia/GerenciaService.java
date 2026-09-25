package br.com.araujoabreu.sgi.service.gerencia;

import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface GerenciaService extends CrudService<GerenciaTO> {

	List<GerenciaTO> getCFsByUnidade(UnidadeTO unidade) throws Exception;

	List<GerenciaTO> buscarTodasGerenciasTelaRelatorio();

	List<GerenciaTO> buscarGerenciasPorUnidadeTelaRelatorio(Long codigoUnidade);

	List<GerenciaTO> buscarTodasGerenciasHabilitadasFaturamento();

	GerenciaTO obterGerencia(Long codigoGerencia);

	List<GerenciaTO> getGerenciaSimplesAtivas();

	List<GerenciaTO> findByCodigoDiretoria(long codigo);

	List<GerenciaTO> getGerenciaSimplesAtivasUsuarioLogado(long id);
}
