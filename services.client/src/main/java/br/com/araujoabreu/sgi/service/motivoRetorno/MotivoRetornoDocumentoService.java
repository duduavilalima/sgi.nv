package br.com.araujoabreu.sgi.service.motivoRetorno;

import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

public interface MotivoRetornoDocumentoService extends CrudService<MotivoRetornoDocumentoTO> {
	MotivoRetornoDocumentoTO findByCodigo(Long codigo);

	MotivoRetornoDocumentoTO buscarMotivoRetornoDocumentoByCod(Long codigoMotivoRetornoDocumento);

	JPAQuery getJpaQuery();
}
