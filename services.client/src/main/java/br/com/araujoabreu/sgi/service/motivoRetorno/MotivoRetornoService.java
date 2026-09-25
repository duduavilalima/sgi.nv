package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface MotivoRetornoService extends CrudService<MotivoRetornoTO> {
	List<MotivoRetornoTO> findMotivosRetornoByMotivoAplicacao(MotivoAplicacaoTO motivoAplicacaoTO);

	List<MotivoRetornoTO> buscarListaMotivoRetornoByChaveAplicacao(String chave) throws Exception;
}
