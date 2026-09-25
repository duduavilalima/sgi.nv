package br.com.araujoabreu.sgi.service.impl.contaContabil;

import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.service.contaContabil.ContaContabilService;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutoCompleteContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.contaContabil.ContaContabilRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaContabilServiceImpl extends BaseSearchService<ContaContabil, ContaContabilTO, ContaContabilPesquisaTO, ContaContabilRepository> implements ContaContabilService {

	private static final QContaContabil qContaContabil = QContaContabil.contaContabil;

	@Autowired
	public ContaContabilServiceImpl(ContaContabilRepository repository) {
		super(repository);
	}

	@Override
	public List<ContaContabilTO> buscarContaContabilPorNomeOuNumero(String nomeOuNumero) {

		String nomeOuNumeroUp = nomeOuNumero.toUpperCase().trim();

		JPAQuery jpaQuery = select();
		jpaQuery.from(qContaContabil)
			.where(qContaContabil.descricao.upper().contains(nomeOuNumeroUp)
				.or(qContaContabil.numero.upper().contains(nomeOuNumeroUp)))
			.orderBy(qContaContabil.codigo.asc(), qContaContabil.descricao.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<ContaContabilTO> getContaContabil(AutocompleteTO autocompleteTO) {
		return buscarContaContabilPorNomeOuNumero(autocompleteTO.getNome());
	}

	private List<ContaContabilTO> buscarContaContabilPorNomeOuNumeroECodigoEmpresa(String nomeOuNumero, Long idEmpresa) {
		String nomeOuNumeroUp = nomeOuNumero.toUpperCase().trim();

		JPAQuery jpaQuery = select(Projections.fields(
			ContaContabil.class,
			qContaContabil.codigo,
			qContaContabil.numero,
			qContaContabil.descricao
		));
		jpaQuery.from(qContaContabil)
			.where(qContaContabil.empresa.codigo.eq(idEmpresa).and(
				qContaContabil.descricao.upper().contains(nomeOuNumeroUp)
					.or(qContaContabil.numero.upper().startsWith(nomeOuNumeroUp))
				)
			)
			.orderBy(qContaContabil.codigo.asc(), qContaContabil.descricao.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<ContaContabilTO> getContaContabilPorEmpresa(AutocompleteTO autocompleteTO) {
		return buscarContaContabilPorNomeOuNumeroECodigoEmpresa(autocompleteTO.getNome(), autocompleteTO.getUnidade().getCodigo());
	}

	private List<ContaContabilTO> buscarContaContabilPorNomeOuNumeroECodigoEmpresaClassificacao(String nomeOuNumero, Long idEmpresa, Integer classificacao) {
		String nomeOuNumeroUp = nomeOuNumero.toUpperCase().trim();
		ContaContabil.ClassificacaoContaContabil aux = ContaContabil.ClassificacaoContaContabil.getClassificacaoByOrdinal(classificacao);
		boolean isContaFaturamento = (aux != null && (ContaContabil.ClassificacaoContaContabil.Cliente.equals(aux) || ContaContabil.ClassificacaoContaContabil.Faturamento_receber.equals(aux)));

		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<ContaContabil> jpaQuery = select();
		jpaQuery.from(qContaContabil).join(qContaContabil.empresa, qUnidade);

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		booleanBuilder.and(qUnidade.codigo.eq(idEmpresa));
		booleanBuilder.andAnyOf(
			qContaContabil.numero.upper().contains(nomeOuNumeroUp),
			qContaContabil.descricao.upper().contains(nomeOuNumeroUp));

		if (classificacao != null && !isContaFaturamento)
			booleanBuilder.and(qContaContabil.classificacao.eq(ContaContabil.ClassificacaoContaContabil.getClassificacaoByOrdinal(classificacao)));
		else if (classificacao != null && isContaFaturamento) {
			booleanBuilder.and(
				qContaContabil.codigo.in(
					JPAExpressions
						.select(qContaContabil.codigo)
						.from(qContaContabil)
						.where(
							qContaContabil.empresa.codigo.eq(idEmpresa)
								.and(qContaContabil.pai.classificacao.eq(ContaContabil.ClassificacaoContaContabil.getClassificacaoByOrdinal(classificacao))))
				)
			);
		}

		return map(jpaQuery.where(booleanBuilder).orderBy(qContaContabil.codigo.asc(), qContaContabil.descricao.asc()).fetch());
	}

	@Override
	public List<ContaContabilTO> getContaContabilPorEmpresaClassificacao(AutoCompleteContaContabilTO autocompleteTO) {
		return buscarContaContabilPorNomeOuNumeroECodigoEmpresaClassificacao(autocompleteTO.getNome(), autocompleteTO.getUnidade().getCodigo(), autocompleteTO.getClassificacao());
	}

	private List<ContaContabilTO> buscarContaContabilPorNomeOuNumeroECodigoEmpresaTipoEspecialClassificacao(String nomeOuNumero, Long idEmpresa, Integer classificacao, Integer tipoEspecial) {
		String nomeOuNumeroUp = nomeOuNumero.toUpperCase().trim();
		QUnidade qUnidade = QUnidade.unidade;

		JPAQuery<ContaContabil> jpaQuery = select();
		jpaQuery.from(qContaContabil).join(qContaContabil.empresa, qUnidade);

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		booleanBuilder.and(qUnidade.codigo.eq(idEmpresa));
		booleanBuilder.andAnyOf(
			qContaContabil.numero.upper().contains(nomeOuNumeroUp),
			qContaContabil.descricao.upper().contains(nomeOuNumeroUp));

		if (classificacao != null)
			booleanBuilder.and(qContaContabil.classificacao.eq(ContaContabil.ClassificacaoContaContabil.getClassificacaoByOrdinal(classificacao)));
		if (tipoEspecial != null) {
			booleanBuilder.and(
				qContaContabil.pai.codigo.in(
					JPAExpressions
						.select(qContaContabil.codigo)
						.from(qContaContabil)
						.where(
							qContaContabil.empresa.codigo.eq(idEmpresa)
								.and(qContaContabil.tipoEspecial.eq(ContaContabil.TipoEspecial.getTipoEspecialByOrdinal(tipoEspecial))))
				)
			);
		}

		jpaQuery.where(booleanBuilder).orderBy(qContaContabil.codigo.asc(), qContaContabil.descricao.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<ContaContabilTO> getContaContabilPorEmpresaTipoEspecialClassificacao(AutoCompleteContaContabilTO autocompleteTO) {
		return buscarContaContabilPorNomeOuNumeroECodigoEmpresaTipoEspecialClassificacao(autocompleteTO.getNome(), autocompleteTO.getUnidade().getCodigo(), autocompleteTO.getClassificacao(), autocompleteTO.getTipoEspecial());
	}

    @Override
	public List<ContaContabilTO> getContaContabilByCodEmpresa(Long codEmpresa) {

		JPAQuery jpaQuery = select(Projections.fields(
			ContaContabil.class,
			qContaContabil.codigo,
			qContaContabil.numero,
			qContaContabil.descricao
		));
		jpaQuery.from(qContaContabil)
			.where(qContaContabil.empresa.codigo.eq(codEmpresa))
			.orderBy(qContaContabil.codigo.asc());

		return map(jpaQuery.fetch());
	}


}
