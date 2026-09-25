package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaSaidaParcelaTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface MotivoRetornoDocumentoNotaSaidaParcelaService extends CrudService<MotivoRetornoDocumentoNotaSaidaParcelaTO> {

	void salvarMotivoRetornoParcelaRecebimento(MotivoRetornoDocumentoNotaSaidaParcelaTO to);

	void deletarPorParcela(Long codigoNotaSaidaParcela);
}
