package br.com.araujoabreu.sgi.service.impl.pessoaContaContabil;

import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.PessoaContaContabilService;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.pessoaContaContabil.PessoaContaContabilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaContaContabilServiceImpl extends BaseSearchService<PessoaContaContabil, PessoaContaContabilTO, PessoaContaContabilPesquisaTO, PessoaContaContabilRepository> implements PessoaContaContabilService {

	@Autowired
	public PessoaContaContabilServiceImpl(PessoaContaContabilRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(PessoaContaContabil entity, PessoaContaContabilTO object) throws ServiceException {
		super.doBeforeSave(entity, object);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		PessoaContaContabil contaContabil = getRepository().findById(id).orElse(null);

		getRepository().delete(contaContabil);
	}
}
