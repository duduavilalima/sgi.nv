package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.ReinfServicoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ServicoTipoService extends CrudService<ServicoTipoTO> {

	ServicoTipoTO obterServicoTipoPelaSiglaTO(String sigla);

	ServicoTipoTO findByCodigo(Long codigo);

	ReinfServicoTO buscarReinfServicoByCodServicoTipo(String codigoServicoTipo);

	List<ServicoTipoTO> findByCodServicoTipoPai(Long id);

	List<ServicoTipoTO> findBySigla(String sigla);

	List<ServicoTipoTO> findByNomeAndSituacaoAndTipoFaturamento(String nome, String nomeSituacao, String nomeTipoFaturamento);
}
