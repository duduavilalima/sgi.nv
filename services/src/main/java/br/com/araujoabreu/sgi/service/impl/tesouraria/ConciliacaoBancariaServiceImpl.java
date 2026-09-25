package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.compras.domain.QParcelaNota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.ConciliacaoBancariaService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaRelacaoBancariaPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoLancamentoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.DadosConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ItemRelacaoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.RelacaoBancariaPagamentosVO;
import br.com.araujoabreu.sgi.tesouraria.domain.*;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.anexo.AnexoRepository;
import br.com.fasolti.sgi.repository.contasApagar.PagamentoRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoBancariaRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoAssociacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ConciliacaoLancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoPagamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ItemRelacaoRepository;
import br.com.fasolti.sgi.repository.tesouraria.LancamentoRepository;
import br.com.fasolti.sgi.repository.tesouraria.RelacaoBancariaRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ConciliacaoBancariaServiceImpl extends BaseSearchService<ConciliacaoBancaria, ConciliacaoBancariaTO, ConciliacaoBancariaPesquisaTO, ConciliacaoBancariaRepository> implements ConciliacaoBancariaService {

	private static final QConciliacaoBancaria qConciliacaoBancaria = QConciliacaoBancaria.conciliacaoBancaria;
	private static final List<ConciliacaoLancamento> listaConciliacaoLancamento = new ArrayList<>();
	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;
	@Autowired
	private AnexoRepository anexoRepository;
	@Autowired
	private ParametroRepository parametroRepository;
	@Autowired
	private ConciliacaoLancamentoRepository conciliacaoLancamentoRepository;
	@Autowired
	private ItemRelacaoRepository itemRelacaoRepository;
	@Autowired
	private ConciliacaoLancamentoAssociacaoRepository conciliacaoLancamentoAssociacaoRepository;
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@Autowired
	private RelacaoBancariaRepository relacaoBancariaRepository;
	@Autowired
	private ItemRelacaoPagamentoRepository itemRelacaoPagamentoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ChequeContaCorrenteRepository chequeContaCorrenteRepository;

	@Autowired
	public ConciliacaoBancariaServiceImpl(ConciliacaoBancariaRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(ConciliacaoBancaria entity, ConciliacaoBancariaTO object) throws ServiceException {
		Anexo anexo = getMapper().map(object.getAnexo(), Anexo.class);
		Anexo anexoSalvo = anexoRepository.save(anexo);

		object.getAnexo().setCodigo(anexoSalvo.getCodigo());
		salvarArquivo(object);
	}

	private List<String> getLinhasDoArquivo(AnexoTO anexo) {
		String texto = anexo.getTexto();
		String[] arrayLinhas = texto.split("\r\n|\r|\n");

		List<String> linhas = new ArrayList<>(Arrays.asList(arrayLinhas));

		return linhas;
	}

	/* -----------------
	 * 	VALIDAR ARQUIVO
	 * -----------------
	 */

	private void validarUnicidadeDaConciliacaoBancaria(ConciliacaoBancariaTO object) throws ValidationException {
		List<ConciliacaoBancaria> lista = new ArrayList<>();
		lista = getRepository().findByContaCorrenteCodigoAndData(object.getContaCorrente().getCodigo(), object.getData());

		if (lista != null && !lista.isEmpty()) {
			throw new ValidationException("ValidationException","Já existe uma conciliação bancária para a conta corrente nesta data. Não é possível realizar uma nova conciliação bancária");
		}
	}

	@Override
	public List<ConciliacaoBancariaTO> validarUnicidade(ConciliacaoBancariaTO object) {
		List<ConciliacaoBancaria> lista = new ArrayList<>();
		lista = getRepository().findByContaCorrenteCodigoAndData(object.getContaCorrente().getCodigo(), object.getData());
		return map(lista);
	}

	private void validarArquivo(ConciliacaoBancariaTO conciliacaoBancariaTO) throws ValidationException {

		AnexoTO anexo = conciliacaoBancariaTO.getAnexo();

		if (anexo != null && anexo.getTexto() != null && !StringUtils.isEmpty(anexo.getTexto().trim())) {

			List<String> linhas = getLinhasDoArquivo(anexo);

			Integer index = 0;
			Integer indexPenultimaLinha = linhas.size() - 1;
			Integer indexUltimaLinha = linhas.size();
			for (String linha : linhas) {
				index++;

				String colunaOito = linha.substring(7, 8);

				validarTamanhoDeCaracteresDaLinha(linha);
				validarHeaderDoArquivo(index, colunaOito);
				validarHeaderDoLote(index, colunaOito);
				validarDetalhe(index, indexPenultimaLinha, colunaOito);
				validarTrailerDoLote(index, indexPenultimaLinha, colunaOito);
				validarTrailerDoArquivo(index, indexUltimaLinha, colunaOito);

				validarContaCorrente(conciliacaoBancariaTO, linha, index);
				validarData(conciliacaoBancariaTO, linha, index);
			}

		} else {
			throw new ValidationException("ValidationException","Não há dados no arquivo");
		}

	}

	private void validarTamanhoDeCaracteresDaLinha(String linha) throws ValidationException {
		int tamanho = linha.length();
		if (tamanho != 240) {
			throw new ValidationException("ValidationException","As linhas do arquivo não possuem 240 caracteres (conforme layout padrão).");
		}
	}

	private void validarHeaderDoArquivo(Integer index, String colunaOito) throws ValidationException {
		if (index == 1) {
			String zero = "0";

			if (!zero.equals(colunaOito)) {
				throw new ValidationException("ValidationException","Não foi possível encontrar o header do arquivo (conforme layout padrão).");
			}
		}
	}

	private void validarHeaderDoLote(Integer index, String colunaOito) throws ValidationException {
		if (index == 2) {
			String um = "1";

			if (!um.equals(colunaOito)) {
				throw new ValidationException("ValidationException","Não foi possível encontrar o header do lote (conforme layout padrão).");
			}
		}
	}

	private void validarDetalhe(Integer index, Integer penultimaLinha, String colunaOito) throws ValidationException {
		if (index > 2 && index < penultimaLinha) {
			String tres = "3";

			if (!tres.equals(colunaOito)) {
				throw new ValidationException("ValidationException","A " + index + "ª linha do arquivo é inválida (conforme layout padrão).");
			}
		}
	}

	private void validarTrailerDoLote(Integer index, Integer penultimaLinha, String colunaOito) throws ValidationException {
		if (penultimaLinha.equals(index)) {
			String cinco = "5";

			if (!cinco.equals(colunaOito)) {
				throw new ValidationException("ValidationException","Não foi possível encontrar o trailer do lote (conforme layout padrão).");
			}
		}
	}

	private void validarTrailerDoArquivo(Integer index, Integer ultimaLinha, String colunaOito) throws ValidationException {
		if (ultimaLinha.equals(index)) {
			String nove = "9";

			if (!nove.equals(colunaOito)) {
				throw new ValidationException("ValidationException","Não foi possível encontrar o trailer do arquivo (conforme layout padrão).");
			}
		}
	}

	private void validarContaCorrente(ConciliacaoBancariaTO conciliacaoBancaria, String linha, Integer index) throws ValidationException {
		if (index == 1) {
			String codigoBanco = linha.substring(0, 3);

			String agencia = linha.substring(52, 57);
			String digitoAgencia = linha.substring(57, 58);

			String conta = linha.substring(58, 70);
			String digitoConta = linha.substring(71, 72);

			ContaCorrenteTO contaCorrente = conciliacaoBancaria.getContaCorrente();
			BancoTO banco = contaCorrente.getBanco();

			if (!agencia.equals(contaCorrente.getAgencia()) || !conta.equals(contaCorrente.getConta()) || !banco.getCodigo().equals(Long.valueOf(codigoBanco))) {
				throw new ValidationException("ValidationException","O arquivo não corresponde à conta corrente selecionada.");
			}

			//Se houver digitos de agência e conta, validar esses digitos.
			if (contaCorrente.getDigitoAgencia() != null && !digitoAgencia.equals(contaCorrente.getDigitoAgencia())) {
				throw new ValidationException("ValidationException","O arquivo não corresponde à conta corrente selecionada.");
			}
			if (contaCorrente.getDigito() != null && !digitoConta.equals(contaCorrente.getDigito())) {
				throw new ValidationException("ValidationException","O arquivo não corresponde à conta corrente selecionada.");
			}
		}
	}

	private void validarData(ConciliacaoBancariaTO conciliacaoBancaria, String linha, Integer index) throws ValidationException {
		if (index == 1) {

			String data = linha.substring(143, 151);
			int dia = Integer.parseInt(data.substring(0, 2));
			Integer mes = Integer.valueOf(data.substring(2, 4));
			int ano = Integer.parseInt(data.substring(4, 8));

			Calendar dataArquivo = Calendar.getInstance();
			dataArquivo.setTimeInMillis(0);
			dataArquivo.set(ano, mes - 1, dia); //Mês da classe Calendar é 0 based. Ex.: 0 = Janeiro, 1 = Fevereiro...

			Calendar dataTO = Calendar.getInstance();
			dataTO.setTime(conciliacaoBancaria.getData());

			if (dataArquivo.get(Calendar.DAY_OF_MONTH) != dataTO.get(Calendar.DAY_OF_MONTH) || dataArquivo.get(Calendar.MONTH) != dataTO.get(Calendar.MONTH) ||
				dataArquivo.get(Calendar.YEAR) != dataTO.get(Calendar.YEAR)) {

				throw new ValidationException("ValidationException","A data do arquivo não corresponde a data de conciliação informada.");
			}
		}
	}

	/* -------------------
	 * 	PROCESSAR ARQUIVO
	 * -------------------
	 */

	//NAO É MAIS UTILIZADO, A VALIDAÇÃO DO ARQUIVO É FEITA SOMENTE NO FRONTEND
	private void processarArquivo(ConciliacaoBancariaTO conciliacaoBancariaTO) {
		String saldoInicialString = "";
		String situacaoInicialString = "";
		String saldoFinalString = "";
		String situacaoFinalString = "";
		BigDecimal totalDebito = BigDecimal.ZERO;
		BigDecimal totalCredito = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		AnexoTO anexo = conciliacaoBancariaTO.getAnexo();
		List<String> linhas = getLinhasDoArquivo(anexo);

		Integer index = 0;
		Integer indexPenultimaLinha = linhas.size() - 1;
		for (String linha : linhas) {
			index++;

			if (index == 2) {
				saldoInicialString = linha.substring(150, 168);
				situacaoInicialString = linha.substring(168, 169);
			}
			if (Objects.equals(index, indexPenultimaLinha)) {
				saldoFinalString = linha.substring(150, 168);
				situacaoFinalString = linha.substring(168, 169);
			}

			if (index > 2 && index < indexPenultimaLinha) {
				ConciliacaoLancamento conciliacaoLancamento = criarConciliacaoLancamento(linha);

				if (ConciliacaoLancamento.DebitoCreditoEnum.D.equals(conciliacaoLancamento.getDebitoCredito())) {
					totalDebito = totalDebito.add(BigDecimal.valueOf(conciliacaoLancamento.getValor()));
				} else {
					totalCredito = totalCredito.add(BigDecimal.valueOf(conciliacaoLancamento.getValor()));
				}

				listaConciliacaoLancamento.add(conciliacaoLancamento);
			}
		}

		BigDecimal saldoInicial = new BigDecimal(saldoInicialString);
		BigDecimal saldoFinal = new BigDecimal(saldoFinalString);
		String situacaoInicial = situacaoInicialString;
		String situacaoFinal = situacaoFinalString;

		saldo = saldoInicial.add(totalCredito.subtract(totalDebito));

		conciliacaoBancariaTO.setTotalDebito(totalDebito);
		conciliacaoBancariaTO.setTotalCredito(totalCredito);

		conciliacaoBancariaTO.setSaldoFinal(saldoInicial);
		conciliacaoBancariaTO.setSaldoFinal(saldo);
	}

	//NAO É MAIS UTILIZADO, A VALIDAÇÃO DO ARQUIVO É FEITA SOMENTE NO FRONTEND
	private ConciliacaoLancamento criarConciliacaoLancamento(String linha) {
		String numeroRegistro = linha.substring(8, 13);
		String natureza = linha.substring(108, 111);
		String dataLancamento = linha.substring(142, 150);
		String valor = linha.substring(150, 168);
		String tipo = linha.substring(168, 169);
		String historico = linha.substring(176, 201);

		ConciliacaoLancamento conciliacaoLancamento = new ConciliacaoLancamento();
		conciliacaoLancamento.setSituacao(ConciliacaoLancamento.StatusConciliacaoLancamento.PENDENTE);
		conciliacaoLancamento.setRegistro(Integer.valueOf(numeroRegistro));
		conciliacaoLancamento.setNatureza(natureza);

		int dia = Integer.parseInt(dataLancamento.substring(0, 2));
		Integer mes = Integer.valueOf(dataLancamento.substring(2, 4));
		int ano = Integer.parseInt(dataLancamento.substring(4, 8));

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(ano, mes - 1, dia); //Mês da classe Calendar é 0 based. Ex.: 0 = Janeiro, 1 = Fevereiro...

		conciliacaoLancamento.setLancamento(calendar.getTime());
		conciliacaoLancamento.setValor(Double.valueOf(valor));

		if ("D".equals(tipo)) {
			conciliacaoLancamento.setDebitoCredito(ConciliacaoLancamento.DebitoCreditoEnum.D);
		} else {
			conciliacaoLancamento.setDebitoCredito(ConciliacaoLancamento.DebitoCreditoEnum.C);
		}

		return conciliacaoLancamento;
	}

	/*
	 *  ----------------
	 *   SALVAR ARQUIVO
	 *  ----------------
	 */

	private void salvarArquivo(ConciliacaoBancariaTO object) {
		AnexoTO anexo = object.getAnexo();

		String pastaDestino = parametroRepository.findByChave("tesouraria.conciliacao.anexo.diretorio").getValor();

		File fileDiretorio = new File(pastaDestino);
		if (!fileDiretorio.exists()) {
			fileDiretorio.mkdirs();
		}

		String nomeCompleto = criarNomeArquivo(pastaDestino, anexo.getNome());

		try {
			PrintWriter writer = new PrintWriter(nomeCompleto, "UTF-8");
			writer.print(anexo.getTexto());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("ValidationException",e.getMessage());
		}
	}

	/**
	 * Cria e valida o nome do arquivo
	 * prevenindo que não ocorra a sobrescrição de arquivos com o mesmo nome.
	 *
	 * @return
	 */
	private String criarNomeArquivo(String pastaDestino, String nomeArquivo) {

		StringBuilder nomeCompleto = new StringBuilder();

		Boolean nomeInvalido = Boolean.TRUE;
		Integer numero = 1;
		String nome = new String(nomeArquivo);

		while (nomeInvalido) {
			nomeCompleto.setLength(0);
			nomeCompleto.append(pastaDestino);
			nomeCompleto.append("/");
			nomeCompleto.append(nome);

			File fileNomeCompleto = new File(nomeCompleto.toString());
			if (fileNomeCompleto.exists()) {
				nome = new String("Cópia_" + numero + "_" + nomeArquivo);
				numero++;
			} else {
				nomeInvalido = Boolean.FALSE;
			}
		}

		return nomeCompleto.toString();
	}

	/*
	 *  ----------------------------
	 *   PESQUISAR RELAÇÃO BANCÁRIA
	 *  ----------------------------
	 */
	@Override
	public List<RelacaoBancariaPagamentosVO> pesquisarRelacaoBancaria(@RequestBody ConciliacaoBancariaRelacaoBancariaPesquisaTO filtro) throws Exception {
		try {
			return pesquisarItemRelacao(filtro);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	private List<RelacaoBancariaPagamentosVO> pesquisarItemRelacao(ConciliacaoBancariaRelacaoBancariaPesquisaTO filtro) {
		QItemRelacao qItemRelacao = QItemRelacao.itemRelacao;
		QRelacaoBancaria qRelacaoBancaria = QRelacaoBancaria.relacaoBancaria;
		QItemRelacaoPagamento qItemRelacaoPagamento = QItemRelacaoPagamento.itemRelacaoPagamento;
		QPagamento qPagamento = QPagamento.pagamento;
		QPessoa qPessoa = QPessoa.pessoa;

		JPAQuery query = select().from(qItemRelacao);
		query.innerJoin(qItemRelacao.relacaoBancaria, qRelacaoBancaria);
		query.innerJoin(qItemRelacao.listaPagamentos, qItemRelacaoPagamento);
		query.innerJoin(qItemRelacaoPagamento.pagamento, qPagamento);
		query.innerJoin(qPagamento.credor, qPessoa);

		BooleanExpression booleanExpression = qRelacaoBancaria.status.eq(RelacaoBancaria.StatusRelacaoBancaria.FINALIZADA).or(qRelacaoBancaria.status.eq(RelacaoBancaria.StatusRelacaoBancaria.AGUARDANDO_RETORNO));
		booleanExpression = booleanExpression.and(qItemRelacao.status.eq(ItemRelacao.StatusItemRelacao.CONFIRMADO));

		if (Check.isAssigned(filtro.getCodigoContaCorrenteConciliacao())) {
			booleanExpression = booleanExpression.and(qRelacaoBancaria.contaCorrente.codigo.eq(filtro.getCodigoContaCorrenteConciliacao()));
		}

		if (Check.isAssigned(filtro.getNumero())) {
			String numero = filtro.getNumero().trim();
			booleanExpression = booleanExpression.and(qPagamento.numero.containsIgnoreCase(numero));
		}

		if (Check.isAssigned(filtro.getBanco())) {
			booleanExpression = booleanExpression.and(qItemRelacao.banco.codigo.eq(filtro.getBanco().getCodigo()));
		}

		if (Check.isAssigned(filtro.getPagamentoDe())) {
			booleanExpression = booleanExpression.and(qRelacaoBancaria.dataPagamento.goe(filtro.getPagamentoDe()));
		}

		if (Check.isAssigned(filtro.getPagamentoAte())) {
			booleanExpression = booleanExpression.and(qRelacaoBancaria.dataPagamento.loe(filtro.getPagamentoAte()));
		}

		if (Check.isAssigned(filtro.getCredor())) {
			String nome = filtro.getCredor().toUpperCase().trim();

			QPessoaJuridica qPessoaJuridica = QPessoa.pessoa.as(QPessoaJuridica.class);
			booleanExpression = booleanExpression.and(
				qPessoaJuridica.nome.containsIgnoreCase(nome)
					.or(qPessoaJuridica.documento.containsIgnoreCase(nome))
					.or(qPessoaJuridica.nomeFantasia.containsIgnoreCase(nome))
			);
		}

		if (Check.isAssigned(filtro.getValorDe())) {
			booleanExpression = booleanExpression.and(qItemRelacao.total.goe(filtro.getValorDe()));
		}

		if (Check.isAssigned(filtro.getValorAte())) {
			booleanExpression = booleanExpression.and(qItemRelacao.total.loe(filtro.getValorAte()));
		}

		if (Check.isAssigned(filtro.getDocPagamento())) {
			Pagamento.TipoDocumentoPagamento tipoDocPag = getTipoDocumentoPagamento(filtro.getDocPagamento());

			if (tipoDocPag != null) {
				booleanExpression = booleanExpression.and(qPagamento.tipoDocumentoPagamento.eq(tipoDocPag));
			}
		}

		if (Check.isAssigned(filtro.getEmpresa())) {
			booleanExpression = booleanExpression.and(qRelacaoBancaria.unidade.codigo.eq(filtro.getEmpresa().getCodigo()));
		}

		if (Check.isAssigned(filtro.getGerencia())) {

			QParcelaNota qParcelaNota = QPagamento.pagamento.as(QParcelaNota.class);
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QNota qNota = QNota.nota;
			query.innerJoin(qParcelaNota.nota, qNota);
			query.innerJoin(qNota.centroFinanceiro, qCentroFinanceiro);

			booleanExpression = booleanExpression.and(qCentroFinanceiro.codigo.eq(filtro.getGerencia().getCodigo()));
		}

		booleanExpression = removerItensAssociados(booleanExpression, qItemRelacao);

		query.where(booleanExpression);
		query.orderBy(qPagamento.numero.asc());
		query.limit(101L);

		List<Tuple> result = query.select(Projections.tuple(qItemRelacao.codigo)).fetch();

		return montarVO(result, qItemRelacao);
	}

	/**
	 * Adiciona na expressão BooleanExpression a condição pora que os itens que já estão associados em uma conciliação não apareçam no resultado da pesquisa.
	 *
	 * @param booleanExpression
	 * @param qItemRelacao
	 * @return
	 */
	private BooleanExpression removerItensAssociados(BooleanExpression booleanExpression, QItemRelacao qItemRelacao) {
		Iterable<ConciliacaoLancamentoAssociacao> iterable = conciliacaoLancamentoAssociacaoRepository.findAll();
		List<ConciliacaoLancamentoAssociacao> listaAssociacao = Lists.newArrayList(iterable);

		if (listaAssociacao != null && !listaAssociacao.isEmpty()) {

			for (ConciliacaoLancamentoAssociacao associacao : listaAssociacao) {

				if (ConciliacaoLancamentoAssociacao.TipoConciliacaoLancamentoAssociacao.ASSOCIACAO_COM_ITEM_RELACAO_BANCARIA.equals(associacao.getTipo())) {
					if (associacao.getItemRelacao() != null && associacao.getItemRelacao().getCodigo() != null) {
						booleanExpression = booleanExpression.and(qItemRelacao.codigo.eq(associacao.getItemRelacao().getCodigo()).not());
					}
				}
			}
		}

		return booleanExpression;
	}

	private List<RelacaoBancariaPagamentosVO> montarVO(List<Tuple> lista, QItemRelacao qItemRelacao) {
		List<RelacaoBancariaPagamentosVO> listaVO = new ArrayList<>();

		for (Tuple row : lista) {

			ItemRelacao item = itemRelacaoRepository.findById(row.get(qItemRelacao.codigo)).orElse(null);

			RelacaoBancariaPagamentosVO vo = new RelacaoBancariaPagamentosVO();
			vo.setCodigo(String.valueOf(item.getCodigo()));

			if (item.getBanco() != null) {
				vo.setNomeBanco(item.getBanco().getNome());
			} else {
				vo.setNomeBanco("");
			}

			RelacaoBancaria relacaoBancaria = relacaoBancariaRepository.findByCodigo(item.getRelacaoBancaria().getCodigo());

			List<Pagamento> pagamentosDoItemRelacao = getPagamentosByItemRelacao(item);

			vo.setDataPagamento(relacaoBancaria.getDataPagamento());
			vo.setQtdadePagamentos(pagamentosDoItemRelacao.size());
			vo.setTotal(item.getTotal());

			if (pagamentosDoItemRelacao.size() > 1) {
				vo.setNumero("—");

				Set<Long> conjCredor = new HashSet<>();
				Set<Pagamento.TipoDocumentoPagamento> conjTipo = new HashSet<>();

				for (Pagamento pagamento : pagamentosDoItemRelacao) {
					conjCredor.add(pagamento.getCredor().getCodigo());
					conjTipo.add(pagamento.getTipoDocumentoPagamento());
				}

				PessoaTO pessoaTO = new PessoaTO();
				if (conjCredor.size() == 1) {
					pessoaTO = getMapper().map(pagamentosDoItemRelacao.get(0).getCredor(), PessoaTO.class);
				} else {
					pessoaTO.setNome("—");
				}
				vo.setCredor(pessoaTO);

				if (conjTipo.size() == 1) {
					vo.setTipoDocumentoPagamento(pagamentosDoItemRelacao.get(0).getTipoDocumentoPagamento().getDescricao());
				} else {
					vo.setTipoDocumentoPagamento("—");
				}

			} else if (pagamentosDoItemRelacao.size() == 1) {
				vo.setNumero(pagamentosDoItemRelacao.get(0).getNumero());

				if (pagamentosDoItemRelacao.get(0).getCredor() != null) {
					PessoaTO pessoaTO = getMapper().map(pagamentosDoItemRelacao.get(0).getCredor(), PessoaTO.class);
					vo.setCredor(pessoaTO);
				}

				if (pagamentosDoItemRelacao.get(0).getTipoDocumentoPagamento() != null) {
					vo.setTipoDocumentoPagamento(pagamentosDoItemRelacao.get(0).getTipoDocumentoPagamento().getDescricao());
				}
			}

			ItemRelacaoTO itemRelacaoTO = new ItemRelacaoTO();
			itemRelacaoTO.setCodigo(item.getCodigo());
			vo.setItemRelacao(itemRelacaoTO);

			listaVO.add(vo);
		}

		return listaVO;
	}

	private Pagamento.TipoDocumentoPagamento getTipoDocumentoPagamento(String docPagamento) {
		switch (docPagamento) {
			case "Boleto":
			case "Boleto (a vencer/vencidos)":
			case "Boleto (a vencer)":
			case "Boleto (vencidos)":
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

	/**
	 * Retorna os pagamentos de um Item Relação
	 *
	 * @param itemRelacao
	 * @return
	 */
	private List<Pagamento> getPagamentosByItemRelacao(ItemRelacao itemRelacao) {
		List<Pagamento> pagamentos = new ArrayList<>();
		List<ItemRelacaoPagamento> listaIrp = itemRelacaoPagamentoRepository.findByItemRelacaoCodigo(itemRelacao.getCodigo());

		QPagamento qPagamento = QPagamento.pagamento;
		QPessoa qPessoa = QPessoa.pessoa;

		for (ItemRelacaoPagamento irp : listaIrp) {

			Pagamento pgto = new Pagamento();

			JPAQuery query = select().from(qPagamento);
			query.innerJoin(qPagamento.credor, qPessoa);
			query.where(qPagamento.codigo.eq(irp.getPagamento().getCodigo()));

			List<Tuple> result = query.select(Projections.tuple(qPagamento.numero, qPagamento.tipoDocumentoPagamento, qPagamento.credor)).fetch();

			for (Tuple row : result) {
				pgto.setNumero(row.get(qPagamento.numero));
				pgto.setTipoDocumentoPagamento(row.get(qPagamento.tipoDocumentoPagamento));
				pgto.setCredor(row.get(qPagamento.credor));
			}

			pagamentos.add(pgto);
		}

		return pagamentos;
	}

	/*
	 * -------------------------------------
	 * SALVAR DADOS DA CONCILIAÇÃO BANCARIA
	 * -------------------------------------
	 */

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/salvardados")
	public ConciliacaoBancariaTO salvarDados(@RequestBody DadosConciliacaoBancariaTO dados) throws Exception {
		try {

			if (!temPermissaoParaGravarArquivo()) {
				throw new Exception("Erro durante a gravação do arquivo: acesso negado");
			}

			Anexo anexoSalvo = salvarAnexo(dados.getConciliacaoBancaria().getAnexo());
			ConciliacaoBancariaTO conciliacaoBancariaTO = dados.getConciliacaoBancaria();
			conciliacaoBancariaTO.getAnexo().setCodigo(anexoSalvo.getCodigo());

			salvarArquivo(conciliacaoBancariaTO);

			ConciliacaoBancaria conciliacaoBancariaSalvo = salvarConciliacaoBancaria(conciliacaoBancariaTO);
			salvarListaConciliacaoLancamento(dados.getListaConciliacaoLancamento(), conciliacaoBancariaSalvo);

			return getMapper().map(conciliacaoBancariaSalvo, ConciliacaoBancariaTO.class);
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	/**
	 * Verifica se o diretório está habilitado para gravar arquivos.
	 *
	 * @return
	 */
	private Boolean temPermissaoParaGravarArquivo() {
		String pastaDestino = parametroRepository.findById("tesouraria.conciliacao.anexo.diretorio").get().getValor();

		File fileDiretorio = new File(pastaDestino);
		if (!fileDiretorio.exists()) {
			fileDiretorio.mkdirs();
		}

		if (fileDiretorio.canWrite()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	private Anexo salvarAnexo(AnexoTO anexoTO) {
		anexoTO.setCriador(usuarioSecurityService.getUsername());

		Anexo anexo = getMapper().map(anexoTO, Anexo.class);

		return anexoRepository.save(anexo);
	}

	private ConciliacaoBancaria salvarConciliacaoBancaria(ConciliacaoBancariaTO conciliacaoBancariaTO) {
		ConciliacaoBancaria conciliacaoBancaria = getMapper().map(conciliacaoBancariaTO, ConciliacaoBancaria.class);
		conciliacaoBancaria.setCriador(usuarioSecurityService.getUsername());
		return getRepository().save(conciliacaoBancaria);
	}

	private void salvarListaConciliacaoLancamento(List<ConciliacaoLancamentoTO> listaConciliacaoLancamento, ConciliacaoBancaria conciliacaoBancariaSalvo) {
		for (ConciliacaoLancamentoTO conciliacaoLancamentoTO : listaConciliacaoLancamento) {

			ConciliacaoLancamento conciliacaoLancamento = getMapper().map(conciliacaoLancamentoTO, ConciliacaoLancamento.class);
			conciliacaoLancamento.setConciliacaoBancaria(conciliacaoBancariaSalvo);
			ConciliacaoLancamento conciliacaoLancamentoSalvo = conciliacaoLancamentoRepository.save(conciliacaoLancamento);

			salvarLancamento(conciliacaoLancamentoSalvo, conciliacaoLancamentoTO);
			salvarRelacaoBancaria(conciliacaoLancamentoSalvo, conciliacaoLancamentoTO);

			ChequeContaCorrenteTO cheque = conciliacaoLancamentoTO.getCheque();
			if (cheque != null && cheque.getCodigo() != null) {

				Double valorCheque = cheque.getValor().doubleValue();

				if (valorCheque.equals(conciliacaoLancamento.getValor())) {
					atualizarCheque(cheque, conciliacaoLancamento.getLancamento());
				}
			}
		}
	}

	private void salvarLancamento(ConciliacaoLancamento conciliacaoLancamentoSalvo, ConciliacaoLancamentoTO conciliacaoLancamentoTO) {
		if (conciliacaoLancamentoTO.getObjLancamento() != null) {

			Lancamento lancamento = getMapper().map(conciliacaoLancamentoTO.getObjLancamento(), Lancamento.class);

			if (lancamento.getCodigo() == null) {
				String numero = "LT/" + System.currentTimeMillis();
				if (numero.length() > 13) {
					numero = numero.substring(0, 13);
				}

				lancamento.setNumero(numero);
				lancamento.setCriador(usuarioSecurityService.getUsername());
//                lancamento.setDataCriacao(new Date());
				lancamento.setStatus(Lancamento.StatusLancamento.ABERTO);

				lancamento = lancamentoRepository.save(lancamento);
				lancamento.setNumero("LT/" + lancamento.getCodigo());

				lancamento = lancamentoRepository.save(lancamento);
			}

			ConciliacaoLancamentoAssociacao entity = new ConciliacaoLancamentoAssociacao();
			entity.setTipo(ConciliacaoLancamentoAssociacao.TipoConciliacaoLancamentoAssociacao.ASSOCIACAO_COM_LANCAMENTO_TESOURARIA);

			entity.setLancamento(lancamento);
			entity.setConciliacaoLancamento(conciliacaoLancamentoSalvo);

			conciliacaoLancamentoAssociacaoRepository.save(entity);
		}
	}

	private void salvarRelacaoBancaria(ConciliacaoLancamento conciliacaoLancamentoSalvo, ConciliacaoLancamentoTO conciliacaoLancamentoTO) {

		if (conciliacaoLancamentoTO.getObjPagamento() != null && !conciliacaoLancamentoTO.getObjPagamento().isEmpty()) {

			for (RelacaoBancariaPagamentosVO vo : conciliacaoLancamentoTO.getObjPagamento()) {

				ItemRelacao itemRelacao = new ItemRelacao();
				itemRelacao.setCodigo(vo.getItemRelacao().getCodigo());

				ConciliacaoLancamentoAssociacao entity = new ConciliacaoLancamentoAssociacao();
				entity.setTipo(ConciliacaoLancamentoAssociacao.TipoConciliacaoLancamentoAssociacao.ASSOCIACAO_COM_ITEM_RELACAO_BANCARIA);
				entity.setItemRelacao(itemRelacao);
				entity.setConciliacaoLancamento(conciliacaoLancamentoSalvo);

				conciliacaoLancamentoAssociacaoRepository.save(entity);
			}
		}
	}

	private void atualizarCheque(ChequeContaCorrenteTO cheque, Date dataConciliacao) {
		ChequeContaCorrente chequeContaCorrente = chequeContaCorrenteRepository.findById(cheque.getCodigo()).orElse(null);

		chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.PAGO);
		chequeContaCorrente.setDataConciliacao(dataConciliacao);

		Pessoa conciliador = usuarioSecurityService.getUser().getPessoaFisica();
		chequeContaCorrente.setResponsavelConciliacao(conciliador);

		chequeContaCorrenteRepository.save(chequeContaCorrente);
	}

	@Override
	protected void doBeforeUpdate(ConciliacaoBancaria entity, ConciliacaoBancariaTO object) {
		super.doBeforeUpdate(entity, object);
	}
}
