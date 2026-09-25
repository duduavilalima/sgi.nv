package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import br.com.araujoabreu.sgi.service.tesouraria.ChequesService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.araujoabreu.sgi.tesouraria.DocumentPrintEntity;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacaoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.QItemRelacaoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.RelacaoBancaria;
import br.com.fasolti.corporativo.pessoa.domain.*;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import br.com.fasolti.sgi.repository.categoriaTributo.CategoriaTributoRepository;
import br.com.fasolti.sgi.repository.contaCorrente.ContaCorrenteRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaRepository;
import br.com.fasolti.sgi.repository.tesouraria.DadosBancariosRepository;
import br.com.fasolti.sgi.repository.tesouraria.DocumentPrintEntityRepository;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.PrintStatus;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.TiposImpressao;
import br.com.fasolti.sgi.repository.tributo.TributoRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ChequesServiceImpl implements ChequesService {

	//FIXME Transformar em Enum
	final String ASSIN = "assin";
	final String CONTA = "conta";
	final String NOME_BANCO = "nomeBanco";
	final String CODIGO_BANCO = "codigoBanco";
	final String ESPACO_DUPLO = "  ";
	final String DIVERSOS = "diversos";

	@Autowired
	private DocumentPrintEntityRepository documentPrintEntityRepository;
	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private DadosBancariosRepository dadosBancariosRepository;
	@Autowired
	private NotaTributoAliquotaRepository notaTributoAliquotaRepository;
	@Autowired
	private AliquotaRepository aliquotaRepository;
	@Autowired
	private CategoriaTributoRepository categoriaTributoRepository;
	@Autowired
	private TributoRepository tributoRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private UnidadeRepository unidadeRepository;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private Mapper mapper;

	@Override
	public Long imprimirTodosCheques(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador) {
		StringBuilder builder = new StringBuilder();

		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		/*
		 * Para relação bancária com informação de pagamento individual, será criado um cheque
		 * para cada pagamento. Se não, criar um único cheque para todos os pagamentos.
		 */
		if (relacaoBancaria.getPagamentoIndividual() != null && relacaoBancaria.getPagamentoIndividual()) {
			builder.append(obterTextoChequeIndividual(listaItensRelacao, relacaoBancaria));
		} else {
			builder.append(obterTextoChequeNaoIndiv(listaItensRelacao, relacaoBancaria));
		}

		DocumentPrintEntity documentPrintEntity = createDocumentPrint(builder.toString(), TiposImpressao.CHEQUE, colaborador);
		documentPrintEntityRepository.save(documentPrintEntity);

		return documentPrintEntity.getCodigo();
	}


	@Override
	public Long imprimirTodosChequesEndosso(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador) {
		StringBuilder builder = new StringBuilder();

		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		/*
		 * Para relação bancária com informação de pagamento individual, será criado um endosso
		 * para cada pagamento. Se não, criar um único cheque para todos os pagamentos.
		 */
		if (relacaoBancaria.getPagamentoIndividual() != null && relacaoBancaria.getPagamentoIndividual()) {
			builder.append(obterTextoChequesEndossoIndividual(listaItensRelacao, relacaoBancaria));
		} else {
			builder.append(endosso(listaItensRelacao, relacaoBancaria));
		}

		DocumentPrintEntity documentPrintEntity = createDocumentPrint(builder.toString(), TiposImpressao.ENDOSSO, colaborador);
		documentPrintEntityRepository.save(documentPrintEntity);

		return documentPrintEntity.getCodigo();
	}

	public String endosso(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria) {
		StringBuilder builder = new StringBuilder();

		relacaoBancaria.setContaCorrente(mapper.map(contaCorrenteRepository.findById(relacaoBancaria.getContaCorrente().getCodigo()).orElse(null), ContaCorrenteTO.class));

		// Saltos configurado na conta corrente.
		for (int x = 1; x <= relacaoBancaria.getContaCorrente().getSaltoEndosso(); x++) {
			builder.append(GlobalBean.SALTO);
		}

		// Montar o endosso do cheque.
		builder.append(endossoCheque(listaItensRelacao, relacaoBancaria, false));

		int linhas = builder.toString().split(GlobalBean.SALTO).length;

		/*
		 * Todo cheque tem um máximo de saltos, aqui preenchemos os
		 * saltos que faltam para preencher um cheque.
		 */
		for (int x = linhas; x < GlobalBean.SALTOS_MAX_CHEQUE; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO + GlobalBean.SALTO);
		}

		return builder.toString();
	}


	@Override
	public Long imprimirTodosChequesCopia(List<ItemRelacaoTO> listaItensRelacao, RelacaoBancariaTO relacaoBancaria, String colaborador) {
		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		/*
		 * Para relação bancária com informação de pagamento individual, será criado um cheque (cópia)
		 * para cada pagamento. Se não, criar um único cheque para todos os pagamentos.
		 */
		if (relacaoBancaria.getPagamentoIndividual() != null && relacaoBancaria.getPagamentoIndividual()) {
			StringBuilder builder = new StringBuilder();
			for (String copia : obterTextoChequeCopiaIndividual(listaItensRelacao, relacaoBancaria)) {
				builder.append(copia);
			}
			DocumentPrintEntity documentPrintEntity = createDocumentPrint(builder.toString(), TiposImpressao.COPIA, colaborador);
			documentPrintEntityRepository.save(documentPrintEntity);

			return documentPrintEntity.getCodigo();
		} else {
			DocumentPrintEntity documentPrintEntity = createDocumentPrint(obterChequeCopia(listaItensRelacao, relacaoBancaria, null, false), TiposImpressao.COPIA, colaborador);
			documentPrintEntityRepository.save(documentPrintEntity);

			return documentPrintEntity.getCodigo();
		}
	}

	public String obterTextoChequeNaoIndiv(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria) {
		Unidade unidade = new JPAQuery<Unidade>(entityManager)
			.from(QUnidade.unidade)
			.join(QUnidade.unidade.endereco)
			.select(Projections.fields(Unidade.class, QUnidade.unidade.codigo,
				Projections.bean(Endereco.class, QUnidade.unidade.endereco.codigo).as("endereco")))
			.where(QUnidade.unidade.codigo.eq(relacaoBancaria.getUnidade().getCodigo()))
			.fetchOne();

		Endereco endereco = new JPAQuery<Endereco>(entityManager)
			.from(QEndereco.endereco1)
			.select(Projections.fields(Endereco.class, QEndereco.endereco1.codigo,
				Projections.bean(Cidade.class, QCidade.cidade.codigo, QCidade.cidade.nome).as("cidade")))
			.where(QEndereco.endereco1.codigo.eq(unidade.getEndereco().getCodigo()))
			.fetchOne();

		ContaCorrente contaCorrente = contaCorrenteRepository.findById(relacaoBancaria.getContaCorrente().getCodigo()).orElse(null);

		return obterImpressaoCheque(
			mapper.map(contaCorrente, ContaCorrenteTO.class),
			relacaoBancaria.getTotal(),
			obterPagamentoACheque(listaItensRelacao, relacaoBancaria, false),
			montarDataCheque(endereco.getCidade().getNome(), relacaoBancaria.getDataPagamento(), false));
	}


	@Override
	public Long imprimirChequesCopiaSelecionados(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria) {
		StringBuilder builder = new StringBuilder();

		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		for (String copia : obterTextoChequeCopiaIndividual(listaItensRelacao, relacaoBancaria)) {
			builder.append(copia);
		}

		DocumentPrintEntity documentPrintEntity = createDocumentPrint(builder.toString(), (TiposImpressao) tipo, colaborador);
		documentPrintEntityRepository.save(documentPrintEntity);

		return documentPrintEntity.getCodigo();
	}


	@Override
	public Long imprimirChequesEndossoSelecionados(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria) {
		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		DocumentPrintEntity documentPrintEntity = createDocumentPrint(obterTextoChequesEndossoIndividual(listaItensRelacao, relacaoBancaria), TiposImpressao.ENDOSSO, colaborador);
		documentPrintEntityRepository.save(documentPrintEntity);

		return documentPrintEntity.getCodigo();
	}


	@Override
	public Long imprimirChequesSelecionadosIndividual(List<ItemRelacaoTO> listaItensRelacao, Enum tipo, String colaborador, RelacaoBancariaTO relacaoBancaria) {
		listaItensRelacao.sort((o1, o2) -> o1.getCheque().compareTo(o2.getCheque()));

		DocumentPrintEntity documentPrintEntity = createDocumentPrint(obterTextoChequeIndividual(listaItensRelacao, relacaoBancaria), (TiposImpressao) tipo, colaborador);
		documentPrintEntityRepository.save(documentPrintEntity);

		return documentPrintEntity.getCodigo();
	}


	@Override
	public List<String> obterTextoChequeCopiaIndividual(final List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria) {
		List<String> copias = new ArrayList<>();

		for (ItemRelacaoTO itemRelacao : list) {
			copias.add(obterChequeCopia(list, relacaoBancaria, itemRelacao, true));
		}

		return copias;
	}


	@Override
	public String obterChequeCopia(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria, final ItemRelacaoTO itemRelacao, boolean selecionado) {
		// Obrigatório, mesmo que seja para cheques selecionados.
		if (relacaoBancaria == null) {
			throw new IllegalArgumentException("RelacaoBancaria não pode ser nulo!");
		}

		StringBuilder builder = new StringBuilder();

		// realizado esse procedimento porque a contaCorrente é "zerada" no load da página devido a problemas.
		List<ContaCorrente> listaContaCorrente = contaCorrenteRepository.findBylistaRelacaoBancariaCodigo(relacaoBancaria.getCodigo());
		ContaCorrente contaCorrente = listaContaCorrente.get(0);
		relacaoBancaria.setContaCorrente(mapper.map(contaCorrente, ContaCorrenteTO.class));
		Endereco endereco = enderecoRepository.findById(relacaoBancaria.getUnidade().getCodigoEndereco()).orElse(null);

		// Se não tem item, então cópia de um cheque para pagamento não individual.
		if (itemRelacao == null) {
			selecionado = false;
		}

		// Saltos padrão para cópia de cheque.
		builder.append(GlobalBean.SALTO).append(GlobalBean.SALTO);

		// Informações do Cheque
		if (selecionado) {
			// Cada item tem seu valor de cheque.
			builder.append(obterInformacaosCheque(relacaoBancaria, itemRelacao.getCheque(), itemRelacao.getValorAPagar()));
		} else {
			// Para pagamento não individual, será o cheque anterior + 1.
			builder.append(obterInformacaosCheque(relacaoBancaria, ((relacaoBancaria.getChequeAnterior() == null ? 0 : relacaoBancaria.getChequeAnterior()) + 1), relacaoBancaria.getTotal()));
		}

		builder.append(GlobalBean.SALTO);

		// Valor por extenso com apenas um salto, pois o mesmo é uma cópia.
		if (selecionado) {
			builder.append(obterValorPorExtenso(itemRelacao.getValorAPagar(), relacaoBancaria.getContaCorrente(), true));
		} else {
			builder.append(obterValorPorExtenso(relacaoBancaria.getTotal(), relacaoBancaria.getContaCorrente(), true));
		}

		builder.append(GlobalBean.SALTO);

		if (selecionado) {
			builder.append(obterPagamentoACheque(obterPagamentos(itemRelacao.getCodigo()), itemRelacao, true));
		} else {
			builder.append(obterPagamentoACheque(listaItensRelacao, relacaoBancaria, true));
		}

		builder.append(GlobalBean.SALTO);

		List<Pagamento> listaPagamento = null;

		if (selecionado) {
			listaPagamento = pagamentoRepository.findByItemRelacoesCodigo(itemRelacao.getCodigo());
		} else {
			listaPagamento = pagamentoRepository.findByItemRelacoesCodigo(listaItensRelacao.get(0).getCodigo());
		}


		//FIXME CONFERIR!!!
		if (selecionado) {
			builder.append(obterDataCheque(
				montarDataCheque(
					endereco.getCidade().getNome(),
					listaPagamento.get(0).getDataPagamento(),
					true)));
		} else {
			builder.append(obterDataCheque(montarDataCheque(
				endereco.getCidade().getNome(),
				relacaoBancaria.getDataPagamento(),
				true)));
		}

		builder.append(GlobalBean.SALTO);

		// Endosso
		if (selecionado) {
			builder.append(endossoChequeSelect(relacaoBancaria, itemRelacao, true));
		} else {
			builder.append(endossoCheque(listaItensRelacao, relacaoBancaria, true));
		}

		builder.append(finalChequeCopia());

		// Espaços para completar o tamanho do cheque.
		int linhas = builder.toString().split(GlobalBean.SALTO).length;

		for (int x = linhas; x < GlobalBean.SALTOS_MAX_CHEQUE; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO + GlobalBean.SALTO);
		}

		return builder.toString();
	}

	/**
	 * Texto final da cópia de cheque.
	 *
	 * @return O texto apresentado no final da cópia de um cheque.
	 */

	@Override
	public String finalChequeCopia() {
		StringBuilder builder = new StringBuilder();
		// Final Cópia
		builder.append(GlobalBean.SALTO).append(GlobalBean.SALTO);
		builder.append("Preparado por: ______________________  ");
		builder.append("Assinado por: ______________________");

		builder.append(GlobalBean.SALTO);

		return builder.toString();
	}

	/**
	 * Método utilizado para criar o endosso de um cheque simples.
	 *
	 * @param relacaoBancaria A {@link RelacaoBancaria} para obter as informações do endosso do cheque.
	 * @param copia           Informa se esse endosso será usado para cópia ou não. <code>true</code> Usar para cópia
	 *                        de um cheque. <code>false</code> para endosso normal.
	 * @return O endosso de um cheque simples.
	 */
	@Override
	public String endossoCheque(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria, final boolean copia) {
		StringBuilder builder = new StringBuilder();

		Map<String, String> infCheque = createInformacoesCheque(relacaoBancaria);

		String destinaSe = obterDestinaSeEndossoCheque(listaItensRelacao);

		String espacoEndosso = GlobalBean.VAZIO;
		String espacoAssinat = GlobalBean.VAZIO;

		if (copia) {
			// Usar espaços default para apresentar na cópia do cheque.
			espacoEndosso = "            ";
			// A assinatura receberá, também, espaços default.
			espacoAssinat = espacoEndosso + espacoEndosso;
		} else {
			// Não sendo cópia, usar configurações da conta corrente.
			for (int x = 1; x <= relacaoBancaria.getContaCorrente().getAntesEndosso(); x++) {
				espacoEndosso = espacoEndosso + GlobalBean.ESPACO_VAZIO;
			}
			for (int x = 1; x <= relacaoBancaria.getContaCorrente().getAntesAssinatura(); x++) {
				espacoAssinat = espacoAssinat + GlobalBean.ESPACO_VAZIO;
			}
		}

		DadosBancarios dadosBancarios = null;

		/*
		 * Neste momento, se o "destina-se" for vazio,
		 * então deveremos obter as informações do banco
		 * para informar no endosso.
		 */
		if (GlobalBean.VAZIO.equals(destinaSe)) {
			builder.append(GlobalBean.SALTO);
			List<DadosBancariosTO> list = obterDadosBancariosPorItemRelacao(listaItensRelacao.get(0));
			for (DadosBancariosTO bancarios : list) {
				if (bancarios.getSelecao()) {
					dadosBancarios = mapper.map(bancarios, DadosBancarios.class);
					break;
				} else if (bancarios.getPrincipal()) {
					dadosBancarios = mapper.map(bancarios, DadosBancarios.class);
				}
			}
		}

		/*
		 * Neste momento iremos verificar se as datas são diferentes.
		 * Caso afirmativo, devemos colocar a palavra "diverso" em
		 * vencimento.
		 */
		Date auxDate = null;
		for (ItemRelacaoTO itemRelacao : listaItensRelacao) {

			QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
			QPagamento qPagamento = QPagamento.pagamento;

			JPAQuery<ItemRelacaoPagamento> jpaQuery = new JPAQuery(entityManager);
			jpaQuery.from(qItemRelacaoPagamento).join(qItemRelacaoPagamento.pagamento, qPagamento)
				.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(itemRelacao.getCodigo())))
				.select(Projections.bean(ItemRelacaoPagamento.class, qItemRelacaoPagamento.codigo,
					Projections.bean(Pagamento.class, qPagamento.codigo, qPagamento.tipo, qPagamento.dataPagamento, qPagamento.tipoDocumentoPagamento, qPagamento.numero).as("pagamento")));
			List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

			List<Pagamento> listaPagamento = new ArrayList<>();

			for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
				listaPagamento.add(new JPAQuery<Pagamento>(entityManager)
					.from(QPagamento.pagamento)
					.select(Projections.fields(Pagamento.class, QPagamento.pagamento.codigo, QPagamento.pagamento.tipo,
						Projections.fields(Pessoa.class, QPagamento.pagamento.credor.codigo, QPagamento.pagamento.credor.nome).as("credor")))
					.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
					.fetchOne());
			}

			for (Pagamento pg : listaPagamento) {
				if (auxDate != null && !pg.equals(auxDate)) {
					auxDate = null;
					break;
				}
				auxDate = pg.getDataPagamento();
			}
		}

		// Criar todo o texto padrão do endosso.
		builder.append(endossoTexto(destinaSe, espacoEndosso, espacoAssinat, obterPagamentoA(listaItensRelacao, relacaoBancaria), infCheque.get(ASSIN),
			auxDate, dadosBancarios != null ? mapper.map(dadosBancarios, DadosBancariosTO.class) : null, null));

		return builder.toString();
	}

	/**
	 * Obter a informação de "Destina-se" do cheque com as informações
	 * de pagamento de uma {@link RelacaoBancaria}. Quando há apenas um
	 * pagamento ({@link ItemRelacao}), e o mesmo for um TR ou TA, obteremos
	 * a informação do tributo. Caso seja apenas um pagamento, será retornado
	 * vazio. Ou, se há mais de um pagamento, é verificado se todos os pagamentos
	 * são de um mesmo credor, se positivo, será retornado vazio, para posteriormente
	 * ser adicionado as informações de depósito do credor. Se não, retornar a informação
	 * de "diversos.
	 *
	 * @param list A lista de pagamentos da {@link RelacaoBancaria}.
	 * @return O nome do tributos, se houver apenas tributo. Vazio se for apenas um (1)
	 * pagamento ou for pagamentos de um mesmo fornecedor. E por fim, retorna "diversos"
	 * quando é pagamento para vários credores.
	 */

	@Override
	public String obterDestinaSeEndossoCheque(final List<ItemRelacaoTO> list) {
		String destinaSe = GlobalBean.VAZIO;
		if (list.size() == 1) {
			//FIXME CONFERIR!!
			if (list.get(0).getListaPgtos().size() == 1) {
				PagamentoTO pg_1 = list.get(0).getListaPgtos().get(0);
				if (Pagamento.TipoPagamento.TRIBUTO.equals(pg_1.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pg_1.getTipo())) {
//                    destinaSe = pg_1.getListaNotaTributoAliquota().getAliquota().getCodigoCategoriaTributo().getTributoTO().getNome();
				} else {
					destinaSe = GlobalBean.VAZIO;
				}
			}
		} else {
			String auxFav = GlobalBean.VAZIO;
			boolean mesmoFav = true;
			for (ItemRelacaoTO itemRelacao : list) {
				QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
				QPagamento qPagamento = QPagamento.pagamento;

				JPAQuery<ItemRelacaoPagamento> jpaQuery = new JPAQuery(entityManager);
				jpaQuery.from(qItemRelacaoPagamento).join(qItemRelacaoPagamento.pagamento, qPagamento)
					.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(itemRelacao.getCodigo())))
					.select(Projections.bean(ItemRelacaoPagamento.class, qItemRelacaoPagamento.codigo,
						Projections.bean(Pagamento.class, qPagamento.codigo, qPagamento.tipo, qPagamento.dataPagamento, qPagamento.tipoDocumentoPagamento, qPagamento.numero).as("pagamento")));
				List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

				List<Pagamento> listaPagamento = new ArrayList<>();

				for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
					listaPagamento.add(new JPAQuery<Pagamento>(entityManager)
						.from(QPagamento.pagamento)
						.select(Projections.fields(Pagamento.class, QPagamento.pagamento.codigo, QPagamento.pagamento.tipo,
							Projections.fields(Pessoa.class, QPagamento.pagamento.credor.codigo, QPagamento.pagamento.credor.nome).as("credor")))
						.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
						.fetchOne());
				}

				for (Pagamento pg : listaPagamento) {
					if (!auxFav.equals(pg.getCredor().getNome()) && !auxFav.isEmpty()) {
						/*
						 * Se houver pelo menos um diferente, devemos
						 * para a interação e retornar falso.
						 */
						mesmoFav = false;
						break;
					}
					auxFav = pg.getCredor().getNome();
				}
			}
			if (mesmoFav) {
				/*
				 * É mesmo fornecedor, retorna vazio para posteriormente ser preenchido
				 * com as informações do banco do credor.
				 */
				destinaSe = GlobalBean.VAZIO;
			} else {
				/*
				 * Tendo pagamentos para vários fornecedores,
				 * retornar "diversos".
				 */
				destinaSe = DIVERSOS;
			}
		}
		return destinaSe;
	}

	/**
	 * Cria texto com algumas informações para utilizar na cópia do cheque.<br>
	 * Informações como Código do banco, Nome do banco, Número do conta, número
	 * do cheque e valor total do cheque.
	 *
	 * @param relacaoBancaria A relação bancária do cheque.
	 * @param NumCheque       O número do cheque.
	 * @param valorTotal      O valor total do cheque.
	 * @return Texto com informações para serão utilizadas na cópia do cheque.
	 */

	@Override
	public String obterInformacaosCheque(final RelacaoBancariaTO relacaoBancaria, final Long NumCheque, final BigDecimal valorTotal) {
		Map<String, String> infCheque = createInformacoesCheque(relacaoBancaria);
		StringBuilder builder = new StringBuilder();
		builder.append(infCheque.get(CODIGO_BANCO));
		builder.append(ESPACO_DUPLO);
		builder.append(infCheque.get(NOME_BANCO).toUpperCase());

		// O número e nome do banco devem ter no total 30 caracteres.
		if (builder.length() < 25) {
			for (int x = builder.length(); x < 25; x++) {
				builder.append(GlobalBean.ESPACO_VAZIO);
			}
		}

		builder.append("No.");
		builder.append(NumCheque);

		// Valor
		String valor = GlobalBean.formatarValor(valorTotal);

		// Neste momento, colocaremos o valor no final da folha (58 espaços)
		int espacos = (GlobalBean.TAMANHO_MAX_COPIA_CHEQUE - builder.length()) - valor.length();

		for (int x = 1; x <= espacos; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}

		builder.append(valor);

		builder.append(GlobalBean.SALTO);
		return builder.toString();
	}

	/**
	 * Na apresentação da data, na cópia do cheque, de forma alinhada.
	 *
	 * @param dataVenc A data do cheque.
	 * @return A data com os espaços necessários para alinhamento na cópia do cheque.
	 */

	@Override
	public String obterDataCheque(final String dataVenc) {
		StringBuilder builder = new StringBuilder();
		int totalEspacosData = GlobalBean.TAMANHO_MAX_COPIA_CHEQUE - dataVenc.length();
		for (int x = 0; x < totalEspacosData; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}
		builder.append(dataVenc);
		builder.append(GlobalBean.SALTO);
		return builder.toString();
	}


	@Override
	public String obterTextoChequesEndossoIndividual(final List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria) {
		StringBuilder builder = new StringBuilder();

		for (ItemRelacaoTO itemRelacao : list) {
			builder.append(endossoChequeSelect(relacaoBancaria, itemRelacao, false));
			// Por algum motivo, os textos (após) o primeiro, estão vindo com 24 linhas. O certo são 25.
			builder.append(GlobalBean.SALTO).append(GlobalBean.ESPACO_VAZIO);
		}

		return builder.toString();
	}

	/**
	 * Método utilizado para criar o endosso de cheques selecionados. Utilizado para cheques ou cópia
	 * de cheques selecionados. Este utiliza a informação de {@link ItemRelacao} para obter as informações
	 * de endosso do cheque selecionado.
	 *
	 * @param relacaoBancaria A {@link RelacaoBancaria} para obter as informações de saltos e espaços
	 *                        configurado na conta corrente.
	 * @param itemRelacao     O pagamentos do cheques selecionado.
	 * @param copia           <code>true</code> endosso para apresentação na cópia do cheque.
	 *                        <code>false</code> endosso para apresentação normal no cheque. Neste, será usado
	 *                        as configurações de saltos e espaços informados na {@link ContaCorrente}.
	 * @return O endosso para cheque selecionado.
	 */

	@Override
	public String endossoChequeSelect(final RelacaoBancariaTO relacaoBancaria, final ItemRelacaoTO itemRelacao, final boolean copia) {
		// realizado esse procedimento porque a contaCorrente é "zerada" no load da página devido a problemas.
		ContaCorrente contaCorrente = contaCorrenteRepository.findById(relacaoBancaria.getContaCorrente().getCodigo()).orElse(null);
		relacaoBancaria.setContaCorrente(mapper.map(contaCorrente, ContaCorrenteTO.class));

		Map<String, String> infCheque = createInformacoesCheque(relacaoBancaria);

		String destinaSe = GlobalBean.VAZIO;

		boolean isTR = false;

		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;

		JPAQuery<ItemRelacaoPagamento> jpaQuery = new JPAQuery(entityManager);
		jpaQuery.from(qItemRelacaoPagamento)
			.join(qItemRelacaoPagamento.pagamento, qPagamento)
			.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(itemRelacao.getCodigo())))
			.select(Projections.bean(ItemRelacaoPagamento.class,
				qItemRelacaoPagamento.codigo,
				Projections.bean(Pagamento.class,
					qPagamento.codigo,
					qPagamento.tipo,
					qPagamento.dataPagamento,
					qPagamento.tipoDocumentoPagamento,
					qPagamento.numero).as("pagamento")));
		List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

		List<Pagamento> listaPagamento = new ArrayList<>();

		for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
			listaPagamento.add(new JPAQuery<Pagamento>(entityManager)
				.from(QPagamento.pagamento)
				.select(Projections.fields(
					Pagamento.class,
					QPagamento.pagamento.codigo,
					QPagamento.pagamento.tipo,
					QPagamento.pagamento.numero,
					QPagamento.pagamento.tipoDocumentoPagamento,
					Projections.fields(Pessoa.class,
						QPessoa.pessoa.codigo,
						QPessoa.pessoa.nome,
						QPessoa.pessoa.documento).as("credor")))
				.leftJoin(QPagamento.pagamento.credor, QPessoa.pessoa)
				.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
				.fetchOne());
		}

		// Verificar se esse cheque é para pagamento de TR/TA ou não.
		if (listaPagamento.size() == 1) {
			Pagamento pg_1 = listaPagamento.get(0);
			if (Pagamento.TipoPagamento.TRIBUTO.equals(pg_1.getTipo()) || Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pg_1.getTipo())) {
				NotaTributoAliquota notaTributoAliquota = notaTributoAliquotaRepository.findByPagamentoCodigo(listaPagamento.get(0).getCodigo());
				if (notaTributoAliquota != null) {
					Aliquota aliquota = aliquotaRepository.findById(notaTributoAliquota.getAliquota().getId()).get();
					CategoriaTributo categoriaTributo = categoriaTributoRepository.findById(aliquota.getCategoriaTributo().getCodigo()).get();
					Tributo tributo = tributoRepository.findById(categoriaTributo.getTributo().getCodigo()).get();
					destinaSe = tributo.getNome();
				} else {
					destinaSe = pg_1.getNumero();
				}
				isTR = true;
			} else {
				destinaSe = GlobalBean.VAZIO;
			}
		}

		String espacoEndosso = GlobalBean.VAZIO;
		String espacoAssinat = GlobalBean.VAZIO;

		if (copia) {
			// Cópia, utilizar valores default.
			espacoEndosso = "            ";
			espacoAssinat = espacoEndosso + espacoEndosso;
		} else {
			// Se não, utilizar o que está configurado na conta corrente.
			for (int x = 1; x <= relacaoBancaria.getContaCorrente().getAntesEndosso(); x++) {
				espacoEndosso = espacoEndosso + GlobalBean.ESPACO_VAZIO;
			}
			for (int x = 1; x <= relacaoBancaria.getContaCorrente().getAntesAssinatura(); x++) {
				espacoAssinat = espacoAssinat + GlobalBean.ESPACO_VAZIO;
			}
		}

		DadosBancarios dadosBancarios = null;

		// Se "destina-se" estiver vazio, obter as informações do banco do credor.
		if (GlobalBean.VAZIO.equals(destinaSe)) {
			List<DadosBancariosTO> list = obterDadosBancariosPorItemRelacao(itemRelacao);
			for (DadosBancariosTO bancarios : list) {
				if (bancarios.getSelecao() != null && bancarios.getSelecao()) {
					dadosBancarios = mapper.map(bancarios, DadosBancarios.class);
					break;
				} else if (bancarios.getPrincipal()) {
					dadosBancarios = mapper.map(bancarios, DadosBancarios.class);
				}
			}
		}

		StringBuilder builder = new StringBuilder();

		if (!copia) {
			for (int x = 1; x <= relacaoBancaria.getContaCorrente().getSaltoEndosso(); x++) {
				builder.append(GlobalBean.SALTO);
			}
		}

		Pagamento.TipoDocumentoPagamento tipoDocumentoPagamento = listaPagamento.get(0).getTipoDocumentoPagamento();

		if (Pagamento.TipoDocumentoPagamento.BOLETO.equals(tipoDocumentoPagamento)
			|| Pagamento.TipoDocumentoPagamento.GUIA.equals(tipoDocumentoPagamento)
			|| Pagamento.TipoDocumentoPagamento.CONCESSIONARIA.equals(tipoDocumentoPagamento)
		) {
			destinaSe = listaPagamento.get(0).getNumero();
		}

		Pagamento pg = new JPAQuery<Pagamento>(entityManager)
			.from(QPagamento.pagamento)
			.select(
				Projections.fields(Pagamento.class,
					QPagamento.pagamento.codigo,
					QPagamento.pagamento.numero,
					QPagamento.pagamento.dataPagamento,
					Projections.fields(Pessoa.class,
						QPessoa.pessoa.codigo,
						QPessoa.pessoa.nome,
						QPessoa.pessoa.documento).as("credor")))
			.leftJoin(QPagamento.pagamento.credor, QPessoa.pessoa)
			.where(QPagamento.pagamento.codigo.eq(listaPagamento.get(0).getCodigo()))
			.fetchOne();

		// Texto do endosso.
		builder.append(endossoTexto(
			destinaSe,
			espacoEndosso,
			espacoAssinat,
			(isTR ? relacaoBancaria.getUnidade().getNome() : pg.getCredor() != null ? pg.getCredor().getNome() + " " + pg.getCredor().getDocumento() : ""),
			infCheque.get(ASSIN),
			pg.getDataPagamento(),
			(dadosBancarios == null ? null : mapper.map(dadosBancarios, DadosBancariosTO.class)),
			tipoDocumentoPagamento.toString()));

		/*
		 * Não sendo cópia, devemos completar os espaços até o máximo de um cheque.
		 * Assim, o próximo cheque começará na posição correta.
		 */
		if (!copia) {
			int linhas = builder.toString().split(GlobalBean.SALTO).length;
			for (int x = linhas; x < GlobalBean.SALTOS_MAX_CHEQUE - 1; x++) {
				builder.append(GlobalBean.ESPACO_VAZIO + GlobalBean.SALTO);
			}
		}


		return builder.toString();
	}


	/**
	 * Cria o texto padrão de um endosso.
	 *
	 * @param destinaSe      A quem se destina o endosso.
	 * @param espacoEndosso  Os espaços configurados para o endosso.
	 * @param espacoAssinat  Os espaços configurados para a assinatura.
	 * @param favorecido     O favorecidos no endosso.
	 * @param assinatura     A assinatura no endosso.
	 * @param vencimento     O vencimento do cheque. Se nulo, será apresentado a mensagem "diversos".
	 * @param dadosBancarios Os dados bancários do credor.
	 * @return O texto completo do endosso.
	 */

	@Override
	public String endossoTexto(final String destinaSe, final String espacoEndosso, final String espacoAssinat, final String favorecido,
							   final String assinatura, final Date vencimento, final DadosBancariosTO dadosBancarios, String tipoDocumentoPagamento) {
		StringBuilder builder = new StringBuilder();

		boolean apresentAssinat = true;

		builder.append(espacoEndosso).append("Este cheque destina-se a: ");
		if (Pagamento.TipoDocumentoPagamento.BOLETO.getDescricao().equals(tipoDocumentoPagamento)) {
			builder.append(GlobalBean.SALTO);
			builder.append(espacoEndosso).append("Pagamento : ");
			builder.append(destinaSe);
		} else if (!GlobalBean.VAZIO.equals(destinaSe) && dadosBancarios == null) {
			builder.append(GlobalBean.SALTO);
			builder.append(espacoEndosso).append("Pgto. de ");
			builder.append(destinaSe);
		} else {
			builder.append(GlobalBean.SALTO);
			builder.append(espacoEndosso);
			builder.append("Banco: ");
			builder.append(montarCodigoBanco(dadosBancarios == null ? "" : dadosBancarios.getBanco().getCodigo().toString()));
			builder.append(ESPACO_DUPLO);
			builder.append("Agência: ");
			builder.append(dadosBancarios == null ? "" : dadosBancarios.getAgencia());
			if (dadosBancarios != null && dadosBancarios.getDigitoAgencia() != null) {
				builder.append("-");
				builder.append(dadosBancarios.getDigitoAgencia());
			}
			builder.append(ESPACO_DUPLO);
			builder.append("C/C: ");
			builder.append(dadosBancarios == null ? "" : dadosBancarios.getConta());
			if (dadosBancarios != null && dadosBancarios.getDigito() != null && !dadosBancarios.getDigito().isEmpty()) {
				builder.append(GlobalBean.TRACO);
				builder.append(dadosBancarios.getDigito());
			}
			apresentAssinat = false;
		}
		builder.append(GlobalBean.SALTO);
		builder.append(espacoEndosso).append("Vencimento: ");
		builder.append(vencimento == null ? DIVERSOS : GlobalBean.formatarData(vencimento, GlobalBean.DATE_PATTERN));
		builder.append(GlobalBean.SALTO);
		builder.append(espacoEndosso).append("Favorecido: ");

		// Caso o fornecedor ultrapasse o máximo do cheque, dividir em duas linhas.
		String[] favorecidoDiv = GlobalBean.dividirString(favorecido, GlobalBean.TAMANHO_MAX_COPIA_CHEQUE
			- (espacoEndosso.length() + "Favorecido: ".length())).split(GlobalBean.SALTO);

		builder.append(favorecidoDiv[GlobalBean.ZERO]);
		if (favorecidoDiv.length == 2) {
			builder.append(GlobalBean.SALTO);
			builder.append(espacoEndosso);
			builder.append("            ");
			builder.append(favorecidoDiv[1]);
		}

		// Se houver "destina-se", apresentar assinatura.
		if (apresentAssinat) {
			builder
				.append(GlobalBean.SALTO)
				.append(GlobalBean.SALTO)
				.append(GlobalBean.SALTO)
				.append(GlobalBean.SALTO);
			builder.append(espacoAssinat);
			builder.append("---------------------------");
			builder.append(GlobalBean.SALTO);
			builder.append(espacoAssinat);
			builder.append(assinatura);
		}
		builder.append(GlobalBean.SALTO);

		return builder.toString();
	}


	@Override
	public List<DadosBancariosTO> obterDadosBancariosPorItemRelacao(ItemRelacaoTO itemRelacao) {
		// Usado para informa se existe algum banco selecionado.
		boolean selecionado = false;

		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;

		JPAQuery<ItemRelacaoPagamento> jpaQuery = new JPAQuery(entityManager);
		jpaQuery.from(qItemRelacaoPagamento).join(qItemRelacaoPagamento.pagamento, qPagamento)
			.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(itemRelacao.getCodigo())))
			.select(Projections.bean(ItemRelacaoPagamento.class, qItemRelacaoPagamento.codigo,
				Projections.bean(Pagamento.class, qPagamento.codigo, qPagamento.tipo).as("pagamento")));
		List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

		List<Pagamento> listaPagamento = new ArrayList<>();

		for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
			listaPagamento.add(new JPAQuery<Pagamento>(entityManager)
				.from(QPagamento.pagamento)
				.select(
					Projections.fields(Pagamento.class, QPagamento.pagamento.codigo,
						Projections.fields(DadosBancarios.class, QDadosBancarios.dadosBancarios.codigo).as("dadosBancarios"),
						Projections.fields(Pessoa.class, QPagamento.pagamento.credor.codigo).as("credor")))
				.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
				.fetchOne());
		}

		List<DadosBancarios> list = new JPAQuery<DadosBancarios>(entityManager)
			.from(QDadosBancarios.dadosBancarios)
			.select(Projections.fields(DadosBancarios.class,
				QDadosBancarios.dadosBancarios.codigo,
				QDadosBancarios.dadosBancarios.principal,
				QDadosBancarios.dadosBancarios.agencia,
				QDadosBancarios.dadosBancarios.digitoAgencia,
				QDadosBancarios.dadosBancarios.conta,
				QDadosBancarios.dadosBancarios.digito,
				Projections.fields(Banco.class, QDadosBancarios.dadosBancarios.banco.codigo, QDadosBancarios.dadosBancarios.banco.nome).as("banco"))
			)
			.where(QDadosBancarios.dadosBancarios.pessoa.codigo.eq(listaPagamento.get(0).getCredor().getCodigo()))
			.fetch();


		for (DadosBancarios bancarios : list) {
			/*
			 * Todos os pagamentos são apenas um banco, tipo de documento, fornecedor e data de pagamento.
			 * Neste caso, o primeiro banco igual ao banco usado nas pagamentos será o selecionado.
			 */
			if (listaPagamento.get(0).getDadosBancarios() != null
			    && listaPagamento.get(0).getDadosBancarios().getCodigo() != null
				&& bancarios != null
				&& listaPagamento.get(0).getDadosBancarios().getCodigo().equals(bancarios.getCodigo())) {
				bancarios.setSelecao(Boolean.TRUE);
				selecionado = true;
				break;
			}
		}

		/*
		 * Neste momento, caso não tenha nenhum banco selecionado,
		 * iremos marcar o banco principal como selecionado. Isso
		 * para apresentar o "X" se seleção na tabela de bancos.
		 * Apenas para indicar ao usuário que há um banco selecionado.
		 */
		if (!selecionado) {
			for (DadosBancarios bancarios : list) {
				if (bancarios.getPrincipal()) {
					bancarios.setSelecao(true);
				}
			}
		}

		Collections.sort(list);

		List<DadosBancariosTO> dadosBancariosTOS = new ArrayList<>();

		for (DadosBancarios dadosBancarios : list) {
			dadosBancariosTOS.add(mapper.map(dadosBancarios, DadosBancariosTO.class));
		}

		return dadosBancariosTOS;
	}


	@Override
	public Map<String, String> createInformacoesCheque(final RelacaoBancariaTO relacaoBancaria) {
		// Map que guardará as informações.
		Map<String, String> infCheque = new TreeMap<>();

		ContaCorrenteTO contaCorrente = relacaoBancaria.getContaCorrente();

		infCheque.put(CODIGO_BANCO, montarCodigoBanco(contaCorrente.getBanco().getCodigo().toString()));
		infCheque.put(NOME_BANCO, contaCorrente.getBanco().getNome());
		infCheque.put(CONTA, contaCorrente.getConta() + (contaCorrente.getDigito() != null ? GlobalBean.TRACO + contaCorrente.getDigito() : GlobalBean.VAZIO));
		infCheque.put(ASSIN, relacaoBancaria.getUnidade().getNome());

		// Retornando map com as informações que serão usadas no cheque.
		return infCheque;
	}

	/**
	 * Montar o código do banco com 3 dígitos.
	 *
	 * @param codBanco O código do banco.
	 * @return Retorna o código do banco com 3 dígitos.
	 */

	@Override
	public String montarCodigoBanco(String codBanco) {
		if (codBanco.length() == 1) {
			codBanco = "00" + codBanco;
		} else if (codBanco.length() == 2) {
			codBanco = "0" + codBanco;
		}

		return codBanco;
	}


	@Override
	public String obterTextoChequeIndividual(final List<ItemRelacaoTO> list, RelacaoBancariaTO relacaoBancaria) {
		StringBuilder builder = new StringBuilder();
		/*
		 * Observação: A data de vencimento do cheque será
		 * a mesma data do pagamento. Quando pagamento é
		 * individual, todos os pagamentos (deste item)
		 * tem a mesma data.
		 */
		Unidade unidade = new JPAQuery<Unidade>(entityManager)
			.from(QUnidade.unidade)
			.join(QUnidade.unidade.endereco)
			.select(Projections.fields(Unidade.class,
				QUnidade.unidade.codigo,
				Projections.bean(Endereco.class,
					QUnidade.unidade.endereco.codigo).as("endereco")))
			.where(QUnidade.unidade.codigo.eq(relacaoBancaria.getUnidade().getCodigo()))
			.fetchOne();

		Endereco endereco = new JPAQuery<Endereco>(entityManager)
			.from(QEndereco.endereco1)
			.select(Projections.fields(Endereco.class,
				QEndereco.endereco1.codigo,
				Projections.bean(Cidade.class,
					QCidade.cidade.codigo,
					QCidade.cidade.nome).as("cidade")))
			.where(QEndereco.endereco1.codigo.eq(unidade.getEndereco().getCodigo()))
			.fetchOne();

		for (ItemRelacaoTO itemRelacao : list) {
			// realizado esse procedimento porque a contaCorrente é "zerada" no load da página devido a problemas.
			ContaCorrente contaCorrente = contaCorrenteRepository.findById(relacaoBancaria.getContaCorrente().getCodigo()).orElse(null);

			relacaoBancaria.setContaCorrente(mapper.map(contaCorrente, ContaCorrenteTO.class));

			List<Pagamento> listaPagamento = obterPagamentos(itemRelacao.getCodigo());

			builder.append(obterImpressaoCheque(
				relacaoBancaria.getContaCorrente(),
				itemRelacao.getValorAPagar(),
				obterPagamentoACheque(listaPagamento, itemRelacao, false),
				montarDataCheque(endereco.getCidade().getNome(), listaPagamento.get(0).getDataPagamento(), false))
			);
		}

		return builder.toString();
	}

	public DocumentPrintEntity createDocumentPrint(String text, TiposImpressao tipo, String colaborador) {
		DocumentPrintEntity entity = new DocumentPrintEntity();
		entity.setTextoImpressao(text);
		entity.setStatus(PrintStatus.LIBERADO);
		entity.setTipoImpressao(tipo);
		entity.setCriador(colaborador);
		entity.setDataCriacao(new Date());
		return entity;
	}

	/**
	 * Método responsável por apresentar as informações de impressão de cheque.
	 *
	 * @param cc               A {@link ContaCorrente} para obter as informações de banco e configuração de saltos e espaços.
	 * @param total            O total do cheque.
	 * @param pagamentoAAlguem A informação de "pagamento a".
	 * @param cidadeDataVenc   A cidade e data de vencimento.
	 * @return O texto das informações de cheque para impressão.
	 */

	@Override
	public String obterImpressaoCheque(final ContaCorrenteTO cc, final BigDecimal total, final String pagamentoAAlguem, final String cidadeDataVenc) {
		Integer antesValor = cc.getAntesValor();
		Integer antesFav = cc.getAntesFavorecido();
		Integer saltoFav = cc.getSaltoFavorecido();
		Integer saltoData = cc.getSaltoData();
		Integer antesData = cc.getAntesData();

		StringBuilder builder = new StringBuilder();

		for (int x = 1; x <= antesValor; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}

		// Valor do cheque.
		builder.append(GlobalBean.formatarValor(total));
		builder.append(GlobalBean.SALTO);

		// Valor por extenso do cheque.
		String extenso = obterValorPorExtenso(total, cc, false);
		builder.append(extenso);

		/*
		 * O procedimento abaixo é quando a extensão for
		 * mais de uma linha, manteremos o próximo item
		 * na mesmo linha com referencia a primeira linha
		 * do extenso. Isso para manter sempre os ítens na
		 * mesmo posição, pois a extensão é dinâmica.
		 */
		int salto = 0;
		if ((extenso.split(GlobalBean.SALTO).length - (cc.getSaltoExtenso() + 1)) > 0) {
			salto = extenso.split(GlobalBean.SALTO).length - (cc.getSaltoExtenso() + 1);
		}

		for (int x = 1; x <= (saltoFav - salto); x++) {
			builder.append(GlobalBean.SALTO);
		}
		for (int x = 1; x <= antesFav; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}

		builder.append(pagamentoAAlguem);

		for (int x = 1; x <= saltoData; x++) {
			builder.append(GlobalBean.SALTO);
		}
		for (int x = 1; x <= antesData; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}

		builder.append(cidadeDataVenc);

		builder.append(GlobalBean.SALTO);

		// Abaixo, saltos para completar um cheque.
		int linhas = builder.toString().split(GlobalBean.SALTO).length;

		for (int x = linhas; x < GlobalBean.SALTOS_MAX_CHEQUE; x++) {
			// Adicionando espaço vazio. Não sei por que?!?!
			builder.append(GlobalBean.ESPACO_VAZIO + GlobalBean.SALTO + GlobalBean.ESPACO_VAZIO);
		}

		return builder.toString();
	}

	/**
	 * Obter o valor por extenso com quebra de linha. Quando a quebra for em um caractere em branco,
	 * a troca de linha é feita normalmente. Mas quando a quebra for em um caractere qualquer, a troca
	 * é feita após o métodos verificar algum caractere em branco. Assim, o texto não fica quebrado no
	 * meio da "palavra".
	 *
	 * @param valor O valor para obter por extenso.
	 * @param cc    A conta corrente para obter a configuração de saltos e espaços. E este for nulo, mesmo
	 *              informando que não é "cópia", ele irá retornar sem qualquer salto ou espaços.
	 * @param copia <code>true</code> Indica que não usará {@link ContaCorrente} para obter as configurações
	 *              de salto ou espaços definidos na configuração do cheque. <code>false</code> Usará as configurações
	 *              da {@link ContaCorrente} para obter os saltos e espaços definidos na configuração dos cheques.
	 * @return O valor informado por extenso. Caso informado {@link ContaCorrente}, o valor por extenso
	 * com as configurações de saltos e espaços.
	 */

	@Override
	public String obterValorPorExtenso(final BigDecimal valor, final ContaCorrenteTO cc, final boolean copia) {
		StringBuilder builder = new StringBuilder();
		// Se cópia e informado conta corrente, usar configurações para o cheque.
		if (!copia && cc != null) {
			for (int x = 1; x <= cc.getSaltoExtenso(); x++) {
				builder.append(GlobalBean.SALTO);
			}
			for (int x = 1; x <= cc.getAntesExtenso(); x++) {
				builder.append(GlobalBean.ESPACO_VAZIO);
			}
		}
		builder.append("(");
		// Obter valor por extenso.
		String porExtenso = GlobalBean.valorPorExtenso(valor).toUpperCase();

		/*
		 *Neste momento, se o cheque passar do comprimento máximos definido em "GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE",
		 *devemos quebrar a linha. Quando a quebra for em um caractere em branco, "pular" a linhas normalmente. Caso a
		 *quebra seja em um caractere qualquer, a quebre será no próximo caracter em branco.
		 */
		if (porExtenso.length() <= GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE) {
			StringBuilder emUmaLinha = new StringBuilder(porExtenso);
			emUmaLinha.append(")");
			for (int x = emUmaLinha.length(); x <= GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE; x++) {
				emUmaLinha.append("x");
			}
			builder.append(emUmaLinha);
		} else {
			if (porExtenso.charAt(GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE - 1) == ' ') {
				builder.append(porExtenso.substring(0, GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE - 1));
				builder.append(GlobalBean.SALTO);
				if (!copia && cc != null) {
					for (int x = 1; x <= cc.getSaltoExtenso2(); x++) {
						builder.append(GlobalBean.SALTO);
					}
					for (int x = 1; x <= cc.getAntesExtenso2(); x++) {
						builder.append(GlobalBean.ESPACO_VAZIO);
					}
				}
				builder.append(porExtenso.substring((GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE - 1), porExtenso.length()));

				builder.append(")");

				// Adicionando caracteres "x" até o tamanho por extenso.
				int value = porExtenso.substring((GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE), porExtenso.length()).length();

				for (int x = value; x <= GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE; x++) {
					builder.append("x");
				}
			} else {
				int aux = 0;
				for (int x = (GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE - 1); x != 0; x--) {
					if (porExtenso.charAt(x) == ' ') {
						aux = x;
						break;
					}
				}
				builder.append(porExtenso.substring(0, aux++));
				builder.append(GlobalBean.SALTO);
				if (!copia && cc != null) {
					for (int x = 1; x <= cc.getSaltoExtenso2(); x++) {
						builder.append(GlobalBean.SALTO);
					}
					for (int x = 1; x <= cc.getAntesExtenso2(); x++) {
						builder.append(GlobalBean.ESPACO_VAZIO);
					}
				}
				StringBuilder segundaLinha = new StringBuilder(porExtenso.substring(aux, porExtenso.length()) + ")");
				if (segundaLinha.length() < GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE) {
					for (int x = segundaLinha.length(); x <= GlobalBean.TAMANHO_MAX_EXTENSO_CHEQUE; x++) {
						segundaLinha.append("x");
					}
				}

				builder.append(segundaLinha);
			}
		}

		builder.append(GlobalBean.SALTO);

		return builder.toString();
	}

	/**
	 * Obter o "pagamento a" em um cheque. Este método é usado na impressão de cheques
	 * e cópia de cheques apenas, não endosso.
	 *
	 * @param relacaoBancaria A {@link RelacaoBancaria} para obter o "pagamento a".
	 * @param copia           <code>true</code> será adicionado a letra "a " antes do credor.
	 *                        <code>false</code> apresenta apenas o nome do credor.
	 * @return O nome do credor para adicionar ao cheque.
	 */

	@Override
	public String obterPagamentoACheque(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria, final boolean copia) {
		StringBuilder builder = new StringBuilder();
		if (copia) {
			builder.append("a ");
		}
		builder.append(obterPagamentoA(listaItensRelacao, relacaoBancaria));
		builder.append(GlobalBean.SALTO);
		return builder.toString();
	}

	@Override
	public String obterPagamentoACheque(ItemRelacaoTO itemRelacao, boolean copia) {
		StringBuilder builder = new StringBuilder();
		if (copia) {
			builder.append("a ");
		}
//		builder.append(obterPagamentoA(itemRelacao));
		builder.append(GlobalBean.SALTO);
		return builder.toString();
	}

	/**
	 * Obter o "pagamento a" em um cheque. Este método é usado na impressão de cheques
	 * e cópia de cheques apenas, não endosso. Usado em cheques individuais.
	 *
	 * @param listaPagamento
	 * @param itemRelacao    O {@link ItemRelacao} para obter o "pagamento a".
	 * @param copia          <code>true</code> será adicionado a letra "a " antes do credor.
	 *                       <code>false</code> apresenta apenas o nome do credor.
	 * @return O nome do credor para adicionar ao cheque.
	 */

	public String obterPagamentoACheque(List<Pagamento> listaPagamento, final ItemRelacaoTO itemRelacao, final boolean copia) {
		StringBuilder builder = new StringBuilder();

		if (listaPagamento.size() == 1) {
			if (Pagamento.TipoPagamento.TRIBUTO.equals(listaPagamento.get(0).getTipo())
				|| Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(listaPagamento.get(0).getTipo())) {
				if (copia) {
					builder.append("a ");
				}
				builder.append(itemRelacao.getRelacaoBancaria().getUnidade().getNome());
			} else {
				if (copia) {
					builder.append("a ");
				}
				builder.append(listaPagamento.get(0).getCredor().getNome());
			}
		} else {
			if (copia) {
				builder.append("a ");
			}
			builder.append(listaPagamento.get(0).getCredor().getNome());
		}
		builder.append(GlobalBean.SALTO);
		return builder.toString();
	}

	/**
	 * Obter a informação de "Pagamento a" para um cheque. É passado a relação bancária,
	 * quando a mesma há apenas um pagamento e esse pagamento for um TR ou TA, será passado
	 * o nome da unidade (empresa) da {@link RelacaoBancaria}. Caso contrário, se em todos
	 * os pagamentos há o mesmo fornecedor, será retornado o nome do mesmo. Se pelo menos
	 * um dos fornecedores for diferente será passado o nome da unidade (empresa) da
	 * {@link RelacaoBancaria}. Este método é usado principalmente no endosso do cheque.
	 *
	 * @param relacaoBancaria A relação bancária para obter os informações de "pagamento a".
	 * @return A informação de pagamento do cheque "Pagamento a".
	 */

	@Override
	public String obterPagamentoA(List<ItemRelacaoTO> listaItensRelacao, final RelacaoBancariaTO relacaoBancaria) {
		// Obtendo o nome da unidade (empresa).
		String destinadoA = relacaoBancaria.getUnidade().getNome();
		if (listaItensRelacao.size() == 1) {
			if (listaItensRelacao.get(0).getListaPgtos().size() == 1) {
				PagamentoTO pg_1 = listaItensRelacao.get(0).getListaPgtos().get(0);
				if (!Pagamento.TipoPagamento.TRIBUTO.equals(pg_1.getTipo()) && !Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pg_1.getTipo())) {
					/*
					 * Se não for um TA ou TA, esse pagamento (único) tem um credor
					 * e esse pagamento será para ele.
					 */
					destinadoA = pg_1.getCredor().getNome();
				}
			}
		} else {
			String auxCredor = GlobalBean.VAZIO;
			/*
			 * Vamos verificar todos os pagamentos, se pelo menos
			 * um dos pagamentos tiver um credor diferente, iremos
			 * retorna a unidade da relação bancária.
			 */
			for (ItemRelacaoTO itemRelacao : listaItensRelacao) {
				QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
				QPagamento qPagamento = QPagamento.pagamento;

				JPAQuery<ItemRelacaoPagamento> jpaQuery = new JPAQuery(entityManager);
				jpaQuery.from(qItemRelacaoPagamento)
					.join(qItemRelacaoPagamento.pagamento, qPagamento)
					.where(new BooleanBuilder().and(qItemRelacaoPagamento.itemRelacao.codigo.eq(itemRelacao.getCodigo())))
					.select(Projections.bean(ItemRelacaoPagamento.class,
						qItemRelacaoPagamento.codigo,
						Projections.bean(Pagamento.class,
							qPagamento.codigo,
							qPagamento.tipo,
							qPagamento.dataPagamento,
							qPagamento.tipoDocumentoPagamento,
							qPagamento.numero).as("pagamento")));
				List<ItemRelacaoPagamento> listaItemRelacaoPagamento = jpaQuery.fetch();

				List<Pagamento> listaPagamento = new ArrayList<>();

				for (ItemRelacaoPagamento itemRelacaoPagamento : listaItemRelacaoPagamento) {
					listaPagamento.add(new JPAQuery<Pagamento>(entityManager)
						.from(QPagamento.pagamento)
						.select(Projections.fields(Pagamento.class,
							QPagamento.pagamento.codigo,
							QPagamento.pagamento.tipo,
							Projections.fields(Pessoa.class,
								QPagamento.pagamento.credor.codigo,
								QPagamento.pagamento.credor.nome,
								QPagamento.pagamento.credor.documento).as("credor")))
						.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
						.fetchOne());
				}

				for (Pagamento pg : listaPagamento) {
					if (!Pagamento.TipoPagamento.TRIBUTO.equals(pg.getTipo()) && !Pagamento.TipoPagamento.TRIBUTO_ACUMULADO.equals(pg.getTipo())) {
						destinadoA = pg.getCredor().getNome() + " " + pg.getCredor().getDocumento();
						if (!auxCredor.isEmpty() && !auxCredor.equals(destinadoA)) {
							/*
							 * Se pelo menos um dos pagamentos tiver favorecido
							 * diferente, então pagamento é "diverso" e o
							 * cheque destina-se a unidade (Nome da Unidade).
							 */
							destinadoA = relacaoBancaria.getUnidade().getNome();
							break;
						}
						auxCredor = destinadoA;
					}
				}
			}
		}
		return destinadoA;
	}


	@Override
	public String montarDataCheque(final String endereco, final Date dataVenc, boolean copia) {
		if (copia) {
			return endereco + ", " + GlobalBean.formatarData(dataVenc, GlobalBean.DATA_CHEQUE_COPIA).toLowerCase();
		}
		String dia = GlobalBean.formatarData(dataVenc, GlobalBean.DATA_CHEQUE_DIA);
		String mes = GlobalBean.formatarData(dataVenc, GlobalBean.DATA_CHEQUE_MES).toLowerCase();
		String ano = GlobalBean.formatarData(dataVenc, GlobalBean.DATA_CHEQUE_ANO);

		StringBuilder builder = new StringBuilder();
		builder.append(endereco);
		builder.append(",");

		/*
		 * Data com um caracter, adicionar dois (2) espaços.
		 * Data com dois caracteres, adicionar apenas um espaço.
		 */
		if (dia.length() == 1) {
			builder.append(GlobalBean.ESPACO_VAZIO);
			builder.append(GlobalBean.ESPACO_VAZIO);
		} else {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}
		builder.append(dia);
		builder.append(GlobalBean.ESPACO_VAZIO);
		builder.append(GlobalBean.ESPACO_VAZIO);

		builder.append(mes);
		// Adicionando espaço padrão no mês.
		int diferenca = GlobalBean.TAMANHO_MES_DA_DATA - mes.length();
		for (int x = 1; x <= diferenca; x++) {
			builder.append(GlobalBean.ESPACO_VAZIO);
		}

		builder.append(ano);

		return builder.toString();
	}

	public List<Pagamento> obterPagamentos(Long codigoItemRelacao) {
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;

		List<ItemRelacaoPagamento> list = new JPAQuery<ItemRelacaoPagamento>(entityManager)
			.from(qItemRelacaoPagamento)
			.select(Projections.fields(ItemRelacaoPagamento.class, qItemRelacaoPagamento.codigo,
				Projections.fields(Pagamento.class,
					qPagamento.codigo
				).as("pagamento")))
			.where(qItemRelacaoPagamento.itemRelacao.codigo.eq(codigoItemRelacao))
			.fetch();

		List<Pagamento> listaPagamentos = new ArrayList<>();

		for (ItemRelacaoPagamento itemRelacaoPagamento : list) {
			Pagamento pg = new JPAQuery<Pagamento>(entityManager)
				.from(QPagamento.pagamento)
				.leftJoin(QPagamento.pagamento.credor)
				.leftJoin(QPagamento.pagamento.dadosBancarios)
				.select(Projections.fields(Pagamento.class,
					qPagamento.codigo,
					qPagamento.dataPagamento,
					qPagamento.numero,
					qPagamento.status,
					qPagamento.tipo,
					qPagamento.tipoDocumentoPagamento,
					Projections.bean(Pessoa.class,
						QPagamento.pagamento.credor.codigo,
						QPagamento.pagamento.credor.nome).as("credor"),
					Projections.bean(DadosBancarios.class,
						QPagamento.pagamento.dadosBancarios.codigo).as("dadosBancarios"))
				)
				.where(QPagamento.pagamento.codigo.eq(itemRelacaoPagamento.getPagamento().getCodigo()))
				.fetchOne();

			if (pg.getDadosBancarios() != null && pg.getDadosBancarios().getCodigo() != null) {
				DadosBancarios db = new JPAQuery<DadosBancarios>(entityManager)
					.from(QDadosBancarios.dadosBancarios)
					.select(Projections.bean(DadosBancarios.class,
						QDadosBancarios.dadosBancarios.codigo,
						Projections.bean(Banco.class,
							QBanco.banco.codigo,
							QBanco.banco.nome).as("banco")
					))
					.where(QDadosBancarios.dadosBancarios.codigo.eq(pg.getDadosBancarios().getCodigo()))
					.fetchOne();

				pg.setDadosBancarios(db);
			}

			listaPagamentos.add(pg);
		}

		return listaPagamentos;
	}

}
