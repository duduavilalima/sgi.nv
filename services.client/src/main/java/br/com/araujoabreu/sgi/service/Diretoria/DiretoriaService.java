package br.com.araujoabreu.sgi.service.Diretoria;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface DiretoriaService extends CrudService<DiretoriaSimpleTO> {
	List<DiretoriaSimpleTO> buscarDiretoriasDeContratos();

	List<DiretoriaSimpleTO> buscarDiretoriaDeContratosByColaborador();

	List<DiretoriaSimpleTO> buscarDiretoriaPorEmpresa(String idEmpresa) throws Exception;
}
