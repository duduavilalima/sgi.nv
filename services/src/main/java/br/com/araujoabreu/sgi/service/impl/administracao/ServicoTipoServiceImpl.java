package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.QReinfServico;
import br.com.araujoabreu.sgi.administracao.domain.QServicoTipo;
import br.com.araujoabreu.sgi.administracao.domain.ReinfServico;
import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.service.administracao.ServicoTipoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.ReinfServicoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.ServicoTipoTO;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.notaSaida.ServicoTipoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoTipoServiceImpl extends
	BaseSearchService<
		ServicoTipo,
		ServicoTipoTO,
		ServicoTipoPesquisaTO,
		ServicoTipoRepository> implements ServicoTipoService {

	private final QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;

	@Autowired
	public ServicoTipoServiceImpl(ServicoTipoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ServicoTipoPesquisaTO exampleObj) {
		if (exampleObj.getSigla() != null && !exampleObj.getSigla().trim().isEmpty()) {
			builder.and(qServicoTipo.sigla.like(exampleObj.getSigla() + "%"));
		}

		if (exampleObj.getNome() != null && !exampleObj.getNome().trim().isEmpty()) {
			builder.and(qServicoTipo.nome.containsIgnoreCase(exampleObj.getNome()));
		}

		if (exampleObj.getSituacao() != null && !exampleObj.getSituacao().trim().isEmpty()) {
			ServicoTipo.Situacao situacao = null;
			for (ServicoTipo.Situacao tipo : ServicoTipo.Situacao.values()) {
				if (tipo.getDescricao().equals(exampleObj.getSituacao())) {
					situacao = tipo;
					break;
				}
			}
			builder.and(qServicoTipo.situacao.eq(situacao));
		}

		if (exampleObj.getTipoFaturamento() != null) {
			ServicoTipo.TipoFaturamento tipo = ServicoTipo.getTipoFaturamentoByNome(exampleObj.getTipoFaturamento());
			if (tipo != null) {
				builder.and(qServicoTipo.tipoFaturamento.eq(tipo));
			}
		}
	}

	@Override
	protected void doBeforeSave(ServicoTipo entity, ServicoTipoTO object) {
		List<ServicoTipoTO> list = this.findBySigla(object.getSigla().trim());

		if (!list.isEmpty()) {
			throw new ValidationException("servico-tipo", String.format("O tipo de serviço com sigla \"%s\" já existe.", object.getSigla()));
		}
		entity.setServicoTipoPai(obterServicoTipoPelaSigla(object.getSigla()));
	}

	@Override
	protected void doBeforeUpdate(ServicoTipo entity, ServicoTipoTO object) {
		List<ServicoTipoTO> list = this.findBySigla(object.getSigla().trim());

		if (!list.isEmpty() && !list.get(0).getCodigo().equals(object.getCodigo())) {
			throw new ValidationException("servico-tipo", String.format("O tipo de serviço com sigla \"%s\" já existe.", object.getSigla()));
		}
		entity.setServicoTipoPai(obterServicoTipoPelaSigla(object.getSigla()));
	}

	private ServicoTipo obterServicoTipoPelaSigla(String sigla) {
		while (sigla.length() > 3) {
			sigla = sigla.substring(0, sigla.length() - 3);
			List<ServicoTipoTO> list = this.findBySigla(sigla);
			if (!list.isEmpty()) {
				ServicoTipoTO servicoTipoTO = list.get(0);
				return this.getRepository().findById(servicoTipoTO.getId()).get();
			}
		}
		return null;
	}

	@Override
	public ServicoTipoTO obterServicoTipoPelaSiglaTO(String sigla) {
		if (sigla != null) {
			while (sigla.length() > 3) {
				sigla = sigla.substring(0, sigla.length() - 3);
				List<ServicoTipoTO> list = this.findBySigla(sigla);
				if (!list.isEmpty()) {
					ServicoTipoTO servicoTipoTO = list.get(0);
					ServicoTipo servicoTipo = this.getRepository().findById(servicoTipoTO.getId()).get();
					return getMapper().map(servicoTipo, ServicoTipoTO.class);
				}
			}
		}
		return null;
	}

	@Override
	protected void doBeforeDelete(ServicoTipoTO object) throws ValidationException {
		List<ServicoTipoTO> list = this.findByCodServicoTipoPai(object.getId());

		if (list != null && !list.isEmpty()) {
			throw new ValidationException("servico-tipo", "Este tipo de serviço não pode ser removido pois existe um ou mais tipo(s) de serviço associado(s) ao mesmo");
		}
	}

	@Override
	public ReinfServicoTO buscarReinfServicoByCodServicoTipo(String codigoServicoTipo) {
		JPAQuery jpaQuery = select();
		QReinfServico qReinfServico = QReinfServico.reinfServico;

		jpaQuery.select(
			Projections.fields(ServicoTipo.class,
				Projections.bean(ReinfServico.class,
					qReinfServico.codigo,
					qReinfServico.codigoClassificacao
				).as(qServicoTipo.reinfServico)
			)
		).innerJoin(qServicoTipo.reinfServico, qReinfServico);

		jpaQuery.where(qServicoTipo.codigo.eq(Long.parseLong(codigoServicoTipo)));

		ServicoTipo servicoTipo = (ServicoTipo) jpaQuery.fetchOne();

		if (servicoTipo != null) {
			ReinfServico reinfServico = servicoTipo.getReinfServico();
			return getMapper().map(reinfServico, ReinfServicoTO.class);

		} else {
			return null;
		}
	}

	@Override
	public List<ServicoTipoTO> findByCodServicoTipoPai(Long id) {
		List<ServicoTipo> lista = select(
			Projections.fields(
				ServicoTipo.class,
				qServicoTipo.codigo
			)
		).where(
			qServicoTipo.servicoTipoPai.codigo.eq(id)
		).fetch();

		List<ServicoTipoTO> listaTO = new ArrayList<>();

		for (ServicoTipo servicoTipo : lista) {
			ServicoTipoTO to = getMapper().map(servicoTipo, ServicoTipoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public List<ServicoTipoTO> findBySigla(String sigla) {
		List<ServicoTipo> lista = select(
			Projections.fields(
				ServicoTipo.class,
				qServicoTipo.codigo
			)
		).where(
			qServicoTipo.sigla.eq(sigla)
		).fetch();

		List<ServicoTipoTO> listaTO = new ArrayList<>();

		for (ServicoTipo servicoTipo : lista) {
			ServicoTipoTO to = getMapper().map(servicoTipo, ServicoTipoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public List<ServicoTipoTO> findByNomeAndSituacaoAndTipoFaturamento(String nome, String nomeSituacao, String nomeTipoFaturamento) {

		List<ServicoTipo> lista = select(
			Projections.fields(
				ServicoTipo.class,
				qServicoTipo.codigo,
				qServicoTipo.sigla,
				qServicoTipo.nome,
				qServicoTipo.situacao,
				qServicoTipo.tipoFaturamento,
				Projections.fields(
					ServicoTipo.class,
					qServicoTipo.codigo
				).as(qServicoTipo.servicoTipoPai)
			)
		).where(
			qServicoTipo.nome.containsIgnoreCase(nome).or(qServicoTipo.sigla.containsIgnoreCase(nome))
				.and(qServicoTipo.tipoFaturamento.eq(ServicoTipo.getTipoFaturamentoByNome(nomeTipoFaturamento)))
				.and(qServicoTipo.situacao.eq(ServicoTipo.getSituacaoByNome(nomeSituacao)))
		).orderBy(qServicoTipo.nome.asc()).fetch();

		List<ServicoTipoTO> listaTO = new ArrayList<>();

		for (ServicoTipo servicoTipo : lista) {
			ServicoTipoTO to = getMapper().map(servicoTipo, ServicoTipoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public ServicoTipoTO findByCodigo(Long codigo) {
		JPAQuery jpaQuery = select();

		QBean qBean = Projections.bean(
			ServicoTipo.class,
			qServicoTipo.codigo
		);

		jpaQuery
			.select(qBean)
			.from(qServicoTipo)
			.where(qServicoTipo.codigo.eq(codigo));

		ServicoTipo servicoTipo = (ServicoTipo) jpaQuery.fetchOne();

		if (servicoTipo != null) {
			return getMapper().map(servicoTipo, ServicoTipoTO.class);
		}

		return null;
	}
}
