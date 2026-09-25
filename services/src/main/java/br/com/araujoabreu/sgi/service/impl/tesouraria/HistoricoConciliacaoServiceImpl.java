package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.tesouraria.HistoricoConciliacaoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoContaContabilTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.HistoricoConciliacaoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.HistoricoConciliacao;
import br.com.araujoabreu.sgi.tesouraria.domain.HistoricoConciliacaoContaContabil;
import br.com.araujoabreu.sgi.tesouraria.domain.QHistoricoConciliacao;
import br.com.araujoabreu.sgi.tesouraria.domain.QHistoricoConciliacaoContaContabil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.tesouraria.HistoricoConciliacaoContaContabilRepository;
import br.com.fasolti.sgi.repository.tesouraria.HistoricoConciliacaoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricoConciliacaoServiceImpl extends BaseSearchService<HistoricoConciliacao, HistoricoConciliacaoTO, HistoricoConciliacaoPesquisaTO, HistoricoConciliacaoRepository> implements HistoricoConciliacaoService {

	private static final QHistoricoConciliacao qHistoricoAplicacao = QHistoricoConciliacao.historicoConciliacao;

	@Autowired
	private HistoricoConciliacaoContaContabilRepository historicoConciliacaoContaContabilRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	public HistoricoConciliacaoServiceImpl(HistoricoConciliacaoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<HistoricoConciliacao> jpaQuery, HistoricoConciliacaoPesquisaTO exampleObj) {
		jpaQuery.from(qHistoricoAplicacao).orderBy(qHistoricoAplicacao.descricao.asc());
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, HistoricoConciliacaoPesquisaTO filtroTO) {
		HistoricoConciliacao historicoConciliacao = getMapper().map(filtroTO, HistoricoConciliacao.class);

		if (filtroTO.getDescricao() != null) {
			builder.and(qHistoricoAplicacao.descricao.upper().contains(filtroTO.getDescricao().toUpperCase().trim()));
		}

		if (Check.isNotEmpty(filtroTO.getSituacao())) {
			builder.and(qHistoricoAplicacao.situacao.eq(historicoConciliacao.getSituacao()));
		}

		if (Check.isNotEmpty(filtroTO.getTipoAcao())) {
			builder.and(qHistoricoAplicacao.tipoAcao.eq((HistoricoConciliacao.TipoAcao) Util.getEnumByString(HistoricoConciliacao.TipoAcao.class, filtroTO.getTipoAcao())));
		}
	}

	@Override
	protected void doAfterLoad(HistoricoConciliacaoTO historicoConciliacaoTO) {

		if (historicoConciliacaoTO != null && historicoConciliacaoTO.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(historicoConciliacaoTO.getCriador());
			if (list != null && !list.isEmpty()) {
				historicoConciliacaoTO.setCriador(list.get(0).getNome());
			}
		}

		if (historicoConciliacaoTO != null && historicoConciliacaoTO.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(historicoConciliacaoTO.getAtualizador());
			if (list != null && !list.isEmpty()) {
				historicoConciliacaoTO.setAtualizador(list.get(0).getNome());
			}
		}
	}

	@Override
	public @ResponseBody
	List<HistoricoConciliacaoContaContabilTO> buscarHistoricoCCC(@PathVariable String codigo) {
		List<HistoricoConciliacaoContaContabil> listaHistoricoCCC = historicoConciliacaoContaContabilRepository.findByHistoricoConciliacaoCodigo(new Long(codigo));

		List<HistoricoConciliacaoContaContabilTO> historicoConciliacaoContaContabilTOs = new ArrayList<>();

		if (listaHistoricoCCC != null && !listaHistoricoCCC.isEmpty()) {

			for (HistoricoConciliacaoContaContabil historicoCCC : listaHistoricoCCC) {

				historicoConciliacaoContaContabilTOs.add(getMapper().map(historicoCCC, HistoricoConciliacaoContaContabilTO.class));
			}
		}

		return historicoConciliacaoContaContabilTOs;
	}


	public void prepareValidateToSave(HistoricoConciliacaoTO historicoConciliacaoTO) throws ServiceException {

		HistoricoConciliacao historicoConciliacao = getRepository().findByDescricao(historicoConciliacaoTO.getDescricao().trim());

		if (historicoConciliacao != null && !historicoConciliacao.getCodigo().equals(historicoConciliacaoTO.getCodigo())) {
			throw new ValidationException("ValidationException","Já existe um histórico de conciliação bancária com esta descrição");
		}

		if (historicoConciliacaoTO.getDescricao() == null) {
			throw new ValidationException("ValidationException","Descrição (início) é um campo obrigatório");
		}

		if (historicoConciliacaoTO.getTipoAcao() == null) {
			throw new ValidationException("ValidationException","Tipo de ação é um campo obrigatório");
		}
	}

	@Override
	protected void doBeforeSave(HistoricoConciliacao entity, HistoricoConciliacaoTO object) throws ServiceException {
		prepareValidateToSave(object);
	}

	@Override
	protected void doBeforeUpdate(HistoricoConciliacao entity, HistoricoConciliacaoTO object) throws ServiceException {
		prepareValidateToSave(object);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		HistoricoConciliacao historicoConciliacao = getRepository().findById(id).orElse(null);

		List<HistoricoConciliacaoContaContabil> listaHistoricoCCC = historicoConciliacaoContaContabilRepository.findByHistoricoConciliacaoCodigo(id);

		if (listaHistoricoCCC != null && !listaHistoricoCCC.isEmpty()) {

			for (HistoricoConciliacaoContaContabil contaContabil : listaHistoricoCCC) {
				historicoConciliacaoContaContabilRepository.delete(contaContabil);
			}
		}

		getRepository().delete(historicoConciliacao);
	}

	@Override
	public List<ContaContabilTO> buscarContaContabil(Long codigoHistoricoConciliacao) throws Exception {
		try {
			return buscarContaContabilByCodHistorico(codigoHistoricoConciliacao);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private List<ContaContabilTO> buscarContaContabilByCodHistorico(Long codigoHistoricoConciliacao) {
		List<ContaContabilTO> listaContaContabilTO = new ArrayList<>();

		if (codigoHistoricoConciliacao != null) {

			QHistoricoConciliacaoContaContabil qHistoricoConciliacaoContaContabil = QHistoricoConciliacaoContaContabil.historicoConciliacaoContaContabil;
			QContaContabil qContaContabil = QContaContabil.contaContabil;
			QUnidade qUnidade = QUnidade.unidade;

			JPAQuery jpaQueryHistoricoCCC = select().from(qHistoricoConciliacaoContaContabil);

			QBean qBean = Projections.bean(
				HistoricoConciliacaoContaContabil.class,
				qHistoricoConciliacaoContaContabil.codigo,
				Projections.bean(
					ContaContabil.class,
					qContaContabil.codigo,
					qContaContabil.numero,
					qContaContabil.descricao,
					Projections.bean(
						Unidade.class,
						qUnidade.codigo
					).as("empresa")
				).as("contaContabil")
			);

			jpaQueryHistoricoCCC
				.select(qBean)
				.from(qHistoricoConciliacaoContaContabil)
				.innerJoin(qHistoricoConciliacaoContaContabil.contaContabil, qContaContabil)
				.innerJoin(qContaContabil.empresa, qUnidade)
				.where(new BooleanBuilder().and(qHistoricoConciliacaoContaContabil.historicoConciliacao.codigo.eq(codigoHistoricoConciliacao)));

			List<HistoricoConciliacaoContaContabil> listaHistoricoCCC = jpaQueryHistoricoCCC.fetch();

			for (HistoricoConciliacaoContaContabil histCCC : listaHistoricoCCC) {
				ContaContabil contaContabil = histCCC.getContaContabil();
				ContaContabilTO contaContabilTO = getMapper().map(contaContabil, ContaContabilTO.class);
				listaContaContabilTO.add(contaContabilTO);
			}
		}

		return listaContaContabilTO;
	}

}
