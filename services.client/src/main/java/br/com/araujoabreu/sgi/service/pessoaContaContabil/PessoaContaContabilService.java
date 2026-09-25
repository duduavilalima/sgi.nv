package br.com.araujoabreu.sgi.service.pessoaContaContabil;

import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface PessoaContaContabilService extends CrudService<PessoaContaContabilTO> {
	void remover(Long id);
}
