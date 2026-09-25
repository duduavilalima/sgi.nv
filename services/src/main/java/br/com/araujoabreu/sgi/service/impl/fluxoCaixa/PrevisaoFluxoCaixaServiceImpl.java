package br.com.araujoabreu.sgi.service.impl.fluxoCaixa;

import br.com.araujoabreu.sgi.fluxoCaixa.domain.PrevisaoFluxoCaixa;
import br.com.araujoabreu.sgi.fluxoCaixa.domain.QPrevisaoFluxoCaixa;
import br.com.araujoabreu.sgi.service.fluxoCaixa.PrevisaoFluxoCaixaService;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.PrevisaoFluxoCaixaPesquisaTO;
import br.com.araujoabreu.sgi.service.fluxoCaixa.transfer.PrevisaoFluxoCaixaTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.fluxoCaixa.PrevisaoFluxoCaixaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PrevisaoFluxoCaixaServiceImpl extends BaseSearchService<PrevisaoFluxoCaixa, PrevisaoFluxoCaixaTO, PrevisaoFluxoCaixaPesquisaTO, PrevisaoFluxoCaixaRepository> implements PrevisaoFluxoCaixaService {

	private static final QPrevisaoFluxoCaixa qPrevisao = QPrevisaoFluxoCaixa.previsaoFluxoCaixa;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	public PrevisaoFluxoCaixaServiceImpl(PrevisaoFluxoCaixaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, PrevisaoFluxoCaixaPesquisaTO filtroTO) {
		PrevisaoFluxoCaixa previsaoFluxoCaixa = getMapper().map(filtroTO, PrevisaoFluxoCaixa.class);

		if (Check.isAssigned(filtroTO.getUnidade())) {
			builder.and(qPrevisao.unidade.codigo.eq(filtroTO.getUnidade().getCodigo()));
		}

		if (Check.isAssigned(filtroTO.getDiretoria())) {
			builder.and(qPrevisao.diretoria.codigo.eq(filtroTO.getDiretoria().getCodigo()));
		}

		if (filtroTO.getDespesaFC() != null) {
			builder.and(qPrevisao.despesaFluxoCaixa.descricao.upper().contains(filtroTO.getDespesaFC().toUpperCase().trim()));
		}

		if (Check.isAssigned(filtroTO.getData())) {
			builder.and(qPrevisao.data.eq(filtroTO.getData()));
		}

		if (Check.isAssigned(filtroTO.getValor())) {
			builder.and(qPrevisao.valor.eq(BigDecimal.valueOf(filtroTO.getValor())));
		}
	}

	@Override
	protected void doAfterLoad(PrevisaoFluxoCaixaTO transferObj) {
		if (transferObj != null && transferObj.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getCriador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeCriador(list.get(0).getNome());
			}
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getAtualizador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeAtualizador(list.get(0).getNome());
			}
		}
	}

	public void prepareValidateToSave(PrevisaoFluxoCaixaTO previsaoFluxoCaixaTO) throws ServiceException {
		PrevisaoFluxoCaixa previsaoFluxoCaixa = getRepository().findPrevisaoFluxoCaixa(previsaoFluxoCaixaTO.getUnidade().getCodigo(),
			previsaoFluxoCaixaTO.getDiretoria().getCodigo(), previsaoFluxoCaixaTO.getDespesaFluxoCaixa().getCodigo(), previsaoFluxoCaixaTO.getData());

		if (previsaoFluxoCaixa != null && !previsaoFluxoCaixa.getCodigo().equals(previsaoFluxoCaixaTO.getCodigo())) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Previsão já cadastrada");
		}
		if (previsaoFluxoCaixaTO.getUnidade() == null) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Empresa é um campo obrigatório");
		}
		if (previsaoFluxoCaixaTO.getDiretoria() == null) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Diretoria é um campo obrigatório");
		}
		if (previsaoFluxoCaixaTO.getDespesaFluxoCaixa() == null) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Despesa é um campo obrigatório");
		}
		if (previsaoFluxoCaixaTO.getValor() == null) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Valor é um campo obrigatório");
		}
		if (previsaoFluxoCaixaTO.getData() == null) {
			throw new ValidationException("PrevisaoFluxoCaixaServiceImpl", "Data é um campo obrigatório");
		}
	}

	@Override
	protected void doBeforeSave(PrevisaoFluxoCaixa entity, PrevisaoFluxoCaixaTO previsaoFluxoCaixaTO) throws ServiceException {
		prepareValidateToSave(previsaoFluxoCaixaTO);
	}

	@Override
	protected void doBeforeUpdate(PrevisaoFluxoCaixa entity, PrevisaoFluxoCaixaTO previsaoFluxoCaixaTO) throws ServiceException {
		prepareValidateToSave(previsaoFluxoCaixaTO);
	}

	@Override
	public void remover(@PathVariable Long id) {
		PrevisaoFluxoCaixa previsaoFluxoCaixa = getRepository().findById(id).orElse(null);
		getRepository().delete(previsaoFluxoCaixa);
	}
}
