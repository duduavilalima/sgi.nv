package br.com.araujoabreu.sgi.service.impl.fluxoCaixa;

import br.com.araujoabreu.sgi.fluxoCaixa.domain.DespesaFluxoCaixa;
import br.com.araujoabreu.sgi.fluxoCaixa.domain.PrevisaoFluxoCaixa;
import br.com.araujoabreu.sgi.fluxoCaixa.domain.QDespesaFluxoCaixa;
import br.com.araujoabreu.sgi.service.fluxoCaixa.DespesaFluxoCaixaService;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.DespesaFluxoCaixaPesquisaTO;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.DespesaFluxoCaixaTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.fluxoCaixa.DespesaFluxoCaixaRepository;
import br.com.fasolti.sgi.repository.fluxoCaixa.PrevisaoFluxoCaixaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class DespesaFluxoCaixaServiceImpl extends BaseSearchService<DespesaFluxoCaixa, DespesaFluxoCaixaTO, DespesaFluxoCaixaPesquisaTO, DespesaFluxoCaixaRepository> implements DespesaFluxoCaixaService {

	private static final QDespesaFluxoCaixa qDespesa = QDespesaFluxoCaixa.despesaFluxoCaixa;

	@Autowired
	private PrevisaoFluxoCaixaRepository previsaoFluxoCaixaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	public DespesaFluxoCaixaServiceImpl(DespesaFluxoCaixaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, DespesaFluxoCaixaPesquisaTO filtroTO) {
		DespesaFluxoCaixa despesaFluxoCaixa = getMapper().map(filtroTO, DespesaFluxoCaixa.class);

		if (filtroTO.getSigla() != null) {
			builder.and(qDespesa.sigla.upper().contains(filtroTO.getSigla().toUpperCase().trim()));
		}

		if (filtroTO.getDescricao() != null) {
			builder.and(qDespesa.descricao.upper().contains(filtroTO.getDescricao().toUpperCase().trim()));
		}

		if (Check.isNotEmpty(filtroTO.getSituacao())) {
			builder.and(qDespesa.situacao.eq(despesaFluxoCaixa.getSituacao()));
		}
	}

	@Override
	protected void doAfterLoad(DespesaFluxoCaixaTO despesaFluxoCaixaTO) {

		if (despesaFluxoCaixaTO != null && despesaFluxoCaixaTO.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(despesaFluxoCaixaTO.getCriador());
			if (list != null && !list.isEmpty()) {
				despesaFluxoCaixaTO.setCriador(list.get(0).getNome());
			}
		}

		if (despesaFluxoCaixaTO != null && despesaFluxoCaixaTO.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(despesaFluxoCaixaTO.getAtualizador());
			if (list != null && !list.isEmpty()) {
				despesaFluxoCaixaTO.setAtualizador(list.get(0).getNome());
			}
		}
	}

	@Override
	@Transactional
	public void remover(@PathVariable Long id) {
		DespesaFluxoCaixa despesaFluxoCaixa = getRepository().findById(id).orElse(null);

		List<PrevisaoFluxoCaixa> listaPrevisaoFluxoCaixas = previsaoFluxoCaixaRepository.findByDespesaFluxoCaixaCodigo(id);

		if (listaPrevisaoFluxoCaixas != null && !listaPrevisaoFluxoCaixas.isEmpty()) {

			for (PrevisaoFluxoCaixa previsaoFluxoCaixa : listaPrevisaoFluxoCaixas) {
				previsaoFluxoCaixaRepository.delete(previsaoFluxoCaixa);
			}
		}

		getRepository().delete(despesaFluxoCaixa);
	}

	public void prepareValidateToSave(DespesaFluxoCaixaTO despesaFluxoCaixaTO) throws ServiceException {
		despesaFluxoCaixaTO.setSigla(despesaFluxoCaixaTO.getSigla().toUpperCase());
		DespesaFluxoCaixa despesaFluxoCaixa = getRepository().findBySigla(despesaFluxoCaixaTO.getSigla().trim());

		if (despesaFluxoCaixa != null && !despesaFluxoCaixa.getCodigo().equals(despesaFluxoCaixaTO.getCodigo())) {
			throw new ValidationException("ValidationException","Despesa já cadastrada");
		}

		if (despesaFluxoCaixaTO.getSigla() == null) {
			throw new ValidationException("ValidationException","Subconta é um campo obrigatório");
		}

		if (despesaFluxoCaixaTO.getDescricao() == null) {
			throw new ValidationException("ValidationException","Descrição é um campo obrigatório");
		}
	}

	@Override
	protected void doBeforeSave(DespesaFluxoCaixa entity, DespesaFluxoCaixaTO despesaFluxoCaixaTO) {
		prepareValidateToSave(despesaFluxoCaixaTO);
		entity.setSigla(entity.getSigla().toUpperCase());
	}

	@Override
	protected void doBeforeUpdate(DespesaFluxoCaixa entity, DespesaFluxoCaixaTO despesaFluxoCaixaTO) {
		prepareValidateToSave(despesaFluxoCaixaTO);
		entity.setSigla(entity.getSigla().toUpperCase());
	}

	@Override
	public List<DespesaFluxoCaixaTO> buscarDespesaFluxoCaixaPorDescricao(String descricao) {

		String descricaoUp = descricao.toUpperCase().trim();

		JPAQuery jpaQuery = select();
		jpaQuery.from(qDespesa)
			.where(qDespesa.descricao.upper().contains(descricaoUp))
			.orderBy(qDespesa.descricao.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<DespesaFluxoCaixaTO> getDespesaFluxoCaixa(@RequestBody AutocompleteTO autocompleteTO) throws Exception {
		try {
			return buscarDespesaFluxoCaixaPorDescricao(autocompleteTO.getNome());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
