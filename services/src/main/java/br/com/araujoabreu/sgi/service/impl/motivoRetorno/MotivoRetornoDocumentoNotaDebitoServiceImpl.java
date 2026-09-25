package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.api.EmailAPIJavamail;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoNotaDebitoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.*;
import br.com.araujoabreu.sgi.service.nota.NotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.compras.NotaDebitoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoNotaDebitoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import br.com.fasolti.sgi.repository.nota.NotaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoDocumentoNotaDebitoServiceImpl extends BaseSearchService<MotivoRetornoDocumentoNotaDebito, MotivoRetornoDocumentoNotaDebitoTO, MotivoRetornoDocumentoNotaDebitoPesquisaTO, MotivoRetornoDocumentoNotaDebitoRepository> implements MotivoRetornoDocumentoNotaDebitoService {

	private static final QMotivoRetornoDocumentoNotaDebito qMotivoRetornoDocumentoNotaDebito = QMotivoRetornoDocumentoNotaDebito.motivoRetornoDocumentoNotaDebito;
	private static final Log LOGGER = LogFactory.getLog(MotivoRetornoDocumentoNotaDebitoServiceImpl.class);
	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private NotaDebitoRepository notaDebitoRepository;

	@Autowired
	private NotaRepository notaRepository;

	@Autowired
	private NotaService notaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private EmailAPIJavamail emailAPI;

	@Autowired
	private NotaSaidaService notaDebitoService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private FaturamentoService faturamentoService;


	@Override
	protected void buildSearchQuery(JPAQuery<MotivoRetornoDocumentoNotaDebito> jpaQuery, MotivoRetornoDocumentoNotaDebitoPesquisaTO exampleObj) {
		QNotaDebito qNotaDebito = QNotaDebito.notaDebito;
		QMotivoRetornoDocumento qMotivoRetornoDocumento =  QMotivoRetornoDocumento.motivoRetornoDocumento;
		QMotivoAplicacao qMotivoAplicacao = QMotivoAplicacao.motivoAplicacao;
		QPessoa qPessoa = QPessoa.pessoa;

		jpaQuery.select(
			Projections.fields(
				MotivoRetornoDocumentoNotaDebito.class,
				qMotivoRetornoDocumentoNotaDebito.codigo,
				Projections.fields(
					NotaDebito.class,
					qNotaDebito.codigo,
					qNotaDebito.numero
				).as("notaDebito"),
				Projections.fields(
					MotivoRetornoDocumento.class,
					qMotivoRetornoDocumento.codigo,
					qMotivoRetornoDocumento.observacao,
					qMotivoRetornoDocumento.dataCriacao,
					Projections.fields(
						Pessoa.class,
						qPessoa.codigo,
						qPessoa.nome
					).as("pessoa"),
					Projections.fields(
						MotivoAplicacao.class,
						qMotivoAplicacao.codigo,
						qMotivoAplicacao.nome,
						qMotivoAplicacao.chave,
						qMotivoAplicacao.tipoMotivo,
						qMotivoAplicacao.criador,
						qMotivoAplicacao.dataCriacao
					).as("aplicacao")
				).as("motivoRetornoDocumento")
			)
		);

		jpaQuery
			.join(qMotivoRetornoDocumentoNotaDebito.notaDebito, qNotaDebito)
			.join(qMotivoRetornoDocumentoNotaDebito.motivoRetornoDocumento, qMotivoRetornoDocumento)
			.leftJoin(qMotivoRetornoDocumento.aplicacao, qMotivoAplicacao)
			.leftJoin(qMotivoRetornoDocumento.pessoa, qPessoa)
		.orderBy(qMotivoRetornoDocumento.dataCriacao.desc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoNotaDebitoPesquisaTO exampleObj) {
		QNotaDebito qNotaDebito = QNotaDebito.notaDebito;

		if (exampleObj.getNotaDebito() != null && exampleObj.getNotaDebito().getCodigo() != null) {
			builder.and(qNotaDebito.codigo.eq(exampleObj.getNotaDebito().getCodigo()));
		}
	}

	public MotivoRetornoDocumentoNotaDebitoServiceImpl(MotivoRetornoDocumentoNotaDebitoRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeUpdate(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	@Transactional
	public void prepareSave(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO to) {

		String chaveAplicacao = null;
		//para Reversão do cancelamentos
		if (to != null && to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getAplicacao() != null && "fatura-revert_canc_nf".equals(to.getMotivoRetornoDocumento().getAplicacao().getChave())) {

			notaRepository.updateResponsavelReversaoCancelamento(securityService.getUser().getPessoaFisica(), to.getNotaDebito().getCodigo());
			notaRepository.updateDataReversaoCancelamento(new Date(), to.getNotaDebito().getCodigo());

			NotaDebito notaDebito = notaDebitoRepository.findById(to.getNotaDebito().getCodigo()).orElse(new NotaDebito());

			if (notaDebito.getResponsavelNumeracao() != null) {
				notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.EM_RECEBIMENTO);
			} else {
				notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.ABERTA);
			}
		}

		if (to != null && to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getAplicacao() != null && to.getMotivoRetornoDocumento().getAplicacao().getChave() != null) {
			chaveAplicacao = to.getMotivoRetornoDocumento().getAplicacao().getChave();
		}

		if ("faturamento-cancelar_nd".equals(chaveAplicacao)) {
			LOGGER.info("CANCELAMENTO - Inicinado: faturamento-cancelar_nd");
			prepareSaveCancelamentoNd(entity, to);
			LOGGER.info("CANCELAMENTO - Finalizado");
		}
		if ("faturamento-rever_canc_nd".equals(chaveAplicacao)) {
			prepareSaveRevertCancNd(entity, to);
		}
		if ("faturamento-remover_nd".equals(chaveAplicacao)) {
			prepareSaveRemoverNd(entity, to);
		}

		entity.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(to.getMotivoRetornoDocumento().getAplicacao().getChave()));

		entity.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		entity.getMotivoRetornoDocumento().setCriador(securityService.getUsername());
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

	public void prepareValidate(MotivoRetornoDocumentoNotaDebitoTO to) throws ServiceException {
		if (to.getNotaDebito() == null || to.getNotaDebito().getCodigo() == null) {
			throw new ValidationException("ValidationException","Nota não informada.");
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

	@Override
	@Transactional
	public MotivoRetornoDocumentoNotaDebitoTO retornarNotaDebito(MotivoRetornoDocumentoNotaDebitoTO to) throws ValidationException {
		prepareValidate(to);

		MotivoRetornoDocumentoNotaDebito motivoRetornoDocumentoNotaDebito = map(to);

		String chave = to.getMotivoRetornoDocumento().getAplicacao().getChave();

		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(chave));

		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setCriador(securityService.getUsername());
		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setDataCriacao(new Date());

		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

		motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().setObservacao(to.getMotivoRetornoDocumento().getObservacao());

		if (to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivoDocumentoTO : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento().getListaMotivoRetorno().add(new MotivoDocumento(motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento(), motivoRetornoRepository.findById(motivoDocumentoTO.getMotivoRetorno().getCodigo()).get()));
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(motivoRetornoDocumentoNotaDebito.getMotivoRetornoDocumento());
		motivoRetornoDocumentoNotaDebito.setMotivoRetornoDocumento(motivoRetornoDocumento);

		motivoRetornoDocumentoNotaDebito = getRepository().save(motivoRetornoDocumentoNotaDebito);

		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNd: Atualizando NOTADEBITO " + to.getNotaDebito().getCodigo());
		Date data = new Date(System.currentTimeMillis());
		notaDebitoRepository.updateAtualizador(to.getNotaDebito().getCodigo(), securityService.getUsername());
		notaDebitoRepository.updateDataAtualizacao(to.getNotaDebito().getCodigo(), data);

		if (chave.equals("faturamento-cancelar_nd")) {
			notaDebitoRepository.updateResponsavelCancelamento(to.getNotaDebito().getCodigo(), securityService.getUser().getPessoaFisica());
			notaDebitoRepository.updateDataResponsavelCancelamento(to.getNotaDebito().getCodigo(), data);
			notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.CANCELADA);
		}
		else if (chave.equals("faturamento-rever_canc_nd") || chave.equals("faturamento-canc_receb_nd")) {
			notaDebitoRepository.updateResponsavelReverterCancelamento(to.getNotaDebito().getCodigo(), securityService.getUser().getPessoaFisica());
			notaDebitoRepository.updateDataResponsavelReverterCancelamento(to.getNotaDebito().getCodigo(), data);
			notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.EM_RECEBIMENTO);
		}

		return map(motivoRetornoDocumentoNotaDebito);
	}

	@Transactional
	private void prepareSaveCancelamentoNd(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO to) {
		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNd: Atualizando NOTADEBITO " + to.getNotaDebito().getCodigo());
		notaDebitoRepository.updateResponsavelCancelamento(to.getNotaDebito().getCodigo(), securityService.getUser().getPessoaFisica());
		notaDebitoRepository.updateAtualizador(to.getNotaDebito().getCodigo(), securityService.getUsername());
		notaDebitoRepository.updateDataAtualizacao(to.getNotaDebito().getCodigo(), new Date());
		notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.CANCELADA);
	}

	@Transactional
	private void prepareSaveRevertCancNd(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO to) {
		try {
			notaDebitoRepository.updateStatus(to.getNotaDebito().getCodigo(), NotaDebito.SituacaoNotaDebito.EM_RECEBIMENTO);
		} catch (Exception e) {
			e.printStackTrace();
			emailAPI.sendEmailError("Erro ao reverter cancelamento - prepareSaveRevertCancNd", e);
		}
	}

	/**
	 * Preparação do salvamento para Remover ND numeradao.
	 * Quando o motivo de retorno tem uma aplicação com chave "faturamento-remover_nd".
	 * SGINV-1780
	 *
	 * @param entity
	 * @param to
	 */
	private void prepareSaveRemoverNd(MotivoRetornoDocumentoNotaDebito entity, MotivoRetornoDocumentoNotaDebitoTO to) {
		notaDebitoService.cancelamento(to.getNotaDebito().getCodigo());

		List<FaturamentoEtapaTO> listaFaturamentoEtapaTO = faturamentoEtapaService.obterFaturamentoEtapaPorNotaDebito(to.getNotaDebito().getCodigo());
		FaturamentoEtapaTO faturamentoEtapaTO = listaFaturamentoEtapaTO.get(0);

		faturamentoEtapaService.updateNotaDebito(faturamentoEtapaTO.getCodigo(), null);
		faturamentoEtapaService.updateSituacaoAFaturar(faturamentoEtapaTO.getCodigo());

		String infoCompl = "Faturamento: #{numeroFaturamento} / Etapa: #{numeroEtapaFaturamento}";

		infoCompl = infoCompl
			.replace("#{numeroFaturamento}", "FT/" + String.format("%06d", faturamentoEtapaTO.getFaturamento().getCodigo()))
			.replace("#{numeroEtapaFaturamento}", String.format("%03d", Integer.valueOf(faturamentoEtapaTO.getNumero())));

		List<FaturamentoEtapaTO> etapasDoFaturamento = faturamentoEtapaService.obterFaturamentoEtapaPorCodFaturamento(faturamentoEtapaTO.getFaturamento().getCodigo());

		Boolean mudarSituacaoFaturamento = Boolean.FALSE;

		if (etapasDoFaturamento.size() == 1) {
			mudarSituacaoFaturamento = Boolean.TRUE;
		} else {
			Boolean temSituacaoDiferente = Boolean.FALSE;
			for (FaturamentoEtapaTO etapaTO : etapasDoFaturamento) {
				if (!"A faturar".equals(etapaTO.getSituacao()) && !"Cancelada".equals(etapaTO.getSituacao())) {
					temSituacaoDiferente = Boolean.TRUE;
					break;
				}
			}

			if (!temSituacaoDiferente) {
				mudarSituacaoFaturamento = Boolean.TRUE;
			}
		}

		if (mudarSituacaoFaturamento) {
			faturamentoService.updateSituacaoAberto(faturamentoEtapaTO.getFaturamento().getCodigo());
		}
	}
}
