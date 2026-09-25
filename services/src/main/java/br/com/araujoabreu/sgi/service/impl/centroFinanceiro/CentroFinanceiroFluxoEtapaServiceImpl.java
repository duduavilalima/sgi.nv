package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.comercial.domain.Fecho;
import br.com.araujoabreu.sgi.service.administracao.EtapaFluxoService;
import br.com.araujoabreu.sgi.service.administracao.FluxoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroFluxoEtapaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoAplicacaoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.RetornoAssinaturaTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroFluxoEtapaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.etapafluxo.EtapaFluxoRepository;
import br.com.fasolti.sgi.repository.fecho.FechoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoCentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CentroFinanceiroFluxoEtapaServiceImpl extends BaseSearchService<CentroFinanceiroFluxoEtapa, CentroFinanceiroFluxoEtapaTO, CentroFinanceiroFluxoEtapaPesquisaTO, CentroFinanceiroFluxoEtapaRepository> implements CentroFinanceiroFluxoEtapaService {

	private static final QCentroFinanceiroFluxoEtapa qCentroFinanceiroFluxoEtapa = QCentroFinanceiroFluxoEtapa.centroFinanceiroFluxoEtapa;

	@Autowired
	private EtapaFluxoRepository etapaFluxoRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private FechoRepository fechoRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoRetornoCentroFinanceiroRepository motivoRetornoCentroFinanceiroRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private FechoService fechoService;

	@Autowired
	private EtapaFluxoService etapaFluxoService;

	@Autowired
	private FluxoService fluxoService;

	@Autowired
	private MotivoAplicacaoService motivoAplicacaoService;

	@Autowired
	private PessoaContaContabilRepository pessoaContaContabilRepository;

	@Autowired
	public CentroFinanceiroFluxoEtapaServiceImpl(CentroFinanceiroFluxoEtapaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<CentroFinanceiroFluxoEtapa> query, CentroFinanceiroFluxoEtapaPesquisaTO exampleObj) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		query.leftJoin(qCentroFinanceiroFluxoEtapa.centroFinanceiro, qCentroFinanceiro);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CentroFinanceiroFluxoEtapaPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCentroFinanceiro()) && Check.isAssigned(exampleObj.getCentroFinanceiro().getCodigo())) {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			builder.and(qCentroFinanceiro.codigo.eq(exampleObj.getCentroFinanceiro().getCodigo()));
		}
	}

	@Override
	public List<EtapaFluxoTO> buscarTodosEtapaFluxo() {
		List<EtapaFluxoTO> listaTO = new ArrayList<>();
		return etapaFluxoService.obterEtapaFluxoColaboradorEPerfil();
	}

	/**
	 * Cria e retona uma lista de assinaturas.
	 *
	 * @return
	 */
	@Override
	public List<CentroFinanceiroFluxoEtapaTO> criarAssinaturas() {
		List<CentroFinanceiroFluxoEtapaTO> listaAssinaturas = new ArrayList<>();
		List<Fluxo> fluxos = this.findFluxoByNome("Manutenção");
		if (fluxos != null && !fluxos.isEmpty()) {
			Fluxo fluxo = fluxos.get(0);
			List<EtapaFluxo> etapas = etapaFluxoRepository.findByFluxo(fluxo);
			if (etapas != null && !etapas.isEmpty()) {
				for (EtapaFluxo etapa : etapas) {
					CentroFinanceiroFluxoEtapa cfFluxo = new CentroFinanceiroFluxoEtapa();
					cfFluxo.setEtapaFluxo(etapa);
					CentroFinanceiroFluxoEtapaTO to = getMapper().map(cfFluxo, CentroFinanceiroFluxoEtapaTO.class);
					listaAssinaturas.add(to);
				}
			}
		}

		return listaAssinaturas;
	}

	/**
	 * Busca uma lista de FLuxos pelo nome.
	 *
	 * @param nome
	 * @return
	 */
	private List<Fluxo> findFluxoByNome(String nome) {
		List<FluxoTO> listaTO = fluxoService.findFluxoByNome(nome);

		List<Fluxo> lista = new ArrayList<>();

		for (FluxoTO fluxoTO : listaTO) {
			lista.add(getMapper().map(fluxoTO, Fluxo.class));
		}

		return lista;
	}

	@Override
	public MotivoAplicacaoTO motivoAplicacaoByFluxoEtapaId(String id) throws Exception {
		try {
			MotivoAplicacao motivoAplicacao = buscarMotivoAplicacaoByFluxoEtapaId(Long.parseLong(id));
			return getMapper().map(motivoAplicacao, MotivoAplicacaoTO.class);
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	private MotivoAplicacao buscarMotivoAplicacaoByFluxoEtapaId(Long id) {

		EtapaFluxoTO etapaFluxoTO = etapaFluxoService.obterEtapaFLuxoComMotivoAplicacao(id);

		return getMapper().map(etapaFluxoTO.getMotivoAplicacao(), MotivoAplicacao.class);
	}

	/**
	 * Busca uma lista de CentroFinanceiroFluxoEtapa pelo codigo do CentroFinanceiro
	 * @param codCentroFinanceiro
	 * @return
	 */
	private List<CentroFinanceiroFluxoEtapa> findCfFluxoEtapaByCodCf(Long codCentroFinanceiro) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QEtapaFluxo qEtapaFluxo = QEtapaFluxo.etapaFluxo;

		JPAQuery<CentroFinanceiroFluxoEtapa> jpaQuery = select();

		QBean<CentroFinanceiroFluxoEtapa> qBean = Projections.bean(
			CentroFinanceiroFluxoEtapa.class,
			qCentroFinanceiroFluxoEtapa.codigo,
				Projections.bean(EtapaFluxo.class,
					qEtapaFluxo.codigo,
					qEtapaFluxo.nome,
					qEtapaFluxo.statusAnterior,
					qEtapaFluxo.statusAtual,
					qEtapaFluxo.proximoStatus,
					qEtapaFluxo.statusBase,
					qEtapaFluxo.ordem)
				.as(qCentroFinanceiroFluxoEtapa.etapaFluxo)
		);

		jpaQuery
			.select(qBean)
			.from(qCentroFinanceiroFluxoEtapa)
			.join(qCentroFinanceiroFluxoEtapa.etapaFluxo, qEtapaFluxo)
			.leftJoin(qCentroFinanceiroFluxoEtapa.centroFinanceiro, qCentroFinanceiro)
			.where(qCentroFinanceiro.codigo.eq(codCentroFinanceiro));

		List<CentroFinanceiroFluxoEtapa> lista = jpaQuery.fetch();

		return lista;
	}

	@Transactional
	@Override
	public MenssagemPadraoTO assinarCentroFinanceiro(Long codEtapaFluxo, Long codCentroFinanceiro) {
		String FECHO_INICIAL = "Fecho inicial";

		CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(codCentroFinanceiro).orElse(null);
		EtapaFluxo etapaFluxo = etapaFluxoRepository.findById(codEtapaFluxo).orElse(null);


		MenssagemPadraoTO menssagemPadraoTO = new MenssagemPadraoTO();
		assert etapaFluxo != null;
		assert centroFinanceiro != null;
		if (etapaFluxo.getValidarContaContabilCliente() && !possuiContaContabilAssociadaAhMatrizDoCL(centroFinanceiro) ){
			String nomeEmpresa = centroFinanceiro.getUnidade().getMatriz() != null ?
				centroFinanceiro.getUnidade().getMatriz().getCodnome() : centroFinanceiro.getUnidade().getMatriz().getCodnome();
			 menssagemPadraoTO.addWarnings(
				String.format("Este cliente não possui conta contábil associada para a empresa '%s'. É necessário o cadastro para evitar problemas nos lotes contábeis.",nomeEmpresa));
		}

		Long codigoColaborador = securityService.getUser().getPessoaFisica().getCodigo();
		Colaborador colaborador = colaboradorRepository.findById(codigoColaborador).orElse(null);

		List<CentroFinanceiroFluxoEtapa> listaFluxo = findCfFluxoEtapaByCodCf(codCentroFinanceiro);

		CentroFinanceiroFluxoEtapa assinatura = new CentroFinanceiroFluxoEtapa();
		assinatura.setEtapaFluxo(etapaFluxo);

		if (!listaFluxo.isEmpty()) {
			for (CentroFinanceiroFluxoEtapa item : listaFluxo) {
				if (item.getEtapaFluxo().getCodigo().equals(etapaFluxo.getCodigo())) {
					assinatura = item;
				}
			}
		}

		assinatura.setCentroFinanceiro(centroFinanceiro);
		assinatura.setColaborador(colaborador);
		assinatura.setDataRealizacao(new Date(System.currentTimeMillis()));

		//FIXME acertar isso!
		CentroFinanceiro.SituacaoCentroFinanceiro scfAtual = centroFinanceiro.getSituacao();
		CentroFinanceiro.SituacaoCentroFinanceiro scfNova = centroFinanceiro.getSituacao();

		if (assinatura.getEtapaFluxo().getStatusBase() == null) {
			if (assinatura.getEtapaFluxo().getProximoStatus() != null) {
				scfNova = GlobalBean.getSituacaoCF(assinatura.getEtapaFluxo().getProximoStatus());
				centroFinanceiro.setSituacao(scfNova);
				if ("Orçamento".equalsIgnoreCase(assinatura.getEtapaFluxo().getNome())) {
					List<FechoTO> listaFechoTO = fechoService.buscarListaFechoByCodCentroFinanceiro(centroFinanceiro.getCodigo());

					if (listaFechoTO != null && listaFechoTO.size() > 0) {
						FechoTO fechoTO = listaFechoTO.get(0);
						if (fechoTO != null && fechoTO.getCodigo() != null) {
							Fecho fecho = fechoRepository.findById(listaFechoTO.get(0).getCodigo()).orElseThrow(EntityNotFoundException::new);
							fecho.setResponsavelReajuste(assinatura.getColaborador());
							fecho.setDataReajuste(assinatura.getDataRealizacao());
							fecho.setIndiceRajuste(FECHO_INICIAL);
							fechoRepository.save(fecho);
						}
					}
				}

			} else {
				centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);

				List<FechoTO> listaFechoTO = fechoService.buscarListaFechoByCodCentroFinanceiro(centroFinanceiro.getCodigo());
				if (listaFechoTO != null && listaFechoTO.size() > 0) {
					for (FechoTO to : listaFechoTO) {
						Fecho fecho = fechoRepository.findById(to.getCodigo()).orElseThrow(EntityNotFoundException::new);
						fecho.setDataReajuste(new Date(System.currentTimeMillis()));
						fechoRepository.save(fecho);
					}
				}
			}

		} else {
			// CRIA 1º LISTA
			// OBTER AS ASSINATURAS.
			List<CentroFinanceiroFluxoEtapa> listaAssinaturasBase = new ArrayList<>();
			List<Fluxo> fluxos = findFluxoByNome("Manutenção");
			if (fluxos != null && !fluxos.isEmpty()) {
				Fluxo fluxo = fluxos.get(0);
				List<EtapaFluxo> etapas = etapaFluxoRepository.findByFluxo(fluxo);
				if (etapas != null && !etapas.isEmpty()) {
					for (EtapaFluxo etapa2 : etapas) {
						CentroFinanceiroFluxoEtapa cfFluxo = new CentroFinanceiroFluxoEtapa();
						if (etapa2.getStatusBase() != null) {
							cfFluxo.setEtapaFluxo(etapa2);
							listaAssinaturasBase.add(cfFluxo);
						}
					}
				}
			}

			// CRIA 2º LISTA
			List<CentroFinanceiroFluxoEtapa> listaAssinaturasBaseCentroFinanceiro = new ArrayList<>();
			listaAssinaturasBaseCentroFinanceiro.add(assinatura);

			List<CentroFinanceiroFluxoEtapa> assinaturasDoCentroFinanceiro = findCfFluxoEtapaByCodCf(centroFinanceiro.getCodigo());
			for (CentroFinanceiroFluxoEtapa fluxoEtapa : assinaturasDoCentroFinanceiro) {
				if (fluxoEtapa.getEtapaFluxo().getStatusBase() != null) {
					listaAssinaturasBaseCentroFinanceiro.add(fluxoEtapa);
				}
			}

			// COMPARA O TAMANHO DAS DUAS LISTAS.
			// SE A QUANDIDADE DE ASSINATURAS IGUAL A ASSINADAS.
			if (listaAssinaturasBase.size() == listaAssinaturasBaseCentroFinanceiro.size()) {
				if (assinatura.getEtapaFluxo().getStatusBase() == null) {
					if (assinatura.getEtapaFluxo().getProximoStatus() != null) {
						scfNova = GlobalBean.getSituacaoCF(assinatura.getEtapaFluxo().getProximoStatus());
						centroFinanceiro.setSituacao(scfNova);
					} else {
						centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);

						List<FechoTO> listaFechoTO = fechoService.buscarListaFechoByCodCentroFinanceiro(centroFinanceiro.getCodigo());
						if (listaFechoTO != null && listaFechoTO.size() > 0) {
							for (FechoTO to : listaFechoTO) {
								Fecho fecho = fechoRepository.findById(to.getCodigo()).orElse(null);
								fecho.setDataReajuste(new Date(System.currentTimeMillis()));
								fechoRepository.save(fecho);
							}
						}
					}
				} else {
					EtapaFluxo etapa = etapaFluxoRepository.findByCodFluxoAndStatusAtual(assinatura.getEtapaFluxo().getFluxo().getCodigo(), assinatura.getEtapaFluxo().getStatusBase());
					scfNova = GlobalBean.getSituacaoCF(etapa.getProximoStatus());
					centroFinanceiro.setSituacao(scfNova);
				}
			}
		}

		getRepository().save(assinatura);

		// indica que houve mudança na situação do CF, logo deve ser incluído na tabela centro_financeiro_situacao
		if (!scfAtual.equals(scfNova)) {
			contratoService.adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), scfNova.toString());
		}

		centroFinanceiroRepository.save(centroFinanceiro);
		if (centroFinanceiro.getContrato() != null && centroFinanceiro.getContrato().getCodigo() != null) {
			contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());

			// FIXME: No Legado, neste trecho do código, ainda é implementado um metodo de envio de SMS para usuário.
			// Anotações do Legado: É feita a informação aqui, pois se houver algum erro de banco, as mensagens não serão enviadas,
			//                      Ou se houver erro de envio de mensagem, não afetará a persistência da assinatura.
		}

		return menssagemPadraoTO;
	}

	private boolean possuiContaContabilAssociadaAhMatrizDoCL(CentroFinanceiro centroFinanceiro) {

		Long codigoCliente = centroFinanceiro.getContrato() != null &&
			centroFinanceiro.getContrato().getCliente() != null ?
				centroFinanceiro.getContrato().getCliente().getCodigo() : 0l;
		Long codigoUnidade = centroFinanceiro.getUnidade().getMatriz() != null ?
			centroFinanceiro.getUnidade().getMatriz().getCodigo() : centroFinanceiro.getUnidade().getCodigo();

		Long quantidadeContasContabeis = pessoaContaContabilRepository.countByPessoaCodigoAndContaContabilEmpresaCodigo(codigoCliente, codigoUnidade);

		return quantidadeContasContabeis > 0 ? true : false;

	}

	/**
	 * Método para retorar assinatura do centro financeiro
	 * Comercial > Centros Financeiros > Exibição (ABA ASSINATURAS)
	 *
	 * @param retornoAssinaturaTO
	 */
	@Override
	@Transactional
	public void retornarAssinaturaCentroFinanceiro(RetornoAssinaturaTO retornoAssinaturaTO) {
		Long codColaborador = securityService.getUser().getPessoaFisica().getCodigo();
		Long codEtapaFluxo = retornoAssinaturaTO.getCodEtapaFluxo();
		Long codCentroFinanceiro = retornoAssinaturaTO.getCodCentroFinanceiro();
		String chaveMotivoAplicacao = retornoAssinaturaTO.getChave();

		MotivoRetornoDocumentoTO motivoRetornoDocumentoTO = retornoAssinaturaTO.getMotivoRetornoDocumento();

		Colaborador colaborador = new Colaborador(codColaborador);
		CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(codCentroFinanceiro).orElse(null);
		MotivoRetornoDocumento motivoRetornoDocumento = getMapper().map(motivoRetornoDocumentoTO, MotivoRetornoDocumento.class);
		EtapaFluxo etapaFluxo = null;

		EtapaFluxoTO etapaFluxoTO = etapaFluxoService.obterEtapaFLuxoComMotivoAplicacao(codEtapaFluxo);
		EtapaFluxo etapaFluxoSelecionado = getMapper().map(etapaFluxoTO, EtapaFluxo.class);

		if (ObjectUtil.isNull(etapaFluxoSelecionado.getStatusBase())) {
			etapaFluxo = etapaFluxoSelecionado;
		} else {
			EtapaFluxoTO etapaFluxoTO2 = etapaFluxoService.findByCodFluxoAndStatusAtual(1L, etapaFluxoSelecionado.getStatusBase());
			etapaFluxo = getMapper().map(etapaFluxoTO2, EtapaFluxo.class);
		}

		limparSituacoes(etapaFluxo, codCentroFinanceiro);
		persistirMotivoRetornoCentroFinanceiro(motivoRetornoDocumento, centroFinanceiro, etapaFluxo, colaborador, chaveMotivoAplicacao);
	}

	/**
	 * Função que remove algumas ou todas as assinaturas(CentroFinanceiroFluxoEtapa) do centro financeiro
	 *
	 * @param etapaFluxo
	 * @param codCentroFinanceiro
	 */
	private void limparSituacoes(EtapaFluxo etapaFluxo, Long codCentroFinanceiro) {
		Integer situacaoAtual = etapaFluxo.getStatusAnterior();
		List<CentroFinanceiroFluxoEtapa> listaCfLixo = new ArrayList<>();
		Integer ordem = null;

		List<CentroFinanceiroFluxoEtapa> listaAssinaturas = findCfFluxoEtapaByCodCf(codCentroFinanceiro);

		if (listaAssinaturas != null && !listaAssinaturas.isEmpty()) {

			for (CentroFinanceiroFluxoEtapa cf : listaAssinaturas) {
				if (cf.getEtapaFluxo().getStatusAtual().equals(situacaoAtual)) {
					ordem = cf.getEtapaFluxo().getOrdem();
					break;
				}
			}

			if (ordem != null) {
				for (CentroFinanceiroFluxoEtapa cf : listaAssinaturas) {
					if (cf.getEtapaFluxo().getOrdem() >= ordem) {
						listaCfLixo.add(cf);
					}
				}
			}

			if (listaCfLixo != null && !listaCfLixo.isEmpty()) {
				for (CentroFinanceiroFluxoEtapa cf : listaCfLixo) {
					CentroFinanceiroFluxoEtapa assinatura = getRepository().findById(cf.getCodigo()).orElse(null);
					getRepository().delete(assinatura);
				}
			}
		}
	}

	/**
	 * Executa o retorno da assinatura do centro financeiro, atualizando e inserindo dados novos no banco de dados.
	 * Tabela Atualizada: centro_financeiro
	 * tabelas persistidas: centro_financeiro_situacao, motivo_retorno_documento, motivo_retorno_documento_cf, motivo_retorno_motivo_retorno_documento
	 *
	 * @param motivoRetornoDocumento
	 * @param centroFinanceiro
	 * @param etapaFluxo
	 * @param colaborador
	 * @param chave
	 */
	@Transactional
	private void persistirMotivoRetornoCentroFinanceiro(MotivoRetornoDocumento motivoRetornoDocumento, CentroFinanceiro centroFinanceiro, EtapaFluxo etapaFluxo, Colaborador colaborador, String chave) {

		// salva CentroFinanceiroSituacao
		contratoService.adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class),
			getMapper().map(centroFinanceiro, CentroFinanceiroTO.class),
			CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO.toString());

		// update CentroFinanceiro
		updateCentroFinanceiroRetornoAssinatura(centroFinanceiro, etapaFluxo);

		MotivoAplicacaoTO motivoAplicacaoTO = motivoAplicacaoService.findMotivoAplicacaoByChave(chave);
		MotivoAplicacao motivoAplicacao = getMapper().map(motivoAplicacaoTO, MotivoAplicacao.class);

		MotivoRetornoDocumento novoMotivoRetornoDocumento = new MotivoRetornoDocumento();
		novoMotivoRetornoDocumento.setAplicacao(motivoAplicacao);
		novoMotivoRetornoDocumento.setPessoa(colaborador);
		novoMotivoRetornoDocumento.setObservacao(motivoRetornoDocumento.getObservacao());
		novoMotivoRetornoDocumento.setDataCriacao(new Date());
		novoMotivoRetornoDocumento.setCriador(securityService.getUsername());

		// salva MotivoRetornoDocumento
		MotivoRetornoDocumento motivoRetornoDocumentoSalvo = motivoRetornoDocumentoRepository.save(novoMotivoRetornoDocumento);

		MotivoRetornoCentroFinanceiro motivoRetornoCentroFinanceiro = new MotivoRetornoCentroFinanceiro();
		motivoRetornoCentroFinanceiro.setMotivoRetornoDocumento(motivoRetornoDocumentoSalvo);
		motivoRetornoCentroFinanceiro.setCentroFinanceiro(centroFinanceiro);

		// salva MotivoRetornoCentroFinanceiro
		motivoRetornoCentroFinanceiroRepository.save(motivoRetornoCentroFinanceiro);

		// salva MotivoDocumento, se tiver itens na lista
		if (motivoRetornoDocumento.getListaMotivoRetorno() != null && motivoRetornoDocumento.getListaMotivoRetorno().size() > 0) {

			for (MotivoDocumento motivoDocumento : motivoRetornoDocumento.getListaMotivoRetorno()) {

				if (motivoDocumento.getMotivoRetorno().getCodigo() != null) {
					MotivoDocumento novoMotivoDocumento = new MotivoDocumento();
					novoMotivoDocumento.setMotivoRetornoDocumento(motivoRetornoDocumentoSalvo);
					novoMotivoDocumento.setMotivoRetorno(motivoDocumento.getMotivoRetorno());
					motivoDocumentoRepository.save(novoMotivoDocumento);
				}
			}
		}
	}

	/**
	 * Método para retorar assinatura do centro financeiro. Mas sem o motivo de retorno.
	 * Comercial > Centros Financeiros > Exibição (ABA ASSINATURAS)
	 *
	 * @param retornoAssinaturaTO
	 */
	@Override
	public void retornarAssinaturaSemMotivoRetorno(RetornoAssinaturaTO retornoAssinaturaTO) {
		Long codEtapaFluxo = retornoAssinaturaTO.getCodEtapaFluxo();
		EtapaFluxoTO etapaFluxoTO = etapaFluxoService.obterEtapaFLuxoComMotivoAplicacao(codEtapaFluxo);

		if (etapaFluxoTO.getStatusAnterior() != null) {
			Long codCentroFinanceiro = retornoAssinaturaTO.getCodCentroFinanceiro();
			Long codColaborador = securityService.getUser().getPessoaFisica().getCodigo();

			EtapaFluxo etapaFluxo = getMapper().map(etapaFluxoTO, EtapaFluxo.class);
			CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(codCentroFinanceiro).orElse(null);

			ColaboradorTO colaboradorTO = new ColaboradorTO();
			colaboradorTO.setCodigo(codColaborador);

			// remove assinaturas(CentroFinanceiroFluxoEtapa)
			limparSituacoes(etapaFluxo, codCentroFinanceiro);

			// salva CentroFinanceiroSituacao
			contratoService.adicionarCentroFinanceiroSituacao(colaboradorTO, getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO.toString());

			// update contrato
			contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());

			// update centro financeiro
			updateCentroFinanceiroRetornoAssinatura(centroFinanceiro, etapaFluxo);
		}
	}

	/**
	 * Atualiza centro financeiro, para os casos de retorno de assinatura.
	 *
	 * @param centroFinanceiro
	 */
	private void updateCentroFinanceiroRetornoAssinatura(CentroFinanceiro centroFinanceiro, EtapaFluxo etapaFluxo) {
		if (etapaFluxo.getStatusAnterior() != null) {
			centroFinanceiro.setSituacao(GlobalBean.getSituacaoCF(etapaFluxo.getStatusAnterior()));
		} else {
			centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO);
		}

		centroFinanceiroRepository.save(centroFinanceiro);
	}
}
