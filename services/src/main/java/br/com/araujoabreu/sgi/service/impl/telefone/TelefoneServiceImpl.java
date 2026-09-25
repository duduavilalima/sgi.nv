package br.com.araujoabreu.sgi.service.impl.telefone;

import br.com.araujoabreu.sgi.service.telefone.TelefoneService;
import br.com.araujoabreu.sgi.service.telefone.transfer.OperadoraTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefonePesquisaTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Operadora;
import br.com.fasolti.corporativo.pessoa.domain.QOperadora;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QTelefone;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.sgi.repository.telefone.OperadoraRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelefoneServiceImpl extends BaseSearchService<Telefone, TelefoneTO, TelefonePesquisaTO, TelefoneRepository> implements TelefoneService {
	private static final QTelefone qTelefone = QTelefone.telefone;

	@Autowired
	private OperadoraRepository operadoraRepository;

	@Autowired
	public TelefoneServiceImpl(TelefoneRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, TelefonePesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getPessoa()) && Check.isAssigned(exampleObj.getPessoa().getCodigo())) {
			builder.and(qTelefone.pessoa.codigo.eq(exampleObj.getPessoa().getCodigo()));
		}
	}

	@Override
	public List<OperadoraTO> buscarOperadora() {
		List<Operadora> operadora = (List<Operadora>) operadoraRepository.findAll();

		List<OperadoraTO> listaTO = new ArrayList<>();

		listaTO.add(null);
		if (operadora != null && !operadora.isEmpty()) {
			for (Operadora opera : operadora) {
				listaTO.add(getMapper().map(opera, OperadoraTO.class));
			}
		}
		return listaTO;
	}

	@Override
	public List<TelefoneTO> buscarTelefones(@RequestBody TelefoneTO telefoneTO) throws Exception {
		try {
			List<Telefone> lista = buscarListaTelefone(telefoneTO);
			List<TelefoneTO> listaTO = new ArrayList<>();
			lista.stream().forEach(telefone -> listaTO.add(getMapper().map(telefone, TelefoneTO.class)));
			return listaTO;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private List<Telefone> buscarListaTelefone(TelefoneTO telefoneTO) {
		QOperadora qOperadora = QOperadora.operadora;

		QBean qBean = Projections.bean(
			Telefone.class,
			qTelefone.codigo,
			qTelefone.principal,
			qTelefone.numero,
			qTelefone.tipo,
			qTelefone.ddd,
			qTelefone.ramal,
			Projections.bean(Operadora.class, qOperadora.codigo, qOperadora.nome).as("operadora")
		);

		JPAQuery<Telefone> jpaQuery = select();
		BooleanBuilder builder = filtroParaBuscarTelefone(telefoneTO);

		QPessoa qPessoa = QPessoa.pessoa;

		jpaQuery
			.select(qBean)
			.from(qTelefone).leftJoin(qTelefone.pessoa, qPessoa).leftJoin(qTelefone.operadora, qOperadora)
			.where(builder)
			.orderBy(qTelefone.codigo.asc());

		List<Telefone> lista = new ArrayList<>();
		lista = jpaQuery.fetch();

		return lista;
	}

	private BooleanBuilder filtroParaBuscarTelefone(TelefoneTO to) {
		BooleanBuilder builder = new BooleanBuilder();

		if (Check.isAssigned(to.getPessoa()) && Check.isAssigned(to.getPessoa().getCodigo())) {
			builder.and(qTelefone.pessoa.codigo.eq(to.getPessoa().getCodigo()));
		}

		if (Check.isAssigned(to.getContato()) && Check.isAssigned(to.getContato().getCodigo())) {
			builder.and(qTelefone.contato.codigo.eq(to.getContato().getCodigo()));
		}

		return builder;
	}
}
