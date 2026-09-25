package br.com.araujoabreu.sgi.service.impl.faturamento;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.compras.domain.NotaDebito;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.QNotaDebito;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaida;
import br.com.araujoabreu.sgi.faturamento.domain.*;
import br.com.araujoabreu.sgi.service.administracao.PessoaFisicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaJuridicaService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaCheckDescricaoTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.*;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoEtapaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FaturamentoEtapaServiceImpl extends BaseSearchService<FaturamentoEtapa, FaturamentoEtapaTO, FaturamentoEtapaPesquisaTO, FaturamentoEtapaRepository> implements FaturamentoEtapaService {

	private static final QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

	@Autowired
	private FaturamentoEtapaRepository faturamentoEtapaRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private FaturamentoService faturamentoService;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private NotaSaidaRepository notaSaidaRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private NotaSaidaService notaSaidaService;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	public FaturamentoEtapaServiceImpl(FaturamentoEtapaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<FaturamentoEtapa> jpaQuery, FaturamentoEtapaPesquisaTO exampleObj) {
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QContrato qContrato = QContrato.contrato;
		QPessoa qCliente = QPessoa.pessoa;

		jpaQuery.from(FaturamentoEtapaServiceImpl.qFaturamentoEtapa);

		jpaQuery.leftJoin(qFaturamentoEtapa.notaSaida, QNotaSaida.notaSaida);

		jpaQuery.leftJoin(qFaturamentoEtapa.notaDebito, QNotaDebito.notaDebito);

		if (Check.isAssigned(exampleObj.getNomeCliente())) {
			jpaQuery.join(qFaturamentoEtapa.faturamento.cliente, qCliente);
		}

		if (Check.isAssigned(exampleObj.getNumCentroFinanceiro())) {
			jpaQuery.join(qFaturamentoEtapa.faturamento.centroFinanceiro, qCentroFinanceiro);
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			jpaQuery.join(qFaturamentoEtapa.faturamento.centroFinanceiro, qCentroFinanceiro);
			jpaQuery.join(qCentroFinanceiro.contrato, qContrato);
		}

		jpaQuery.select(Projections.fields(FaturamentoEtapa.class,
			qFaturamentoEtapa.codigo,
			qFaturamentoEtapa.valor,
			qFaturamentoEtapa.numero,
			qFaturamentoEtapa.descricao,
			qFaturamentoEtapa.inicio,
			qFaturamentoEtapa.termino,
			qFaturamentoEtapa.situacao,
			Projections.fields(NotaSaida.class,
				QNotaSaida.notaSaida.codigo,
				QNotaSaida.notaSaida.numero).as("notaSaida"),
			Projections.fields(NotaDebito.class,
				QNotaDebito.notaDebito.codigo,
				QNotaDebito.notaDebito.numero).as("notaDebito"),
			Projections.fields(Faturamento.class,
				qFaturamentoEtapa.faturamento.codigo,
				qFaturamentoEtapa.faturamento.tipo,
				qFaturamentoEtapa.faturamento.identificacao,
				qFaturamentoEtapa.faturamento.periodo,
				qFaturamentoEtapa.faturamento.tipoFornecimento,
				qFaturamentoEtapa.faturamento.tipoDocumentoFaturamento,
				Projections.fields(Pessoa.class,
					qFaturamentoEtapa.faturamento.cliente.codigo,
					qFaturamentoEtapa.faturamento.cliente.nome,
					qFaturamentoEtapa.faturamento.cliente.documento).as(qFaturamentoEtapa.faturamento.cliente),
				Projections.fields(CentroFinanceiro.class,
					qFaturamentoEtapa.faturamento.centroFinanceiro.codigo,
					qFaturamentoEtapa.faturamento.centroFinanceiro.situacao,
					qFaturamentoEtapa.faturamento.centroFinanceiro.nome,
					qFaturamentoEtapa.faturamento.centroFinanceiro.identificadorFinal,
					qFaturamentoEtapa.faturamento.centroFinanceiro.permiteClienteLivre).as(qFaturamentoEtapa.faturamento.centroFinanceiro)
			).as("faturamento")
		));

		jpaQuery.orderBy(qFaturamentoEtapa.numero.desc(), qFaturamentoEtapa.dataCriacao.desc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FaturamentoEtapaPesquisaTO exampleObj) {
		Faturamento f = getMapper().map(exampleObj, Faturamento.class);

		if (Check.isAssigned(exampleObj.getCodigoFaturamento()))
			builder.and(qFaturamentoEtapa.faturamento.codigo.eq(exampleObj.getCodigoFaturamento()));

		if (Check.isAssigned(exampleObj.getTotalDe()))
			builder.and(qFaturamentoEtapa.valor.goe(exampleObj.getTotalDe()));

		if (Check.isAssigned(exampleObj.getTotalAte()))
			builder.and(qFaturamentoEtapa.valor.loe(exampleObj.getTotalAte()));

		if (exampleObj.getDescricao() != null && !exampleObj.getDescricao().isEmpty()) {
			builder.and(qFaturamentoEtapa.descricao.lower().trim().contains(exampleObj.getDescricao().toLowerCase().trim()));
		}

		// A Situação da >>> ETAPA <<<
		if (Check.isAssigned(exampleObj.getSituacao())) {
			builder.and(qFaturamentoEtapa.situacao.eq((FaturamentoEtapa.Status) Util.getEnumByString(FaturamentoEtapa.Status.class, exampleObj.getSituacao())));
		}

		// FATURAMENTO
		if (exampleObj.getFaturamento() != null) {
			if (Check.isAssigned(exampleObj.getFaturamento().getIdentificacao())) {
				builder.and(qFaturamentoEtapa.faturamento.identificacao.lower().contains(exampleObj.getFaturamento().getIdentificacao().toLowerCase()));
			}

			if (Check.isAssigned(exampleObj.getFaturamento().getSituacao())) {
				builder.and(qFaturamentoEtapa.faturamento.situacao.eq((Faturamento.Status) Util.getEnumByString(Faturamento.Status.class, exampleObj.getFaturamento().getSituacao())));
			}

			if (Check.isAssigned(exampleObj.getFaturamento().getTipo())) {
				builder.and(qFaturamentoEtapa.faturamento.tipo.eq((Faturamento.TipoFaturamento) Util.getEnumByString(Faturamento.TipoFaturamento.class, exampleObj.getFaturamento().getTipo())));
			}
		}

		if (Check.isAssigned(exampleObj.getNumCentroFinanceiro())) {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			builder.and(qCentroFinanceiro.identificador.contains(exampleObj.getNumCentroFinanceiro()));
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			QContrato qContrato = QContrato.contrato;
			builder.and(qContrato.codigo.eq(exampleObj.getCodigoContrato()));
		}

		if (Check.isAssigned(exampleObj.getNomeCliente())) {
			QPessoaJuridica qPessoaJuridica = QPessoa.pessoa.as(QPessoaJuridica.class);
			builder.and(qPessoaJuridica.nome.upper().trim().contains(exampleObj.getNomeCliente().toUpperCase().trim()))
				.or(qPessoaJuridica.documento.upper().trim().contains(exampleObj.getNomeCliente().toUpperCase().trim()))
				.or(qPessoaJuridica.nomeFantasia.upper().trim().contains(exampleObj.getNomeCliente().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getTipoFornecimento())) {
			builder.and(qFaturamentoEtapa.faturamento.tipoFornecimento.eq(f.getTipoFornecimento()));
		}

		if (Check.isAssigned(exampleObj.getTipoDocumentoFaturamento())) {
			builder.and(qFaturamentoEtapa.faturamento.tipoDocumentoFaturamento.eq(f.getTipoDocumentoFaturamento()));
	}
	}

	@Override
	protected void doAfterSearch(List<FaturamentoEtapa> entities, List<FaturamentoEtapaPesquisaTO> result) throws ServiceException {
		for (FaturamentoEtapaPesquisaTO transferObj : result) {
			if (transferObj.getNotaSaida() != null && transferObj.getNotaSaida().getCodigo() != null) {
				NotaSaidaTO notaSaidaTO = notaSaidaService.getNotaSaidaByCodigo(transferObj.getNotaSaida().getCodigo());
				transferObj.setNotaSaida(new NotaSaidaTO(notaSaidaTO.getCodigo(), notaSaidaTO.getNumero()));
			}

			transferObj.setNomeCliente(pessoaJuridicaService.obterPessoaFisicaApenasNomeFantasia(transferObj.getFaturamento().getCliente().getCodigo()));

			if (transferObj.getNumero() != null) {
				if (transferObj.getNumero().length() == 1) {
					transferObj.setNumero("00" + transferObj.getNumero());
				} else if (transferObj.getNumero().length() == 2) {
					transferObj.setNumero("0" + transferObj.getNumero());
				}
			}
		}
	}

	@Override
	protected void doAfterLoad(FaturamentoEtapaTO transferObj) {
		if (transferObj != null && transferObj.getCriador() != null) {
			transferObj.setNomeCriador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getCriador()));
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			transferObj.setNomeAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getAtualizador()));
		}

		if (transferObj != null && transferObj.getFaturamento() != null && transferObj.getFaturamento().getCriador() != null) {
			transferObj.setNomeCriadorFaturamento(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getFaturamento().getCriador()));
		}

		if (transferObj != null && transferObj.getNotaSaida() != null && transferObj.getNotaSaida().getCodigo() != null) {
			transferObj.setNotaSaida(notaSaidaService.obterNotaSaidaApenasCodigoENumero(transferObj.getNotaSaida().getCodigo()));
		}
	}

	@Override
	@Transactional
	protected void doBeforeSave(FaturamentoEtapa entity, FaturamentoEtapaTO faturamentoEtapaTO) {
		prepareValidateToSave(faturamentoEtapaTO);
		entity.setNotaSaida(null);
	}

	@Override
	@Transactional
	protected void doBeforeUpdate(FaturamentoEtapa entity, FaturamentoEtapaTO faturamentoEtapaTO) {
		checkStatusToUpdate(faturamentoEtapaTO);

		if (faturamentoEtapaTO.getCodigo() == null) {
			throw new ValidationException("ValidationException","Código obrigatório!");
		}

		prepareValidateToSave(faturamentoEtapaTO);

		if (faturamentoEtapaTO.getNotaSaida() != null && faturamentoEtapaTO.getNotaSaida().getCodigo() != null) {
			entity.setNotaSaida(new NotaSaida(faturamentoEtapaTO.getNotaSaida().getCodigo()));
			faturamentoEtapaTO.setNotaSaida(new NotaSaidaTO(faturamentoEtapaTO.getNotaSaida().getCodigo()));
		} else {
			entity.setNotaSaida(null);
			faturamentoEtapaTO.setNotaSaida(null);
		}
	}

	@Override
	@Transactional
	protected void doAfterUpdate(FaturamentoEtapa entity, FaturamentoEtapaTO to) throws ServiceException {
		if (entity.getNotaSaida() != null && entity.getNotaSaida().getCodigo() == null) {
			entity.setNotaSaida(null);
		}
	}

	private List<FaturamentoEtapa> obterEtapasDeUmFaturamento(Long codigoFaturamento) {
		JPAQuery jpaQuery = select();
		jpaQuery.from(QFaturamentoEtapa.faturamentoEtapa);
		jpaQuery.select(Projections.fields(FaturamentoEtapa.class, QFaturamentoEtapa.faturamentoEtapa.codigo));
		jpaQuery.where(QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(codigoFaturamento));
		jpaQuery.orderBy(QFaturamentoEtapa.faturamentoEtapa.codigo.desc());
		return (List<FaturamentoEtapa>) jpaQuery.fetch();
	}

	@Override
	public List<Long> obterIdsANDCodigoNotaSaidaEtapasDeUmFaturamento(Long codigoFaturamento) {
		JPAQuery jpaQuery = select();
		jpaQuery.from(QFaturamentoEtapa.faturamentoEtapa);
		jpaQuery.select(
			Projections.fields(
				FaturamentoEtapa.class,
				QFaturamentoEtapa.faturamentoEtapa.codigo,
				Projections.fields(NotaSaida.class,
					QFaturamentoEtapa.faturamentoEtapa.notaSaida.codigo
				).as("notaSaida"))
		);
		jpaQuery.where(QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(codigoFaturamento));
		jpaQuery.orderBy(QFaturamentoEtapa.faturamentoEtapa.numero.desc());
		List<FaturamentoEtapa> list = jpaQuery.fetch();
		List<Long> ids = new ArrayList();
		for (FaturamentoEtapa etapa : list) {
			ids.add(etapa.getNotaSaida().getCodigo());
		}
		return ids;
	}

	private FaturamentoEtapa obterFaturamentoEtapaSimples(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery.from(QFaturamentoEtapa.faturamentoEtapa).join(QFaturamentoEtapa.faturamentoEtapa.faturamento, QFaturamento.faturamento);
		jpaQuery.select(Projections.fields(FaturamentoEtapa.class, QFaturamentoEtapa.faturamentoEtapa.codigo, QFaturamentoEtapa.faturamentoEtapa.situacao,
			Projections.bean(Faturamento.class, QFaturamento.faturamento.codigo).as("faturamento")));
		jpaQuery.where(QFaturamentoEtapa.faturamentoEtapa.codigo.eq(codigo));
		return (FaturamentoEtapa) jpaQuery.fetchOne();
	}

	@Override
	@Transactional
	protected void doBeforeDelete(FaturamentoEtapaTO object) throws ServiceException {
		if (object.getId() != null) {
			FaturamentoEtapa faturamentoEtapa = obterFaturamentoEtapaSimples(object.getId());

			if (faturamentoEtapa != null) {
				if (!FaturamentoEtapa.Status.A_FATURAR.equals(faturamentoEtapa.getSituacao())) {
					String msg = String.format("Etapa %s não pode ser removida. Status: %s.", faturamentoEtapa.getNumero(), faturamentoEtapa.getSituacao().getDescricao());
					throw new ValidationException("ValidationException",msg);
				}
			}

			List<FaturamentoEtapa> listFaturamentoEtapa = obterEtapasDeUmFaturamento(faturamentoEtapa.getFaturamento().getCodigo());

			if (listFaturamentoEtapa.isEmpty()) {
				faturamentoService.updateSituacaoAberto(faturamentoEtapa.getFaturamento().getCodigo());
			}
		}
	}

	/**
	 * Verificar os status que podem ser permitido atualizar a etapa.
	 *
	 * @param faturamentoEtapaTO O objeto a ser verificado o status.
	 * @throws ServiceException Caso seja um status diferente do permitido para atualização.
	 */
	public void checkStatusToUpdate(FaturamentoEtapaTO faturamentoEtapaTO) throws ServiceException {
		if (!FaturamentoEtapa.Status.A_FATURAR.getNome().equals(faturamentoEtapaTO.getSituacao())) {
			throw new ValidationException("ValidationException",String.format("O status atual da etapa (%s), não permite atualização.", faturamentoEtapaTO.getSituacao()));
		}
	}

	public void prepareValidateToSave(FaturamentoEtapaTO faturamentoEtapaTO) throws ServiceException {
		if (faturamentoEtapaTO == null) {
			throw new ValidationException("ValidationException","A etapa de faturamento não pode ser nula nem vazia.");
		}

		if (faturamentoEtapaTO.getFaturamento() == null) {
			throw new ValidationException("ValidationException","O objeto do faturamento não pode ser nulo nem vazio.");
		}

		if (faturamentoEtapaTO.getFaturamento().getCodigo() == null) {
			throw new ValidationException("ValidationException","O código do faturamento não pode ser nulo nem vazio.");
		}

		if (Check.isEmpty(faturamentoEtapaTO.getSituacao())) {
			throw new ValidationException("ValidationException","A situação da etapa não pode ser nula nem vazia.");
		}

		if (Check.isEmpty(faturamentoEtapaTO.getDescricao())) {
			throw new ValidationException("ValidationException","O campo descrição item é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getDescricao().length() > 200) {
			throw new ValidationException("ValidationException","A descrição do item informada é muito longa.");
		}

		if (Check.isEmpty(faturamentoEtapaTO.getDiscriminacaoInicial())) {
			throw new ValidationException("ValidationException","O campo discriminação dos serviços (parte inicial) é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getDiscriminacaoInicial().length() > 2000) {
			throw new ValidationException("ValidationException","A discriminação dos serviços (parte inicial) informada é muito longa.");
		}

		if (!Check.isEmpty(faturamentoEtapaTO.getDiscriminacaoCentral()) && faturamentoEtapaTO.getDiscriminacaoCentral().length() > 2000) {
			throw new ValidationException("ValidationException","A discriminação dos serviços (parte central) informada é muito longa.");
		}

		if (Check.isEmpty(faturamentoEtapaTO.getDiscriminacaoFinal())) {
			throw new ValidationException("ValidationException","O campo discriminação dos serviços (parte final) é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getDiscriminacaoFinal().length() > 2000) {
			throw new ValidationException("ValidationException","A discriminação dos serviços (parte final) informada é muito longa.");
		}

		if (faturamentoEtapaTO.getValor() == null) {
			throw new ValidationException("ValidationException","O campo valor bruto é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getValor().doubleValue() <= 0) {
			throw new ValidationException("ValidationException","O valor bruto deve ser maior do que zero.");
		}

		if (faturamentoEtapaTO.getValorDesconto() == null) {
			throw new ValidationException("ValidationException","O campo desconto é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getValorJuros() == null) {
			throw new ValidationException("ValidationException","O campo juros é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getValorMulta() == null) {
			throw new ValidationException("ValidationException","O campo multa é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getValorPermuta() == null) {
			throw new ValidationException("ValidationException","O campo permuta é um campo de preenchimento obrigatório.");
		}

		if ((faturamentoEtapaTO.getValor().toString().replace(".", "").replace(",", "")).length() > 14) {
			throw new ValidationException("ValidationException","Valor bruto não pode ser maior de 999.999.999.999,99O.");
		}

		if (faturamentoEtapaTO.getInicio() == null) {
			throw new ValidationException("ValidationException","O campo início é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getTermino() == null) {
			throw new ValidationException("ValidationException","O campo término é um campo de preenchimento obrigatório.");
		}

		if (faturamentoEtapaTO.getTermino().before(faturamentoEtapaTO.getInicio())) {
			throw new ValidationException("ValidationException","A data de término da etapa deve ser maior ou igual a data de início.");
		}
	}

	@Override
	protected OrderSpecifier defineSearchOrder(FaturamentoEtapaPesquisaTO exampleObj) {
		QFaturamento qFaturamento = QFaturamento.faturamento;
		return (new OrderSpecifier<>(Order.ASC, qFaturamento.codigo));
	}

	/**
	 * Busca um faturamento etapa pelo codigo de sua nota de saida
	 *
	 * @param id
	 * @return
	 */
	public FaturamentoEtapaTO getFaturamentoEtapaByNotaSaidaId(Long id) {
		QPessoa qPessoa = QPessoa.pessoa;
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		QFaturamento qFaturamento = QFaturamento.faturamento;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QContrato qContrato = QContrato.contrato;

		JPAQuery<FaturamentoEtapa> jpaQueryFaturamentoEtapa = select();

		QBean qBeanFaturamentoEtapa = Projections.bean(
			FaturamentoEtapa.class,
			qFaturamentoEtapa.codigo,
			qFaturamentoEtapa.valor,
			Projections.bean(
				Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.tipo,
				Projections.bean(
					Pessoa.class,
					qPessoa.codigo,
					qPessoa.nome,
					qPessoa.documento
				).as("cliente"),
				Projections.bean(
					CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.identificadorFinal,
					qCentroFinanceiro.nome,
					Projections.bean(Contrato.class, qContrato.codigo).as("contrato")
				).as("centroFinanceiro")
			).as("faturamento")
		);

		jpaQueryFaturamentoEtapa
			.select(qBeanFaturamentoEtapa)
			.from(qFaturamentoEtapa)
			.join(qFaturamentoEtapa.faturamento, qFaturamento)
			.join(qFaturamento.cliente, qPessoa)
			.join(qFaturamento.centroFinanceiro, qCentroFinanceiro)
			.leftJoin(qCentroFinanceiro.contrato, qContrato)
			.leftJoin(qFaturamentoEtapa.notaSaida, qNotaSaida)
			.where(qNotaSaida.codigo.eq(id));

		FaturamentoEtapa faturamentoEtapa = jpaQueryFaturamentoEtapa.fetchOne();

		return map(faturamentoEtapa);
	}

	public FaturamentoEtapaTO getFaturamentoEtapaByNotaDebitoId(Long id) {
		QPessoa qPessoa = new QPessoa("cliente");
		QNotaDebito qNotaDebito = QNotaDebito.notaDebito;
		QFaturamento qFaturamento = QFaturamento.faturamento;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QContrato qContrato = QContrato.contrato;
		QEndereco qEndereco = QEndereco.endereco1;
		QCidade qCidade = new QCidade("cidade");
		QCidade qCidadePrestacao = new QCidade("cidadePrestacao");
		QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
		QBanco qBanco= QBanco.banco;
		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
		QTipoCobrancaFaturamento qTipoCobrancaFaturamento = QTipoCobrancaFaturamento.tipoCobrancaFaturamento;

		JPAQuery<FaturamentoEtapa> jpaQueryFaturamentoEtapa = select();

		QBean qBeanFaturamentoEtapa = Projections.bean(
			FaturamentoEtapa.class,
			qFaturamentoEtapa.codigo,
			qFaturamentoEtapa.valor,
			qFaturamentoEtapa.numero,
			qFaturamentoEtapa.situacao,
			qFaturamentoEtapa.inicio,
			qFaturamentoEtapa.termino,
			qFaturamentoEtapa.descricao,
			qFaturamentoEtapa.discriminacaoInicial,
			qFaturamentoEtapa.discriminacaoCentral,
			qFaturamentoEtapa.discriminacaoFinal,
			Projections.bean(
				Faturamento.class,
				qFaturamento.codigo,
				qFaturamento.tipo,
				qFaturamento.situacao,
				qFaturamento.periodo,
				qFaturamento.identificacao,
				qFaturamento.tipoDocumentoPagamento,
				qFaturamento.tipoFornecimento,
				Projections.bean(
					Cidade.class,
					qCidade.codigo,
					qCidade.nome,
					qCidade.uf
				).as("cidadePrestacaoServico"),
				Projections.bean(
					ContaCorrente.class,
					qContaCorrente.codigo,
					qContaCorrente.agencia,
					qContaCorrente.conta,
					Projections.bean(
						Banco.class,
						qBanco.codigo,
						qBanco.nome
						).as("banco")
				).as("contaCorrente"),
				Projections.bean(
					TipoCobrancaFaturamento.class,
					qTipoCobrancaFaturamento.codigo,
					qTipoCobrancaFaturamento.descricao
				).as("tipoCobrancaFaturamento"),
				Projections.bean(
					Pessoa.class,
					qPessoa.codigo,
					qPessoa.nome,
					qPessoa.tipoPessoa,
					qPessoa.documento,
					Projections.bean(
						Endereco.class,
						qEndereco.endereco,
						qEndereco.uf,
						Projections.bean(
							Cidade.class,
							qCidade.codigo,
							qCidade.nome,
							qCidade.uf
						).as("cidade")
					).as("endereco")
				).as(qFaturamento.cliente),
				Projections.bean(
					CentroFinanceiro.class,
					qCentroFinanceiro.codigo,
					qCentroFinanceiro.identificadorFinal,
					qCentroFinanceiro.nome,
					Projections.bean(Contrato.class, qContrato.codigo).as("contrato")
				).as("centroFinanceiro")
			).as("faturamento")
		);

		jpaQueryFaturamentoEtapa
			.select(qBeanFaturamentoEtapa)
			.join(qFaturamentoEtapa.faturamento, qFaturamento)
			.join(qFaturamento.centroFinanceiro, qCentroFinanceiro)
			.join(qFaturamento.cliente, qPessoa)
			.leftJoin(qFaturamento.cidadePrestacaoServico, qCidadePrestacao)
			.leftJoin(qPessoa.endereco, qEndereco)
			.leftJoin(qEndereco.cidade, qCidade)
			.leftJoin(qCentroFinanceiro.contrato, qContrato)
			.leftJoin(qFaturamentoEtapa.notaDebito, qNotaDebito)
			.leftJoin(qFaturamento.servicoTipo, qServicoTipo)
			.leftJoin(qFaturamento.contaCorrente, qContaCorrente)
			.leftJoin(qContaCorrente.banco, qBanco)
			.leftJoin(qFaturamento.tipoCobrancaFaturamento, qTipoCobrancaFaturamento)
			.where(qNotaDebito.codigo.eq(id));

		FaturamentoEtapa faturamentoEtapa = jpaQueryFaturamentoEtapa.fetchOne();

		PessoaTO clientePrincipalTO = pessoaService.buscarClientePrincipalPorCodCliente(faturamentoEtapa.getFaturamento().getCliente().getCodigo());

		FaturamentoEtapaTO faturamentoEtapaTO = map(faturamentoEtapa);

		if (clientePrincipalTO != null && clientePrincipalTO.getCodigo() > 0) {
			faturamentoEtapaTO.getFaturamento().setSubcliente(faturamentoEtapaTO.getFaturamento().getCliente());
			faturamentoEtapaTO.getFaturamento().setCliente(clientePrincipalTO);
		}

		return faturamentoEtapaTO;
	}

	@Override
	public FaturamentoEtapaTO pesquisarFaturamentoEtapaPorCodigoNotaSaidaComCodigoUnidadeCentroFinanceiro(Long codigoNotaSaida) {
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;

		QBean qBean = Projections.bean(
			FaturamentoEtapa.class,
			qFaturamentoEtapa.codigo,
			Projections.bean(
				NotaSaida.class,
				qFaturamentoEtapa.notaSaida.codigo
			).as("notaSaida"),
			Projections.bean(
				Faturamento.class,
				qFaturamentoEtapa.faturamento.codigo,
				Projections.bean(
					CentroFinanceiro.class,
					qFaturamentoEtapa.faturamento.centroFinanceiro.codigo
				).as("centroFinanceiro")
			).as("faturamento")
		);

		JPAQuery<FaturamentoEtapa> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qFaturamentoEtapa)
			.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida))
			.orderBy(qFaturamentoEtapa.codigo.asc());

		FaturamentoEtapa faturamentoEtapa = jpaQuery.fetchFirst();
		FaturamentoEtapaTO faturamentoEtapaTO = map(faturamentoEtapa);
		Long codigoCentroFinanceiro = faturamentoEtapa.getFaturamento().getCentroFinanceiro().getCodigo();
		CentroFinanceiroTO centroFinanceiroTO =
			centroFinanceiroService.pesquisarCentroFinanceiroTOPorCodigoComCodigoUnidade(codigoCentroFinanceiro);
		faturamentoEtapaTO.getFaturamento().setCentroFinanceiro(centroFinanceiroTO);

		return faturamentoEtapaTO;
	}

	@Override
	public FaturamentoEtapaTO obterFaturamentoEtapaParaNotaFiscal(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo,
					qFaturamentoEtapa.faturamento.situacao,
					qFaturamentoEtapa.faturamento.tipo,
					qFaturamentoEtapa.faturamento.periodo,
					qFaturamentoEtapa.faturamento.identificacao,
					Projections.fields(Pessoa.class,
						qFaturamentoEtapa.faturamento.cliente.codigo,
						qFaturamentoEtapa.faturamento.cliente.nome
					).as(qFaturamentoEtapa.faturamento.cliente),
					Projections.fields(CentroFinanceiro.class,
						QCentroFinanceiro.centroFinanceiro.codigo,
						QCentroFinanceiro.centroFinanceiro.identificador,
						QCentroFinanceiro.centroFinanceiro.nome,
						Projections.fields(Contrato.class,
							QContrato.contrato.codigo,
							Projections.fields(Unidade.class,
								QUnidade.unidade.codigo,
								QUnidade.unidade.codnome,
								QUnidade.unidade.tipoUnidade
							).as(QUnidade.unidade)
						).as(QContrato.contrato)
					).as(QCentroFinanceiro.centroFinanceiro)
				).as(qFaturamentoEtapa.faturamento)
			)
		)
			.leftJoin(qFaturamentoEtapa.faturamento.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(QCentroFinanceiro.centroFinanceiro.contrato, QContrato.contrato)
			.leftJoin(qFaturamentoEtapa.faturamento.cliente, QPessoa.pessoa)
			.leftJoin(QContrato.contrato.unidade, QUnidade.unidade);
		jpaQuery.where(qFaturamentoEtapa.codigo.eq(codigo));
		return map((FaturamentoEtapa) jpaQuery.fetchOne());
	}

	@Override
	public FaturamentoEtapaTO obterFaturamentoEtapaParaNotaFiscalSemFaturamento(Long codigo) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo
				).as("faturamento")
			)
		);
		jpaQuery.where(qFaturamentoEtapa.codigo.eq(codigo));
		return map((FaturamentoEtapa) jpaQuery.fetchOne());
	}

	@Override
	public List<FaturamentoEtapaTO> obterFaturamentoEtapaPorNotaSaida(Long codigoNotaSaida) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo,
					qFaturamentoEtapa.faturamento.situacao,
					qFaturamentoEtapa.faturamento.tipo,
					qFaturamentoEtapa.faturamento.periodo,
					qFaturamentoEtapa.faturamento.identificacao,
					Projections.fields(PessoaJuridica.class,
						qFaturamentoEtapa.faturamento.cliente.codigo,
						qFaturamentoEtapa.faturamento.cliente.nome)
						.as("cliente")
				).as("faturamento")
			)
		);
		jpaQuery.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida));
		return map(jpaQuery.fetch());
	}

	@Override
	public FaturamentoEtapaTO obterFaturamentoEtapaPorNotaSaida2(Long codigoNotaSaida) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo,
					Projections.fields(PessoaJuridica.class,
						qFaturamentoEtapa.faturamento.cliente.codigo,
						qFaturamentoEtapa.faturamento.cliente.nome,
						qFaturamentoEtapa.faturamento.cliente.documento,
						qFaturamentoEtapa.faturamento.cliente.tipoPessoa)
						.as("cliente"),
					Projections.fields(CentroFinanceiro.class,
						qFaturamentoEtapa.faturamento.centroFinanceiro.codigo,
						qFaturamentoEtapa.faturamento.centroFinanceiro.nome,
						qFaturamentoEtapa.faturamento.centroFinanceiro.identificadorFinal,
						qFaturamentoEtapa.faturamento.centroFinanceiro.situacao
					).as("centroFinanceiro")
				).as("faturamento")
			)
		);

		jpaQuery.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida)).orderBy(qFaturamentoEtapa.codigo.desc());

		FaturamentoEtapa faturamentoEtapa = (FaturamentoEtapa) jpaQuery.fetchFirst();

		if (faturamentoEtapa != null) {
			return getMapper().map(faturamentoEtapa, FaturamentoEtapaTO.class);
		}

		return null;
	}

	@Override
	public FaturamentoEtapaTO obterFaturamentoEtapaTelaNotaFiscalComCodigoFaturamento(Long codigoNotaSaida) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				qFaturamentoEtapa.ultAlteracaoValor,
				qFaturamentoEtapa.dataUltAlteracaoValor,
				qFaturamentoEtapa.codigoUltAlteracaoValor,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo
				).as("faturamento")
			)
		);
		jpaQuery.innerJoin(qFaturamentoEtapa.faturamento, QFaturamento.faturamento);
		jpaQuery.innerJoin(qFaturamentoEtapa.notaSaida, QNotaSaida.notaSaida);
		jpaQuery.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida));

		FaturamentoEtapa faturamentoEtapa = (FaturamentoEtapa) jpaQuery.fetchOne();

		if (faturamentoEtapa == null) {
			return new FaturamentoEtapaTO();
		}

		FaturamentoEtapaTO faturamentoEtapaTO = map(faturamentoEtapa);

		if (faturamentoEtapa.getCodigoUltAlteracaoValor() != null) {
			PessoaFisica pessoaFisica = pessoaFisicaRepository.findByCodigo(faturamentoEtapa.getCodigoUltAlteracaoValor());
			faturamentoEtapaTO.setNomeResponsavelUltAlteracao(pessoaFisica.getNome());
		}

		return faturamentoEtapaTO;
	}

	@Override
	public JPAQuery<?> obterFaturamentoEtapaSimplesListaCodigoNotaSaida(Long codigoNotaSaida) {
		return select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo,
					qFaturamentoEtapa.faturamento.issRetidoCliente,
					qFaturamentoEtapa.faturamento.percentualServico,
					qFaturamentoEtapa.faturamento.tipo,
					Projections.fields(Pessoa.class,
						QPessoa.pessoa.codigo,
						QPessoa.pessoa.nome,
						QPessoa.pessoa.documento,
						QPessoa.pessoa.tipoPessoa
					).as("cliente"),
					Projections.fields(CentroFinanceiro.class,
						QCentroFinanceiro.centroFinanceiro.codigo,
						QCentroFinanceiro.centroFinanceiro.nome,
						QCentroFinanceiro.centroFinanceiro.identificadorFinal,
						Projections.fields(Contrato.class,
							QContrato.contrato.codigo
						).as(QContrato.contrato),
						Projections.fields(Unidade.class,
							QUnidade.unidade.codigo
						).as(QUnidade.unidade)
					).as(QCentroFinanceiro.centroFinanceiro),
					Projections.fields(Cidade.class,
						qFaturamentoEtapa.faturamento.cidadePrestacaoServico.codigo,
						qFaturamentoEtapa.faturamento.cidadePrestacaoServico.nome,
						qFaturamentoEtapa.faturamento.cidadePrestacaoServico.uf
					).as(qFaturamentoEtapa.faturamento.cidadePrestacaoServico)
				).as(qFaturamentoEtapa.faturamento)
			)
		)
			.leftJoin(qFaturamentoEtapa.faturamento.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(QCentroFinanceiro.centroFinanceiro.contrato, QContrato.contrato)
			.leftJoin(QCentroFinanceiro.centroFinanceiro.unidade, QUnidade.unidade)
			.leftJoin(qFaturamentoEtapa.faturamento.cidadePrestacaoServico, QCidade.cidade)
			.leftJoin(qFaturamentoEtapa.faturamento.cliente, QPessoa.pessoa)
			.where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida));
	}

	@Override
	public List<FaturamentoEtapaTO> obterFaturamentoEtapaPorCodFaturamento(Long codigoFaturamento) {
		List<FaturamentoEtapa> lista = obterEtapasDeUmFaturamento(codigoFaturamento);
		List<FaturamentoEtapaTO> listaTO = new ArrayList<>();

		for (FaturamentoEtapa etapa : lista) {
			listaTO.add(getMapper().map(etapa, FaturamentoEtapaTO.class));
		}

		return listaTO;
	}

	@Override
	public JPAQuery<FaturamentoEtapa> jpaQueryFaturamentoEtapa() {
		return select();
	}

	@Override
	public JPAQuery<?> obterFaturamentoEtapaMinimoListaCodigoNotaSaida(Long codigoNotaSaida) {
		return select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.descricao
			)
		).where(qFaturamentoEtapa.notaSaida.codigo.eq(codigoNotaSaida));
	}

	@Override
	public FaturamentoEtapaTO obterProximoNumeroEtapaByFatCod(Long codigoFaturamento) {
		Long ultimoNumero = faturamentoEtapaRepository.findUltimoNumeroEtapaByFatCod(codigoFaturamento);

		FaturamentoEtapaTO to = new FaturamentoEtapaTO();

		if (ultimoNumero != null) {
			to.setNumero(String.valueOf(ultimoNumero + 1));
		} else {
			to.setNumero("1");
		}

		return to;
	}

	@Override
	@Transactional
	public void updateSituacaoAFaturar(Long codigo) {
		updateSituacao(codigo, FaturamentoEtapa.Status.A_FATURAR);
	}

	@Override
	@Transactional
	public void updateSituacaoFaturada(Long codigo) {
		updateSituacao(codigo, FaturamentoEtapa.Status.FATURADA);
	}

	@Override
	@Transactional
	public void updateSituacaoRecebida(Long codigo) {
		updateSituacao(codigo, FaturamentoEtapa.Status.RECEBIDA);
	}

	@Override
	@Transactional
	public void updateSituacaoCancelada(Long codigo) {
		updateSituacao(codigo, FaturamentoEtapa.Status.CANCELADA);
	}

	@Transactional
	private void updateSituacao(Long codigo, FaturamentoEtapa.Status status) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.situacao, status).execute();
	}

	@Transactional
	@Override
	public void updateNotaSaida(Long codigo, Long codigoNotaSaida) {
		NotaSaida notaSaida = null;
		if (codigoNotaSaida != null) {
			notaSaida = new NotaSaida();
			notaSaida.setCodigo(codigoNotaSaida);
		}
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.notaSaida, notaSaida).execute();
	}

	@Override
	@Transactional
	public void updateDataAtualizacao(Long codigo, Date date) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.dataAtualizacao, date).execute();
	}

	@Override
	@Transactional
	public void updateAtualizador(Long codigo, String username) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.atualizador, username).execute();
	}

	@Override
	@Transactional
	public void updateUltimoValor(Long codigo, BigDecimal valor) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.ultAlteracaoValor, valor).execute();
	}

	@Override
	@Transactional
	public void updateValor(Long codigo, BigDecimal valor) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.valor, valor).execute();
	}

	@Override
	@Transactional
	public void updateDataUltAlteracaoValor(Long codigo, Date dtAlteracao) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.dataUltAlteracaoValor, dtAlteracao).execute();
	}

	@Override
	@Transactional
	public void updateCodigoAlteradorValor(Long codigo, Long userCod) {
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.codigoUltAlteracaoValor, userCod).execute();
	}

	@Override
	public FaturamentoEtapaCheckDescricaoTO verificarDescricaoEtapaDeFaturamento(FaturamentoEtapaCheckDescricaoTO faturamentoEtapaCheckDescricaoTO) {
		List<FaturamentoEtapa> faturamentoEtapaLista = select().where(
			QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(faturamentoEtapaCheckDescricaoTO.getCodigoFaturamento())
				.and(QFaturamentoEtapa.faturamentoEtapa.descricao.trim().equalsIgnoreCase(faturamentoEtapaCheckDescricaoTO.getDescricao().trim()))
				.and(QFaturamentoEtapa.faturamentoEtapa.codigo.eq(faturamentoEtapaCheckDescricaoTO.getCodigoEtapa()).not())
		).fetch();
		if (faturamentoEtapaLista.size() > 0) {
			faturamentoEtapaCheckDescricaoTO.setDescriptionExists(Boolean.TRUE);
			faturamentoEtapaCheckDescricaoTO.setNumeroEtapa(faturamentoEtapaLista.get(0).getNumero());
		} else {
			FaturamentoEtapa resultado = verificarDescricaoSeguimento(faturamentoEtapaCheckDescricaoTO);
			if (resultado != null) {
				faturamentoEtapaCheckDescricaoTO.setDescriptionExists(Boolean.TRUE);
				faturamentoEtapaCheckDescricaoTO.setNumeroEtapa(resultado.getNumero());
			}
		}

		return faturamentoEtapaCheckDescricaoTO;
	}

	/**
	 * Verificar se já existe alguma etapa com um seguimento da descrição com o padrão "XXXX - YYYY - WWWW".
	 * Se tiver, retorna o primeiro encontrado. Se não achar, retorna nulo.
	 * @param faturamentoEtapaCheckDescricaoTO
	 * @return
	 */
	private FaturamentoEtapa verificarDescricaoSeguimento(FaturamentoEtapaCheckDescricaoTO faturamentoEtapaCheckDescricaoTO) {
		FaturamentoEtapa resultado = null;

		String descricaoEtapa = faturamentoEtapaCheckDescricaoTO.getDescricao().trim();

		String[] listaDescricao = descricaoEtapa.split("-");

		if (listaDescricao != null && listaDescricao.length > 0) {

			Set<String> conj = new HashSet<>();

			for (String segDescr : Arrays.asList(listaDescricao)) {
				conj.add(segDescr.trim());
			}

			List<FaturamentoEtapa> faturamentoEtapaLista = select().where(
				QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(faturamentoEtapaCheckDescricaoTO.getCodigoFaturamento())
					.and(QFaturamentoEtapa.faturamentoEtapa.codigo.eq(faturamentoEtapaCheckDescricaoTO.getCodigoEtapa()).not())
			).fetch();

			if (faturamentoEtapaLista != null && faturamentoEtapaLista.size() > 0) {

				for (FaturamentoEtapa faturamentoEtapa : faturamentoEtapaLista) {

					String[] arrayDescricao = faturamentoEtapa.getDescricao().split("-");

					if (arrayDescricao != null && arrayDescricao.length > 0) {

						for (String item : Arrays.asList(arrayDescricao)) {
							if (conj.contains(item.trim()) && resultado == null) {
								resultado = faturamentoEtapa;
							}
						}
					}
				}
			}
		}

		return resultado;
	}

	@Override
	public JPAQuery<?> montarNotaFiscalFaturamento(FaturamentoTO exampleObj, PageRequest page) {
		Long codigoFaturamento = exampleObj.getCodigo();
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QFaturamento qFaturamento = QFaturamento.faturamento;
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

		JPAQuery<NotaSaida> jpaQuery =
			select(
				Projections.fields(NotaSaida.class,
					qNotaSaida.codigo,
					qNotaSaida.numero,
					qNotaSaida.status,
					qNotaSaida.dataEmissao,
					qNotaSaida.valor,
					qNotaSaida.issRetidoCliente,
					qNotaSaida.descricao
				)
			).where(
				qFaturamento.codigo.eq(codigoFaturamento)
			)
				.orderBy(
					qFaturamentoEtapa.numero.desc(),
					qNotaSaida.numero.asc()
				);

		jpaQuery.offset(page.getPageNumber() * page.getPageSize()).limit(page.getPageSize());

		return jpaQuery;
	}

	@Override
	public JPAQuery<?> montarNotaDebitoFaturamento(FaturamentoTO exampleObj, PageRequest page) {
		Long codigoFaturamento = exampleObj.getCodigo();
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QFaturamento qFaturamento = QFaturamento.faturamento;
		QNotaDebito qNotaDebito = QNotaDebito.notaDebito;

		JPAQuery<NotaDebito> jpaQuery =
			select(
				Projections.fields(NotaDebito.class,
					qNotaDebito.codigo,
					qNotaDebito.numero,
					qNotaDebito.situacao,
					qNotaDebito.dataEmissao,
					qNotaDebito.valorBruto,
					qNotaDebito.valorLiquidoPrevisto,
					qNotaDebito.valorLiquidoRecebido,
					qNotaDebito.valorDescontoPrevisto,
					qNotaDebito.valorDevolucaoPrevisto,
					qNotaDebito.descricao,
					qFaturamentoEtapa.numero.as("etapaNumero"),
					qFaturamentoEtapa.descricao.as("etapaDescricao")
				)
			)
			.where(
				qFaturamento.codigo.eq(codigoFaturamento)
			)
			.orderBy(
				qFaturamentoEtapa.numero.desc(),
				qNotaDebito.numero.asc()
			);

		jpaQuery.offset(page.getPageNumber() * page.getPageSize()).limit(page.getPageSize());

		return jpaQuery;
	}

	@Override
	public ValueTO etapaAFaturarCheck(Long codigoFaturamento) {
		Long count = select()
			.where(qFaturamentoEtapa.situacao.eq(FaturamentoEtapa.Status.A_FATURAR).and(qFaturamentoEtapa.faturamento.codigo.eq(codigoFaturamento)))
			.fetchCount();
		return new ValueTO(count > 0);
	}

	@Override
	public List<FaturamentoEtapaTO> obterFaturamentoEtapaPorNotaDebito(Long codigoNotaDebito) {
		JPAQuery jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				Projections.fields(Faturamento.class,
					qFaturamentoEtapa.faturamento.codigo,
					qFaturamentoEtapa.faturamento.situacao,
					qFaturamentoEtapa.faturamento.tipo,
					qFaturamentoEtapa.faturamento.periodo,
					qFaturamentoEtapa.faturamento.identificacao,
					Projections.fields(PessoaJuridica.class,
						qFaturamentoEtapa.faturamento.cliente.codigo,
						qFaturamentoEtapa.faturamento.cliente.nome)
						.as("cliente")
				).as("faturamento")
			)
		);
		jpaQuery.where(qFaturamentoEtapa.notaDebito.codigo.eq(codigoNotaDebito));
		return map(jpaQuery.fetch());
	}

	@Transactional
	@Override
	public void updateNotaDebito(Long codigo, Object o) {
		NotaDebito notaDebito = null;
		if (o != null) {
			notaDebito = new NotaDebito();
			notaDebito.setCodigo(codigo);
		}
		selectUpdate().where(qFaturamentoEtapa.codigo.eq(codigo)).set(qFaturamentoEtapa.notaDebito, notaDebito).execute();
	}

	@Override
	public FaturamentoEtapaTO getFaturamentoEtapaByCodigo(Long codigo) {
		QFaturamento qFaturamento = QFaturamento.faturamento;

		JPAQuery<FaturamentoEtapa> jpaQuery = select();
		jpaQuery.select(
			Projections.fields(FaturamentoEtapa.class,
				qFaturamentoEtapa.codigo,
				qFaturamentoEtapa.numero,
				qFaturamentoEtapa.situacao,
				qFaturamentoEtapa.inicio,
				qFaturamentoEtapa.termino,
				qFaturamentoEtapa.descricao,
				qFaturamentoEtapa.valor,
				qFaturamentoEtapa.valorPermuta,
				qFaturamentoEtapa.valorMulta,
				qFaturamentoEtapa.valorJuros,
				qFaturamentoEtapa.valorDesconto,
				qFaturamentoEtapa.discriminacaoFinal,
				qFaturamentoEtapa.discriminacaoCentral,
				qFaturamentoEtapa.discriminacaoInicial,
				qFaturamentoEtapa.criador,
				qFaturamentoEtapa.dataCriacao,
				qFaturamentoEtapa.atualizador,
				qFaturamentoEtapa.dataAtualizacao,
				Projections.fields(Faturamento.class,
					qFaturamento.codigo,
					qFaturamento.situacao,
					qFaturamento.tipo,
					qFaturamento.periodo,
					qFaturamento.identificacao,
					Projections.fields(PessoaJuridica.class,
						qFaturamento.cliente.codigo,
						qFaturamento.cliente.nome)
						.as("cliente")
				).as("faturamento")
			)
		)
		.join(qFaturamentoEtapa.faturamento, qFaturamento)
		.where(qFaturamentoEtapa.codigo.eq(codigo));


		FaturamentoEtapaTO transferObj = map(jpaQuery.fetchOne());
		if (transferObj != null && transferObj.getCriador() != null) {
			transferObj.setNomeCriador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getCriador()));
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			transferObj.setNomeAtualizador(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getAtualizador()));
		}

		if (transferObj != null && transferObj.getFaturamento() != null && transferObj.getFaturamento().getCriador() != null) {
			transferObj.setNomeCriadorFaturamento(pessoaFisicaService.nomeUsuarioPorLogin(transferObj.getFaturamento().getCriador()));
		}

		if (transferObj != null && transferObj.getNotaSaida() != null && transferObj.getNotaSaida().getCodigo() != null) {
			transferObj.setNotaSaida(notaSaidaService.obterNotaSaidaApenasCodigoENumero(transferObj.getNotaSaida().getCodigo()));
		}

		return transferObj;
	}
}
