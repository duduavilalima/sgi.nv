package br.com.araujoabreu.sgi.service.impl.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterfaceNota;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceFaturamentoService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceNotaService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfacePesquisaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceTO;
import br.com.araujoabreu.sgi.service.nota.NotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.FileBean;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.anexo.AnexoRepository;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import br.com.fasolti.sgi.repository.loteInterface.LoteInterfaceRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceFaturamentoRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceNotaRepository;
import br.com.fasolti.sgi.repository.nota.NotaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.security.UsuarioRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoteInterfaceFaturamentoServiceImpl extends BaseSearchService<LoteInterface, LoteInterfaceTO, LoteInterfacePesquisaTO, LoteInterfaceFaturamentoRepository> implements LoteInterfaceFaturamentoService {

	public static final String FATURAMENTO_INTERFACE_ANEXO_DIRETORIO = "faturamento.interface.anexo.diretorio";
	public static final String FATURAMENTO_INTERFACE_OITELEMAR_CNPJ = "faturamento.interface.oitelemar.cnpj";
	public static final String VAR_LIB_SGI_INTERFACE = "/var/lib/sgi/interface/";
	public static final String FILE_SEPARATOR = "file.separator";
	private static final QLoteInterface query = QLoteInterface.loteInterface;
	private static String path = "/var/lib/sgi/interface/";
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(LoteInterfaceFaturamentoServiceImpl.class);

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private AnexoRepository anexoRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private LoteInterfaceNotaRepository loteInterfaceNotaRepository;

	@Autowired
	private LoteInterfaceRepository loteInterfaceRepository;

	@Autowired
	private NotaRepository notaRepository;

	@Autowired
	private NotaService notaService;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private LoteInterfaceNotaService loteInterfaceNotaService;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	public ContaContabilRepository contaContabilRepository;

	@Autowired
	public LoteInterfaceFaturamentoServiceImpl(LoteInterfaceFaturamentoRepository repository) {
		super(repository);
	}

	/**
	 * Retorna um enum TipoLoteInterface relativo ao nome passado por parâmetro.
	 * FIXME: E verificar outro modo de identificar o TipoLoteInterface ao invés do seu nome.
	 *
	 * @param nomeTipoLote
	 * @return
	 */
	public static LoteInterface.TipoLoteInterface getTipoLoteByNome(String nomeTipoLote) {

		LoteInterface.TipoLoteInterface tipoLote = null;

		switch (nomeTipoLote) {

			case "FATN - Faturamento emitido no período":
				tipoLote = LoteInterface.TipoLoteInterface.FATN;
				break;

			case "Retenção de INSS folha onerada (SSNI)":
				tipoLote = LoteInterface.TipoLoteInterface.SEFIP;
				break;
			case "Retenção de INSS folha desonerada (INSS)":
				tipoLote = LoteInterface.TipoLoteInterface.Baixas_diarias;
				break;

			case "DSFP - 2% sobre faturamento (desoneração da folha de pagamento)":
				tipoLote = LoteInterface.TipoLoteInterface.DSFP;
				break;

			case "FATC - Cancelamentos fora do período":
				tipoLote = LoteInterface.TipoLoteInterface.FATC;
				break;

			case "Retenção de IRRF (IRRF)":
				tipoLote = LoteInterface.TipoLoteInterface.SISTEMA_FISCAL;
				break;

			case "Boletos (Itaú)":
				tipoLote = LoteInterface.TipoLoteInterface.Boletos;
				break;

			case "Nota carioca":
				tipoLote = LoteInterface.TipoLoteInterface.Nota_Carioca;
				break;

			case "Sistema Fiscal (PIS/COFINS/ISS)":
				tipoLote = LoteInterface.TipoLoteInterface.ISS_PIS_COFINS_CSLL;
				break;

			case "Portal Oi":
				tipoLote = LoteInterface.TipoLoteInterface.Portal_Oi;
				break;

			case "Nota Norte (Belém)":
				tipoLote = LoteInterface.TipoLoteInterface.NOTA_NORTE;
				break;

			case "Nota Paulistana":
				tipoLote = LoteInterface.TipoLoteInterface.NOTA_PAULISTANA;
				break;

			case "Nota BH":
				tipoLote = LoteInterface.TipoLoteInterface.NOTA_BH;
				break;

			case "Nota Brasília":
				tipoLote = LoteInterface.TipoLoteInterface.NOTA_BRASILIA;
				break;

			case "NF/Material":
				tipoLote = LoteInterface.TipoLoteInterface.NF_MATERIAL;
				break;

			default:
				tipoLote = null;
				break;
		}

		return tipoLote;
	}

	@Override
	protected void buildSearchQuery(JPAQuery<LoteInterface> jpaQuery, LoteInterfacePesquisaTO exampleObj) {
		QBean<LoteInterface> qBean = Projections.fields(
			LoteInterface.class,
			QLoteInterface.loteInterface.codigo,
			QLoteInterface.loteInterface.situacao,
			Projections.fields(Unidade.class, QUnidade.unidade.codigo, QUnidade.unidade.codnome, QUnidade.unidade.nome, QUnidade.unidade.nomeFantasia).as("unidade"),
			QLoteInterface.loteInterface.tipoLoteInterface,
			QLoteInterface.loteInterface.dataEvento,
			QLoteInterface.loteInterface.criador,
			QLoteInterface.loteInterface.dataCriacao
		);

		jpaQuery.select(qBean).orderBy(QLoteInterface.loteInterface.codigo.desc()).innerJoin(QLoteInterface.loteInterface.unidade,
			QUnidade.unidade);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, LoteInterfacePesquisaTO exampleObj) {
		builder.and(query.tipoLoteInterface.in(
			LoteInterface.TipoLoteInterface.Boletos,
			LoteInterface.TipoLoteInterface.Portal_Oi,
			LoteInterface.TipoLoteInterface.Nota_Carioca,
			LoteInterface.TipoLoteInterface.NOTA_NORTE,
			LoteInterface.TipoLoteInterface.NOTA_PAULISTANA,
			LoteInterface.TipoLoteInterface.NOTA_BH,
			LoteInterface.TipoLoteInterface.NOTA_BRASILIA,
			LoteInterface.TipoLoteInterface.NF_MATERIAL));

		LoteInterface lc = getMapper().map(exampleObj, LoteInterface.class);

		if (Check.isAssigned(exampleObj.getUnidade()) && Check.isAssigned(exampleObj.getUnidade().getCodigo())) {
			builder.and(query.unidade.codigo.eq(exampleObj.getUnidade().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getCodigo()))
			builder.and(query.codigo.eq(exampleObj.getCodigo()));

		if (Check.isAssigned(exampleObj.getSituacao()))
			builder.and(query.situacao.eq(lc.getSituacao()));

		if (Check.isAssigned(exampleObj.getTipoLoteInterface()))
			builder.and(query.tipoLoteInterface.eq(lc.getTipoLoteInterface()));

		if (Check.isAssigned(exampleObj.getDataExecucaoDe()))
			builder.and(query.dataCriacao.goe(exampleObj.getDataExecucaoDe()));

		if (Check.isAssigned(exampleObj.getDataExecucaoAte()))
			builder.and(query.dataCriacao.loe(exampleObj.getDataExecucaoAte()));

		if (Check.isAssigned(exampleObj.getDataEmissaoDe()))
			builder.and(query.dataEvento.goe(exampleObj.getDataEmissaoDe()));

		if (Check.isAssigned(exampleObj.getDataEmissaoAte()))
			builder.and(query.dataEvento.loe(exampleObj.getDataEmissaoAte()));

		if (Check.isAssigned(exampleObj.getDataCancelamentoDe()))
			builder.and(query.dataEvento.goe(exampleObj.getDataCancelamentoDe()));

		if (Check.isAssigned(exampleObj.getDataCancelamentoAte()))
			builder.and(query.dataEvento.loe(exampleObj.getDataCancelamentoAte()));
	}

	@Override
	protected void doAfterSearch(List<LoteInterface> entities, List<LoteInterfacePesquisaTO> result) throws ServiceException {
		for (LoteInterfacePesquisaTO to : result) {
			to.setCriador(pessoaFisicaService.nomeUsuarioPorLogin(to.getCriador()));
			String codnome = unidadeService.obterCodNomeUnidade(to.getUnidade().getCodigo());
			to.getUnidade().setCodnome(codnome);
			if (to.getSituacao().equals("Importado na contabilidade") && to.getTipoLoteInterface().equals("Nota carioca")) {
				to.setSituacao("Importado na Prefeitura");
			}
		}
	}

	@Override
	protected OrderSpecifier defineSearchOrder(LoteInterfacePesquisaTO exampleObj) {
		QLoteInterface qLoteInterface = QLoteInterface.loteInterface;
		return (new OrderSpecifier<>(Order.ASC, qLoteInterface.codigo));
	}

	@Override
	protected LoteInterfaceTO doAfterCreate(LoteInterfaceTO object) {
		//ao clicar em Novo, para a tela de edição
		object.setSituacao(LoteInterface.SituacaoLoteInterface.CRIADO.toString());
		object.setDataCriacao(null);
		List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(securityService.getUsername());
		if (list != null && !list.isEmpty()) {
			object.setCriador(list.get(0).getNome());
		}
		return object;
	}

	@Override
	protected void doAfterLoad(LoteInterfaceTO transferObj) {
		//após pesquisar, somente para exibição na grid de resultados
		if (transferObj.getCriador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(transferObj.getCriador());
			if (list != null && !list.isEmpty()) {
				transferObj.setCriador(list.get(0).getNome());
			}
		}

		Unidade unidade = unidadeRepository.findById(transferObj.getUnidade().getCodigo()).orElse(null);
		transferObj.setUnidade(new UnidadeTO(unidade.getCodigo(), unidade.getCodnome()));
	}

	@Override
	protected void doBeforeSave(LoteInterface loteInterface, LoteInterfaceTO loteInterfaceTO) {
		prepareValidateToSave(loteInterfaceTO);
		verificarParametrosRepetidos(loteInterfaceTO);

		//após persistir
		loteInterface.setCriador(securityService.getUsername());
		loteInterface.setDataCriacao(new Date());

		if (loteInterface.getTipoLoteInterface() == LoteInterface.TipoLoteInterface.DSFP) {
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.setTime(loteInterfaceTO.getDataEvento());
			dataInicio.set(Calendar.DAY_OF_MONTH, dataInicio.getActualMinimum(Calendar.DAY_OF_MONTH));
			loteInterface.setDataEvento(dataInicio.getTime());
		}
	}

	@Override
	protected void doBeforeUpdate(LoteInterface entity, LoteInterfaceTO loteInterfaceTO) throws ServiceException {
		prepareValidateToSave(loteInterfaceTO);
		loteInterfaceTO.setDataCriacao(new Date());
	}

	/**
	 * fixme: refatorar
	 *
	 * @param entity A entidade persistida.
	 * @param object
	 * @throws Exception
	 */
	@Transactional
	@Override
	protected void doAfterSave(LoteInterface entity, LoteInterfaceTO object) {
		if (LoteInterface.TipoLoteInterface.Portal_Oi.equals(entity.getTipoLoteInterface())) {

			Calendar dataInicio = Calendar.getInstance();
			Calendar dataFim = Calendar.getInstance();

			//dataInicio = Calendar.getInstance();
			dataInicio.setTime(entity.getDataEvento());
			dataInicio.set(Calendar.HOUR_OF_DAY, dataInicio.getActualMinimum(Calendar.HOUR_OF_DAY));
			dataInicio.set(Calendar.MINUTE, dataInicio.getActualMinimum(Calendar.MINUTE));
			dataInicio.set(Calendar.SECOND, dataInicio.getActualMinimum(Calendar.SECOND));
			dataInicio.set(Calendar.MILLISECOND, dataInicio.getActualMinimum(Calendar.MILLISECOND));

			//calendar = Calendar.getInstance();
			dataFim.setTime(entity.getDataEvento());
			dataFim.set(Calendar.HOUR_OF_DAY, dataFim.getActualMaximum(Calendar.HOUR_OF_DAY));
			dataFim.set(Calendar.MINUTE, dataFim.getActualMaximum(Calendar.MINUTE));
			dataFim.set(Calendar.SECOND, dataFim.getActualMaximum(Calendar.SECOND));
			dataFim.set(Calendar.MILLISECOND, dataFim.getActualMaximum(Calendar.MILLISECOND));

			List<Long> listaCodNotaSaida = getRepository().listaCodNotaSaidaLotePortalOi(dataInicio.getTime(), dataFim.getTime(), Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.ordinal(), this.strCnpjsPortalOi());

			for (Long codNotaSaida : listaCodNotaSaida) {
				List<LoteInterfaceNotaTO> listaINotaTO = loteInterfaceNotaService.findByCodNotaTipo(codNotaSaida, 2L);

				if (listaINotaTO == null || listaINotaTO.isEmpty()) {
					LoteInterfaceNota loteInterfaceNota = new LoteInterfaceNota();
					loteInterfaceNota.setLoteInterface(entity);
					loteInterfaceNota.setTipo(LoteInterfaceNota.Tipo.PORTAL_OI);
					loteInterfaceNota.setNota(new Nota());
					loteInterfaceNota.getNota().setCodigo(codNotaSaida);

					loteInterfaceNotaRepository.save(loteInterfaceNota);
				}
			}
		}

		super.doAfterSave(entity, object);
	}

	public void prepareValidateToSave(LoteInterfaceTO loteInterfaceTO) throws ServiceException {
		// Validações antes de persistir (edição)
		if (loteInterfaceTO.getTipoLoteInterface() == null)
			throw new ValidationException("ValidationException","Tipo Lote Interface é um campo obrigatório");

		if (loteInterfaceTO.getUnidade() == null || loteInterfaceTO.getUnidade().getCodigo() == null)
			throw new ValidationException("ValidationException","Unidade é um campo obrigatório");

		if (loteInterfaceTO.getDataEvento() == null) {
			throw new ValidationException("ValidationException","Data de Emissão é um campo obrigatório");
		}
	}

	/**
	 * Verifica se já existe algum lote com os mesmos parametros.
	 * Os parametros verificados são: tipo, codigo_unidade e data_evento da tabela de lote_interface.
	 *
	 * @param loteInterfaceTO
	 * @throws Exception
	 */
	private void verificarParametrosRepetidos(LoteInterfaceTO loteInterfaceTO) throws ServiceException {

		LoteInterface.TipoLoteInterface tipoLote = getTipoLoteByNome(loteInterfaceTO.getTipoLoteInterface());

		if (tipoLote != null) {

			Date dataEvento = loteInterfaceTO.getDataEvento();
			UnidadeTO unidadeTO = loteInterfaceTO.getUnidade();

			//Pesquisa no banco
			QBean qBean = Projections.bean(
				LoteInterface.class,
				query.codigo
			);

			JPAQuery<LoteInterface> jpaQueryLoteInterface = select();
			List<LoteInterface> listaLote = Collections.emptyList();

			if (!LoteInterface.TipoLoteInterface.Nota_Carioca.equals(tipoLote) && !LoteInterface.TipoLoteInterface.Portal_Oi.equals(tipoLote)) {
				jpaQueryLoteInterface
					.select(qBean)
					.from(query)
					.where(
						query.unidade.codigo.eq(unidadeTO.getCodigo())
							.and(query.dataEvento.eq(dataEvento))
							.and(query.tipoLoteInterface.eq(tipoLote))
					);

				listaLote = jpaQueryLoteInterface.fetch();
			}

			if (listaLote != null && !listaLote.isEmpty() && listaLote.size() > 0) {

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				QUnidade qUnidade = QUnidade.unidade;
				JPAQuery jpaQueryUnidade = select();

				QBean qBeanUnidade = Projections.bean(
					Unidade.class,
					qUnidade.codnome
				);

				UnidadeTO unidadeTo = unidadeService.obterUnidade(unidadeTO.getCodigo());
				StringBuilder sb = new StringBuilder();
				sb.append("Já existe o lote de tipo \"")
					.append(tipoLote.getNome())
					.append("\" para ")
					.append(sdf.format(dataEvento))
					.append(" da empresa \"")
					.append(unidadeTo.getCodnome())
					.append("\".");

				throw new ValidationException("ValidationException",sb.toString());
			}

			if (LoteInterface.TipoLoteInterface.Portal_Oi.equals(tipoLote)) {
				verificarPortalOi(loteInterfaceTO);
			}
		}
	}

	//fixme: refatorar
	private void verificarPortalOi(LoteInterfaceTO loteInterfaceTO) throws ServiceException {

		Calendar dataInicio = Calendar.getInstance();
		Calendar dataFim = Calendar.getInstance();

		//dataInicio = Calendar.getInstance();
		dataInicio.setTime(loteInterfaceTO.getDataEvento());
		dataInicio.set(Calendar.HOUR_OF_DAY, dataInicio.getActualMinimum(Calendar.HOUR_OF_DAY));
		dataInicio.set(Calendar.MINUTE, dataInicio.getActualMinimum(Calendar.MINUTE));
		dataInicio.set(Calendar.SECOND, dataInicio.getActualMinimum(Calendar.SECOND));
		dataInicio.set(Calendar.MILLISECOND, dataInicio.getActualMinimum(Calendar.MILLISECOND));

		//calendar = Calendar.getInstance();
		dataFim.setTime(loteInterfaceTO.getDataEvento());
		dataFim.set(Calendar.HOUR_OF_DAY, dataFim.getActualMaximum(Calendar.HOUR_OF_DAY));
		dataFim.set(Calendar.MINUTE, dataFim.getActualMaximum(Calendar.MINUTE));
		dataFim.set(Calendar.SECOND, dataFim.getActualMaximum(Calendar.SECOND));
		dataFim.set(Calendar.MILLISECOND, dataFim.getActualMaximum(Calendar.MILLISECOND));

		QBean qBean = Projections.bean(
			LoteInterface.class,
			query.codigo
		);

		JPAQuery<LoteInterface> jpaQueryLoteInterface = select();
		List<LoteInterface> listaLote = Collections.emptyList();

		jpaQueryLoteInterface
			.select(qBean)
			.from(query)
			.where(
				query.unidade.codigo.eq(loteInterfaceTO.getUnidade().getCodigo())
					.and(query.dataEvento.eq(loteInterfaceTO.getDataEvento()))
					.and(query.tipoLoteInterface.eq(LoteInterface.TipoLoteInterface.Portal_Oi))
			);

		listaLote = jpaQueryLoteInterface.fetch();

		if (listaLote != null && !listaLote.isEmpty()) {
			List<Long> listaCods = getRepository().listaCodNotaSaidaLotePortalOi(dataInicio.getTime(), dataFim.getTime(), Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.ordinal(), this.strCnpjsPortalOi());

			if (listaCods != null && !listaCods.isEmpty()) {

				int count = 0;

				for (Long codNotaSaida : listaCods) {
					List<LoteInterfaceNotaTO> listaINotaTO = loteInterfaceNotaService.findByCodNotaTipo(codNotaSaida, 2L);

					if (listaINotaTO != null && !listaINotaTO.isEmpty()) {
						count++;
					}
				}

				if (listaCods.size() == count) {
					throw new ValidationException("ValidationException","Não foi possível gerar o lote, pois não há dados suficientes para a geração.");
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public LoteInterfaceTO processarLote(Long id) {
		this.gerarLoteAPartirDeJobs(id);
		return map(loteInterfaceRepository.findById(id).orElse(new LoteInterface()));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public LoteInterfaceTO reprocessarLote(Long id) {
		LoteInterface lote = getRepository().findById(id).orElse(new LoteInterface());
		lote.setSituacao(LoteInterface.SituacaoLoteInterface.CRIADO);
		lote.setDataCriacao(null);

		PessoaFisica pessoaFisica = usuarioSecurityService.getUser().getPessoaFisica();
		lote.setCriador(pessoaFisica.getUsuario().getLogin());

		lote = getRepository().save(lote);

		if (LoteInterface.TipoLoteInterface.Portal_Oi.equals(lote.getTipoLoteInterface())) {
			reprocessarLotePortalOi(lote);
		}
		else if (LoteInterface.TipoLoteInterface.NF_MATERIAL.equals(lote.getTipoLoteInterface())) {
			reprocessarLoteNFMaterila(lote);
		}

		return map(lote);
	}

	@Transactional
	private void reprocessarLoteNFMaterila(LoteInterface lote) {
		List<LoteInterfaceNota> listaLIN = loteInterfaceNotaRepository.findNotaByCodigoLote(lote.getCodigo());

		for (LoteInterfaceNota loteInterfaceNota : listaLIN) {
			NotaSaidaTO notaSaidaTO = getMapper().map(loteInterfaceNota.getNota(), NotaSaidaTO.class);
			loteInterfaceNotaService.reprocessarNfEmRecebimentoFaturamento(notaSaidaTO);
			loteInterfaceNotaRepository.deleteById(loteInterfaceNota.getCodigo());
		}
	}
	//fixme: refatorar
	@Transactional
	private void reprocessarLotePortalOi(LoteInterface lote) {
		List<LoteInterfaceNota> listaLIN = loteInterfaceNotaRepository.findNotaByCodigoLote(lote.getCodigo());

		for (LoteInterfaceNota loteInterfaceNota : listaLIN) {
			loteInterfaceNotaRepository.deleteById(loteInterfaceNota.getCodigo());
		}

		Calendar dataInicio = Calendar.getInstance();
		Calendar dataFim = Calendar.getInstance();

		//dataInicio = Calendar.getInstance();
		dataInicio.setTime(lote.getDataEvento());
		dataInicio.set(Calendar.HOUR_OF_DAY, dataInicio.getActualMinimum(Calendar.HOUR_OF_DAY));
		dataInicio.set(Calendar.MINUTE, dataInicio.getActualMinimum(Calendar.MINUTE));
		dataInicio.set(Calendar.SECOND, dataInicio.getActualMinimum(Calendar.SECOND));
		dataInicio.set(Calendar.MILLISECOND, dataInicio.getActualMinimum(Calendar.MILLISECOND));

		//calendar = Calendar.getInstance();
		dataFim.setTime(lote.getDataEvento());
		dataFim.set(Calendar.HOUR_OF_DAY, dataFim.getActualMaximum(Calendar.HOUR_OF_DAY));
		dataFim.set(Calendar.MINUTE, dataFim.getActualMaximum(Calendar.MINUTE));
		dataFim.set(Calendar.SECOND, dataFim.getActualMaximum(Calendar.SECOND));
		dataFim.set(Calendar.MILLISECOND, dataFim.getActualMaximum(Calendar.MILLISECOND));

		List<Long> listaCodNotaSaida = getRepository().listaCodNotaSaidaLotePortalOi(dataInicio.getTime(), dataFim.getTime(), Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.ordinal(), this.strCnpjsPortalOi());

		for (Long codNotaSaida : listaCodNotaSaida) {
			List<LoteInterfaceNotaTO> listaINotaTO = loteInterfaceNotaService.findByCodNotaTipo(codNotaSaida, 2L);

			if (listaINotaTO == null || listaINotaTO.isEmpty()) {
				LoteInterfaceNota loteInterfaceNota = new LoteInterfaceNota();
				loteInterfaceNota.setLoteInterface(lote);
				loteInterfaceNota.setTipo(LoteInterfaceNota.Tipo.PORTAL_OI);

				loteInterfaceNota.setNota(new Nota());
				loteInterfaceNota.getNota().setCodigo(codNotaSaida);

				loteInterfaceNotaRepository.save(loteInterfaceNota);
			}
		}
	}

	@Override
	public void baixarAnexo(String id, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		try {
			List<Anexo> anexo = anexoRepository.findByCodigoLoteInterfaceJoinFetchLotesInterfaces(Long.parseLong(id));

			if (anexo == null || anexo.isEmpty()) {
				throw new FileNotFoundException("Anexo não encontrado");
			} else if (anexo != null && anexo.size() == 1) {
				anexo.get(0).getNome();

				LoteInterface loteInterface = getRepository().findById(Long.parseLong(id)).orElse(null);
				String path = obterCaminhoArquivo(loteInterface);

				File arquivo = new File(path + anexo.get(0).getNome());

				Util.download(arquivo, anexo.get(0).getContentType().trim(), request, response);
			} else if (anexo != null && !anexo.isEmpty()) {
				LoteInterface loteInterface = getRepository().findById(Long.parseLong(id)).orElse(null);
				String path = obterCaminhoArquivo(loteInterface);

				downloadCompactados(loteInterface, path, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public void downloadCompactados(LoteInterface loteInterface, String destino, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Anexo anexoLote = loteInterface.getAnexos().get(0);

		List<FileBean> listFileBean = new ArrayList<>();

		for (Anexo anexo : loteInterface.getAnexos()) {
			listFileBean.add(new FileBean(anexo.getNome().substring(0, anexo.getNome().indexOf(".")), destino, (anexo.getNome().substring(anexo.getNome().indexOf(".") + 1, anexo.getNome().length()))));
		}

		FileBean fileZipe = new FileBean();
		fileZipe.setNome(anexoLote.getNome().substring(0, anexoLote.getNome().indexOf(".")));
		fileZipe.setPastaDestino(destino);
		fileZipe.setExtensao("zip");

		File fileZipCriar = new File(fileZipe.getCaminhoCompleto());
		fileZipCriar.createNewFile();

		File arquivoCompactado = Util.compactarArquivos(listFileBean, fileZipe);

		Util.download(arquivoCompactado, "application/download", request, response);

		fileZipCriar.deleteOnExit();
		arquivoCompactado.deleteOnExit();
	}

	/**
	 * Verifica a existencia do diretório e do arquivo do anexo.
	 * Utilizar este método antes do 'baixarAnexo'.
	 *
	 * @param id
	 * @return
	 */
	@Override
	public void verificarAnexo(String id) throws Exception {

		List<Anexo> anexo = anexoRepository.findByCodigoLoteInterfaceJoinFetchLotesInterfaces(Long.parseLong(id));
		if (anexo == null || anexo.isEmpty()) {
			throw new FileNotFoundException("Anexo não encontrado");
		} else if (anexo != null && !anexo.isEmpty() && anexo.get(0).getNome() != null && !anexo.get(0).getNome().isEmpty()) {
			anexo.get(0).getNome();

			LoteInterface loteInterface = getRepository().findById(Long.parseLong(id)).orElse(null);
			String path = obterCaminhoArquivo(loteInterface);

			File directory = new File(path);
			File arquivo = new File(path + anexo.get(0).getNome());

			if (!directory.exists()) {
				throw new FileNotFoundException("Não foi possível realizar o download do arquivo do lote \"LC/" + loteInterface.getCodigo() + "\". Diretório não encontrado. Caminho: " + path + anexo.get(0).getNome());
			}

			if (!arquivo.exists()) {
				throw new FileNotFoundException("Não foi possível realizar o download do arquivo do lote \"LC/" + loteInterface.getCodigo() + "\". Arquivo não encontrado.  Caminho: " + path + anexo.get(0).getNome());
			}
		}
	}

	@Override
	public List<AnexoTO> getListaAnexo(Long id) {
		List<Anexo> listaAnexo = new ArrayList<>();
		for (Anexo obj : anexoRepository.findByCodigoLoteInterfaceJoinFetchLotesInterfaces(id)) {
			Anexo anexo = new Anexo();
			anexo.setCodigo(obj.getCodigo());
			anexo.setNome(obj.getNome());
			listaAnexo.add(anexo);
		}

		List<AnexoTO> listaAnexoTO = new ArrayList<>();

		for (Anexo anexo : listaAnexo) {
			listaAnexoTO.add(getMapper().map(anexo, AnexoTO.class));
		}

		return listaAnexoTO;
	}

	@Override
	public LoteInterfaceTO qtdLote(LoteInterfaceTO loteInterfaceTO) {
		LoteInterface.TipoLoteInterface tipoLote = getTipoLoteByNome(loteInterfaceTO.getTipoLoteInterface());

		//empresa
		Unidade unidade = unidadeRepository.findById(loteInterfaceTO.getUnidade().getCodigo()).orElse(null);

		//Data de inicio
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(loteInterfaceTO.getDataEvento());
		dataInicio.set(Calendar.DAY_OF_MONTH, dataInicio.getActualMinimum(Calendar.DAY_OF_MONTH));

		//Data fim
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(loteInterfaceTO.getDataEvento());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		Date dataFim = calendar.getTime();

		Long contagem = null;
		List<NotaSaida> listaNotaSaida = new ArrayList<>();
		Page<NotaSaida> paginaNotaSaida = null;

		if (tipoLote == LoteInterface.TipoLoteInterface.FATN) {
			contagem = getRepository().countLoteFATN(
				unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
				Nota.StatusNota.RECEBIDA_FATURAMENTO);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.DSFP) {

			List<LoteInterface> lotes = getRepository().findLoteByPeriodAndTipo(dataInicio.getTime(), dataFim, LoteInterface.TipoLoteInterface.DSFP);

			if (lotes != null && !lotes.isEmpty()) {
				loteInterfaceTO.setQtdLote(0L);
				return loteInterfaceTO;
			}
			contagem = getRepository().countLoteDSFP(unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
				Nota.StatusNota.RECEBIDA_FATURAMENTO);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.Baixas_diarias) {
			contagem = getRepository().countLoteINSS(unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
				Nota.StatusNota.RECEBIDA_FATURAMENTO,
				Nota.StatusNota.CANCELADA_FATURAMENTO);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.FATC) {

			// data do cancelamento com hora ZERO
			Calendar dataInicioCancelamento = Calendar.getInstance();
			dataInicioCancelamento.setTime(loteInterfaceTO.getDataEvento());
			dataInicioCancelamento.set(Calendar.DAY_OF_MONTH, dataInicioCancelamento.getActualMinimum(Calendar.DAY_OF_MONTH));
			dataInicioCancelamento.set(Calendar.HOUR_OF_DAY, 0);

			// data do cancelamento com a ultima hora do dia (23:59:59)
			Calendar dataFimCancelamento = Calendar.getInstance();
			dataFimCancelamento.setTime(loteInterfaceTO.getDataEvento());
			dataFimCancelamento.set(Calendar.DAY_OF_MONTH, dataFimCancelamento.getActualMaximum(Calendar.DAY_OF_MONTH));
			dataFimCancelamento.set(Calendar.HOUR_OF_DAY, dataFimCancelamento.getActualMaximum(Calendar.HOUR_OF_DAY));
			dataFimCancelamento.set(Calendar.MINUTE, dataFimCancelamento.getActualMaximum(Calendar.MINUTE));
			dataFimCancelamento.set(Calendar.SECOND, dataFimCancelamento.getActualMaximum(Calendar.SECOND));
			dataFimCancelamento.set(Calendar.MILLISECOND, dataFimCancelamento.getActualMaximum(Calendar.MILLISECOND));

			//Data do mês anterior (com o ultimo dia do mês)
			Calendar dataFimMesAnterior = Calendar.getInstance();
			dataFimMesAnterior.setTime(loteInterfaceTO.getDataEvento());
			dataFimMesAnterior.set(Calendar.MONTH, dataFimMesAnterior.get(Calendar.MONTH) - 1);
			dataFimMesAnterior.set(Calendar.DAY_OF_MONTH, dataFimMesAnterior.getActualMaximum(Calendar.DAY_OF_MONTH));

			contagem = getRepository().countLoteFATC(unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicioCancelamento.getTime(),
				dataFimCancelamento.getTime(),
				dataFimMesAnterior.getTime());
		} else if (tipoLote == LoteInterface.TipoLoteInterface.Nota_Carioca || tipoLote == LoteInterface.TipoLoteInterface.NOTA_NORTE || tipoLote == LoteInterface.TipoLoteInterface.NOTA_PAULISTANA
			|| tipoLote == LoteInterface.TipoLoteInterface.NOTA_BH || tipoLote == LoteInterface.TipoLoteInterface.NOTA_BRASILIA) {

			dataInicio.setTime(loteInterfaceTO.getDataEvento());
			dataInicio.set(Calendar.HOUR_OF_DAY, 0);
			dataInicio.set(Calendar.MINUTE, 0);
			dataInicio.set(Calendar.SECOND, 0);
			dataInicio.set(Calendar.MILLISECOND, 0);

			calendar.setTime(loteInterfaceTO.getDataEvento());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);

			contagem = getRepository().countLoteNotaCarioca(
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				calendar.getTime(),
				Nota.StatusNota.A_PROCESSAR_FATURAMENTO,
				tipoLote);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.NF_MATERIAL) {

			dataInicio.setTime(loteInterfaceTO.getDataEvento());
			dataInicio.set(Calendar.HOUR_OF_DAY, 0);
			dataInicio.set(Calendar.MINUTE, 0);
			dataInicio.set(Calendar.SECOND, 0);
			dataInicio.set(Calendar.MILLISECOND, 0);

			calendar.setTime(loteInterfaceTO.getDataEvento());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);

			contagem = getRepository().countLoteNFMaterial(
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				Nota.StatusNota.A_PROCESSAR_FATURAMENTO,
				loteInterfaceTO.getUnidade().getCodigo(),
				Faturamento.TipoFornecimentoNF.MATERIAL
				);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.SISTEMA_FISCAL) {

			contagem = getRepository().countLoteIRRF(unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
				Nota.StatusNota.RECEBIDA_FATURAMENTO,
				Nota.StatusNota.CANCELADA_FATURAMENTO);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.Boletos) {
			Long banco = 341L;

			Calendar inicio = Calendar.getInstance();
			inicio.setTime(loteInterfaceTO.getDataEvento());
			inicio.set(Calendar.HOUR_OF_DAY, inicio.getActualMinimum(Calendar.HOUR_OF_DAY));
			inicio.set(Calendar.MINUTE, inicio.getActualMinimum(Calendar.MINUTE));
			inicio.set(Calendar.SECOND, inicio.getActualMinimum(Calendar.SECOND));
			inicio.set(Calendar.MILLISECOND, inicio.getActualMinimum(Calendar.MILLISECOND));

			// data do cancelamento com a ultima hora do dia (23:59:59)
			Calendar fim = Calendar.getInstance();
			fim.setTime(loteInterfaceTO.getDataEvento());
			fim.set(Calendar.HOUR_OF_DAY, fim.getActualMaximum(Calendar.HOUR_OF_DAY));
			fim.set(Calendar.MINUTE, fim.getActualMaximum(Calendar.MINUTE));
			fim.set(Calendar.SECOND, fim.getActualMaximum(Calendar.SECOND));
			fim.set(Calendar.MILLISECOND, fim.getActualMaximum(Calendar.MILLISECOND));

			contagem = getRepository().countLoteBoletoItau(banco,
				NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO,
				unidade.getCodigo(),
				inicio.getTime(),
				fim.getTime(),
				Pagamento.TipoDocumentoPagamento.BOLETO);
		} else if (tipoLote == LoteInterface.TipoLoteInterface.SEFIP) {

			contagem = getRepository().countLoteSSNI(unidade,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
				Nota.StatusNota.RECEBIDA_FATURAMENTO,
				Nota.StatusNota.CANCELADA_FATURAMENTO);

		} else if (tipoLote == LoteInterface.TipoLoteInterface.ISS_PIS_COFINS_CSLL) {
			contagem = verificaSistemaFiscal(unidade, dataInicio, dataFim);
		} else if (tipoLote == LoteInterface.TipoLoteInterface.Portal_Oi) {

			/*Prefixos de CNPJ para clientes do portal Oi:
			 * 76.535.764/%, 04.014.081/%, 07.953.678/%, 33.000.118/%, 05.423.963/% */

			//dataInicio = Calendar.getInstance();
			dataInicio.setTime(loteInterfaceTO.getDataEvento());
			dataInicio.set(Calendar.HOUR_OF_DAY, dataInicio.getActualMinimum(Calendar.HOUR_OF_DAY));
			dataInicio.set(Calendar.MINUTE, dataInicio.getActualMinimum(Calendar.MINUTE));
			dataInicio.set(Calendar.SECOND, dataInicio.getActualMinimum(Calendar.SECOND));
			dataInicio.set(Calendar.MILLISECOND, dataInicio.getActualMinimum(Calendar.MILLISECOND));

			//calendar = Calendar.getInstance();
			calendar.setTime(loteInterfaceTO.getDataEvento());
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

			dataFim = calendar.getTime();

			contagem = getRepository().countLotePortalOi(dataInicio.getTime(),
				dataFim,
				Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO.ordinal(),
				this.strCnpjsPortalOi());

		}

		Long qtdLote = contagem != null ? contagem
			: paginaNotaSaida != null ? Long.valueOf(paginaNotaSaida.getTotalElements()).intValue()
			: listaNotaSaida.size();
		loteInterfaceTO.setQtdLote(qtdLote);

		if (qtdLote == null) {
			loteInterfaceTO.setQtdLote(0L);
		}

		return loteInterfaceTO;
	}

	private String strCnpjsPortalOi() {
		String cnpjsOi = parametroRepository.findByChave(FATURAMENTO_INTERFACE_OITELEMAR_CNPJ).getValor();
		String str = "(" + cnpjsOi.replaceAll(",", "|") + ")%";
		return "(" + cnpjsOi.replaceAll(",", "|") + ")%";
	}

	public boolean isNotaPrefeituraOuMaterial(LoteInterface loteInterface) {
		switch (loteInterface.getTipoLoteInterface()) {
			case Nota_Carioca:
			case NOTA_NORTE:
			case NOTA_BRASILIA:
			case NOTA_PAULISTANA:
			case NF_MATERIAL:
			case NOTA_BH:
				return Boolean.TRUE;
			default:
				return Boolean.FALSE;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void gerarLoteAPartirDeJobs(Long codigoLote) {
		LoteInterface loteInterface = loteInterfaceRepository.findById(codigoLote).orElse(null);
		if (loteInterface != null){
			if(isNotaPrefeituraOuMaterial(loteInterface)){
				gerarLoteNotaFiscalMaterial(loteInterface);
			}
		}
	}

	@Transactional
	private void gerarLoteNotaFiscalMaterial(LoteInterface loteInterface){
		boolean contemErros   = false;

		LoteInterface.TipoLoteInterface tipoLote = getTipoLoteByNome(loteInterface.getTipoLoteInterface().toString());

		//empresa
		Unidade unidade = unidadeRepository.findById(loteInterface.getUnidade().getCodigo()).orElse(null);

		//Data de inicio
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(loteInterface.getDataEvento());
		dataInicio.set(Calendar.DAY_OF_MONTH, dataInicio.getActualMinimum(Calendar.DAY_OF_MONTH));

		//Data fim
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(loteInterface.getDataEvento());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		Date dataFim = calendar.getTime();

		List<NotaSaida> listaNotaSaida = new ArrayList<>();
		Page<NotaSaida> paginaNotaSaida = null;

		try{
			String msg = "%s geração de lote de Nota %s: LT/%d, período - %s a %s, empresa - %s(%d)";
			dataInicio.setTime(loteInterface.getDataEvento());
			dataInicio.set(Calendar.HOUR_OF_DAY, 0);
			dataInicio.set(Calendar.MINUTE, 0);
			dataInicio.set(Calendar.SECOND, 0);
			dataInicio.set(Calendar.MILLISECOND, 0);

			listaNotaSaida = getRepository().selectJobNotaFiscalMaterial(
				dataInicio.getTime(),
				Nota.StatusNota.A_PROCESSAR_FATURAMENTO,
				NotaFiscal.TipoNotaFiscal.FATURAMENTO,
				loteInterface.getUnidade().getCodigo(),
				Faturamento.TipoFornecimentoNF.MATERIAL
				);

			if(listaNotaSaida != null){
				int ordem = 0;
				for (NotaFiscal nf : listaNotaSaida) {
					loteInterfaceNotaService.atualizarNfEmRecebimentoFaturamento(getMapper().map(nf, NotaFiscalTO.class));
					LoteInterfaceNota loteInterfaceNota = new LoteInterfaceNota();
					loteInterfaceNota.setLoteInterface(loteInterface);
					loteInterfaceNota.setNota(nf);
					loteInterfaceNota.setTipo(LoteInterfaceNota.Tipo.ENVIADA);
					loteInterfaceNota.setOrdem(ordem++);
					loteInterfaceNotaRepository.save(loteInterfaceNota);
				}
			}

		}catch (Exception e){
			e.printStackTrace();
			logger.error("Erro ao processar lote Nota Carioca", e);
			contemErros = true;
		}

		if (contemErros) {
			loteInterfaceRepository.updateStatus(loteInterface.getCodigo(), loteInterface.getSituacao());
		} else {
			loteInterfaceRepository.updateStatus(loteInterface.getCodigo(), LoteInterface.SituacaoLoteInterface.GERADO);
		}
	}

	/**
	 * Obter o caminho do diretório do anexo.
	 * Seguindo os padrões so SGI Legado.
	 *
	 * @param loteInterface
	 * @return
	 */
	private String obterCaminhoArquivo(LoteInterface loteInterface) {
		// A pasta do lote será por ano e mês.
		String dir = null;

		LoteInterface.TipoLoteInterface tipo = loteInterface.getTipoLoteInterface();

		if (LoteInterface.TipoLoteInterface.Custos.equals(tipo)) {
			dir = GlobalBean.formatarData(loteInterface.getDataEvento(), "yyyyMM");
		} else if (LoteInterface.TipoLoteInterface.DIRF.equals(tipo)) {
			dir = GlobalBean.formatarData(loteInterface.getDataEvento(), "yyyy");
		} else {
			dir = GlobalBean.formatarData(loteInterface.getDataEvento(), "yyyyMMdd");
		}

		String caminhoPrincipal = null;

		try {
			// Obter o caminho definido no parâmetro.
			caminhoPrincipal = parametroRepository.findByChave(FATURAMENTO_INTERFACE_ANEXO_DIRETORIO).getValor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Para garantir que o processo não pare, obter o caminho padrão.
		if (caminhoPrincipal == null) {
			caminhoPrincipal = VAR_LIB_SGI_INTERFACE;
		}

		String caminhoFinal = null;
		if (LoteInterface.TipoLoteInterface.DIRF.equals(tipo)) {
			// Obter o caminho completo, exemplo: "/var/lib/sgi/interfaces/anexo/2015"
			caminhoFinal = caminhoPrincipal + dir;
		} else {
			// Obter o caminho completo, exemplo: "/var/lib/sgi/interfaces/anexo/20150115"
			if (caminhoPrincipal.endsWith(System.getProperty(FILE_SEPARATOR)))
				caminhoFinal = caminhoPrincipal + dir + System.getProperty(FILE_SEPARATOR);
			else
				caminhoFinal = caminhoPrincipal + System.getProperty(FILE_SEPARATOR) + dir + System.getProperty(FILE_SEPARATOR);
		}

//		return "/home/FASOLTI/daniel.oliveira/interface/";
		return caminhoFinal;
	}

	@Override
	@Transactional
	public void confirmarEnvio(Long id) {
		List<LoteInterfaceNota> loteInterfaceNotaList = loteInterfaceNotaRepository.findNotaByCodigoLote(id);
		Colaborador colaborador = (Colaborador) usuarioSecurityService.getUser().getPessoaFisica();

		for (LoteInterfaceNota loteInterfaceNota : loteInterfaceNotaList) {
			notaService.updateStatusByLoteInterfaceNotaEmRecebimentoFaturamento(loteInterfaceNota.getCodigo());
			notaSaidaParcelaRepository.updateStatusByLoteInterfaceNota(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO, loteInterfaceNota.getCodigo());
		}

		LoteInterface loteInterface = loteInterfaceRepository.findById(id).orElse(null);
		loteInterface.setSituacao(LoteInterface.SituacaoLoteInterface.IMPORTADO);
		loteInterface.setDataImportacao(new Date());
		loteInterface.setColaborador(colaborador);
		loteInterfaceRepository.save(loteInterface);
	}

	/**
	 * Verifica se existe dados para geração do lote do tipo ISS, COFINS e PIS.
	 * Caso um desses não possua dados, não é possível gerar o lote e retorna o valor 0.
	 *
	 * @param unidade
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private Long verificaSistemaFiscal(Unidade unidade, Calendar dataInicio, Date dataFim) {

		Long countNotaSaidaISS = getRepository().countLoteISS(unidade,
			NotaFiscal.TipoNotaFiscal.FATURAMENTO,
			dataInicio.getTime(),
			dataFim,
			Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
			Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
			Nota.StatusNota.RECEBIDA_FATURAMENTO,
			Nota.StatusNota.CANCELADA_FATURAMENTO);

		Long countNotaSaidaCOFINS = getRepository().countLoteCOFINS(unidade,
			NotaFiscal.TipoNotaFiscal.FATURAMENTO,
			dataInicio.getTime(),
			dataFim,
			Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
			Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
			Nota.StatusNota.RECEBIDA_FATURAMENTO,
			Nota.StatusNota.CANCELADA_FATURAMENTO);

		Long countNotaSaidaPIS = getRepository().countLotePIS(unidade,
			NotaFiscal.TipoNotaFiscal.FATURAMENTO,
			dataInicio.getTime(),
			dataFim,
			Nota.StatusNota.EM_PROCESSAMENTO_FATURAMENTO,
			Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO,
			Nota.StatusNota.RECEBIDA_FATURAMENTO,
			Nota.StatusNota.CANCELADA_FATURAMENTO);

		if (countNotaSaidaISS > 0 && countNotaSaidaCOFINS > 0 && countNotaSaidaPIS > 0) {
			return (countNotaSaidaISS + countNotaSaidaCOFINS + countNotaSaidaPIS);
		} else {
			return 0L;
		}
	}

	@Override
	public List<NotaTO> getNotasFiscaisLotePrefeitura(String id) {
		Long codigo = Long.valueOf(id);

		List<LoteInterfaceNota> lista = loteInterfaceNotaService.findNotaByCodigoLoteReturnJPAQuery(codigo)
			.fetch();

		List<NotaTO> listaNotas = new ArrayList<>();

		for (LoteInterfaceNota loteInterfaceNota : lista) {
			NotaTO notaTO = new NotaTO();
			notaTO.setCodigo(loteInterfaceNota.getNota().getCodigo());
			if (LoteInterfaceNota.Tipo.BOLETO_ITAU.equals(loteInterfaceNota.getTipo())) {
				notaTO.setNumero(String.format("%s-%d", loteInterfaceNota.getNota().getNumero(),loteInterfaceNota.getOrdem()));

			} else {
				notaTO.setNumero(loteInterfaceNota.getNota().getNumero());
			}
			notaTO.setCliente(obterClienteSubclienteNotaSaida(loteInterfaceNota.getNota().getCodigo()));

			CentroFinanceiroTO centroFinanceiroTO = new CentroFinanceiroTO();
			centroFinanceiroTO.setCodigo(loteInterfaceNota.getNota().getCentroFinanceiro().getCodigo());
			centroFinanceiroTO.setIdentificador(loteInterfaceNota.getNota().getCentroFinanceiro().getIdentificador());
			centroFinanceiroTO.setIdentificadorFinal(loteInterfaceNota.getNota().getCentroFinanceiro().getIdentificadorFinal());
			centroFinanceiroTO.setNome(loteInterfaceNota.getNota().getCentroFinanceiro().getNome());

			notaTO.setCentroFinanceiro(centroFinanceiroTO);
			notaTO.setValor(loteInterfaceNota.getNota().getValor());

			listaNotas.add(notaTO);
		}
		listaNotas.sort(Comparator.comparing(NotaTO::getNumero));
		return listaNotas;
	}

	private PessoaTO obterClienteSubclienteNotaSaida(Long codigoNota) {
		FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.obterFaturamentoEtapaPorNotaSaida2(codigoNota);
		return faturamentoEtapaTO == null ? null : faturamentoEtapaTO.getFaturamento().getCliente();
	}

	@Override
	public List<NotaSaidaTO> getNotasPorTipoPrefeitura(LoteInterfaceTO loteInterfaceTO) {
		//Data de inicio
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(loteInterfaceTO.getDataEvento());
		dataInicio.set(Calendar.DAY_OF_MONTH, dataInicio.getActualMinimum(Calendar.DAY_OF_MONTH));

		//Data fim
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(loteInterfaceTO.getDataEvento());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		dataInicio.setTime(loteInterfaceTO.getDataEvento());
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);

		calendar.setTime(loteInterfaceTO.getDataEvento());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		if(loteInterfaceTO.getTipoLoteInterface().equals("NF/Material")){
			List<NotaSaidaTO> listTO = notaSaidaService.obterNotasDeFaturamentoNFMaterial(dataInicio.getTime(), calendar.getTime(), loteInterfaceTO.getTipoLoteInterface());
			if (!listTO.isEmpty() && listTO.size() > 1) {
				boolean intervalosGrandes = false;
				NotaSaidaTO notaSaidaTOAnterior = null;

				for (NotaSaidaTO notaSaidaTO : listTO) {
					if (notaSaidaTOAnterior != null) {
						long numero = Long.parseLong(notaSaidaTO.getNumero().replace("NF/", ""));
						long numeroAnterior = Long.parseLong(notaSaidaTOAnterior.getNumero().replace("NF/", ""));
						if ((numero - numeroAnterior) > 1) {
							intervalosGrandes = true;
							notaSaidaTO.setLinhaVermelha(true);
						}
					}
					notaSaidaTOAnterior = notaSaidaTO;
				}

				if (!intervalosGrandes) {
					Long primeiroNumero = Long.valueOf(listTO.get(0).getNumero().replace("NF/", ""));
					Long ultimoNumero = Long.valueOf(listTO.get(listTO.size() - 1).getNumero().replace("NF/", ""));
					Map<Long, NotaSaidaTO> map = new TreeMap<>();
					for (long x = primeiroNumero; x <= ultimoNumero; x++) {
						map.put(x, new NotaSaidaTO());
					}
					for (Long numero : map.keySet()) {
						for (NotaSaidaTO notaSaidaTO : listTO) {
							if (numero.equals(Long.valueOf(notaSaidaTO.getNumero().replace("NF/", "")))) {
								map.put(numero, notaSaidaTO);
								break;
							}
						}
					}
					listTO = new ArrayList<>();
					for (Long numero : map.keySet()) {
						NotaSaidaTO notaSaidaTO = map.get(numero);
						if (notaSaidaTO.getCodigo() == null) {
							notaSaidaTO.setTextoVermelha(true);
							notaSaidaTO.setNumero(String.format("NF/%09d", numero));
						}
						listTO.add(notaSaidaTO);
					}
				}
			}

			return listTO;
		} else if(loteInterfaceTO.getTipoLoteInterface().equals("Nota Norte (Belém)")){
			List<NotaSaidaTO> listTO = notaSaidaService.obterNotasDeFaturamentoNotaNorteBelem(dataInicio.getTime(), calendar.getTime(), loteInterfaceTO.getTipoLoteInterface());
			if (!listTO.isEmpty() && listTO.size() > 1) {
				boolean intervalosGrandes = false;
				NotaSaidaTO notaSaidaTOAnterior = null;

				for (NotaSaidaTO notaSaidaTO : listTO) {
					if (notaSaidaTOAnterior != null) {
						long numero = Long.parseLong(notaSaidaTO.getNumero().replace("NF/", ""));
						long numeroAnterior = Long.parseLong(notaSaidaTOAnterior.getNumero().replace("NF/", ""));
						if ((numero - numeroAnterior) > 1) {
							intervalosGrandes = true;
							notaSaidaTO.setLinhaVermelha(true);
						}
					}
					notaSaidaTOAnterior = notaSaidaTO;
				}

				if (!intervalosGrandes) {
					Long primeiroNumero = Long.valueOf(listTO.get(0).getNumero().replace("NF/", ""));
					Long ultimoNumero = Long.valueOf(listTO.get(listTO.size() - 1).getNumero().replace("NF/", ""));
					Map<Long, NotaSaidaTO> map = new TreeMap<>();
					for (long x = primeiroNumero; x <= ultimoNumero; x++) {
						map.put(x, new NotaSaidaTO());
					}
					for (Long numero : map.keySet()) {
						for (NotaSaidaTO notaSaidaTO : listTO) {
							if (numero.equals(Long.valueOf(notaSaidaTO.getNumero().replace("NF/", "")))) {
								map.put(numero, notaSaidaTO);
								break;
							}
						}
					}
					listTO = new ArrayList<>();
					for (Long numero : map.keySet()) {
						NotaSaidaTO notaSaidaTO = map.get(numero);
						if (notaSaidaTO.getCodigo() == null) {
							notaSaidaTO.setTextoVermelha(true);
							notaSaidaTO.setNumero(String.format("NF/%09d", numero));
						}
						listTO.add(notaSaidaTO);
					}
				}
			}

			return listTO;
		} else {
			List<NotaSaidaTO> listTO = notaSaidaService.obterNotasDeFaturamentoPorTipoPrefeitura(dataInicio.getTime(), calendar.getTime(), loteInterfaceTO.getTipoLoteInterface());
			if (!listTO.isEmpty() && listTO.size() > 1) {
				boolean intervalosGrandes = false;
				NotaSaidaTO notaSaidaTOAnterior = null;

				for (NotaSaidaTO notaSaidaTO : listTO) {
					if (notaSaidaTOAnterior != null) {
						long numero = Long.parseLong(notaSaidaTO.getNumero().replace("NF/", ""));
						long numeroAnterior = Long.parseLong(notaSaidaTOAnterior.getNumero().replace("NF/", ""));
						if ((numero - numeroAnterior) > 1) {
							intervalosGrandes = true;
							notaSaidaTO.setLinhaVermelha(true);
						}
					}
					notaSaidaTOAnterior = notaSaidaTO;
				}

				if (!intervalosGrandes) {
					Long primeiroNumero = Long.valueOf(listTO.get(0).getNumero().replace("NF/", ""));
					Long ultimoNumero = Long.valueOf(listTO.get(listTO.size() - 1).getNumero().replace("NF/", ""));
					Map<Long, NotaSaidaTO> map = new TreeMap<>();
					for (long x = primeiroNumero; x <= ultimoNumero; x++) {
						map.put(x, new NotaSaidaTO());
					}
					for (Long numero : map.keySet()) {
						for (NotaSaidaTO notaSaidaTO : listTO) {
							if (numero.equals(Long.valueOf(notaSaidaTO.getNumero().replace("NF/", "")))) {
								map.put(numero, notaSaidaTO);
								break;
							}
						}
					}
					listTO = new ArrayList<>();
					for (Long numero : map.keySet()) {
						NotaSaidaTO notaSaidaTO = map.get(numero);
						if (notaSaidaTO.getCodigo() == null) {
							notaSaidaTO.setTextoVermelha(true);
							notaSaidaTO.setNumero(String.format("NF/%09d", numero));
						}
						listTO.add(notaSaidaTO);
					}
				}
			}

			return listTO;
		}

	}

	@Override
	public Boolean notaFiscalDisponivelParaLoteFATN(Date dataEmissao, Long codigoMatrizUnidade) {
		JPAQuery jpaQuery = select();

		QBean qBean = Projections.fields(
			LoteInterface.class,
			query.codigo
		);

		Calendar inicio = Calendar.getInstance();
		inicio.setTime(dataEmissao);
		inicio.set(Calendar.DAY_OF_MONTH, inicio.getActualMinimum(Calendar.DAY_OF_MONTH));
		inicio.set(Calendar.HOUR_OF_DAY, inicio.getActualMinimum(Calendar.HOUR_OF_DAY));
		inicio.set(Calendar.MINUTE, inicio.getActualMinimum(Calendar.MINUTE));
		inicio.set(Calendar.SECOND, inicio.getActualMinimum(Calendar.SECOND));
		inicio.set(Calendar.MILLISECOND, inicio.getActualMinimum(Calendar.MILLISECOND));

		Calendar fim = Calendar.getInstance();
		fim.setTime(dataEmissao);
		fim.set(Calendar.DAY_OF_MONTH, fim.getActualMaximum(Calendar.DAY_OF_MONTH));
		fim.set(Calendar.HOUR_OF_DAY, fim.getActualMaximum(Calendar.HOUR_OF_DAY));
		fim.set(Calendar.MINUTE, fim.getActualMaximum(Calendar.MINUTE));
		fim.set(Calendar.SECOND, fim.getActualMaximum(Calendar.SECOND));
		fim.set(Calendar.MILLISECOND, fim.getActualMaximum(Calendar.MILLISECOND));

		jpaQuery.select(qBean)
			.from(query)
			.where(query.dataEvento.between(inicio.getTime(), fim.getTime())
				.and(query.unidade.codigo.eq(codigoMatrizUnidade))
				.and(query.tipoLoteInterface.eq(LoteInterface.TipoLoteInterface.FATN))
			);

		if (jpaQuery.fetchCount() > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Boolean notaFiscalDisponivelParaLoteBaixasDiarias(Date data, Long codigoMatrizUnidade) {
		JPAQuery jpaQuery = select();

		QBean qBean = Projections.fields(
			LoteInterface.class,
			query.codigo
		);

		jpaQuery.select(qBean)
			.from(query)
			.where(query.dataEvento.eq(data)
				.and(query.unidade.codigo.eq(codigoMatrizUnidade))
				.and(query.tipoLoteInterface.eq(LoteInterface.TipoLoteInterface.Baixas_diarias))
			);

		if (jpaQuery.fetchCount() > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}
}
