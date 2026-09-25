package br.com.araujoabreu.sgi.service.impl.cidade;

import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.service.cidade.CidadeService;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadePesquisaTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.sgi.repository.cidade.CidadeRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
public class CidadeServiceImpl extends BaseSearchService<Cidade, CidadeTO, CidadePesquisaTO, CidadeRepository> implements CidadeService {

	private static final QCidade query = QCidade.cidade;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	public CidadeServiceImpl(CidadeRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CidadePesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(query.cidade.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(QCidade.cidade.nome.eq(exampleObj.getNome()));
		}

		if (Check.isAssigned(exampleObj.getUf())) {
			builder.and(QCidade.cidade.uf.eq(exampleObj.getUf()));
		}
	}

	@Override
	public List<CidadeTO> buscarCidadesByNome(String nomeCidade) {
		try {
			String nomeSemAcento = removerAcentos(nomeCidade);
			List<Cidade> cidades;
			cidades = getRepository().findCidadesByNome(nomeCidade, nomeSemAcento);

			return map(cidades);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String removerAcentos(String nomeCidade) {
		nomeCidade = Normalizer.normalize(nomeCidade, Normalizer.Form.NFD);
		nomeCidade = nomeCidade.replaceAll("[^\\p{ASCII}]", "");
		return nomeCidade;
	}

	@Override
	public @ResponseBody
	List<CidadeTO> buscarCidades(String nome) {
		String nomeUp = nome.toUpperCase().trim();

		JPAQuery<Cidade> jpaQuery = select();
		jpaQuery.from(query)
			.where(query.nome.upper().contains(nomeUp))
			.orderBy(query.nome.asc());

		return map(jpaQuery.fetch());
	}

	@Override
	public List<CidadeTO> getCidade(AutocompleteTO autocompleteTO) {
		return (buscarCidades(autocompleteTO.getNome()));
	}

	@Override
	public CidadeTO obterCidadeSimpleTelaNotaFiscal(Long codigoCidade) {

		JPAQuery<Cidade> jpaQuery = select();
		jpaQuery.select(
			Projections.fields(Cidade.class,
				query.codigo,
				query.uf,
				query.nome)
		);
		jpaQuery.where(query.codigo.eq(codigoCidade));
		return map((Cidade) jpaQuery.fetchOne());

	}

	@Override
	public List<String> buscarUFs() {

		JPAQuery<Cidade> jpaQuery = select();

		jpaQuery.select(
			Projections.fields(Cidade.class,
				query.uf)
		);

		jpaQuery.groupBy(query.uf);
		jpaQuery.orderBy(query.uf.asc());

		List<String> listaUf = new ArrayList<>();
		List<Cidade> listaCidade = new ArrayList<>();

		listaCidade = jpaQuery.fetch();

		for (Cidade cidade : listaCidade) {
			listaUf.add(cidade.getUf());
		}

		return listaUf;
	}

	@Override
	public List<CidadeTO> getCidadesISS(String uf) {
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;

		JPAQuery<Cidade> jpaQuery = select();

		jpaQuery.select(
			Projections.fields(Cidade.class,
				query.codigo,
				query.nome,
				query.uf
			)
		)
		.from(qCategoriaTributo, qTributo)
		.where(
			qCategoriaTributo.tributo.eq(qTributo)
			.and(qTributo.nome.eq("ISS"))
			.and(qCategoriaTributo.cidade.eq(query))
			.and(query.uf.eq(uf))
		)
		.orderBy(query.nome.asc());

		return map(jpaQuery.distinct().fetch());
	}
}
