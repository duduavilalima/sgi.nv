package br.com.araujoabreu.sgi.service.controlefiscal;

import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfTO;
import br.com.araujoabreu.sgi.service.reinf.retorno.Reinf;
import br.com.fasolti.commons.client.service.CrudService;

import java.io.File;
import java.util.List;

public interface ReinfService extends CrudService<ReinfTO> {

	ReinfTO gerarReinf(String referencia, Long codigoEmpresa) throws Exception;

	ReinfTO gerarReinfPorEvento(String registro, String referencia, Long codigoEmpresa) throws Exception;

	void processarRetorno(String referencia, Long codigoEmpresa)  throws Exception;

	void processarTodosRetornos(Integer ano) throws Exception;

	List<ReinfTO> findReinfBySituacao(Integer situacao);

	ReinfTO findReinf(String referencia, Long codigoEmpresa) throws Exception;

	void processaArquivoRetorno(String referencia, Long codigoEmpresa) throws Exception;;
}
