package br.com.araujoabreu.sgi.service.impl.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoEndereco;
import br.com.araujoabreu.sgi.administracao.domain.QContratoEndereco;
import br.com.araujoabreu.sgi.service.contrato.ContratoEnderecoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoEnderecoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.sgi.repository.contrato.ContratoEnderecoRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ContratoEnderecoServiceImpl extends BaseSearchService<ContratoEndereco, ContratoEnderecoTO, ContratoEnderecoPesquisaTO, ContratoEnderecoRepository> implements ContratoEnderecoService {

	private static final QContratoEndereco qContratoEndereco = QContratoEndereco.contratoEndereco;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	public ContratoEnderecoServiceImpl(ContratoEnderecoRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(ContratoEndereco entity, ContratoEnderecoTO object) throws ServiceException {
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));
	}

	@Override
	protected void doBeforeUpdate(ContratoEndereco entity, ContratoEnderecoTO object) throws ServiceException {
		entity.setEndereco(enderecoRepository.save(getMapper().map(object.getEndereco(), Endereco.class)));
	}

	@Transactional
	@Override
	public void remover(@PathVariable Long id) throws Exception {
		try {
			ContratoEndereco contratoEndereco = getRepository().findById(id).orElse(null);

			getRepository().delete(contratoEndereco);

			enderecoRepository.deleteById(contratoEndereco.getEndereco().getCodigo());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<ContratoEndereco> getListaByEndereco(Long codEndereco) {
		QEndereco qEndereco = QEndereco.endereco1;

		QBean qBean = Projections.bean(
			ContratoEndereco.class,
			qContratoEndereco.codigo
		);

		JPAQuery<ContratoEndereco> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qContratoEndereco)
			.join(qContratoEndereco.endereco, qEndereco)
			.where(qEndereco.codigo.eq(codEndereco));

		return jpaQuery.fetch();
	}

}
