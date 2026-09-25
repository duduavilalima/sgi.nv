package br.com.araujoabreu.sgi.service.impl.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoContato;
import br.com.araujoabreu.sgi.administracao.domain.QContrato;
import br.com.araujoabreu.sgi.administracao.domain.QContratoContato;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.ContratoContatoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoContatoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.corporativo.pessoa.domain.QContato;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.contato.ContatoRepository;
import br.com.fasolti.sgi.repository.contato.RelacionamentoContatoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoContatoRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContratoContatoServiceImpl extends BaseSearchService<ContratoContato, ContratoContatoTO, ContratoContatoPesquisaTO, ContratoContatoRepository> implements ContratoContatoService {

	private static final QContratoContato qContratoContato = QContratoContato.contratoContato;

	@Autowired
	private ContatoRepository contatoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private RelacionamentoContatoRepository relacionamentoContatoRepository;

	@Autowired
	public ContratoContatoServiceImpl(ContratoContatoRepository repository) {
		super(repository);
	}

	@Override
	public List<RelacionamentoContatoTO> buscarRelacionamento() {

		List<RelacionamentoContato> relacionamento = (List<RelacionamentoContato>) relacionamentoContatoRepository.findAll();

		List<RelacionamentoContatoTO> listaTO = new ArrayList<>();

		listaTO.add(null);
		if (relacionamento != null && !relacionamento.isEmpty()) {
			for (RelacionamentoContato rela : relacionamento) {
				listaTO.add(getMapper().map(rela, RelacionamentoContatoTO.class));
			}
		}
		return listaTO;
	}

	@Override
	protected void doAfterSave(ContratoContato entity, ContratoContatoTO object) {
		try {
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
		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
	}

	@Override
	protected void doBeforeSave(ContratoContato entity, ContratoContatoTO object) throws ServiceException {
		try {
			entity.setContato(contatoRepository.save(getMapper().map(object.getContato(), Contato.class)));
		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
	}

	@Override
	protected void doBeforeUpdate(ContratoContato entity, ContratoContatoTO object) throws ServiceException {
		try {
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
			if (object.getContato().getTelefone1() != null) {
				if (object.getContato().getTelefone1().getCodigo() == null) {
					object.getContato().getTelefone1().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
					object.getContato().getTelefone1().setPrincipal(Boolean.TRUE);
					object.getContato().getTelefone1().setCriador(securityService.getUsername());
					object.getContato().getTelefone1().setDataCriacao(new Date());
					telefoneRepository.save(getMapper().map(object.getContato().getTelefone1(), Telefone.class));
				} else {
					object.getContato().getTelefone1().setAtualizador(securityService.getUsername());
					object.getContato().getTelefone1().setDataAtualizacao(new Date());
					telefoneRepository.save(getMapper().map(object.getContato().getTelefone1(), Telefone.class));
				}
			}

			if (object.getContato().getTelefone2() != null) {
				if (entity.getContato().getTelefone1() != null) {
					object.getContato().getTelefone2().setPrincipal(Boolean.FALSE);
				} else {
					object.getContato().getTelefone2().setPrincipal(Boolean.TRUE);
				}

				if (object.getContato().getTelefone2().getCodigo() == null) {
					object.getContato().getTelefone2().setContato(getMapper().map(entity.getContato(), ContatoTO.class));
					object.getContato().getTelefone2().setPrincipal(Boolean.TRUE);
					object.getContato().getTelefone2().setCriador(securityService.getUsername());
					object.getContato().getTelefone2().setDataCriacao(new Date());
					telefoneRepository.save(getMapper().map(object.getContato().getTelefone2(), Telefone.class));
				} else {
					object.getContato().getTelefone2().setAtualizador(securityService.getUsername());
					object.getContato().getTelefone2().setDataAtualizacao(new Date());
					telefoneRepository.save(getMapper().map(object.getContato().getTelefone2(), Telefone.class));
				}
			}

			entity.setContato(contatoRepository.save(getMapper().map(object.getContato(), Contato.class)));

		} catch (Exception e) {
			throw new ValidationException("ValidationException",e.getMessage());
		}
	}
	@Transactional
	@Override
	public void remover(@PathVariable Long id) throws Exception {
		try {

			ContratoContato contratoContato = getRepository().findById(id).orElse(null);

			//Lista Telefones
			List<Telefone> listaTelefones = telefoneRepository.findByContatoCodigo(contratoContato.getContato().getCodigo());
			if (listaTelefones != null && !listaTelefones.isEmpty()) {
				for (Telefone tel : listaTelefones) {
					telefoneRepository.delete(tel);
				}
			}

			getRepository().delete(contratoContato);
			contatoRepository.delete(contratoContato.getContato());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<ContratoContatoTO> listaContratoContatoByCodContrato(@PathVariable String id) throws Exception {
		try {
			List<ContratoContatoTO> listaTO = new ArrayList<>();
			List<ContratoContato> lista = buscarListaContratoContatoByCodContrato(Long.parseLong(id));

			if (lista != null && lista.size() > 0) {
				for (ContratoContato cc : lista) {
					ContratoContatoTO ccTO = getMapper().map(cc, ContratoContatoTO.class);
					listaTO.add(ccTO);
				}
			}

			return listaTO;
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	private List<ContratoContato> buscarListaContratoContatoByCodContrato(Long codContrato) {
		QContrato qContrato = QContrato.contrato;
		QContato qContato = QContato.contato;

		QBean qBean = Projections.bean(
			ContratoContato.class,
			qContratoContato.codigo,
			qContratoContato.ordem,
			Projections.bean(Contato.class, qContato.codigo, qContato.nome, qContato.email).as("contato")
		);

		JPAQuery<ContratoContato> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qContratoContato).join(qContratoContato.contato, qContato).join(qContratoContato.contrato, qContrato)
			.where(qContrato.codigo.eq(codContrato));

		return jpaQuery.fetch();
	}

}
