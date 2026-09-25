package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaBaixaRetencao;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaRetencao;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaida;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaidaParcela;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterfaceNota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaService;
import br.com.araujoabreu.sgi.service.nota.ParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaRetencaoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceFaturamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRetencaoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NotaSaidaParcelaServiceImpl extends BaseSearchService<NotaSaidaParcela, NotaSaidaParcelaTO, NotaSaidaParcelaPesquisaTO, NotaSaidaParcelaRepository> implements NotaSaidaParcelaService {

	private static final QNotaSaidaParcela qNotaSaidaParcela = QNotaSaidaParcela.notaSaidaParcela;

	private static final QLoteInterfaceNota subQuery = QLoteInterfaceNota.loteInterfaceNota;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private DozerHelper dozerHelper;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private LoteInterfaceFaturamentoRepository loteInterfaceFaturamentoRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;
	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;
	@Autowired
	private FaturamentoService faturamentoService;
	@Autowired
	private ParcelaService parcelaService;
	@Autowired
	private NotaSaidaParcelaRetencaoRepository notaSaidaParcelaRetencaoRepository;
	@Autowired
	private NotaTributoAliquotaService notaTributoAliquotaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	public NotaSaidaParcelaServiceImpl(NotaSaidaParcelaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaSaidaParcela> jpaQuery, NotaSaidaParcelaPesquisaTO filter) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida);
		jpaQuery.join(qNotaSaida.centroFinanceiro, QCentroFinanceiro.centroFinanceiro);

		if (filter.getNotaSaida() != null) {
			if (filter.getNotaSaida().getEmpresa() != null) {
				QUnidade qUnidade = QUnidade.unidade;
				jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
					.join(qNotaSaida.empresa, qUnidade);
			}

			if (filter.getNotaSaida().getServicoTipo() != null) {
				QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
				jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
					.join(qNotaSaida.servicoTipo, qServicoTipo);
			}

			if (filter.getNotaSaida().getCentroFinanceiro() != null) {
				QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

				jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
					.join(qNotaSaida.centroFinanceiro, qCentroFinanceiro);

				if (filter.getNotaSaida().getCentroFinanceiro().getContratoTO() != null) {
					QContrato qContrato = QContrato.contrato;

					jpaQuery.join(qNotaSaida.centroFinanceiro, qCentroFinanceiro)
						.join(qCentroFinanceiro.contrato, qContrato);
				}
			}
		}

		if (filter.getNomeFantasiaDocumento() != null && !filter.getNomeFantasiaDocumento().isEmpty()) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			jpaQuery.join(qNotaSaida.faturamentoEtapas, qFaturamentoEtapa);

			QFaturamento qFaturamento = QFaturamento.faturamento;
			jpaQuery.join(qFaturamentoEtapa.faturamento, qFaturamento);

			QPessoa qPessoa = QPessoa.pessoa;
			jpaQuery.join(qFaturamento.cliente, qPessoa);
		}

		if (filter.getEtapa() != null) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			QFaturamento qFaturamento = QFaturamento.faturamento;

			jpaQuery.leftJoin(qNotaSaida.faturamentoEtapas, qFaturamentoEtapa);
			jpaQuery.leftJoin(qFaturamentoEtapa.faturamento, qFaturamento);

		}

		if (filter.getCidade() != null) {
			QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
			QAliquota qAliquota = QAliquota.aliquota;
			QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
			QCidade qCidade = QCidade.cidade;

			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
				.join(qNotaSaida.listaNotaTributoAliquota, qNotaTributoAliquota)
				.join(qNotaTributoAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.join(qCategoriaTributo.cidade, qCidade);
		}

		if (filter.getCodigoLote() != null) {
			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
				.where(
					qNotaSaida.codigo.in(JPAExpressions
						.selectFrom(subQuery)
						.where(subQuery.loteInterface.codigo.eq(filter.getCodigoLote()))
						.select(subQuery.nota.codigo)
					)
				);
		}

		if (filter.getServicoTipoCodigo() != null) {
			QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
				.join(qNotaSaida.servicoTipo, qServicoTipo);
		}

		jpaQuery.select(createQBean());

		jpaQuery.orderBy(qNotaSaida.numero.asc(), qNotaSaidaParcela.ordem.asc(), qNotaSaida.empresa.codnome.asc(), qNotaSaida.dataEmissao.desc());
	}

	protected QBean createQBean() {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		return Projections.fields(NotaSaidaParcela.class,
			qNotaSaidaParcela.codigo,
			qNotaSaidaParcela.dataPagamento,
			qNotaSaidaParcela.dataVencimento,
			qNotaSaidaParcela.ordem,
			qNotaSaidaParcela.situacao,
			qNotaSaidaParcela.valorBruto,
			qNotaSaidaParcela.valorDesconto,
			qNotaSaidaParcela.valorJuros,
			qNotaSaidaParcela.valorMulta,
			qNotaSaidaParcela.valorLiquido,
			qNotaSaidaParcela.valorPermuta,
			qNotaSaidaParcela.observacoesImpressao,
			Projections.fields(NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.protocoloEntrega,
				qNotaSaida.status,
				qNotaSaida.dataEmissao,
				qNotaSaida.tipoFornecimento,
				Projections.fields(Unidade.class, qNotaSaida.empresa.codigo, qNotaSaida.empresa.codnome).as("empresa"),
				Projections.fields(CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.nome,
					qCentroFinanceiro.identificadorFinal,
					qCentroFinanceiro.situacao,
					Projections.fields(Contrato.class, qCentroFinanceiro.contrato.codigo).as("contrato")
				).as("centroFinanceiro")
			).as("notaSaida"),
			Projections.fields(ContaCorrente.class,
				qNotaSaidaParcela.contaCorrenteRecebimento.codigo,
				qNotaSaidaParcela.contaCorrenteRecebimento.agencia,
				qNotaSaidaParcela.contaCorrenteRecebimento.digitoAgencia,
				qNotaSaidaParcela.contaCorrenteRecebimento.conta,
				qNotaSaidaParcela.contaCorrenteRecebimento.digito,
				Projections.fields(Banco.class, qNotaSaidaParcela.contaCorrenteRecebimento.banco.codigo, qNotaSaidaParcela.contaCorrenteRecebimento.banco.nome).as("banco")
			).as("contaCorrenteRecebimento")
		);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, NotaSaidaParcelaPesquisaTO filter) {
		// SEMPRE TRAZER NOTA DE FATURAMENTO
		builder.and(qNotaSaidaParcela.notaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO));

		// SEMPRE DEVE TER NOTA DE SAÍDA.
		if (filter.getNotaSaida() != null) {
			NotaSaidaTO notaSaidaTO = filter.getNotaSaida();

			if (notaSaidaTO.getCodigo() != null) {
				builder.and(qNotaSaidaParcela.notaSaida.codigo.eq(notaSaidaTO.getCodigo()));
			}

			// SGINV-2193
			// Foi usado o 'Expressions.booleanTemplate' para criar a expressão abaixo.
			// É retirado os caracteres não númericos com o 'REGEXP_REPLACE' e com o 'NULLIF' é removido da pesquisa os valores vazios ('')
			// Por fim é usado o 'CAST' para converter de string para BigInteger. No hibernate o BigInteger se escreve 'big_integer'.
			if (notaSaidaTO.getNumero() != null && !notaSaidaTO.getNumero().isEmpty()) {
				builder.and(
					Expressions.booleanTemplate("CAST( NULLIF(REGEXP_REPLACE({0}, '[^0-9]*', '', 'g'), '') as big_integer ) = {1}", qNotaSaidaParcela.notaSaida.numero, Long.parseLong(notaSaidaTO.getNumero()))
				);
			}
			if (notaSaidaTO.getStatus() != null && !notaSaidaTO.getStatus().isEmpty()) {
				Nota.StatusNota status = null;
				for (Nota.StatusNota situacaoNota : Nota.StatusNota.values()) {
					if (situacaoNota.getOrdemStatus() == Integer.parseInt(notaSaidaTO.getStatus())) {
						status = situacaoNota;
						break;
					}
				}
				builder.and(qNotaSaidaParcela.notaSaida.status.eq(status));
			}
			if (notaSaidaTO.getEmpresa() != null && notaSaidaTO.getEmpresa().getCodigo() != null) {
				QUnidade qUnidade = QUnidade.unidade;
				builder.and(qUnidade.codigo.eq(notaSaidaTO.getEmpresa().getCodigo()));
			}

			if (notaSaidaTO.getServicoTipo() != null && notaSaidaTO.getServicoTipo().getCodigo() != null) {
				QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
				builder.and(qServicoTipo.codigo.eq(notaSaidaTO.getServicoTipo().getCodigo()));
			}
			if (notaSaidaTO.getCentroFinanceiro() != null) {
				if (notaSaidaTO.getCentroFinanceiro().getIdentificadorFinal() != null && !notaSaidaTO.getCentroFinanceiro().getIdentificadorFinal().isEmpty()) {
					QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
					builder.and(qCentroFinanceiro.identificadorFinal.eq(notaSaidaTO.getCentroFinanceiro().getIdentificadorFinal()));
				}

				if (notaSaidaTO.getCentroFinanceiro().getContratoTO() != null && notaSaidaTO.getCentroFinanceiro().getContratoTO().getCodigo() != null) {
					QContrato qContrato = QContrato.contrato;
					builder.and(qContrato.codigo.eq(notaSaidaTO.getCentroFinanceiro().getContratoTO().getCodigo()));
				}
			}

			if (Check.isNotEmpty(notaSaidaTO.getTipoFornecimento())) {
				builder.and(qNotaSaidaParcela.notaSaida.tipoFornecimento.eq((NaturezaFornecimento.TipoFornecimento) Util.getEnumByString(NaturezaFornecimento.TipoFornecimento.class, notaSaidaTO.getTipoFornecimento())));
			}
		}

		if (filter.getNomeFantasiaDocumento() != null && !filter.getNomeFantasiaDocumento().isEmpty()) {
			QPessoa qPessoa = QPessoa.pessoa;
			QPessoaJuridica qPessoaJuridica = qPessoa.as(QPessoaJuridica.class);

			builder.and(qPessoaJuridica.nome.containsIgnoreCase(filter.getNomeFantasiaDocumento())
				.or(qPessoaJuridica.nomeFantasia.containsIgnoreCase(filter.getNomeFantasiaDocumento()))
				.or(qPessoaJuridica.documento.containsIgnoreCase(filter.getNomeFantasiaDocumento())));
		}

		if (filter.getEtapa() != null) {
			FaturamentoEtapaTO etapa = filter.getEtapa();

			// ETAPA
			if (etapa.getDescricao() != null && !etapa.getDescricao().isEmpty()) {
				QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
				builder.and(qFaturamentoEtapa.descricao.containsIgnoreCase(etapa.getDescricao()));
			}
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Emissão")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.loe(filter.getDataAte()));
		}

		if (filter.getDataEmissaoDe() != null) {
			builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.goe(filter.getDataEmissaoDe()));
		}

		if (filter.getDataEmissaoAte() != null) {
			builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.loe(filter.getDataEmissaoAte()));
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Vencimento")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.dataVencimento.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.dataVencimento.loe(filter.getDataAte()));
		}

		if (filter.getDataVencimentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataVencimento.goe(filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataVencimento.loe(filter.getDataVencimentoAte()));
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Pagamento")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.dataPagamento.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.dataPagamento.loe(filter.getDataAte()));
		}

		if (filter.getDataPagamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataPagamento.goe(filter.getDataPagamentoDe()));
		}

		if (filter.getDataPagamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataPagamento.loe(filter.getDataPagamentoAte()));
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Cancelamento")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.loe(filter.getDataAte()));
		}

		if (filter.getDataResponsavelCancelamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.goe(filter.getDataResponsavelCancelamentoDe()));
		}

		if (filter.getDataResponsavelCancelamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.loe(filter.getDataResponsavelCancelamentoAte()));
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Agendamento")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.dataAgendamento.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.dataAgendamento.loe(filter.getDataAte()));
		}

		if (filter.getDataAgendamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataAgendamento.goe(filter.getDataAgendamentoDe()));
		}

		if (filter.getDataAgendamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataAgendamento.loe(filter.getDataAgendamentoAte()));
		}

		if (Check.isAssigned(filter.getTipoData()) && filter.getTipoData().equals("Contabilização")) {
			if (Check.isAssigned(filter.getDataDe()))
				builder.and(qNotaSaidaParcela.dataContabilizacao.goe(filter.getDataDe()));
			if (Check.isAssigned(filter.getDataAte()))
				builder.and(qNotaSaidaParcela.dataContabilizacao.loe(filter.getDataAte()));
		}

		if (filter.getDataContabilizacaoDe() != null) {
			builder.and(qNotaSaidaParcela.dataContabilizacao.goe(filter.getDataContabilizacaoDe()));
		}

		if (filter.getDataContabilizacaoAte() != null) {
			builder.and(qNotaSaidaParcela.dataContabilizacao.loe(filter.getDataContabilizacaoAte()));
		}

		if (filter.getCidade() != null && !filter.getCidade().getNome().isEmpty()) {
			builder.and(QCidade.cidade.nome.containsIgnoreCase(filter.getCidade().getNome()));
		}

		if (filter.getNotaSaida() != null && filter.getNotaSaida().getNumero() != null && !filter.getNotaSaida().getNumero().isEmpty()) {
			builder.and(qNotaSaidaParcela.notaSaida.numero.containsIgnoreCase(filter.getNotaSaida().getNumero()));
		}

		if (filter.getSituacao() != null && !filter.getSituacao().isEmpty()) {
			NotaSaidaParcela.SituacaoParcela status = null;
			for (NotaSaidaParcela.SituacaoParcela situacaoNota : NotaSaidaParcela.SituacaoParcela.values()) {
				if (situacaoNota.ordinal() == Integer.parseInt(filter.getSituacao())) {
					status = situacaoNota;
					break;
				}
			}

			builder.and(qNotaSaidaParcela.situacao.eq(status));
		}

		if (filter.getContaCorrenteRecebimento() != null && filter.getContaCorrenteRecebimento().getBanco().getNome() != null) {
			builder.and(qNotaSaidaParcela.contaCorrenteRecebimento.banco.nome.containsIgnoreCase(filter.getContaCorrenteRecebimento().getBanco().getNome()));
		}

		if (filter.getContaCorrenteRecebimento() != null && filter.getContaCorrenteRecebimento().getCodigo() != null) {
			builder.and(qNotaSaidaParcela.contaCorrenteRecebimento.codigo.eq(filter.getContaCorrenteRecebimento().getCodigo()));
		}

		if (filter.getTipoDocumentoPrevisto() != null && !filter.getTipoDocumentoPrevisto().isEmpty()) {
			Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento = null;
			for (Pagamento.TipoDocumentoPagamento tdp : Pagamento.TipoDocumentoPagamento.values()) {
				if (tdp.getDescricao().equals(filter.getTipoDocumentoPrevisto())) {
					tipoDocumentoPagamento = tdp;
					break;
				}
			}

			builder.and(qNotaSaidaParcela.tipoDocumentoPrevisto.eq(tipoDocumentoPagamento));
		}

		// Usuário com esse perfil só pode ser notas dos CLs associados a ele.
		if (usuarioService.possuiUmPerfilLista("faturamento-cns-gerencia")) {
			List<CentroFinanceiroTO> list = usuarioService.getCentroFinanceiros();
			List<Long> codigos = new ArrayList<>();
			list.forEach(cf -> codigos.add(cf.getCodigo()));
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			builder.and(qCentroFinanceiro.codigo.in(codigos).or(QCentroFinanceiro.centroFinanceiro.pai.codigo.in(codigos)));
		}
	}

	@Override
	protected void doAfterSearch(List<NotaSaidaParcela> entities, List<NotaSaidaParcelaPesquisaTO> result) throws ServiceException {
		for (NotaSaidaParcelaPesquisaTO to : result) {
			FaturamentoTO faturamentoTO = faturamentoService.obterNomeClientePorNotaSaida(to.getNotaSaida().getCodigo());
			// FIXME: Por algum motivo, a projeção não traz o protocolo de entrega. Rever isse bug.
			to.getNotaSaida().setProtocoloEntrega(notaSaidaService.getProtocoloEntrega(to.getNotaSaida().getCodigo()));
			if (faturamentoTO != null) {
				to.setNomeFantasiaDocumento(faturamentoTO.getCliente().getNome());
			}
		}
	}

	@Override
	protected void doAfterLoad(NotaSaidaParcelaTO to) {
		if (to != null && to.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(to.getCriador());
			if (list != null && !list.isEmpty()) {
				to.setCriador(list.get(0).getNome());
			}
		}

		if (to != null && to.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(to.getAtualizador());
			if (list != null && !list.isEmpty()) {
				to.setAtualizador(list.get(0).getNome());
			}
		}

		if (to != null && to.getResponsavelRecebimento() != null) {
			Pessoa usuario = pessoaFisicaRepository.findById(to.getResponsavelRecebimento().getCodigo()).orElse(null);
			to.setResponsavelRecebimento(new PessoaTO(usuario.getCodigo(), usuario.getNome()));
		}


		if (to != null && to.getDataResponsavelRecebimento() != null) {
			to.setDataResponsavelRecebimento(to.getDataResponsavelRecebimento());
		}

		if (to != null && to.getResponsavelCancelamento() != null) {
			Pessoa usuario = pessoaFisicaRepository.findById(to.getResponsavelCancelamento().getCodigo()).orElse(null);
			to.setResponsavelCancelamento(new PessoaTO(usuario.getCodigo(), usuario.getNome()));
		}

		if (to != null && to.getDataResponsavelCancelamento() != null) {
			to.setDataResponsavelCancelamento(to.getDataResponsavelCancelamento());
		}

		Long codigoNotaSaida = to.getNotaSaida().getCodigo();
		to.setNotaSaida(getMapper().map(notaSaidaRepository.findById(codigoNotaSaida).orElse(null), NotaSaidaTO.class));
		FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.pesquisarFaturamentoEtapaPorCodigoNotaSaidaComCodigoUnidadeCentroFinanceiro(codigoNotaSaida);
		faturamentoEtapaTO.setFaturamento(getMapper().map(faturamentoRepository.findById(faturamentoEtapaTO.getFaturamento().getCodigo()).orElse(null), FaturamentoTO.class));
		to.getNotaSaida().setFaturamentoEtapaTO(faturamentoEtapaTO);
	}

	/**
	 * Busca um StatusNota que tenha o mesmo OrderStatus usado no parâmetro.
	 *
	 * @param ordemStatus
	 * @return Nota.StatusNota
	 */
	private Nota.StatusNota getEnumStatusNotaByOrdemStatus(Long ordemStatus) {

		Nota.StatusNota[] lista = Nota.StatusNota.values();

		for (Nota.StatusNota sn : lista) {
			if (sn.getOrdemStatus() == ordemStatus) {
				return sn;
			}
		}

		return null;
	}

	/**
	 * Busca uma lista de NotaSaidaParcela pelo ID da nota de saida.
	 *
	 * @param codigoNotaSaida
	 * @return
	 */
	@Override
	public List<NotaSaidaParcelaTO> getListaParcelaByNotaSaidaId(Long codigoNotaSaida) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QPessoa qPessoa = QPessoa.pessoa;

		QBean qBean = Projections.bean(
			NotaSaidaParcela.class,
			qNotaSaidaParcela.codigo,
			qNotaSaidaParcela.ordem,
			qNotaSaidaParcela.dataVencimento,
			qNotaSaidaParcela.valorBruto,
			qNotaSaidaParcela.valorDesconto,
			qNotaSaidaParcela.valorDevolucao,
			qNotaSaidaParcela.valorJuros,
			qNotaSaidaParcela.valorMulta,
			qNotaSaidaParcela.valorPermuta,
			qNotaSaidaParcela.valorLiquido,
			qNotaSaidaParcela.situacao,
			qNotaSaidaParcela.dataPagamento,
			qNotaSaidaParcela.dataContabilizacao,
			qNotaSaidaParcela.dataResponsavelRecebimento,
			Projections.bean(Pessoa.class, qPessoa.codigo, qPessoa.nome).as("responsavelRecebimento")
		);

		JPAQuery<NotaSaidaParcela> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qNotaSaidaParcela)
			.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
			.leftJoin(qNotaSaidaParcela.responsavelRecebimento, qPessoa)
			.where(qNotaSaida.codigo.eq(codigoNotaSaida));

		List<NotaSaidaParcela> lista = jpaQuery.fetch();
		List<NotaSaidaParcelaTO> listaTO = map(lista);

		boolean isIssRetidoCliente = notaSaidaService.isIssRetidoCliente(codigoNotaSaida);

		for (NotaSaidaParcelaTO nsp : listaTO) {
			List<NotaTributoAliquotaTO> list = notaTributoAliquotaService.listarPorCodigoNotaPorOrdem(codigoNotaSaida, nsp.getOrdem());
			nsp.setValorTributo(BigDecimal.ZERO);
			// Calculando os tributos
			for (NotaTributoAliquotaTO nta : list) {
				// tributo Iss
				if (nta.getAliquota().getCodigoTributo() == 4) {
					if (isIssRetidoCliente) {
						nsp.setValorTributo(nsp.getValorTributo().add(nta.getValor()));
					}
				} else {
					nsp.setValorTributo(nsp.getValorTributo().add(nta.getValor()));
				}
			}
			// Calculando as retenções
			nsp.setParcelaRetencoes(mapSearchParcelaRetencaoTO(notaSaidaParcelaRetencaoRepository.findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcela(nsp.getCodigo())));
			if (!nsp.getParcelaRetencoes().isEmpty()) {
				for (NotaSaidaParcelaRetencaoTO retencao : nsp.getParcelaRetencoes()) {
					if ("Contratual".equals(retencao.getTipo())) {
						nsp.setValorRetencaoContratual(retencao.getValorRetido());
					} else if ("Acessória".equals(retencao.getTipo())) {
						nsp.setValorRetencaoContratual(retencao.getValorRetido());
					}
				}
			}
		}

		return listaTO;
	}

	@Override
	public NotaSaidaParcelaTO findByCodigo(Long codigo) {
		return map(select().where(qNotaSaidaParcela.codigo.eq(codigo)).fetchOne());
	}

	@Override
	public JPAQuery jpaQueryNotaSaidaParcel() {
		return select();
	}

	/**
	 * FIXME: As contas correntes estão com valores certos. Mas os Bancos das contas correntes estão com valores errados.
	 *
	 * @param codigo
	 * @return
	 */
	@Override
	public JPAQuery<?> obterNotaSaidaParcelaBasica(Long codigo) {
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.ordem,
				Projections.fields(ContaCorrente.class,
					Projections.fields(Banco.class,
						QContaCorrente.contaCorrente.banco.codigo,
						QContaCorrente.contaCorrente.banco.nome)
						.as(QContaCorrente.contaCorrente.banco),
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.codigo,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.agencia,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.digitoAgencia,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.conta,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.digito,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.principal,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.homologado,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.dataHomologacao,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.tipoContaCorrente,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.internetBanking,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.chequeCarbono,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.identificadorCliente,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.identificadorFise,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.prazoProcessamentoIb,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.boletoFaturamento,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.conciliacao,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista.chequeInicial
				).as(QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista),
				Projections.fields(ContaCorrente.class,
					Projections.fields(Banco.class,
						QContaCorrente.contaCorrente.banco.codigo,
						QContaCorrente.contaCorrente.banco.nome)
						.as(QContaCorrente.contaCorrente.banco),
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.codigo,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.agencia,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.digitoAgencia,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.conta,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.digito,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.principal,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.homologado,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.dataHomologacao,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.tipoContaCorrente,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.internetBanking,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.chequeCarbono,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.identificadorCliente,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.identificadorFise,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.prazoProcessamentoIb,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.boletoFaturamento,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.conciliacao,
					QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento.chequeInicial
				).as(QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento),
				Projections.fields(Pessoa.class,
					QNotaSaidaParcela.notaSaidaParcela.responsavelCancelamento.codigo,
					QNotaSaidaParcela.notaSaidaParcela.responsavelCancelamento.nome
				).as(QNotaSaidaParcela.notaSaidaParcela.responsavelCancelamento),
				Projections.fields(Pessoa.class,
					QNotaSaidaParcela.notaSaidaParcela.responsavelRecebimento.codigo,
					QNotaSaidaParcela.notaSaidaParcela.responsavelRecebimento.nome
				).as(QNotaSaidaParcela.notaSaidaParcela.responsavelRecebimento)
			)
		).leftJoin(QNotaSaidaParcela.notaSaidaParcela.contaCorrentePrevista, QContaCorrente.contaCorrente)
			.leftJoin(QContaCorrente.contaCorrente.banco, QBanco.banco)
			.leftJoin(QNotaSaidaParcela.notaSaidaParcela.contaCorrenteRecebimento, QContaCorrente.contaCorrente)
			.leftJoin(QNotaSaidaParcela.notaSaidaParcela.responsavelCancelamento, QPessoa.pessoa)
			.leftJoin(QNotaSaidaParcela.notaSaidaParcela.responsavelRecebimento, QPessoa.pessoa)
			.where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigo));
	}

	@Override
	public JPAQuery<?> findNotaSaidaByCodigoNotaSaidaParcela(Long codigo) {
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.dataContabilizacao,
				Projections.fields(NotaSaida.class,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.codigo,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.numero,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.status,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.dataEmissao,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.valor,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.issRetidoCliente,
					QNotaSaidaParcela.notaSaidaParcela.notaSaida.issRetidoClienteRecebimento,
					Projections.fields(Unidade.class,
						QNotaSaidaParcela.notaSaidaParcela.notaSaida.empresa.codigo,
						QNotaSaidaParcela.notaSaidaParcela.notaSaida.empresa.codnome,
						Projections.fields(Unidade.class,
							QUnidade.unidade.matriz.codigo,
							QUnidade.unidade.matriz.nome).as(QUnidade.unidade.matriz)
					).as(QNotaSaidaParcela.notaSaidaParcela.notaSaida.empresa),
					Projections.fields(CentroFinanceiro.class,
						QNotaSaidaParcela.notaSaidaParcela.notaSaida.centroFinanceiro.codigo,
						QNotaSaidaParcela.notaSaidaParcela.notaSaida.centroFinanceiro.identificadorFinal,
						QNotaSaidaParcela.notaSaidaParcela.notaSaida.centroFinanceiro.nome,
						Projections.fields(Contrato.class,
							QCentroFinanceiro.centroFinanceiro.contrato.codigo
						).as(QCentroFinanceiro.centroFinanceiro.contrato)
					).as(QNotaSaidaParcela.notaSaidaParcela.notaSaida.centroFinanceiro)
				).as(QNotaSaidaParcela.notaSaidaParcela.notaSaida)
			)
		).where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigo))
			.leftJoin(QNotaSaidaParcela.notaSaidaParcela.notaSaida.empresa, QUnidade.unidade)
			.leftJoin(QUnidade.unidade.matriz, QUnidade.unidade)
			.leftJoin(QNotaSaidaParcela.notaSaidaParcela.notaSaida.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(QCentroFinanceiro.centroFinanceiro.contrato, QContrato.contrato);
	}

	@Override
	public JPAQuery<?> findNotaSaidaByCodigoNotaSaida(Long codigoNotaSaida) {
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.valorBruto,
				QNotaSaidaParcela.notaSaidaParcela.valorDesconto,
				QNotaSaidaParcela.notaSaidaParcela.valorDevolucao,
				QNotaSaidaParcela.notaSaidaParcela.valorJuros,
				QNotaSaidaParcela.notaSaidaParcela.valorLiquido,
				QNotaSaidaParcela.notaSaidaParcela.valorMulta,
				QNotaSaidaParcela.notaSaidaParcela.valorPermuta,
				QNotaSaidaParcela.notaSaidaParcela.situacao
			)
		).where(QNotaSaidaParcela.notaSaidaParcela.notaSaida.codigo.eq(codigoNotaSaida));
	}

	/**
	 * Traz todas as parcelas, menos as Finalizadas.
	 *
	 * @param codigoNotaSaida O código da Nota de saída.
	 * @return A query para a pesquisa de parcelas.
	 */
	@Override
	public JPAQuery<?> findNotaSaidaByCodigoNotaSaidaNotFinalizada(Long codigoNotaSaida) {
		if (codigoNotaSaida == null) {
			return null;
		}
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.valorBruto,
				QNotaSaidaParcela.notaSaidaParcela.valorDesconto,
				QNotaSaidaParcela.notaSaidaParcela.valorDevolucao,
				QNotaSaidaParcela.notaSaidaParcela.valorJuros,
				QNotaSaidaParcela.notaSaidaParcela.valorLiquido,
				QNotaSaidaParcela.notaSaidaParcela.valorMulta,
				QNotaSaidaParcela.notaSaidaParcela.valorPermuta,
				QNotaSaidaParcela.notaSaidaParcela.situacao
			)
		).where(QNotaSaidaParcela.notaSaidaParcela.notaSaida.codigo.eq(codigoNotaSaida)
			.and(QNotaSaidaParcela.notaSaidaParcela.situacao.eq(NotaSaidaParcela.SituacaoParcela.FINALIZADA).not()));
	}

	@Override
	public JPAQuery<?> obterNotaSaidaParcelaApenasComCodigo(Long codigo) {
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo))
			.where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigo));
	}

	private List<NotaSaidaParcelaRetencaoTO> mapSearchParcelaRetencaoTO(Iterable<NotaSaidaParcelaRetencao> iterable) {
		return dozerHelper.map(iterable, NotaSaidaParcelaRetencaoTO.class);
	}

	@Override
	public NotaSaidaParcelaTO obterNotaSaidaParcelaBasicaCodigoOrdem(Long codigo) {
		NotaSaidaParcela nsp = select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.ordem
			)
		).where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigo)).fetchOne();
		return map(nsp);
	}

	@Override
	public JPAQuery<?> obterJpaQueryNotaSaidaParcelaBasicaCodigoSituacao(Long codigo) {
		return select(
			Projections.fields(NotaSaidaParcela.class,
				QNotaSaidaParcela.notaSaidaParcela.codigo,
				QNotaSaidaParcela.notaSaidaParcela.situacao
			)
		).where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigo));
	}

	@Override
	@Transactional()
	public void updateAtualizadorAndData(Long codigo) {
		selectUpdate()
			.where(qNotaSaidaParcela.codigo.eq(codigo))
			.set(qNotaSaidaParcela.atualizador, securityService.getUsername())
			.set(qNotaSaidaParcela.dataAtualizacao, new Date())
			.execute();
	}

	@Transactional()
	private void updateSituacao(Long codigo, NotaSaidaParcela.SituacaoParcela situacao) {
		selectUpdate()
			.where(qNotaSaidaParcela.codigo.eq(codigo))
			.set(qNotaSaidaParcela.situacao, situacao)
			.set(qNotaSaidaParcela.atualizador, securityService.getUsername())
			.set(qNotaSaidaParcela.dataAtualizacao, new Date())
			.execute();
	}

	@Override
	@Transactional()
	public void updateSituacaoEmRecebimento(Long codigo) {
		updateSituacao(codigo, NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO);
	}

	@Override
	@Transactional()
	public void updateSituacaoAberta(Long codigo) {
		updateSituacao(codigo, NotaSaidaParcela.SituacaoParcela.ABERTA);
	}

	@Override
	@Transactional()
	public void updateSituacaoFinalizada(Long codigo) {
		updateSituacao(codigo, NotaSaidaParcela.SituacaoParcela.FINALIZADA);
	}

	@Override
	@Transactional()
	public void updateSituacaoCancelada(Long codigo) {
		updateSituacao(codigo, NotaSaidaParcela.SituacaoParcela.CANCELADA);
	}

	@Override
	@Transactional()
	public void cancelamento(Long codigo) {
		selectUpdate()
			.where(qNotaSaidaParcela.codigo.eq(codigo))
			.set(qNotaSaidaParcela.dataResponsavelCancelamento, new Date())
			.set(qNotaSaidaParcela.responsavelCancelamento, securityService.getUser().getPessoaFisica())
			.set(qNotaSaidaParcela.situacao, NotaSaidaParcela.SituacaoParcela.CANCELADA)
			.set(qNotaSaidaParcela.atualizador, securityService.getUsername())
			.set(qNotaSaidaParcela.dataAtualizacao, new Date())
			.execute();
	}

	@Override
	@Transactional()
	public void atualizarBaixaRetencaoRemovidaValor(long codigoNotaSaidaParcela, BigDecimal valorBaixa) {
		NotaSaidaParcela notaSaidaParcela = select(Projections.fields(NotaSaidaParcela.class, qNotaSaidaParcela.valorRetencaoRetida, qNotaSaidaParcela.saldoRetencao, qNotaSaidaParcela.valorBaixaRetencao))
			.where(qNotaSaidaParcela.codigo.eq(codigoNotaSaidaParcela))
			.fetchOne();

		BigDecimal valorBaixaRetencao = notaSaidaParcela.getValorBaixaRetencao();

		valorBaixaRetencao = valorBaixaRetencao.subtract(valorBaixa);
		BigDecimal saldoRetencao = notaSaidaParcela.getValorRetencaoRetida().subtract(valorBaixaRetencao);

		selectUpdate()
			.where(qNotaSaidaParcela.codigo.eq(codigoNotaSaidaParcela))
			.set(qNotaSaidaParcela.valorBaixaRetencao, valorBaixaRetencao)
			.set(qNotaSaidaParcela.saldoRetencao, saldoRetencao)
			.execute();
	}

	@Override
	@Transactional()
	public void atualizarBaixaRetencaoValor(long codigoNotaSaidaParcela, BigDecimal valorBaixa, BigDecimal valorJuros) {
		NotaSaidaParcela notaSaidaParcela = select(Projections.fields(NotaSaidaParcela.class, qNotaSaidaParcela.valorRetencaoRetida, qNotaSaidaParcela.saldoRetencao, qNotaSaidaParcela.valorBaixaRetencao))
			.where(qNotaSaidaParcela.codigo.eq(codigoNotaSaidaParcela))
			.fetchOne();

		BigDecimal valorBaixaRetencao = notaSaidaParcela.getValorBaixaRetencao().add(valorBaixa).add(valorJuros == null ? BigDecimal.ZERO : valorJuros);
		BigDecimal saldoRetencao = notaSaidaParcela.getValorRetencaoRetida().subtract(valorBaixaRetencao);;

		selectUpdate()
			.where(qNotaSaidaParcela.codigo.eq(codigoNotaSaidaParcela))
			.set(qNotaSaidaParcela.valorBaixaRetencao, valorBaixaRetencao)
			.set(qNotaSaidaParcela.saldoRetencao, saldoRetencao)
			.execute();
	}

	@Override
	public ValueTO verificarLoteLoteContabilDiario(long codigoNotaSaidaParcela) throws ValidationException {
		NotaSaidaParcela notaSaidaParcela = (NotaSaidaParcela) findNotaSaidaByCodigoNotaSaidaParcela(codigoNotaSaidaParcela).fetchOne();

		if (notaSaidaParcela == null) {
			throw new ValidationException("DIARIO", "Parcela não existe");
		}

		QBean qBean = Projections.bean(LoteInterface.class, QLoteInterface.loteInterface.codigo);

		JPAQuery<LoteInterface> jpaQueryLoteInterface = getJPAQueryFactory().selectFrom(QLoteInterface.loteInterface);

		jpaQueryLoteInterface
			.select(qBean)
			.from(QLoteInterface.loteInterface)
			.where(
				QLoteInterface.loteInterface.dataEvento.eq(notaSaidaParcela.getDataContabilizacao())
					.and(QLoteInterface.loteInterface.tipoLoteInterface.eq(LoteInterface.TipoLoteInterface.Baixas_diarias))
					.and(QLoteInterface.loteInterface.unidade.codigo.eq(notaSaidaParcela.getNotaSaida().getEmpresa().getCodigo()))
			);

		ValueTO valueTO;
		if (jpaQueryLoteInterface.fetchCount() > 0) {
			valueTO = ValueTO.Builder.newInstance().value(true).build();
		} else {
			valueTO = ValueTO.Builder.newInstance().value(false).build();
		}

		return valueTO;
	}

}
