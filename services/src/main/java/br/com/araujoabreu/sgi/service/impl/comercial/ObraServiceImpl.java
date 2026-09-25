package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.administracao.domain.Obra;
import br.com.araujoabreu.sgi.administracao.domain.ObraCliente;
import br.com.araujoabreu.sgi.administracao.domain.QObra;
import br.com.araujoabreu.sgi.administracao.domain.QObraCliente;
import br.com.araujoabreu.sgi.administracao.domain.TipoObra;
import br.com.araujoabreu.sgi.enumerators.Setor;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.araujoabreu.sgi.service.administracao.transfer.CnaeTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.comercial.ObraService;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ObraClienteTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ObraPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ObraTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.TipoObraTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.sgi.repository.administracao.SituacaoRepository;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.comercial.ObraClienteRepository;
import br.com.fasolti.sgi.repository.comercial.ObraRepository;
import br.com.fasolti.sgi.repository.comercial.TipoObraRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Service
public class ObraServiceImpl extends BaseSearchService<Obra, ObraTO, ObraPesquisaTO, ObraRepository> implements ObraService {

	private static final QObra qObra = QObra.obra;

	@Autowired
	private TipoObraRepository tipoObraRepository;

	@Autowired
	private ObraClienteRepository obraClienteRepository;

	@Autowired
	private SituacaoRepository situacaoRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	public ObraServiceImpl(ObraRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Obra> jpaQuery, ObraPesquisaTO exampleObj) {
		QEndereco qEndereco = QEndereco.endereco1;
		QCidade qCidade = QCidade.cidade;
		QObraCliente qObraCliente = QObraCliente.obraCliente;

		jpaQuery.join(qObra.endereco, qEndereco)
			.leftJoin(qObra.obraClientes, qObraCliente)
			.leftJoin(qEndereco.cidade, qCidade)
			.distinct();
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder booleanBuilder, ObraPesquisaTO filtro) {
		if (Check.isAssigned(filtro.getNome())) {
			booleanBuilder.and(qObra.nome.toUpperCase().contains(filtro.getNome().toUpperCase()));
		}

		if (Check.isAssigned(filtro.getSituacao())) {
			for (Situacao situacao : Situacao.values()) {
				if (situacao.toString().equals(filtro.getSituacao())) {
					booleanBuilder.and(qObra.situacao.eq((situacao)));
					break;
				}
			}
		}

		if (Check.isAssigned(filtro.getSetor())) {
			for (Setor setor : Setor.values()) {
				if (setor.toString().equals(filtro.getSetor())) {
					booleanBuilder.and(qObra.setor.eq((setor)));
					break;
				}
			}
		}

		if (filtro.getCliente() != null && !filtro.getCliente().trim().isEmpty()) {
			booleanBuilder.and(QObraCliente.obraCliente.principal.eq(Boolean.TRUE));
			booleanBuilder.and(QObraCliente.obraCliente.pessoa.nome.toUpperCase().contains(filtro.getCliente().toUpperCase()));
		}

		if (Check.isAssigned(filtro.getCidade())) {
			booleanBuilder.and(QEndereco.endereco1.cidade.codigo.eq(filtro.getCidade().getCodigo()));
		}

		if (Check.isAssigned(filtro.getUf())) {
			booleanBuilder.and(QEndereco.endereco1.cidade.uf.toUpperCase().eq(filtro.getUf().toUpperCase()));

		}

		if (Check.isAssigned(filtro.getTipoObraTO())) {
			booleanBuilder.and(qObra.tipoObra.codigo.eq(filtro.getTipoObraTO().getCodigo()));
		}

		if (filtro.getInicioVigencia() != null) {
			booleanBuilder.and(qObra.inicioVigencia.after(filtro.getInicioVigencia()));
		}
		if (filtro.getFimVigencia() != null) {
			booleanBuilder.and(qObra.fimVigencia.before(filtro.getFimVigencia()));

		}
	}

	@Override
	protected void doAfterSearch(List<Obra> entities, List<ObraPesquisaTO> result) throws ServiceException {
		for (ObraPesquisaTO obraPesquisaTO : result) {
			List<ObraCliente> obraClientes = obraClienteRepository.findByObraCodigo(obraPesquisaTO.getCodigo());
			if (!obraClientes.isEmpty()) {
				for (ObraCliente obraCliente : obraClientes) {
					if (obraCliente.isPrincipal()) {
						obraPesquisaTO.setCliente(obraCliente.getPessoa().getNome());
					}
				}
			}
		}
	}
	@Transactional
	@Override
	protected void doBeforeUpdate(Obra entity, ObraTO to) throws ServiceException {
		ObraTO verificar = findByNome(to.getNome());
		if (!verificar.getCodigo().equals(to.getCodigo())) {
			throw new ValidationException("ValidationException",String.format("Obra com o nome %s já existe.", to.getNome()));
		}

		List<ObraCliente> obraClientes = obraClienteRepository.findByObraCodigo(to.getCodigo());
		for (ObraCliente obraCliente : obraClientes) {
			obraClienteRepository.delete(obraCliente);
		}

		entity.getObraClientes().clear();

		ObraCliente obraClientePrimario = new ObraCliente();
		obraClientePrimario.setPessoa(new Pessoa(to.getClientePrincipal().getPessoa().getCodigo()));
		obraClientePrimario.setObra(entity);
		obraClientePrimario.setPrincipal(Boolean.TRUE);
		obraClientePrimario.setPercFaturamento(to.getClientePrincipal().getPercFaturamento());
		entity.getObraClientes().add(obraClienteRepository.save(obraClientePrimario));

		if (to.getClientePrincipal().getPercFaturamento() < 100) {
			ObraCliente obraClienteSecundario = new ObraCliente();
			obraClienteSecundario.setPessoa(new Pessoa(to.getClienteSecundario().getPessoa().getCodigo()));
			obraClienteSecundario.setObra(entity);
			obraClienteSecundario.setPrincipal(Boolean.FALSE);
			obraClienteSecundario.setPercFaturamento(to.getClienteSecundario().getPercFaturamento());
			entity.getObraClientes().add(obraClienteRepository.save(obraClienteSecundario));
		}

		entity.getObraClientes();
	}

	@Override
	protected void doBeforeSave(Obra entity, ObraTO to) throws ServiceException {
		if (verificarSeNomeJaExiste(to)) {
			throw new ValidationException("ValidationException",String.format("Obra com o nome %s já existe.", to.getNome()));
		}

		entity.setObraClientes(new TreeSet<>());

		ObraCliente obraClientePrimario = new ObraCliente();
		obraClientePrimario.setPessoa(new Pessoa(to.getClientePrincipal().getPessoa().getCodigo()));
		obraClientePrimario.setObra(entity);
		obraClientePrimario.setPrincipal(Boolean.TRUE);
		obraClientePrimario.setPercFaturamento(to.getClientePrincipal().getPercFaturamento());
		entity.getObraClientes().add(obraClientePrimario);

		if (to.getClientePrincipal().getPercFaturamento() < 100) {
			ObraCliente obraClienteSecundario = new ObraCliente();
			obraClienteSecundario.setPessoa(new Pessoa(to.getClienteSecundario().getPessoa().getCodigo()));
			obraClienteSecundario.setObra(entity);
			obraClienteSecundario.setPrincipal(Boolean.FALSE);
			obraClienteSecundario.setPercFaturamento(to.getClienteSecundario().getPercFaturamento());
			entity.getObraClientes().add(obraClienteSecundario);
		}

		if (to.getEndereco().getCodigo() != null) {
			entity.setEndereco(enderecoRepository.findById(to.getEndereco().getCodigo()).orElse(null));
		} else {
			Endereco endereco = getMapper().map(to.getEndereco(), Endereco.class);
			entity.setEndereco(enderecoRepository.save(endereco));
		}
	}

	private boolean verificarSeNomeJaExiste(ObraTO object) {
		if (object == null || object.getNome() == null || object.getNome().trim().isEmpty()) {
			return Boolean.FALSE;
		} else {
			ObraTO obra = findByNome(object.getNome());
			if (obra != null && obra.getNome() != null) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public ObraTO findByNome(String nome) {
		JPAQuery<Obra> obraJPAQuery = select();
		obraJPAQuery.from(qObra);
		obraJPAQuery.select(Projections.fields(Obra.class, qObra.codigo, qObra.nome));
		return map(obraJPAQuery.where(new BooleanBuilder().and(qObra.nome.lower().eq(nome.toLowerCase()))).fetchOne());
	}

	@Override
	public List<TipoObraTO> buscarTipoObra() {
		try {
			Iterable<TipoObra> iterable = tipoObraRepository.findAll();
			List<TipoObra> listaTipoObra = Lists.newArrayList(iterable);
			listaTipoObra.sort((tipoObra1, tipoObra2) -> tipoObra1.getNome().compareTo(tipoObra2.getNome()));

			List<TipoObraTO> listaTipoObraTO = new ArrayList<>();

			for (TipoObra tipoObra : listaTipoObra) {

				TipoObraTO tipoObraTO = getMapper().map(tipoObra, TipoObraTO.class);
				listaTipoObraTO.add(tipoObraTO);

			}
			return listaTipoObraTO;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doAfterLoad(ObraTO obraTO) {
		List<ObraCliente> obraClientes = obraClienteRepository.findByObraCodigo(obraTO.getCodigo());

		if (!obraClientes.isEmpty()) {
			for (ObraCliente obraCliente : obraClientes) {
				ObraClienteTO obraClienteTO = getMapper().map(obraCliente, ObraClienteTO.class);

				obterCamposObraCliente(obraCliente, obraTO, obraClienteTO);
			}
		}
		obraTO.getClienteSecundario();
	}

	private void obterCamposObraCliente(ObraCliente obraCliente, ObraTO obraTO, ObraClienteTO cliente) {

		if (cliente.isPrincipal()) {
			obraTO.setClientePrincipal(cliente);
			obraTO.getClientePrincipal().setPessoa(getMapper().map(obraCliente.getPessoa(), PessoaJuridicaTO.class));
			if (obraCliente.getPessoa() instanceof PessoaJuridica && ((PessoaJuridica) obraCliente.getPessoa()).getCnae() != null) {
				cliente.getPessoa().setCnaeTO(getMapper().map(((PessoaJuridica) obraCliente.getPessoa()).getCnae(), CnaeTO.class));
			}
			if (obraCliente.getPessoa().getClassificacaoCliente() != null) {
				obraTO.getClientePrincipal().getPessoa().setClassificacaoClienteTO(getMapper().map(obraCliente.getPessoa().getClassificacaoCliente(), ClassificacaoClienteTO.class));
			}

		} else {
			obraTO.setClienteSecundario(cliente);
			obraTO.getClienteSecundario().setPessoa(getMapper().map(obraCliente.getPessoa(), PessoaJuridicaTO.class));
			if (obraCliente.getPessoa() instanceof PessoaJuridica && ((PessoaJuridica) obraCliente.getPessoa()).getCnae() != null) {
				cliente.getPessoa().setCnaeTO(getMapper().map(((PessoaJuridica) obraCliente.getPessoa()).getCnae(), CnaeTO.class));
			}
			if (obraCliente.getPessoa().getClassificacaoCliente() != null) {
				obraTO.getClienteSecundario().getPessoa().setClassificacaoClienteTO(getMapper().map(obraCliente.getPessoa().getClassificacaoCliente(), ClassificacaoClienteTO.class));
			}
		}
	}

}
