package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoCentroFinanceiro;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoCentroFinanceiroService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoCentroFinanceiroPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoCentroFinanceiroTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoCentroFinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotivoRetornoCentroFinanceiroServiceImpl extends BaseSearchService<MotivoRetornoCentroFinanceiro, MotivoRetornoCentroFinanceiroTO, MotivoRetornoCentroFinanceiroPesquisaTO, MotivoRetornoCentroFinanceiroRepository> implements MotivoRetornoCentroFinanceiroService {

	private static final QMotivoRetornoCentroFinanceiro qMotivoRetornoCentroFinanceiro = QMotivoRetornoCentroFinanceiro.motivoRetornoCentroFinanceiro;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	public MotivoRetornoCentroFinanceiroServiceImpl(MotivoRetornoCentroFinanceiroRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(MotivoRetornoCentroFinanceiro entity, MotivoRetornoCentroFinanceiroTO object) {
		prepare(entity, object);
	}

	private void prepare(MotivoRetornoCentroFinanceiro entity, MotivoRetornoCentroFinanceiroTO object) {

		if (entity != null && entity.getMotivoRetornoDocumento() != null && entity.getMotivoRetornoDocumento().getCodigo() != null && entity.getMotivoRetornoDocumento().getListaMotivoRetorno() != null
			&& entity.getMotivoRetornoDocumento().getListaMotivoRetorno().size() > 0) {

			for (MotivoDocumento motivoDocumento : entity.getMotivoRetornoDocumento().getListaMotivoRetorno()) {

				if (motivoDocumento.getMotivoRetorno().getCodigo() != null) {
					MotivoDocumento novoMotivoDocumento = new MotivoDocumento();
					novoMotivoDocumento.setMotivoRetornoDocumento(entity.getMotivoRetornoDocumento());
					novoMotivoDocumento.setMotivoRetorno(motivoDocumento.getMotivoRetorno());
					motivoDocumentoRepository.save(novoMotivoDocumento);
				}
			}
		}
	}


}
