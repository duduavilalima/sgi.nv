package br.com.araujoabreu.sgi.service.impl.contato;

import br.com.araujoabreu.sgi.administracao.domain.QContrato;
import br.com.araujoabreu.sgi.administracao.domain.QContratoContato;
import br.com.araujoabreu.sgi.service.contato.ContatoService;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoPesquisaTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.corporativo.pessoa.domain.QContato;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.contato.ContatoRepository;
import br.com.fasolti.sgi.repository.telefone.TelefoneRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ContatoServiceImpl extends BaseSearchService<Contato, ContatoTO, ContatoPesquisaTO, ContatoRepository> implements ContatoService {
	private static final QContato qContato = QContato.contato;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public ContatoServiceImpl(ContatoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Contato> jpaQuery, ContatoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			QContratoContato qContratoContato = QContratoContato.contratoContato;
			QContrato qContrato = QContrato.contrato;

			jpaQuery.join(qContato.contratoContatos, qContratoContato)
				.join(qContratoContato.contrato, qContrato);
		}

		if (Check.isAssigned(exampleObj.getCodigoCliente())) {
			if (jpaQuery == null) {
				jpaQuery = select().from(qContato);
			}
			QPessoa qPessoa = QPessoa.pessoa;
			jpaQuery.join(qContato.pessoa, qPessoa);
		}
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ContatoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(qContato.codigo.eq(exampleObj.getCodigo()));
		}

		if (Check.isAssigned(exampleObj.getNomeRelacionamentoContato())) {
			builder.and(qContato.relacionamentoContato.nome.contains(exampleObj.getNomeRelacionamentoContato()));
		}

		if (Check.isAssigned(exampleObj.getCodigoContrato())) {
			QContrato qContrato = QContrato.contrato;
			builder.and(qContrato.codigo.eq(exampleObj.getCodigoContrato()));
		}

		if (Check.isAssigned(exampleObj.getCodigoCliente())) {
			builder.and(qContato.pessoa.codigo.eq(exampleObj.getCodigoCliente()));
		}
	}

	@Override
	protected void doAfterSave(Contato entity, ContatoTO object) {
		//FIXME: Passar a responsabilidade de persistir o telefone para o TelefoneService.
		//Salvar Telefones
		if (object.getTelefone1() != null) {
			object.getTelefone1().setContato(getMapper().map(entity, ContatoTO.class));
			object.getTelefone1().setPrincipal(Boolean.TRUE);
			object.getTelefone1().setCriador(entity.getCriador());
			object.getTelefone1().setDataCriacao(entity.getDataCriacao());
			telefoneRepository.save(getMapper().map(object.getTelefone1(), Telefone.class));
		}
		if (object.getTelefone2() != null) {
			object.getTelefone2().setContato(getMapper().map(entity, ContatoTO.class));
			if (object.getTelefone1() != null) {
				object.getTelefone2().setPrincipal(Boolean.FALSE);
			} else {
				object.getTelefone2().setPrincipal(Boolean.TRUE);
			}
			object.getTelefone2().setCriador(entity.getCriador());
			object.getTelefone2().setDataCriacao(entity.getDataCriacao());
			telefoneRepository.save(getMapper().map(object.getTelefone2(), Telefone.class));
		}

	}

	@Transactional
	@Override
	protected void doBeforeUpdate(Contato entity, ContatoTO object) {
		gerenciarTelefones(entity, object);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		try {
			/*Lista Telefones*/
			List<Telefone> listaTelefones = telefoneRepository.findByContatoCodigo(id);
			if (listaTelefones != null && !listaTelefones.isEmpty()) {
				for (Telefone tel : listaTelefones) {
					telefoneRepository.delete(tel);
				}
			}
			Contato contato = getRepository().findById(id).orElse(null);
			getRepository().delete(contato);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	private void gerenciarTelefones(Contato entity, ContatoTO object) {

		List<Telefone> telefonesDoContato = telefoneRepository.findByContatoCodigo(entity.getCodigo());

		if (object.getTelefone1() == null && object.getTelefone2() == null) {
			telefonesDoContato.stream().forEach(telefone -> telefoneRepository.delete(telefone));
		} else {
			Map<Long, Telefone> mapaTelefones = new HashMap<>();
			Set<Long> codTelefonesAtualizados = new HashSet<>();
			Boolean escolhidoPrincipal = Boolean.FALSE;

			telefonesDoContato.stream().forEach(telefone -> mapaTelefones.put(telefone.getCodigo(), telefone));

			if (object.getTelefone1() != null) {
				TelefoneTO telefoneTO = object.getTelefone1();
				escolhidoPrincipal = Boolean.TRUE;

				saveUpdateTelefone(telefoneTO, mapaTelefones, entity, escolhidoPrincipal);

				if (telefoneTO.getCodigo() != null) {
					codTelefonesAtualizados.add(telefoneTO.getCodigo());
				}
			}

			if (object.getTelefone2() != null) {
				TelefoneTO telefoneTO = object.getTelefone2();

				saveUpdateTelefone(telefoneTO, mapaTelefones, entity, !escolhidoPrincipal);

				if (telefoneTO.getCodigo() != null) {
					codTelefonesAtualizados.add(telefoneTO.getCodigo());
				}
			}

			//deletar telefones ausentes
			telefonesDoContato.stream()
				.filter(telefone -> !codTelefonesAtualizados.contains(telefone.getCodigo()))
				.forEach(telefone -> telefoneRepository.delete(telefone));
		}
	}

	@Transactional
	private void saveUpdateTelefone(TelefoneTO telefoneTO, Map<Long, Telefone> mapaTelefones, Contato contato, Boolean isPrincipal) {
		Telefone telefone;

		if (telefoneTO.getCodigo() == null) {
			telefone = getMapper().map(telefoneTO, Telefone.class);
			telefone.setPrincipal(isPrincipal);
			telefone.setCriador(securityService.getUser().getLogin());
			telefone.setDataCriacao(new Date());
			telefone.setContato(contato);
			telefoneRepository.save(telefone);
		} else {
			telefone = mapaTelefones.get(telefoneTO.getCodigo());
			Telefone telefoneAtualizado = getMapper().map(telefoneTO, Telefone.class);

			if (!telefone.equals(telefoneAtualizado)) {
				telefone.setTipo(telefoneAtualizado.getTipo());
				telefone.setPrincipal(isPrincipal);
				telefone.setDdd(telefoneAtualizado.getDdd());
				telefone.setNumero(telefoneAtualizado.getNumero());
				telefone.setRamal(telefoneAtualizado.getRamal());

				if (telefoneAtualizado.getOperadora() != null && telefoneAtualizado.getOperadora().getCodigo() != null) {
					telefone.setOperadora(telefoneAtualizado.getOperadora());
				} else {
					telefone.setOperadora(null);
				}

				telefone.setAtualizador(securityService.getUser().getLogin());
				telefone.setDataAtualizacao(new Date());
				telefoneRepository.save(telefone);
			}
		}
	}
}
