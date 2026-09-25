package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface PessoaService extends CrudService<PessoaTO> {

	List<PessoaPesquisaTO> getPessoas(String nome);

	PessoaTO obterPessoa(Long codigo);

	JPAQuery getJpaQuery();

	PessoaTO obterClienteSimpleTelaNotaFiscal(Long codigoCliente);

	PessoaTO obterClientePrincipalSimpleTelaNotaFiscal(Long codigoClientePrincipal);

	List<PessoaTO> buscarNomeFantasiaDocumento(PessoaPesquisaTO exampleObj);

	PessoaTO buscarClientePrincipalPorCodCliente(Long codigo);
}
