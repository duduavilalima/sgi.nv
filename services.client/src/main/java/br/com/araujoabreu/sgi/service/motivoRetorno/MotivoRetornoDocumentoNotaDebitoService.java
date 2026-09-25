package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaDebitoTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;

public interface MotivoRetornoDocumentoNotaDebitoService extends CrudService<MotivoRetornoDocumentoNotaDebitoTO> {

	MotivoRetornoDocumentoNotaDebitoTO retornarNotaDebito(MotivoRetornoDocumentoNotaDebitoTO transferObj) throws ValidationException;

}
