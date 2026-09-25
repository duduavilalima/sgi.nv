package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoNota;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoNota;
import br.com.araujoabreu.sgi.api.EmailAPIJavamail;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceNotaService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoNotaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoNotaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.NotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoNotaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import br.com.fasolti.sgi.repository.nota.NotaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoNotaServiceImpl extends BaseSearchService<MotivoRetornoNota, MotivoRetornoNotaTO, MotivoRetornoNotaPesquisaTO, MotivoRetornoNotaRepository> implements MotivoRetornoNotaService {

	private static final QMotivoRetornoNota qMotivoRetornoNota = QMotivoRetornoNota.motivoRetornoNota;
	private static final Log LOGGER = LogFactory.getLog(MotivoRetornoNotaServiceImpl.class);
	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private FaturamentoService faturamentoService;

	@Autowired
	private MotivoRetornoDocumentoService motivoRetornoDocumentoService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private LoteInterfaceNotaService loteInterfaceNotaService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private NotaRepository notaRepository;

	@Autowired
	private NotaService notaService;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private EmailAPIJavamail emailAPI;

	@Autowired
	public MotivoRetornoNotaServiceImpl(MotivoRetornoNotaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoNotaPesquisaTO exampleObj) {
		if (exampleObj.getNota() != null && exampleObj.getNota().getCodigo() != null) {
			builder.and(qMotivoRetornoNota.nota.codigo.eq(exampleObj.getNota().getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<MotivoRetornoNota> entities, List<MotivoRetornoNotaPesquisaTO> result) throws ServiceException {
		for (MotivoRetornoNotaPesquisaTO to : result) {
			MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.findById(to.getMotivoRetornoDocumento().getCodigo()).orElse(null);

			Pessoa pessoa = pessoaRepository.findById(motivoRetornoDocumento.getPessoa().getCodigo()).orElse(null);

			to.setMotivoRetornoDocumento(new MotivoRetornoDocumentoTO(motivoRetornoDocumento.getCodigo(), motivoRetornoDocumento.getObservacao(), new PessoaTO(pessoa.getCodigo(), pessoa.getDocumento(), pessoa.getNome(), pessoa.getTipoPessoa().toString(), pessoa.getSituacao().toString())));
			to.getMotivoRetornoDocumento().setCriador(motivoRetornoDocumento.getCriador());
			to.getMotivoRetornoDocumento().setDataCriacao(motivoRetornoDocumento.getDataCriacao());
			to.getMotivoRetornoDocumento().setAtualizador(motivoRetornoDocumento.getAtualizador());
			to.getMotivoRetornoDocumento().setDataAtualizacao(motivoRetornoDocumento.getDataAtualizacao());

			if (motivoRetornoDocumento.getAplicacao() != null && motivoRetornoDocumento.getAplicacao().getCodigo() != null) {
				MotivoAplicacaoTO motivoAplicacaoTO = new MotivoAplicacaoTO();
				motivoAplicacaoTO.setNome(motivoRetornoDocumento.getAplicacao().getNome());
				motivoAplicacaoTO.setChave(motivoRetornoDocumento.getAplicacao().getChave());
				motivoAplicacaoTO.setTipoMotivo(motivoRetornoDocumento.getAplicacao().getTipoMotivo().toString());
				to.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoTO);
			}

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
	protected void doBeforeUpdate(MotivoRetornoNota entity, MotivoRetornoNotaTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoNota entity, MotivoRetornoNotaTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	@Transactional
	public void prepareSave(MotivoRetornoNota entity, MotivoRetornoNotaTO to) {

		String chaveAplicacao = null;
		//para Reversão do cancelamentos
		if (to != null && to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getAplicacao() != null && "fatura-revert_canc_nf".equals(to.getMotivoRetornoDocumento().getAplicacao().getChave())) {

			notaRepository.updateResponsavelReversaoCancelamento(securityService.getUser().getPessoaFisica(), to.getNota().getCodigo());
			notaRepository.updateDataReversaoCancelamento(new Date(), to.getNota().getCodigo());

			NotaSaida notaSaida = notaSaidaRepository.findById(to.getNota().getCodigo()).orElse(new NotaSaida());

			if (notaSaida.getResponsavelNumeracao() != null) {
				notaSaidaRepository.updateStatus(to.getNota().getCodigo(), Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO);
			} else {
				notaSaidaRepository.updateStatus(to.getNota().getCodigo(), Nota.StatusNota.ABERTA_FATURAMENTO);
			}
		}

		if (to != null && to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getAplicacao() != null && to.getMotivoRetornoDocumento().getAplicacao().getChave() != null) {
			chaveAplicacao = to.getMotivoRetornoDocumento().getAplicacao().getChave();
		}

		if ("faturamento-canc_nf".equals(chaveAplicacao)) {
			LOGGER.info("CANCELAMENTO - Inicinado: faturamento-canc_nf");
			prepareSaveCancelamentoNf(entity, to);
			LOGGER.info("CANCELAMENTO - Finalizado");
		}
		if ("fatura-revert_canc_nf".equals(chaveAplicacao)) {
			prepareSaveRevertCancNf(entity, to);
		}
		if ("faturamento-remover_nf".equals(chaveAplicacao)) {
			prepareSaveRemoverNf(entity, to);
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

	public void prepareValidate(MotivoRetornoNotaTO to) throws ServiceException {
		if (to.getNota() == null || to.getNota().getCodigo() == null) {
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

	/**
	 * Preparação do salvamento para cancelamento da NF.
	 * Quando o motivo de retorno tem uma aplicação com chave "faturamento-canc_nf".
	 *
	 * @param entity
	 * @param to
	 */
	@Transactional
	private void prepareSaveCancelamentoNf(MotivoRetornoNota entity, MotivoRetornoNotaTO to) {
		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: Atualizando NOTAFISCAL " + to.getNota().getCodigo());
		notaSaidaRepository.updateResponsavelCancelamento(to.getNota().getCodigo(), securityService.getUser().getPessoaFisica());
		notaSaidaRepository.updateDataCancelamento(to.getNota().getCodigo(), to.getDataCancelamento());
		notaSaidaRepository.updateAtualizador(to.getNota().getCodigo(), securityService.getUsername());
		notaSaidaRepository.updateDataAtualizacao(to.getNota().getCodigo(), new Date());
		notaSaidaRepository.updateStatus(to.getNota().getCodigo(), Nota.StatusNota.CANCELADA_FATURAMENTO);

		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: FindNotaSaidaByCodigoNotaSaida para " + to.getNota().getCodigo());
		JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaNotFinalizada(to.getNota().getCodigo());

		List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: Número de parcelas " + parcelas.size());

		for (NotaSaidaParcela parcela : parcelas) {
			notaSaidaParcelaService.cancelamento(parcela.getCodigo());
			LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: Parcela cancelada código " + parcela.getCodigo());
		}
	}

	/**
	 * Preparação do salvamento para Reversão do cancelamento.
	 * Quando o motivo de retorno tem uma aplicação com chave "fatura-revert_canc_nf".
	 *
	 * @param entity
	 * @param to
	 */
	@Transactional
	private void prepareSaveRevertCancNf(MotivoRetornoNota entity, MotivoRetornoNotaTO to) {
		try {
			notaService.updateResponsavelReversaoCancelamento(to.getNota().getCodigo(), securityService.getUser().getPessoaFisica().getCodigo());
			notaService.updateDataReversaoCancelamento(to.getNota().getCodigo(), new Date());

			NotaSaidaTO notaSaidaTO = notaSaidaService.obterNotaSaidaApenasCodigoNumeroStatusResponsavelNumeracao(to.getNota().getCodigo());

			if (notaSaidaTO.getResponsavelNumeracao() != null) {
				notaSaidaService.updateStatusEmRecebimentoFaturamento(to.getNota().getCodigo());
			} else {
				notaSaidaService.updateStatusAbertaFaturamento(to.getNota().getCodigo());
			}

			JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaida(to.getNota().getCodigo());

			List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

			notaSaidaService.updateReverterCancelamentoERemoverCodigoAssociarNFSubstituta(to.getNota().getCodigo());

			for (NotaSaidaParcela parcela : parcelas) {
				if (notaSaidaTO.getResponsavelNumeracao() != null) {
					notaSaidaParcelaService.updateSituacaoEmRecebimento(parcela.getCodigo());
				} else {
					notaSaidaParcelaService.updateSituacaoAberta(parcela.getCodigo());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			emailAPI.sendEmailError("Erro ao reverter cancelamento - prepareSaveRevertCancNf", e);
		}
	}

	/**
	 * Preparação do salvamento para Remover NF numeradao.
	 * Quando o motivo de retorno tem uma aplicação com chave "faturamento-remover_nf".
	 * SGINV-1780
	 *
	 * @param entity
	 * @param to
	 */
	private void prepareSaveRemoverNf(MotivoRetornoNota entity, MotivoRetornoNotaTO to) {
		notaFiscalService.cancelamento(to.getNota().getCodigo());

		JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaNotFinalizada(to.getNota().getCodigo());

		List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

		for (NotaSaidaParcela parcela : parcelas) {
			notaSaidaParcelaService.cancelamento(parcela.getCodigo());
		}

		List<FaturamentoEtapaTO> listaFaturamentoEtapaTO = faturamentoEtapaService.obterFaturamentoEtapaPorNotaSaida(to.getNota().getCodigo());
		FaturamentoEtapaTO faturamentoEtapaTO = listaFaturamentoEtapaTO.get(0);

		faturamentoEtapaService.updateNotaSaida(faturamentoEtapaTO.getCodigo(), null);
		faturamentoEtapaService.updateSituacaoAFaturar(faturamentoEtapaTO.getCodigo());

		String infoCompl = "Faturamento: #{numeroFaturamento} / Etapa: #{numeroEtapaFaturamento}";

		infoCompl = infoCompl
			.replace("#{numeroFaturamento}", "FT/" + String.format("%06d", faturamentoEtapaTO.getFaturamento().getCodigo()))
			.replace("#{numeroEtapaFaturamento}", String.format("%03d", Integer.valueOf(faturamentoEtapaTO.getNumero())));

		NotaSaidaTO notaSaidaTO = notaSaidaService.getNotaSaidaByCodigo(to.getNota().getCodigo());

		String infoInicial = notaSaidaTO.getInfoComplementarContribuinte();
		String infoFinal;

		if (infoInicial == null || infoInicial.trim().length() == 0) {
			infoFinal = infoCompl;
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(infoInicial).append("\n\n").append(infoCompl);
			infoFinal = builder.toString();
		}

		notaSaidaService.updateInfoComplementarContribuinte(to.getNota().getCodigo(), infoFinal);

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

		enviarEmailRemocaoNf(faturamentoEtapaTO, notaSaidaTO, parcelas.size());
	}

	/**
	 * Envia e-mail sobre a remoção da Nota Fiscal.
	 *
	 * @param faturamentoEtapaTO
	 * @param notaSaidaTO
	 * @param qtdParcelas
	 */
	private void enviarEmailRemocaoNf(FaturamentoEtapaTO faturamentoEtapaTO, NotaSaidaTO notaSaidaTO, int qtdParcelas) {
		String FATURAMENTO_REMOVER_EMAIL = "faturamento.remover.logico.email";
		String emails = parametroRepository.findByChave(FATURAMENTO_REMOVER_EMAIL).getValor();
		List<String> listaEmail = new ArrayList<>();

		if (emails != null && emails.length() > 0) {
			if (emails.contains(";")) {
				String[] arrayEmail = emails.split(";");
				for (String email : arrayEmail) {
					listaEmail.add(email.trim());
				}

			} else {
				listaEmail.add(emails.trim());
			}
		}

		if (listaEmail != null && !listaEmail.isEmpty()) {
			String msgBody = criarBodyEmail(faturamentoEtapaTO, notaSaidaTO, qtdParcelas);
			String fromAddress = "suporte_sgi@copony.com.br";
			String titulo = "[Araujo Abreu] Exclusão de NF numerada";

			for (String email : listaEmail) {
				if (GlobalBean.validarEmail(email)) {
					emailAPI.sendEmail(email, fromAddress, titulo, msgBody);
				}
			}
		}
	}

	private String criarBodyEmail(FaturamentoEtapaTO faturamentoEtapaTO, NotaSaidaTO notaSaidaTO, int qtdParcelas) {

		List<LoteInterfaceNotaTO> listaInterface = loteInterfaceNotaService.findListaByCodNota(notaSaidaTO.getCodigo());

		Boolean notaTemInterfaceassociado = Boolean.FALSE;

		String numeroDoLote = "";

		if (listaInterface != null && !listaInterface.isEmpty()) {
			for (LoteInterfaceNotaTO loteInterfaceNotaTO : listaInterface) {
				if ("Enviada".equals(loteInterfaceNotaTO.getTipo())) {
					notaTemInterfaceassociado = Boolean.TRUE;
					numeroDoLote = "LT/" + loteInterfaceNotaTO.getLoteInterface().getCodigo();
				}
			}
		}

		StringBuilder builder = new StringBuilder();

		builder
			.append("<html>\n")
			.append("\t\t    <body>\n")
			.append("            <p>Prezado,</p>\n");

		if (notaTemInterfaceassociado) {
			builder.append("            <p>A <b>#{numeroNotaFiscal}</b> do faturamento <b>#{numeroFaturamento}</b> (etapa <b>#{numeroEtapaFaturamento}</b>) foi removida (<b>#{numeroParcelas}</b>) e já estava numerada (<b>#{respNumeracao}</b> em <b>#{dataRespNumeracao}</b>) e estava no <b>#{numeroLote}</b>.</p>\n");
		} else {
			builder.append("            <p>A <b>#{numeroNotaFiscal}</b> do faturamento <b>#{numeroFaturamento}</b> (etapa <b>#{numeroEtapaFaturamento}</b>) foi removida (<b>#{numeroParcelas}</b>) e já estava numerada (<b>#{respNumeracao}</b> em <b>#{dataRespNumeracao}</b>).</p>\n");
		}

		builder.append("            <p/>\n")
			.append("\t\t        <p>Equipe Araujo Abreu</p>\n")
			.append("\t\t        <p><i>Esta mensagem foi gerada automaticamente pelo Sistema de Gestão Integrada da Araujo Abreu.</i></p>\n")
			.append("\t\t    </body>\n")
			.append("\t\t</html>       ");

		String body = builder.toString();

		String parcela;
		if (qtdParcelas == 1) {
			parcela = "parcela única";
		} else {
			parcela = qtdParcelas + " parcelas";
		}

		body = body
			.replace("#{numeroNotaFiscal}", notaSaidaTO.getNumero())
			.replace("#{numeroFaturamento}", "FT/" + String.format("%06d", faturamentoEtapaTO.getFaturamento().getCodigo()))
			.replace("#{numeroEtapaFaturamento}", String.format("%03d", Integer.valueOf(faturamentoEtapaTO.getNumero())))
			.replace("#{numeroParcelas}", parcela)
			.replace("#{respNumeracao}", notaSaidaTO.getResponsavelNumeracao().getNome())
			.replace("#{dataRespNumeracao}", GlobalBean.formatarData(notaSaidaTO.getDataNumeracao(), "dd/MM/yyyy HH:mm:ss"))
			.replace("#{numeroLote}", numeroDoLote);

		return body;
	}

	@Override
	@Transactional
	public MotivoRetornoNotaTO saveNotaFiscal(MotivoRetornoNotaTO to) throws ValidationException {
		prepareValidate(to);

		MotivoRetornoNota motivoRetornoNota = map(to);

		motivoRetornoNota.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave("faturamento-canc_nf"));

		motivoRetornoNota.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		motivoRetornoNota.getMotivoRetornoDocumento().setCriador(securityService.getUsername());
		motivoRetornoNota.getMotivoRetornoDocumento().setDataCriacao(new Date());

		motivoRetornoNota.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

		motivoRetornoNota.getMotivoRetornoDocumento().setObservacao(to.getMotivoRetornoDocumento().getObservacao());

		if (to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivoDocumentoTO : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				motivoRetornoNota.getMotivoRetornoDocumento().getListaMotivoRetorno().add(new MotivoDocumento(motivoRetornoNota.getMotivoRetornoDocumento(), motivoRetornoRepository.findById(motivoDocumentoTO.getMotivoRetorno().getCodigo()).get()));
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(motivoRetornoNota.getMotivoRetornoDocumento());
		motivoRetornoNota.setMotivoRetornoDocumento(motivoRetornoDocumento);

		motivoRetornoNota = getRepository().save(motivoRetornoNota);

		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: Atualizando NOTAFISCAL " + to.getNota().getCodigo());
		notaSaidaRepository.updateResponsavelCancelamento(to.getNota().getCodigo(), securityService.getUser().getPessoaFisica());
		notaSaidaRepository.updateDataCancelamento(to.getNota().getCodigo(), to.getDataCancelamento());
		notaSaidaRepository.updateAtualizador(to.getNota().getCodigo(), securityService.getUsername());
		notaSaidaRepository.updateDataAtualizacao(to.getNota().getCodigo(), new Date());
		notaSaidaRepository.updateStatus(to.getNota().getCodigo(), Nota.StatusNota.CANCELADA_FATURAMENTO);

		LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: FindNotaSaidaByCodigoNotaSaida para " + to.getNota().getCodigo());
		List<NotaSaidaParcela> parcelas = (List<NotaSaidaParcela>) notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaNotFinalizada(to.getNota().getCodigo()).fetch();

		for (NotaSaidaParcela parcela : parcelas) {
			LOGGER.info("CANCELAMENTO - PrepareSaveCancelamentoNf: Parcela cancelada código " + parcela.getCodigo());
			notaSaidaParcelaService.cancelamento(parcela.getCodigo());
		}

		return map(motivoRetornoNota);
	}
}
