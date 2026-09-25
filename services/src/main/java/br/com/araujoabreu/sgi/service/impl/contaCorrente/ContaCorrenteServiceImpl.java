package br.com.araujoabreu.sgi.service.impl.contaCorrente;

import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contaCorrente.ContaCorrenteService;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrentePesquisaTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.FaixaChequeTO;
import br.com.araujoabreu.sgi.service.contasApagar.PagamentoService;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.ChequeContaCorrenteItemRelacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.ChequeContaCorrenteService;
import br.com.araujoabreu.sgi.service.tesouraria.ItemRelacaoPagamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.ItemRelacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.RelacaoBancariaService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequesEmitidosExibirTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoPagamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.tesouraria.domain.*;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import br.com.fasolti.sgi.repository.contaCorrente.ContaCorrenteRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoBancariaRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoAssociacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ContaCorrenteChequeItemRelacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoPagamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.LancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.RelacaoBancariaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ContaCorrenteServiceImpl extends BaseSearchService<ContaCorrente, ContaCorrenteTO, ContaCorrentePesquisaTO, ContaCorrenteRepository> implements ContaCorrenteService {

	private static final QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;


	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;

	@Autowired
	private RelacaoBancariaRepository relacaoBancariaRepository;

	@Autowired
	private ChequeContaCorrenteRepository chequeContaCorrenteRepository;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ContaContabilRepository contaContabilRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private RelacaoBancariaService relacaoBancariaService;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private ItemRelacaoRepository itemRelacaoRepository;

	@Autowired
	private ItemRelacaoPagamentoRepository itemRelacaoPagamentoRepository;

	@Autowired
	private ContaCorrenteChequeItemRelacaoRepository contaCorrenteChequeItemRelacaoRepository;

	@Autowired
	private ConciliacaoBancariaRepository conciliacaoBancariaRepository;

	@Autowired
	private ConciliacaoLancamentoRepository conciliacaoLancamentoRepository;

	@Autowired
	private ConciliacaoLancamentoAssociacaoRepository conciliacaoLancamentoAssociacaoRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private ChequeContaCorrenteService chequeContaCorrenteService;

	@Autowired
	private ChequeContaCorrenteItemRelacaoService chequeContaCorrenteItemRelacaoService;

	@Autowired
	private ItemRelacaoService itemRelacaoService;

	@Autowired
	private ItemRelacaoPagamentoService itemRelacaoPagamentoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	public ContaCorrenteServiceImpl(ContaCorrenteRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<ContaCorrente> jpaQuery, ContaCorrentePesquisaTO exampleObj) {
		QUnidade qUnidade = QUnidade.unidade;
		QBanco qBanco = QBanco.banco;
		jpaQuery.select(createQBean());
		jpaQuery.distinct().from(qContaCorrente).from(qUnidade).join(qContaCorrente.banco, qBanco)
			.where(qContaCorrente.pessoa.codigo.eq(qUnidade.codigo).and(qUnidade.tipoUnidade.eq(Unidade.TipoUnidade.MATRIZ)));
	}

	protected QBean createQBean() {
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
		QBanco qBanco = QBanco.banco;
		QUnidade qUnidade = QUnidade.unidade;

		return Projections.bean(
			ContaCorrente.class,
			qContaCorrente.codigo,
			qContaCorrente.agencia,
			qContaCorrente.digitoAgencia,
			qContaCorrente.conta,
			qContaCorrente.digito,
			qContaCorrente.tipoContaCorrente,
			qContaCorrente.chequeCarbono,
			qContaCorrente.internetBanking,
			qContaCorrente.boletoFaturamento,
			qContaCorrente.conciliacao,
			Projections.bean(Banco.class, qBanco.nome, qBanco.codigo).as("banco"),
			Projections.bean(Unidade.class, qUnidade.codnome, qUnidade.codigo).as("pessoa")
		);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ContaCorrentePesquisaTO exampleObj) {
		if (Check.isNotEmpty(exampleObj.getUnidadePesquisa())) {
			if (Check.isNotEmpty(exampleObj.getUnidadePesquisa().getCodigo())) {
				QUnidade qUnidade = QUnidade.unidade;
				builder.and(qUnidade.codigo.eq(exampleObj.getUnidadePesquisa().getCodigo()));
			}
		}

		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(qContaCorrente.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getPessoa())) {
			if (Check.isAssigned(exampleObj.getPessoa().getCodigo())) {
				builder.and(qContaCorrente.pessoa.codigo.eq(exampleObj.getPessoa().getCodigo()));
			}
		}

		if (Check.isAssigned(exampleObj.getBanco())) {
			if (Check.isAssigned(exampleObj.getBanco().getCodigo())) {
				builder.and(qContaCorrente.banco.codigo.eq(exampleObj.getBanco().getCodigo()));
			}
		}

		if (Check.isNotEmpty(exampleObj.getInternetBanking())) {
			builder.and(qContaCorrente.internetBanking.eq(exampleObj.getInternetBanking()));
		}

		if (Check.isAssigned(exampleObj.getTipoConta())) {
			builder.and(qContaCorrente.tipoConta.eq((DadosBancarios.TipoConta) Util.getEnumByString(DadosBancarios.TipoConta.class, exampleObj.getTipoConta())));
		}

		if (Check.isAssigned(exampleObj.getTipoContaCorrente())) {
			builder.and(qContaCorrente.tipoContaCorrente.eq((ContaCorrente.TipoContaCorrente) Util.getEnumByString(ContaCorrente.TipoContaCorrente.class, exampleObj.getTipoContaCorrente())));
		}

		if (Check.isAssigned(exampleObj.getTipoHomologacao())) {
			builder.and(qContaCorrente.tipoHomologacao.eq((DadosBancarios.TipoHomologacaoDadosBancarios) Util.getEnumByString(DadosBancarios.TipoHomologacaoDadosBancarios.class, exampleObj.getTipoHomologacao())));
		}

		if (Check.isAssigned(exampleObj.getConciliacao())) {
			builder.and(qContaCorrente.conciliacao.eq(exampleObj.getConciliacao()));
		}
	}

	@Override
	protected void doAfterSearch(List<ContaCorrente> entities, List<ContaCorrentePesquisaTO> result) throws ServiceException {
		for (ContaCorrentePesquisaTO contaCorrentePesquisaTO : result) {
			contaCorrentePesquisaTO.setUnidade(obterUnidadeDeConatCorrente(contaCorrentePesquisaTO.getCodigo()));
		}
	}


	@Override
	protected ContaCorrenteTO doAfterCreate(ContaCorrenteTO contaCorrenteTO) {
		contaCorrenteTO.setInternetBanking(Boolean.FALSE);
		contaCorrenteTO.setBoletoFaturamento(Boolean.FALSE);
		contaCorrenteTO.setChequeInicial(0L);
		return contaCorrenteTO;
	}

	@Override
	protected void doBeforeSave(ContaCorrente entity, ContaCorrenteTO contaCorrenteTO) throws ServiceException {
		prepareValidateToSave(contaCorrenteTO);
		preencheZerosAgencia(entity);
		preencheZerosConta(entity);
		populateEmpresa(entity, contaCorrenteTO);
		entity.setSituacao(Situacao.ATIVO);
	}

	@Override
	protected void doBeforeUpdate(ContaCorrente entity, ContaCorrenteTO contaCorrenteTO) throws ServiceException {
		prepareValidateToSave(contaCorrenteTO);
		preencheZerosAgencia(entity);
		preencheZerosConta(entity);
		populateEmpresa(entity, contaCorrenteTO);
		entity.setListaRelacaoBancaria(relacaoBancariaRepository.findByContaCorrenteCodigo(entity.getCodigo()));
	}

	@Override
	protected void doAfterLoad(ContaCorrenteTO transferObj) {

		transferObj.setUnidade(obterUnidadeDeConatCorrente(transferObj.getCodigo()));
		//removido no issue SGINV-551
//		transferObj.setListaFaixaDeCheques(obterFaixaDeCheques(transferObj.getChequeInicial(), transferObj.getChequeAnterior()));

		if (transferObj != null && transferObj.getCriador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(transferObj.getCriador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeCriador(list.get(0).getNome());
			}
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			List<PessoaFisicaTO> list = pessoaFisicaService.findByUsuarioLogin(transferObj.getAtualizador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeAtualizador(list.get(0).getNome());
			}
		}

		super.doAfterLoad(transferObj);
	}

	/**
	 * Retorna uma faixa de cheques.
	 *
	 * @param chequeInicial  O valor do cheque inicial.
	 * @param chequeAnterior O valor do cheque final.
	 * @return Lista de {@link FaixaChequeTO}.
	 */
	private List<FaixaChequeTO> obterFaixaDeCheques(Long chequeInicial, Long chequeAnterior) {
		int aux = 0;
		List<FaixaChequeTO> lista = new ArrayList<>();

		FaixaChequeTO faixaChequeTO = new FaixaChequeTO(chequeInicial, 0L, null);

		for (Long x = chequeInicial; x <= chequeAnterior; x++) {
			if (aux == 24) {
				Long ate = faixaChequeTO.getDe() + aux;
				faixaChequeTO.setAte(ate);
				faixaChequeTO.setDesc(String.format("%05d", faixaChequeTO.getDe()) + " - " + String.format("%05d", faixaChequeTO.getAte()));
				lista.add(faixaChequeTO);
				aux = 0;
				faixaChequeTO = new FaixaChequeTO(ate + 1L, 0L, null);
			}
			aux++;
		}

		if (!lista.isEmpty() && aux < 24) {
			aux = 0;
			faixaChequeTO = new FaixaChequeTO(lista.get(lista.size() - 1).getDe() + 1L, 0L, null);
			for (int x = aux; x <= 25; x++) {
				if (aux == 25) {
					Long ate = faixaChequeTO.getDe() + aux;
					faixaChequeTO.setAte(ate);
					faixaChequeTO.setDesc(String.format("%05d", faixaChequeTO.getDe()) + " - " + String.format("%05d", faixaChequeTO.getAte()));
					lista.add(faixaChequeTO);
				}
			}
		}

		return lista;
	}

	/**
	 * Obter a unidade de uma conta corrente.
	 *
	 * @param codigoContaCorrente O código da conta corrente.
	 * @return O unidade da conta corrente. Caso não seja encontrado, será retornado <code>null</code>.
	 */
	@Override
	public UnidadeTO obterUnidadeDeConatCorrente(Long codigoContaCorrente) {
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;

		JPAQuery<ContaCorrente> jpaQueryContaCorrente = select();
		jpaQueryContaCorrente.from(qContaCorrente);

		QBean qBean = Projections.bean(
			ContaCorrente.class,
			qContaCorrente.codigo,
			Projections.bean(
				Pessoa.class,
				qContaCorrente.pessoa.codigo
			).as("pessoa")
		);

		jpaQueryContaCorrente.where(qContaCorrente.codigo.eq(codigoContaCorrente));
		jpaQueryContaCorrente.select(qBean);
		ContaCorrente contaCorrente = jpaQueryContaCorrente.fetchOne();

		if (contaCorrente != null) {
			return unidadeService.obterUnidade(contaCorrente.getPessoa().getCodigo());
		}

		return null;
	}

	@Transactional
	@Override
	protected void doBeforeDelete(ContaCorrenteTO object) throws ServiceException {
		List<RelacaoBancaria> lista = relacaoBancariaRepository.findByContaCorrenteCodigo(object.getId());

		if (lista != null && !lista.isEmpty()) {
			throw new ValidationException("ValidationException","Não é possível excluir a conta corrente, pois a mesma é utilizada em pelo menos um relação bancária!");
		}

		List<ChequeContaCorrente> listChequeContaCorrente = chequeContaCorrenteRepository.findByContaCorrenteCodigo(object.getId());

		if (listChequeContaCorrente != null && !listChequeContaCorrente.isEmpty()) {
			throw new ValidationException("ValidationException","Não é possível excluir a conta corrente, pois há cheques associados a essa conta!");
		}
	}

	private void populateEmpresa(ContaCorrente entity, ContaCorrenteTO object) {
		entity.setPessoa(getMapper().map(object.getPessoa(), Pessoa.class));
	}

	private void preencheZerosAgencia(ContaCorrente contaCorrente) {
		if (contaCorrente.getAgencia().length() < 5) {
			String valor = contaCorrente.getAgencia();
			contaCorrente.setAgencia(String.format("%05d", Integer.parseInt(valor)));
		}
	}


	private void preencheZerosConta(ContaCorrente contaCorrente) {
		if (contaCorrente.getConta().length() < 12) {
			String valor = contaCorrente.getConta();
			contaCorrente.setConta(String.format("%012d", Long.parseLong(valor)));
		}
	}

	@Override
	public List<ChequesEmitidosExibirTO> buscarChequesContaCorrente(String codigo) {
		return buscarChequesContaCorrente(Long.parseLong(codigo));
	}

	public List<ChequesEmitidosExibirTO> buscarChequesContaCorrente(Long codigoContaCorrente) {
		List<ChequesEmitidosExibirTO> listaExibicao = new ArrayList<>();

		List<ChequeContaCorrenteTO> listaTO = chequeContaCorrenteService.obterListaChequeContaCorrenteDeContaCorrente(codigoContaCorrente);

		List<ChequeContaCorrente> lista = new ArrayList<>();

		for (ChequeContaCorrenteTO chequeContaCorrenteTO : listaTO) {
			lista.add(getMapper().map(chequeContaCorrenteTO, ChequeContaCorrente.class));
		}

		if (lista != null && !lista.isEmpty()) {

			for (ChequeContaCorrente chequeContaCorrente : lista) {

				List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaoTO = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoChequeContaCorrente(chequeContaCorrente.getCodigo());

				if (listaChequeContaCorrenteItemRelacaoTO != null && !listaChequeContaCorrenteItemRelacaoTO.isEmpty()) {
					for (ChequeContaCorrenteItemRelacaoTO chequeItem : listaChequeContaCorrenteItemRelacaoTO) {

						ItemRelacaoTO itemRelacao = itemRelacaoService.obterItemRelacaoComBanco(chequeItem.getCodigoItemRelacao());

						List<ItemRelacaoPagamentoTO> listaItensPagamento = itemRelacaoPagamentoService.obterItemRelacaoPagamentoPorCodigoItemRelacao(itemRelacao.getCodigo());

						List<Pagamento> listaPagamentos = new ArrayList<>();

						for (ItemRelacaoPagamentoTO itemRelacaoPagamento : listaItensPagamento) {
							PagamentoTO listaPagamentosTO = pagamentoService.obterPagamento(itemRelacaoPagamento.getPagamento().getCodigo());
							listaPagamentos.add(getMapper().map(listaPagamentosTO, Pagamento.class));
						}

						PessoaTO credor = null;

						if (listaPagamentos != null && !listaPagamentos.isEmpty()) {
							Pessoa favorecido = listaPagamentos.get(0).getCredor();

							if (favorecido != null && favorecido.getCodigo() != null) {

								PessoaJuridicaTO pessoaJuridicaTO = pessoaJuridicaService.obterPessoaJuridica(favorecido.getCodigo());

								if (pessoaJuridicaTO != null && pessoaJuridicaTO.getNomeFantasia() != null) {
									credor = pessoaJuridicaTO;
									credor.setNome(pessoaJuridicaTO.getNomeFantasia());
								} else {
									credor = getMapper().map(favorecido, PessoaTO.class);

									if (pessoaJuridicaTO != null) {
										credor.setNome(pessoaJuridicaTO.getNome());
									}
								}
							} else {
								credor = new PessoaTO();
							}
						}

						ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();

						to.setChequeItem(itemRelacao.getCheque());
						if (itemRelacao.getQtdPagamentos() > 1) {
							to.setNumero("—");
						} else {
							if (listaPagamentos != null && !listaPagamentos.isEmpty())
								to.setNumero(listaPagamentos.get(0).getNumero());
							else
								to.setNumero("—");
						}
						to.setQtdPagamentos(itemRelacao.getQtdPagamentos());
						to.setCredor(credor);
						if (itemRelacao.getBanco() != null) {
							to.setBanco(getMapper().map(itemRelacao.getBanco(), BancoTO.class));
						}
						if (itemRelacao.getTipoDocumentoPagamento() != null) {
							ItemRelacao itemRelacaoNovo = getMapper().map(itemRelacao, ItemRelacao.class);
							to.setDocPagamento(itemRelacaoNovo.getTipoDocumentoPagamento().getDescricao());
						}
						to.setDataPagamento(itemRelacao.getDataPagamento());
						to.setTotal(itemRelacao.getTotal());
						to.setStatus(chequeContaCorrente.getSituacao().toString());
						to.setDataCompensacao(chequeContaCorrente.getDataCompensacao());

						to.setCriacao(chequeContaCorrente.getDataCriacao());


						listaExibicao.add(to);

					}
				} else {

					ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();

					to.setCodigo(chequeContaCorrente.getCodigo());
					to.setChequeItem(chequeContaCorrente.getCheque());
					if (ChequeContaCorrente.StatusChequeContaCorrente.CANCELADO_USUARIO.equals(chequeContaCorrente.getSituacao())) {
						to.setStatus(chequeContaCorrente.getSituacao().toString());
					} else {
						to.setStatus("");
					}

					to.setCriacao(chequeContaCorrente.getDataCriacao());
					listaExibicao.add(to);

				}
			}
		}

		return listaExibicao;
	}

	@Override
	public List<ChequesEmitidosExibirTO> buscarChequesContaCorrenteNumero(Long codigoContaCorrente) {
		List<ChequesEmitidosExibirTO> listaExibicao = new ArrayList<>();
		List<ChequeContaCorrenteTO> listaTO = chequeContaCorrenteService.obterListaChequeContaCorrenteDeContaCorrente(codigoContaCorrente);
		List<ChequeContaCorrente> lista = new ArrayList<>();
		for (ChequeContaCorrenteTO chequeContaCorrenteTO : listaTO) {
			lista.add(getMapper().map(chequeContaCorrenteTO, ChequeContaCorrente.class));
		}
		if (lista != null && !lista.isEmpty()) {
			for (ChequeContaCorrente chequeContaCorrente : lista) {
				List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaoTO = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoChequeContaCorrente(chequeContaCorrente.getCodigo());

				if (listaChequeContaCorrenteItemRelacaoTO != null && !listaChequeContaCorrenteItemRelacaoTO.isEmpty()) {
					for (ChequeContaCorrenteItemRelacaoTO chequeItem : listaChequeContaCorrenteItemRelacaoTO) {
						ItemRelacaoTO itemRelacao = itemRelacaoService.obterItemRelacaoComBanco(chequeItem.getCodigoItemRelacao());
						ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();
						to.setCodigoRelacaoBancaria(itemRelacao.getRelacaoBancaria().getCodigo());
						to.setNumeroRelacaoBancaria(itemRelacao.getRelacaoBancaria().getNumero());
						to.setCodigoItemRelacao(itemRelacao.getCodigo());
						to.setChequeItem(itemRelacao.getCheque());
						listaExibicao.add(to);
					}
				} else {
					ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();
					to.setChequeItem(chequeContaCorrente.getCheque());
					to.setCriacao(chequeContaCorrente.getDataCriacao());
					listaExibicao.add(to);

				}
			}
		}
		return listaExibicao;
	}

	public void prepareValidateToSave(ContaCorrenteTO contaCorrenteTO) throws ServiceException {

		// Validações antes de persistir (edição/novo)

		if (contaCorrenteTO.getPessoa() == null || contaCorrenteTO.getPessoa().getCodigo() == null) {
			throw new ValidationException("ValidationException","Empresa é um campo obrigatório");
		}

		if (contaCorrenteTO.getTipoContaCorrente() == null) {
			throw new ValidationException("ValidationException","Tipo é um campo obrigatório");
		}

		if (contaCorrenteTO.getContaContabil() == null || contaCorrenteTO.getContaContabil().getCodigo() == null) {
			throw new ValidationException("ValidationException","Conta contábil é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getBanco() == null || contaCorrenteTO.getBanco().getCodigo() == null) {
			throw new ValidationException("ValidationException","Banco é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAgencia() == null) {
			throw new ValidationException("ValidationException","Agência é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getConta() == null) {
			throw new ValidationException("ValidationException","Conta é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getChequeCarbono() == null) {
			throw new ValidationException("ValidationException","Cheque carbono é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getChequeAnterior() == null) {
			throw new ValidationException("ValidationException","Último cheque é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesValor() == null) {
			throw new ValidationException("ValidationException","Antes valor é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getSaltoExtenso() == null) {
			throw new ValidationException("ValidationException","Salto extenso é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesExtenso() == null) {
			throw new ValidationException("ValidationException","Antes extenso é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getSaltoExtenso2() == null) {
			throw new ValidationException("ValidationException","Salto 2ª linha é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesExtenso2() == null) {
			throw new ValidationException("ValidationException","Antes 2ª linha é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getSaltoFavorecido() == null) {
			throw new ValidationException("ValidationException","Salto favorecido é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesFavorecido() == null) {
			throw new ValidationException("ValidationException","Antes favorecido é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getSaltoData() == null) {
			throw new ValidationException("ValidationException","Salto data é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesData() == null) {
			throw new ValidationException("ValidationException","Antes data é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getSaltoEndosso() == null) {
			throw new ValidationException("ValidationException","Salto endosso é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesEndosso() == null) {
			throw new ValidationException("ValidationException","Antes endosso é um campo de preenchimento obrigatório");
		}

		if (contaCorrenteTO.getAntesAssinatura() == null) {
			throw new ValidationException("ValidationException","Antes assinatura é um campo de preenchimento obrigatório");
		}
	}

	@Override
	public ConciliacaoBancariaTO buscarSaldos(ConciliacaoBancariaTO to) {
		if (to.getData() == null) {
			throw new ValidationException("ValidationException","Informe a data da conciliação.");
		}

		if (to.getContaCorrente().getCodigo() == null) {
			throw new ValidationException("ValidationException","Informe o código da conta corrente.");
		}

		ConciliacaoBancariaTO conciliacaoBancariaTO = new ConciliacaoBancariaTO();
		conciliacaoBancariaTO.setListaDeLancamentos(new ArrayList<>());

		List<ConciliacaoBancaria> listaConciliacaoBancaria = conciliacaoBancariaRepository.findByContaCorrenteCodigoAndData(to.getContaCorrente().getCodigo(), to.getData());

		if (listaConciliacaoBancaria.isEmpty()) {
			return conciliacaoBancariaTO;
		}

		List<ConciliacaoLancamento> listaConciliacaoLancamento = new ArrayList<>();

		for (ConciliacaoBancaria conciliacaoBancaria : listaConciliacaoBancaria) {
			listaConciliacaoLancamento.addAll(conciliacaoLancamentoRepository.findByConciliacaoBancariaCodigo(conciliacaoBancaria.getCodigo()));
		}

		/*
		 * É obtido o primeiro elemento pois temos certeza aque há apenas uma conciliação bancária por data de uma conta corrente.
		 * Feito a valiação (acima) onde retorna caso a lista vazia, não há problema de obter o GET(0).
		 */
		conciliacaoBancariaTO = getMapper().map(listaConciliacaoBancaria.get(0), ConciliacaoBancariaTO.class);

		conciliacaoBancariaTO.setListaDeLancamentos(new ArrayList<>());

		listaConciliacaoLancamento.sort((o1, o2) -> o1.getRegistro().compareTo(o2.getRegistro()));

		for (ConciliacaoLancamento conciliacaoLancamento : listaConciliacaoLancamento) {
			ConciliacaoLancamentoTO conciliacaoLancamentoTO = getMapper().map(conciliacaoLancamento, ConciliacaoLancamentoTO.class);

			List<ConciliacaoLancamentoAssociacao> listaConciliacaoLancamentoAssociacao = conciliacaoLancamentoAssociacaoRepository.findByConciliacaoLancamentoCodigo(conciliacaoLancamento.getCodigo());

			if (!listaConciliacaoLancamentoAssociacao.isEmpty()) {

				if (listaConciliacaoLancamentoAssociacao.size() == 1) {
					if (listaConciliacaoLancamentoAssociacao.get(0).getLancamento() != null) {
						Lancamento lancamento = lancamentoRepository.findById(listaConciliacaoLancamentoAssociacao.get(0).getLancamento().getCodigo()).orElse(null);
						conciliacaoLancamentoTO.setExibirNumero(lancamento.getNumero());
						ContaContabil contaContabil = contaContabilRepository.findById(lancamento.getContaContabil().getCodigo()).orElse(null);
						conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(String.format("%s — %s", contaContabil.getNumero(), contaContabil.getDescricao()));
					} else if (listaConciliacaoLancamentoAssociacao.get(0).getItemRelacao() != null) {
						ItemRelacao itemRelacao = itemRelacaoRepository.findById(listaConciliacaoLancamentoAssociacao.get(0).getItemRelacao().getCodigo()).orElse(null);

						List<ItemRelacaoPagamento> listaPagamentos = itemRelacaoPagamentoRepository.findByItemRelacaoCodigo(itemRelacao.getCodigo());

						if (!listaPagamentos.isEmpty()) {
							ItemRelacaoPagamento itemRelacaoPagamento = itemRelacaoPagamentoRepository.findById(listaPagamentos.get(0).getCodigo()).orElse(null);
							Pagamento pagamento = pagamentoRepository.findById(itemRelacaoPagamento.getPagamento().getCodigo()).orElse(null);

							if (listaPagamentos.size() == 1) {
								conciliacaoLancamentoTO.setExibirNumero(pagamento.getNumero());
							} else {
								conciliacaoLancamentoTO.setExibirNumero("Vários");
							}

							if (Pagamento.TipoPagamento.TRIBUTO.equals(pagamento.getTipo())) {
								conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(relacaoBancariaService.obterNomeTributo(getMapper().map(pagamento, PagamentoTO.class)));
							} else {
								Pessoa pessoa = pessoaRepository.findById(pagamento.getCredor().getCodigo()).orElse(null);

								if (pessoa != null) {
									if (pessoa instanceof PessoaJuridica) {
										conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNomeFantasia());
										conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
									} else if (pessoa instanceof PessoaFisica) {
										conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNome());
										conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
									} else {
										conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNome());
										conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
									}
								}
							}
						}
					} else if (listaConciliacaoLancamentoAssociacao.get(0).getNotaSaidaParcela() != null) {
						NotaSaidaParcela notaSaidaParcela = notaSaidaParcelaRepository.findById(listaConciliacaoLancamentoAssociacao.get(0).getNotaSaidaParcela().getCodigo()).orElse(null);
						NotaSaida notaSaida = notaSaidaRepository.findById(notaSaidaParcela.getNotaSaida().getCodigo()).orElse(null);

						conciliacaoLancamentoTO.setExibirNumero(notaSaida.getNumero());

						Pessoa pessoa = pessoaRepository.findById(notaSaida.getCredor().getCodigo()).orElse(null);

						if (pessoa instanceof PessoaJuridica) {
							conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNomeFantasia());
							conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
						} else if (pessoa instanceof PessoaFisica) {
							conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNome());
							conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
						} else {
							conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(pessoa.getNome());
							conciliacaoLancamentoTO.setCredor(getMapper().map(pessoa, PessoaTO.class));
						}
					}
				} else {
					conciliacaoLancamentoTO.setExibirNumero("Vários");
					conciliacaoLancamentoTO.setExibirCredorContaFavorecidoTributo(" — ");
				}
			}

			conciliacaoLancamentoTO.setRegistroExibicao(String.format("%05d", conciliacaoLancamentoTO.getRegistro()));
			conciliacaoBancariaTO.getListaDeLancamentos().add(conciliacaoLancamentoTO);
		}

		Pessoa criador = pessoaRepository.findByUsuario(conciliacaoBancariaTO.getCriador());

		conciliacaoBancariaTO.setCriador(criador.getNome());

		return conciliacaoBancariaTO;
	}

	@Override
	public List<ContaCorrenteTO> buscarChequesContaCorrenteMatriz(String codigopessoa) {
		UnidadeTO unidadeTO = unidadeService.obterUnidadeSimples(new Long(codigopessoa));

		if (unidadeTO == null) {
			return new ArrayList<>();
		}

		Long codigoUnidade = null;

		if (Unidade.TipoUnidade.FILIAL.equals(unidadeTO.getTipoUnidade())) {
			codigoUnidade = unidadeTO.getMatriz().getCodigo();
		} else {
			codigoUnidade = unidadeTO.getCodigo();
		}

		List<ContaCorrente> listaContaCorrente = getRepository().findByPessoaCodigo(codigoUnidade);

		List<ContaCorrenteTO> listaTO = new ArrayList<>();

		//FIXME: Melhorar mapeamento.
		for (ContaCorrente contaCorrente : listaContaCorrente) {
			listaTO.add(getMapper().map(contaCorrente, ContaCorrenteTO.class));
		}

		return listaTO;
	}

	@Override
	public Long obterChequeAnteriorPeloCodigo(long codigoContaCorrente) {
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
		JPAQuery jpaQuery = select().from(qContaCorrente);
		jpaQuery
			.from(qContaCorrente)
			.select(Projections.bean(ContaCorrente.class, qContaCorrente.codigo, qContaCorrente.chequeAnterior))
			.where(qContaCorrente.codigo.eq(codigoContaCorrente));
		ContaCorrente contaCorrente = (ContaCorrente) jpaQuery.fetchOne();
		return contaCorrente.getChequeAnterior();
	}

	/**
	 * Obter o cheque anterior de uma {@link ContaCorrente}.
	 * Caso a conta corrente não exita, será retornado 0 (zero).
	 *
	 * @param codigoContaCorrente O código da conta corrente.
	 * @return O número do cheque anterior de uma conta corrente.
	 */
	@Override
	public Long obterNumeroChequeAnterior(Long codigoContaCorrente) {
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;

		JPAQuery<ContaCorrente> jpaQuery = select();
		jpaQuery.from(qContaCorrente);

		QBean qBean = Projections.bean(
			ContaCorrente.class,
			qContaCorrente.codigo,
			qContaCorrente.chequeAnterior
		);

		// FIXME: Melhorar, retornar apenas o cheque anterior.
		jpaQuery.where(new BooleanBuilder(qContaCorrente.codigo.eq(codigoContaCorrente))).select(qBean);
		ContaCorrente contaCorrente = jpaQuery.fetchOne();

		if (contaCorrente == null) {
			return 0L;
		} else {
			return contaCorrente.getChequeAnterior();
		}
	}

	@Override
	public ContaCorrenteTO obterContaCorrenteApenasCodigoEBanco(Long codigo) {
		ContaCorrente contaCorrente = select()
			.select(Projections.fields(ContaCorrente.class,
				QContaCorrente.contaCorrente.codigo,
				Projections.fields(Banco.class,
					QBanco.banco.codigo,
					QBanco.banco.nome)
					.as(QBanco.banco)))
			.where(QContaCorrente.contaCorrente.codigo.eq(codigo))
			.fetchOne();
		return map(contaCorrente);
	}

	@Override
	public JPAQuery<?> obterBancoDeContaCorrente(Long codigoContaCorrente) {
		return select()
			.select(Projections.fields(ContaCorrente.class,
				QContaCorrente.contaCorrente.codigo,
				Projections.fields(Banco.class,
					QBanco.banco.codigo,
					QBanco.banco.nome)
					.as(QBanco.banco)))
			.where(QContaCorrente.contaCorrente.codigo.eq(codigoContaCorrente));
	}

	@Override
	public List<ContaCorrenteTO> obterContasCorrentes(ContaCorrenteTO contaCorrenteTO) {
		JPAQuery<ContaCorrente> jpaQuery = select();

		if (contaCorrenteTO.getBanco() != null && contaCorrenteTO.getBanco().getCodigo() != null) {
			jpaQuery.where(QContaCorrente.contaCorrente.banco.codigo.eq(contaCorrenteTO.getBanco().getCodigo()));
		}

		if (contaCorrenteTO.getPessoa() != null && contaCorrenteTO.getPessoa().getCodigo() != null) {
			jpaQuery.where(QContaCorrente.contaCorrente.pessoa.codigo.eq(contaCorrenteTO.getPessoa().getCodigo()));
		}

		if (contaCorrenteTO.getTipoContaCorrente() != null && ContaCorrente.TipoContaCorrente.NORMAL.toString().equals(contaCorrenteTO.getTipoContaCorrente())) {
			jpaQuery.where(QContaCorrente.contaCorrente.tipoContaCorrente.eq(ContaCorrente.TipoContaCorrente.NORMAL));
		}

		List<ContaCorrente> list = jpaQuery.fetch();

		list.sort((o1, o2) -> o1.getBanco().getNome().compareTo(o2.getBanco().getNome()));
		return map(list);
	}

	@Override
	public ContaCorrenteTO findByCodigo(Long codigo) {
		return map(select().where(qContaCorrente.codigo.eq(codigo)).fetchOne());
	}

	@Override
	public ContaCorrenteTO obterContaCorrenteSimpleTelaNotaFiscal(Long codigoContaCorrente) {

		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(ContaCorrente.class,
				qContaCorrente.codigo,
				qContaCorrente.agencia,
				qContaCorrente.conta,
				Projections.bean(Banco.class, qContaCorrente.banco.codigo).as("banco")

			)
		);
		jpaQuery.where(qContaCorrente.codigo.eq(codigoContaCorrente));

		return map((ContaCorrente) jpaQuery.fetchOne());
	}

	@Override
	public List<ChequesEmitidosExibirTO> buscarChequesContaCorrenteNumero(Long codigoContaCorrente, Long chequeAnterior, Long chequeFinal) {
		List<ChequesEmitidosExibirTO> listaExibicao = new ArrayList<>();
		List<ChequeContaCorrenteTO> listaTO = chequeContaCorrenteService.obterListaChequeContaCorrenteDeContaCorrente(codigoContaCorrente, chequeAnterior, chequeFinal);
		List<ChequeContaCorrente> lista = new ArrayList<>();
		for (ChequeContaCorrenteTO chequeContaCorrenteTO : listaTO) {
			lista.add(getMapper().map(chequeContaCorrenteTO, ChequeContaCorrente.class));
		}
		if (lista != null && !lista.isEmpty()) {
			for (ChequeContaCorrente chequeContaCorrente : lista) {
				List<ChequeContaCorrenteItemRelacaoTO> listaChequeContaCorrenteItemRelacaoTO = chequeContaCorrenteItemRelacaoService.obterChequeContaCorrenteItemRelacaoPorCodigoChequeContaCorrente(chequeContaCorrente.getCodigo());

				if (listaChequeContaCorrenteItemRelacaoTO != null && !listaChequeContaCorrenteItemRelacaoTO.isEmpty()) {
					for (ChequeContaCorrenteItemRelacaoTO chequeItem : listaChequeContaCorrenteItemRelacaoTO) {
						ItemRelacaoTO itemRelacao = itemRelacaoService.obterItemRelacaoComBanco(chequeItem.getCodigoItemRelacao());
						ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();
						to.setCodigoRelacaoBancaria(itemRelacao.getRelacaoBancaria().getCodigo());
						to.setNumeroRelacaoBancaria(itemRelacao.getRelacaoBancaria().getNumero());
						to.setCodigoItemRelacao(itemRelacao.getCodigo());
						to.setChequeItem(itemRelacao.getCheque());
						listaExibicao.add(to);
					}
				} else {
					ChequesEmitidosExibirTO to = new ChequesEmitidosExibirTO();
					to.setChequeItem(chequeContaCorrente.getCheque());
					to.setCriacao(chequeContaCorrente.getDataCriacao());
					listaExibicao.add(to);

				}
			}
		}
		return listaExibicao;
	}
}
