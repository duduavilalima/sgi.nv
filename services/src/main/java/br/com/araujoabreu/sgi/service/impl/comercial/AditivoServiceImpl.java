package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.comercial.domain.Aditivo;
import br.com.araujoabreu.sgi.comercial.domain.QAditivo;
import br.com.araujoabreu.sgi.service.aditivo.AditivoPesquisaTO;
import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.comercial.AditivoService;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.comercial.AditivoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoAnexoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AditivoServiceImpl extends BaseSearchService<Aditivo, AditivoTO, AditivoPesquisaTO, AditivoRepository> implements AditivoService {

	private static final QAditivo qAditivo = QAditivo.aditivo;

	@Autowired
	ContratoRepository contratoRepository;

	@Autowired
	ContratoAnexoRepository contratoAnexoRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public AditivoServiceImpl(AditivoRepository repository) {
		super(repository);
	}

	@Transactional
	@Override
	public void remover(Long id) {
		try {
			Long qtdContratoAnexo = contratoAnexoRepository.findQtdByAditivo(id);

			if (qtdContratoAnexo != null && qtdContratoAnexo > 0) {
				throw new ValidationException("Aditivo", "Não é possível excluir este aditivo, pois há um anexo associado.");
			} else {
				Aditivo aditivo = getRepository().findById(id).orElse(null);
				Contrato contrato = aditivo.getContrato();

				getRepository().delete(aditivo);

				List<Aditivo> listAditivo = getRepository().findByContratoCodigo(contrato.getCodigo());

				if (listAditivo != null && !listAditivo.isEmpty()) {
					Aditivo ultimoAditivo = listAditivo.stream().sorted(Comparator.comparingLong(Aditivo::getCodigo)).collect(Collectors.toList()).get(listAditivo.size() - 1);
					atualizarContrato(ultimoAditivo);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doAfterSave(Aditivo entity, AditivoTO object) throws ServiceException {
		atualizarContrato(entity);
	}

	@Override
	protected void doAfterUpdate(Aditivo entity, AditivoTO object) throws ServiceException {
		atualizarContrato(entity);
	}

	private void atualizarContrato(Aditivo aditivo) {
		Contrato contrato = aditivo.getContrato();

		contratoRepository.updateDataAssinatura(aditivo.getDataAssinatura(), contrato.getCodigo());
		contratoRepository.updateDataInicioVigencia(aditivo.getDataInicioVigencia(), contrato.getCodigo());
		contratoRepository.updateDataFimVigencia(aditivo.getDataFimVigencia(), contrato.getCodigo());
		contratoRepository.updateIndiceReajuste(aditivo.getIndiceReajuste(), contrato.getCodigo());
		contratoRepository.updateResumo(aditivo.getResumo(), contrato.getCodigo());
		contratoRepository.updatePeriodoReajuste(aditivo.getPeriodoReajuste(), contrato.getCodigo());
		contratoRepository.updatePrazoAtual(aditivo.getPrazoAtual(), contrato.getCodigo());
		contratoRepository.updateDataAtualizacao(new Date(), contrato.getCodigo());
		contratoRepository.updateAtualizador(securityService.getUser().getLogin(), contrato.getCodigo());
	}
}
