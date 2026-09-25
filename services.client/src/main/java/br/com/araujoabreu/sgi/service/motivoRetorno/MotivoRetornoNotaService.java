package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;

public interface MotivoRetornoNotaService extends CrudService<MotivoRetornoNotaTO> {
	MotivoRetornoNotaTO saveNotaFiscal(MotivoRetornoNotaTO transferObj) throws ValidationException;
}
