package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.comercial.domain.Fecho;
import br.com.araujoabreu.sgi.comercial.domain.FechoDespesaGeral;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.OrdemCompra;
import br.com.araujoabreu.sgi.compras.domain.RateioItemRecebimento;
import br.com.araujoabreu.sgi.compras.domain.RateioNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.RelacaoNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.RequisicaoCompra;
import br.com.araujoabreu.sgi.contasApagar.domain.RateioNotaPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.fundoFixo.domain.Transacao;
import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroAliquotaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroEnderecoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroSituacaoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.*;
import br.com.araujoabreu.sgi.service.comercial.transfer.CentroFinanceiroFechoTO;
import br.com.araujoabreu.sgi.service.compras.ParcelaNotaService;
import br.com.araujoabreu.sgi.service.contrato.ContratoEnderecoService;
import br.com.araujoabreu.sgi.service.contrato.ContratoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.service.gerencia.GerenciaService;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer.FiltroGraficoCLTO;
import br.com.araujoabreu.sgi.service.regional.RegionalGerenciaService;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroAliquotaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroEnderecoRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroFluxoEtapaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroSituacaoRepository;
import br.com.fasolti.sgi.repository.colaboradorCentroFinanceiro.ColaboradorCentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import br.com.fasolti.sgi.repository.diretoria.DiretoriaRepository;
import br.com.fasolti.sgi.repository.etapafluxo.EtapaFluxoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.fecho.FechoRepository;
import br.com.fasolti.sgi.repository.fundofixo.FundoFixoRepository;
import br.com.fasolti.sgi.repository.gerencia.GerenciaRepository;
import br.com.fasolti.sgi.repository.lote.LoteInativacaoRegistroRepository;
import br.com.fasolti.sgi.repository.mensagem.MensagemGerenciaRepository;
import br.com.fasolti.sgi.repository.mensagem.MensagemUsuarioRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoCentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.nota.NotaRepository;
import br.com.fasolti.sgi.repository.ordemcompra.OrdemCompraRepository;
import br.com.fasolti.sgi.repository.rateio.RateioItemRecebimentoRepository;
import br.com.fasolti.sgi.repository.rateio.RateioNotaFiscalRepository;
import br.com.fasolti.sgi.repository.rateio.RateioNotaPagamentoRepository;
import br.com.fasolti.sgi.repository.relacaonotafiscal.RelacaoNotaFiscalRepository;
import br.com.fasolti.sgi.repository.requisicaocompra.RequisicaoCompraRepository;
import br.com.fasolti.sgi.repository.transacao.TransacaoRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CentroFinanceiroServiceImpl extends BaseSearchService<CentroFinanceiro, CentroFinanceiroTO, CentroFinanceiroPesquisaTO, CentroFinanceiroRepository> implements CentroFinanceiroService {

	private static final QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
	private static final QGerencia qGerencia = QGerencia.gerencia;
	private static final Log logger = LogFactory.getLog(CentroFinanceiroServiceImpl.class);
	private static final Map<String, CentroFinanceiro.SituacaoCentroFinanceiro> MAPA_SITUACAO_CF = criarMapaSituacaoCf();
	public static final String ZERO = "0";
	public static final long ZERO_LONG = 0L;

	@Autowired
	private FechoService fechoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private GerenciaRepository gerenciaRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private DiretoriaRepository diretoriaRepository;

	@Autowired
	private ColaboradorCentroFinanceiroRepository colaboradorCentroFinanceiroRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private CentroFinanceiroSituacaoRepository centroFinanceiroSituacaoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private CentroFinanceiroEnderecoRepository centroFinanceiroEnderecoRepository;

	@Autowired
	private CentroFinanceiroFluxoEtapaRepository centroFinanceiroFluxoEtapaRepository;

	@Autowired
	private CentroFinanceiroAliquotaRepository centroFinanceiroAliquotaRepository;

	@Autowired
	private FechoRepository fechoRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private MotivoRetornoCentroFinanceiroRepository motivoRetornoCentroFinanceiroRepository;

	@Autowired
	private MensagemGerenciaRepository mensagemGerenciaRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ParcelaNotaService parcelaNotaService;

	@Autowired
	private FundoFixoRepository fundoFixoRepository;

	@Autowired
	private LoteInativacaoRegistroRepository loteInativacaoRegistroRepository;

	@Autowired
	private NotaRepository notaRepository;

	@Autowired
	private OrdemCompraRepository ordemCompraRepository;

	@Autowired
	private MensagemUsuarioRepository mensagemUsuarioRepository;

	@Autowired
	private RateioItemRecebimentoRepository rateioItemRecebimentoRepository;

	@Autowired
	private RateioNotaFiscalRepository rateioNotaFiscalRepository;

	@Autowired
	private RateioNotaPagamentoRepository rateioNotaPagamentoRepository;

	@Autowired
	private RelacaoNotaFiscalRepository relacaoNotaFiscalRepository;

	@Autowired
	private RequisicaoCompraRepository requisicaoCompraRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private RegionalGerenciaService regionalGerenciaService;

	@Autowired
	private EtapaFluxoRepository etapaFluxoRepository;

	@Autowired
	private AliquotaRepository aliquotaRepository;

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private CentroFinanceiroEnderecoService centroFinanceiroEnderecoService;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	private ContratoEnderecoService contratoEnderecoService;

	@Autowired
	private CentroFinanceiroSituacaoService centroFinanceiroSituacaoService;

	@Autowired
	private GerenciaService gerenciaService;

	@Autowired
	private CentroFinanceiroAliquotaService centroFinanceiroAliquotaService;

	@Autowired
	public CentroFinanceiroServiceImpl(CentroFinanceiroRepository repository) {
		super(repository);
	}

	private static Map<String, CentroFinanceiro.SituacaoCentroFinanceiro> criarMapaSituacaoCf() {
		Map<String, CentroFinanceiro.SituacaoCentroFinanceiro> mapa = new HashMap<>();
		for (CentroFinanceiro.SituacaoCentroFinanceiro situacao : CentroFinanceiro.SituacaoCentroFinanceiro.values()) {
			mapa.put(situacao.getNomeSituacao(), situacao);
		}
		return mapa;
	}

	@Override
	protected void buildSearchQuery(JPAQuery<CentroFinanceiro> jpaQuery, CentroFinanceiroPesquisaTO exampleObj) {
		QCentroFinanceiroEndereco qCentroFinanceiroEndereco = QCentroFinanceiroEndereco.centroFinanceiroEndereco;
		QEndereco qEndereco = QEndereco.endereco1;
		QCidade qCidade = QCidade.cidade;
		QContrato qContrato = QContrato.contrato;

		jpaQuery
			.leftJoin(qCentroFinanceiro.listaEnderecos, qCentroFinanceiroEndereco)
			.leftJoin(qCentroFinanceiroEndereco.endereco, qEndereco)
			.leftJoin(qEndereco.cidade, qCidade)
			.leftJoin(qCentroFinanceiro.contrato, qContrato);

		jpaQuery.orderBy(qCentroFinanceiro.identificador.asc()).distinct();

		jpaQuery.select(Projections.fields(
			CentroFinanceiro.class,
			qCentroFinanceiro.codigo,
			qCentroFinanceiro.identificador,
			qCentroFinanceiro.nome,
			qCentroFinanceiro.cumulativo,
			qCentroFinanceiro.situacao,
			Projections.bean(Contrato.class, qContrato.codigo).as("contrato"),
			Projections.bean(CentroFinanceiro.class, qCentroFinanceiro.pai.codigo, qCentroFinanceiro.pai.nome).as("pai")
		));
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CentroFinanceiroPesquisaTO exampleObj) {
		builder.and(qCentroFinanceiro.tipo.eq(CentroFinanceiro.TipoCentroFinanceiro.CENTRO_LUCRO));

		if (Check.isAssigned(exampleObj.getGerencia()) && Check.isAssigned(exampleObj.getGerencia().getCodigo())) {
			builder.and(qCentroFinanceiro.pai.codigo.eq(exampleObj.getGerencia().getCodigo()));
		} else {
			List<GerenciaTO> listaGerenciaTO = buscarGerencias();
			List<Long> ids = new ArrayList<>();

			for (GerenciaTO gerencia : listaGerenciaTO) {
				ids.add(gerencia.getCodigo());
			}

			builder.and(qCentroFinanceiro.pai.codigo.in(ids).or(qCentroFinanceiro.codigo.in(ids)));
		}

		if (Check.isAssigned(exampleObj.getPermiteClienteLivre())) {
			builder.and(qCentroFinanceiro.permiteClienteLivre.eq(exampleObj.getPermiteClienteLivre()));
		}

		if (Check.isAssigned(exampleObj.getUnidade()) && Check.isAssigned(exampleObj.getUnidade().getCodigo())) {
			builder.and(qCentroFinanceiro.unidade.codigo.eq(exampleObj.getUnidade().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			builder.and(qCentroFinanceiro.contrato.codigo.eq(exampleObj.getCodigoContrato()));
		}

		if (Check.isAssigned(exampleObj.getIdentificador())) {
			builder.and(qCentroFinanceiro.identificador.lower().contains(exampleObj.getIdentificador().toLowerCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(qCentroFinanceiro.nome.upper().contains(exampleObj.getNome().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qCentroFinanceiro.situacao.eq(MAPA_SITUACAO_CF.get(exampleObj.getSituacao())));
		}

		if (Check.isAssigned(exampleObj.getCumulativo())) {
			builder.and(qCentroFinanceiro.cumulativo.eq(exampleObj.getCumulativo()));
		}

		if (Check.isAssigned(exampleObj.getUF())) {
			QEndereco qEndereco = QEndereco.endereco1;
			builder.and(qEndereco.uf.upper().eq(exampleObj.getUF().toUpperCase()));
		}

		if (Check.isAssigned(exampleObj.getCidade())) {
			QCidade qCidade = QCidade.cidade;
			builder.and(qCidade.codigo.eq(exampleObj.getCidade().getCodigo()));
		}
	}

	@Override
	public CentroFinanceiroTO getGerenciaLogada() {
		Pessoa pessoa = securityService.getUser().getPessoaFisica();

		if (pessoa != null && pessoa instanceof Colaborador) {
			CentroFinanceiro centroFinanceiro = ((Colaborador) pessoa).getCentroFinanceiro();
			return map(centroFinanceiro);
		}

		return null;
	}

	@Override
	public List<CentroFinanceiroTO> getCFsByUnidade(CentroFinanceiroPesquisaTO exampleObj) {
		try {
			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();
			List<CentroFinanceiroTO> listaTO = new ArrayList<>();

			if (exampleObj.getUnidade() != null) {
				listaCentroFinanceiros = getRepository().findCentrosFinanceirosByUnidade(exampleObj.getUnidade().getCodigo());
				for (CentroFinanceiro entidade : listaCentroFinanceiros) {
					listaTO.add(getMapper().map(entidade, CentroFinanceiroTO.class));
				}
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<CentroFinanceiroTO> getCFsByUnidadeTelaRelatorio(CentroFinanceiroPesquisaTO exampleObj) {
		try {
			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();

			if (exampleObj.getUnidade() != null) {
				QBean qBean = Projections.bean(
					CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.nome,
					qCentroFinanceiro.identificadorFinal,
					Projections.bean(Unidade.class, qCentroFinanceiro.unidade.codigo).as("unidade")
				);

				JPAQuery<CentroFinanceiro> jpaQuery = select();

				jpaQuery
					.select(qBean)
					.where(qCentroFinanceiro.unidade.codigo.eq(exampleObj.getUnidade().getCodigo()));

				listaCentroFinanceiros = jpaQuery.fetch();
			}

			return map(listaCentroFinanceiros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JPAQuery getJpaQuery() {
		return select();
	}

	@Override
	public List<GerenciaTO> buscarGerenciasPorUnidade(Long codigoUnidade) {
		return gerenciaService.buscarGerenciasPorUnidadeTelaRelatorio(codigoUnidade);
	}

	/**
	 * Busca uma lista de gerencias para o campo de Gêrencia na tela de pesquisa do Centro Financeiro.
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GerenciaTO> buscarGerenciasPesquisa() {
		try {
			return buscarGerencias();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Busca uma lista de gerencias para o campo de Gêrencia na tela de novo/edição do Centro Financeiro.
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GerenciaTO> buscarGerenciasEdicao(CentroFinanceiroTO centroFinanceiroTO) {
		try {
			List<Gerencia> listaGerencia = buscarGerenciasParaEdicao(centroFinanceiroTO.getCodigo());
			List<GerenciaTO> listaTO = new ArrayList<>();

			for (Gerencia gerencia : listaGerencia) {
				GerenciaTO to = getMapper().map(gerencia, GerenciaTO.class);
				listaTO.add(to);
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<GerenciaTO> buscarGerencias() {
		List<GerenciaTO> listaGerenciaTO = new ArrayList<>();
		List<Gerencia> listaGerencia = null;
		Long codigoColaborador = securityService.getUser().getPessoaFisica().getCodigo();
		Set<Diretoria> diretoriasDoUsuario = findDiretoriasByColaborador(codigoColaborador);
		Set<Diretoria> diretoriasQueAceitamContrato = new HashSet<>();

		if (diretoriasDoUsuario != null && !diretoriasDoUsuario.isEmpty()) {
			for (Diretoria diretoria : diretoriasDoUsuario) {
				if (diretoria.getPermiteContrato()) {
					diretoriasQueAceitamContrato.add(diretoria);
				}
			}
		}

		List<Perfil> listaPerfil = securityService.getUser().getPerfis();
		Set<String> conjPerfil = new HashSet<>();
		for (Perfil perfil : listaPerfil) {
			conjPerfil.add(perfil.getNome());
		}

		if (conjPerfil.contains("admin-contrato")) {
			if (diretoriasQueAceitamContrato.size() == 1) {
				listaGerencia = ordenarGerencias(getListaGerenciasPermitemContrato(diretoriasQueAceitamContrato));
			} else {
				listaGerencia = ordenarGerencias(getGerenciasPermitemContrato());
			}

		} else if (conjPerfil.contains("aprov-contrato-ger")) {
			if (diretoriasQueAceitamContrato.size() > 0) {
				List<Gerencia> gerenciasTotais = ordenarGerencias(getListaGerenciasPermitemContrato(diretoriasQueAceitamContrato));
				listaGerencia = ordenarGerencias(filtrarGerenciasDoUsuario(gerenciasTotais, codigoColaborador));
			} else {
				listaGerencia = ordenarGerencias(getGerenciasPermitemContrato());
			}

		} else if (conjPerfil.contains("aprov-contrato-dir")) {
			listaGerencia = ordenarGerencias(getListaGerenciasPermitemContrato(diretoriasQueAceitamContrato));

		} else if (conjPerfil.contains("aprov-contrato-rh") || conjPerfil.contains("aprov-contrato-cont") || conjPerfil.contains("aprov-contrato-fin") ||
			conjPerfil.contains("aprov-contrato-com") || conjPerfil.contains("aprov-contrato-pres")) {
			listaGerencia = ordenarGerencias(getGerenciasPermitemContrato());

		} else {
			listaGerencia = new ArrayList<>();
		}

		if (listaGerencia != null && !listaGerencia.isEmpty()) {
			for (Gerencia gerencia : listaGerencia) {
				GerenciaTO to = getMapper().map(gerencia, GerenciaTO.class);
				listaGerenciaTO.add(to);
			}
		}

		return listaGerenciaTO;
	}

	/**
	 * Busca uma lista de gerencias para a tela de edição do centro financeiro.
	 * O parametro codCentroFinanceiro pode ser nulo. Se não for é verificar associação de contrato do centro financeiro.
	 *
	 * @param codCentroFinanceiro
	 * @return
	 */
	private List<Gerencia> buscarGerenciasParaEdicao(Long codCentroFinanceiro) {

		if (codCentroFinanceiro != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(codCentroFinanceiro).orElse(null);

			//Centro financeiro associado a um contrato
			if (centroFinanceiro != null && centroFinanceiro.getContrato() != null && centroFinanceiro.getContrato().getCodigo() != null) {
				List<Long> listaCodigoDiretoria = new ArrayList<>();
				listaCodigoDiretoria.add(centroFinanceiro.getContrato().getDiretoria().getCodigo());

				List<Gerencia> gerenciasDaDiretoria = buscarGerenciasPorCodigoDiretoria(listaCodigoDiretoria);
				return ordenarGerencias(gerenciasDaDiretoria);

			} else {
				return gerenciasParaEdicao();
			}

		} else {
			return gerenciasParaEdicao();
		}
	}

	/**
	 * Busca uma lista de gerencias para a tela de edição do centro financeiro.
	 *
	 * @return
	 */
	private List<Gerencia> gerenciasParaEdicao() {

		List<Gerencia> listaGerencia = null;

		Long codigoColaborador = securityService.getUser().getPessoaFisica().getCodigo();
		Set<Diretoria> diretoriasDoUsuario = findDiretoriasByColaborador(codigoColaborador);
		Set<Diretoria> diretoriasQueAceitamContrato = new HashSet<>();

		if (diretoriasDoUsuario != null && !diretoriasDoUsuario.isEmpty()) {
			for (Diretoria diretoria : diretoriasDoUsuario) {
				if (diretoria.getPermiteContrato()) {
					diretoriasQueAceitamContrato.add(diretoria);
				}
			}
		}

		List<Perfil> listaPerfil = securityService.getUser().getPerfis();
		Set<String> conjPerfil = new HashSet<>();
		for (Perfil perfil : listaPerfil) {
			conjPerfil.add(perfil.getNome());
		}

		if (conjPerfil.contains("admin-contrato")) {
			if (diretoriasQueAceitamContrato.size() == 1) {
				listaGerencia = ordenarGerencias(getListaGerenciasPermitemContrato(diretoriasQueAceitamContrato));
			} else {
				listaGerencia = ordenarGerencias(getGerenciasPermitemContrato());
			}

		} else {
			if (diretoriasQueAceitamContrato.size() > 0) {
				listaGerencia = ordenarGerencias(getListaGerenciasPermitemContrato(diretoriasQueAceitamContrato));
				listaGerencia = ordenarGerencias(filtrarGerenciasDoUsuario(listaGerencia, codigoColaborador));
			} else {
				listaGerencia = ordenarGerencias(getGerenciasPermitemContrato());
			}
		}

		return listaGerencia;
	}

	private Set<Diretoria> findDiretoriasByColaborador(Long codigoColaborador) {

		Set<Diretoria> diretorias = new HashSet<>();
		List<ColaboradorCentroFinanceiro> ccfs = buscarColaboradorCentroFinanceiroPorCodColaborador(codigoColaborador);
		if (ccfs != null && !ccfs.isEmpty()) {

			JPAQuery jpaQueryGerencia = null;
			QDiretoria qDiretoria = QDiretoria.diretoria;
			QBean qBeanGerencia = Projections.bean(Gerencia.class, qGerencia.codigo, qGerencia.nome,
				Projections.bean(Diretoria.class, qDiretoria.codigo, qDiretoria.permiteContrato).as("diretoria"));

			List<Long> listaCodigoGerencia = new ArrayList<>();

			for (ColaboradorCentroFinanceiro ccf : ccfs) {

				CentroFinanceiro cfGerencia = null;

				if (ccf.getCentroFinanceiro().getPai() != null && ccf.getCentroFinanceiro().getPai().getCodigo() != null) {
					cfGerencia = ccf.getCentroFinanceiro().getGerencia();
				} else {
					cfGerencia = ccf.getCentroFinanceiro();
				}

				if (cfGerencia != null && cfGerencia.getCodigo() != null && !listaCodigoGerencia.contains(cfGerencia.getCodigo())) {
					listaCodigoGerencia.add(cfGerencia.getCodigo());
				}
			}

			if (listaCodigoGerencia != null && !listaCodigoGerencia.isEmpty()) {
				jpaQueryGerencia = select();
				jpaQueryGerencia.select(qBeanGerencia).from(qGerencia).join(qGerencia.diretoria, qDiretoria).where(qGerencia.codigo.in(listaCodigoGerencia));
				List<Gerencia> listaGerencia = jpaQueryGerencia.fetch();

				if (listaGerencia != null && !listaGerencia.isEmpty()) {
					for (Gerencia g : listaGerencia) {
						diretorias.add(g.getDiretoria());
					}
				}
			}
		}
		return diretorias;
	}

	private List<Gerencia> ordenarGerencias(List<Gerencia> gerencias) {
		if (gerencias != null && !gerencias.isEmpty()) {
			Comparator<Gerencia> comparator = (o1, o2) -> o1.getNome().compareToIgnoreCase(o2.getNome());

			gerencias.sort(comparator);
		}
		return gerencias;
	}

	private List<Gerencia> getGerenciasPermitemContrato() {
		Set<Diretoria> conjDiretoria = new HashSet<>(buscarDiretoriasPorPermiteContrato(Boolean.TRUE));

		if (conjDiretoria != null && !conjDiretoria.isEmpty()) {
			return getListaGerenciasPermitemContrato(conjDiretoria);
		} else {
			return new ArrayList<>();
		}
	}

	private List<Gerencia> filtrarGerenciasDoUsuario(List<Gerencia> gerenciasTotais, Long codigoColaborador) {
		List<ColaboradorCentroFinanceiro> ccfs = buscarColaboradorCentroFinanceiroPorCodColaborador(codigoColaborador);
		List<Long> gerenciasDoUsuario = new ArrayList<>();
		for (ColaboradorCentroFinanceiro ccf : ccfs) {
			CentroFinanceiro cf = null;

			if (ccf.getCentroFinanceiro().getPai() != null && ccf.getCentroFinanceiro().getPai().getCodigo() != null) {
				cf = ccf.getCentroFinanceiro().getGerencia();
			} else {
				cf = ccf.getCentroFinanceiro();
			}

			gerenciasDoUsuario.add(cf.getCodigo());
		}

		List<Gerencia> gerenciasComAcesso = new ArrayList<>();
		for (Gerencia gerencia : gerenciasTotais) {
			if (gerenciasDoUsuario.contains(gerencia.getCodigo())) {
				gerenciasComAcesso.add(gerencia);
			}
		}

		return gerenciasComAcesso;
	}

	private List<Gerencia> getListaGerenciasPermitemContrato(Set<Diretoria> diretorias) {
		Set<Gerencia> gerencias = new HashSet<>();
		List<Long> listaCodigoDiretoria = new ArrayList<>();

		for (Diretoria diretoria : diretorias) {
			listaCodigoDiretoria.add(diretoria.getCodigo());
		}

		if (listaCodigoDiretoria != null && !listaCodigoDiretoria.isEmpty()) {
			List<Gerencia> listaGerencia = buscarGerenciasPorCodigoDiretoria(listaCodigoDiretoria);
			gerencias.addAll(listaGerencia);
		}

		return new ArrayList<>(gerencias);
	}

	@Override
	public List<GerenciaTO> buscarGerenciasPorDiretoria(DiretoriaSimpleTO diretoria) {
		return obterGerenciasColaboradorPorPerfilContratos(diretoria);
	}

	public List<GerenciaTO> obterGerenciasColaboradorPorPerfilContratos(DiretoriaSimpleTO diretoriaSimpleTO) {

		if (Check.isAssigned(diretoriaSimpleTO.getCodigo())) {

			Diretoria diretoria = diretoriaRepository.findById(diretoriaSimpleTO.getCodigo()).orElse(null);
			List<GerenciaTO> gerenciasDiretoria = gerenciaService.findByCodigoDiretoria(diretoria.getCodigo());

			List<Perfil> listaPerfil = securityService.getUser().getPerfis();
			Set<String> conjPerfil = new HashSet<>();

			for (Perfil perfil : listaPerfil) {
				conjPerfil.add(perfil.getNome());
			}

			if (conjPerfil.contains("faturamento-cns-gerencia")) {
				return gerenciaService.buscarTodasGerenciasHabilitadasFaturamento();
			}
			if (conjPerfil.contains("admin-contrato")
				|| conjPerfil.contains("aprov-contrato-dir")
				|| conjPerfil.contains("aprov-contrato-rh")
				|| conjPerfil.contains("aprov-contrato-fin")
				|| conjPerfil.contains("aprov-contrato-cont")
				|| conjPerfil.contains("aprov-contrato-com")
				|| conjPerfil.contains("aprov-contrato-pres")) {

				return gerenciasDiretoria;
			}

			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);
			List<Gerencia> gerenciasUsuario = new ArrayList<>();
			List<ColaboradorCentroFinanceiro> colaboradorCentroFinanceiro = colaboradorCentroFinanceiroRepository.findByColaborador(colaborador);

			if (conjPerfil.contains("aprov-contrato-ger")) {
				for (GerenciaTO gerencia : gerenciasDiretoria) {
					for (ColaboradorCentroFinanceiro gerenciaUser : colaboradorCentroFinanceiro) {
						if (gerenciaUser.getCentroFinanceiro() instanceof Gerencia) {
							if (gerencia.getCodigo().equals(gerenciaUser.getCentroFinanceiro().getCodigo())) {
								gerenciasUsuario.add(getMapper().map(gerencia, Gerencia.class));
								break;
							}
						}
					}
				}
			}

			return gerenciasDiretoria;
		}
		return null;
	}

	@Override
	public List<CentroFinanceiroTO> buscarGerenciasPorContratos(String idGerencia) {
		try {
			JPAQuery<CentroFinanceiro> query = select();
			query.from(qCentroFinanceiro);
			query.where(new BooleanBuilder().and(qCentroFinanceiro.pai.codigo.eq(Long.parseLong(idGerencia))));
			query.select(Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome
			));
			query.orderBy(qCentroFinanceiro.nome.asc());

			List<CentroFinanceiro> listaCentros = query.fetch();

			List<CentroFinanceiroTO> listaTO = new ArrayList<>();

			for (CentroFinanceiro centroFinanceiro : listaCentros) {
				listaTO.add(getMapper().map(centroFinanceiro, CentroFinanceiroTO.class));
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doAfterLoad(CentroFinanceiroTO transferObj) {

		// BUSCAR NOME DO CRIADOR
		if (transferObj != null && transferObj.getCriador() != null) {
			transferObj.setNomeCriador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getCriador()));
		}

		// BUSCAR NOME DO ATUALIZADOR
		if (transferObj != null && transferObj.getAtualizador() != null) {
			transferObj.setNomeAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getAtualizador()));
		}

		//BUSCAR NOMEs E DATAs DO ATIVADOR E DO INATIVADOR

		List<CentroFinanceiroSituacaoTO> listaSituacaoTO = centroFinanceiroSituacaoService.obterCentroFinanceiroSituacao(transferObj.getCodigo());

		List<CentroFinanceiroSituacao> listaSituacao = new ArrayList<>();

		for (CentroFinanceiroSituacaoTO centroFinanceiroSituacaoTO : listaSituacaoTO) {
			listaSituacao.add(getMapper().map(centroFinanceiroSituacaoTO, CentroFinanceiroSituacao.class));
		}

		CentroFinanceiroSituacao situacaoAtivo = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO, listaSituacao);
		CentroFinanceiroSituacao situacaoInativo = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO, listaSituacao);

		transferObj.setNomeAtivacao(situacaoAtivo.getColaborador().getNome());
		transferObj.setDataAtivacao(situacaoAtivo.getData());
		transferObj.setNomeInativacao(situacaoInativo.getColaborador().getNome());
		transferObj.setDataInativacao(situacaoInativo.getData());

		//Quando Centro financeiro for associado a um contrato.
		if (transferObj.getContratoTO() != null && transferObj.getContratoTO().getCodigo() != null) {
			CentroFinanceiroSituacao situacaoEncerramento = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO, listaSituacao);
			CentroFinanceiroSituacao situacaoParalisacao = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO, listaSituacao);
			CentroFinanceiroSituacao situacaoReativacao = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.REATIVADO, listaSituacao);
			CentroFinanceiroSituacao situacaoCancelamento = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO, listaSituacao);
			CentroFinanceiroSituacao situacaoEncerramentoComFat = getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO, listaSituacao);

			transferObj.setNomeEncerramento(situacaoEncerramento.getColaborador().getNome());
			transferObj.setDataEncerramento(situacaoEncerramento.getData());
			transferObj.setNomeParalisacao(situacaoParalisacao.getColaborador().getNome());
			transferObj.setDataParalisacao(situacaoParalisacao.getData());
			transferObj.setNomeReativacao(situacaoReativacao.getColaborador().getNome());
			transferObj.setDataReativacao(situacaoReativacao.getData());
			transferObj.setNomeCancelamento(situacaoCancelamento.getColaborador().getNome());
			transferObj.setDataCancelamento(situacaoCancelamento.getData());
			transferObj.setNomeEncerramentoComFat(situacaoEncerramentoComFat.getColaborador().getNome());
			transferObj.setDataEncerramentoComFat(situacaoEncerramentoComFat.getData());
		}
	}

	private CentroFinanceiroSituacao getSituacao(CentroFinanceiro.SituacaoCentroFinanceiro situacao, List<CentroFinanceiroSituacao> listaSituacao) {
		if (situacao != null && listaSituacao != null && !listaSituacao.isEmpty()) {
			for (CentroFinanceiroSituacao centroFinanceiroSituacao : listaSituacao) {
				if (centroFinanceiroSituacao.getSituacao().equals(situacao)) {
					return centroFinanceiroSituacao;
				}
			}
		}

		CentroFinanceiroSituacao cfs = new CentroFinanceiroSituacao();
		cfs.setColaborador(new Colaborador());
		return cfs;
	}

	/**
	 * Função de busca de Centros Financeiros, para ser usado em telas que NÃO sejam de pesquisa.
	 *
	 * @param exampleObj
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CentroFinanceiroTO> buscarCFs(CentroFinanceiroTO exampleObj) {
		try {
			QContrato qContrato = QContrato.contrato;
			QUnidade qUnidade = QUnidade.unidade;
			QUnidade qUnidadeMatriz = QUnidade.unidade;
			QPessoa qPessoa = QPessoa.pessoa;

			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.situacao,
				qCentroFinanceiro.permiteClienteLivre,
				Projections.bean(Contrato.class, qContrato.codigo).as("contrato"),
				Projections.bean(Unidade.class, qUnidade.codigo, qUnidade.tipoUnidade,
					Projections.bean(Unidade.class, qUnidadeMatriz.codigo).as("matriz")
				).as("unidade")
			);

			JPAQuery<CentroFinanceiro> jpaQueryCentroFinanceiro = select();
			BooleanBuilder builder = restricaoParaBuscaCF(exampleObj);
			builder.and(qCentroFinanceiro.pai.isNotNull());

			jpaQueryCentroFinanceiro
				.select(qBean)
				.from(qCentroFinanceiro)
				.join(qCentroFinanceiro.unidade, qUnidade)
				.leftJoin(qCentroFinanceiro.contrato, qContrato)
				.leftJoin(qUnidade.matriz, qUnidadeMatriz)
				.leftJoin(qContrato.cliente, qPessoa)
				.where(builder)
				.orderBy(qCentroFinanceiro.codigo.asc());

			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();
			List<CentroFinanceiroTO> listaTO = new ArrayList<>();

			listaCentroFinanceiros = jpaQueryCentroFinanceiro.fetch();

			for (CentroFinanceiro cf : listaCentroFinanceiros) {
				CentroFinanceiroTO to = getMapper().map(cf, CentroFinanceiroTO.class);
				listaTO.add(to);
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Método auxiliar para função 'buscarCFs'.
	 *
	 * @param to
	 * @return
	 */
	private BooleanBuilder restricaoParaBuscaCF(CentroFinanceiroTO to) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(to.getSituacao())) {
			builder.and(qCentroFinanceiro.situacao.eq(MAPA_SITUACAO_CF.get(to.getSituacao())));
		}

		if (Check.isAssigned(to.getPermiteClienteLivre())) {
			builder.and(qCentroFinanceiro.permiteClienteLivre.eq(to.getPermiteClienteLivre()));
		}

		if (Check.isAssigned(to.getNome()) && Check.isAssigned(to.getIdentificadorFinal())) {
			builder.and(qCentroFinanceiro.nome.upper().contains(to.getNome().toUpperCase().trim())
				.or(qCentroFinanceiro.identificadorFinal.upper().contains(to.getIdentificadorFinal().toUpperCase().trim())));

		} else if (Check.isAssigned(to.getNome())) {
			builder.and(qCentroFinanceiro.nome.upper().contains(to.getNome().toUpperCase().trim()));

		} else if (Check.isAssigned(to.getIdentificadorFinal())) {
			builder.and(qCentroFinanceiro.identificadorFinal.upper().contains(to.getIdentificadorFinal().toUpperCase().trim()));
		}

		if (Check.isAssigned(to.getContratoTO()) && Check.isAssigned(to.getContratoTO().getCliente())
			&& Check.isAssigned(to.getContratoTO().getCliente().getCodigo())) {
			QPessoa qPessoa = QPessoa.pessoa;
			builder.and(qPessoa.codigo.eq(to.getContratoTO().getCliente().getCodigo()));
		}

		if (Check.isAssigned(to.getUnidade()) && Check.isAssigned(to.getUnidade().getCodigo())) {
			builder.and(qCentroFinanceiro.unidade.codigo.eq(to.getUnidade().getCodigo()));
		}

		if(Check.isAssigned(to.getContratoTO())	&& Check.isAssigned(to.getContratoTO().getCodigo())){
			QContrato qContrato = QContrato.contrato;
			builder.and(qContrato.codigo.eq(to.getContratoTO().getCodigo()));
		}

		return builder;
	}

	private List<Gerencia> buscarGerenciasPorCodigoDiretoria(List<Long> listaCodigoDiretoria) {
		List<Gerencia> listaGerencia = new ArrayList<>();
		QDiretoria qDiretoria = QDiretoria.diretoria;

		QBean qBeanGerencia = Projections.bean(Gerencia.class, qGerencia.codigo, qGerencia.nome, qGerencia.numero,
			Projections.bean(Diretoria.class, qDiretoria.codigo, qDiretoria.permiteContrato, qDiretoria.numero).as("diretoria"));

		JPAQuery jpaQueryGerencia = select();
		jpaQueryGerencia.select(qBeanGerencia).from(qGerencia).join(qGerencia.diretoria, qDiretoria).where(qDiretoria.codigo.in(listaCodigoDiretoria));
		listaGerencia = jpaQueryGerencia.fetch();

		return listaGerencia;
	}

	private List<Diretoria> buscarDiretoriasPorPermiteContrato(Boolean permiteContrato) {
		List<Diretoria> diretorias = new ArrayList<>();
		QDiretoria qDiretoria = QDiretoria.diretoria;

		QBean qBean = Projections.bean(Diretoria.class, qDiretoria.codigo);

		JPAQuery jpaQuery = select();
		jpaQuery.select(qBean).from(qDiretoria).where(qDiretoria.permiteContrato.eq(permiteContrato));
		diretorias = jpaQuery.fetch();

		return diretorias;
	}

	private List<ColaboradorCentroFinanceiro> buscarColaboradorCentroFinanceiroPorCodColaborador(Long codigoColaborador) {
		List<ColaboradorCentroFinanceiro> ccfs = new ArrayList<>();

		QColaboradorCentroFinanceiro qColaboradorCentroFinanceiro = QColaboradorCentroFinanceiro.colaboradorCentroFinanceiro;
		QDiretoria qDiretoria = QDiretoria.diretoria;

		QBean qBean = Projections.bean(
			ColaboradorCentroFinanceiro.class,
			qColaboradorCentroFinanceiro.codigo,
			Projections.bean(CentroFinanceiro.class, qCentroFinanceiro.codigo,
				Projections.bean(CentroFinanceiro.class, qCentroFinanceiro.pai.codigo).as("pai")
			).as("centroFinanceiro")
		);

		JPAQuery jpaQuery = select();
		jpaQuery
			.select(qBean)
			.from(qColaboradorCentroFinanceiro).join(qColaboradorCentroFinanceiro.centroFinanceiro, qCentroFinanceiro)
			.where(qColaboradorCentroFinanceiro.colaborador.codigo.eq(codigoColaborador));

		ccfs = jpaQuery.fetch();

		return ccfs;
	}

	@Override
	public CentroFinanceiroTO loadViewById(String id) throws Exception {
		try {
			return buscarCentroFinanceiroById(id);
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public CentroFinanceiroTO buscarCentroFinanceiroById(String id) {
		CentroFinanceiroTO to = new CentroFinanceiroTO();

		if (id != null) {
			QContrato qContrato = QContrato.contrato;
			QColaborador qEngResponsavel = QColaborador.colaborador;
			QColaborador qContatoAA = QColaborador.colaborador;
			QUnidade qUnidade = QUnidade.unidade;

			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.situacao,
				qCentroFinanceiro.cumulativo,
				qCentroFinanceiro.formaPagamento,
				qCentroFinanceiro.garantias,
				qCentroFinanceiro.criterioMedicao,
				qCentroFinanceiro.dataPrevisaoReajuste,
				qCentroFinanceiro.totalHH,
				qCentroFinanceiro.observacao,
				qCentroFinanceiro.dataCriacao,
				qCentroFinanceiro.dataAtualizacao,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.criador,
				qCentroFinanceiro.atualizador,
				qCentroFinanceiro.proximoNumOC,
				qCentroFinanceiro.tipo,
				Projections.bean(Unidade.class, qUnidade.codigo, qUnidade.codnome).as("unidade"),
				Projections.bean(Contrato.class, qContrato.codigo, qContrato.prazoAtual, qContrato.proposta).as("contrato"),
				Projections.bean(CentroFinanceiro.class, qCentroFinanceiro.pai.codigo, qCentroFinanceiro.pai.nome).as("pai"),
				Projections.bean(Colaborador.class, qCentroFinanceiro.engenheiroResponsavel.codigo, qCentroFinanceiro.engenheiroResponsavel.nome).as("engenheiroResponsavel"),
				Projections.bean(Colaborador.class, qContatoAA.codigo, qContatoAA.nome).as("contatoAA")
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select(qBean);

			jpaQuery.join(qCentroFinanceiro.unidade, qUnidade)
				.leftJoin(qCentroFinanceiro.contrato, qContrato)
				.leftJoin(qCentroFinanceiro.engenheiroResponsavel, qEngResponsavel)
				.leftJoin(qCentroFinanceiro.contatoAA, qContatoAA)
				.where(qCentroFinanceiro.codigo.eq(Long.parseLong(id)));

			CentroFinanceiro centroFinanceiro = jpaQuery.fetchOne();

			to = map(centroFinanceiro);

			doAfterLoad(to);
		}

		return to;
	}

	@Override
	@Transactional
	public void inativarById(String id) throws Exception {
		try {
			inativarCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public void inativarCentroFinanceiroById(Long id) {
		if (id != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(id).orElse(null);

			if (CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO.equals(centroFinanceiro.getSituacao())) {
				centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO);
				adicionarCentroFinanceiroSituacao(centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO);

				if (ObjectUtil.isNotNull(centroFinanceiro.getContrato()) && ObjectUtil.isNotNull(centroFinanceiro.getContrato().getCodigo())) {
					contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());
				}

				getRepository().save(centroFinanceiro);
			}
		}
	}

	@Override
	@Transactional
	public void reativarById(String id) throws Exception {
		try {
			reativarCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public void reativarCentroFinanceiroById(Long id) {
		if (id != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(id).orElse(null);

			List<CentroFinanceiro.SituacaoCentroFinanceiro> situacoes = Arrays.asList(CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO, CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO,
				CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO, CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO);

			if(situacoes.stream().anyMatch(s -> s.equals(centroFinanceiro.getSituacao()))) {

				if (situacoes.stream().filter(s -> !CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO.equals(s)).anyMatch(s -> s.equals(centroFinanceiro.getSituacao()))) {
					adicionarCentroFinanceiroSituacao(centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.REATIVADO);

				} else if (CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO.equals(centroFinanceiro.getSituacao())) {
					adicionarCentroFinanceiroSituacao(centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);
				}

				if (ObjectUtil.isNotNull(centroFinanceiro.getContrato()) && ObjectUtil.isNotNull(centroFinanceiro.getContrato().getCodigo())) {
					contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());
				}

				centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO);
				centroFinanceiro.setDataAtualizacao(new Date());
				centroFinanceiro.setAtualizador(securityService.getUsername());

				getRepository().save(centroFinanceiro);
			} else {
				throw new ValidationException("ValidationException","Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.");
			}
		}
	}

	@Override
	@Transactional
	public void paralisarById(String id) throws Exception {
		try {
			paralisarCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public void paralisarCentroFinanceiroById(Long id) {
		if (id != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(id).orElse(null);
			centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO);
			adicionarCentroFinanceiroSituacao(centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO);

			if (ObjectUtil.isNotNull(centroFinanceiro.getContrato()) && ObjectUtil.isNotNull(centroFinanceiro.getContrato().getCodigo())) {
				contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());
			}

			getRepository().save(centroFinanceiro);
		}
	}

	@Override
	@Transactional
	public void encerrarById(String id) throws Exception {
		try {
			encerrarCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	@Transactional
	public void encerrarFatById(String id) throws Exception {
		try {
			encerrarFatCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public void encerrarCentroFinanceiroById(Long id) {
		encerrarCentroFinanceiroComOuSemFat(id, CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO);
	}

	@Override
	public void encerrarFatCentroFinanceiroById(Long id) {
		encerrarCentroFinanceiroComOuSemFat(id, CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO);
	}

	private void encerrarCentroFinanceiroComOuSemFat(Long id, CentroFinanceiro.SituacaoCentroFinanceiro situacaoCentroFinanceiro) {
		if (id != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(id).orElse(null);

			List<CentroFinanceiro.SituacaoCentroFinanceiro> ls = Arrays.asList(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO,
				CentroFinanceiro.SituacaoCentroFinanceiro.PARALISADO, CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO);

			if(ls.stream().anyMatch(s -> s.equals(centroFinanceiro.getSituacao()))) {

				boolean salvarDataEncerramento = true;

				// SGINV-2628
				if (CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO.equals(situacaoCentroFinanceiro)
					&& CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO_COM_FATURAMENTO.equals(centroFinanceiro.getSituacao())) {
					salvarDataEncerramento = false;
				}

				centroFinanceiro.setSituacao(situacaoCentroFinanceiro);
				adicionarCentroFinanceiroSituacao(centroFinanceiro, situacaoCentroFinanceiro);

				if (salvarDataEncerramento) {
					centroFinanceiroRepository.updateDataEncerramento(new Date(), id);
				}

				getRepository().save(centroFinanceiro);

			} else {
				throw new ValidationException("ValidationException","Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.");
			}
		}
	}

	@Override
	@Transactional
	public void cancelarById(String id) throws Exception {
		try {
			cancelarCentroFinanceiroById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public void cancelarCentroFinanceiroById(Long id) {
		if (id != null) {
			CentroFinanceiro centroFinanceiro = getRepository().findById(id).orElse(null);
			centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO);
			adicionarCentroFinanceiroSituacao(centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO);

			if (ObjectUtil.isNotNull(centroFinanceiro.getContrato()) && ObjectUtil.isNotNull(centroFinanceiro.getContrato().getCodigo())) {
				contratoService.atualizarContratoDeCentroFinanceiro(centroFinanceiro.getContrato().getCodigo());
			}

			getRepository().save(centroFinanceiro);
		}
	}

	@Override
	@Transactional
	public void removerById(String id) throws Exception {
		removerCentroFinanceiroById(Long.parseLong(id));
	}

	@Override
	public BigDecimal totalCentrosFinanceirosDeContrato(ContratoTO contratoTO) {
		BigDecimal total = new BigDecimal(ZERO_LONG);

		List<CentroFinanceiro> listaCentroFinanceiroContrato = select()
			.from(QCentroFinanceiro.centroFinanceiro)
			.select(Projections.fields(
				CentroFinanceiro.class,
				QCentroFinanceiro.centroFinanceiro.codigo,
				QCentroFinanceiro.centroFinanceiro.nome,
				QCentroFinanceiro.centroFinanceiro.identificador,
				QCentroFinanceiro.centroFinanceiro.identificadorFinal,
				QCentroFinanceiro.centroFinanceiro.situacao
			))
			.where(QCentroFinanceiro.centroFinanceiro.contrato.codigo.eq(contratoTO.getCodigo()))
			.fetch();

		if (ObjectUtil.isNotEmpty(listaCentroFinanceiroContrato)) {
			for (CentroFinanceiro centroFinanceiro : listaCentroFinanceiroContrato) {
				total = total.add(getValorTotalCentroFinanceiro(getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), contratoTO));
			}
		}

		return total;
	}

	@Override
	public BigDecimal getValorTotalCentroFinanceiro(CentroFinanceiroTO centroFinanceiro, ContratoTO contrato) {
		BigDecimal valorTotal = calcularValorMensalCentroFinanceiro(centroFinanceiro);
		Integer prazo = getPrazo(centroFinanceiro, contrato);
		if (prazo != null && prazo.doubleValue() > 0) {
			valorTotal = valorTotal.multiply(new BigDecimal(prazo));
		}

		return valorTotal.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal calcularValorMensalCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		FechoTO ultimoFecho = fechoService.getUltimoFechoByCodCentroFinanceiro(centroFinanceiro.getCodigo());
		return ultimoFecho.getCodigo() == null ? BigDecimal.ZERO : fechoService.valorMensal(ultimoFecho.getCodigo());
	}

	@Override
	public BigDecimal calcularValorMensalCentroFinanceiro(long codigoCf) {
		FechoTO ultimoFecho = fechoService.getUltimoFechoByCodCentroFinanceiro(codigoCf);
		return fechoService.valorMensal(ultimoFecho.getCodigo());
	}

	@Override
	public BigDecimal calcularValorTotal(long codigoCf) {
		CentroFinanceiro centroFinanceiro = getRepository().findById(codigoCf).orElseThrow(EntityNotFoundException::new);
		Integer prazo = contratoService.obterPrazo(centroFinanceiro.getContrato().getCodigo());
		return calcularValorMensalCentroFinanceiro(codigoCf).multiply(new BigDecimal(prazo));
	}

	public BigDecimal getSubTotal(FechoTO ultimoFecho, FechoDespesaGeral.TipoDespesaGeral tipoDespesa) {
		BigDecimal total = new BigDecimal(ZERO);
		if (ultimoFecho.getListaFechosDespesasGerais() != null && !ultimoFecho.getListaFechosDespesasGerais().isEmpty()) {
			for (FechoDespesaGeralTO despesa : ultimoFecho.getListaFechosDespesasGerais()) {
				// FIXME: Por que o TO usa os enums formatados?
				if (despesa.getTipoDespesaGeral().equals(tipoDespesa.toString()) || despesa.getTipoDespesaGeral().equals(tipoDespesa.getNome())) {
					total = total.add(despesa.getValorTotalItem());
				}
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal getTotal(FechoTO ultimoFecho) {
		BigDecimal total = new BigDecimal(ZERO);
		total = total.add(this.getSubTotalMaoDeObra(ultimoFecho)).add(getTotalEncargosSociais(ultimoFecho));
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal getSubTotalMaoDeObra(FechoTO ultimoFecho) {
		BigDecimal total = new BigDecimal(ZERO);
		if (ultimoFecho.getListaFechosMaoDeObra() != null && !ultimoFecho.getListaFechosMaoDeObra().isEmpty()) {
			for (FechoMaoDeObraTO maoDeObra : ultimoFecho.getListaFechosMaoDeObra()) {
				total = total.add(maoDeObra.getValorTotal());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal getTotalEncargosSociais(FechoTO ultimoFecho) {
		BigDecimal total = new BigDecimal(ZERO);
		if (ultimoFecho.getPercentualEncargosSociais() != null) {
			total = this.getSubTotalMaoDeObra(ultimoFecho).multiply(ultimoFecho.getPercentualEncargosSociais());
			total = total.divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public Integer getPrazo(CentroFinanceiroTO centroFinanceiro, ContratoTO contratoTO) {
		if (centroFinanceiro != null) {
			return contratoTO.getPrazoAtual();
		}
		return 0;
	}

	@Override
	public List<CentroFinanceiroTO> obterCentroDeLucroPorContrato(ContratoTO contrato) throws Exception {
		List<CentroFinanceiro> listaCentroFinanceiroContrato = select()
			.from(QCentroFinanceiro.centroFinanceiro)
			.select(Projections.fields(
				CentroFinanceiro.class,
				QCentroFinanceiro.centroFinanceiro.codigo,
				QCentroFinanceiro.centroFinanceiro.nome,
				QCentroFinanceiro.centroFinanceiro.identificador,
				QCentroFinanceiro.centroFinanceiro.identificadorFinal,
				QCentroFinanceiro.centroFinanceiro.situacao,
				Projections.bean(CentroFinanceiro.class, QCentroFinanceiro.centroFinanceiro.pai.codigo, QCentroFinanceiro.centroFinanceiro.pai.nome).as("pai")
			))
			.where(QCentroFinanceiro.centroFinanceiro.contrato.codigo.eq(contrato.getCodigo()))
			.orderBy(QCentroFinanceiro.centroFinanceiro.identificadorFinal.asc())
			.fetch();

		List<CentroFinanceiroTO> listCentroFinanceiroTOs = new ArrayList<>();

		Contrato contratoOriginal = contratoRepository.findById(contrato.getCodigo()).orElse(new Contrato());

		if (listaCentroFinanceiroContrato != null && !listaCentroFinanceiroContrato.isEmpty()) {
			for (CentroFinanceiro cf : listaCentroFinanceiroContrato) {
				CentroFinanceiroTO cfTO = getMapper().map(cf, CentroFinanceiroTO.class);
				// Calcula os valores totais e mensais de cada Centro de Lucro
				cfTO.setValorMensalCentroFinanceiro(calcularValorMensalCentroFinanceiro(cfTO));
				cfTO.setValorTotalCentroFinanceiro(getValorTotalCentroFinanceiro(cfTO, contrato));
				cfTO.setPrazo(contratoOriginal.getPrazoAtual() == null ? 1 : contratoOriginal.getPrazoAtual());
				listCentroFinanceiroTOs.add(cfTO);
			}
		}
		return listCentroFinanceiroTOs;
	}

	@Override
	public List<CentroFinanceiroTO> obterCentrosFinanceirosPeloCodigoDoContrato(Long codigo) {
		return map(select()
			.from(QCentroFinanceiro.centroFinanceiro)
			.select(Projections.fields(CentroFinanceiro.class,
				QCentroFinanceiro.centroFinanceiro.codigo,
				QCentroFinanceiro.centroFinanceiro.nome,
				QCentroFinanceiro.centroFinanceiro.situacao))
			.where(QCentroFinanceiro.centroFinanceiro.contrato.codigo.eq(codigo))
			.fetch());
	}

	@Override
	@Transactional
	public void removerCentroFinanceiroById(Long id) {
		if (id != null) {

			//Verifica se o centro financeiro está associado com alguma entidade
			if (!centroFinanceiroByIdSemAssociacoes(id)) {
				throw new ValidationException("ValidationException","Este Centro Financeiro não pode ser removido.");
			}

			//criar CentroFinanceiroEnderecoTO para fazer a pesquisa de CentroFinanceiroEnderecos relacionados ao CentroFinanceiro
			CentroFinanceiroEnderecoTO filtroCfEndereco = new CentroFinanceiroEnderecoTO();
			filtroCfEndereco.setCentroFinanceiro(new CentroFinanceiroTO());
			filtroCfEndereco.getCentroFinanceiro().setCodigo(id);

			List<CentroFinanceiroEnderecoTO> listaCentroFinanceiroEnderecoTO = centroFinanceiroEnderecoService.buscarListaCentroFinanceiroEndereco(filtroCfEndereco);

			List<CentroFinanceiroEndereco> listaCentroFinanceiroEndereco = new ArrayList<>();

			for (CentroFinanceiroEnderecoTO centroFinanceiroEnderecoTO : listaCentroFinanceiroEnderecoTO) {
				listaCentroFinanceiroEndereco.add(getMapper().map(centroFinanceiroEnderecoTO, CentroFinanceiroEndereco.class));
			}

			// CentroFinanceiroEndereco e Endereco
			for (CentroFinanceiroEndereco cfEndereco : listaCentroFinanceiroEndereco) {
				Endereco endereco = cfEndereco.getEndereco();
				centroFinanceiroEnderecoRepository.delete(cfEndereco);
				enderecoRepository.delete(endereco);
			}

			//remove as entidades associadas ao centro financeiro (no legado, com relacionamento CascadeType.DELETE_ORPHAN)
			removerAssociacoesByCodigoCentroFinanceiro(id);

			CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
			centroFinanceiro.setCodigo(id);

			//remover CentroFinanceiro
			getRepository().delete(centroFinanceiro);

		}
	}

	/**
	 * Verifica se um centro financeiro está associado com alguma entidade.
	 * Com exceção das entidades que no legado tinham um relacionamento com CascadeType.DELETE_ORPHAN.
	 *
	 * @param id
	 * @return
	 */
	private Boolean centroFinanceiroByIdSemAssociacoes(Long id) {

		long lista1 = colaboradorRepository.countByCentroFinanceiroCodigo(id);
		if (lista1 > 0) {
			return Boolean.FALSE;
		}

		long lista2 = faturamentoRepository.countByCentroFinanceiroCodigo(id);
		if (lista2 > 0) {
			return Boolean.FALSE;
		}

		long lista3 = pagamentoRepository.countByCentroFinanceiroCodigo(id);
		if (lista3 > 0) {
			return Boolean.FALSE;
		}

		long lista4 = fundoFixoRepository.countByCentroFinanceiroCodigo(id);
		if (lista4 > 0) {
			return Boolean.FALSE;
		}

		List<LoteInativacaoRegistro> lista5 = loteInativacaoRegistroRepository.findByCentroFinanceiroCodigo(id);
		if (lista5 != null && !lista5.isEmpty()) {
			return Boolean.FALSE;
		}

		List<Nota> lista6 = notaRepository.findByCentroFinanceiroCodigo(id);
		if (lista6 != null && !lista6.isEmpty()) {
			return Boolean.FALSE;
		}

		List<OrdemCompra> lista7 = ordemCompraRepository.findByCentroFinanceiroCodigo(id);
		if (lista7 != null && !lista7.isEmpty()) {
			return Boolean.FALSE;
		}

		List<MensagemUsuario> lista8 = mensagemUsuarioRepository.findByCentroFinanceiroCodigo(id);
		if (lista8 != null && !lista8.isEmpty()) {
			return Boolean.FALSE;
		}

		List<RateioItemRecebimento> lista9 = rateioItemRecebimentoRepository.findByCentroFinanceiroCodigo(id);
		if (lista9 != null && !lista9.isEmpty()) {
			return Boolean.FALSE;
		}

		List<RateioNotaFiscal> lista10 = rateioNotaFiscalRepository.findByCentroFinanceiroCodigo(id);
		if (lista10 != null && !lista10.isEmpty()) {
			return Boolean.FALSE;
		}

		List<RateioNotaPagamento> lista11 = rateioNotaPagamentoRepository.findByCentroFinanceiroCodigo(id);
		if (lista11 != null && !lista11.isEmpty()) {
			return Boolean.FALSE;
		}

		List<RelacaoNotaFiscal> lista12 = relacaoNotaFiscalRepository.findByCentroFinanceiroCodigo(id);
		if (lista12 != null && !lista12.isEmpty()) {
			return Boolean.FALSE;
		}

		List<RequisicaoCompra> lista13 = requisicaoCompraRepository.findByCentroFinanceiroCodigo(id);
		if (lista13 != null && !lista13.isEmpty()) {
			return Boolean.FALSE;
		}

		List<Transacao> lista14 = transacaoRepository.findByCentroFinanceiroCodigo(id);
		if (lista14 != null && !lista14.isEmpty()) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Deleta do banco de dados as entidades associadas a um centro financeiro.
	 * No legado eram relacionamentos com CascadeType.DELETE_ORPHAN.
	 *
	 * @param id
	 */
	@Override
	@Transactional
	public void removerAssociacoesByCodigoCentroFinanceiro(Long id) {

		try {
			logger.info("Deletando lista de CentroFinanceiroSituacao.");
			List<CentroFinanceiroSituacao> lista1 = centroFinanceiroSituacaoRepository.findByCentroFinanceiroCodigo(id);
			for (CentroFinanceiroSituacao item : lista1) {
				centroFinanceiroSituacaoRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de CentroFinanceiroSituacao.", e);
		}

		try {
			logger.info("Deletando lista de CentroFinanceiroFluxoEtapa.");
			List<CentroFinanceiroFluxoEtapa> lista2 = centroFinanceiroFluxoEtapaRepository.findByCentroFinanceiroCodigo(id);
			for (CentroFinanceiroFluxoEtapa item : lista2) {
				centroFinanceiroFluxoEtapaRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de CentroFinanceiroFluxoEtapa.", e);
		}

		try {
			logger.info("Deletando lista de CentroFinanceiroAliquota.");
			List<CentroFinanceiroAliquota> lista3 = centroFinanceiroAliquotaRepository.findByCentroFinanceiroCodigo(id);
			for (CentroFinanceiroAliquota item : lista3) {
				centroFinanceiroAliquotaRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de CentroFinanceiroAliquota.", e);
		}

		try {
			logger.info("Deletando lista de Fecho.");
			List<Fecho> lista4 = fechoRepository.findByCentroFinanceiroCodigo(id);
			for (Fecho item : lista4) {
				fechoRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de Fecho.", e);
		}

		try {
			logger.info("Deletando lista de MotivoRetornoCentroFinanceiro.");
			List<MotivoRetornoCentroFinanceiro> lista5 = motivoRetornoCentroFinanceiroRepository.findByCentroFinanceiroCodigo(id);
			for (MotivoRetornoCentroFinanceiro item : lista5) {
				motivoRetornoCentroFinanceiroRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de MotivoRetornoCentroFinanceiro.", e);
		}

		try {
			logger.info("Deletando lista de MensagemGerencia.");
			List<MensagemGerencia> lista6 = mensagemGerenciaRepository.findByCentroFinanceiroCodigo(id);
			for (MensagemGerencia item : lista6) {
				mensagemGerenciaRepository.delete(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Erro ao deletar lista de MensagemGerencia.", e);
		}
	}

	/**
	 * Cria uma nova entidade CentroFinanceiroSituacao e a salva ela no banco de dados.
	 *
	 * @param centroFinanceiro
	 * @param situacao
	 */
	private void adicionarCentroFinanceiroSituacao(CentroFinanceiro centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro situacao) {
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(securityService.getUser().getPessoaFisica().getCodigo());
		contratoService.adicionarCentroFinanceiroSituacao(getMapper().map(colaborador, ColaboradorTO.class), getMapper().map(centroFinanceiro, CentroFinanceiroTO.class), situacao.toString());
	}

	@Override
	public CentroFinanceiroTO findByCodigo(Long codigo) {
		return map(select().where(qCentroFinanceiro.codigo.eq(codigo)).fetchOne());
	}

	/**
	 * Cria uma lista de novas situações para um centro financeiro.
	 *
	 * @param centroFinanceiro
	 * @return
	 */
	@Override
	public List<CentroFinanceiroSituacaoTO> criarSituacoesParaCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		List<CentroFinanceiroSituacao> lista = new ArrayList<>();

		PessoaFisica pessoaFisica = usuarioSecurityService.getUser().getPessoaFisica();
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(pessoaFisica.getCodigo());

		if (centroFinanceiro.getCodigo() == null) {
			CentroFinanceiroSituacao situacao = criarSituacao(colaborador, CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO, getMapper().map(centroFinanceiro, CentroFinanceiro.class));
			lista.add(situacao);
		}

		if (CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO.equals(centroFinanceiro.getSituacao())) {
			CentroFinanceiroSituacao situacao = criarSituacao(colaborador, CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO, getMapper().map(centroFinanceiro, CentroFinanceiro.class));
			lista.add(situacao);
		}

		List<CentroFinanceiroSituacaoTO> centroFinanceiroSituacaoTOS = new ArrayList<>();

		for (CentroFinanceiroSituacao centroFinanceiroSituacao : lista) {
			centroFinanceiroSituacaoTOS.add(getMapper().map(centroFinanceiroSituacao, CentroFinanceiroSituacaoTO.class));
		}

		return centroFinanceiroSituacaoTOS;
	}

	/**
	 * Criar um CentroFinanceiroSituacao.
	 *
	 * @param colaborador
	 * @param situacao
	 * @param centroFinanceiro
	 * @return
	 */
	private CentroFinanceiroSituacao criarSituacao(Colaborador colaborador, CentroFinanceiro.SituacaoCentroFinanceiro situacao, CentroFinanceiro centroFinanceiro) {
		CentroFinanceiroSituacao cfSituacao = new CentroFinanceiroSituacao();
		cfSituacao.setCentroFinanceiro(centroFinanceiro);
		cfSituacao.setColaborador(colaborador);
		cfSituacao.setData(new Date(System.currentTimeMillis()));
		cfSituacao.setSituacao(situacao);
		return cfSituacao;
	}

	private List<CentroFinanceiro> getListaCentroFinanceiroByContrato(Long codContrato) {
		QContrato qContrato = QContrato.contrato;

		QBean<CentroFinanceiro> qBean = Projections.bean(
			CentroFinanceiro.class,
			qCentroFinanceiro.codigo,
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.pai.codigo
			).as("pai")
		);

		JPAQuery<CentroFinanceiro> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qCentroFinanceiro)
			.join(qCentroFinanceiro.contrato, qContrato)
			.where(qContrato.codigo.eq(codContrato));

		return jpaQuery.fetch();
	}

	@Override
	protected void doBeforeSave(CentroFinanceiro entity, CentroFinanceiroTO object) throws ServiceException {
		object.setDataCriacao(new Date());
		object.setCriador(usuarioSecurityService.getUsername());

		CentroFinanceiroTO novoCentro = this.getCentroLucroByIndetificadorFinal(object.getIdentificador().substring(7));

		if (novoCentro != null){
			throw new ValidationException("ValidationException", "Já existe outro centro financeiro com este identificador.");
		}
	}

	@Override
	protected void doBeforeUpdate(CentroFinanceiro entity, CentroFinanceiroTO object) throws ServiceException {
		super.doBeforeUpdate(entity, object);
	}

	@Override
	protected void doAfterSave(CentroFinanceiro entity, CentroFinanceiroTO object) throws ServiceException {
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(securityService.getUser().getPessoaFisica().getCodigo());
		adicionarCentroFinanceiroSituacao(entity, CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO);
	}

	@Override
	protected void doAfterUpdate(CentroFinanceiro entity, CentroFinanceiroTO object) throws ServiceException {
		if (CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO.equals(entity.getSituacao())) {
			if (!centroFinanceiroSituacaoService.ultimaSituacaoInativa(entity.getCodigo())) {
				Colaborador colaborador = new Colaborador();
				colaborador.setCodigo(securityService.getUser().getPessoaFisica().getCodigo());
				adicionarCentroFinanceiroSituacao(entity, CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO);
			}
		}
	}

	@Override
	public CentroFinanceiroTO pesquisarCentroFinanceiroTOPorCodigoComCodigoUnidade(Long codigoCentroFinanceiro) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		QBean<CentroFinanceiro> qBean = Projections.bean(
			CentroFinanceiro.class,
			qCentroFinanceiro.codigo,
			Projections.bean(
				Unidade.class,
				qCentroFinanceiro.unidade.codigo
			).as("unidade")
		);
		JPAQuery<CentroFinanceiro> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qCentroFinanceiro)
			.where(qCentroFinanceiro.codigo.eq(codigoCentroFinanceiro));

		CentroFinanceiro centroFinanceiro = jpaQuery.fetchFirst();

		return map(centroFinanceiro);
	}


	@Override
	public List<CentroFinanceiroTO> buscarClPorCliente(String idCliente) {
		List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();

		try {

			QContrato qContrato = QContrato.contrato;
			QPessoa qPessoa = QPessoa.pessoa;
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QBean<CentroFinanceiro> qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select();
			Long id = Long.parseLong(idCliente);

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.join(qCentroFinanceiro.contrato, qContrato)
				.join(qContrato.cliente, qPessoa)
				.where(qContrato.cliente.codigo.eq(id));

			listaCentroFinanceiros = jpaQuery.fetch();
			return map(listaCentroFinanceiros);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Retorna Todas as Gerencias com Situação Ativa
	 * @return
	 */

	@Override
	public List<CentroFinanceiroTO> buscarTodasGerencias() {
		try {
			List<CentroFinanceiro> listaCentroFinanceiros;

			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QGerencia qGerencia = QGerencia.gerencia;
			QBean<CentroFinanceiro> qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.situacao
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.join(qGerencia).on(qCentroFinanceiro.codigo.eq(qGerencia.codigo))
				.where(
					(qCentroFinanceiro.pai.isNull()))
				.orderBy(
					qCentroFinanceiro.situacao.asc(),
					qCentroFinanceiro.identificador.asc(),
					qCentroFinanceiro.nome.asc());

			listaCentroFinanceiros = jpaQuery.fetch();
			return map(listaCentroFinanceiros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CentroFinanceiroTO obterCentroFinanceiroSimpleTelaNotaFiscal(Long codigoCentroFinanceiro) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				Projections.bean(Contrato.class, qCentroFinanceiro.contrato.codigo).as("contrato"))
		);
		jpaQuery.where(qCentroFinanceiro.codigo.eq(codigoCentroFinanceiro));
		return map((CentroFinanceiro) jpaQuery.fetchOne());

	}

	@Override
	public List<CentroFinanceiroTO> getCFsByUnidadeMatriz(Long codigoMatriz) {
		try {
			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();

			if (codigoMatriz != null) {
				QBean<CentroFinanceiro> qBean = Projections.bean(
					CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.nome,
					qCentroFinanceiro.identificadorFinal,
					Projections.bean(Unidade.class, qCentroFinanceiro.unidade.codigo).as("unidade")
				);

				QUnidade qUnidade = QUnidade.unidade;

				JPAQuery<CentroFinanceiro> jpaQuery = select();

				jpaQuery
					.select(qBean)
					.from(qCentroFinanceiro).innerJoin(qCentroFinanceiro.unidade, qUnidade)
					.where(qUnidade.matriz.codigo.eq(codigoMatriz));

				listaCentroFinanceiros = jpaQuery.fetch();
			}

			return map(listaCentroFinanceiros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<CentroFinanceiroTO> getCFsByUnidadeGerencia(FiltroGraficoCLTO filtroGraficoCLTO) {
		try {
			List<CentroFinanceiro> centrosFinanceiros = new ArrayList<>();

			QBean<CentroFinanceiro> qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				Projections.bean(Unidade.class, qCentroFinanceiro.unidade.codigo).as("unidade")
			);

			QUnidade qUnidade = QUnidade.unidade;

			JPAQuery<CentroFinanceiro> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro).innerJoin(qCentroFinanceiro.unidade, qUnidade)
				.where(qUnidade.codigo.in(filtroGraficoCLTO.getListaCodEmpresa())
					.and(qCentroFinanceiro.pai.codigo.eq(filtroGraficoCLTO.getCodigoCentroFinanceiro()))
					.and(QCentroFinanceiro.centroFinanceiro.situacao.ne(CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO)));

			centrosFinanceiros = jpaQuery.fetch();

			centrosFinanceiros.sort((o1, o2) -> o1.getIdentificadorFinal().compareTo(o2.getIdentificadorFinal()));

			return map(centrosFinanceiros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<CentroFinanceiroTO> buscaCcClPorGerencia(CentroFinanceiroTO exampleObj) {

		JPAQuery<CentroFinanceiro> jpaQuery = select();

		jpaQuery.select(Projections.fields(CentroFinanceiro.class,
			QCentroFinanceiro.centroFinanceiro.codigo,
			QCentroFinanceiro.centroFinanceiro.nome,
			QCentroFinanceiro.centroFinanceiro.identificador,
			QCentroFinanceiro.centroFinanceiro.identificadorFinal,
			QCentroFinanceiro.centroFinanceiro.situacao,
			Projections.bean(Unidade.class, QCentroFinanceiro.centroFinanceiro.unidade.codigo).as("unidade")
		));

		jpaQuery.where(
			(QCentroFinanceiro.centroFinanceiro.pai.codigo.eq(exampleObj.getCodigo()))
				.and(QCentroFinanceiro.centroFinanceiro.situacao.ne(CentroFinanceiro.SituacaoCentroFinanceiro.INATIVO))
		);

		List<CentroFinanceiro> centrosFinanceiros = jpaQuery.fetch();

		centrosFinanceiros.sort((o1, o2) -> o1.getIdentificadorFinal().compareTo(o2.getIdentificadorFinal()));

		return map(centrosFinanceiros);
	}

	@Override
	public List<CentroFinanceiroTO> buscarGerenciasPorRegional(Long codigoRegional) {
		List<Long> listaCodGerencia = regionalGerenciaService.listaCodGerenciaPorCodRegional(codigoRegional);

		try {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

			QBean<CentroFinanceiro> qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.situacao
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.where(
					(qCentroFinanceiro.pai.isNull()).and(qCentroFinanceiro.codigo.in(listaCodGerencia))
				)
				.orderBy(
					qCentroFinanceiro.situacao.asc(),
					qCentroFinanceiro.nome.asc());

			List<CentroFinanceiro> listaCentroFinanceiros = jpaQuery.fetch();
			return map(listaCentroFinanceiros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JPAQuery jPAQueryObterCentroFinanceiroSimple(Long codigoCentroFinanceiro) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal
			));
		jpaQuery.where(qCentroFinanceiro.codigo.eq(codigoCentroFinanceiro));
		return jpaQuery;
	}

	@Override
	public JPAQuery jPAQueryObterCentroFinanceiroComUnidade(Long codigoCentroFinanceiro) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				Projections.fields(Unidade.class,
					QUnidade.unidade.codigo,
					QUnidade.unidade.nome,
					QUnidade.unidade.nomeFantasia,
					QUnidade.unidade.codnome,
					Projections.fields(Unidade.class,
						QUnidade.unidade.matriz.codigo,
						QUnidade.unidade.matriz.nome,
						QUnidade.unidade.matriz.nomeFantasia,
						QUnidade.unidade.matriz.codnome
					).as(QUnidade.unidade.matriz)
				).as(QUnidade.unidade)
			));
		jpaQuery
			.innerJoin(qCentroFinanceiro.unidade, QUnidade.unidade)
			.where(qCentroFinanceiro.codigo.eq(codigoCentroFinanceiro));
		return jpaQuery;
	}

	/**
	 * Função de busca de Centros Financeiros, para ser usado em telas que NÃO sejam de pesquisa.
	 * É usado nas telas de faturamento e contas a receber para verificação de permissão.
	 * @param exampleObj
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<CentroFinanceiroTO> buscarFaturamentoCFs(CentroFinanceiroTO exampleObj) {
		try {
			QContrato qContrato = QContrato.contrato;
			QUnidade qUnidade = QUnidade.unidade;
			QUnidade qUnidadeMatriz = QUnidade.unidade;
			QPessoa qPessoa = QPessoa.pessoa;

			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.situacao,
				Projections.bean(Contrato.class, qContrato.codigo).as("contrato"),
				Projections.bean(Unidade.class, qUnidade.codigo, qUnidade.tipoUnidade,
					Projections.bean(Unidade.class, qUnidadeMatriz.codigo).as("matriz")
				).as("unidade")
			);

			JPAQuery<CentroFinanceiro> jpaQueryCentroFinanceiro = select();
			BooleanBuilder builder = restricaoParaBuscaCF(exampleObj);

			jpaQueryCentroFinanceiro
				.select(qBean)
				.from(qCentroFinanceiro)
				.join(qCentroFinanceiro.unidade, qUnidade)
				.leftJoin(qCentroFinanceiro.contrato, qContrato)
				.leftJoin(qUnidade.matriz, qUnidadeMatriz)
				.leftJoin(qContrato.cliente, qPessoa)
				.where(builder)
				.orderBy(qCentroFinanceiro.codigo.asc());

			// FIXME: Usuário com esse perfil só pode ser notas dos CLs associados a ele.
			if (usuarioService.possuiUmPerfilLista("faturamento-cns-gerencia")) {
				List<CentroFinanceiroTO> list = usuarioService.getCentroFinanceiros();
				List<Long> codigos = new ArrayList<>();
				list.forEach(cf -> codigos.add(cf.getCodigo()));
				jpaQueryCentroFinanceiro.where(qCentroFinanceiro.codigo.in(codigos).or(qCentroFinanceiro.pai.codigo.in(codigos)));
			}

			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();
			List<CentroFinanceiroTO> listaTO = new ArrayList<>();

			listaCentroFinanceiros = jpaQueryCentroFinanceiro.fetch();

			for (CentroFinanceiro cf : listaCentroFinanceiros) {
				CentroFinanceiroTO to = getMapper().map(cf, CentroFinanceiroTO.class);
				listaTO.add(to);
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retorna Todas as Gerencias Cadastradas
	 * @return
	 */
	@Override
	public List<CentroFinanceiroTO> findAllGerencias() {
		List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();

		try {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.identificador
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.where(qCentroFinanceiro.pai.isNull()).orderBy(qCentroFinanceiro.nome.asc());

			listaCentroFinanceiros = jpaQuery.fetch();
			return map(listaCentroFinanceiros);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<CentroFinanceiroTO> obterListaCentrosLucrosAtivos() {
		try {
			List<CentroFinanceiro> listaCentroLucros;
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.identificador
			);
			JPAQuery<CentroFinanceiro> jpaQuery = select();
			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.where(qCentroFinanceiro.situacao.eq(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO)
				.and(qCentroFinanceiro.tipo.eq(CentroFinanceiro.TipoCentroFinanceiro.CENTRO_LUCRO)))
				.orderBy(qCentroFinanceiro.identificadorFinal.asc());
			listaCentroLucros = jpaQuery.fetch();
			return map(listaCentroLucros);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<CentroFinanceiroTO> buscarCFsSimples(CentroFinanceiroTO exampleObj) {
		try {
			QContrato qContrato = QContrato.contrato;
			QUnidade qUnidade = QUnidade.unidade;
			QUnidade qUnidadeMatriz = QUnidade.unidade;
			QPessoa qPessoa = QPessoa.pessoa;

			QBean qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.identificadorFinal
			);

			JPAQuery<CentroFinanceiro> jpaQueryCentroFinanceiro = select();
			BooleanBuilder builder = restricaoParaBuscaCF(exampleObj);
			builder.and(qCentroFinanceiro.pai.isNotNull());

			jpaQueryCentroFinanceiro
				.select(qBean)
				.from(qCentroFinanceiro)
				.join(qCentroFinanceiro.unidade, qUnidade)
				.leftJoin(qCentroFinanceiro.contrato, qContrato)
				.leftJoin(qUnidade.matriz, qUnidadeMatriz)
				.leftJoin(qContrato.cliente, qPessoa)
				.where(builder)
				.orderBy(qCentroFinanceiro.codigo.asc());

			List<CentroFinanceiro> listaCentroFinanceiros = new ArrayList<>();
			List<CentroFinanceiroTO> listaTO = new ArrayList<>();

			listaCentroFinanceiros = jpaQueryCentroFinanceiro.fetch();

			for (CentroFinanceiro cf : listaCentroFinanceiros) {
				CentroFinanceiroTO to = getMapper().map(cf, CentroFinanceiroTO.class);
				listaTO.add(to);
			}

			return listaTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CentroFinanceiroTO getCentroLucroByIndetificadorFinal(Long codigoGerencia, String identificadorFinal) {
		return map(select(
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.identificadorFinal
			)
		)
			.where(QCentroFinanceiro.centroFinanceiro.identificadorFinal.equalsIgnoreCase(identificadorFinal.trim())
				.and(QCentroFinanceiro.centroFinanceiro.pai.codigo.eq(codigoGerencia))
				.and(QCentroFinanceiro.centroFinanceiro.pai.isNotNull())
				.and(QCentroFinanceiro.centroFinanceiro.tipo.eq(CentroFinanceiro.TipoCentroFinanceiro.CENTRO_LUCRO)))
			.fetchOne());
	}

	@Override
	public CentroFinanceiroTO getCentroLucroByIndetificadorFinal(String identificadorFinal){
		return map(select(
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificador,
				qCentroFinanceiro.identificadorFinal
			)
		)
			.where(QCentroFinanceiro.centroFinanceiro.identificadorFinal.equalsIgnoreCase(identificadorFinal.trim())
				.and(QCentroFinanceiro.centroFinanceiro.pai.isNotNull())
				.and(QCentroFinanceiro.centroFinanceiro.tipo.eq(CentroFinanceiro.TipoCentroFinanceiro.CENTRO_LUCRO)))
			.fetchFirst());
	}

	@Override
	public BigDecimal custoMensal(Long codigoCl) {
		FechoTO fechoTO = fechoService.getUltimoFechoByCodCentroFinanceiro(codigoCl);
		if (fechoTO == null || fechoTO.getCodigo() == null || fechoTO.getValorGastosGerais() == null
		|| fechoTO.getValorMaoObra() == null || fechoTO.getValorMaterial() == null || fechoTO.getValorServicoTerceiro() == null) {
			return BigDecimal.ZERO;
		}
		return fechoTO.getValorGastosGerais()
			.add(fechoTO.getValorMaterial())
			.add(fechoTO.getValorServicoTerceiro());
	}

	@Override
	public List<CentroFinanceiroTO> cLsComDespesasAcimaLimite(Date dataAnalise, Double limite, String meses) {
		try {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QContrato qContrato = QContrato.contrato;

			QBean<CentroFinanceiro> qBean = Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.identificador,
				Projections.fields(CentroFinanceiro.class,
					qCentroFinanceiro.pai.codigo,
					qCentroFinanceiro.pai.nome).as(qCentroFinanceiro.pai)
			);

			JPAQuery<CentroFinanceiro> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qCentroFinanceiro)
				.innerJoin(qCentroFinanceiro.contrato, qContrato)
				.where(
					qCentroFinanceiro.situacao.eq(CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO)
						.and(qCentroFinanceiro.tipo.eq(CentroFinanceiro.TipoCentroFinanceiro.CENTRO_LUCRO))
						.and(qContrato.situacao.eq(Contrato.SituacaoContrato.ATIVO))

//						.and(qCentroFinanceiro.codigo.eq(591553L))
//						.and(qCentroFinanceiro.codigo.eq(592019L))

				)
				.orderBy(qCentroFinanceiro.identificadorFinal.asc(), qCentroFinanceiro.pai.nome.asc());

			List<CentroFinanceiroTO> cls = map(jpaQuery.fetch());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataAnalise);

			Calendar de = Calendar.getInstance();
			de.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));

			Calendar ate = Calendar.getInstance();
			ate.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

			cls = cls.stream()
			.map(centroFinanceiroTO -> {
				BigDecimal custo = custoMensal(centroFinanceiroTO.getCodigo());
				centroFinanceiroTO.setCustoMensal(custo);
				return centroFinanceiroTO;
			})
			.filter(centroFinanceiroTO -> centroFinanceiroTO.getCustoMensal() != null && centroFinanceiroTO.getCustoMensal().doubleValue() > 0).collect(Collectors.toList());

			int itens[] = new int[2];

			itens[0] = cls.size();
			itens[1] = 0;

			logger.debug(String.format("Calculando custos para %d CLs...", itens[0]));

			Stream<CentroFinanceiroTO> parallel = cls.stream().parallel();

			parallel.forEach(to -> {
				BigDecimal homologado = parcelaNotaService.obterPorCentroFinanceiroHomologadas(to.getCodigo(), de.getTime(), ate.getTime());

				if (homologado.doubleValue() > 0) {
					to.setPercentualHomologado(homologado.multiply(new BigDecimal(100D)).divide(to.getCustoMensal(), 2, RoundingMode.HALF_UP).setScale(1, RoundingMode.HALF_UP));
				} else {
					to.setPercentualHomologado(BigDecimal.ZERO);
				}

				BigDecimal lancado = parcelaNotaService.obterPorCentroFinanceiroLancadas(to.getCodigo(), de.getTime(), ate.getTime());

				if (lancado.doubleValue() > 0) {
					to.setPercentualLancado(lancado.multiply(new BigDecimal(100D)).divide(to.getCustoMensal(), 2, RoundingMode.HALF_UP).setScale(1, RoundingMode.HALF_UP));
				} else {
					to.setPercentualLancado(BigDecimal.ZERO);
				}

				//Incluído em 02/06/2021 - SGINV-2690
				Integer numMeses = (meses == null || !Check.isNumeric(meses)) ? 5 : Integer.valueOf(meses);
				String mesAno = "Total %s/%s";
				for (int contador = 1; contador <= numMeses; contador++) {
					Calendar proximoMesDe = Calendar.getInstance();
					proximoMesDe.setTime(de.getTime());
					Calendar proximoMesAte = Calendar.getInstance();
					proximoMesAte.setTime(ate.getTime());

					proximoMesDe.add(Calendar.MONTH, contador);
					proximoMesAte.add(Calendar.MONTH, contador);
					proximoMesAte.set(Calendar.DAY_OF_MONTH, proximoMesAte.getActualMaximum(Calendar.DAY_OF_MONTH));

					to.getEmailJobCabecalho().add(String.format(mesAno, Feriado.Meses.getMes(proximoMesDe.get(Calendar.MONTH)), proximoMesDe.get(Calendar.YEAR)));
					if (to.getCustoLancadoFuturoCurrency() == null)
						to.setCustoLancadoFuturoCurrency(new ArrayList<>());
					to.getCustoLancadoFuturoCurrency().add(formatarNumero(parcelaNotaService.obterPorCentroFinanceiroLancadas(to.getCodigo(), proximoMesDe.getTime(), proximoMesAte.getTime())));
				}

				to.setNome(to.getPai().getNome().substring(0, (Math.min(to.getPai().getNome().length(), 40))));
				to.setCustoHomologado(homologado);
				to.setCustoLancado(lancado);

				to.setCustoHomologadoCurrency(formatarNumero(to.getCustoHomologado()));

				to.setCustoLancadoCurrency(formatarNumero(to.getCustoLancado()));

				to.setCustoMensalCurrency(formatarNumero(to.getCustoMensal()));

				to.setPercentualHomologadoCurrency(formatarNumero(to.getPercentualHomologado()));
				to.setPercentualHomologadoCurrency(to.getPercentualHomologadoCurrency().substring(0, to.getPercentualHomologadoCurrency().length() - 1));

				to.setPercentualLancadoCurrency(formatarNumero(to.getPercentualLancado()));
				to.setPercentualLancadoCurrency(to.getPercentualLancadoCurrency().substring(0, to.getPercentualLancadoCurrency().length() - 1));

				logger.info(String.format("CLs processados: %d/%d - %s", itens[1], itens[0],to.getIdentificadorFinal()));

				itens[1] = itens[1] + 1;
			});

			logger.debug(String.format("Total de CLs processados: %d", cls.size()));

			List<CentroFinanceiroTO> list = cls
				.stream()
				.filter(centroFinanceiroTO -> centroFinanceiroTO.getPercentualHomologado().doubleValue() > limite || centroFinanceiroTO.getPercentualLancado().doubleValue() > limite)
				.collect(Collectors.toList());

			logger.info(String.format("%d CLs acima do limite de %f", list.size(), limite));

			logger.debug("Lista de CLs processadas.");

			list.sort(Comparator.comparing(CentroFinanceiroTO::getIdentificador));
			list.sort((o1, o2) -> (o1.getPercentualLancado().compareTo(o2.getPercentualLancado())) * -1);
			list.sort((o1, o2) -> (o1.getPercentualHomologado().compareTo(o2.getPercentualHomologado())) * -1);
			list.sort(Comparator.comparing(CentroFinanceiroTO::getNome));

			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String formatarNumero(BigDecimal valor) {
		// FIXME: Deve ser usado replace?
		return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor.doubleValue()).replace("R$", "").trim();
	}

	@Override
	@Transactional
	public void criarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		CentroFinanceiroTO centroFinanceiroTO = centroFinanceiroFechoTO.getCentroFinanceiro();
		FechoTO fechoTO = centroFinanceiroFechoTO.getFecho();
		List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquotaTO = centroFinanceiroFechoTO.getListaCentroFinanceiroAliquota();

		// atualiza centro financeiro
		CentroFinanceiro centroFinanceiro = getRepository().findById(centroFinanceiroTO.getCodigo()).orElseThrow(EntityNotFoundException::new);

		fechoService.criarFecho(fechoTO, centroFinanceiro.getCodigo());

		// salvar/atualizar tributos
		salvarAtualizarListaCentroFinanceiroAliquota(listaCentroFinanceiroAliquotaTO, centroFinanceiro.getCodigo());

		// apaga  lista de CentroFinanceiroFluxoEtapa associados ao centro financeiro
		List<CentroFinanceiroFluxoEtapa> lista = centroFinanceiroFluxoEtapaRepository.findByCentroFinanceiroCodigo(centroFinanceiro.getCodigo());

		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElseThrow(EntityNotFoundException::new);

		if (lista.size() == 1) {
			CentroFinanceiroFluxoEtapa centroFinanceiroFluxoEtapa = lista.get(0);
			centroFinanceiroFluxoEtapa.setColaborador(colaborador);
			centroFinanceiroFluxoEtapa.setDataRealizacao(new Date());
			centroFinanceiroFluxoEtapaRepository.save(centroFinanceiroFluxoEtapa);
		} else {
			for (CentroFinanceiroFluxoEtapa item : lista) {
				if (item.getEtapaFluxo().getCodigo() == 1) {
					item.setColaborador(colaborador);
					item.setDataRealizacao(new Date());
					centroFinanceiroFluxoEtapaRepository.save(item);
				} else {
					centroFinanceiroFluxoEtapaRepository.delete(item);
				}
			}
		}

		centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO);

		// salva/atualiza centro financeiro
		getRepository().save(centroFinanceiro);

		// Atualizar o valor do contrato ao atualizar o Fecho.
		contratoService.atualizarCustoMensalEhTotal(centroFinanceiro.getContrato().getCodigo());
	}

	@Override
	@Transactional
	public void atualizarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		CentroFinanceiroTO centroFinanceiroTO = centroFinanceiroFechoTO.getCentroFinanceiro();
		FechoTO fechoTO = centroFinanceiroFechoTO.getFecho();
		List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquotaTO = centroFinanceiroFechoTO.getListaCentroFinanceiroAliquota();

		CentroFinanceiro centroFinanceiro = getRepository().findById(centroFinanceiroTO.getCodigo()).orElseThrow(EntityNotFoundException::new);

		// salvar/atualizar tributos
		salvarAtualizarListaCentroFinanceiroAliquota(listaCentroFinanceiroAliquotaTO, centroFinanceiro.getCodigo());

		fechoService.atualizarFecho(fechoTO);
	}

	@Transactional
	private void salvarAtualizarListaCentroFinanceiroAliquota(List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquotaTO, Long codigoCentroFinanceiro) throws ServiceException {
		List<CentroFinanceiroAliquota> listaNova = new ArrayList<>();

		List<CentroFinanceiroAliquotaTO> listaOriginal = null;

		try {
			listaOriginal = centroFinanceiroAliquotaService.getListByCodCentroFinanceiro(String.valueOf(codigoCentroFinanceiro));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}

		Set<Long> codigosNotaLista = new HashSet<>();

		for (CentroFinanceiroAliquotaTO to : listaCentroFinanceiroAliquotaTO) {
			CentroFinanceiroAliquota cfa = getMapper().map(to, CentroFinanceiroAliquota.class);
			listaNova.add(cfa);

			if (cfa.getCodigo() != null) {
				codigosNotaLista.add(cfa.getCodigo());
			}
		}

		// deletar itens
		for (CentroFinanceiroAliquotaTO to : listaOriginal) {
			if (!codigosNotaLista.contains(to.getCodigo())) {
				centroFinanceiroAliquotaRepository.deleteById(to.getCodigo());
			}
		}

		CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(codigoCentroFinanceiro).orElseThrow(EntityNotFoundException::new);

		for (CentroFinanceiroAliquota cfa : listaNova) {
			if (cfa.getCodigo() != null) {
				CentroFinanceiroAliquota cfaOriginal = centroFinanceiroAliquotaRepository.findById(cfa.getCodigo()).orElse(null);
				if (!cfa.getAliquota().getCodigo().equals(cfaOriginal.getAliquota().getCodigo())) {
					Aliquota aliquota = aliquotaRepository.findById(cfa.getAliquota().getCodigo()).orElse(null);
					cfaOriginal.setAliquota(aliquota);
					cfaOriginal.setAtualizador(securityService.getUsername());
					cfaOriginal.setDataAtualizacao(new Date());
					centroFinanceiroAliquotaRepository.save(cfaOriginal);
				}

			} else {
				Aliquota aliquota = aliquotaRepository.findById(cfa.getAliquota().getCodigo()).orElse(null);

				cfa.setCentroFinanceiro(centroFinanceiro);
				cfa.setAliquota(aliquota);
				cfa.setPercentualBaseCalculo(BigDecimal.ZERO);
				cfa.setCriador(securityService.getUsername());
				cfa.setDataCriacao(new Date());

				centroFinanceiroAliquotaRepository.save(cfa);
			}
		}
	}

	@Override
	@Transactional
	public void salvarAlterarFecho(CentroFinanceiroFechoTO centroFinanceiroFechoTO) {
		CentroFinanceiroTO centroFinanceiroTO = centroFinanceiroFechoTO.getCentroFinanceiro();
		FechoTO fechoTO = centroFinanceiroFechoTO.getFecho();
		List<CentroFinanceiroAliquotaTO> listaCentroFinanceiroAliquotaTO = centroFinanceiroFechoTO.getListaCentroFinanceiroAliquota();

		// atualiza centro financeiro
		CentroFinanceiro centroFinanceiro = getRepository().findById(centroFinanceiroTO.getCodigo()).orElse(null);
//		centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO);

		// cria novo fecho
		fechoTO.setIndiceRajuste("Fecho alterado");
		fechoService.criarReajuste(fechoTO);

		//Só é feito assinatura quando o Centro Financeiro estiver na situação 'Ativo'
		if (CentroFinanceiro.SituacaoCentroFinanceiro.ATIVO.equals(centroFinanceiro.getSituacao())) {

			// apaga  lista de CentroFinanceiroFluxoEtapa associados ao centro financeiro
			List<CentroFinanceiroFluxoEtapa> lista = centroFinanceiroFluxoEtapaRepository.findByCentroFinanceiroCodigo(centroFinanceiro.getCodigo());

			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElseThrow(EntityNotFoundException::new);

			if (lista.isEmpty()) {
				// cria um unico CentroFinanceiroFluxoEtapa para associar ao CentroFinanceiro
				CentroFinanceiroFluxoEtapa centroFinanceiroFluxoEtapa = new CentroFinanceiroFluxoEtapa();
				centroFinanceiroFluxoEtapa.setCentroFinanceiro(centroFinanceiro);
				centroFinanceiroFluxoEtapa.setEtapaFluxo(etapaFluxoRepository.findById(1L).orElse(null));

				centroFinanceiroFluxoEtapa.setColaborador(colaborador);

				centroFinanceiroFluxoEtapa.setDataRealizacao(new Date());
				centroFinanceiroFluxoEtapaRepository.save(centroFinanceiroFluxoEtapa);

			} else if (lista.size() == 1) {
				CentroFinanceiroFluxoEtapa centroFinanceiroFluxoEtapa = lista.get(0);
				centroFinanceiroFluxoEtapa.setColaborador(colaborador);
				centroFinanceiroFluxoEtapa.setDataRealizacao(new Date());
				centroFinanceiroFluxoEtapaRepository.save(centroFinanceiroFluxoEtapa);

			} else {
				for (CentroFinanceiroFluxoEtapa item : lista) {
					if (item.getEtapaFluxo().getCodigo() == 1) {
						item.setColaborador(colaborador);
						item.setDataRealizacao(new Date());
						centroFinanceiroFluxoEtapaRepository.save(item);
					} else {
						centroFinanceiroFluxoEtapaRepository.delete(item);
					}
				}
			}

			centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.EM_ELABORACAO);
		}

		// salvar/atualizar tributos
		salvarAtualizarListaCentroFinanceiroAliquota(listaCentroFinanceiroAliquotaTO, centroFinanceiro.getCodigo());

		// salva/atualiza centro financeiro
		getRepository().save(centroFinanceiro);
	}

	@Override
	public List<CentroFinanceiroTO> getCentroFinanceiroPerfilFaturamento() {
		List<CentroFinanceiro> cfs = new ArrayList<>();
		String[] nomePerfil = {"admin-contrato", "comercial-cns-geral", "comercial-cns-gerencia"};
		List<String> listaNomesPerfis = securityService.getUser().getPerfis().stream().map(Perfil::getNome).collect(Collectors.toList());

		if (listaNomesPerfis.contains(nomePerfil[0]) || listaNomesPerfis.contains(nomePerfil[1])) {
			return this.findAllGerencias();
		}
		else if (listaNomesPerfis.contains(nomePerfil[2])) {
			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElseThrow(EntityNotFoundException::new);
			for (ColaboradorCentroFinanceiro ccf: colaborador.getCentrosFinanceiros()) {
				if (ccf.getCentroFinanceiro().getPai() == null)
					cfs.add(ccf.getCentroFinanceiro());
			}
			Comparator<CentroFinanceiro> comparator = (cf1, cf2) -> cf1.getNome().compareToIgnoreCase(cf2.getNome());
			cfs.sort(comparator);
			return map(cfs);
		}

		return new ArrayList<>();

	}
}
