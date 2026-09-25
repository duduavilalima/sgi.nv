package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterfaceNota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.BancoService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceFaturamentoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaMotivoDescDevService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaRetencaoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.ParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.araujoabreu.sgi.service.security.UsuarioService;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.*;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.contaCorrente.ContaCorrenteRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRetencaoRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceFaturamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRetencaoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ParcelaServiceImpl extends BaseSearchService<NotaSaidaParcela, NotaSaidaParcelaTO, NotaSaidaParcelaPesquisaTO, NotaSaidaParcelaRepository> implements ParcelaService {

	private static final QNotaSaidaParcela qNotaSaidaParcela = QNotaSaidaParcela.notaSaidaParcela;
	private static final QLoteInterfaceNota qLoteInterfaceNota = QLoteInterfaceNota.loteInterfaceNota;
	private static boolean PARCELAS_CONCLUIDAS = true;
	private static boolean EM_RECEBIMENTO = false;
	private static List<NotaTributoAliquota> LISTA_TRIBUTOS = new ArrayList<>();

	@Autowired
	private FeriadoService feriadoService;

	@Autowired
	private NotaSaidaParcelaRetencaoService notaSaidaParcelaRetencaoService;

	@Autowired
	private NotaSaidaParcelaMotivoDescDevService notaSaidaParcelaMotivoDescDevService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private LoteInterfaceFaturamentoRepository loteInterfaceFaturamentoRepository;

	@Autowired
	private FaturamentoRetencaoRepository faturamentoRetencaoRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private NotaSaidaParcelaRetencaoRepository notaSaidaParcelaRetencaoRepository;

	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;

	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;

	@Autowired
	private AliquotaRepository aliquotaRepository;

	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private DozerHelper dozerHelper;

	@Autowired
	private FaturamentoService faturamentoService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private BancoService bancoService;

	@Autowired
	private ContaCorrenteService contaCorrenteService;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private LoteInterfaceFaturamentoService loteInterfaceFaturamentoService;

	@Autowired
	public ParcelaServiceImpl(NotaSaidaParcelaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaSaidaParcela> jpaQuery, NotaSaidaParcelaPesquisaTO filter) {
		if (filter.getNotaSaida() != null) {
			QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida);

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
					jpaQuery.join(qNotaSaida.centroFinanceiro, qCentroFinanceiro).join(qCentroFinanceiro.contrato, qContrato);
				}
			}
		}

		if (filter.getNomeFantasiaDocumento() != null && !filter.getNomeFantasiaDocumento().isEmpty()) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

			QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida);

			jpaQuery.join(qNotaSaida.faturamentoEtapas, qFaturamentoEtapa);

			QFaturamento qFaturamento = QFaturamento.faturamento;
			jpaQuery.join(qFaturamentoEtapa.faturamento, qFaturamento);

			QPessoa qPessoa = QPessoa.pessoa;
			jpaQuery.join(qFaturamento.cliente, qPessoa);
		}

		if (filter.getEtapa() != null) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			QFaturamento qFaturamento = QFaturamento.faturamento;

			jpaQuery.join(qNotaSaidaParcela.notaSaida.faturamentoEtapas, qFaturamentoEtapa);
			jpaQuery.join(qFaturamentoEtapa.faturamento, qFaturamento);
		}

		if (filter.getContaCorrenteRecebimento() != null) {
			QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
			QBanco qBanco = QBanco.banco;
			jpaQuery.join(qNotaSaidaParcela.contaCorrenteRecebimento, qContaCorrente);
			jpaQuery.join(qContaCorrente.banco, qBanco);
		}

		if (filter.getCidade() != null) {
			QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
			QContrato qContrato = QContrato.contrato;
			QUnidade qUnidade = QUnidade.unidade;
			QEndereco qEndereco = QEndereco.endereco1;
			QCidade qCidade = QCidade.cidade;
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
				.join(qNotaSaida.centroFinanceiro, qCentroFinanceiro)
				.join(qCentroFinanceiro.contrato, qContrato)
				.join(qContrato.unidade, qUnidade)
				.join(qUnidade.endereco, qEndereco)
				.join(qEndereco.cidade, qCidade);
		}

		if (filter.getCodigoLote() != null) {
			QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
			jpaQuery.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
				.where(
					qNotaSaida.codigo.in(JPAExpressions
						.selectFrom(qLoteInterfaceNota)
						.where(qLoteInterfaceNota.loteInterface.codigo.eq(filter.getCodigoLote()))
						.select(qLoteInterfaceNota.nota.codigo)
					)
				);
		}

		// FIXME: Usuário com esse perfil só pode ser notas dos CLs associados a ele.
		if (usuarioService.possuiUmPerfilLista("faturamento-cns-gerencia")) {
			List<CentroFinanceiroTO> list = usuarioService.getCentroFinanceiros();
			List<Long> codigos = new ArrayList<>();
			list.forEach(cf -> codigos.add(cf.getCodigo()));
			QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
			jpaQuery.where(qNotaSaida.centroFinanceiro.codigo.in(codigos).or(qNotaSaida.centroFinanceiro.pai.codigo.in(codigos)));
		}

		jpaQuery.select(createQBean());

		jpaQuery.orderBy(qNotaSaidaParcela.notaSaida.numero.desc(), qNotaSaidaParcela.ordem.desc());
	}

	protected QBean createQBean() {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

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
			qNotaSaidaParcela.tipoDocumentoRecebimento,
			Projections.fields(NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.status,
				qNotaSaida.issRetidoCliente,
				qNotaSaida.dataEmissao,
				qNotaSaida.tipoFornecimento
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

			if (notaSaidaTO.getNumero() != null && !notaSaidaTO.getNumero().isEmpty()) {
				builder.and(qNotaSaidaParcela.notaSaida.numero.like("%" + String.format("%09d", Long.parseLong(notaSaidaTO.getNumero())) + "%"));
			}

			if (notaSaidaTO.getStatus() != null && !notaSaidaTO.getStatus().isEmpty()) {
				builder.and(qNotaSaidaParcela.notaSaida.status.eq(getEnumStatusNotaByOrdemStatus(Long.parseLong(notaSaidaTO.getStatus()))));
			} else {
				List<Nota.StatusNota> list = new ArrayList<>();
			}

			if (notaSaidaTO.getEmpresa() != null && notaSaidaTO.getEmpresa().getCodigo() != null) {
				QUnidade qUnidade = QUnidade.unidade;
				builder.and(qUnidade.codigo.eq(notaSaidaTO.getEmpresa().getCodigo()));
			}

			if (notaSaidaTO.getServicoTipo() != null && notaSaidaTO.getServicoTipo().getCodigo() != null) {
				QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
				builder.and(qServicoTipo.codigo.eq(notaSaidaTO.getServicoTipo().getCodigo()));
			}

			if (Check.isNotEmpty(notaSaidaTO.getTipoFornecimento())) {
				builder.and(qNotaSaidaParcela.notaSaida.tipoFornecimento.eq((NaturezaFornecimento.TipoFornecimento) Util.getEnumByString(NaturezaFornecimento.TipoFornecimento.class, notaSaidaTO.getTipoFornecimento())));
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

			// FATURAMENTO
			if (etapa.getFaturamento() != null) {
				// BANCO
				if (etapa.getFaturamento().getContaCorrente() != null && etapa.getFaturamento().getContaCorrente().getBanco() != null && etapa.getFaturamento().getContaCorrente().getBanco().getCodigo() != null) {
					//QFaturamento qFaturamento = QFaturamento.faturamento;
					QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
					builder.and(qContaCorrente.banco.codigo.eq(etapa.getFaturamento().getContaCorrente().getBanco().getCodigo()));
				}

				// CONTA CORRENTE
				if (etapa.getFaturamento().getContaCorrente() != null && etapa.getFaturamento().getContaCorrente().getCodigo() != null) {
					QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
					builder.and(QContaCorrente.contaCorrente.codigo.eq(etapa.getFaturamento().getContaCorrente().getCodigo()));
				}

				// DOCUMENTO PAGAMENTO
				if (Check.isNotEmpty(etapa.getFaturamento().getTipoDocumentoPagamento())) {
					for (Pagamento.TipoDocumentoPagamento documentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {
						if (documentoPagamento.getDescricao().equals(etapa.getFaturamento().getTipoDocumentoPagamento())) {
							QFaturamento qFaturamento = QFaturamento.faturamento;
							builder.and(qFaturamento.tipoDocumentoPagamento.eq(documentoPagamento));
							break;
						}
					}
				}

			}

		}

		if (filter.getCodigo() != null) {
			builder.and(qNotaSaidaParcela.codigo.eq(filter.getCodigo()));
		}

		if (filter.getContaCorrenteRecebimento() != null) {
			if (filter.getContaCorrenteRecebimento().getCodigo() != null) {
				QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
				builder.and(qContaCorrente.codigo.eq(filter.getContaCorrenteRecebimento().getCodigo()));
			}
			if (filter.getContaCorrenteRecebimento().getBanco() != null && filter.getContaCorrenteRecebimento().getBanco().getCodigo() != null) {
				QBanco qBanco = QBanco.banco;
				builder.and(qBanco.codigo.eq(filter.getContaCorrenteRecebimento().getBanco().getCodigo()));
			}
		}

		if (filter.getSituacao() != null && !filter.getSituacao().isEmpty()) {
			builder.and(qNotaSaidaParcela.situacao.eq(getEnumSituacaoParcela(filter.getSituacao())));
		} else {
			List<NotaSaidaParcela.SituacaoParcela> list = new ArrayList<>();
			list.add(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO);
			list.add(NotaSaidaParcela.SituacaoParcela.FINALIZADA);
			list.add(NotaSaidaParcela.SituacaoParcela.CANCELADA);
			builder.and(qNotaSaidaParcela.situacao.in(list));
		}

		if (filter.getDataEmissaoDe() != null) {
			builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.goe(filter.getDataEmissaoDe()));
		}

		if (filter.getDataEmissaoAte() != null) {
			builder.and(qNotaSaidaParcela.notaSaida.dataEmissao.loe(filter.getDataEmissaoAte()));
		}

		if (filter.getDataVencimentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataVencimento.goe(filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataVencimento.loe(filter.getDataVencimentoAte()));
		}

		if (filter.getDataContabilizacaoDe() != null) {
			builder.and(qNotaSaidaParcela.dataContabilizacao.goe(filter.getDataContabilizacaoDe()));
		}

		if (filter.getDataContabilizacaoAte() != null) {
			builder.and(qNotaSaidaParcela.dataContabilizacao.loe(filter.getDataContabilizacaoAte()));
		}

		if (filter.getDataPagamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataPagamento.goe(filter.getDataPagamentoDe()));
		}

		if (filter.getDataPagamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataPagamento.loe(filter.getDataPagamentoAte()));
		}

		if (filter.getDataAgendamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataAgendamento.goe(filter.getDataAgendamentoDe()));
		}

		if (filter.getDataAgendamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataAgendamento.loe(filter.getDataAgendamentoAte()));
		}

		if (filter.getDataResponsavelCancelamentoDe() != null) {
			builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.goe(filter.getDataResponsavelCancelamentoDe()));
		}

		if (filter.getDataResponsavelCancelamentoAte() != null) {
			builder.and(qNotaSaidaParcela.dataResponsavelCancelamento.loe(filter.getDataResponsavelCancelamentoDe()));
		}

		if (filter.getValorDe() != null && filter.getValorDe().doubleValue() > 0) {
			builder.and(qNotaSaidaParcela.valorBruto.goe(filter.getValorDe()));
		}

		if (filter.getValorAte() != null && filter.getValorAte().doubleValue() > 0) {
			builder.and(qNotaSaidaParcela.valorBruto.loe(filter.getValorAte()));
		}

		if (filter.getValorLiquido() != null && filter.getValorLiquido().doubleValue() > 0) {
			builder.and(qNotaSaidaParcela.valorLiquido.eq(filter.getValorLiquido()));
		}

		if (filter.getCidade() != null && !filter.getCidade().getNome().isEmpty()) {
			builder.and(QCidade.cidade.nome.containsIgnoreCase(filter.getCidade().getNome()));
		}


		if (filter.getTipoDocumentoPrevisto() != null) {
			//FIXME: Melhorar código.
			for (Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento : Pagamento.TipoDocumentoPagamento.values()) {
				if (tipoDocumentoPagamento.getDescricao().equalsIgnoreCase(filter.getTipoDocumentoPrevisto())) {
					builder.and(qNotaSaidaParcela.tipoDocumentoPrevisto.eq(tipoDocumentoPagamento));
					break;
				}
			}
		}
	}

	@Override
	protected void doAfterSearch(List<NotaSaidaParcela> entities, List<NotaSaidaParcelaPesquisaTO> result) throws ServiceException {
		for (NotaSaidaParcelaPesquisaTO to : result) {
			NotaSaidaParcela nsp = getRepository().findById(to.getCodigo()).orElse(null);

			List<FaturamentoEtapaTO> listaFA = faturamentoEtapaService.obterFaturamentoEtapaPorNotaSaida(to.getNotaSaida().getCodigo());

			to.setContaCorrenteRecebimento(montarContaCorrente(nsp.getContaCorrenteRecebimento()));
			to.setParcelaRetencoes(carregaRetencoesParcela(nsp.getCodigo()));
			to.setOrdem(to.getOrdem());

			FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.obterFaturamentoEtapaPorNotaSaida2(to.getNotaSaida().getCodigo());

			if (faturamentoEtapaTO != null) {
				to.getNotaSaida().setFaturamentoEtapaTO(faturamentoEtapaTO);

				if (Pessoa.TipoPessoa.PESSOA_FISICA.toString().equals(faturamentoEtapaTO.getFaturamento().getCliente().getTipoPessoa())) {
					to.setNomeFantasiaDocumento(faturamentoEtapaTO.getFaturamento().getCliente().getNome());
				} else {
					to.setNomeFantasiaDocumento(pessoaJuridicaService.obterPessoaFisicaApenasNomeFantasia(faturamentoEtapaTO.getFaturamento().getCliente().getCodigo()));
				}
			}
		}
	}

	@Override
	public void calcularValorLiquido(NotaSaidaParcelaPesquisaTO to) {
		BigDecimal liquido = to.getValorBruto();
		BigDecimal tributos = BigDecimal.ZERO;
		BigDecimal retencoes = BigDecimal.ZERO;
		BigDecimal permuta = to.getValorPermuta();
		BigDecimal desconto = to.getValorDesconto();
		BigDecimal multa = to.getValorMulta();
		BigDecimal juros = to.getValorJuros();

		retencoes = calcularRetencoesParcela(to);

		if (to.getOrdem() == 1) {
			tributos = calcularTributosParcela(to.getNotaSaida().getCodigo(), to.getValorBruto());
		}

		to.setValorLiquido(liquido.subtract(tributos.add(retencoes.add(permuta.add(desconto)))).add(multa.add(juros)));
	}

	/**
	 * Calcular os valores das retenções de uma parcela.
	 *
	 * @param to A parcela.
	 * @return O valor total das rentenções.
	 */
	public BigDecimal calcularRetencoesParcela(NotaSaidaParcelaPesquisaTO to) {
		BigDecimal retencoes = new BigDecimal(0d);
		for (NotaSaidaParcelaRetencaoTO notaSaidaParcelaRetencaoTO : to.getParcelaRetencoes()) {
			if (to.getSituacao().equals(NotaSaidaParcela.SituacaoParcela.FINALIZADA.toString())) {
				retencoes = retencoes.add(notaSaidaParcelaRetencaoTO.getValorRetido());
			} else {
				retencoes = retencoes.add(notaSaidaParcelaRetencaoTO.getValorAReter());
			}
		}

		return retencoes;
	}

	/**
	 * Calcular os valores de tributos de uma nota.
	 *
	 * @param codigoNota O código da nota.
	 * @return O valor total de tributos.
	 */
	public BigDecimal calcularTributosParcela(Long codigoNota, BigDecimal valorParcela) {
		BigDecimal tributos = new BigDecimal(0d);
		List<NotaTributoAliquota> list = notaTributoAliquotaRepository.findByNotaCodigoOrderByCodigoAsc(codigoNota);

		Boolean issRetidoCliente = notaSaidaService.isIssRetidoCliente(codigoNota);
		if (issRetidoCliente == null) {
			issRetidoCliente = Boolean.TRUE;
		}

		if (!list.isEmpty()) {
			for (NotaTributoAliquota notaTributoAliquota : list) {
				if (notaTributoAliquota.getAliquota().getCodigo() != null) {
					Aliquota aliquota = aliquotaRepository.findById(notaTributoAliquota.getAliquota().getCodigo()).orElse(null);
					if (aliquota != null) {
						Long codigoTributo = aliquota.getCategoriaTributo().getTributo().getCodigo();

						// 4 = codigo do tributo iss
						// É necessário verificar se o ISS será retido pelo cliente. Se o ISS não for retido pelo cliente, então o valor do tributo ISS não é adicionado ao total dos tributos.
						if (issRetidoCliente || codigoTributo != 4) {
							tributos = tributos.add((valorParcela.multiply(aliquota.getPercentual()).divide(BigDecimal.valueOf(100d))));
						}
					}
				}
			}
		}
		return tributos;
	}

	private ContaCorrenteTO montarContaCorrente(ContaCorrente contaCorrente) {
		ContaCorrenteTO contaCorrenteTO = getMapper().map(contaCorrente, ContaCorrenteTO.class);
		if (contaCorrenteTO.getDigito() != null && !contaCorrenteTO.getDigito().trim().equals("")) {
			contaCorrenteTO.setConta(contaCorrenteTO.getConta() + "-" + contaCorrenteTO.getDigito());
		}
		if (contaCorrenteTO.getDigitoAgencia() != null && !contaCorrenteTO.getDigitoAgencia().trim().equals("")) {
			contaCorrenteTO.setAgencia(contaCorrenteTO.getAgencia() + "-" + contaCorrenteTO.getDigitoAgencia());
		}
		return contaCorrenteTO;
	}

	@Override
	protected void doBeforeSave(NotaSaidaParcela entity, NotaSaidaParcelaTO parcelaTO) {
		prepareValidateToSave(parcelaTO);
		if (parcelaTO.getResponsavelRecebimento() != null) {
			if (entity.getResponsavelRecebimento() == null ||
				(entity.getResponsavelRecebimento() != null && !Objects.equals(entity.getResponsavelRecebimento().getCodigo(), parcelaTO.getResponsavelRecebimento().getCodigo()))) {
				entity.setResponsavelRecebimento(mergeTO(parcelaTO.getResponsavelRecebimento(), new Pessoa()));
			}
		}

		buscarListaTributos(parcelaTO);
	}

	@Transactional
	protected void doAfterSave(NotaSaidaParcela entity, NotaSaidaParcelaTO object) throws ServiceException {
		if (entity.getOrdem() == 0 && EM_RECEBIMENTO) {
			if (PARCELAS_CONCLUIDAS) {
				notaSaidaRepository.updateStatus(entity.getNotaSaida().getCodigo(), Nota.StatusNota.RECEBIDA_FATURAMENTO);
			}
			NotaSaida notaSaida = getRepository().findNotaSaidaByCodigoNotaSaidaParcela(entity.getCodigo());
			notaSaida.setListaNotaTributoAliquota(LISTA_TRIBUTOS);
			LISTA_TRIBUTOS = new ArrayList<>();
			entity.setNotaSaida(notaSaidaRepository.save(notaSaida));
		} else {
			if (PARCELAS_CONCLUIDAS || parcelasRecebidas(entity.getNotaSaida().getCodigo())) {
				notaSaidaRepository.updateStatus(entity.getNotaSaida().getCodigo(), Nota.StatusNota.RECEBIDA_FATURAMENTO);
			}
		}
	}

	/**
	 * Verifica se todas as parcelas de uma nota já foram recebidas
	 *
	 * @param codigoNotaSaida
	 * @return
	 */
	private Boolean parcelasRecebidas(Long codigoNotaSaida) {
		List<NotaSaidaParcela.SituacaoParcela> lista = getRepository().findSituacaoNotaSaidaParcelaByCodigoNotaSaida(codigoNotaSaida);
		int itensRecebidos = 0;

		for (NotaSaidaParcela.SituacaoParcela situacao : lista) {
			if (NotaSaidaParcela.SituacaoParcela.FINALIZADA.equals(situacao) || NotaSaidaParcela.SituacaoParcela.CANCELADA.equals(situacao)) {
				itensRecebidos++;
			}
		}

		if (lista.size() == itensRecebidos) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	@Override
	protected void doAfterLoad(NotaSaidaParcelaTO to) {
		NotaSaidaParcela nsp = (NotaSaidaParcela) notaSaidaParcelaService.obterNotaSaidaParcelaBasica(to.getCodigo()).fetchOne();

		adicionarBancos(nsp);

		NotaSaida notaSaida = ((NotaSaidaParcela) notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaParcela(nsp.getCodigo())
			.fetchOne()).getNotaSaida();

		NotaSaidaTO notaSaidaTO = new NotaSaidaTO();

		notaSaidaTO = getMapper().map(notaSaida, NotaSaidaTO.class);
		adicionarMatriz(notaSaidaTO);

		notaSaidaTO.setNumero(notaSaida.getNumero());

		to.setNotaSaida(notaSaidaTO);

		FaturamentoEtapa faturamentoEtapa = (FaturamentoEtapa) faturamentoEtapaService.obterFaturamentoEtapaSimplesListaCodigoNotaSaida(notaSaida.getCodigo()).fetchFirst();

		to.setContaCorrentePrevista(montarContaCorrente(nsp.getContaCorrentePrevista()));
		to.setContaCorrenteRecebimento(montarContaCorrente(nsp.getContaCorrenteRecebimento()));

		to.getNotaSaida().setFaturamentoEtapaTO(getMapper().map(faturamentoEtapa, FaturamentoEtapaTO.class));

		to.setParcelaRetencoes(carregaRetencoesParcela(nsp.getCodigo()));

		to.setValorRetencaoContratual(BigDecimal.ZERO);
		to.setValorRetencaoAcessoria(BigDecimal.ZERO);
		for (NotaSaidaParcelaRetencaoTO nspr : to.getParcelaRetencoes()) {
			if ("Contratual".equals(nspr.getTipo())) {
				to.setValorRetencaoContratual(to.getValorRetencaoContratual().add(nspr.getValorRetido()));
			} else if ("Acessória".equals(nspr.getTipo())) {
				to.setValorRetencaoAcessoria(to.getValorRetencaoAcessoria().add(nspr.getValorRetido()));
			}
		}

		to.setOrdem(to.getOrdem());

		to.setListaNotaSaidaParcelaMotivoDescDev(notaSaidaParcelaMotivoDescDevService.findNotaSaidaParcelaMotivoDescDev(nsp.getCodigo()));

		if (to != null && to.getCriador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(to.getCriador());
			if (list != null && !list.isEmpty()) {
				to.setNomeCriador(list.get(0).getNome());
			} else {
				to.setNomeCriador(to.getCriador());
			}
		}

		if (to != null && to.getAtualizador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(to.getAtualizador());
			if (list != null && !list.isEmpty()) {
				to.setNomeAtualizador(list.get(0).getNome());
			} else {
				to.setNomeAtualizador(to.getAtualizador());
			}
		}

		if (nsp != null && nsp.getResponsavelRecebimento() != null) {
			to.setResponsavelRecebimento(new PessoaTO(nsp.getResponsavelRecebimento().getCodigo(), nsp.getResponsavelRecebimento().getNome()));
		}

		if (to != null && to.getDataResponsavelRecebimento() != null) {
			to.setDataResponsavelRecebimento(to.getDataResponsavelRecebimento());
		}

		if (nsp != null && nsp.getResponsavelCancelamento() != null) {
			to.setResponsavelCancelamento(new PessoaTO(nsp.getResponsavelCancelamento().getCodigo(), nsp.getResponsavelCancelamento().getNome()));
		}

		if (to != null && to.getDataResponsavelCancelamento() != null) {
			to.setDataResponsavelCancelamento(to.getDataResponsavelCancelamento());
		}
	}

	private void adicionarBancos(NotaSaidaParcela notaSaidaParcela) {
		if (notaSaidaParcela != null && notaSaidaParcela.getContaCorrentePrevista() != null && notaSaidaParcela.getContaCorrentePrevista().getCodigo() != null) {
			ContaCorrente contaCorrentePrevista = (ContaCorrente) contaCorrenteService.obterBancoDeContaCorrente(notaSaidaParcela.getContaCorrentePrevista().getCodigo()).fetchOne();
			notaSaidaParcela.getContaCorrentePrevista().setBanco(contaCorrentePrevista.getBanco());
		}

		if (notaSaidaParcela != null && notaSaidaParcela.getContaCorrenteRecebimento() != null && notaSaidaParcela.getContaCorrenteRecebimento().getCodigo() != null) {
			ContaCorrente contaCorrenteRecebimento = (ContaCorrente) contaCorrenteService.obterBancoDeContaCorrente(notaSaidaParcela.getContaCorrenteRecebimento().getCodigo()).fetchOne();
			notaSaidaParcela.getContaCorrenteRecebimento().setBanco(contaCorrenteRecebimento.getBanco());
		}
	}

	private void adicionarMatriz(NotaSaidaTO notaSaidaTO) {
		if (notaSaidaTO != null && notaSaidaTO.getEmpresa() != null && notaSaidaTO.getEmpresa().getCodigo() != null) {
			UnidadeTO unidadeTO = unidadeService.obterUnidade(notaSaidaTO.getEmpresa().getCodigo());
			notaSaidaTO.getEmpresa().setMatriz(unidadeTO.getMatriz());
		}
	}

	@Override
	protected void doBeforeUpdate(NotaSaidaParcela entity, NotaSaidaParcelaTO object) {
		entity.setContaCorrentePrevista(getMapper().map(object.getContaCorrentePrevista(), ContaCorrente.class));
		entity.setContaCorrenteRecebimento(getMapper().map(object.getContaCorrenteRecebimento(), ContaCorrente.class));
		if (object.getAction().equals("receber")) {
			verificarValorLiquido(object);
			verificarDataContabilizacao(object.getDataContabilizacao());
			entity.setResponsavelRecebimento(getMapper().map(pessoaFisicaService.findByUsuarioLogin(getUser().getUsername()).get(0), PessoaFisica.class));
			notaSaidaRepository.updateIssRetidoCliente(object.getNotaSaida().getCodigo(), object.getNotaSaida().getIssRetidoCliente());
		}
		entity.setValorLiquido(object.getValorLiquido());
	}

	public void prepareValidateToSave(NotaSaidaParcelaTO parcelaTO) throws ValidationException {
		//Validações de campos obrigatórios
		if (parcelaTO.getDataVencimento() == null)
			throw new ValidationException("ValidationException","A data de vencimento é um campo de preenchimento obrigatório.");

		if (parcelaTO.getDataPagamento() == null)
			throw new ValidationException("ValidationException","A data de pagamento é um campo de preenchimento obrigatório.");

		if (parcelaTO.getContaCorrentePrevista() == null)
			throw new ValidationException("ValidationException","Agência/Conta é um campo de preenchimento obrigatório.");

		if (parcelaTO.getContaCorrenteRecebimento() == null && parcelaTO.getAction() != null && parcelaTO.getAction().equals("editar"))
			parcelaTO.setContaCorrenteRecebimento(parcelaTO.getContaCorrentePrevista());

		if (parcelaTO.getContaCorrenteRecebimento() == null)
			throw new ValidationException("ValidationException","Agência/Conta é um campo de preenchimento obrigatório.");

		if (parcelaTO.getTipoDocumentoPrevisto() == null)
			throw new ValidationException("ValidationException","Tipo de documento para o pagamento do cliente é um campo de preenchimento obrigatório.");

		if (parcelaTO.getTipoDocumentoRecebimento() == null && parcelaTO.getAction() != null && parcelaTO.getAction().equals("editar"))
			parcelaTO.setTipoDocumentoRecebimento(parcelaTO.getTipoDocumentoPrevisto());

		if (parcelaTO.getTipoDocumentoRecebimento() == null)
			throw new ValidationException("ValidationException","Tipo de documento para o pagamento do cliente é um campo de preenchimento obrigatório.");

		if (parcelaTO.getValorPermuta() == null)
			throw new ValidationException("ValidationException","Valor permuta é um campo de preenchimento obrigatório.");

		if (parcelaTO.getValorDesconto() == null)
			throw new ValidationException("ValidationException","Valor desconto é um campo de preenchimento obrigatório.");

		if (parcelaTO.getValorMulta() == null)
			throw new ValidationException("ValidationException","Valor da multa é um campo de preenchimento obrigatório.");

		if (parcelaTO.getValorJuros() == null)
			throw new ValidationException("ValidationException","Valor do juros é um campo de preenchimento obrigatório.");


		//Validações de regras de negócio
		if (Util.comparaDatas(parcelaTO.getDataVencimento(), parcelaTO.getNotaSaida().getDataEmissao()) < 0)
			throw new ValidationException("ValidationException","A data de vencimento desta parcela deve ser maior ou igual a data de emissão da nota fiscal.");

		if (Util.comparaDatas(parcelaTO.getDataPagamento(), parcelaTO.getNotaSaida().getDataEmissao()) < 0)
			throw new ValidationException("ValidationException","A data de pagamento deve ser maior ou igual a data de emissão da nota fiscal.");

		if (parcelaTO.getOrdem() != null) {
			parcelaTO.setOrdem(parcelaTO.getOrdem());

			if (parcelaTO.getOrdem() > 0 && parcelaTO.getNotaSaida() != null && parcelaTO.getNotaSaida().getCodigo() != null) {
				NotaSaidaParcela parcelaAnterior = getRepository().findNotaSaidaParcelaByCodigoNotaSaidaAndOrdem(parcelaTO.getNotaSaida().getCodigo(), parcelaTO.getOrdem() - 1);

				if (parcelaAnterior != null && Util.comparaDatas(parcelaTO.getDataVencimento(), parcelaAnterior.getDataVencimento()) <= 0)
					throw new ValidationException("ValidationException","A data de vencimento desta parcela deve ser maior que a data de vencimento da parcela anterior.");
			}
		}

		if (parcelaTO.getMotivoDataVencimento() == null || parcelaTO.getMotivoDataPagamento() == null) {
			NotaSaidaParcela parcela = getRepository().findById(parcelaTO.getCodigo()).orElse(null);

			if (Util.comparaDatas(parcela.getDataVencimento(), parcelaTO.getDataVencimento()) != 0 && parcelaTO.getMotivoDataVencimento() == null)
				throw new ValidationException("ValidationException","Motivo de alteração da data de vencimento é um campo de preenchimento obrigatório.");

			if (Util.comparaDatas(parcela.getDataPagamento(), parcelaTO.getDataPagamento()) != 0 && parcelaTO.getMotivoDataPagamento() == null)
				throw new ValidationException("ValidationException","Motivo de alteração da data de pagamento é um campo de preenchimento obrigatório.");
		}

		if (parcelaTO.getAction() != null) {
			if (parcelaTO.getAction().equals("receber")) {
				EM_RECEBIMENTO = true;
				parcelaTO.setSituacao(NotaSaidaParcela.SituacaoParcela.FINALIZADA.toString());

				JPAQuery jpaQueryNotaSaidaParcela = notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaida(parcelaTO.getNotaSaida().getCodigo());

				List<NotaSaidaParcela> parcelas = jpaQueryNotaSaidaParcela.fetch();

				for (NotaSaidaParcela parcela : parcelas) {
					if ((parcela.getCodigo().doubleValue() != parcelaTO.getCodigo().doubleValue()) && (!parcela.getSituacao().equals(NotaSaidaParcela.SituacaoParcela.FINALIZADA) || !parcela.getSituacao().equals(NotaSaidaParcela.SituacaoParcela.CANCELADA))) {
						PARCELAS_CONCLUIDAS = false;
						break;
					}
				}

				if (PARCELAS_CONCLUIDAS)
					parcelaTO.getNotaSaida().setStatus(Nota.StatusNota.RECEBIDA_FATURAMENTO.toString());

				PessoaFisica pessoa = securityService.getUser().getPessoaFisica();

				if (pessoa != null) {
					//TODO Por que está mapeando manualmente?
					PessoaTO pessoaTO = new PessoaTO(pessoa.getCodigo(), pessoa.getNome());
					parcelaTO.setResponsavelRecebimento(pessoaTO);
					parcelaTO.setDataResponsavelRecebimento(new Date());
				}
			}
		}
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

	private NotaSaidaParcela.SituacaoParcela getEnumSituacaoParcela(String ordinalEnum) {
		for (NotaSaidaParcela.SituacaoParcela situacaoParcela : NotaSaidaParcela.SituacaoParcela.values()) {
			if (situacaoParcela.ordinal() == Integer.parseInt(ordinalEnum))
				return situacaoParcela;
		}

		return null;
	}

	public List<NotaSaidaParcelaRetencaoTO> carregaRetencoesParcela(Long codigoParcela) {
		List<NotaSaidaParcelaRetencaoTO> retencoes = null;

		Iterable<NotaSaidaParcelaRetencao> iterable = notaSaidaParcelaRetencaoRepository.findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcela(codigoParcela);

		if (iterable != null) {
			retencoes = dozerHelper.map(iterable, NotaSaidaParcelaRetencaoTO.class);
		}

		return retencoes;
	}

	private NotaTributoAliquota mergeTO(NotaTributoAliquotaTO object, NotaTributoAliquota entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	private Pessoa mergeTO(PessoaTO object, Pessoa entity) {
		if (object == null) return entity;
		entity = getMapper().map(object, entity.getClass());
		return entity;
	}

	/**
	 * Converte as NotaTributoAliquotas de TO para entity. E insere elas no atributo LISTA_TRIBUTOS.
	 * Esse processo só será realizado se for a primeira parcela.
	 *
	 * @param parcelaTO
	 */
	private void buscarListaTributos(NotaSaidaParcelaTO parcelaTO) {

		List<NotaTributoAliquota> listaTributos = new ArrayList<>();

		//Só para as primeiras parcelas
		if (parcelaTO.getOrdem() == 0 && parcelaTO.getListaTributos() != null && !parcelaTO.getListaTributos().isEmpty()) {

			for (NotaTributoAliquotaTO tributo : parcelaTO.getListaTributos()) {
				tributo.setCodigo(null);
				listaTributos.add(mergeTO(tributo, new NotaTributoAliquota()));
			}
		}

		LISTA_TRIBUTOS = listaTributos;
	}

	@Override
	@Transactional
	public NotaSaidaParcelaTO updateParcela(NotaSaidaParcelaTO transferObj, Long id) {
		try {
			NotaSaidaParcela notaSaidaParcela = map(transferObj);
			doBeforeUpdate(notaSaidaParcela, transferObj);
			Date data = new Date();

			if (notaSaidaParcela.getCodigo() == null) {
				notaSaidaParcela.setDataCriacao(data);
				notaSaidaParcela.setCriador(securityService.getUsername());
			} else {
				notaSaidaParcela.setDataAtualizacao(data);
				notaSaidaParcela.setAtualizador(securityService.getUsername());
			}

			transferObj.setDataPagamento(feriadoService.devolveDataUtil(transferObj.getDataVencimento()));
			transferObj.setDataAgendamento(feriadoService.devolveDataUtil(transferObj.getDataPagamento(), 1));

			getRepository().updateTipoDocumentoPrevisto(notaSaidaParcela.getTipoDocumentoPrevisto(), id);
			getRepository().updateTipoDocumentoRecebimento(notaSaidaParcela.getTipoDocumentoRecebimento(), id);
			getRepository().updateDataVencimento(transferObj.getDataVencimento(), id);
			getRepository().updateDataPagamento(transferObj.getDataPagamento(), id);
			getRepository().updateDataAgendamento(transferObj.getDataAgendamento(), id);
			getRepository().updateDataContabilizacao(transferObj.getDataContabilizacao(), id);
			getRepository().updateContaCorrentePrevista(getMapper().map(transferObj.getContaCorrentePrevista(), ContaCorrente.class), id);
			getRepository().updateContaCorrenteRecebimento(getMapper().map(transferObj.getContaCorrenteRecebimento(), ContaCorrente.class), id);

			getRepository().updateMotivoDataVencimento(transferObj.getMotivoDataVencimento(), id);
			getRepository().updateMotivoDataPagamento(transferObj.getMotivoDataPagamento(), id);

			getRepository().updateAtualizador(transferObj.getCodigo(), securityService.getUser().getLogin());
			getRepository().updateDataAtualizacao(transferObj.getCodigo(), new Date());


			for (NotaSaidaParcelaRetencaoTO notaSaidaParcelaRetencaoTO : transferObj.getParcelaRetencoes()) {
				notaSaidaParcelaRetencaoService.deleteNotaSaidaParcelaRetencaoDaParcela(id);
				notaSaidaParcelaRetencaoRepository.save(getMapper().map(notaSaidaParcelaRetencaoTO, NotaSaidaParcelaRetencao.class));
			}

			doAfterUpdate(notaSaidaParcela, transferObj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return transferObj;
	}

	@Transactional
	@Override
	public NotaSaidaParcelaTO updateParcelaRecebimento(NotaSaidaParcelaTO notaSaidaParcelaTO, Long id) throws Exception {
		verificarSeNotaEstaCancelada(id);
		try {
			verificarValorLiquido(notaSaidaParcelaTO);
			verificarDataContabilizacao(notaSaidaParcelaTO.getDataContabilizacao());
			PessoaFisica usuarioLogado = pessoaFisicaRepository.findByUsuarioLogin(getUser().getUsername()).get(0);

			boolean em_recebimento = true;
			boolean parcelas_concluidas = false;

			notaSaidaParcelaTO.setSituacao(NotaSaidaParcela.SituacaoParcela.FINALIZADA.toString());

			List<NotaSaidaParcela> parcelas = select(Projections.fields(NotaSaidaParcela.class, qNotaSaidaParcela.codigo, qNotaSaidaParcela.situacao))
				.where(qNotaSaidaParcela.notaSaida.codigo.eq(notaSaidaParcelaTO.getNotaSaida().getCodigo())).fetch();

			for (NotaSaidaParcela parcela : parcelas) {
				if ((parcela.getCodigo().equals(notaSaidaParcelaTO.getCodigo()))
					&& (!parcela.getSituacao().equals(NotaSaidaParcela.SituacaoParcela.FINALIZADA)
					|| !parcela.getSituacao().equals(NotaSaidaParcela.SituacaoParcela.CANCELADA))) {
					parcelas_concluidas = false;
					break;
				}
			}

			if (parcelas_concluidas) {
				notaSaidaRepository.updateStatus(notaSaidaParcelaTO.getNotaSaida().getCodigo(), Nota.StatusNota.RECEBIDA_FATURAMENTO);
			}

			// Novas retenções
			selectUpdate().where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(notaSaidaParcelaTO.getCodigo()))
				.set(QNotaSaidaParcela.notaSaidaParcela.valorRetencaoRetida, notaSaidaParcelaTO.getValorRetencaoRetida()).execute();
			selectUpdate().where(QNotaSaidaParcela.notaSaidaParcela.codigo.eq(notaSaidaParcelaTO.getCodigo()))
				.set(QNotaSaidaParcela.notaSaidaParcela.saldoRetencao, notaSaidaParcelaTO.getValorRetencaoRetida()).execute();

			// Retenções antigas - Remover depois
			List<NotaSaidaParcelaRetencao> listaRetencoes = notaSaidaParcelaRetencaoRepository.findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcela(notaSaidaParcelaTO.getCodigo());
			if (listaRetencoes.isEmpty()) {
				if (notaSaidaParcelaTO.getValorRetencaoAcessoria() != null && notaSaidaParcelaTO.getValorRetencaoAcessoria().doubleValue() > 0) {
					notaSaidaParcelaRetencaoService.salvarNovaNotaSaidaParcelaRetencaoACESSORIA(notaSaidaParcelaTO.getCodigo(), BigDecimal.ZERO);
				}
				if (notaSaidaParcelaTO.getValorRetencaoContratual() != null && notaSaidaParcelaTO.getValorRetencaoContratual().doubleValue() > 0) {
					notaSaidaParcelaRetencaoService.salvarNovaNotaSaidaParcelaRetencaoCONTRATUAL(notaSaidaParcelaTO.getCodigo(), notaSaidaParcelaTO.getValorRetencaoRetida());
				}
			} else {
				for (NotaSaidaParcelaRetencao parcelaRetencao : listaRetencoes) {
					for (NotaSaidaParcelaRetencaoTO parcelaRetencaoTO : notaSaidaParcelaTO.getParcelaRetencoes()) {
						if (parcelaRetencao.getTipo().getNome().equals(parcelaRetencaoTO.getTipo())) {
							if (NotaSaidaParcelaRetencao.TipoRetencao.ACESSORIA.equals(parcelaRetencao.getTipo()) && notaSaidaParcelaTO.getValorRetencaoAcessoria() != null) {
								notaSaidaParcelaRetencaoService.updateValorNotaSaidaParcelaRetencao(parcelaRetencao.getCodigo(), BigDecimal.ZERO);
							} else if (NotaSaidaParcelaRetencao.TipoRetencao.CONTRATUAL.equals(parcelaRetencao.getTipo()) && notaSaidaParcelaTO.getValorRetencaoContratual() != null) {
								notaSaidaParcelaRetencaoService.updateValorNotaSaidaParcelaRetencao(parcelaRetencao.getCodigo(), notaSaidaParcelaTO.getValorRetencaoRetida());
							}
						}
					}
				}
			}

			getRepository().updateResponsavelRecebimento(usuarioLogado, notaSaidaParcelaTO.getCodigo());
			getRepository().updateDataResponsavelRecebimento(new Date(), notaSaidaParcelaTO.getCodigo());

			notaSaidaRepository.updateIssRetidoClienteRecebimento(notaSaidaParcelaTO.getNotaSaida().getCodigo(), notaSaidaParcelaTO.getNotaSaida().getIssRetidoClienteRecebimento());

			getRepository().updateContaCorrentePrevista(getMapper().map(notaSaidaParcelaTO.getContaCorrentePrevista(), ContaCorrente.class), notaSaidaParcelaTO.getCodigo());
			getRepository().updateContaCorrenteRecebimento(getMapper().map(notaSaidaParcelaTO.getContaCorrenteRecebimento(), ContaCorrente.class), notaSaidaParcelaTO.getCodigo());

			getRepository().updateDataAtualizacao(notaSaidaParcelaTO.getCodigo(), new Date());
			getRepository().updateAtualizador(notaSaidaParcelaTO.getCodigo(), securityService.getUsername());

			getRepository().updateTipoDocumentoPrevisto(getTipoDocumentoPagamento(notaSaidaParcelaTO.getTipoDocumentoPrevisto()), id);
			getRepository().updateTipoDocumentoRecebimento(getTipoDocumentoPagamento(notaSaidaParcelaTO.getTipoDocumentoRecebimento()), id);
			getRepository().updateDataVencimento(notaSaidaParcelaTO.getDataVencimento(), id);
			getRepository().updateDataPagamento(notaSaidaParcelaTO.getDataPagamento(), id);
			getRepository().updateDataContabilizacao(notaSaidaParcelaTO.getDataContabilizacao(), id);
			getRepository().updateContaCorrentePrevista(getMapper().map(notaSaidaParcelaTO.getContaCorrentePrevista(), ContaCorrente.class), id);
			getRepository().updateContaCorrenteRecebimento(getMapper().map(notaSaidaParcelaTO.getContaCorrenteRecebimento(), ContaCorrente.class), id);

			getRepository().updateMotivoDataVencimento(notaSaidaParcelaTO.getMotivoDataVencimento(), id);
			getRepository().updateMotivoDataPagamento(notaSaidaParcelaTO.getMotivoDataPagamento(), id);

			getRepository().updateValorPermuta(notaSaidaParcelaTO.getValorPermuta() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorPermuta(), id);
			getRepository().updateValorDesconto(notaSaidaParcelaTO.getValorDesconto() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorDesconto(), id);
			getRepository().updateValorMulta(notaSaidaParcelaTO.getValorMulta() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorMulta(), id);
			getRepository().updateValorJuros(notaSaidaParcelaTO.getValorJuros() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorJuros(), id);
			getRepository().updateValorDevolucao(id, notaSaidaParcelaTO.getValorDevolucao() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorDevolucao());

			getRepository().updateSituacao(NotaSaidaParcela.SituacaoParcela.FINALIZADA, id);

			Long codigoNotaSaida = null;

			if (notaSaidaParcelaTO != null && notaSaidaParcelaTO.getNotaSaida() != null && notaSaidaParcelaTO.getNotaSaida().getCodigo() != null) {
				codigoNotaSaida = notaSaidaParcelaTO.getNotaSaida().getCodigo();
			}

			List<NotaTributoAliquota> tributos = saveUpdateNotaTributoAliquota(codigoNotaSaida, notaSaidaParcelaTO.getListaTributos(), notaSaidaParcelaTO.getOrdem());

			// FIXME: Refatorar e adicionar em um método.
			AtomicReference<BigDecimal> totalValorTributos = new AtomicReference<>(BigDecimal.ZERO);
			tributos.forEach(notaTributoAliquota -> {
				CategoriaTributo categoriaTributo = categoriaTributoRepository.findById(notaTributoAliquota.getCategoriaTributo().getCodigo()).get();
				// tributo Iss
				if (categoriaTributo.getTributo().getCodigo() == 4) {
					if (notaSaidaParcelaTO.getNotaSaida().getIssRetidoClienteRecebimento()) {
						totalValorTributos.set(totalValorTributos.get().add(notaTributoAliquota.getValor()));
					}
				} else {
					totalValorTributos.set(totalValorTributos.get().add(notaTributoAliquota.getValor()));
				}
			});

			// FIXME: Refatorar e adicionar em um método.
			BigDecimal valorLiquido = notaSaidaParcelaTO.getValorBruto()
				.subtract(totalValorTributos.get() == null ? BigDecimal.ZERO : totalValorTributos.get())
				.subtract(notaSaidaParcelaTO.getValorRetencaoRetida() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorRetencaoRetida())
				.subtract(notaSaidaParcelaTO.getValorDesconto() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorDesconto())
				.add(notaSaidaParcelaTO.getValorDevolucao() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorDevolucao())
				.add(notaSaidaParcelaTO.getValorJuros() == null ? BigDecimal.ZERO : notaSaidaParcelaTO.getValorJuros());

			getRepository().updateValorLiquido(notaSaidaParcelaTO.getCodigo(), valorLiquido);

			if (notaSaidaParcelaTO.getOrdem() == 0 && em_recebimento) {
				if (parcelas_concluidas) {
					notaSaidaRepository.updateStatus(notaSaidaParcelaTO.getNotaSaida().getCodigo(), Nota.StatusNota.RECEBIDA_FATURAMENTO);
				}
				NotaSaida notaSaida = getRepository().findNotaSaidaByCodigoNotaSaidaParcela(notaSaidaParcelaTO.getCodigo());
				notaSaidaRepository.save(notaSaida);
			} else {
				if (parcelas_concluidas || parcelasRecebidas(notaSaidaParcelaTO.getNotaSaida().getCodigo())) {
					notaSaidaRepository.updateStatus(notaSaidaParcelaTO.getNotaSaida().getCodigo(), Nota.StatusNota.RECEBIDA_FATURAMENTO);
				}
			}

			notaSaidaParcelaMotivoDescDevService.salvarNotaSaidaParcelaMotivoDescDev(notaSaidaParcelaTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return notaSaidaParcelaTO;
	}

	private Pagamento.TipoDocumentoPagamento getTipoDocumentoPagamento(String descricao) {
		switch (descricao) {
			case "Boleto":
				return Pagamento.TipoDocumentoPagamento.BOLETO;
			case "Depósito em conta/Transferência":
				return Pagamento.TipoDocumentoPagamento.DEPOSITO;
			case "DDA":
				return Pagamento.TipoDocumentoPagamento.DDA;
			case "Guia":
				return Pagamento.TipoDocumentoPagamento.GUIA;
			case "DOC":
				return Pagamento.TipoDocumentoPagamento.DOC;
			case "TED":
				return Pagamento.TipoDocumentoPagamento.TED;
			case "Concessionária":
				return Pagamento.TipoDocumentoPagamento.CONCESSIONARIA;
			case "Outros":
				return Pagamento.TipoDocumentoPagamento.OUTROS;
			default:
				return null;
		}
	}

	private void verificarSeNotaEstaCancelada(Long codigoNotaSaidaParcela) throws Exception {
		NotaSaida notaSaida = ((NotaSaidaParcela) notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaParcela(codigoNotaSaidaParcela).fetchOne()).getNotaSaida();
		if (Nota.StatusNota.CANCELADA_FATURAMENTO.equals(notaSaida.getStatus())) {
			throw new Exception("Esta parcela não pode ser recebida, pois a Nota Fiscal está cancelada.");
		}
	}

	@Transactional
	private List<NotaTributoAliquota> saveUpdateNotaTributoAliquota(Long codigoNotaSaida, List<NotaTributoAliquotaTO> lista, Integer ordemParcela) {

		List<NotaTributoAliquota> tributos = new ArrayList<>();

		if (codigoNotaSaida != null) {
			//FIXME: Usar serviço para deletar os aliquotas usando QueryDSL
			List<NotaTributoAliquota> listaNta = notaTributoAliquotaRepository.findByNotaCodigoAndOrdem(codigoNotaSaida, ordemParcela);

			if (listaNta != null && !listaNta.isEmpty()) {
				for (NotaTributoAliquota notaTributoAliquota : listaNta) {
					notaTributoAliquotaRepository.delete(notaTributoAliquota);
				}
			}
		}

		for (NotaTributoAliquotaTO to : lista) {
			NotaTributoAliquota nta = getMapper().map(to, NotaTributoAliquota.class);
			nta.setOrdem(ordemParcela);
			nta.setBaseCalculo(to.getBaseCalculo());
			nta.setValor(to.getBaseCalculo().multiply(to.getPercentual()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN));
			tributos.add(nta);
			notaTributoAliquotaRepository.save(nta);
		}

		return tributos;
	}

	@Override
	@Transactional
	public void cancelarParcelaRecebimento(Long codigoNotaSaidaParcela) {

		Long codigoNota = getRepository().findCodigoNotaSaidaByCodigoNotaSaidaParcela(codigoNotaSaidaParcela);

		getRepository().updateSituacao(NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO, codigoNotaSaidaParcela);
		notaSaidaRepository.updateStatus(codigoNota, Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO);
		notaSaidaRepository.updateStatus(codigoNota, Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO);

		PessoaFisica pessoa = securityService.getUser().getPessoaFisica();
		if (pessoa != null) {
			getRepository().updateResponsavelCancRecebimento(pessoa, codigoNotaSaidaParcela);
			getRepository().updateDataResponsavelCancRecebimento(new Date(), codigoNotaSaidaParcela);
			getRepository().updateDataContabilizacao(null, codigoNotaSaidaParcela);
		}
	}

	@Override
	@Transactional
	public NotaSaidaParcelaTO updateAgendamento(NotaSaidaParcelaTO transferObj, Long id) {
		getRepository().updateDataAgendamento(feriadoService.devolveDataUtil(transferObj.getDataAgendamento()), id);
		getRepository().updateMotivoDataAgendamento(transferObj.getMotivoDataAgendamento(), id);
		return transferObj;
	}

	@Override
	@Transactional
	public NotaSaidaParcelaTO marcarCobrancaCronica(NotaSaidaParcelaTO transferObj, Long codigoNotaSaidaParcela) {

		getRepository().updateCobrancaCronica(Boolean.TRUE, codigoNotaSaidaParcela);

		PessoaFisica pessoa = securityService.getUser().getPessoaFisica();
		if (pessoa != null) {
			getRepository().updateResponsavelMarcaCobrancaCronica(pessoa, codigoNotaSaidaParcela);
			getRepository().updateDataResponsavelMarcaCobrancaCronica(new Date(), codigoNotaSaidaParcela);
		}

		return transferObj;
	}

	@Override
	@Transactional
	public NotaSaidaParcelaTO desmarcarCobrancaCronica(NotaSaidaParcelaTO transferObj, Long codigoNotaSaidaParcela) {

		getRepository().updateCobrancaCronica(Boolean.FALSE, codigoNotaSaidaParcela);

		PessoaFisica pessoa = securityService.getUser().getPessoaFisica();
		if (pessoa != null) {
			getRepository().updateResponsavelDesmarcaCobrancaCronica(pessoa, codigoNotaSaidaParcela);
			getRepository().updateDataResponsavelDesmarcaCobrancaCronica(new Date(), codigoNotaSaidaParcela);
		}

		return transferObj;
	}

	@Override
	@Transactional
	public void salvarObservacao(NotaSaidaParcelaTO to) {
		selectUpdate()
			.set(qNotaSaidaParcela.observacoesImpressao, to.getObservacoesImpressao())
			.where(qNotaSaidaParcela.codigo.eq(to.getCodigo())).execute();

	}

	private NotaSaidaParcela getNotaSaidaParcelaById(Long id) {
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

		JPAQuery<NotaSaidaParcela> jpaQuery = select();

		QBean qBean = Projections.bean(
			NotaSaidaParcela.class,
			qNotaSaidaParcela.codigo,
			qNotaSaidaParcela.dataContabilizacao,

			Projections.bean(
				NotaSaida.class,
				qNotaSaida.codigo,

				Projections.bean(
					Unidade.class,
					qNotaSaida.empresa.codigo,
					qNotaSaida.empresa.codnome,

					Projections.bean(
						Unidade.class,
						qNotaSaida.empresa.matriz.codigo,
						qNotaSaida.empresa.matriz.codnome
					).as("matriz")

				).as("empresa")

			).as("notaSaida")

		);

		jpaQuery
			.select(qBean)
			.from(qNotaSaidaParcela)
			.join(qNotaSaidaParcela.notaSaida, qNotaSaida)
			.where(qNotaSaidaParcela.codigo.eq(id));

		return jpaQuery.fetchOne();
	}

	@Override
	public Boolean parcelaDisponivelParaLote(Long id) {
		NotaSaidaParcela notaSaidaParcela = this.getNotaSaidaParcelaById(id);
		if (notaSaidaParcela.getDataContabilizacao() != null) {
			return loteInterfaceFaturamentoService.notaFiscalDisponivelParaLoteBaixasDiarias(notaSaidaParcela.getDataContabilizacao(), notaSaidaParcela.getNotaSaida().getEmpresa().getMatriz().getCodigo());
		} else {
			return Boolean.FALSE;
		}
	}

	private void verificarDataContabilizacao(Date dataContabilizacao) throws ValidationException {
		Calendar calendarDataContabilizacao = Calendar.getInstance();
		calendarDataContabilizacao.setTime(dataContabilizacao);
		calendarDataContabilizacao.set(Calendar.HOUR_OF_DAY, 0);
		calendarDataContabilizacao.set(Calendar.MINUTE, 0);
		calendarDataContabilizacao.set(Calendar.SECOND, 0);
		Calendar calendarHoje = Calendar.getInstance();
		calendarHoje.set(Calendar.HOUR_OF_DAY, 0);
		calendarHoje.set(Calendar.MINUTE, 0);
		calendarHoje.set(Calendar.SECOND, 0);
		if (calendarDataContabilizacao.getTime().after(calendarHoje.getTime())) {
			throw new ValidationException("ValidationException","A data de contabilização informada é posterior ao dia de hoje. Recebimento não permitido.");
		}

		String msg = "A data de contabilização informada não é um dia útil ou sábado (casos especiais). Recebimento não permitido.";

		if (calendarDataContabilizacao.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			throw new ValidationException("ValidationException",msg);
		}

		if (feriadoService.isFeriado(dataContabilizacao)) {
			throw new ValidationException("ValidationException",msg);
		}
	}

	private void verificarValorLiquido(NotaSaidaParcelaTO notaSaidaParcelaTO) {

		Double liquido = 0d;
		Double totalTributos = 0d;
		for(NotaTributoAliquotaTO tributo : notaSaidaParcelaTO.getListaTributos()) {
			totalTributos += tributo.getValor().doubleValue();
		}


		if ( (notaSaidaParcelaTO != null) &&
			(notaSaidaParcelaTO.getValorBruto() != null) ){
			liquido = notaSaidaParcelaTO.getValorBruto().doubleValue() -
				totalTributos -
				notaSaidaParcelaTO.getValorRetencaoRetida().doubleValue() -
				notaSaidaParcelaTO.getValorDesconto().doubleValue() + notaSaidaParcelaTO.getValorMulta().doubleValue() +
				notaSaidaParcelaTO. getValorJuros().doubleValue() + notaSaidaParcelaTO.getValorDevolucao().doubleValue();
			if (liquido == null ) {
				liquido = 0d;
			}
		}

		if (liquido < 0d){
			throw new ValidationException("ValidationException","O valor líquido da parcela não pode ser menor que zero.");
		}

	}
}
