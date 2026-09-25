package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoNota;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.enums.TipoDocumentoPagamentoPesquisa;
import br.com.araujoabreu.sgi.administracao.enums.TipoInformacaoNumeroBoleto;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.compras.domain.QNotaPagamento;
import br.com.araujoabreu.sgi.compras.domain.QParcelaNota;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.PagamentoContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.compras.NotaPagamentoService;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.araujoabreu.sgi.service.tesouraria.BoletoService;
import br.com.araujoabreu.sgi.service.tesouraria.MovimentoAutorizadoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.tesouraria.util.ConcessionariaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.GuiaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.BoletoUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.pessoa.domain.QDadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoContaContabilRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.contasApagar.ParcelaNotaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoNotaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import br.com.fasolti.sgi.repository.tributo.TributoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MovimentoAutorizadoServiceImpl extends BaseSearchService<Pagamento, MovimentoAutorizadoTO, MovimentoAutorizadoPesquisaTO, PagamentoRepository> implements MovimentoAutorizadoService {

	private static final QPagamento qpagamento = QPagamento.pagamento;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ContaContabilRepository contaContabilRepository;

	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;

	@Autowired
	private AliquotaRepository aliquotaRepository;

	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;

	@Autowired
	private TributoRepository tributoRepository;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoRetornoNotaRepository motivoRetornoNotaRepository;

	@Autowired
	private MotivoRetornoService motivoRetornoService;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoContaContabilRepository pagamentoContaContabilRepository;

	@Autowired
	private ParcelaNotaRepository parcelaNotaRepository;

	@Autowired
	private NotaPagamentoService notaPagamentoService;

	@Autowired
	public MovimentoAutorizadoServiceImpl(PagamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Pagamento> jpaQuery, MovimentoAutorizadoPesquisaTO exampleObj) {
		NumberPath<BigDecimal> aliasQuantity = Expressions.numberPath(BigDecimal.class, "quantity");

		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QPessoa qPessoa = QPessoa.pessoa;
		QNota qNota = QNota.nota;

		jpaQuery.distinct()
			.leftJoin(qpagamento.centroFinanceiro, qCentroFinanceiro)
			.leftJoin(qpagamento.as(QParcelaNota.class).nota, qNota)
			.leftJoin(qpagamento.credor, qPessoa)
			.where(qpagamento.status.eq(Pagamento.Status.PAGAMENTO));


		if (exampleObj.getBanco() != null) {
			QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
			QBanco qBanco = QBanco.banco;

			jpaQuery.leftJoin(qpagamento.dadosBancarios, qDadosBancarios)
				.leftJoin(qDadosBancarios.banco, qBanco);
		}

		jpaQuery.leftJoin(qpagamento.listaNotaTributoAliquota, QNotaTributoAliquota.notaTributoAliquota);

		if (exampleObj.getEmpresa() != null && exampleObj.getEmpresa().getCodigo() != null) {
			jpaQuery.leftJoin(QNotaTributoAliquota.notaTributoAliquota.nota, QNota.nota);

			QPagamento qPagamentoTA = new QPagamento("pagamentoTR");
			QNotaTributoAliquota qNotaTributoAliquotaTA = new QNotaTributoAliquota("listaNotaTributoAliquotaTR");
			QNota qNotaTR = new QNota("notaTR");

			jpaQuery.leftJoin(qpagamento.listaPagamento, qPagamentoTA);
			jpaQuery.leftJoin(qPagamentoTA.listaNotaTributoAliquota, qNotaTributoAliquotaTA);
			jpaQuery.leftJoin(qNotaTributoAliquotaTA.nota, qNotaTR);
		}

		if (Check.isNotEmpty(exampleObj.getTributoPesquisa())) {
			QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
			QAliquota qAliquota = QAliquota.aliquota;
			QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
			QTributo qTributo = QTributo.tributo;

			jpaQuery
				.join(qNotaTributoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.tributo, qTributo);
		}

		jpaQuery.select(Projections.fields(Pagamento.class,
			qpagamento.codigo,
			qpagamento.numero,
			qpagamento.dataPagamento,
			qpagamento.dataVencimento,
			qpagamento.tipo,
			qpagamento.tipoDocumentoPagamento,
			qpagamento.valor.subtract(qpagamento.valorAdiantamento.coalesce(BigDecimal.ZERO)).subtract(qpagamento.valorDesconto.coalesce(BigDecimal.ZERO)).add(qpagamento.valorJuros.coalesce(BigDecimal.ZERO)).add(qpagamento.valorMulta.coalesce(BigDecimal.ZERO)).as(aliasQuantity),
			qpagamento.valorAdiantamento,
			qpagamento.valorDesconto,
			qpagamento.valorDevolucao,
			qpagamento.valorJuros,
			qpagamento.valorMulta,
			Projections.fields(Pessoa.class, qpagamento.credor.codigo, qpagamento.credor.nome, qpagamento.credor.tipoPessoa).as("credor"),
			Projections.fields(Gerencia.class, qpagamento.centroFinanceiro.codigo, qpagamento.centroFinanceiro.nome, qpagamento.centroFinanceiro.situacao, qpagamento.centroFinanceiro.identificadorFinal, qpagamento.centroFinanceiro.identificador).as("centroFinanceiro")
		));

		jpaQuery.orderBy(aliasQuantity.desc(), qpagamento.numero.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MovimentoAutorizadoPesquisaTO filtroTO) {
		if (Check.isAssigned(filtroTO.getEmpresa()) && filtroTO.getEmpresa().getCodigo() != null) {
			builder.or(qpagamento.as(QParcelaNota.class).nota.empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()));
			builder.or(QNota.nota.empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()));
			builder.or(new QNota("notaTR").empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()));
		}

		if (Check.isNotEmpty(filtroTO.getTipoDocumentoPagamento())) {

			if (filtroTO.getTipoDocumentoPagamento().equals(TipoDocumentoPagamentoPesquisa.BOLETO_VENCER.getDescricao())) {

				builder.and(qpagamento.tipoDocumentoPagamento.eq(Pagamento.TipoDocumentoPagamento.BOLETO));
				builder.and(qpagamento.dataPagamento.after(new Date()));

			} else if (filtroTO.getTipoDocumentoPagamento().equals(TipoDocumentoPagamentoPesquisa.BOLETO_VENCIDO.getDescricao())) {

				builder.and(qpagamento.tipoDocumentoPagamento.eq(Pagamento.TipoDocumentoPagamento.BOLETO));
				builder.and(qpagamento.dataPagamento.before(new Date()));

			} else if (filtroTO.getTipoDocumentoPagamento().equals(TipoDocumentoPagamentoPesquisa.BOLETOS.getDescricao())) {

				builder.and(qpagamento.tipoDocumentoPagamento.eq(Pagamento.TipoDocumentoPagamento.BOLETO))
					.and(qpagamento.dataPagamento.after(new Date()).or(qpagamento.dataPagamento.before(new Date())));

			} else {

				for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {

					if (tipoDocumentoPagamento.getDescricao().equalsIgnoreCase(filtroTO.getTipoDocumentoPagamento())) {
						builder.and(qpagamento.tipoDocumentoPagamento.eq(tipoDocumentoPagamento));
					}
				}
			}

		}

		if (filtroTO.getContaContabil() != null) {
			QParcelaNota qParcelaNota = QPagamento.pagamento.as(QParcelaNota.class);
			QNotaPagamento qNotaPagamento = qParcelaNota.nota.as(QNotaPagamento.class);
			builder.and(qNotaPagamento.contaContabil.codigo.eq(filtroTO.getContaContabil().getCodigo()));
		}

		if (Check.isNotEmpty(filtroTO.getPagamentoDe()) && !Check.isNotEmpty(filtroTO.getPagamentoAte())) {
			builder.and(
				qpagamento.dataPagamento.after(filtroTO.getPagamentoDe())
					.or(qpagamento.dataPagamento.eq(filtroTO.getPagamentoDe()))
			);
		} else if (Check.isNotEmpty(filtroTO.getPagamentoAte()) && !Check.isNotEmpty(filtroTO.getPagamentoDe())) {
			builder.and(qpagamento.dataPagamento.before(filtroTO.getPagamentoAte()));
		} else if (Check.isNotEmpty(filtroTO.getPagamentoDe()) && Check.isNotEmpty(filtroTO.getPagamentoAte())) {
			builder.and(qpagamento.dataPagamento.between(filtroTO.getPagamentoDe(), filtroTO.getPagamentoAte()));
		}

		if (Check.isNotEmpty(filtroTO.getGerencia()) && Check.isNotEmpty(filtroTO.getGerencia().getNome())) {
			builder.and(qpagamento.centroFinanceiro.nome.eq(filtroTO.getGerencia().getNome()));
		}

		if (Check.isNotEmpty(filtroTO.getNumero())) {
			builder.and(qpagamento.numero.containsIgnoreCase(filtroTO.getNumero()));
		}

		if (Check.isNotEmpty(filtroTO.getNomeCredor())) {
			QPessoaJuridica qPessoaJuridica = QPessoa.pessoa.as(QPessoaJuridica.class);
			builder.andAnyOf(
				qPessoaJuridica.nome.containsIgnoreCase(filtroTO.getNomeCredor()),
				qPessoaJuridica.documento.containsIgnoreCase(filtroTO.getNomeCredor()),
				qPessoaJuridica.nomeFantasia.containsIgnoreCase(filtroTO.getNomeCredor())
			);
		}

		if (Check.isNotEmpty(filtroTO.getBanco()) && Check.isNotEmpty(filtroTO.getBanco().getCodigo())) {
			QBanco qBanco = QBanco.banco;
			builder.and(qBanco.codigo.eq(filtroTO.getBanco().getCodigo()));
		}

		if (Check.isNotEmpty(filtroTO.getTributoPesquisa())) {
			QTributo qTributo = QTributo.tributo;

			List<Tributo> tributos = new ArrayList<>();
			for (TributoTO tributoTO : filtroTO.getTributoPesquisa()) {
				tributos.add(getMapper().map(tributoTO, Tributo.class));
			}

			builder.and(qTributo.in(tributos));
		}

		if (Check.isAssigned(filtroTO.getValorDe())) {
			builder.and(qpagamento.valor.goe(filtroTO.getValorDe()));
		}

		if (Check.isAssigned(filtroTO.getValorAte())) {
			builder.and(qpagamento.valor.loe(filtroTO.getValorAte()));
		}
	}

	@Override
	protected void doAfterSearch(List<Pagamento> entities, List<MovimentoAutorizadoPesquisaTO> result) throws ServiceException {
		if (result.size() > 0) {
			for (MovimentoAutorizadoPesquisaTO to : result) {

				if (Pagamento.TipoPagamento.TRIBUTO.toString().equalsIgnoreCase(to.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equalsIgnoreCase(to.getTipo())) {
					TributoTO tributoTO = new TributoTO();
					tributoTO.setNome(pagamentoService.obterNomeTributo(to.getCodigo(), to.getTipo()));
					to.setTributoExibicao(tributoTO);
				} else {
					QPagamento qPagamento = QPagamento.pagamento;
					QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
					QBanco qBanco = QBanco.banco;

					Pagamento pg = getJPAQueryFactory().selectFrom(qPagamento)
						.join(qpagamento.dadosBancarios, qDadosBancarios)
						.join(qDadosBancarios.banco, qBanco)
						.where(qPagamento.codigo.eq(to.getCodigo()))
						.select(Projections.bean(
							Pagamento.class,
							Projections.bean(DadosBancarios.class, qpagamento.dadosBancarios.codigo, qpagamento.dadosBancarios.tipoConta,
								Projections.bean(Banco.class, qpagamento.dadosBancarios.banco.codigo, qpagamento.dadosBancarios.banco.nome).as("banco")).as("dadosBancarios")
						)).fetchOne();
					if (pg != null) {
						to.setDadosBancarios(map(pg).getDadosBancarios());
					}
				}


				to.setValorPagar(pagamentoService.obterValorPagar(to.getCodigo(), to.getTipo()));
				to.setCodigoNota(pagamentoService.getCodigoNota(to.getCodigo()));
			}
		}
	}

	/**
	 * Obter o tributo de um pagamento.
	 *
	 * @param codigo O código do pagamento.
	 * @return O tributo associado a nota tributo alíquota / categoria tributo / tributo.
	 */
	public TributoTO obterTributo(Long codigo, String tipo) {
		NotaTributoAliquota notaTributoAliquota = null;

		if (Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equalsIgnoreCase(tipo)) {
			List<Pagamento> listaPagamentos = (List<Pagamento>) pagamentoService.findByTributoAcumuladoCodigo(codigo).fetch();
			if (!listaPagamentos.isEmpty()) {
				for (Pagamento pagamento : listaPagamentos) {
					notaTributoAliquota = notaTributoAliquotaRepository.findByPagamentoCodigo(pagamento.getCodigo());
					if (notaTributoAliquota != null) {
						break;
					}
				}
			}
		} else if (Pagamento.TipoPagamento.TRIBUTO.toString().equalsIgnoreCase(tipo)) {
			notaTributoAliquota = notaTributoAliquotaRepository.findByPagamentoCodigo(codigo);
		}

		if (notaTributoAliquota != null) {
			Aliquota aliquota = aliquotaRepository.findById(notaTributoAliquota.getAliquota().getCodigo()).orElse(null);
			if (aliquota != null) {
				CategoriaTributo categoriaTributo = categoriaTributoRepository.findById(aliquota.getCategoriaTributo().getCodigo()).orElse(null);
				if (categoriaTributo != null) {
					Tributo tributo = tributoRepository.findById(categoriaTributo.getTributo().getCodigo()).orElse(null);
					if (tributo != null) {
						return getMapper().map(tributo, TributoTO.class);
					}
				}
			}
		}

		return null;
	}

	@Override
	protected void buildLoadQuery(JPAQuery<Pagamento> jpaQuery) {
		QPessoa qPessoa = QPessoa.pessoa;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		QBanco qBanco = QBanco.banco;
		QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);
		jpaQuery.select(Projections.fields(Pagamento.class,
			qpagamento.codigo,
			qpagamento.numero,
			qpagamento.dataPagamento,
			qpagamento.tipo,
			qpagamento.tipoDocumentoPagamento,
			qpagamento.valor,
			qpagamento.valorAdiantamento,
			qpagamento.valorDesconto,
			qpagamento.valorDevolucao,
			qpagamento.valorJuros,
			qpagamento.data,
			qpagamento.dataVencimento,
			qpagamento.linhaDigitavel,
			qpagamento.codigoBarras,
			qpagamento.valorMulta,
			Projections.fields(
				DadosBancarios.class,
				Projections.fields(PessoaJuridica.class,
					qPessoaJuridica.codigo,
					qPessoaJuridica.nome,
					qPessoaJuridica.nomeFantasia,
					qPessoaJuridica.documento).as("pessoa"),
				Projections.fields(Banco.class,
					qpagamento.dadosBancarios.banco.codigo, qpagamento.dadosBancarios.banco.nome).as("banco"),
				qpagamento.dadosBancarios.codigo,
				qpagamento.dadosBancarios.agencia,
				qpagamento.dadosBancarios.digitoAgencia,
				qpagamento.dadosBancarios.conta,
				qpagamento.dadosBancarios.digito,
				qpagamento.dadosBancarios.principal,
				qpagamento.dadosBancarios.homologado,
				qpagamento.dadosBancarios.dataHomologacao,
				qpagamento.dadosBancarios.tipoHomologacao,
				qpagamento.dadosBancarios.tipoConta).as("dadosBancarios"),
			Projections.fields(Pessoa.class,
				qpagamento.credor.codigo,
				qpagamento.credor.nome,
				qpagamento.credor.tipoPessoa).as("credor"),
			Projections.fields(Gerencia.class,
				qpagamento.centroFinanceiro.codigo,
				qpagamento.centroFinanceiro.nome,
				qpagamento.centroFinanceiro.situacao,
				qpagamento.centroFinanceiro.identificadorFinal,
				qpagamento.centroFinanceiro.identificador).as("centroFinanceiro")
		))
			.leftJoin(qpagamento.credor, qPessoa)
			.leftJoin(qpagamento.dadosBancarios, qDadosBancarios)
			.leftJoin(qpagamento.dadosBancarios.banco, qBanco)
			.leftJoin(qpagamento.centroFinanceiro, qCentroFinanceiro);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void doAfterLoad(MovimentoAutorizadoTO to) { // Tenha cuidado ao mexer Aqui!
		to.setValor_liquido(pagamentoService.obterValorLiquido(to.getCodigo(), to.getTipo()));
		to.setValorAPagar(pagamentoService.obterValorPagar(to.getCodigo(), to.getTipo()));
		to.setTotalOutrosDescontos(pagamentoService.getTotalOutrosDescontos(to.getCodigo()));

		if (to.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO.toString())) {
			to.setValorTributo(notaPagamentoService.obterValorTibutoDeNotaPagamentoPeloNumero(to.getNumero()));
			to.setTributoMaisOutrosDescNP(notaPagamentoService.obterValorTibutoDeNotaPagamentoPeloNumero(to.getNumero()).add(to.getTotalOutrosDescontos()));
		} else {
			to.setValorTributo(pagamentoService.calcularTotalAcumulado(to.getCodigo(), to.getTipo()));
			to.setTributoMaisOutrosDescNP(pagamentoService.calcularTotalAcumulado(to.getCodigo(), to.getTipo()).add(to.getTotalOutrosDescontos()));
		}

		if (Pagamento.TipoPagamento.TRIBUTO.toString().equalsIgnoreCase(to.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equalsIgnoreCase(to.getTipo())) {
			Pagamento pagamento = getRepository().findByCodigo(to.getCodigo());
			to.setTributoExibicao(obterTributo(to.getCodigo(), to.getTipo()));
			to.setValorTributo(pagamentoService.calcularTotalAcumulado(pagamento.getCodigo(), to.getTipo()));

		} else if (Pagamento.TipoPagamento.NOTA_FISCAL.toString().equalsIgnoreCase(to.getTipo())) {
			ParcelaNota parcelaNota = parcelaNotaRepository.findByCodigo(to.getCodigo());
			// Essas Regras só precisa quando o "Pagamento" for do Tipo "Nota Fiscal"
			List<ParcelaNota> parcelaNotas = parcelaNotaRepository.findByNotaCodigo(parcelaNota.getNota().getCodigo());

			if (Check.isNotEmpty(parcelaNotas)) {
				parcelaNotas.sort((o1, o2) -> o1.getOrdem().compareTo(o2.getOrdem()));

				ParcelaNota parcelaNota1 = parcelaNotas.get(parcelaNotas.size() - 1);
				PagamentoTO pagamentoto = new PagamentoTO();
				pagamentoto.setCodigo(parcelaNota1.getCodigo());
				pagamentoto.setDataVencimento(parcelaNota1.getDataVencimento());
				pagamentoto.setDataPagamento(parcelaNota1.getDataPagamento());
				pagamentoto.setValor(parcelaNota.getValor());
				pagamentoto.setTipoDocumentoPagamento(parcelaNota1.getTipoDocumentoPagamento().name());
				pagamentoto.setOrdenacao(String.valueOf(parcelaNota1.getOrdem()));

				to.setUltimaParcela(pagamentoto);
			}
		} else {
			ParcelaNota parcelaNota = parcelaNotaRepository.findByCodigo(to.getCodigo());
			to.setValorTributo(pagamentoService.calcularTotalAcumulado(parcelaNota.getCodigo(), parcelaNota.getTipo().toString()));
		}

		// É usado na Dialog de Conta Contábil
		List<PagamentoContaContabil> listaPagamento = pagamentoContaContabilRepository.findPagamentoContaContabilByPagamento(to.getCodigo());

		if (listaPagamento != null && !listaPagamento.isEmpty()) {
			List<PagamentoContaContabilTO> listaTO = new ArrayList<>();

			for (PagamentoContaContabil pcc : listaPagamento) {

				PagamentoContaContabilTO pagamentoContaContabilTO = new PagamentoContaContabilTO();
				pagamentoContaContabilTO.setCodigo(pcc.getCodigo());
				pagamentoContaContabilTO.setTipo(pcc.getTipo().getDescricao());

				PagamentoTO pagamentoTO = new PagamentoTO();
				pagamentoTO.setCodigo(pcc.getPagamento().getCodigo());

				ContaContabil contaContabil = contaContabilRepository.findById(pcc.getContaContabil().getCodigo()).orElse(null);

				ContaContabilTO contaContabilTO = getMapper().map(contaContabil, ContaContabilTO.class);

				pagamentoContaContabilTO.setPagamento(pagamentoTO);
				pagamentoContaContabilTO.setContaContabil(contaContabilTO);

				listaTO.add(pagamentoContaContabilTO);
			}

			to.setListaPgtoCC(listaTO);
		}

	} // fim do método "doAfterLoad"

	@Override
	@Transactional
	public void cancelar(@RequestBody Long codigo) {
		String chave = "tesoraria_canc-pg";

		//TODO Deveria verificar a validade do cast
		Colaborador colaborador = (Colaborador) securityService.getUser().getPessoaFisica();

		MotivoRetornoDocumento motivoRetornoDocumento = new MotivoRetornoDocumento(new ArrayList<>());
		motivoRetornoDocumento.setPessoa(colaborador);
		motivoRetornoDocumento.setCriador(colaborador.getUsuario().getLogin());
		motivoRetornoDocumento.setDataCriacao(new Date());
		motivoRetornoDocumento.setAplicacao(findMotivoAplicacao(chave));

		MotivoRetornoTO motivoRetornoTO = listaMotivoRetorno(chave).get(0);

		motivoRetornoDocumento.getListaMotivoRetorno().add(new MotivoDocumento(motivoRetornoDocumento, getMapper().map(motivoRetornoTO, MotivoRetorno.class)));
		Collections.sort(motivoRetornoDocumento.getListaMotivoRetorno());

		persistirMotivoRetorno(motivoRetornoDocumento, chave, codigo);
	}

	private List<MotivoRetornoTO> listaMotivoRetorno(String chave) {
		MotivoAplicacao motivoAplicacao = findMotivoAplicacao(chave);
		MotivoAplicacaoTO motivoAplicacaoTO = getMapper().map(motivoAplicacao, MotivoAplicacaoTO.class);

		List<MotivoRetornoTO> lista = new ArrayList<>();

		if (motivoAplicacao != null && motivoAplicacao.getCodigo() != null) {
			lista = motivoRetornoService.findMotivosRetornoByMotivoAplicacao(motivoAplicacaoTO);
		}

		lista.sort((o1, o2) -> o1.getMotivo().compareTo(o2.getMotivo()));

		return lista;
	}

	private MotivoAplicacao findMotivoAplicacao(String chave) {
		return motivoAplicacaoRepository.findByChave(chave);
	}

	@Transactional
	private void persistirMotivoRetorno(MotivoRetornoDocumento motivoRetornoDocumento, String chave, Long codigoPagamento) {

		Pagamento pagamento = getRepository().findById(codigoPagamento).orElse(null);

		if (pagamento != null && pagamento.getTipo() != null && (pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_FISCAL) || pagamento.getTipo().equals(Pagamento.TipoPagamento.NOTA_PAGAMENTO))) {
			if (motivoRetornoDocumento.isPreencimentoValido()) {

				pagamentoService.retornar(getMapper().map(pagamento, PagamentoTO.class), Pagamento.Status.AUTORIZACAO.getDescricao(), Boolean.FALSE);

				persistirMotivosRetornoDocumento(motivoRetornoDocumento);

				Nota nota = ((ParcelaNota) pagamento).getNota();
				MotivoRetornoNota motivoRetornoNota = new MotivoRetornoNota(motivoRetornoDocumento, nota);
				motivoRetornoNotaRepository.save(motivoRetornoNota);
			}
		} else if (pagamento != null && pagamento.getTipo() != null && (pagamento.getTipo().equals(Pagamento.TipoPagamento.TRIBUTO) || pagamento.getTipo().equals(Pagamento.TipoPagamento.TRIBUTO_ACUMULADO))) {
			pagamentoService.retornar(getMapper().map(pagamento, PagamentoTO.class), Pagamento.Status.AUTORIZACAO.getDescricao(), Boolean.FALSE);
		}

	}

	@Transactional
	private void persistirMotivosRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		if (motivoRetornoDocumento.getListaMotivoRetorno() != null && !motivoRetornoDocumento.getListaMotivoRetorno().isEmpty()) {
			// Salvando todos os motivos documento para cada motivo retorno escolhido.
			for (MotivoDocumento documento : motivoRetornoDocumento.getListaMotivoRetorno()) {
				documento.setMotivoRetornoDocumento(motivoRetornoDocumento);
			}

		} else {
			motivoRetornoDocumento.setListaMotivoRetorno(null);
		}

		motivoRetornoDocumentoRepository.save(motivoRetornoDocumento);
	}

	@Override
	@Transactional
	protected void doBeforeUpdate(Pagamento entity, MovimentoAutorizadoTO to) {
		if (to.getCredor() != null && to.getCredor().getCodigo() == null) {
			entity.setCredor(null);
			to.setCredor(null);
		} else {
			Pessoa credor = pessoaRepository.findByCodigo(to.getCredor().getCodigo());
			entity.setCredor(credor);
		}
		if (to.getDadosBancarios() != null && to.getDadosBancarios().getCodigoPagamento() == null) {
			if (to.getDadosBancarios().getCodigo() != null) {
				pagamentoService.updateDadosBancarios(to.getCodigo(), to.getDadosBancarios().getId());
				to.setDadosBancarios(getMapper().map(dadosBancariosRepository.findById(to.getDadosBancarios().getId()).get(), DadosBancariosTO.class));
			} else {
				to.setDadosBancarios(null);
			}
		}

		if (to.getLinhaDigitavel() == null || to.getLinhaDigitavel().trim().length() == 0) {
			to.setLinhaDigitavel(null);
		}

		if (to.getCodigoBarras() == null || to.getCodigoBarras().trim().length() == 0) {
			to.setCodigoBarras(null);
		}

		try {
			validarDadosPagamento(to);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("doBeforeUpdate", e.getMessage());
		}

		if (to.getListaPgtoCC() != null && !to.getListaPgtoCC().isEmpty()) {
			for (PagamentoContaContabilTO pgTo : to.getListaPgtoCC()) {
				PagamentoContaContabil pagamentoCc = getMapper().map(pgTo, PagamentoContaContabil.class);
				pagamentoCc.setTipo(getTipoPagamentoContaContabil(pgTo.getTipo()));
				pagamentoContaContabilRepository.save(pagamentoCc);
			}
		}
	}

	private PagamentoContaContabil.Tipo getTipoPagamentoContaContabil(String tipo) {
		switch (tipo) {
			case "Multas":
				return PagamentoContaContabil.Tipo.MULTAS;
			case "Juros":
				return PagamentoContaContabil.Tipo.JUROS;
			case "Descontos concedidos":
				return PagamentoContaContabil.Tipo.DESCONTOS;
			default:
				return null;
		}
	}

	private void validarDadosPagamento(MovimentoAutorizadoTO to) throws Exception {
		if (Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(to.getTipoDocumentoPagamento())) {
			String aux = parametroRepository.findByChave("tesouraria.limite.superior.doc").getValor();
			BigDecimal valorDoc = new BigDecimal(aux);

			if (to.getValorAPagar().doubleValue() >= valorDoc.doubleValue()) {
				throw new ValidationException("validarDadosPagamento", String.format("Este pagamento possui valor maior ou igual ao valor permitido para DOC (%s).", Util.formatarValor(valorDoc)));
			}
		}

		if (Pagamento.TipoPagamento.TRIBUTO.getDescricao().equals(to.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.getDescricao().equals(to.getTipo())) {
			if (to.getDataPagamento().after(to.getDataVencimento())) {
				if (to.getValorMulta() == null || to.getValorMulta().equals(BigDecimal.ZERO)) {
					throw new ValidationException("validarDadosPagamento", "Multa é um campo obrigatório");
				}
				if (to.getValorJuros() == null || to.getValorJuros().equals(BigDecimal.ZERO)) {
					throw new ValidationException("validarDadosPagamento", "Juros é um campo obrigatório");
				}
			}
		}

		if ((Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(to.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.DDA.getDescricao().equals(to.getTipoDocumentoPagamento())
			|| Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(to.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.TED.getDescricao().equals(to.getTipoDocumentoPagamento())) &&
			(to.getDadosBancarios() == null || to.getDadosBancarios().getCodigo() == null)) {
			throw new ValidationException("validarDadosPagamento", "Não é permitido selecionar '" + to.getTipoDocumentoPagamento() + "' para um credor/favorecido que não possui dados bancários.");
		}

		if (Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(to.getTipoDocumentoPagamento()) && !to.getDadosBancarios().getHomologado()) {
			throw new ValidationException("validarDadosPagamento", "Os dados bancários selecionados não estão homologados e, por isso, não é permitido selecionar 'Depósito em conta/transferência'.");
		}

		if (Pagamento.TipoDocumentoPagamento.BOLETO.getDescricao().equals(to.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(to.getInfoBoleto())) {
				if (to.getLinhaDigitavel() == null || Constants.BLANK.equals(to.getLinhaDigitavel())) {
					throw new ValidationException("validarDadosPagamento", "Linha digitável é obrigatória.");

				} else if (boletoService.validarLinhaAndCodigoBarras(to.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(to, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					to.setCodigoBarras(BoletoUtil.linhaDigitavelNumericaEmCodigoDeBarras(to.getLinhaDigitavel().replace(".", "")));
					to.setLinhaDigitavel(to.getLinhaDigitavel().replace(".", ""));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(to.getInfoBoleto())) {
				if (to.getCodigoBarras() == null || Constants.BLANK.equals(to.getCodigoBarras())) {
					throw new ValidationException("validarDadosPagamento", "Código de barras é obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(to.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(to, PagamentoTO.class))) {
					to.setLinhaDigitavel(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(to.getCodigoBarras().replace(".", "")));

				}
			}

			// Obter o código do banco de um código de barras.
			if (boletoService.validarBancoDoCodigoDeBarras(to.getCodigoBarras())) {

				BancoTO banco = boletoService.getCodDoBancoDoCodigoDeBarras(to.getCodigoBarras());
				to.setBancoBoleto(getMapper().map(banco, BancoTO.class));

			} else {
				throw new ValidationException("validarDadosPagamento", String.format("Banco com código [%s] não existe.", boletoService.getCodDoBancoDoCodigoDeBarras(to.getCodigoBarras())));

			}
		}

		if (Pagamento.TipoDocumentoPagamento.GUIA.getDescricao().equals(to.getTipoDocumentoPagamento()) &&
			(to.getLinhaDigitavel() != null || to.getCodigoBarras() != null)) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(to.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(to.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(to, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					to.setCodigoBarras(GuiaUtil.linhaDigitavelNumericaEmCodigoDeBarras(to.getLinhaDigitavel()));
					to.setLinhaDigitavel(GuiaUtil.linhaDigitavelFormatadaEmNumerica(to.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(to.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(to.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(to, PagamentoTO.class))) {
					to.setLinhaDigitavel(GuiaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(to.getCodigoBarras().replace(".", "")));

				}
			}
		}

		if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.getDescricao().equals(to.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(to.getInfoBoleto())) {
				if (to.getLinhaDigitavel() == null || Constants.BLANK.equals(to.getLinhaDigitavel())) {
					throw new ValidationException("validarDadosPagamento", "Linha digitável é obrigatória.");

				} else if (boletoService.validarLinhaAndCodigoBarras(to.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(to, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					to.setCodigoBarras(ConcessionariaUtil.linhaDigitavelNumericaEmCodigoDeBarras(to.getLinhaDigitavel()));
					to.setLinhaDigitavel(ConcessionariaUtil.linhaDigitavelFormatadaEmNumerica(to.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(to.getInfoBoleto())) {
				if (to.getCodigoBarras() == null || Constants.BLANK.equals(to.getCodigoBarras())) {
					throw new ValidationException("validarDadosPagamento", "Código de barras é obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(to.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(to, PagamentoTO.class))) {
					to.setLinhaDigitavel(ConcessionariaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(to.getCodigoBarras().replace(".", "")));

				}
			}
		}
	} // fim do método "validarDadosPagamento"

	@Override
	@Transactional
	public PagamentoTO pagamentoAtualizarMovimento(PagamentoTO pagamentoTO) {
		if (pagamentoTO.getDataPagamento() != null) {
			pagamentoService.updateDataPagamento(pagamentoTO.getDataPagamento(), pagamentoTO.getCodigo());
		}

		if (pagamentoTO.getTipoDocumentoPagamento() != null) {
			Pagamento.TipoDocumentoPagamento tipo = null;
			for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {
				if (pagamentoTO.getTipoDocumentoPagamento().equals(tipoDocumentoPagamento.getDescricao())) {
					tipo = tipoDocumentoPagamento;
					break;
				}
			}
			switch (tipo) {
				case BOLETO:
					pagamentoService.updateTipoDocumentoPagamentoBoleto(pagamentoTO.getCodigo());
					break;
				case DEPOSITO:
					pagamentoService.updateTipoDocumentoPagamentoDeposito(pagamentoTO.getCodigo());
					break;
				case DDA:
					pagamentoService.updateTipoDocumentoPagamentoDda(pagamentoTO.getCodigo());
					break;
				case GUIA:
					pagamentoService.updateTipoDocumentoPagamentoGuia(pagamentoTO.getCodigo());
					break;
				case DOC:
					pagamentoService.updateTipoDocumentoPagamentoDoc(pagamentoTO.getCodigo());
					break;
				case TED:
					pagamentoService.updateTipoDocumentoPagamentoTed(pagamentoTO.getCodigo());
					break;
				case CONCESSIONARIA:
					pagamentoService.updateTipoDocumentoPagamentoConcessionaria(pagamentoTO.getCodigo());
					break;
				case OUTROS:
					pagamentoService.updateTipoDocumentoPagamentoOutros(pagamentoTO.getCodigo());
					break;
			}
		}

		return pagamentoTO;
	}
}
