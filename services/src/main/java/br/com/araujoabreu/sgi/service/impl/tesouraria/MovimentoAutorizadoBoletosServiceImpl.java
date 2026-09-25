package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QGerencia;
import br.com.araujoabreu.sgi.administracao.enums.StatusCodigoBarraLd;
import br.com.araujoabreu.sgi.administracao.enums.TipoInformacaoNumeroBoleto;
import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.compras.domain.QParcelaNota;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.PagamentoContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquota;
import br.com.araujoabreu.sgi.service.banco.BancoService;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.tesouraria.BoletoService;
import br.com.araujoabreu.sgi.service.tesouraria.MovimentoAutorizadoBoletosService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoBoletosPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.MovimentoAutorizadoBoletosTO;
import br.com.araujoabreu.sgi.tesouraria.lang.CodigoDeBarrasException;
import br.com.araujoabreu.sgi.tesouraria.lang.LinhaDigitavelException;
import br.com.araujoabreu.sgi.tesouraria.util.ConcessionariaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.GuiaUtil;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.BoletoUtil;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.pessoa.domain.QDadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoContaContabilRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.contasApagar.ParcelaNotaRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MovimentoAutorizadoBoletosServiceImpl extends BaseSearchService<Pagamento, MovimentoAutorizadoBoletosTO, MovimentoAutorizadoBoletosPesquisaTO, PagamentoRepository> implements MovimentoAutorizadoBoletosService {

	private static final QPagamento qpagamento = QPagamento.pagamento;

	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private BancoService bancoService;

	@Autowired
	private ParcelaNotaRepository parcelaNotaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private PagamentoContaContabilRepository pagamentoContaContabilRepository;

	@Autowired
	private ContaContabilRepository contaContabilRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	public MovimentoAutorizadoBoletosServiceImpl(PagamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Pagamento> jpaQuery, MovimentoAutorizadoBoletosPesquisaTO exampleObj) {
		NumberPath<BigDecimal> aliasQuantity = Expressions.numberPath(BigDecimal.class, "quantity");

		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QPessoa qPessoa = QPessoa.pessoa;
		QNota qNota = QNota.nota;


		jpaQuery.distinct()
			.leftJoin(qpagamento.centroFinanceiro, qCentroFinanceiro)
			.leftJoin(qpagamento.as(QParcelaNota.class).nota, qNota)
			.leftJoin(qpagamento.credor, qPessoa)
			.where(
				qpagamento.status.eq(Pagamento.Status.PAGAMENTO)
					.and(qpagamento.tipoDocumentoPagamento.eq(Pagamento.TipoDocumentoPagamento.BOLETO))
			);

		QBanco qBanco = QBanco.banco;
		QDadosBancarios qDadosBancarios = QDadosBancarios.dadosBancarios;
		jpaQuery.leftJoin(qpagamento.dadosBancarios, qDadosBancarios)
			.leftJoin(qDadosBancarios.banco, qBanco);

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

		jpaQuery.select(Projections.fields(Pagamento.class,
			qpagamento.codigo,
			qpagamento.numero,
			qpagamento.dataPagamento,
			qpagamento.dataVencimento,
			qpagamento.tipo,
			qpagamento.linhaDigitavel,
			qpagamento.codigoBarras,
			qpagamento.tipoDocumentoPagamento,
			qpagamento.valor.subtract(qpagamento.valorAdiantamento.coalesce(BigDecimal.ZERO)).subtract(qpagamento.valorDesconto.coalesce(BigDecimal.ZERO)).add(qpagamento.valorJuros.coalesce(BigDecimal.ZERO)).add(qpagamento.valorMulta.coalesce(BigDecimal.ZERO)).as(aliasQuantity),
			qpagamento.valorAdiantamento,
			qpagamento.valorDesconto,
			qpagamento.valorDevolucao,
			qpagamento.valorJuros,
			qpagamento.valorMulta,
			Projections.fields(DadosBancarios.class, qpagamento.dadosBancarios.codigo).as("dadosBancarios"),
			Projections.fields(Pessoa.class, qpagamento.credor.codigo, qpagamento.credor.nome, qpagamento.credor.tipoPessoa).as("credor"),
			Projections.fields(Gerencia.class, qpagamento.centroFinanceiro.codigo, qpagamento.centroFinanceiro.nome, qpagamento.centroFinanceiro.situacao, qpagamento.centroFinanceiro.identificadorFinal, qpagamento.centroFinanceiro.identificador).as("centroFinanceiro")
		));

		jpaQuery.orderBy(aliasQuantity.desc(), qpagamento.numero.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MovimentoAutorizadoBoletosPesquisaTO filtroTO) {
		builder.and(qpagamento.tipoDocumentoPagamento.eq(Pagamento.TipoDocumentoPagamento.BOLETO));

		if (Check.isNotEmpty(filtroTO.getCodigoBarras()) || Check.isNotEmpty(filtroTO.getLinhaDigitavel())) {
			try {
				if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.toString().equalsIgnoreCase(filtroTO.getInfoBoleto())) {
					if (boletoService.validarBoletoCodigoBarras(filtroTO.getCodigoBarras())) {
						Date date = BoletoUtil.getDataVencimentoCodigoBarras(filtroTO.getCodigoBarras());
						filtroTO.setPagamentoDe(date);
						filtroTO.setPagamentoAte(date);

						BigDecimal valor = BoletoUtil.getValorBigDecimalDoTituloDoCodigoDeBarras(filtroTO.getCodigoBarras());
						filtroTO.setValorDe(valor);
						filtroTO.setValorAte(valor);
					}
				} else if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.toString().equalsIgnoreCase(filtroTO.getInfoBoleto())) {
					if (boletoService.validarBoletoCodigoBarras(filtroTO.getLinhaDigitavel())) {
						Date date = BoletoUtil.getDataVencimentoLinhaDigitavel(filtroTO.getLinhaDigitavel());
						filtroTO.setPagamentoDe(date);
						filtroTO.setPagamentoAte(date);

						BigDecimal valor = BoletoUtil.getValorBigDecimalDoTituloDaLinhaDigitavelFormatada(filtroTO.getLinhaDigitavel());
						filtroTO.setValorDe(valor);
						filtroTO.setValorAte(valor);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (Check.isNotEmpty(filtroTO.getNumero())) {
			builder.and(qpagamento.numero.containsIgnoreCase(filtroTO.getNumero().toLowerCase()));
		}

		if (filtroTO.getPagamentoDe() != null && filtroTO.getPagamentoAte() != null) {
			builder.and(qpagamento.dataPagamento.between(filtroTO.getPagamentoDe(), filtroTO.getPagamentoAte()));
		} else if (filtroTO.getPagamentoDe() != null) {
			builder.andAnyOf(qpagamento.dataPagamento.after(filtroTO.getPagamentoDe()), qpagamento.dataPagamento.eq(filtroTO.getPagamentoDe()));
		} else if (filtroTO.getPagamentoAte() != null) {
			builder.andAnyOf(qpagamento.dataPagamento.before(filtroTO.getPagamentoAte()), qpagamento.dataPagamento.eq(filtroTO.getPagamentoAte()));
		}

		if (filtroTO.getValorDe() != null) {
			builder.and(qpagamento.valor.goe(filtroTO.getValorDe()));
		}

		if (filtroTO.getValorAte() != null) {
			builder.and(qpagamento.valor.loe(filtroTO.getValorAte()));
		}

		if (Check.isNotEmpty(filtroTO.getNomeCredor())) {
			QPessoaJuridica qPessoaJuridica = QPessoa.pessoa.as(QPessoaJuridica.class);
			builder.andAnyOf(
				qPessoaJuridica.nome.containsIgnoreCase(filtroTO.getNomeCredor()),
				qPessoaJuridica.documento.containsIgnoreCase(filtroTO.getNomeCredor()),
				qPessoaJuridica.nomeFantasia.containsIgnoreCase(filtroTO.getNomeCredor())
			);
		}

		if (Check.isAssigned(filtroTO.getEmpresa()) && filtroTO.getEmpresa().getCodigo() != null) {
			builder.andAnyOf(qpagamento.as(QParcelaNota.class).nota.empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()),
				QNota.nota.empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()),
				new QNota("notaTR").empresa.codigo.eq(filtroTO.getEmpresa().getCodigo()));
		}

		if (filtroTO.getBanco() != null && filtroTO.getBanco().getCodigo() != null) {
			QBanco qBanco = QBanco.banco;
			builder.and(qBanco.codigo.eq(filtroTO.getBanco().getCodigo()));
		}

		if (Check.isAssigned(filtroTO.getStatusCodBarraLd())) {
			if (StatusCodigoBarraLd.NAO_PREENCHIDO.getNome().equals(filtroTO.getStatusCodBarraLd())) {
				builder.and((qpagamento.codigoBarras.isNull().or(qpagamento.codigoBarras.eq(Constants.BLANK)))
					.and((qpagamento.linhaDigitavel.isNull().or(qpagamento.linhaDigitavel.eq(Constants.BLANK)))));
			} else {
				builder.and((qpagamento.codigoBarras.isNotNull().or(qpagamento.codigoBarras.eq(Constants.BLANK).not()))
					.and((qpagamento.linhaDigitavel.isNotNull().or(qpagamento.linhaDigitavel.eq(Constants.BLANK).not()))));
			}
		}

		if (Check.isAssigned(filtroTO.getGerencia()) && filtroTO.getGerencia().getCodigo() != null) {
			builder.and(qpagamento.centroFinanceiro.codigo.eq(filtroTO.getGerencia().getCodigo()));
		}

	}

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
	protected void doAfterLoad(MovimentoAutorizadoBoletosTO movAutorizadoBoletoTO) {
		Pagamento pagamento = getRepository().findById(movAutorizadoBoletoTO.getCodigo()).orElse(null);

		movAutorizadoBoletoTO.setValorLiquido(pagamentoService.obterValorLiquido(movAutorizadoBoletoTO.getCodigo(), movAutorizadoBoletoTO.getTipo()));
		movAutorizadoBoletoTO.setValorPagar(movAutorizadoBoletoTO.getValor());

		DadosBancarios dadosBancarios = dadosBancariosRepository.findByPagamentoCodigo(movAutorizadoBoletoTO.getCodigo());


		if (Pagamento.TipoPagamento.TRIBUTO.toString().equalsIgnoreCase(movAutorizadoBoletoTO.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.toString().equalsIgnoreCase(movAutorizadoBoletoTO.getTipo())) {

			movAutorizadoBoletoTO.setValorTributo(pagamentoService.calcularTotalAcumulado(pagamento.getCodigo(), pagamento.getTipo().toString()));

		} else if (Pagamento.TipoPagamento.NOTA_PAGAMENTO.toString().equalsIgnoreCase(movAutorizadoBoletoTO.getTipo())) {
			if (dadosBancarios != null) {
				movAutorizadoBoletoTO.setDadosBancarios(getMapper().map(dadosBancarios, DadosBancariosTO.class));
			}

			/*movAutorizadoBoletoTO.setValorTributo(pagamentoService.tributoMaisOutrosDesc(movAutorizadoBoletoTO.getCodigo(), movAutorizadoBoletoTO.getTipo()));*/
			movAutorizadoBoletoTO.setTributoMaisOutrosDesc(pagamentoService.tributoMaisOutrosDesc(movAutorizadoBoletoTO.getCodigo(), movAutorizadoBoletoTO.getTipo()));

		} else if (Pagamento.TipoPagamento.NOTA_FISCAL.toString().equalsIgnoreCase(movAutorizadoBoletoTO.getTipo())) {
			ParcelaNota parcelaNota = parcelaNotaRepository.findById(pagamento.getCodigo()).get();
			List<ParcelaNota> listaParcelaNotas = parcelaNotaRepository.findByNotaCodigo(parcelaNota.getNota().getCodigo());

			if (dadosBancarios != null) {
				movAutorizadoBoletoTO.setDadosBancarios(getMapper().map(dadosBancarios, DadosBancariosTO.class));
			}

			movAutorizadoBoletoTO.setTotalOutrosDescontos(pagamentoService.getTotalOutrosDescontos(pagamento.getCodigo()));

			if (Check.isNotEmpty(listaParcelaNotas)) {
				listaParcelaNotas.sort((o1, o2) -> o1.getOrdem().compareTo(o2.getOrdem()));

				ParcelaNota parcelaNota1 = listaParcelaNotas.get(listaParcelaNotas.size() - 1);
				PagamentoTO pagamentoto = new PagamentoTO();
				pagamentoto.setCodigo(parcelaNota1.getCodigo());
				pagamentoto.setDataVencimento(parcelaNota1.getDataVencimento());
				pagamentoto.setDataPagamento(parcelaNota1.getDataPagamento());
				pagamentoto.setValor(pagamento.getValor());
				pagamentoto.setTipoDocumentoPagamento(parcelaNota1.getTipoDocumentoPagamento().name());
				pagamentoto.setOrdenacao(String.valueOf(parcelaNota1.getOrdem()));

				movAutorizadoBoletoTO.setUltimaParcela(pagamentoto);

			}

			if (movAutorizadoBoletoTO.getUltimaParcela().getCodigo().equals(movAutorizadoBoletoTO.getCodigo())) {
				movAutorizadoBoletoTO.setValorTributo(BigDecimal.ZERO);
			} else {
				movAutorizadoBoletoTO.setValorTributo(movAutorizadoBoletoTO.getValorTributo());
			}
		} else {
			movAutorizadoBoletoTO.setValorTributo(pagamentoService.calcularTotalAcumulado(pagamento.getCodigo(), pagamento.getTipo().toString()));
		}

		List<PagamentoContaContabil> listaPagamento = pagamentoContaContabilRepository.findPagamentoContaContabilByPagamento(movAutorizadoBoletoTO.getCodigo());

		if (listaPagamento != null && !listaPagamento.isEmpty()) {
			List<PagamentoContaContabilTO> listaTO = new ArrayList<>();

			for (PagamentoContaContabil pcc : listaPagamento) {

				PagamentoContaContabilTO to = new PagamentoContaContabilTO();
				to.setCodigo(pcc.getCodigo());
				to.setTipo(pcc.getTipo().getDescricao());

				PagamentoTO pagamentoTO = new PagamentoTO();
				pagamentoTO.setCodigo(pcc.getPagamento().getCodigo());

				ContaContabil contaContabil = contaContabilRepository.findById(pcc.getContaContabil().getCodigo()).orElse(null);

				ContaContabilTO contaContabilTO = getMapper().map(contaContabil, ContaContabilTO.class);

				to.setPagamento(pagamentoTO);
				to.setContaContabil(contaContabilTO);

				listaTO.add(to);
			}

			movAutorizadoBoletoTO.setListaPgtoCC(listaTO);
		}

	}

	@Override
	protected void doBeforeUpdate(Pagamento entity, MovimentoAutorizadoBoletosTO movAutorizadoBoletoTO) throws ServiceException {
		if (movAutorizadoBoletoTO.getLinhaDigitavel() == null || movAutorizadoBoletoTO.getLinhaDigitavel().trim().length() == 0) {
			movAutorizadoBoletoTO.setLinhaDigitavel(null);
		}
		if (movAutorizadoBoletoTO.getCodigoBarras() == null || movAutorizadoBoletoTO.getCodigoBarras().trim().length() == 0) {
			movAutorizadoBoletoTO.setCodigoBarras(null);
		}

		try {
			validarDadosPagamento(movAutorizadoBoletoTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("ValidationException",e.getMessage());
		}

		if (movAutorizadoBoletoTO.getListaPgtoCC() != null && !movAutorizadoBoletoTO.getListaPgtoCC().isEmpty()) {
			for (PagamentoContaContabilTO pgTo : movAutorizadoBoletoTO.getListaPgtoCC()) {
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

	private void validarDadosPagamento(MovimentoAutorizadoBoletosTO movAutorizadoBoletoTO) throws Exception {
		if (Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento())) {
			String aux = parametroRepository.findByChave("tesouraria.limite.superior.doc").getValor();
			BigDecimal valorDoc = new BigDecimal(aux);

			if (movAutorizadoBoletoTO.getValorPagar().doubleValue() >= valorDoc.doubleValue()) {
				throw new ValidationException("ValidationException",String.format("Este pagamento possui valor maior ou igual ao valor permitido para DOC (%s).",
					Util.formatarValor(valorDoc)));
			}
		}

		if (Pagamento.TipoPagamento.TRIBUTO.getDescricao().equals(movAutorizadoBoletoTO.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.getDescricao().equals(movAutorizadoBoletoTO.getTipo())) {
			if (movAutorizadoBoletoTO.getDataPagamento().after(movAutorizadoBoletoTO.getDataVencimento())) {
				if (movAutorizadoBoletoTO.getValorMulta() == null || movAutorizadoBoletoTO.getValorMulta().equals(BigDecimal.ZERO)) {
					throw new ValidationException("ValidationException","Multa é um campo obrigatório");
				}
				if (movAutorizadoBoletoTO.getValorJuros() == null || movAutorizadoBoletoTO.getValorJuros().equals(BigDecimal.ZERO)) {
					throw new ValidationException("ValidationException","Juros é um campo obrigatório");
				}
			}
		}

		if ((Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.DDA.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento())
			|| Pagamento.TipoDocumentoPagamento.DOC.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento()) || Pagamento.TipoDocumentoPagamento.TED.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento())) &&
			(movAutorizadoBoletoTO.getDadosBancarios() == null || movAutorizadoBoletoTO.getDadosBancarios().getCodigo() == null)) {
			throw new ValidationException("ValidationException","Não é permitido selecionar '" + movAutorizadoBoletoTO.getTipoDocumentoPagamento() + "' para um credor/favorecido que não possui dados bancários.");
		}

		if (Pagamento.TipoDocumentoPagamento.DEPOSITO.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento()) && !movAutorizadoBoletoTO.getDadosBancarios().getHomologado()) {
			throw new ValidationException("ValidationException","Os dados bancários selecionados não estão homologados e, por isso, não é permitido selecionar 'Depósito em conta/transferência'.");
		}

		if (Pagamento.TipoDocumentoPagamento.BOLETO.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (movAutorizadoBoletoTO.getLinhaDigitavel() == null || Constants.BLANK.equals(movAutorizadoBoletoTO.getLinhaDigitavel())) {
					throw new ValidationException("ValidationException","Linha digitável é um campo de preenchimento obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					movAutorizadoBoletoTO.setCodigoBarras(BoletoUtil.linhaDigitavelNumericaEmCodigoDeBarras(movAutorizadoBoletoTO.getLinhaDigitavel().replace(".", "")));
					movAutorizadoBoletoTO.setLinhaDigitavel(movAutorizadoBoletoTO.getLinhaDigitavel().replace(".", ""));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (movAutorizadoBoletoTO.getCodigoBarras() == null || Constants.BLANK.equals(movAutorizadoBoletoTO.getCodigoBarras())) {
					throw new ValidationException("ValidationException","Código de barras é um campo de preenchimento obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					movAutorizadoBoletoTO.setLinhaDigitavel(BoletoUtil.codigoDeBarrasEmLinhaDigitavelNumerica(movAutorizadoBoletoTO.getCodigoBarras().replace(".", "")));

				}
			}

			// Obter o código do banco de um código de barras.
			if (boletoService.validarBancoDoCodigoDeBarras(movAutorizadoBoletoTO.getCodigoBarras())) {
				BancoTO banco = boletoService.getCodDoBancoDoCodigoDeBarras(movAutorizadoBoletoTO.getCodigoBarras());
				movAutorizadoBoletoTO.setBancoBoleto(getMapper().map(banco, BancoTO.class));
			} else {
				throw new ValidationException("ValidationException",String.format("Banco com código [%s] não existe.", boletoService.getCodDoBancoDoCodigoDeBarras(movAutorizadoBoletoTO.getCodigoBarras())));
			}
		}

		if (Pagamento.TipoDocumentoPagamento.GUIA.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento()) &&
			((movAutorizadoBoletoTO.getLinhaDigitavel() != null || movAutorizadoBoletoTO.getCodigoBarras() != null))
		) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					movAutorizadoBoletoTO.setCodigoBarras(GuiaUtil.linhaDigitavelNumericaEmCodigoDeBarras(movAutorizadoBoletoTO.getLinhaDigitavel()));
					movAutorizadoBoletoTO.setLinhaDigitavel(GuiaUtil.linhaDigitavelFormatadaEmNumerica(movAutorizadoBoletoTO.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					movAutorizadoBoletoTO.setLinhaDigitavel(GuiaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(movAutorizadoBoletoTO.getCodigoBarras().replace(".", "")));

				}
			}
		}

		if (Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.getDescricao().equals(movAutorizadoBoletoTO.getTipoDocumentoPagamento())) {
			if (TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (movAutorizadoBoletoTO.getLinhaDigitavel() == null || Constants.BLANK.equals(movAutorizadoBoletoTO.getLinhaDigitavel())) {
					throw new ValidationException("ValidationException","Linha digitável é um campo de preenchimento obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getLinhaDigitavel(), TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					// Passar linha digitável em código de barras.
					movAutorizadoBoletoTO.setCodigoBarras(ConcessionariaUtil.linhaDigitavelNumericaEmCodigoDeBarras(movAutorizadoBoletoTO.getLinhaDigitavel()));
					movAutorizadoBoletoTO.setLinhaDigitavel(ConcessionariaUtil.linhaDigitavelFormatadaEmNumerica(movAutorizadoBoletoTO.getLinhaDigitavel()));

				}
			} else if (TipoInformacaoNumeroBoleto.CODIGO_BARRAS.getNome().equals(movAutorizadoBoletoTO.getInfoBoleto())) {
				if (movAutorizadoBoletoTO.getCodigoBarras() == null || Constants.BLANK.equals(movAutorizadoBoletoTO.getCodigoBarras())) {
					throw new ValidationException("ValidationException","Código de barras é um campo de preenchimento obrigatório.");

				} else if (boletoService.validarLinhaAndCodigoBarras(movAutorizadoBoletoTO.getCodigoBarras(), TipoInformacaoNumeroBoleto.CODIGO_BARRAS, getMapper().map(movAutorizadoBoletoTO, PagamentoTO.class))) {
					movAutorizadoBoletoTO.setLinhaDigitavel(ConcessionariaUtil.codigoDeBarrasEmLinhaDigitavelNumerica(movAutorizadoBoletoTO.getCodigoBarras().replace(".", "")));

				}
			}
		}
	}

	@Override
	@Transactional
	public MovimentoAutorizadoBoletosTO salvarCodigoBarraLinhaDigitavel(@RequestBody MovimentoAutorizadoBoletosTO to) {
		try {
			if (to.getLinhaDigitavelInformada()) {
				to.setInfoBoleto(TipoInformacaoNumeroBoleto.LINHA_DIGITAVEL.toString());
			} else {
				to.setInfoBoleto(TipoInformacaoNumeroBoleto.CODIGO_BARRAS.toString());
			}

			validarDadosPagamento(to);

			pagamentoService.updateCodigoBarras(to.getCodigoBarras(), to.getCodigo());
			pagamentoService.updateLinhaDigitavel(to.getLinhaDigitavel(), to.getCodigo());

			to.setDataPagamentoInvalido(validarDataValorPagamento(to));

			return to;
		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
	}

	private boolean validarDataValorPagamento(MovimentoAutorizadoBoletosTO to) {
		Date vencimento = null;
		BigDecimal valor = null;

		if (to.isLinhaDigitavelInformada() && ObjectUtil.isNotNull(to.getLinhaDigitavel())) {
			if (ObjectUtil.isNotNull(to.getLinhaDigitavel())) {
				try {
					vencimento = BoletoUtil.getDataVencimentoLinhaDigitavel(to.getLinhaDigitavel());
					valor = BoletoUtil.getValorBigDecimalDoTituloDaLinhaDigitavelFormatada(BoletoUtil.linhaDigitavelNumericaEmFormatada(to.getLinhaDigitavel()));
				} catch (NullPointerException | LinhaDigitavelException | ParseException | IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
		} else if (to.getCodigoBarras() != null) {
			if (ObjectUtil.isNotNull(to.getCodigoBarras())) {
				try {
					vencimento = BoletoUtil.getDataVencimentoCodigoBarras(to.getCodigoBarras());
					valor = BoletoUtil.getValorBigDecimalDoTituloDoCodigoDeBarras(to.getCodigoBarras());
				} catch (NullPointerException | CodigoDeBarrasException | ParseException | IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
		}

		boolean diferenteVencimento = Boolean.FALSE;
		boolean diferenteValor = Boolean.FALSE;

		if (vencimento != null) {
			Calendar vencimentoAjuste = Calendar.getInstance();
			vencimentoAjuste.setTime(vencimento);
			vencimentoAjuste.set(Calendar.HOUR, 0);
			vencimentoAjuste.set(Calendar.MINUTE, 0);
			vencimentoAjuste.set(Calendar.SECOND, 0);
			vencimento = vencimentoAjuste.getTime();

			vencimentoAjuste = Calendar.getInstance();
			vencimentoAjuste.setTime(to.getDataPagamento());
			vencimentoAjuste.set(Calendar.HOUR, 0);
			vencimentoAjuste.set(Calendar.MINUTE, 0);
			vencimentoAjuste.set(Calendar.SECOND, 0);
			to.setDataPagamento(vencimentoAjuste.getTime());

			if (!vencimento.equals(to.getDataPagamento())) {
				diferenteVencimento = Boolean.TRUE;
			}
		}
		if (valor != null) {
			if (valor.doubleValue() != to.getValorPagar().doubleValue() && valor.doubleValue() > 0) {
				diferenteValor = Boolean.TRUE;
			}
		}

		String msg = "%s %s não confere(m) com o pagamento. Verifique.";

		if (diferenteValor && diferenteVencimento) {
			to.setMsgValidarValorEDataPagamento(String.format(msg, "O valor e data do pagamento", to.getLinhaDigitavelInformada() ? "desta linha digitável" : "deste código de barras"));
			return Boolean.TRUE;
		} else if (diferenteValor) {
			to.setMsgValidarValorEDataPagamento(String.format(msg, "O valor", to.getLinhaDigitavelInformada() ? "desta linha digitável" : "deste código de barras"));
			return Boolean.TRUE;
		} else if (diferenteVencimento) {
			to.setMsgValidarValorEDataPagamento(String.format(msg, "A data do pagamento", to.getLinhaDigitavelInformada() ? "desta linha digitável" : "deste código de barras"));
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	protected void doAfterSearch(List<Pagamento> entities, List<MovimentoAutorizadoBoletosPesquisaTO> result) throws ServiceException {
		for (MovimentoAutorizadoBoletosPesquisaTO to : result) {
			to.setCodBarrasLD(to.getCodigoBarras());
			to.setBanco(bancoService.obterbancoPeloCodigoDadosBancarios(to.getDadosBancarios().getCodigo()));
			to.getDadosBancarios().setBanco(to.getBanco());
			to.setValorPagar(pagamentoService.obterValorPagar(to.getCodigo(), to.getTipo()));
			to.setCodigoNota(pagamentoService.getCodigoNota(to.getCodigo()));
			to.setDataPagamentoInvalido(validarDataValorPagamento(getMapper().map(to, MovimentoAutorizadoBoletosTO.class)));
		}
	}

}
