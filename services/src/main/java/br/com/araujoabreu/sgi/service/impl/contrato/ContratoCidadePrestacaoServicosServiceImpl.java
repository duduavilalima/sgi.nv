package br.com.araujoabreu.sgi.service.impl.contrato;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.ContratoCidadePrestacaoServicos;
import br.com.araujoabreu.sgi.administracao.domain.QContrato;
import br.com.araujoabreu.sgi.administracao.domain.QContratoCidadePrestacaoServicos;
import br.com.araujoabreu.sgi.service.contrato.ContratoCidadePrestacaoServicosService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoCidadePrestacaoServicosTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.sgi.repository.contrato.ContratoCidadePrestacaoServicosRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ContratoCidadePrestacaoServicosServiceImpl extends BaseSearchService<ContratoCidadePrestacaoServicos, ContratoCidadePrestacaoServicosTO, ContratoCidadePrestacaoServicosPesquisaTO, ContratoCidadePrestacaoServicosRepository> implements ContratoCidadePrestacaoServicosService {
	private static final QContratoCidadePrestacaoServicos query = QContratoCidadePrestacaoServicos.contratoCidadePrestacaoServicos;

	@Autowired
	public ContratoCidadePrestacaoServicosServiceImpl(ContratoCidadePrestacaoServicosRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ContratoCidadePrestacaoServicosPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(query.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getCodigoCidade())) {
			builder.and(query.cidade.codigo.eq(exampleObj.getCodigoCidade()));
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			builder.and(query.contrato.codigo.eq(exampleObj.getCodigoContrato()));
		}
	}

	@Override
	protected void doBeforeSave(ContratoCidadePrestacaoServicos entity, ContratoCidadePrestacaoServicosTO contratoCidadePrestacaoServicosTO) throws ServiceException {
		ContratoCidadePrestacaoServicos contratoCidadePrestacaoServicos = getRepository().findByContratoCodigoAndCidadeCodigo(contratoCidadePrestacaoServicosTO.getContrato().getCodigo(), contratoCidadePrestacaoServicosTO.getCidade().getCodigo());

		if (contratoCidadePrestacaoServicos != null) {
			throw new ValidationException("ValidationException","Cidade já cadastrada para este CL.");
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<ContratoCidadePrestacaoServicos> jpaQuery, ContratoCidadePrestacaoServicosPesquisaTO exampleObj) {
		QContratoCidadePrestacaoServicos qContratoCidadePrestacaoServicos = QContratoCidadePrestacaoServicos.contratoCidadePrestacaoServicos;
		jpaQuery = select(
			Projections.fields(ContratoCidadePrestacaoServicos.class,
				qContratoCidadePrestacaoServicos.codigo,
				Projections.fields(Contrato.class,
					QContrato.contrato.codigo).as("contrato"),
				Projections.fields(Cidade.class,
					QCidade.cidade.codigo,
					QCidade.cidade.nome,
					QCidade.cidade.uf,
					QCidade.cidade).as("cidade")
			)
		);
		jpaQuery
			.from(qContratoCidadePrestacaoServicos)
			.innerJoin(qContratoCidadePrestacaoServicos.contrato, QContrato.contrato)
			.innerJoin(qContratoCidadePrestacaoServicos.cidade, QCidade.cidade)
			.orderBy(
				QCidade.cidade.uf.asc(),
				QCidade.cidade.nome.asc(),
				QContrato.contrato.codigo.asc()
			);
	}

	@Override
	public Iterable<ContratoCidadePrestacaoServicosPesquisaTO> buscarTodosPorExemplo(ContratoCidadePrestacaoServicosPesquisaTO parametros) {
		return search(parametros, PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "codigoCidade")));
	}
}
