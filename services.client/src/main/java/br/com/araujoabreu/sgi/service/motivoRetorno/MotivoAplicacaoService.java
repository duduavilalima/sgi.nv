package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface MotivoAplicacaoService extends CrudService<MotivoAplicacaoTO> {
	MotivoAplicacaoTO buscarMotivoAplicacaoByChave(String chave) throws Exception;

	MotivoAplicacaoTO findMotivoAplicacaoByChave(String chave);

	MotivoAplicacaoTO findByCodMotivoRetornoDoc(Long codigo);

}
