package br.com.araujoabreu.sgi.service.impl.tesouraria;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.service.tesouraria.LancamentoService;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.LancamentoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.Lancamento;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QLancamento;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.tesouraria.LancamentoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LancamentoServiceImpl extends BaseSearchService<Lancamento, LancamentoTO, LancamentoPesquisaTO, LancamentoRepository> implements LancamentoService {

	private static final QLancamento qLancamento = QLancamento.lancamento;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public LancamentoServiceImpl(LancamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Lancamento> jpaQuery, LancamentoPesquisaTO exampleObj) {
		jpaQuery.from(qLancamento).orderBy(qLancamento.codigo.asc());

		if (Check.isAssigned(exampleObj.getBanco())) {
			QContaCorrente qContaContacorrente = QContaCorrente.contaCorrente;
			QBanco qBanco = QBanco.banco;

			jpaQuery.join(qLancamento.contaCorrente, qContaContacorrente)
				.join(qContaContacorrente.banco, qBanco);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, LancamentoPesquisaTO exampleObj) {
		Lancamento eLancamento = getMapper().map(exampleObj, Lancamento.class);

		if (Check.isAssigned(exampleObj.getNumero())) {
			builder.and(qLancamento.numero.upper().contains(exampleObj.getNumero().toUpperCase().trim()));
		}

		if (Check.isAssigned(exampleObj.getStatus())) {
			builder.and(qLancamento.status.eq(eLancamento.getStatus()));
		}

		if (Check.isAssigned(exampleObj.getValorDe())) {
			builder.and(qLancamento.valor.goe(exampleObj.getValorDe()));
		}

		if (Check.isAssigned(exampleObj.getValorAte())) {
			builder.and(qLancamento.valor.loe(exampleObj.getValorAte()));
		}

		if (Check.isAssigned(exampleObj.getPagamentoDe())) {
			builder.and(qLancamento.data.goe(exampleObj.getPagamentoDe()));
		}

		if (Check.isAssigned(exampleObj.getPagamentoAte())) {
			builder.and(qLancamento.data.loe(exampleObj.getPagamentoAte()));
		}

		if (Check.isAssigned(exampleObj.getUnidade())) {
			builder.and(qLancamento.unidade.codigo.eq(exampleObj.getUnidade().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getBanco())) {
			QBanco qBanco = QBanco.banco;
			builder.and(qBanco.codigo.eq(exampleObj.getBanco().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getContaCorrente()) && Check.isAssigned(exampleObj.getContaCorrente().getCodigo())) {
			builder.and(qLancamento.contaCorrente.codigo.eq(exampleObj.getContaCorrente().getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getContaContabil())
			&& exampleObj.getContaContabil().getDescricao() != null && !exampleObj.getContaContabil().getDescricao().trim().equals("")
			&& exampleObj.getContaContabil().getNumero() != null && !exampleObj.getContaContabil().getNumero().trim().equals("")) {
			builder.and(qLancamento.contaContabil.descricao.containsIgnoreCase(exampleObj.getContaContabil().getDescricao())
				.or(qLancamento.contaContabil.numero.containsIgnoreCase(exampleObj.getContaContabil().getNumero())));
		}
	}

	@Override
	protected void doAfterLoad(LancamentoTO lancamentoTO) {

		if (lancamentoTO != null && lancamentoTO.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(lancamentoTO.getCriador());
			if (list != null && !list.isEmpty()) {
				lancamentoTO.setNomeCriador(list.get(0).getNome());
			}
		}

		if (lancamentoTO != null && lancamentoTO.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(lancamentoTO.getAtualizador());
			if (list != null && !list.isEmpty()) {
				lancamentoTO.setNomeAtualizador(list.get(0).getNome());
			}
		}

		if (lancamentoTO != null && lancamentoTO.getContaCorrente() != null) {
			lancamentoTO.setBanco(lancamentoTO.getContaCorrente().getBanco());
		}

	}

	@Override
	protected void doBeforeSave(Lancamento entity, LancamentoTO lancamentoTO) throws ServiceException {
		prepareValidateToSave(lancamentoTO);
		entity.setNumero("LT/");
	}

	@Override
	protected void doAfterSave(Lancamento entity, LancamentoTO object) throws ServiceException {
		entity.setNumero("LT/" + entity.getCodigo());
		getRepository().save(entity);
	}

	@Override
	@Transactional
	public void cancelar(Long codigo) {
		Date updateDate = new Date();
		selectUpdate().where(qLancamento.codigo.eq(codigo))
			.set(qLancamento.status, Lancamento.StatusLancamento.CANCELADO)
			.set(qLancamento.dataCancelamento, updateDate)
			.set(qLancamento.atualizador, securityService.getUser().getUsername())
			.set(qLancamento.dataAtualizacao, updateDate)
			.set(qLancamento.colaboradorCancelamento, (Colaborador) securityService.getUser().getPessoaFisica())
			.execute();
	}

	public void prepareValidateToSave(LancamentoTO lancamentoTO) {

		if (lancamentoTO.getContaContabil() == null && lancamentoTO.getContaContabil().getCodigo() == null) {
			throw new ValidationException("ValidationException","É necessário selecionar uma conta contábil da lista.");
		}

	}

	@Override
	protected void doBeforeUpdate(Lancamento entity, LancamentoTO object) throws ServiceException {
		prepareValidateToUpdate(entity, object);

		super.doBeforeUpdate(entity, object);
	}

	private void prepareValidateToUpdate(Lancamento entity, LancamentoTO object) {
		if (!"Aberto".equals(object.getStatus()) || !Lancamento.StatusLancamento.ABERTO.equals(entity.getStatus())) {
			throw new ValidationException("ValidationException","Não é permitido edição par ao status atual do registro.");
		}

	}
}
