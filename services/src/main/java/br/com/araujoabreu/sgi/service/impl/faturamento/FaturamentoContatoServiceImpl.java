package br.com.araujoabreu.sgi.service.impl.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoContato;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoContato;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoContatoService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoContatoTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.corporativo.pessoa.domain.QContato;
import br.com.fasolti.corporativo.pessoa.domain.QRelacionamentoContato;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.sgi.repository.contato.ContatoRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoContatoRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FaturamentoContatoServiceImpl extends BaseSearchService<FaturamentoContato, FaturamentoContatoTO, FaturamentoContatoPesquisaTO, FaturamentoContatoRepository> implements FaturamentoContatoService {

	private static final QFaturamentoContato query = QFaturamentoContato.faturamentoContato;

	@Autowired
	private ContatoRepository contatoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private SecurityService securityService;

	@Autowired
	public FaturamentoContatoServiceImpl(FaturamentoContatoRepository repository) {
		super(repository);
	}

	@Override
	public List<FaturamentoContatoTO> listaPorCodFaturamento(Long codFaturamento) {
		JPAQuery jpaQuery = select();

		QFaturamento qFaturamento = QFaturamento.faturamento;
		QContato qContato = QContato.contato;
		QRelacionamentoContato qRelacionamentoContato = QRelacionamentoContato.relacionamentoContato;

		QBean qBean = Projections.bean(
			FaturamentoContato.class,
			query.codigo,
			query.ordem,
			Projections.bean(Faturamento.class, qFaturamento.codigo).as("faturamento"),
			Projections.bean(Contato.class, qContato.codigo, qContato.nome, qContato.email,
				Projections.bean(RelacionamentoContato.class, qRelacionamentoContato.codigo, qRelacionamentoContato.nome).as("relacionamentoContato")
			).as("contato")
		);

		jpaQuery.select(qBean);

		jpaQuery.from(query)
			.innerJoin(query.faturamento, qFaturamento)
			.innerJoin(query.contato, qContato)
			.innerJoin(qContato.relacionamentoContato, qRelacionamentoContato);

		jpaQuery.where(qFaturamento.codigo.eq(codFaturamento));
		jpaQuery.orderBy(query.ordem.asc());

		List<FaturamentoContato> lista = jpaQuery.fetch();
		List<FaturamentoContatoTO> listaTO = new ArrayList<>();

		for (FaturamentoContato faturamentoContato : lista) {
			FaturamentoContatoTO to = getMapper().map(faturamentoContato, FaturamentoContatoTO.class);

			// Telefones para mostrar na tela de exibição
			List<Telefone> listTelefone = telefoneRepository.findByContatoCodigo(to.getContato().getCodigo());

			if (listTelefone != null && !listTelefone.isEmpty()) {
				to.getContato().setTelefone1(getMapper().map(listTelefone.get(0), TelefoneTO.class));

				if (listTelefone.size() > 1) {
					to.getContato().setTelefone2(getMapper().map(listTelefone.get(1), TelefoneTO.class));
				}
			}

			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	@Transactional
	protected void doBeforeSave(FaturamentoContato entity, FaturamentoContatoTO to) throws ServiceException {
		entity.getContato().setCriador(securityService.getUsername());
		entity.getContato().setDataCriacao(new Date());
		contatoRepository.save(entity.getContato());
	}

	@Override
	protected void doAfterSave(FaturamentoContato entity, FaturamentoContatoTO object) {
		//Salvar Telefones
		if (object.getContato().getTelefone1() != null) {
			object.getContato().getTelefone1().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
			object.getContato().getTelefone1().setPrincipal(Boolean.TRUE);
			object.getContato().getTelefone1().setCriador(securityService.getUsername());
			object.getContato().getTelefone1().setDataCriacao(new Date());
			telefoneRepository.save(getMapper().map(object.getContato().getTelefone1(), Telefone.class));
		}
		if (object.getContato().getTelefone2() != null) {
			object.getContato().getTelefone2().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
			if (object.getContato().getTelefone1() != null) {
				object.getContato().getTelefone2().setPrincipal(Boolean.FALSE);
			} else {
				object.getContato().getTelefone2().setPrincipal(Boolean.TRUE);
			}
			object.getContato().getTelefone2().setCriador(securityService.getUsername());
			object.getContato().getTelefone2().setDataCriacao(new Date());
			telefoneRepository.save(getMapper().map(object.getContato().getTelefone2(), Telefone.class));
		}
	}

	@Override
	@Transactional
	protected void doBeforeUpdate(FaturamentoContato entity, FaturamentoContatoTO to) {
		//Remover telefone do contato
		List<Telefone> removeTel = telefoneRepository.findByContatoCodigo(entity.getContato().getCodigo());
		for (Telefone tel : removeTel) {
			if ((entity.getContato().getTelefone1() != null && tel.getCodigo().longValue() == entity.getContato().getTelefone1().getCodigo().longValue())
				|| (entity.getContato().getTelefone2() != null && tel.getCodigo().longValue() == entity.getContato().getTelefone2().getCodigo().longValue())) {
				//OK
			} else {
				telefoneRepository.delete(tel);
			}
		}

		//Update dos telefones
		if (to.getContato().getTelefone1() != null) {
			if (to.getContato().getTelefone1().getCodigo() == null) {
				to.getContato().getTelefone1().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
				to.getContato().getTelefone1().setPrincipal(Boolean.TRUE);
				to.getContato().getTelefone1().setCriador(securityService.getUsername());
				to.getContato().getTelefone1().setDataCriacao(new Date());
				telefoneRepository.save(getMapper().map(to.getContato().getTelefone1(), Telefone.class));
			} else {
				to.getContato().getTelefone1().setAtualizador(securityService.getUsername());
				to.getContato().getTelefone1().setDataAtualizacao(new Date());
				telefoneRepository.save(getMapper().map(to.getContato().getTelefone1(), Telefone.class));
			}
		}

		if (to.getContato().getTelefone2() != null) {
			if (entity.getContato().getTelefone1() != null) {
				to.getContato().getTelefone2().setPrincipal(Boolean.FALSE);
			} else {
				to.getContato().getTelefone2().setPrincipal(Boolean.TRUE);
			}

			if (to.getContato().getTelefone2().getCodigo() == null) {
				to.getContato().getTelefone2().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
				to.getContato().getTelefone2().setPrincipal(Boolean.TRUE);
				to.getContato().getTelefone2().setCriador(securityService.getUsername());
				to.getContato().getTelefone2().setDataCriacao(new Date());
				telefoneRepository.save(getMapper().map(to.getContato().getTelefone2(), Telefone.class));
			} else {
				to.getContato().getTelefone2().setAtualizador(securityService.getUsername());
				to.getContato().getTelefone2().setDataAtualizacao(new Date());
				telefoneRepository.save(getMapper().map(to.getContato().getTelefone2(), Telefone.class));
			}
		}

		Contato contato = getMapper().map(to.getContato(), Contato.class);
		contato.setAtualizador(securityService.getUsername());
		contato.setDataAtualizacao(new Date());

		entity.setContato(contatoRepository.save(contato));
	}

	@Override
	@Transactional
	public void remover(Long id) throws Exception {
		try {
			FaturamentoContato faturamentoContato = getRepository().findById(id).get();

			//Lista Telefones
			List<Telefone> listaTelefones = telefoneRepository.findByContatoCodigo(faturamentoContato.getContato().getCodigo());
			if (listaTelefones != null && !listaTelefones.isEmpty()) {
				for (Telefone tel : listaTelefones) {
					telefoneRepository.delete(tel);
				}
			}

			getRepository().delete(faturamentoContato);
			contatoRepository.delete(faturamentoContato.getContato());
		} catch (Exception e) {
			throw new Exception(e);
		}

	}
}
