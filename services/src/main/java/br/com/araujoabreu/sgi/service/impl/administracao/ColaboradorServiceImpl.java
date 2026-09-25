package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.QColaborador;
import br.com.araujoabreu.sgi.service.administracao.ColaboradorService;
import br.com.araujoabreu.sgi.service.administracao.PessoaService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ColaboradorServiceImpl extends BaseSearchService<Colaborador, ColaboradorTO, ColaboradorPesquisaTO, ColaboradorRepository> implements ColaboradorService {

	private static final QColaborador qColaborador = QColaborador.colaborador;
	// FIXME: Arrumar alguma forma de trafegar Enums entre os pacotes.
	private static final Map<String, Pessoa.Situacao> MAPA_SITUACAO = criarMapaSituacao();
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	public ColaboradorServiceImpl(ColaboradorRepository repository) {
		super(repository);
	}

	private static Map<String, Pessoa.Situacao> criarMapaSituacao() {
		Map<String, Pessoa.Situacao> mapa = new HashMap<>();
		for (Pessoa.Situacao situacao : Pessoa.Situacao.values()) {
			mapa.put(situacao.getNome(), situacao);
		}
		return mapa;
	}

	@Override
	public List<ColaboradorTO> buscarColaboradores(@RequestBody ColaboradorTO colaboradorTO) {
		try {
			List<Colaborador> lista = buscarListaColaborador(colaboradorTO);
			return map(lista);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Busca uma lista de colaboradores, usando como critério para pesquisa os atributos do ColaboradorTO passado pelo parametro.
	 *
	 * @param colaboradorTO
	 * @return
	 */
	private List<Colaborador> buscarListaColaborador(ColaboradorTO colaboradorTO) {

		QBean qBean = Projections.bean(
			Colaborador.class,
			qColaborador.codigo,
			qColaborador.nome
		);

		JPAQuery<Colaborador> jpaQuery = select();
		BooleanBuilder builder = restricaoParaBuscarColaborador(colaboradorTO);

		jpaQuery
			.select(qBean)
			.from(qColaborador)
			.where(builder)
			.orderBy(qColaborador.nome.asc());

		List<Colaborador> listaColaborador = new ArrayList<>();
		listaColaborador = jpaQuery.fetch();

		return listaColaborador;
	}

	private BooleanBuilder restricaoParaBuscarColaborador(ColaboradorTO to) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(to.getNome())) {
			builder.and(qColaborador.nome.upper().contains(to.getNome().toUpperCase().trim()));
		}

		if (Check.isAssigned(to.getSituacao())) {
			builder.and(qColaborador.situacao.eq(MAPA_SITUACAO.get(to.getSituacao())));
		}

		return builder;
	}
}
