package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoDocumento;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotivoDocumentoServiceImpl extends BaseSearchService<MotivoDocumento, MotivoDocumentoTO, MotivoDocumentoPesquisaTO, MotivoDocumentoRepository> implements MotivoDocumentoService {

	private static final QMotivoDocumento qMotivoDocumento = QMotivoDocumento.motivoDocumento;

	@Autowired
	public MotivoDocumentoServiceImpl(MotivoDocumentoRepository repository) {
		super(repository);
	}
}
