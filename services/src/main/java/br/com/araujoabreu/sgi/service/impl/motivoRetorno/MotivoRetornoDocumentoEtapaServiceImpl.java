package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoEtapa;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoEtapaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoEtapaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoEtapaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoDocumentoEtapaServiceImpl extends BaseSearchService<MotivoRetornoDocumentoEtapa, MotivoRetornoDocumentoEtapaTO, MotivoRetornoDocumentoEtapaPesquisaTO, MotivoRetornoDocumentoEtapaRepository> implements MotivoRetornoDocumentoEtapaService {
	private static final QMotivoRetornoDocumentoEtapa qMotivoRetornoDocumentoEtapa = QMotivoRetornoDocumentoEtapa.motivoRetornoDocumentoEtapa;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private MotivoRetornoDocumentoService motivoRetornoDocumentoService;

	@Autowired
	public MotivoRetornoDocumentoEtapaServiceImpl(MotivoRetornoDocumentoEtapaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoEtapaPesquisaTO exampleObj) {
		if (exampleObj.getFaturamentoEtapa() != null && exampleObj.getFaturamentoEtapa().getCodigo() != null) {
			builder.and(qMotivoRetornoDocumentoEtapa.faturamentoEtapa.codigo.eq(exampleObj.getFaturamentoEtapa().getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<MotivoRetornoDocumentoEtapa> entities, List<MotivoRetornoDocumentoEtapaPesquisaTO> result) throws ServiceException {
		for (MotivoRetornoDocumentoEtapaPesquisaTO to : result) {
			MotivoRetornoDocumentoTO motivoRetornoDocumento = motivoRetornoDocumentoService.findByCodigo(to.getMotivoRetornoDocumento().getCodigo());
			to.setMotivoRetornoDocumento(motivoRetornoDocumento);

			List<MotivoDocumento> list = motivoDocumentoRepository.findByMotivoRetornoDocumentoCodigo(motivoRetornoDocumento.getCodigo());
			to.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

			for (MotivoDocumento motivoDocumento : list) {
				MotivoDocumentoTO auxTO = new MotivoDocumentoTO();

				MotivoRetornoTO mrTO = new MotivoRetornoTO();
				MotivoRetorno mr = motivoRetornoRepository.findById(motivoDocumento.getMotivoRetorno().getCodigo()).orElse(null);
				mrTO.setCodigo(mr.getCodigo());
				mrTO.setMotivo(mr.getMotivo());
				mrTO.setSolucao(mr.getSolucao());
				mrTO.setCriador(mr.getCriador());
				mrTO.setDataCriacao(mr.getDataCriacao());
				mrTO.setAtualizador(mr.getAtualizador());
				mrTO.setDataAtualizacao(mr.getDataAtualizacao());

				auxTO.setCodigo(motivoDocumento.getCodigo());
				auxTO.setMotivoRetorno(mrTO);

				to.getMotivoRetornoDocumento().getListaMotivoRetorno().add(auxTO);
			}
		}
	}

	@Override
	protected void doBeforeUpdate(MotivoRetornoDocumentoEtapa entity, MotivoRetornoDocumentoEtapaTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoEtapa entity, MotivoRetornoDocumentoEtapaTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	public void prepareSave(MotivoRetornoDocumentoEtapa entity, MotivoRetornoDocumentoEtapaTO to) {
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findById(to.getFaturamentoEtapa().getCodigo()).orElse(null);
		faturamentoEtapa.setSituacao(FaturamentoEtapa.Status.CANCELADA);
		faturamentoEtapa.setCancelador(securityService.getUser().getPessoaFisica());
		faturamentoEtapa.setDataCancelamento(new Timestamp(new Date().getTime()));
		faturamentoEtapa.setDataAtualizacao(new Date());
		faturamentoEtapa.setAtualizador(securityService.getUser().getLogin());
		entity.setFaturamentoEtapa(faturamentoEtapa);

		entity.setFaturamentoEtapa(faturamentoEtapaRepository.save(entity.getFaturamentoEtapa()));

		entity.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(to.getMotivoRetornoDocumento().getAplicacao().getChave()));

		entity.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		entity.getMotivoRetornoDocumento().setCriador(securityService.getUser().getLogin());
		entity.getMotivoRetornoDocumento().setDataCriacao(new Date());

		entity.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

		entity.getMotivoRetornoDocumento().setObservacao(to.getMotivoRetornoDocumento().getObservacao());

		if (to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivoDocumentoTO : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				entity.getMotivoRetornoDocumento().getListaMotivoRetorno().add(new MotivoDocumento(entity.getMotivoRetornoDocumento(), motivoRetornoRepository.findById(motivoDocumentoTO.getMotivoRetorno().getCodigo()).orElse(null)));
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(entity.getMotivoRetornoDocumento());

		entity.setMotivoRetornoDocumento(motivoRetornoDocumento);
	}

	public void prepareValidate(MotivoRetornoDocumentoEtapaTO to) throws ValidationException {
		if (to.getFaturamentoEtapa() == null || to.getFaturamentoEtapa().getCodigo() == null) {
			throw new ValidationException("ValidationException","Etapa não informada.");
		}

		if (to.getMotivoRetornoDocumento() == null) {
			throw new ValidationException("ValidationException","Motivo de retorno documento não informado.");
		}

		if (to.getMotivoRetornoDocumento().getAplicacao() == null) {
			throw new ValidationException("ValidationException","Motivo aplicação não informado.");
		} else if (to.getMotivoRetornoDocumento().getAplicacao().getChave() == null || to.getMotivoRetornoDocumento().getAplicacao().getChave().isEmpty()) {
			throw new ValidationException("ValidationException","Chave do motivo aplicação não informada.");
		}

		if ((to.getMotivoRetornoDocumento().getListaMotivoRetorno() == null || to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty())
			&& (to.getMotivoRetornoDocumento().getObservacao() == null || to.getMotivoRetornoDocumento().getObservacao().isEmpty())) {
			throw new ValidationException("ValidationException","Lista de motivos de retorno e observação nulos, pelo menos um deve ser informado.");
		}

		if (to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivo : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				if (motivo.getMotivoRetorno() == null) {
					throw new ValidationException("ValidationException","Motivo de retorno não informado");
				} else {
					if (motivo.getMotivoRetorno().getCodigo() == null) {
						throw new ValidationException("ValidationException",String.format("Código do motivo de retorno [%s], não informado.", motivo.getMotivoRetorno().getMotivo()));
					}
				}
			}
		}
	}
}
