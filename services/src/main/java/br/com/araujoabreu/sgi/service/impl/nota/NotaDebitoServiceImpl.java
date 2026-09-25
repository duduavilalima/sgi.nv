package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.faturamento.domain.*;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.ServicoTipoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.compras.domain.NotaDebito;
import br.com.araujoabreu.sgi.compras.domain.QNotaDebito;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.nota.NotaDebitoService;
import br.com.araujoabreu.sgi.service.nota.transfer.*;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.*;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.compras.NotaDebitoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.*;

@Service
public class NotaDebitoServiceImpl extends BaseSearchService<NotaDebito, NotaDebitoTO, NotaDebitoPesquisaTO, NotaDebitoRepository> implements NotaDebitoService {
	private final QNotaDebito qNotaDebito = QNotaDebito.notaDebito;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private ServicoTipoService servicoTipoService;

	@Autowired
	private NotaDebitoRepository notaDebitoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoService faturamentoService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private DozerHelper dozerHelper;

	public NotaDebitoServiceImpl(NotaDebitoRepository repository) { super(repository); }

	private NotaDebitoTO mapTO(NotaDebitoTO entity) {
		if (entity == null) return null;
		return getMapper().map(entity, NotaDebitoTO.class);
	}

	@Override
	public NotaDebitoTO buscarNotaDebitoById(Long id) {

		QEndereco qEndereco = new QEndereco("enderecoEmpresa");
		QCidade qCidade = new QCidade("cidadeEmpresa");
		QEndereco qEnderecoCliente = new QEndereco("enderecoCliente");
		QCidade qCidadeCliente = new QCidade("cidadeCliente");
		QPessoa qPessoa = QPessoa.pessoa;
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QUnidade qUnidade = new QUnidade("empresa");
		QUnidade qMatriz = new QUnidade("matriz");
		QContaCorrente qContaCorrentePrevista = new QContaCorrente("contaCorrentePrevista");
		QContaCorrente qContaCorrenteRecebimento = new QContaCorrente("contaCorrenteRecebimento");
		QBanco qBancoPrevisto = new QBanco("bancoPrevisto");
		QBanco qBancoRecebimento = new QBanco("bancoRecebimento");

		JPAQuery<NotaDebito> jpaQuery = select(
			Projections.bean(
			NotaDebito.class,
			qNotaDebito.codigo,
			qNotaDebito.numero,
			qNotaDebito.tipoDocumentoPrevisto,
			qNotaDebito.tipoDocumentoRecebimento,
			qNotaDebito.criador,
			qNotaDebito.dataCriacao,
			qNotaDebito.atualizador,
			qNotaDebito.dataAtualizacao,
			qNotaDebito.dataResponsavelRecebimento,
			qNotaDebito.dataResponsavelCancelarRecebimento,
			qNotaDebito.dataResponsavelCancelamento,
			qNotaDebito.dataResponsavelNumeracao,
			qNotaDebito.dataResponsavelCancelarNumeracao,
			qNotaDebito.dataResponsavelReverterCancelamento,
			qNotaDebito.dataEmissao,
			qNotaDebito.dataVencimento,
			qNotaDebito.dataAgendamento,
			qNotaDebito.dataPagamento,
			qNotaDebito.dataContabilizacao,
			qNotaDebito.protocoloEntrega,
			qNotaDebito.valorTributoRecebido,
			qNotaDebito.valorLiquidoRecebido,
			qNotaDebito.valorBruto,
			qNotaDebito.valorJuros,
			qNotaDebito.descricao,
			qNotaDebito.situacao,
			Projections.bean(
				Pessoa.class,
				qPessoa.codigo,
				qPessoa.nome,
				qPessoa.documento,
				qPessoa.tipoPessoa,
				Projections.bean(
					Endereco.class,
					qEnderecoCliente.codigo,
					qEnderecoCliente.endereco,
					qEnderecoCliente.uf,
					qEndereco.numero,
					qEndereco.complemento,
					qEndereco.tipoLogradouro,
					Projections.bean(
						Cidade.class,
						qCidadeCliente.codigo,
						qCidadeCliente.nome,
						qCidadeCliente.uf
					).as("cidade")
				).as("endereco")
			).as(qNotaDebito.cliente),
			Projections.bean(
				CentroFinanceiro.class,
				qCentroFinanceiro.identificadorFinal,
				qCentroFinanceiro.codigo,
				qCentroFinanceiro.nome).as("centroFinanceiro"),
			Projections.bean(
				ServicoTipo.class,
				qServicoTipo.codigo,
				qServicoTipo.sigla,
				qServicoTipo.nome
			).as("servicoTipo"),
			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.codnome,
				qUnidade.documento,
				qUnidade.inscricaoMunicipal,
				qUnidade.nome,
				Projections.bean(
					Endereco.class,
					qEndereco.codigo,
					qEndereco.endereco,
					qEndereco.uf,
					qEndereco.numero,
					qEndereco.complemento,
					qEndereco.tipoLogradouro,
					Projections.bean(
						Cidade.class,
						qCidade.codigo,
						qCidade.nome,
						qCidade.uf
					).as("cidade")
				).as("endereco"),
				Projections.bean(
					Unidade.class,
					qMatriz.codigo).as("matriz")
			).as("empresa"),
			Projections.bean(
				ContaCorrente.class,
				qContaCorrenteRecebimento.codigo,
				qContaCorrenteRecebimento.agencia,
				qContaCorrenteRecebimento.conta,
				Projections.bean(Banco.class,
					qBancoRecebimento.codigo,
					qBancoRecebimento.nome
					).as("banco")
			).as("contaCorrenteRecebimento"),
			Projections.bean(
				ContaCorrente.class,
				qContaCorrentePrevista.codigo,
				qContaCorrentePrevista.agencia,
				qContaCorrentePrevista.conta,
				Projections.bean(Banco.class,
					qBancoPrevisto.codigo,
					qBancoPrevisto.nome
				).as("banco")
			).as("contaCorrentePrevista")
			)
		);

		jpaQuery
			.join(qNotaDebito.centroFinanceiro, qCentroFinanceiro)
			.join(qNotaDebito.cliente, qPessoa)
			.leftJoin(qPessoa.endereco, qEnderecoCliente)
			.leftJoin(qNotaDebito.servicoTipo, qServicoTipo)
			.leftJoin(qEnderecoCliente.cidade, qCidadeCliente)
			.leftJoin(qNotaDebito.empresa, qUnidade)
			.leftJoin(qUnidade.matriz, qMatriz)
			.leftJoin(qUnidade.endereco, qEndereco)
			.leftJoin(qEndereco.cidade, qCidade)

			.leftJoin(qNotaDebito.contaCorrenteRecebimento, qContaCorrenteRecebimento)
			.leftJoin(qContaCorrenteRecebimento.banco, qBancoRecebimento)
			.leftJoin(qNotaDebito.contaCorrentePrevista, qContaCorrentePrevista)
			.leftJoin(qContaCorrentePrevista.banco, qBancoPrevisto)

			.where(qNotaDebito.codigo.eq(id));

		NotaDebito notaDebito = (NotaDebito) jpaQuery.fetchOne();

		if ( (notaDebito.getContaCorrenteRecebimento() == null || notaDebito.getContaCorrenteRecebimento().getCodigo() == null)
				&& notaDebito.getContaCorrentePrevista() != null )
			notaDebito.setContaCorrenteRecebimento(notaDebito.getContaCorrentePrevista());

		if (notaDebito.getTipoDocumentoRecebimento() == null && notaDebito.getTipoDocumentoPrevisto() != null)
			notaDebito.setTipoDocumentoRecebimento(notaDebito.getTipoDocumentoPrevisto());

		NotaDebitoTO notaDebitoTO = map(notaDebito);
		notaDebitoTO.setFaturamentoEtapaTO(faturamentoEtapaService.getFaturamentoEtapaByNotaDebitoId(id));
		notaDebitoTO.getCliente().setInscricaoEstadualCliente(pessoaJuridicaService.obterInscricaoEstadual(notaDebitoTO.getCliente().getCodigo()));
		this.preencherResponsaveis(notaDebitoTO);

		String criador = pessoaFisicaService.nomeUsuarioPorLogin(notaDebito.getCriador());
		if(criador != null && !criador.equals("Não informado"))
			notaDebitoTO.setCriador(pessoaFisicaService.nomeUsuarioPorLogin(notaDebito.getCriador()));

		String atualizador = pessoaFisicaService.nomeUsuarioPorLogin(notaDebito.getAtualizador());
		if(atualizador != null  && !atualizador.equals("Não informado"))
			notaDebitoTO.setAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(notaDebito.getAtualizador()));

		return notaDebitoTO;
	}

	private void preencherResponsaveis(NotaDebitoTO notaDebitoTO) {
		JPAQuery jpaQueryList = getJPAQueryFactory().from(qNotaDebito)
			.select(
				qNotaDebito.responsavelNumeracao.codigo,
				qNotaDebito.responsavelCancelarNumeracao.codigo,
				qNotaDebito.responsavelCancelamento.codigo,
				qNotaDebito.responsavelReverterCancelamento.codigo,
				qNotaDebito.responsavelRecebimento.codigo,
				qNotaDebito.responsavelCancelarRecebimento.codigo
			)
			.where(qNotaDebito.codigo.eq(notaDebitoTO.getCodigo()));

		List<Tuple> result = jpaQueryList.fetch();

		Tuple tupla = result.get(0);
		if (tupla.get(qNotaDebito.responsavelNumeracao.codigo) != null)
			notaDebitoTO.setResponsavelNumeracao(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelNumeracao.codigo)));

		if (tupla.get(qNotaDebito.responsavelCancelarNumeracao.codigo) != null)
			notaDebitoTO.setResponsavelCancelarNumeracao(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelCancelarNumeracao.codigo)));

		if (tupla.get(qNotaDebito.responsavelCancelamento.codigo) != null)
			notaDebitoTO.setResponsavelCancelamento(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelCancelamento.codigo)));

		if (tupla.get(qNotaDebito.responsavelReverterCancelamento.codigo) != null)
			notaDebitoTO.setResponsavelReverterCancelamento(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelReverterCancelamento.codigo)));

		if (tupla.get(qNotaDebito.responsavelRecebimento.codigo) != null)
			notaDebitoTO.setResponsavelRecebimento(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelRecebimento.codigo)));

		if (tupla.get(qNotaDebito.responsavelCancelarRecebimento.codigo) != null)
			notaDebitoTO.setResponsavelCancelarRecebimento(pessoaService.obterPessoa(tupla.get(qNotaDebito.responsavelCancelarRecebimento.codigo)));

	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaDebito> jpaQuery, NotaDebitoPesquisaTO exampleObj) {
		if (Check.isAssigned((exampleObj.getTipoFornecimento()))) {
			QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
			QFaturamento qFaturamento = QFaturamento.faturamento;
			jpaQuery.join(qNotaDebito.faturamentoEtapas, qFaturamentoEtapa)
					.join(qFaturamentoEtapa.faturamento, qFaturamento);
		}

		if (Check.isAssigned(exampleObj.getOrigemPesquisa()) && exampleObj.getOrigemPesquisa().equals("contas_a_receber"))
			jpaQuery.where(qNotaDebito.situacao.ne(NotaDebito.SituacaoNotaDebito.ABERTA));

		jpaQuery.orderBy(qNotaDebito.numero.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, NotaDebitoPesquisaTO exampleObj) {

		if (Check.isAssigned(exampleObj.getNumero())) {
			String num = exampleObj.getNumero().replace("ND/", "");
			int numero = Integer.parseInt(num);
			String numeroFinal = Util.formataNumero(numero, 9);
			num = "ND/" + numeroFinal;

			builder.and(qNotaDebito.numero.eq(num));
		}
		if (Check.isAssigned(exampleObj.getValorLiquidoRecebido())) {
			builder.and(qNotaDebito.valorLiquidoRecebido.eq(exampleObj.getValorLiquidoRecebido()));
		}
		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qNotaDebito.situacao.eq((NotaDebito.SituacaoNotaDebito)Util.getEnumByString(NotaDebito.SituacaoNotaDebito.class, exampleObj.getSituacao())));
		}
		if (Check.isAssigned(exampleObj.getEmpresa())) {
			builder.and(qNotaDebito.empresa.codigo.eq(exampleObj.getEmpresa().getCodigo()));
		}
		if (Check.isAssigned(exampleObj.getServicoTipo())) {
			builder.and(qNotaDebito.servicoTipo.nome.eq(exampleObj.getServicoTipo().getNome()));
		}
		if (Check.isAssigned(exampleObj.getCliente())) {
			builder.and(qNotaDebito.cliente.nome.upper().contains(exampleObj.getCliente().getNomeFantasia().toUpperCase().trim())
				.or(qNotaDebito.cliente.documento.upper().contains(exampleObj.getCliente().getNomeFantasia().toUpperCase().trim())));
		}
		if (Check.isAssigned(exampleObj.getCentroFinanceiro()) && Check.isAssigned(exampleObj.getCentroFinanceiro().getIdentificadorFinal())) {
			builder.and(qNotaDebito.centroFinanceiro.identificadorFinal.contains(String.valueOf(exampleObj.getCentroFinanceiro().getIdentificadorFinal())));
		}

		if (Check.isAssigned((exampleObj.getTipoFornecimento()))) {
			QFaturamento qFaturamento = QFaturamento.faturamento;
			builder.and(
				qFaturamento.tipoFornecimento.eq(
					(Faturamento.TipoFornecimentoNF)
						Util.getEnumByString(
							Faturamento.TipoFornecimentoNF.class,
							exampleObj.getTipoFornecimento()
						)
				)
			);
		}

		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Emissão")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataEmissao);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Vencimento")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataVencimento);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Agendamento")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataAgendamento);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Pagamento")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataPagamento);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Contabilização")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataContabilizacao);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getTipoData()) && exampleObj.getTipoData().equals("Cancelamento")) {
			DateExpression<Date> dtFiltro = Expressions.dateTemplate(Date.class, "date({0})", qNotaDebito.dataResponsavelCancelamento);
			if (Check.isAssigned(exampleObj.getDataDe()))
				builder.and(dtFiltro.goe(exampleObj.getDataDe()));
			if (Check.isAssigned(exampleObj.getDataAte()))
				builder.and(dtFiltro.loe(exampleObj.getDataAte()));
		}
		if (Check.isAssigned(exampleObj.getDescricao())) {
			builder.and(qNotaDebito.descricao.upper().contains(exampleObj.getDescricao().toUpperCase().trim()));
		}
	}

	@Override
	protected void doAfterLoad(NotaDebitoTO object) throws ServiceException {

	}

	@Override
	@Transactional
	public NotaDebitoTO receberNotaDebito(NotaDebitoTO notaDebitoTO) {
		NotaDebito notaDebito = getRepository().findById(notaDebitoTO.getId()).orElse(null);
		notaDebito.setSituacao(NotaDebito.SituacaoNotaDebito.RECEBIDA);

		Date data = new Date(System.currentTimeMillis());
		notaDebito.setAtualizador(securityService.getUsername());
		notaDebito.setDataAtualizacao(data);

		notaDebito.setResponsavelRecebimento(securityService.getUser().getPessoaFisica());
		notaDebito.setDataResponsavelRecebimento(data);

		notaDebito.setValorJuros(notaDebitoTO.getValorJuros());
		notaDebito.setValorLiquidoRecebido(notaDebitoTO.getValorLiquidoRecebido());
		notaDebito.setDataContabilizacao(notaDebitoTO.getDataContabilizacao());
		notaDebito.setDataPagamento(notaDebitoTO.getDataPagamento());
		notaDebito.setContaCorrenteRecebimento(getMapper().map(notaDebitoTO.getContaCorrenteRecebimento(), ContaCorrente.class));
		notaDebito.setTipoDocumentoRecebimento((Pagamento.TipoDocumentoPagamento) Util.getEnumByString(Pagamento.TipoDocumentoPagamento.class, notaDebitoTO.getTipoDocumentoRecebimento()));

		getRepository().save(notaDebito);

		return notaDebitoTO;
	}


	@Override
	@Transactional(readOnly = false)
	public NotaDebitoTO findLastNotaDebitoByUnidadeCodigo(NotaDebitoTO exampleObj) {
		int proximoNumero = 0;
		if (Check.isAssigned(exampleObj) && Check.isAssigned(exampleObj.getEmpresa()) && Check.isAssigned(exampleObj.getEmpresa().getCodigo())) {
			UnidadeTO unidade = unidadeService.obterUnidadeParaVerificacaoDeNumeracao(exampleObj.getEmpresa().getCodigo());
			proximoNumero = unidade.getProximaNd();
		}

		exampleObj.setNumero("ND/" + Util.formataNumero(proximoNumero + 1, 9));

		return exampleObj;
	}

	private void verificarUltimoNumero(NotaDebitoTO notaDebito) throws Exception {
		if (isNumeroDisponivel(notaDebito)) {
			UnidadeTO unidade = unidadeService.obterUnidadeParaVerificacaoDeNumeracao(notaDebito.getEmpresa().getCodigo());
			Integer numeroGravadoNaUnidadeProximaNd = unidade.getProximaNd() + 1;

			String ultimoNumeroGravadoString = "ND/" + Util.formataNumero(numeroGravadoNaUnidadeProximaNd, 9);

			Integer numeroEscolhidoPeloUsuarioInteiro = Integer.parseInt(notaDebito.getNumero().replace("ND/", ""));


			// Usou o número gerado pelo sistema
			if (numeroEscolhidoPeloUsuarioInteiro.equals(ultimoNumeroGravadoString)) {
				notaDebito.setNumero(ultimoNumeroGravadoString);
			} else if (numeroEscolhidoPeloUsuarioInteiro > unidade.getProximaNd()) {
				unidadeService.atualizarNumeroProximaNd(unidade.getCodigo(), numeroEscolhidoPeloUsuarioInteiro);
			} else if (numeroEscolhidoPeloUsuarioInteiro > numeroGravadoNaUnidadeProximaNd) {
				unidadeService.atualizarNumeroProximaNd(unidade.getCodigo(), numeroEscolhidoPeloUsuarioInteiro);
			}
		} else {
			throw new Exception("A " + notaDebito.getNumero() + " já existe.");
		}
	}

	/**
	 * Verifica se alguma ND já tem o número passado no parametro
	 *
	 * @param notaDebitoTO
	 * @return
	 */
	private boolean isNumeroDisponivel(NotaDebitoTO notaDebitoTO) {
		Long numeros = 0L;

		if (unidadeService.zerarNumeracaoNfAno(notaDebitoTO.getEmpresa().getCodigo())) {
			Date dataEmissao = notaDebitoTO.getDataEmissao();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataEmissao);
			int ano = cal.get(Calendar.YEAR);

			numeros = notaSaidaService.obterNumerosFaturamento(
				notaDebitoTO.getNumero(),
				notaDebitoTO.getEmpresa().getCodigo(),
				ano);
		} else {
			numeros = notaSaidaService.obterNumerosFaturamento(
				notaDebitoTO.getNumero(),
				notaDebitoTO.getEmpresa().getCodigo());
		}

		if (numeros != null && numeros > 0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Transactional()
	@Override
	public NotaDebitoTO numerarND(NotaDebitoTO transferObj) throws Exception {
		try {
			if (Check.isNotEmpty(transferObj.getSituacao()) && transferObj.getSituacao().equals(NotaDebito.SituacaoNotaDebito.ABERTA.getDescricao())) {
				NotaDebitoTO nd = this.buscarNotaDebitoByNumeroEmpresa(transferObj.getNumero(), transferObj.getEmpresa().getCodigo());
				if (nd != null && nd.getCodigo() != null) {
					String msg = "A %s já está sendo utilizada na empresa \"%s\". Tente numerar a ND novamente.";
					throw new ValidationException("ValidationException", String.format(msg, transferObj.getNumero(), transferObj.getEmpresa().getCodnome()));
				}
			}

			prepareValidateToSave(transferObj);

			unidadeService.getSelect().setLockMode(LockModeType.PESSIMISTIC_WRITE);

			verificarUltimoNumero(transferObj);

			doBeforeSaveNotaDebito(transferObj);

			getRepository().updateSituacao(transferObj.getCodigo(), NotaDebito.SituacaoNotaDebito.EM_RECEBIMENTO);
			transferObj.setSituacao(NotaDebito.SituacaoNotaDebito.EM_RECEBIMENTO.getDescricao());

			Date data = new Date(System.currentTimeMillis());
			getRepository().updateAtualizador(transferObj.getCodigo(), securityService.getUser().getUsername());
			getRepository().updateDataAtualizacao(transferObj.getCodigo(), data);

			getRepository().updateResponsavelNumeracao(transferObj.getCodigo(), securityService.getUser().getPessoaFisica());
			getRepository().updateDataResponsavelNumeracao(transferObj.getCodigo(), data);

			getRepository().updateDataEmissao(transferObj.getCodigo(), transferObj.getDataEmissao());
			getRepository().updateNumero(transferObj.getCodigo(), transferObj.getNumero());

			unidadeService.getSelect().setLockMode(LockModeType.NONE);

			return transferObj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	private void doBeforeSaveNotaDebito(NotaDebitoTO notaDebitoTO) {
		prepareValidateToSave(notaDebitoTO);

		if (notaDebitoTO != null && notaDebitoTO.getDataVencimento() != null) {
			Calendar dataVencimento = Calendar.getInstance();
			dataVencimento.setTimeInMillis(notaDebitoTO.getDataVencimento().getTime());
			dataVencimento.set(Calendar.AM_PM, Calendar.AM);
			dataVencimento.set(Calendar.HOUR, 12);
			notaDebitoTO.setDataVencimento(dataVencimento.getTime());
		}

	}

	public void prepareValidateToSave(NotaDebitoTO notaDebitoTO) throws ValidationException {
		if (!Check.isAssigned(notaDebitoTO.getEmpresa()) || !Check.isAssigned(notaDebitoTO.getEmpresa().getCodigo())) {
			throw new ValidationException("ValidationException", "Empresa é um campo de preenchimento obrigatório.");
		}

		if (Check.isEmpty(notaDebitoTO.getNumero())) {
			throw new ValidationException("ValidationException", "Número é um campo de preenchimento obrigatório.");
		} else {
			String num = notaDebitoTO.getNumero().replace("ND/", "");
			if (Check.isNumeric(num)) {
				int numero = Integer.parseInt(num);
				String numeroFinal = Util.formataNumero(numero, 9);
				notaDebitoTO.setNumero("ND/" + numeroFinal);
			} else {
				throw new ValidationException("ValidationException", "Número da ND inválido.");
			}
		}

		if (Check.isEmpty(notaDebitoTO.getSituacao())) {
			throw new ValidationException("ValidationException", "Situação é um campo de preenchimento obrigatório.");
		}

		if (notaDebitoTO.getDataEmissao() == null)
			throw new ValidationException("ValidationException", "Emissão é um campo de preenchimento obrigatório.");

		if (notaDebitoTO.getFaturamentoEtapaTO() == null)
			throw new ValidationException("ValidationException", "O objeto do etapa não pode ser nulo nem vazio.");

		if (notaDebitoTO.getFaturamentoEtapaTO().getFaturamento() == null)
			throw new ValidationException("ValidationException", "O objeto do faturamento não pode ser nulo nem vazio.");

		if (notaDebitoTO.getServicoTipo() != null && notaDebitoTO.getServicoTipo().getCodigo() != null) {
			ServicoTipo servicoTipo = findServicoTipoById(notaDebitoTO.getServicoTipo().getCodigo());
			if (servicoTipo == null)
				throw new ValidationException("ValidationException", "O Código do serviço informado é inválido.");
		}
	}

	/**
	 * Busca o ServicoTipo pelo seu ID.
	 *
	 * @param id
	 * @return
	 */
	private ServicoTipo findServicoTipoById(Long id) {
		ServicoTipoTO servicoTipoTO = servicoTipoService.findByCodigo(id);

		if (servicoTipoTO != null) {
			return getMapper().map(servicoTipoTO, ServicoTipo.class);
		}

		return null;
	}

	/**
	 * Busca uma lista de NotaSaidas usando parametros como criterio da busca.
	 *
	 * @param numero
	 * @param codigoUnidade
	 * @param tipo
	 * @param status
	 * @return
	 */
	private List<NotaDebito> findNotasByNumeroAndEmpresa(String numero, Long codigoUnidade, NotaDebito.SituacaoNotaDebito tipo, Nota.StatusNota status) {

		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<NotaDebito> jpaQueryNotaDebito = select();

		QBean qBean = Projections.bean(
			NotaSaida.class,
			qNotaSaida.codigo,
			qNotaSaida.dataEmissao
		);

		jpaQueryNotaDebito
			.select(qBean)
			.from(qNotaSaida)
			.leftJoin(qNotaSaida.empresa, qUnidade)
			.where(
				qNotaSaida.numero.eq(numero)
					.and(qUnidade.codigo.eq(codigoUnidade)
						//.and(qNotaSaida.tipoNotaFiscal.eq(tipo)
							.and(qNotaSaida.status.ne(status)))
			)
			.orderBy(qNotaSaida.numero.desc());

		return jpaQueryNotaDebito.fetch();
	}

	@Override
	@Transactional
	public NotaDebitoTO cancelarNumeracaoND(NotaDebitoTO transferObj) throws Exception {
		try {
			doBeforeSaveNotaDebito(transferObj);

			Date data = new Date(System.currentTimeMillis());
			getRepository().updateSituacao(transferObj.getCodigo(), NotaDebito.SituacaoNotaDebito.ABERTA);
			getRepository().updateNumero(transferObj.getCodigo(), "ND/" + Util.formataNumero(0, 9));

			getRepository().updateResponsavelCancelarNumeracao(transferObj.getCodigo(), securityService.getUser().getPessoaFisica());
			getRepository().updateDataResponsavelCancelamentoNumeracao(transferObj.getCodigo(), data);

			getRepository().updateDataAtualizacao(transferObj.getCodigo(), new Date());
			getRepository().updateAtualizador(transferObj.getCodigo(), getUser().getUsername());

			return transferObj;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	@Transactional
	public NotaDebitoTO salvarProtocolo(NotaDebitoTO transferObj) {
		getRepository().updateProtocoloEntrega(transferObj.getCodigo(), transferObj.getProtocoloEntrega());

		Date data = new Date(System.currentTimeMillis());
		getRepository().updateDataAtualizacao(transferObj.getCodigo(), data);
		getRepository().updateAtualizador(transferObj.getCodigo(), getUser().getUsername());

		transferObj.setDataAtualizacao(data);
		transferObj.setAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(getUser().getUsername()));

		return transferObj;
	}

	@Override
	@Transactional
	public void removerND(NotaDebitoTO transferObj) throws Exception {
		try {
			faturamentoEtapaRepository.removerRelacionamentoND(transferObj.getCodigo(),  FaturamentoEtapa.Status.A_FATURAR);
			notaDebitoRepository.deleteById(transferObj.getCodigo());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void cancelamento(Long codigo) {

	}

	@Override
	public NotaDebitoTO getNotaDebitoByCodigo(Long codigo) {
		QPessoa qPessoa = QPessoa.pessoa;
		QUnidade qUnidade = QUnidade.unidade;
		QEndereco qEndereco = QEndereco.endereco1;

		JPAQuery jpaQuery = select(
			Projections.fields(
				NotaDebito.class,
				qNotaDebito.codigo,
				qNotaDebito.numero,
				qNotaDebito.descricao,
				qNotaDebito.dataEmissao,
				qNotaDebito.dataVencimento,
				Projections.fields(
					Pessoa.class,
					qPessoa.codigo,
					qPessoa.nome
				).as("responsavelNumeracao")
			)
		);

		jpaQuery
			.leftJoin(qNotaDebito.responsavelNumeracao, qPessoa);
		jpaQuery.where(QNotaDebito.notaDebito.codigo.eq(codigo));

		NotaDebito notaDebito = (NotaDebito) jpaQuery.fetchOne();

		return map(notaDebito);
	}

	@Override
	@Transactional
	public NotaDebitoTO saveND(NotaDebitoTO transferObj) throws Exception {
		try {
			doBeforeSaveNotaDebito(transferObj);
			NotaDebito notaDebito = salvarNotaDebito(transferObj);
			transferObj.setCodigo(notaDebito.getCodigo());
			return transferObj;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private NotaDebito salvarNotaDebito(NotaDebitoTO transferObj) throws Exception {
		NotaDebito notaDebito = createOrUpdateNotaDebito(transferObj);

		faturamentoEtapaService.updateSituacaoFaturada(transferObj.getFaturamentoEtapaTO().getCodigo());
		// FIXME: UPDATE "Direto" não funciona quando ND ainda não existe. Refatorar.
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findByCodigo(transferObj.getFaturamentoEtapaTO().getCodigo());
		faturamentoEtapa.setNotaDebito(notaDebito);
		faturamentoEtapaRepository.save(faturamentoEtapa);
		faturamentoService.updateSituacaoEmFaturamento(transferObj.getFaturamentoEtapaTO().getFaturamento().getCodigo());

		return notaDebito;
	}

	@Transactional
	private NotaDebito createOrUpdateNotaDebito(NotaDebitoTO transferObj) throws Exception {
		if (transferObj.getCodigo() == null) {
			transferObj.setDataCriacao(new Date());
			transferObj.setCriador(getUser().getUsername());
		} else {
			transferObj.setDataAtualizacao(new Date());
			transferObj.setAtualizador(getUser().getUsername());
		}
		if (transferObj.getCodigo() == null) {
			return getRepository().save(createNotaDebito(transferObj));
		} else {
			return updateNotaDebito(transferObj);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private NotaDebito createNotaDebito(NotaDebitoTO transferObj) throws Exception {
		FaturamentoEtapaTO faturamentoEtapa = faturamentoEtapaService.obterFaturamentoEtapaParaNotaFiscalSemFaturamento(transferObj.getFaturamentoEtapaTO().getCodigo());
		Pessoa credor = new Pessoa();
		credor.setCodigo(transferObj.getEmpresa().getCodigo());
		transferObj.setContaCorrentePrevista(transferObj.getFaturamentoEtapaTO().getFaturamento().getContaCorrente());
		transferObj.setDataPagamento(transferObj.getDataVencimento());
		transferObj.setDataAgendamento(transferObj.getDataVencimento());
		transferObj.setTipoDocumentoPrevisto(transferObj.getFaturamentoEtapaTO().getFaturamento().getTipoDocumentoPagamento());
		transferObj.setValorLiquidoPrevisto(transferObj.getValorBruto());

		NotaDebito notaDebito = map(transferObj);

		notaDebito.setDataCriacao(new Date());
		notaDebito.setCriador(securityService.getUser().getPessoaFisica().getNome());

		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		centroFinanceiro.setCodigo(transferObj.getFaturamentoEtapaTO().getFaturamento().getCentroFinanceiro().getCodigo());
		notaDebito.setCentroFinanceiro(centroFinanceiro);

		notaDebito.setEmpresa(new Unidade(transferObj.getEmpresa().getCodigo()));
		notaDebito.setProtocoloEntrega(transferObj.getProtocoloEntrega());

		notaDebito.setDataEmissao(transferObj.getDataEmissao());
		notaDebito.setSituacao(NotaDebito.SituacaoNotaDebito.ABERTA);
		notaDebito.setDescricao(transferObj.getDescricao());

		if (transferObj.getServicoTipo() != null && transferObj.getServicoTipo().getCodigo() != null) {
			ServicoTipo servicoTipo = new ServicoTipo();
			servicoTipo.setCodigo(transferObj.getServicoTipo().getCodigo());
			notaDebito.setServicoTipo(servicoTipo);
		}

		notaDebito.setCriador(securityService.getUsername());
		notaDebito.setDataCriacao(new Date());

		return notaDebito;
	}

	@Transactional
	private NotaDebito updateNotaDebito(NotaDebitoTO transferObj) {
		NotaDebito notaDebito = select(
			Projections.fields(NotaDebito.class,
				qNotaDebito.codigo,
				qNotaDebito.numero,
				qNotaDebito.situacao,
				qNotaDebito.dataVencimento,
				qNotaDebito.dataEmissao,
				qNotaDebito.descricao,
				Projections.fields(ServicoTipo.class,
					qNotaDebito.servicoTipo.codigo,
					qNotaDebito.servicoTipo.sigla,
					qNotaDebito.servicoTipo.situacao
				).as(qNotaDebito.servicoTipo)
			)
		).where(qNotaDebito.codigo.eq(transferObj.getCodigo()).and(qNotaDebito.situacao.eq(NotaDebito.SituacaoNotaDebito.ABERTA))).fetchFirst();

		notaDebitoRepository.updateEmpresa(transferObj.getCodigo(), getMapper().map(transferObj.getEmpresa(), Unidade.class));

		//Edição da aba dados principais
		if (!transferObj.getNumero().equals(notaDebito.getNumero())) {
			notaDebitoRepository.updateNumero(transferObj.getCodigo(), transferObj.getNumero());
		}

		notaDebitoRepository.updateProtocoloEntrega(transferObj.getCodigo(), transferObj.getProtocoloEntrega());
		notaDebitoRepository.updateDescricao(transferObj.getCodigo(), transferObj.getDescricao());


		if (!Util.converteDateEmString(transferObj.getDataEmissao(), Constants.DATE_FORMAT_DATE).equals(Util.converteDateEmString(notaDebito.getDataEmissao(), Constants.DATE_FORMAT_DATE))) {
//			notaDebitoRepository.updateDataEmissao(transferObj.getCodigo(), Util.converteDateEmString(transferObj.getDataEmissao(), Constants.DATE_FORMAT_FULL));
			notaDebitoRepository.updateDataEmissao(transferObj.getCodigo(), transferObj.getDataEmissao());
		}

		if (!Util.converteDateEmString(transferObj.getDataVencimento(), Constants.DATE_FORMAT_DATE).equals(Util.converteDateEmString(notaDebito.getDataVencimento(), Constants.DATE_FORMAT_DATE))) {
			notaDebitoRepository.updateDataVencimento(transferObj.getCodigo(), transferObj.getDataVencimento());
		}

		if (!Objects.equals(transferObj.getServicoTipo().getCodigo(), notaDebito.getServicoTipo().getCodigo())) {
			ServicoTipo servicoTipo = new ServicoTipo();
			servicoTipo.setCodigo(transferObj.getServicoTipo().getCodigo());
			notaDebitoRepository.updateServicoTipo(transferObj.getCodigo(), servicoTipo);
		}

		notaDebitoRepository.updateDataAtualizacao(transferObj.getCodigo(), new Date());
		notaDebitoRepository.updateAtualizador(transferObj.getCodigo(), securityService.getUsername());

		return notaDebito;
	}

	@Override
	public List<ServicoTipoPesquisaTO> buscarServicos(ServicoTipoTO exampleObj) {
		List<ServicoTipoTO> listaHabitado = servicoTipoService.findByNomeAndSituacaoAndTipoFaturamento(exampleObj.getNome(), ServicoTipo.NOME_SITUACAO_ATIVO, ServicoTipo.NOME_TF_HABILITADO);
		List<ServicoTipoTO> listaHabitadoCidade = servicoTipoService.findByNomeAndSituacaoAndTipoFaturamento(exampleObj.getNome(), ServicoTipo.NOME_SITUACAO_ATIVO, ServicoTipo.NOME_TF_HABILITADO_CIDADE);

		List<ServicoTipoTO> lista = new ArrayList<>();
		lista.addAll(listaHabitado);
		lista.addAll(listaHabitadoCidade);

		lista.sort((t1, t2) -> t1.getNome().compareToIgnoreCase(t2.getNome()));

		return dozerHelper.map(lista, ServicoTipoPesquisaTO.class);
	}

	/**
	 * O ajuste abaixo foi feito para acertar a exibição do campo data de várias datas,
	 * pois devido ao erro no horário do servidor, é exibido o dia anterior ao gravado	 *
	 * @param data
	 * @return
	 */
	private Date ajustarHorario(Date data) {
		if (data == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(data.getTime());
		calendar.set(Calendar.HOUR, 12);
		return calendar.getTime();
	}

	@Override
	public NotaDebitoTO findNotaDebitoParaImpressaoByCodigo(String id) {

		NotaDebitoTO notaDebito = buscarNotaDebitoById(Long.parseLong(id));
		notaDebito.setDataEmissao(ajustarHorario(notaDebito.getDataEmissao()));

		NotaDebitoTO notaDebitoTO = mapTO(notaDebito);

		FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.getFaturamentoEtapaByNotaSaidaId(notaDebito.getCodigo());

		notaDebito.setFaturamentoEtapaTO(faturamentoEtapaTO);
		return notaDebito;
	}

	@Override
	public NotaDebitoTO montarNovaNotaDebito(Long idEtapa) {
		NotaDebito notaDebito = new NotaDebito();
		FaturamentoEtapa faturamentoEtapa = faturamentoEtapaRepository.findByCodigo(idEtapa);
		notaDebito.setServicoTipo(faturamentoEtapa.getFaturamento().getServicoTipo());

		PessoaTO clientePrincipalTO = pessoaService.buscarClientePrincipalPorCodCliente(faturamentoEtapa.getFaturamento().getCliente().getCodigo());

		notaDebito.setFaturamentoEtapas(new ArrayList<>());
		notaDebito.getFaturamentoEtapas().add(faturamentoEtapa);
		notaDebito.setCliente(faturamentoEtapa.getFaturamento().getCliente());
		notaDebito.setDataEmissao(new Date(System.currentTimeMillis()));
		notaDebito.setDataVencimento(calcularDataVencimento(notaDebito.getDataEmissao(),
								faturamentoEtapa.getFaturamento().getTipoCobrancaFaturamento().getTipoCobrancaVencimentos().get(0)));

		FaturamentoEtapaTO faturamentoEtapaTO = getMapper().map(faturamentoEtapa, FaturamentoEtapaTO.class);

		if (clientePrincipalTO != null && clientePrincipalTO.getCodigo() > 0) {
			faturamentoEtapaTO.getFaturamento().setSubcliente(faturamentoEtapaTO.getFaturamento().getCliente());
			faturamentoEtapaTO.getFaturamento().setCliente(clientePrincipalTO);
		}

		NotaDebitoTO notaDebitoTO = map(notaDebito);
		notaDebitoTO.setFaturamentoEtapaTO(faturamentoEtapaTO);

		return notaDebitoTO;
	}

	private Date calcularDataVencimento(Date dataBase, TipoCobrancaVencimento tipoCobrancaVencimento) {
		Calendar dataVencimento = null;

		if (!tipoCobrancaVencimento.getSolicitaData()) {
			dataVencimento = Calendar.getInstance();
			dataVencimento.setTime(dataBase);

			if (TipoCobrancaVencimento.TipoMesReferencia.PROXIMO.equals(tipoCobrancaVencimento.getMesReferencia())) {
				dataVencimento.set(Calendar.MONTH, dataVencimento.get(Calendar.MONTH) + 1);
			}

			if (Contrato.TipoVencimento.DIA_CORRIDO.equals(tipoCobrancaVencimento.getDiaVencimentoTipo()))
				dataVencimento.add(Calendar.DAY_OF_MONTH, tipoCobrancaVencimento.getDiaVencimento());
			else {
				for (int i = 1; i <= tipoCobrancaVencimento.getDiaVencimento(); i++) {
					dataVencimento.add(Calendar.DAY_OF_MONTH, 1);
					int dias = 0;
					if (dataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
						dias = 2;

					if (dataVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
						dias = 1;

					dataVencimento.add(Calendar.DAY_OF_MONTH, dias);
				}
			}
		}

		return dataVencimento != null ? dataVencimento.getTime() : null;
	}

	@Override
	public NotaDebitoTO buscarNotaDebitoByNumeroEmpresa(String numero, Long idEmpresa) {
		QUnidade qUnidade = QUnidade.unidade;
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;

		NotaDebito notaDebito = select(
			Projections.fields(NotaDebito.class,
				qNotaDebito.codigo,
				qNotaDebito.numero,
				qNotaDebito.situacao,
				qNotaDebito.dataVencimento,
				qNotaDebito.dataEmissao,
				qNotaDebito.descricao,
				Projections.fields(
					ServicoTipo.class,
					qServicoTipo.codigo,
					qServicoTipo.sigla,
					qServicoTipo.situacao
				).as(qNotaDebito.servicoTipo),
				Projections.fields(
					Unidade.class,
					qUnidade.codigo,
					qUnidade.nome,
					qUnidade.codnome
				).as(qNotaDebito.empresa)
			)
		)
		.join(qNotaDebito.empresa, qUnidade)
			.leftJoin(qNotaDebito.servicoTipo, qServicoTipo)
		.where(qNotaDebito.numero.eq(numero)
			.and(qUnidade.codigo.eq(idEmpresa))).fetchOne();

		return map(notaDebito);
	}
}
